package oxide.movement.walk;

import oxide.movement.Movement;
import oxide.movement.MovementHelper;
import oxide.movement.MovementResult;
import oxide.util.helper.Context;

public class WalkDescend extends Movement {

  public static final WalkDescend NORTH = new WalkDescend(0, -1);
  public static final WalkDescend EAST = new WalkDescend(1, 0);
  public static final WalkDescend SOUTH = new WalkDescend(0, 1);
  public static final WalkDescend WEST = new WalkDescend(-1, 0);

  private WalkDescend(int dx, int dz) {
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

    if (!MovementHelper.canWalkThrough(ctx, x, startY, z)) {
      return;
    }

    final int y = startY - 1;

    if (!MovementHelper.canWalkOn(ctx, x, y, z)) {
      return;
    }

    result.set(x, y, z);
    result.cost(ctx.costs().FALL_N_BLOCKS_COST[1] + ctx.get(x, y, z).penalty());
  }
}
