package oxide.movement;

import lombok.Getter;
import oxide.movement.walk.WalkAscend;
import oxide.movement.walk.WalkDescend;
import oxide.movement.walk.WalkDiagonal;
import oxide.movement.walk.WalkTraverse;

public enum MovementType {
  WALK(
    WalkTraverse.NORTH,
    WalkTraverse.EAST,
    WalkTraverse.SOUTH,
    WalkTraverse.WEST,

    WalkAscend.NORTH,
    WalkAscend.EAST,
    WalkAscend.SOUTH,
    WalkAscend.WEST,

    WalkDescend.NORTH,
    WalkDescend.EAST,
    WalkDescend.SOUTH,
    WalkDescend.WEST,

    WalkDiagonal.NORTH_EAST,
    WalkDiagonal.NORTH_WEST,
    WalkDiagonal.SOUTH_EAST,
    WalkDiagonal.SOUTH_WEST
  );

  @Getter
  private final Movement[] movements;

  MovementType(final Movement... movements) {
    this.movements = movements;
  }
}
