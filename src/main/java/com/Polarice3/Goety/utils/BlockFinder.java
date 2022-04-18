package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.WebBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class BlockFinder {
    public static boolean NotDeadSandImmune(BlockState state){
        return !state.is(ModTags.Blocks.DEAD_SAND_IMMUNE) && state.canOcclude()
                && state.getMaterial() != Material.AIR && state.getMaterial() != Material.NETHER_WOOD
                && state.getMaterial() != Material.LAVA && !state.hasTileEntity();
    }

    public static void DeadSandReplace(BlockPos pPos, World pLevel){
        BlockState blockstate = pLevel.getBlockState(pPos);
        if (BlockFinder.NotDeadSandImmune(blockstate)) {
            if (blockstate.getMaterial() == Material.STONE) {
                pLevel.destroyBlock(pPos, false);
                pLevel.setBlockAndUpdate(pPos, ModBlocks.DEAD_SANDSTONE.get().defaultBlockState());
            } else if (blockstate.is(BlockTags.LOGS)) {
                pLevel.destroyBlock(pPos, false);
                pLevel.setBlockAndUpdate(pPos, ModBlocks.HAUNTED_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, blockstate.getValue(RotatedPillarBlock.AXIS)));
            } else if (blockstate.is(BlockTags.ICE)) {
                pLevel.destroyBlock(pPos, false);
            } else {
                pLevel.destroyBlock(pPos, false);
                pLevel.setBlockAndUpdate(pPos, ModBlocks.DEAD_SAND.get().defaultBlockState());
            }
        }
    }

    public static void DeadSandReplaceLagFree(BlockPos pPos, World pLevel){
        BlockState blockstate = pLevel.getBlockState(pPos);
        if (BlockFinder.NotDeadSandImmune(blockstate)) {
            if (blockstate.getMaterial() == Material.STONE) {
                pLevel.removeBlock(pPos, false);
                pLevel.setBlockAndUpdate(pPos, ModBlocks.DEAD_SANDSTONE.get().defaultBlockState());
            } else if (blockstate.is(BlockTags.LOGS)) {
                pLevel.removeBlock(pPos, false);
                pLevel.setBlockAndUpdate(pPos, ModBlocks.HAUNTED_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, blockstate.getValue(RotatedPillarBlock.AXIS)));
            } else if (blockstate.is(BlockTags.ICE)) {
                pLevel.removeBlock(pPos, false);
            } else {
                pLevel.removeBlock(pPos, false);
                pLevel.setBlockAndUpdate(pPos, ModBlocks.DEAD_SAND.get().defaultBlockState());
            }
        }
    }

    public static void WebMovement(LivingEntity livingEntity){
        AxisAlignedBB axisalignedbb = livingEntity.getBoundingBox();
        BlockPos blockpos = new BlockPos(axisalignedbb.minX + 0.001D, axisalignedbb.minY + 0.001D, axisalignedbb.minZ + 0.001D);
        BlockPos blockpos1 = new BlockPos(axisalignedbb.maxX - 0.001D, axisalignedbb.maxY - 0.001D, axisalignedbb.maxZ - 0.001D);
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
        if (livingEntity.level.hasChunksAt(blockpos, blockpos1)) {
            for(int i = blockpos.getX(); i <= blockpos1.getX(); ++i) {
                for(int j = blockpos.getY(); j <= blockpos1.getY(); ++j) {
                    for(int k = blockpos.getZ(); k <= blockpos1.getZ(); ++k) {
                        blockpos$mutable.set(i, j, k);
                        BlockState blockstate = livingEntity.level.getBlockState(blockpos$mutable);
                        if (blockstate.getBlock() instanceof WebBlock){
                            livingEntity.makeStuckInBlock(blockstate, new Vector3d(1.5D, 1.05D, 1.5D));
                        }
                    }
                }
            }
        }
    }

    public static void ClimbAnyWall(LivingEntity livingEntity){
        Vector3d movement = livingEntity.getDeltaMovement();
        if (livingEntity instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) livingEntity;
            if (!player.abilities.flying && player.horizontalCollision){
                movement = new Vector3d(movement.x, 0.2D, movement.z);
            }
            player.setDeltaMovement(movement);
        } else {
            if (livingEntity.horizontalCollision){
                movement = new Vector3d(movement.x, 0.2D, movement.z);
            }
            livingEntity.setDeltaMovement(movement);
        }
    }

    public static double spawnY(LivingEntity livingEntity, BlockPos blockPos) {
        BlockPos blockpos = blockPos;
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = livingEntity.level.getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(livingEntity.level, blockpos1, Direction.UP)) {
                if (!livingEntity.level.isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = livingEntity.level.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(livingEntity.level, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= MathHelper.floor(livingEntity.getY()) - 1);

        if (flag) {
            return blockpos.getY() + d0;
        } else {
            return livingEntity.getY();
        }
    }

}
