package oxide.cache;

public final class ChunkUpdateFlag {

  private static final ScopedValue<Void> CHUNK_DELTA_UPDATING = ScopedValue.newInstance();

  public static boolean isChunkDeltaUpdating() {
    return CHUNK_DELTA_UPDATING.isBound();
  }

  public static void withChunkDeltaUpdating(final Runnable action) {
    ScopedValue.where(CHUNK_DELTA_UPDATING, null).run(action);
  }
}
