package oxide;

import oxide.command.MainCommand;
import oxide.render.PathRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;

public final class Oxide implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    ClientCommandRegistrationCallback.EVENT.register((dispatcher, buildContext) ->
      MainCommand.dispatch(dispatcher)
    );

    LevelRenderEvents.BEFORE_GIZMOS.register(context -> PathRenderer.render());
  }

}
