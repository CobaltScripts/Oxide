package oxide.calculate;

import java.util.List;

import lombok.Getter;
import lombok.experimental.Accessors;
import oxide.movement.MovementHelper;
import oxide.util.helper.Context;

@Getter
@Accessors(fluent = true)
public class Path {

  private final Context ctx;
  private final List<PathNode> nodes;
  private final long timeElapsed;
  private final int nodesExplored;

  public Path(Context ctx, List<PathNode> nodes, long timeElapsed, int nodesExplored) {
    this.ctx = ctx;
    this.nodes = MovementHelper.smoothPath(ctx, nodes);
    this.timeElapsed = timeElapsed;
    this.nodesExplored = nodesExplored;
  }

  public boolean found() {
    return !nodes.isEmpty();
  }

  public int length() {
    return nodes.size();
  }
}
