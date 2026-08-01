package oxide.util.helper;

import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import oxide.cost.ActionCosts;

@Getter
@Accessors(fluent = true)
public class Context {

  private final LocalPlayer player;
  private final ClientLevel level;

  private final boolean useJumpBoost;
  private final int maxFallHeight;

  private final int jumpAmplifier;
  private final BlockAccessor access;
  private final ActionCosts costs;

  public Context() {
    this(Minecraft.getInstance().player, Minecraft.getInstance().level, true, 3);
  }

  public Context(final LocalPlayer player, final ClientLevel level) {
    this(player, level, true, 3);
  }

  public Context(
    final LocalPlayer player,
    final ClientLevel level,
    final boolean useJumpBoost,
    final int maxFallHeight
  ) {
    this.player = player;
    this.level = level;
    this.useJumpBoost = useJumpBoost;
    this.maxFallHeight = maxFallHeight;

    MobEffectInstance effect = player.getEffect(MobEffects.JUMP_BOOST);
    this.jumpAmplifier = effect != null ? effect.getAmplifier() : 0;
    this.access = new BlockAccessor(level);
    this.costs = new ActionCosts(this);
  }

  public BlockInfo get(final int x, final int y, final int z) {
    return access.get(x, y, z);
  }
}
