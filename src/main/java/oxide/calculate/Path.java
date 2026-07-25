package oxide.calculate;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Accessors(fluent = true)
public record Path(List<PathNode> nodes, long timeElapsed, int nodesExplored) {

  public boolean found() {
    return !nodes.isEmpty();
  }

  public int length() {
    return nodes.size();
  }

}
