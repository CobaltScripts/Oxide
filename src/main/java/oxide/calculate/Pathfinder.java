package oxide.calculate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import lombok.AllArgsConstructor;
import oxide.cache.ChunkRegion;
import oxide.calculate.openset.HeapOpenSet;
import oxide.goal.Goal;
import oxide.movement.Movement;
import oxide.movement.MovementResult;
import oxide.movement.MovementType;
import oxide.util.helper.Context;

@AllArgsConstructor
public class Pathfinder {

  private static final double MIN_COST_IMPROVEMENT = 0.01;

  private final int startX, startY, startZ;
  private final Goal goal;
  private final Context ctx;
  private final MovementType movementType;

  private final Long2ObjectOpenHashMap<PathNode> closedSet = new Long2ObjectOpenHashMap<>();

  private static List<PathNode> reconstruct(PathNode node) {
    final List<PathNode> path = new ArrayList<>();

    while (node != null) {
      path.add(node);
      node = node.getParent();
    }

    Collections.reverse(path);
    return path;
  }

  public Path findPath() {
    final MovementResult result = new MovementResult();
    final HeapOpenSet openSet = new HeapOpenSet();

    final PathNode start = createStartNode();
    openSet.add(start);

    int nodesExplored = 0;
    final long startTime = System.nanoTime();

    while (!openSet.isEmpty()) {
      final PathNode current = openSet.poll();
      nodesExplored++;

      if (goal.isAtGoal(current.getX(), current.getY(), current.getZ())) {
        final long elapsed = System.nanoTime() - startTime;
        return new Path(ctx, reconstruct(current), elapsed, nodesExplored);
      }

      for (final Movement movement : movementType.getMovements()) {
        result.reset();
        movement.calculateCost(ctx, current.getX(), current.getY(), current.getZ(), result);

        final double moveCost = result.cost();

        if (Double.isInfinite(moveCost)) {
          continue;
        }

        final int nx = result.x();
        final int ny = result.y();
        final int nz = result.z();

        final PathNode neighbor = getNode(nx, ny, nz);
        final double newCost = current.getCostSoFar() + moveCost;
        final int newTurns = turns(current, nx, nz);

        final boolean betterCost = neighbor.getCostSoFar() - newCost > MIN_COST_IMPROVEMENT;
        final boolean sameCostBetterTurns = neighbor.getCostSoFar() == newCost && newTurns < neighbor.getTurns();

        if (betterCost || sameCostBetterTurns) {
          neighbor.setParent(current);
          neighbor.setCostSoFar(newCost);
          neighbor.setTotalCost(newCost + neighbor.getCostToEnd());
          neighbor.setTurns(newTurns);

          if (neighbor.getHeapPosition() == -1) {
            openSet.add(neighbor);
          } else {
            openSet.update(neighbor);
          }
        }
      }
    }

    final long elapsed = System.nanoTime() - startTime;
    return new Path(ctx, Collections.emptyList(), elapsed, nodesExplored);
  }

  private PathNode createStartNode() {
    PathNode node = new PathNode(startX, startY, startZ, goal);

    node.setCostSoFar(0);
    node.setTotalCost(node.getCostToEnd());
    node.setTurns(0);

    return node;
  }

  private static int turns(final PathNode current, final int x, final int z) {
    final PathNode parent = current.getParent();

    if (parent == null) {
      return 0;
    }

    final int previousX = current.getX() - parent.getX();
    final int previousZ = current.getZ() - parent.getZ();
    final int nextX = x - current.getX();
    final int nextZ = z - current.getZ();

    return current.getTurns() + (previousX == nextX && previousZ == nextZ ? 0 : 1);
  }

  private PathNode getNode(final int x, final int y, final int z) {
    final long key = ChunkRegion.pack(x, y, z);
    PathNode node = this.closedSet.get(key);

    if (node == null) {
      node = new PathNode(x, y, z, goal);
      this.closedSet.put(key, node);
    }

    return node;
  }
}
