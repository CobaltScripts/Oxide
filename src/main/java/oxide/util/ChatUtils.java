package oxide.util;

import lombok.experimental.UtilityClass;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

@UtilityClass
public class ChatUtils {

  private static final Minecraft minecraft = Minecraft.getInstance();

  public static void send(final String message) {
    final Component component = Component.empty()
      .append(Component.literal("[").withStyle(ChatFormatting.DARK_GRAY))
      .append(Component.literal("Oxide").withStyle(s -> s.withColor(0xFF8800)))
      .append(Component.literal("] ").withStyle(ChatFormatting.DARK_GRAY))
      .append(Component.literal(message).withStyle(ChatFormatting.GRAY));

    sendComponent(component);
  }

  private static void sendComponent(final Component component) {
    minecraft.execute(() -> {
      if (minecraft.player != null) {
        minecraft.player.sendSystemMessage(component);
      }
    });
  }

}
