package oxide.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import oxide.cache.ChunkManager;
import oxide.cache.ChunkUpdateFlag;
import oxide.util.helper.BlockInfo;

@Mixin(Level.class)
public class LevelMixin {

  @Inject(method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z", at = @At("RETURN"))
  private void injectBlockStateChange(final BlockPos pos, final BlockState newState, final int flags, final int maxUpdateDepth, final CallbackInfoReturnable<Boolean> cir) {
    if (Minecraft.getInstance().level != (Object) this || ChunkUpdateFlag.isChunkDeltaUpdating()) {
      return;
    }

    ChunkManager.queueUpdate(new BlockInfo(pos.getX(), pos.getY(), pos.getZ(), newState));
  }

}
