package oxide.command;

import oxide.calculate.Path;
import oxide.calculate.Pathfinder;
import oxide.goal.GoalBlock;
import oxide.movement.MovementType;
import oxide.render.PathRenderer;
import oxide.util.ChatUtils;
import oxide.util.helper.Context;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;

public final class MainCommand {

  public static void dispatch(final CommandDispatcher<FabricClientCommandSource> dispatcher) {
    dispatcher.register(
      ClientCommands.literal("oxide")
          .then(ClientCommands.literal("pathfind")
            .then(ClientCommands.argument("x", IntegerArgumentType.integer())
              .then(ClientCommands.argument("y", IntegerArgumentType.integer())
                .then(ClientCommands.argument("z", IntegerArgumentType.integer())
                  .executes(MainCommand::executeCommand)
                )
              )
            )
          )
    );
  }

  private static int executeCommand(final CommandContext<FabricClientCommandSource> context) {
    final int x = IntegerArgumentType.getInteger(context, "x");
    final int y = IntegerArgumentType.getInteger(context, "y");
    final int z = IntegerArgumentType.getInteger(context, "z");

    final LocalPlayer player = Minecraft.getInstance().player;

    if (player == null) {
      return 0;
    }

    final BlockPos start = player.blockPosition();
    ChatUtils.send("Pathfinding from (%d, %d, %d) to (%d, %d, %d)...".formatted(start.getX(), start.getY(), start.getZ(), x, y, z));

    final Context ctx = new Context();
    final GoalBlock goal = new GoalBlock(ctx, x, y, z);
    final Pathfinder finder = new Pathfinder(start.getX(), start.getY(), start.getZ(), goal, ctx, MovementType.WALK);
    final Path path = finder.findPath();

    final long nsPerNode = path.timeElapsed() / Math.max(1, path.nodesExplored());

    if (path.found()) {
      ChatUtils.send("Found path: %d nodes (%dms, %d explored, %d ns/node)".formatted(path.length(), path.timeElapsed() / 1_000_000L, path.nodesExplored(), nsPerNode));
    } else {
      ChatUtils.send("No path found (%dms, %d explored, %d ns/node)".formatted(path.timeElapsed() / 1_000_000L, path.nodesExplored(), nsPerNode));
    }

    PathRenderer.setPath(path);

    return 1;
  }

}
