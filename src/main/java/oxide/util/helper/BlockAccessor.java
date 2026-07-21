package oxide.util.helper;

import lombok.RequiredArgsConstructor;
import oxide.cache.ChunkManager;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;

@RequiredArgsConstructor
public class BlockAccessor {

  private final ClientLevel level;
  private final BlockState air = Blocks.AIR.defaultBlockState();

  private LevelChunk prevChunk;

  public BlockInfo get(final int x, final int y, final int z) {
    BlockInfo info = ChunkManager.get(x, y, z);

    if (info != null) {
      return info;
    }

    info = new BlockInfo(x, y, z, getState(x, y, z));
    ChunkManager.queueUpdate(info);
    return info;
  }

  private BlockState getState(final int x, final int y, final int z) {
    final int minY = level.dimensionType().minY();
    final int height = level.dimensionType().height();
    final int y0 = y - minY;

    if (y0 < 0 || y0 >= height) {
      return air;
    }

    final int cx = x >> 4;
    final int cz = z >> 4;

    if (prevChunk != null && prevChunk.getPos().x() == cx && prevChunk.getPos().z() == cz) {
      return fromChunk(prevChunk, x, y0, z);
    }

    final LevelChunk chunk = level.getChunkSource().getChunk(cx, cz, ChunkStatus.FULL, false);

    if (chunk == null || chunk.isEmpty()) {
      return air;
    }

    prevChunk = chunk;
    return fromChunk(chunk, x, y0, z);
  }

  private BlockState fromChunk(final LevelChunk chunk, final int x, final int y0, final int z) {
    final var sections = chunk.getSections();
    final int sectionIndex = y0 >> 4;

    if (sectionIndex < 0 || sectionIndex >= sections.length) {
      return air;
    }

    final var section = sections[sectionIndex];

    if (section == null || section.hasOnlyAir()) {
      return air;
    }

    return section.getBlockState(x & 15, y0 & 15, z & 15);
  }

}
