package oxide.movement;

import java.util.ArrayList;
import java.util.List;

import oxide.calculate.PathNode;
import oxide.util.helper.Context;

public final class MovementHelper {

  public static boolean canWalkOn(final Context ctx, final int x, final int y, final int z) {
    return ctx.get(x, y - 1, z).standable() && canWalkThrough(ctx, x, y, z);
  }

  public static boolean canWalkThrough(final Context ctx, final int x, final int y, final int z) {
    return ctx.get(x, y, z).passable() && ctx.get(x, y + 1, z).passable();
  }

  public static boolean canWalkDirectly(final Context ctx, final PathNode from, final PathNode to) {
    if (from.getY() != to.getY()) {
      return false;
    }

    int x = from.getX();
    int z = from.getZ();

    final int targetX = to.getX();
    final int targetZ = to.getZ();
    final int distanceX = Math.abs(targetX - x);
    final int distanceZ = Math.abs(targetZ - z);
    final int stepX = Integer.compare(targetX, x);
    final int stepZ = Integer.compare(targetZ, z);

    int error = distanceX - distanceZ;

    while (x != targetX || z != targetZ) {
      final int previousX = x;
      final int previousZ = z;
      final int doubleError = error * 2;

      if (doubleError > -distanceZ) {
        error -= distanceZ;
        x += stepX;
      }

      if (doubleError < distanceX) {
        error += distanceX;
        z += stepZ;
      }

      final int moveX = x - previousX;
      final int moveZ = z - previousZ;

      if (!canWalkOn(ctx, x, from.getY(), z)) {
        return false;
      }

      if (
        moveX != 0 &&
        moveZ != 0 &&
        (!canWalkThrough(ctx, previousX + moveX, from.getY(), previousZ) ||
          !canWalkThrough(ctx, previousX, from.getY(), previousZ + moveZ))
      ) {
        return false;
      }
    }

    return true;
  }

  public static List<PathNode> smoothPath(Context ctx, List<PathNode> nodes) {
    if (nodes.size() < 3) {
      return nodes;
    }

    final List<PathNode> smoothed = new ArrayList<>();
    int anchor = 0;
    smoothed.add(nodes.get(anchor));

    while (anchor < nodes.size() - 1) {
      int next = nodes.size() - 1;

      while (next > anchor + 1 && !MovementHelper.canWalkDirectly(ctx, nodes.get(anchor), nodes.get(next))) {
        next--;
      }

      smoothed.add(nodes.get(next));
      anchor = next;
    }

    return smoothed;
  }
}
