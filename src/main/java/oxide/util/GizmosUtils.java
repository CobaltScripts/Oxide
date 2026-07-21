package oxide.util;

import lombok.experimental.UtilityClass;
import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@UtilityClass
public class GizmosUtils {

  private static final Minecraft minecraft = Minecraft.getInstance();

  public static void drawBlockPos(final BlockPos pos, final Color color, final boolean esp, final float lineWidth) {
    drawBox(
      new AABB(
        pos.getX(),
        pos.getY(),
        pos.getZ(),
        pos.getX() + 1,
        pos.getY() + 1,
        pos.getZ() + 1
      ),
      color, esp, lineWidth
    );
  }

  public static void drawEntityOutline(final Entity entity, final Color color, final boolean esp, final float lineWidth) {
    final float partialTicks = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(true);

    final AABB box = entity.getBoundingBox().move(
      entity.xOld + (entity.getX() - entity.xOld) * partialTicks - entity.getX(),
      entity.yOld + (entity.getY() - entity.yOld) * partialTicks - entity.getY(),
      entity.zOld + (entity.getZ() - entity.zOld) * partialTicks - entity.getZ()
    );

    drawBox(box, color, esp, lineWidth);
  }

  public static void drawTracer(final Vec3 to, final Color color, final boolean esp, final float lineWidth) {
    final var camera = minecraft.gameRenderer.mainCamera();
    final Vec3 from = camera.position().add(Vec3.directionFromRotation(camera.xRot(), camera.yRot()));
    drawLine(from, to, color, esp, lineWidth);
  }

  public static void drawBox(final AABB box, final Color color, final boolean esp, final float lineWidth) {
    if (color.getAlpha() == 0) {
      return;
    }

    final var props = Gizmos.cuboid(box, GizmoStyle.strokeAndFill(
      ARGB.color(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue()), lineWidth,
      ARGB.color(40, color.getRed(), color.getGreen(), color.getBlue())
    ));

    if (esp) {
      props.setAlwaysOnTop();
    }
  }

  public static void drawLine(final Vec3 from, final Vec3 to, final Color color, final boolean esp, final float lineWidth) {
    if (color.getAlpha() == 0) {
      return;
    }

    final var props = Gizmos.line(from, to, ARGB.color(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue()), lineWidth);

    if (esp) {
      props.setAlwaysOnTop();
    }
  }

}
