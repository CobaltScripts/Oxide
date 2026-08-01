package oxide.render;

import java.awt.Color;
import java.util.List;

import net.minecraft.world.phys.Vec3;
import oxide.calculate.Path;
import oxide.calculate.PathNode;
import oxide.util.GizmosUtils;

public final class PathRenderer {

  private static Path currentPath;

  public static void setPath(final Path path) {
    currentPath = path;
  }

  public static void render() {
    if (currentPath == null || !currentPath.found()) {
      return;
    }

    final List<PathNode> nodes = currentPath.nodes();

    for (int i = 1; i < nodes.size(); i++) {
      final PathNode from = nodes.get(i - 1);
      final PathNode to = nodes.get(i);

      GizmosUtils.drawLine(
        new Vec3(from.getX() + 0.5, from.getY() + 0.5, from.getZ() + 0.5),
        new Vec3(to.getX() + 0.5, to.getY() + 0.5, to.getZ() + 0.5),
        Color.ORANGE,
        true,
        2.0f
      );
    }
  }
}
