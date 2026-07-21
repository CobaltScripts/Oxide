package oxide.goal;

import lombok.RequiredArgsConstructor;
import oxide.util.helper.Context;

@RequiredArgsConstructor
public class GoalBlock implements Goal {

  private static final double SQRT_2 = Math.sqrt(2);

  private final Context ctx;
  private final int x, y, z;

  @Override
  public double heuristic(final int x, final int y, final int z) {
    final int xDiff = x - this.x;
    final int yDiff = y - this.y;
    final int zDiff = z - this.z;

    double h = 0;

    if (yDiff > 0) {
      h += ctx.costs().FALL_N_BLOCKS_COST[2] / 2 * yDiff;
    } else if (yDiff < 0) {
      h += -yDiff * ctx.costs().JUMP_ONE_BLOCK_COST;
    }

    final double ax = Math.abs(xDiff);
    final double az = Math.abs(zDiff);
    final double straight;
    final double diagonal;

    if (ax < az) {
      straight = az - ax;
      diagonal = ax;
    } else {
      straight = ax - az;
      diagonal = az;
    }

    h += (diagonal * SQRT_2 + straight) * 4.64;
    return h;
  }

  @Override
  public boolean isAtGoal(final int x, final int y, final int z) {
    return x == this.x && y == this.y && z == this.z;
  }
}
