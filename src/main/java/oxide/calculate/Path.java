package oxide.calculate;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public class Path {

  private final List<PathNode> nodes;
  private final long timeElapsed;
  private final int nodesExplored;

  public boolean found() {
    return !nodes.isEmpty();
  }

  public int length() {
    return nodes.size();
  }

}
