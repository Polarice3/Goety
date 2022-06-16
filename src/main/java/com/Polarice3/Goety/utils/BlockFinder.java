package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.common.tileentities.PithosTileEntity;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModTags;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockFinder {

    public static boolean NoBreak(BlockState state){
        return state.getMaterial() == Material.STONE || state.getMaterial() == Material.METAL || state.getMaterial() == Material.HEAVY_METAL;
    }

    public static boolean NotDeadSandImmune(BlockState state){
        return state.is(ModTags.Blocks.DEAD_SAND_SPREADABLE)
                && state.getMaterial() != Material.AIR && state.getMaterial() != Material.LAVA
                && !state.hasTileEntity();
    }

    public static boolean LivingBlocks(BlockState state){
        return state.getBlock() instanceof HugeMushroomBlock || state.getBlock() instanceof StemGrownBlock || state.is(ModTags.Blocks.DEAD_BLOCK_SPREADABLE);
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
            } else if (blockstate.getBlock() instanceof BushBlock && !LivingBlocks(blockstate)) {
                pLevel.removeBlock(pPos, false);
                if (validPlants(pPos, pLevel)){
                    pLevel.setBlockAndUpdate(pPos, ModBlocks.HAUNTED_BUSH.get().defaultBlockState());
                }
            } else {
                pLevel.removeBlock(pPos, false);
                pLevel.setBlockAndUpdate(pPos, ModBlocks.DEAD_SAND.get().defaultBlockState());
            }
        }
        if (LivingBlocks(blockstate)){
            pLevel.removeBlock(pPos, false);
            pLevel.setBlockAndUpdate(pPos, ModBlocks.DEAD_BLOCK.get().defaultBlockState());
        }
    }

    public static boolean validPlants(BlockPos pPos, IBlockReader pLevel){
        BlockState blockstate = pLevel.getBlockState(pPos);
        if (blockstate.getBlock() instanceof IPlantable){
            IPlantable plantable = (IPlantable) blockstate.getBlock();
            PlantType type = plantable.getPlantType(pLevel, pPos);
            return !type.equals(PlantType.BEACH) && !type.equals(PlantType.WATER) && !type.equals(PlantType.NETHER)
                    && !(blockstate.getBlock() instanceof TallGrassBlock)
                    && !(blockstate.getBlock() instanceof ILiquidContainer);
        }
        return false;
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

    public static boolean noWall(LivingEntity livingEntity){
        World world = livingEntity.level;
        BlockPos.Mutable blockpos$mutable = livingEntity.blockPosition().mutable().move(0, 0, 0);
        return world.hasChunksAt(blockpos$mutable.getX() - 10, blockpos$mutable.getY() - 10, blockpos$mutable.getZ() - 10, blockpos$mutable.getX() + 10, blockpos$mutable.getY() + 10, blockpos$mutable.getZ() + 10);
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

    public static BlockPos SummonRadius(LivingEntity livingEntity, World world){
        BlockPos.Mutable blockpos$mutable = livingEntity.blockPosition().mutable().move(0, 0, 0);
        blockpos$mutable.setX(blockpos$mutable.getX() + world.random.nextInt(5) - world.random.nextInt(5));
        blockpos$mutable.setY((int) BlockFinder.spawnY(livingEntity, livingEntity.blockPosition()));
        blockpos$mutable.setZ(blockpos$mutable.getZ() + world.random.nextInt(5) - world.random.nextInt(5));
        if (noWall(livingEntity)){
            return blockpos$mutable;
        } else {
            return livingEntity.blockPosition().mutable().move(0, (int) BlockFinder.spawnY(livingEntity, livingEntity.blockPosition()), 0);
        }
    }

    public static boolean isWet(World pLevel, BlockPos pPos){
        if (pLevel.getBiome(pPos).getPrecipitation() != Biome.RainType.NONE){
            return pLevel.isRaining() && pLevel.canSeeSky(pPos);
        } else {
            return false;
        }
    }

    public static boolean createPithos(IServerWorld pLevel, MutableBoundingBox pBounds, Random pRandom, BlockPos pPos, ResourceLocation pResourceLocation, @Nullable BlockState pState) {
        if (pBounds.isInside(pPos) && !pLevel.getBlockState(pPos).is(ModBlocks.PITHOS_BLOCK.get())) {
            pLevel.setBlock(pPos, pState != null ? pState: ModBlocks.PITHOS_BLOCK.get().defaultBlockState(), 2);
            TileEntity tileentity = pLevel.getBlockEntity(pPos);
            if (tileentity instanceof PithosTileEntity) {
                ((PithosTileEntity)tileentity).setLootTable(pResourceLocation, pRandom.nextLong());
            }

            return true;
        } else {
            return false;
        }
    }

}
