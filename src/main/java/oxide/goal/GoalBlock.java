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
    final int dx = Math.abs(this.x - x);
    final int dy = this.y - y;
    final int dz = Math.abs(this.z - z);

    final double straight = Math.abs(dx - dz);
    final double diagonal = Math.min(dx, dz);

    double h = (straight + diagonal * SQRT_2) * ctx.costs().SPRINT_ONE_BLOCK_COST;

    if (dy > 0) {
      h += dy * 6.234399666206506; // ctx.costs().JUMP_ONE_BLOCK_COST;
    } else if (dy < 0) {
      h += -dy * (ctx.costs().FALL_N_BLOCKS_COST[2] / 2.0);
    }

    return h;
  }

  @Override
  public boolean isAtGoal(final int x, final int y, final int z) {
    return x == this.x && y == this.y && z == this.z;
  }
}
