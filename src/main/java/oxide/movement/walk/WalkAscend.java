package oxide.movement.walk;

import oxide.movement.Movement;
import oxide.movement.MovementHelper;
import oxide.movement.MovementResult;
import oxide.util.helper.Context;

public class WalkAscend extends Movement {

  public static final WalkAscend NORTH = new WalkAscend(0, -1);
  public static final WalkAscend EAST = new WalkAscend(1, 0);
  public static final WalkAscend SOUTH = new WalkAscend(0, 1);
  public static final WalkAscend WEST = new WalkAscend(-1, 0);

  private WalkAscend(int dx, int dz) {
    super(dx, dz);
  }

  @Override
  public void calculateCost(final Context ctx, final int startX, final int startY, final int startZ, final MovementResult result) {
    final int x = startX + dx;
    final int y = startY + 1;
    final int z = startZ + dz;

    if (!MovementHelper.canWalkOn(ctx, x, y, z)) {
      return;
    }

    if (!MovementHelper.canWalkThrough(ctx, x, y - 1, z)) {
      return;
    }

    result.set(x, y, z);
    result.cost(ctx.costs().JUMP_ONE_BLOCK_COST + ctx.get(x, y, z).penalty());
  }

}
