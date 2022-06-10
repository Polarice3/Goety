package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.utils.BlockFinder;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class DeadSandBlock extends FallingBlock implements IDeadBlock {
    public DeadSandBlock() {
        super(Properties.of(Material.SAND)
                .strength(0.5F)
                .sound(SoundType.SAND)
                .harvestLevel(0)
                .harvestTool(ToolType.SHOVEL)
                .randomTicks()
        );
    }

    public void animateTick(BlockState pState, World pLevel, BlockPos pPos, Random pRand) {
        if (pRand.nextInt(16) == 0) {
            BlockPos blockpos = pPos.below();
            if (pLevel.isEmptyBlock(blockpos)) {
                double d0 = (double) pPos.getX() + pRand.nextDouble();
                double d1 = (double) pPos.getY() - 0.05D;
                double d2 = (double) pPos.getZ() + pRand.nextDouble();
                BlockParticleData particleData = new BlockParticleData(ParticleTypes.FALLING_DUST, pState);
                pLevel.addParticle(particleData, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public void randomTick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom) {
        this.spreadSand(pLevel, pPos, pRandom);
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public int getDustColor(BlockState pState, IBlockReader pLevel, BlockPos pPos) {
        return 5064781;
    }

    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable) {
        BlockState plant = plantable.getPlant(world, pos.relative(facing));
        PlantType type = plantable.getPlantType(world, pos.relative(facing));
        if ((plant.getBlock() instanceof BushBlock && !(plant.getBlock() instanceof CropsBlock)
                && !type.equals(PlantType.BEACH) && !type.equals(PlantType.WATER) && !type.equals(PlantType.NETHER)
                && !(plant.getBlock() instanceof TallGrassBlock)
                && !(plant.getBlock() instanceof ILiquidContainer))
                || plant.getBlock().is(ModBlocks.HAUNTED_CACTUS.get())) {
            return state.is(this);
        }
        return false;
    }
}
