package oxide.cost;

import lombok.RequiredArgsConstructor;
import oxide.util.helper.Context;

@RequiredArgsConstructor
public class ActionCosts {

  public static final double COST_INF = 1000000;

  public final double WALK_ONE_BLOCK_COST = 20 / 4.317;
  public final double WALK_ONE_IN_WATER_COST = 20 / 2.2;
  public final double WALK_ONE_OVER_SOUL_SAND_COST = WALK_ONE_BLOCK_COST * 2;
  public final double LADDER_UP_ONE_COST = 20 / 2.35;
  public final double LADDER_DOWN_ONE_COST = 20 / 3.0;
  public final double SNEAK_ONE_BLOCK_COST = 20 / 1.3;
  public final double SPRINT_ONE_BLOCK_COST = 20 / 5.612;
  public final double SPRINT_MULTIPLIER = SPRINT_ONE_BLOCK_COST / WALK_ONE_BLOCK_COST;
  public final double WALK_OFF_BLOCK_COST = WALK_ONE_BLOCK_COST * 0.8;
  public final double CENTER_AFTER_FALL_COST = WALK_ONE_BLOCK_COST - WALK_OFF_BLOCK_COST;
  public final double FALL_1_25_BLOCKS_COST = distanceToTicks(1.25);
  public final double FALL_0_25_BLOCKS_COST = distanceToTicks(0.25);
  public final double JUMP_ONE_BLOCK_COST = FALL_1_25_BLOCKS_COST - FALL_0_25_BLOCKS_COST;
  public final double[] FALL_N_BLOCKS_COST = generateFallNBlocksCost();

  private final Context ctx;

  private static double[] generateFallNBlocksCost() {
    double[] costs = new double[4097];
    for (int i = 0; i < 4097; i++) {
      costs[i] = distanceToTicks(i);
    }
    return costs;
  }

  private static double velocity(int ticks) {
    return (Math.pow(0.98, ticks) - 1) * -3.92;
  }

  private static double distanceToTicks(double distance) {
    if (distance == 0) {
      return 0;
    }
    double tmpDistance = distance;
    int tickCount = 0;
    while (true) {
      double fallDistance = velocity(tickCount);
      if (tmpDistance <= fallDistance) {
        return tickCount + tmpDistance / fallDistance;
      }
      tmpDistance -= fallDistance;
      tickCount++;
    }
  }
}
