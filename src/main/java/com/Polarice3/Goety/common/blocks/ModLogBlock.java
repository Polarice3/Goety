package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class ModLogBlock extends RotatedPillarBlock{

    public ModLogBlock(MaterialColor color) {
        super(Properties.of(Material.WOOD, color)
                .strength(2.0F)
                .sound(SoundType.WOOD)
        );
    }

    public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.getItem() instanceof AxeItem){
            if (this.getBlock() == ModBlocks.GLOOM_LOG.get() || this.getBlock() == ModBlocks.MURK_LOG.get()){
                itemstack.hurtAndBreak(1, pPlayer, (p_220040_1_) -> {
                    p_220040_1_.broadcastBreakEvent(pHand);
                });
                pLevel.removeBlock(pPos, false);
                pLevel.playSound(pPlayer, pPos, SoundEvents.AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                pLevel.setBlock(pPos, StrippedLog(pState, this.getBlock()), 11);
                return ActionResultType.SUCCESS;
            }
            if (this.getBlock() == ModBlocks.GLOOM_WOOD.get() || this.getBlock() == ModBlocks.MURK_WOOD.get()){
                itemstack.hurtAndBreak(1, pPlayer, (p_220040_1_) -> {
                    p_220040_1_.broadcastBreakEvent(pHand);
                });
                pLevel.removeBlock(pPos, false);
                pLevel.playSound(pPlayer, pPos, SoundEvents.AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                pLevel.setBlock(pPos, StrippedWood(pState, this.getBlock()), 11);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    public static BlockState StrippedLog(BlockState originalState, Block originalBlock) {
        Block block = ModBlocks.STRIPPED_GLOOM_LOG.get();
        if (originalBlock == ModBlocks.GLOOM_LOG.get()){
            block = ModBlocks.STRIPPED_GLOOM_LOG.get();
        }
        if (originalBlock == ModBlocks.MURK_LOG.get()){
            block = ModBlocks.STRIPPED_MURK_LOG.get();
        }
        return block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, originalState.getValue(RotatedPillarBlock.AXIS));
    }

    public static BlockState StrippedWood(BlockState originalState, Block originalBlock) {
        Block block = ModBlocks.STRIPPED_GLOOM_WOOD.get();
        if (originalBlock == ModBlocks.GLOOM_WOOD.get()){
            block = ModBlocks.STRIPPED_GLOOM_WOOD.get();
        }
        if (originalBlock == ModBlocks.MURK_WOOD.get()){
            block = ModBlocks.STRIPPED_MURK_WOOD.get();
        }
        return block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, originalState.getValue(RotatedPillarBlock.AXIS));
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AXIS);
    }

    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        return this.defaultBlockState().setValue(AXIS, pContext.getClickedFace().getAxis());
    }
}
