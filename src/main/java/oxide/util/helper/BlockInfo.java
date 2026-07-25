package oxide.util.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.VoxelShape;
import oxide.util.BlockUtils;

public record BlockInfo(
  int x, int y, int z,
  double penalty,
  boolean air,
  boolean passable,
  boolean standable,
  boolean climbable,
  Direction facing,
  LiquidType liquidType,
  double collisionHeight
) {

  public BlockInfo(final int x, final int y, final int z, final BlockState state) {
    this(
      x, y, z, 1.0,
      state.isAir(),
      state.isAir() || BlockUtils.canWalkThrough(state),
      !state.isAir() && BlockUtils.canWalkOn(state),
      !state.isAir() && BlockUtils.isClimbable(state.getBlock()),
      state.isAir() ? null : facing(state),
      state.isAir() ? LiquidType.NONE : liquidType(state),
      state.isAir() ? 0.0 : collisionHeight(state)
    );
  }

  private static double collisionHeight(final BlockState state) {
    VoxelShape shape = state.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);

    if (shape.isEmpty()) {
      return 0.0;
    }

    return shape.max(Direction.Axis.Y);
  }

  private static Direction facing(final BlockState state) {
    if (state.getBlock() instanceof StairBlock && state.getValue(StairBlock.HALF) == Half.BOTTOM) {
      return state.getValue(StairBlock.FACING);
    }

    if (state.getBlock() instanceof LadderBlock) {
      return state.getValue(LadderBlock.FACING);
    }

    if (state.getBlock() instanceof VineBlock) {
      for (var dir : Direction.Plane.HORIZONTAL) {
        if (state.getValue(VineBlock.getPropertyForFace(dir))) {
          return dir;
        }
      }

      return null;
    }

    return null;
  }

  private static LiquidType liquidType(final BlockState state) {
    FluidState fluid = state.getFluidState();

    if (fluid.isEmpty()) {
      return LiquidType.NONE;
    }

    boolean isLava = fluid.getType() == Fluids.LAVA || fluid.getType() == Fluids.FLOWING_LAVA;
    boolean source = fluid.isSource();

    if (isLava) {
      return source ? LiquidType.LAVA_SOURCE : LiquidType.LAVA_FLOWING;
    } else {
      return source ? LiquidType.WATER_SOURCE : LiquidType.WATER_FLOWING;
    }
  }

}
