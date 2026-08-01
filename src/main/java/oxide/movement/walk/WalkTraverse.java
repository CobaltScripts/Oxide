package oxide.movement.walk;

import oxide.movement.Movement;
import oxide.movement.MovementHelper;
import oxide.movement.MovementResult;
import oxide.util.helper.Context;

public class WalkTraverse extends Movement {

  public static final WalkTraverse NORTH = new WalkTraverse(0, -1);
  public static final WalkTraverse EAST = new WalkTraverse(1, 0);
  public static final WalkTraverse SOUTH = new WalkTraverse(0, 1);
  public static final WalkTraverse WEST = new WalkTraverse(-1, 0);

  private WalkTraverse(int dx, int dz) {
    super(dx, dz);
  }

  @Override
  public void calculateCost(
    final Context ctx,
    final int startX,
    final int startY,
    final int startZ,
    final MovementResult result
  ) {
    final int x = startX + dx;
    final int z = startZ + dz;

    if (!MovementHelper.canWalkOn(ctx, x, startY, z)) {
      return;
    }

    result.set(x, startY, z);
    result.cost(ctx.costs().WALK_ONE_BLOCK_COST + ctx.get(x, startY, z).penalty());
  }
}
