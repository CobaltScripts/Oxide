package oxide.calculate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import oxide.cost.ActionCosts;
import oxide.goal.Goal;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PathNode {

  @EqualsAndHashCode.Include
  private final int x, y, z;

  private double costSoFar;
  private double costToEnd;
  private double totalCost;
  private PathNode parent;

  private int turns = Integer.MAX_VALUE;
  private int heapPosition = -1;

  public PathNode(final int x, final int y, final int z, final Goal goal) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.costSoFar = ActionCosts.COST_INF;
    this.costToEnd = goal.heuristic(x, y, z);
  }
}
