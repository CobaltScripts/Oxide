package oxide.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.LevelRenderer;
import oxide.render.PathRenderer;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

  @Inject(
    method = "submitFeatures",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;finalizeGizmoCollection()V")
  )
  private void beforeCollectGizmos(CallbackInfo ci) {
    PathRenderer.render();
  }
}
