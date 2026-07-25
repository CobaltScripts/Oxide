package oxide.command.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import oxide.calculate.Path;
import oxide.calculate.Pathfinder;
import oxide.command.Command;
import oxide.goal.GoalBlock;
import oxide.movement.MovementType;
import oxide.render.PathRenderer;
import oxide.util.ChatUtils;
import oxide.util.helper.Context;

public final class GoToCommand extends Command {

  public GoToCommand() {
    super(
      "goto",
      "Pathfinds to a block position.",
      "-goto <x> <y> <z>"
    );
  }

  @Override
  public void execute(String[] args) {
    if (args.length != 3) {
      ChatUtils.send("Usage: " + getUsage());
      return;
    }

    final int x;
    final int y;
    final int z;

    try {
      x = Integer.parseInt(args[0]);
      y = Integer.parseInt(args[1]);
      z = Integer.parseInt(args[2]);
    } catch (NumberFormatException e) {
      ChatUtils.send("Coordinates must be integers.");
      return;
    }

    final LocalPlayer player = Minecraft.getInstance().player;

    if (player == null) {
      return;
    }

    final BlockPos start = player.blockPosition();

    ChatUtils.send("Pathfinding from (%d, %d, %d) to (%d, %d, %d)...".formatted(start.getX(), start.getY(), start.getZ(), x, y, z));

    final Context ctx = new Context();
    final GoalBlock goal = new GoalBlock(ctx, x, y, z);
    final Pathfinder finder = new Pathfinder(
      start.getX(), start.getY(), start.getZ(),
      goal, ctx, MovementType.WALK
    );

    final Path path = finder.findPath();
    final long nsPerNode = path.timeElapsed() / Math.max(1, path.nodesExplored());

    if (path.found()) {
      ChatUtils.send("Found path: %d nodes (%dms, %d explored, %d ns/node)".formatted(path.length(), path.timeElapsed() / 1_000_000L, path.nodesExplored(), nsPerNode));
    } else {
      ChatUtils.send("No path found (%dms, %d explored, %d ns/node)".formatted(path.timeElapsed() / 1_000_000L, path.nodesExplored(), nsPerNode));
    }

    PathRenderer.setPath(path);
  }
}
