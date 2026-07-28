package oxide.movement;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import oxide.util.helper.Context;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Movement {

  @Getter
  protected final int dx, dz;

  public abstract void calculateCost(
    final Context ctx,
    final int startX,
    final int startY,
    final int startZ,
    final MovementResult result
  );
}
