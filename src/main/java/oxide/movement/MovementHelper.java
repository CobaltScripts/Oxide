package oxide.movement;

import oxide.util.helper.Context;

public final class MovementHelper {

  public static boolean canWalkOn(final Context ctx, final int x, final int y, final int z) {
    return ctx.get(x, y - 1, z).standable() &&
      canWalkThrough(ctx, x, y, z);
  }

  public static boolean canWalkThrough(final Context ctx, final int x, final int y, final int z) {
    return ctx.get(x, y, z).passable() &&
      ctx.get(x, y + 1, z).passable();
  }

}
