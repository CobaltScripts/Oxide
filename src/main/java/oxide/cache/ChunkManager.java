package oxide.cache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import oxide.util.helper.BlockInfo;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;

public final class ChunkManager {

  private ChunkManager() {}

  private static final Minecraft minecraft = Minecraft.getInstance();

  private static final Long2ObjectOpenHashMap<ChunkRegion> chunkCache = new Long2ObjectOpenHashMap<>();
  private static final LongArrayFIFOQueue pendingChunks = new LongArrayFIFOQueue();
  private static final LongOpenHashSet pendingSet = new LongOpenHashSet();
  private static final ExecutorService executor = Executors.newFixedThreadPool(Math.max(2, Runtime.getRuntime().availableProcessors() - 1));

  private static final int MAX_PENDING = 64;

  public static void queueChunk(final int x, final int z) {
    final long key = packChunk(x, z);

    synchronized (chunkCache) {
      if (chunkCache.containsKey(key)) return;
    }

    synchronized (pendingSet) {
      if (pendingSet.size() < MAX_PENDING && pendingSet.add(key)) {
        pendingChunks.enqueue(key);
      }
    }
  }

  public static void queueUpdate(final BlockInfo info) {
    final int cx = info.x() >> 4;
    final int cz = info.z() >> 4;
    final long chunkKey = packChunk(cx, cz);

    synchronized (chunkCache) {
      final ChunkRegion region = chunkCache.get(chunkKey);

      if (region == null) {
        return;
      }

      if (info.passable()) {
        region.remove(info.x(), info.y(), info.z());

        if (region.isEmpty()) {
          chunkCache.remove(chunkKey);
        }
      } else {
        region.put(info);
      }
    }
  }

  public static void clearCache() {
    synchronized (chunkCache) {
      chunkCache.clear();
    }
  }

  public static void onTick() {
    final ClientLevel level = minecraft.level;

    if (level == null) {
      return;
    }

    final long packedChunk;

    synchronized (pendingSet) {
      if (pendingChunks.isEmpty()) {
        return;
      }

      packedChunk = pendingChunks.dequeueLong();
      pendingSet.remove(packedChunk);
    }

    final int cx = (int) (packedChunk >> 32);
    final int cz = (int) packedChunk;

    executor.submit(() -> processChunk(level, cx, cz));
  }

  private static void processChunk(final ClientLevel level, final int cx, final int cz) {
    final long key = packChunk(cx, cz);

    synchronized (chunkCache) {
      if (chunkCache.containsKey(key)) return;
    }

    final LevelChunk chunk = level.getChunkSource().getChunk(cx, cz, ChunkStatus.FULL, false);

    if (chunk == null || chunk.isEmpty()) {
      return;
    }

    final ChunkRegion region = new ChunkRegion(cx, cz);
    final var sections = chunk.getSections();
    final int minSection = level.getMinSectionY();

    for (int i = 0; i < sections.length; i++) {
      final var section = sections[i];

      if (section == null || section.hasOnlyAir()) {
        continue;
      }

      final int sectionY = (minSection + i) << 4;

      for (int x = 0; x < 16; x++) {
        for (int y = 0; y < 16; y++) {
          for (int z = 0; z < 16; z++) {
            final BlockState state = section.getBlockState(x, y, z);

            if (state.isAir()) {
              continue;
            }

            final int wx = (cx << 4) + x;
            final int wy = sectionY + y;
            final int wz = (cz << 4) + z;

            region.put(new BlockInfo(wx, wy, wz, state));
          }
        }
      }
    }

    synchronized (chunkCache) {
      chunkCache.put(key, region);
    }
  }

  public static BlockInfo get(final int x, final int y, final int z) {
    final int cx = x >> 4;
    final int cz = z >> 4;
    final long chunkKey = packChunk(cx, cz);

    synchronized (chunkCache) {
      final ChunkRegion region = chunkCache.get(chunkKey);
      return region != null ? region.get(x, y, z) : null;
    }
  }

  private static long packChunk(final int x, final int z) {
    return ((long) x << 32) | (z & 0xFFFFFFFFL);
  }

}
