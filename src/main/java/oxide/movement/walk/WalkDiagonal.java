package oxide.movement.walk;

import oxide.movement.Movement;
import oxide.movement.MovementHelper;
import oxide.movement.MovementResult;
import oxide.util.helper.Context;

public class WalkDiagonal extends Movement {

  public static final WalkDiagonal NORTH_EAST = new WalkDiagonal(1, -1);
  public static final WalkDiagonal NORTH_WEST = new WalkDiagonal(-1, -1);
  public static final WalkDiagonal SOUTH_EAST = new WalkDiagonal(1, 1);
  public static final WalkDiagonal SOUTH_WEST = new WalkDiagonal(-1, 1);
  private static final double SQRT_2 = Math.sqrt(2.0);

  private WalkDiagonal(int dx, int dz) {
    super(dx, dz);
  }

  @Override
  public void calculateCost(final Context ctx, final int startX, final int startY, final int startZ, final MovementResult result) {
    final int x = startX + dx;
    final int z = startZ + dz;

    if (!MovementHelper.canWalkOn(ctx, x, startY, z))
      return;
    if (!MovementHelper.canWalkThrough(ctx, startX + dx, startY, startZ))
      return;
    if (!MovementHelper.canWalkThrough(ctx, startX, startY, startZ + dz))
      return;

    result.set(x, startY, z);
    result.cost(ctx.costs().WALK_ONE_BLOCK_COST * SQRT_2 + ctx.get(x, startY, z).penalty());
  }

}
