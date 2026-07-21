package oxide.cache;

import oxide.util.helper.BlockInfo;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class ChunkRegion {

  private final Long2ObjectOpenHashMap<BlockInfo> blocks = new Long2ObjectOpenHashMap<>();
  private final int x, z;

  public ChunkRegion(final int x, final int z) {
    this.x = x;
    this.z = z;
  }

  public BlockInfo get(final int wx, final int wy, final int wz) {
    return blocks.get(pack(wx, wy, wz));
  }

  public void put(final BlockInfo info) {
    blocks.put(pack(info.x(), info.y(), info.z()), info);
  }

  public void remove(final int wx, final int wy, final int wz) {
    blocks.remove(pack(wx, wy, wz));
  }

  public boolean isEmpty() {
    return blocks.isEmpty();
  }

  public static long pack(final int x, final int y, final int z) {
    return ((long) x & 0x3FFFFFFL) << 38 | ((long) y & 0xFFFL) << 26 | ((long) z & 0x3FFFFFFL);
  }

}
