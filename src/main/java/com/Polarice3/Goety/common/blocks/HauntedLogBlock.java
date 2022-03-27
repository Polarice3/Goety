package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.init.ModRegistry;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class HauntedLogBlock extends RotatedPillarBlock implements IDeadBlock{
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;

    public HauntedLogBlock() {
        super(Properties.of(Material.WOOD, MaterialColor.COLOR_GRAY)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .randomTicks()
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(ENABLED, Boolean.TRUE).setValue(AXIS, Direction.Axis.Y));
    }

    public boolean isRandomlyTicking(BlockState pState) {
        return pState.getValue(ENABLED);
    }

    public void randomTick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom) {
        if (pState.getValue(ENABLED)) {
            this.spreadHaunt(pState, pLevel, pPos, pRandom);
        }
    }

    public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.getItem() instanceof AxeItem){
            if (this.getBlock() == ModRegistry.HAUNTED_LOG.get()){
                itemstack.hurtAndBreak(1, pPlayer, (p_220040_1_) -> {
                    p_220040_1_.broadcastBreakEvent(pHand);
                });
                pLevel.removeBlock(pPos, false);
                pLevel.playSound(pPlayer, pPos, SoundEvents.AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                pLevel.setBlock(pPos, StrippedLog(pState), 11);
                return ActionResultType.SUCCESS;
            }
            if (this.getBlock() == ModRegistry.HAUNTED_WOOD.get()){
                itemstack.hurtAndBreak(1, pPlayer, (p_220040_1_) -> {
                    p_220040_1_.broadcastBreakEvent(pHand);
                });
                pLevel.removeBlock(pPos, false);
                pLevel.playSound(pPlayer, pPos, SoundEvents.AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                pLevel.setBlock(pPos, StrippedWood(pState), 11);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    public static BlockState StrippedLog(BlockState originalState) {
        Block block = ModRegistry.STRIPPED_HAUNTED_LOG.get();
        return block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, originalState.getValue(RotatedPillarBlock.AXIS));
    }

    public static BlockState StrippedWood(BlockState originalState) {
        Block block = ModRegistry.STRIPPED_HAUNTED_WOOD.get();
        return block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, originalState.getValue(RotatedPillarBlock.AXIS));
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ENABLED, AXIS);
    }

    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        return this.defaultBlockState().setValue(ENABLED, Boolean.FALSE).setValue(AXIS, pContext.getClickedFace().getAxis());
    }
}
