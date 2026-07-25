package oxide.mixins;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oxide.cache.ChunkManager;
import oxide.cache.ChunkUpdateFlag;
import oxide.command.CommandManager;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

  @Inject(method = "sendChat", at = @At("HEAD"), cancellable = true)
  private void onSendChat(String content, CallbackInfo ci) {
    if (CommandManager.getInstance().execute(content)) {
      ci.cancel();
    }
  }

  @Inject(method = "handleLevelChunkWithLight", at = @At("RETURN"))
  private void loadChunk(final ClientboundLevelChunkWithLightPacket packet, final CallbackInfo ci) {
    ChunkManager.queueChunk(packet.getX(), packet.getZ());
  }

  @WrapMethod(method = "handleChunkBlocksUpdate")
  private void chunkUpdate(final ClientboundSectionBlocksUpdatePacket packet, final Operation<Void> original) {
    ChunkUpdateFlag.withChunkDeltaUpdating(() -> {
      original.call(packet);
    });
  }

}
