package oxide.util;

import lombok.experimental.UtilityClass;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;

@UtilityClass
public class BlockUtils {

  public static boolean canWalkThrough(final BlockState state) {
    final Block block = state.getBlock();

    if (block instanceof AirBlock) {
      return true;
    }

    if (
      block instanceof BaseFireBlock ||
        block == Blocks.COBWEB ||
        block == Blocks.END_PORTAL ||
        block == Blocks.COCOA ||
        block instanceof AbstractSkullBlock ||
        block == Blocks.BUBBLE_COLUMN ||
        block instanceof ShulkerBoxBlock ||
        block instanceof SlabBlock ||
        block instanceof TrapDoorBlock ||
        block == Blocks.HONEY_BLOCK ||
        block == Blocks.END_ROD ||
        block == Blocks.SWEET_BERRY_BUSH ||
        block == Blocks.POINTED_DRIPSTONE ||
        block instanceof AmethystClusterBlock ||
        block instanceof AzaleaBlock
    ) {
      return false;
    }

    if (block == Blocks.BIG_DRIPLEAF || block == Blocks.POWDER_SNOW) {
      return false;
    }

    if (block instanceof DoorBlock || block instanceof FenceGateBlock) {
      return block != Blocks.IRON_DOOR;
    }

    if (block instanceof CarpetBlock || block instanceof SnowLayerBlock) {
      return true;
    }

    final FluidState fluidState = state.getFluidState();

    if (!fluidState.isEmpty()) {
      return fluidState.getType() instanceof WaterFluid &&
        fluidState.getType().getAmount(fluidState) == 8;
    }

    if (block instanceof CauldronBlock) {
      return false;
    }

    return state.isPathfindable(PathComputationType.LAND);
  }

  public static boolean canWalkOn(final BlockState state) {
    final Block block = state.getBlock();

    if (
      isFullCube(state) &&
        block != Blocks.MAGMA_BLOCK &&
        block != Blocks.BUBBLE_COLUMN &&
        block != Blocks.HONEY_BLOCK
    ) {
      return true;
    }

    if (block instanceof AzaleaBlock) {
      return true;
    }

    if (isClimbable(block)) {
      return true;
    }

    if (
      block == Blocks.FARMLAND ||
        block == Blocks.DIRT_PATH ||
        block == Blocks.SOUL_SAND
    ) {
      return true;
    }

    if (
      block == Blocks.ENDER_CHEST ||
        block == Blocks.CHEST ||
        block == Blocks.TRAPPED_CHEST
    ) {
      return true;
    }

    if (block == Blocks.GLASS || block instanceof StainedGlassBlock) {
      return true;
    }

    if (block instanceof StairBlock) {
      return true;
    }

    if (isWater(state)) {
      return false;
    }

    if (block instanceof SlabBlock) {
      return true;
    }

    return false;
  }

  public static boolean fullyPassable(final BlockState state) {
    final Block block = state.getBlock();

    if (block instanceof AirBlock) {
      return true;
    }

    if (
      block instanceof BaseFireBlock ||
        block == Blocks.TRIPWIRE ||
        block == Blocks.COBWEB ||
        block == Blocks.VINE ||
        block == Blocks.LADDER ||
        block == Blocks.COCOA ||
        block instanceof AzaleaBlock ||
        block instanceof DoorBlock ||
        block instanceof FenceGateBlock ||
        block instanceof SnowLayerBlock ||
        !state.getFluidState().isEmpty() ||
        block instanceof TrapDoorBlock ||
        block instanceof EndPortalBlock ||
        block instanceof SkullBlock ||
        block instanceof ShulkerBoxBlock
    ) {
      return false;
    }

    return state.isPathfindable(PathComputationType.LAND);
  }

  public static boolean isClimbable(final Block block) {
    return block == Blocks.LADDER ||
      block == Blocks.VINE ||
      block == Blocks.WEEPING_VINES ||
      block == Blocks.WEEPING_VINES_PLANT ||
      block == Blocks.TWISTING_VINES ||
      block == Blocks.TWISTING_VINES_PLANT;
  }

  private static boolean isFullCube(final BlockState state) {
    final Block block = state.getBlock();

    if (
      block instanceof BambooStalkBlock ||
        block instanceof MovingPistonBlock ||
        block instanceof ScaffoldingBlock ||
        block instanceof ShulkerBoxBlock ||
        block instanceof PointedDripstoneBlock ||
        block instanceof AmethystClusterBlock
    ) {
      return false;
    }

    try {
      return Block.isShapeFullBlock(state.getCollisionShape(null, null));
    } catch (Exception e) {
      return false;
    }
  }

  private static boolean isWater(final BlockState state) {
    final Fluid f = state.getFluidState().getType();
    return f == Fluids.WATER || f == Fluids.FLOWING_WATER;
  }

  public static Vec3 toCenterVec(final BlockPos pos) {
    return new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ());
  }

}
