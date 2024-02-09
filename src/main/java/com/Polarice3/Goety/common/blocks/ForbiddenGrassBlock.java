package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.blocks.tiles.ForbiddenGrassTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class ForbiddenGrassBlock extends SnowyDirtBlock implements ITileEntityProvider {
    public ForbiddenGrassBlock() {
        super(Properties.of(Material.GRASS, MaterialColor.COLOR_PURPLE)
                .randomTicks()
                .strength(0.6F)
                .sound(SoundType.GRASS));
    }

    private static boolean canBeGrass(BlockState pState, IWorldReader pLevelReader, BlockPos pPos) {
        BlockPos blockpos = pPos.above();
        BlockState blockstate = pLevelReader.getBlockState(blockpos);
        if (blockstate.is(Blocks.SNOW) && blockstate.getValue(SnowBlock.LAYERS) == 1) {
            return true;
        } else if (blockstate.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int i = LightEngine.getLightBlockInto(pLevelReader, pState, pPos, blockstate, blockpos, Direction.UP, blockstate.getLightBlock(pLevelReader, blockpos));
            return i < pLevelReader.getMaxLightLevel();
        }
    }

    private static boolean canPropagate(IWorldReader pLevel, BlockPos pPos) {
        BlockPos blockpos = pPos.above();
        return pLevel.getBlockState(blockpos).isAir() && !pLevel.getFluidState(blockpos).is(FluidTags.WATER);
    }

    public void randomTick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom) {
        if (pLevel.getBlockState(pPos.above()).getBlock() instanceof AbstractFireBlock || !canBeGrass(pState, pLevel, pPos)){
            if (!pLevel.isAreaLoaded(pPos, 3)) {
                return;
            }
            pLevel.setBlockAndUpdate(pPos, Blocks.DIRT.defaultBlockState());
        } else {
            BlockState blockstate = this.defaultBlockState();

            for(int i = 0; i < 4; ++i) {
                BlockPos blockpos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(5) - 3, pRandom.nextInt(3) - 1);
                if (pLevel.getBlockState(blockpos).is(Blocks.DIRT) || pLevel.getBlockState(blockpos).is(Blocks.GRASS_BLOCK)) {
                    if (canPropagate(pLevel, blockpos)) {
                        pLevel.setBlockAndUpdate(blockpos, blockstate.setValue(SNOWY, pLevel.getBlockState(blockpos.above()).is(Blocks.SNOW)));
                    }
                }
            }

        }
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState pState, World pLevel, BlockPos pPos, Random pRand) {
        if (pLevel.getBlockState(pPos.above()).isAir()) {
            if (pRand.nextFloat() <= 0.25F) {
                double d0 = (double) pPos.getX() + 0.5D;
                double d1 = (double) pPos.getY() + 0.6D;
                double d2 = (double) pPos.getZ() + 0.5D;
                pLevel.addParticle(ParticleTypes.WITCH, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new ForbiddenGrassTileEntity();
    }
}
