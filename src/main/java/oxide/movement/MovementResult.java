package oxide.movement;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import oxide.cost.ActionCosts;

@Getter
@Accessors(fluent = true)
public class MovementResult {

  private int x, y, z;

  @Setter
  private double cost = ActionCosts.COST_INF;

  public void set(final int x, final int y, final int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public void reset() {
    cost = ActionCosts.COST_INF;
  }
}
