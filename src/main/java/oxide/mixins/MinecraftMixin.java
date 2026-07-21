package oxide.mixins;

import oxide.cache.ChunkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

@Mixin(Minecraft.class)
public class MinecraftMixin {

  @Inject(method = "runTick", at = @At("HEAD"))
  public void tick(final boolean advanceGameTime, final CallbackInfo ci) {
    ChunkManager.onTick();
  }

  @Inject(method = "updateLevelInEngines(Lnet/minecraft/client/multiplayer/ClientLevel;Z)V", at = @At("HEAD"))
  private void levelChange(final ClientLevel world, final boolean bl, final CallbackInfo ci) {
    ChunkManager.clearCache();
  }

}
