package com.Polarice3.Goety.common.fluid;

import com.Polarice3.Goety.common.blocks.IDeadBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.Random;

public abstract class QuickSandFluid extends ForgeFlowingFluid implements IDeadBlock {
    public QuickSandFluid() {
        super(ModFluids.QUICKSAND_PROPERTIES);
    }

    public void randomTick(World pLevel, BlockPos pPos, FluidState pState, Random pRandom) {
        if (!pLevel.isClientSide) {
            boolean flag;
            ServerWorld serverWorld = (ServerWorld) pLevel;
            if (this.isSource(pState)){
                flag = true;
            } else {
                flag = pLevel.random.nextFloat() <= 0.05F;
            }
            if (flag) {
                this.spreadSand(serverWorld, pPos, pRandom);
                this.quickSandSelfSpread(pLevel, pPos);
            }
        }
    }

    public boolean isRandomlyTicking() {
        return true;
    }

    protected boolean canSpreadTo(IBlockReader pLevel, BlockPos pFromPos, BlockState pFromBlockState, Direction pDirection, BlockPos pToPos, BlockState pToBlockState, FluidState pToFluidState, Fluid pFluid) {
        return pToFluidState.is(FluidTags.LAVA) || super.canSpreadTo(pLevel, pFromPos, pFromBlockState, pDirection, pToPos, pToBlockState, pToFluidState, pFluid);
    }

    protected void spreadTo(IWorld pLevel, BlockPos pPos, BlockState pBlockState, Direction pDirection, FluidState pFluidState) {
        if (pDirection == Direction.DOWN) {
            FluidState fluidstate = pLevel.getFluidState(pPos);
            if (fluidstate.is(FluidTags.LAVA)) {
                if (pBlockState.getBlock() instanceof FlowingFluidBlock) {
                    pLevel.setBlock(pPos, net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(pLevel, pPos, pPos, Blocks.SMOOTH_STONE.defaultBlockState()), 3);
                }

                this.fizz(pLevel, pPos);
                return;
            }
        }

        super.spreadTo(pLevel, pPos, pBlockState, pDirection, pFluidState);
    }

    private void fizz(IWorld pLevel, BlockPos pPos) {
        pLevel.levelEvent(1501, pPos, 0);
    }

    public static class Flowing extends QuickSandFluid {
        protected void createFluidStateDefinition(StateContainer.Builder<Fluid, FluidState> pBuilder) {
            super.createFluidStateDefinition(pBuilder);
            pBuilder.add(LEVEL);
        }

        public int getAmount(FluidState pState) {
            return pState.getValue(LEVEL);
        }

        public boolean isSource(FluidState pState) {
            return false;
        }
    }

    public static class Source extends QuickSandFluid {
        public int getAmount(FluidState pState) {
            return 8;
        }

        public boolean isSource(FluidState pState) {
            return true;
        }
    }
}
