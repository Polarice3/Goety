package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.ritual.Ritual;
import com.Polarice3.Goety.common.tileentities.DarkAltarTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeBlock;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class DarkAltarBlock extends ContainerBlock implements IForgeBlock {
    public static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 13.0D, 15.0D);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public DarkAltarBlock() {
        super(Properties.of(Material.STONE)
                .strength(3.0F, 9.0F)
                .sound(SoundType.STONE)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)
                .noOcclusion()
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.FALSE));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos,
                                boolean isMoving) {
        super.neighborChanged(state, world, pos, blockIn, fromPos, isMoving);
        world.getBlockTicks().scheduleTick(pos, this, 0);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player,
                                Hand hand, BlockRayTraceResult hit) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof DarkAltarTileEntity) {
            DarkAltarTileEntity darkAltarTile = (DarkAltarTileEntity) tileEntity;
            return darkAltarTile.activate(world, pos, player, hand,
                    hit.getDirection()) ? ActionResultType.SUCCESS : ActionResultType.PASS;
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    public void onRemove(BlockState pState, World pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            TileEntity tileentity = pLevel.getBlockEntity(pPos);
            if (tileentity instanceof DarkAltarTileEntity) {
                ((DarkAltarTileEntity) tileentity).stopRitual(false);
                tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
                    dropInventoryItems(tileentity.getLevel(), tileentity.getBlockPos(), handler);
                });
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    public static void dropInventoryItems(World worldIn, BlockPos pos, IItemHandler itemHandler) {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            InventoryHelper.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), itemHandler.getStackInSlot(i));
        }
    }

    @SubscribeEvent
    public void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        PlayerEntity player = event.getPlayer();
        if (!player.level.isClientSide) {
            BlockPos pos = player.blockPosition();
            int range = Ritual.ITEM_USE_DETECTION_RANGE;
            for (BlockPos positionToCheck : BlockPos.betweenClosed(pos.offset(-range, -range, -range),
                    pos.offset(range, range, range))) {
                TileEntity tileEntity = player.level.getBlockEntity(positionToCheck);
                if (tileEntity instanceof DarkAltarTileEntity) {
                    DarkAltarTileEntity darkAltarTile = (DarkAltarTileEntity) tileEntity;
                    if (darkAltarTile.getCurrentRitualRecipe() != null && darkAltarTile.getCurrentRitualRecipe().getRitual().isValidItemUse(event)) {
                        darkAltarTile.notifyItemUse(event);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void livingDeath(LivingDeathEvent event) {
        LivingEntity entityLivingBase = event.getEntityLiving();
        if (!entityLivingBase.level.isClientSide) {
            if (event.getSource().getEntity() instanceof PlayerEntity) {
                BlockPos pos = entityLivingBase.blockPosition();
                int range = Ritual.SACRIFICE_DETECTION_RANGE;
                for (BlockPos positionToCheck : BlockPos.betweenClosed(pos.offset(-range, -range, -range),
                        pos.offset(range, range, range))) {
                    TileEntity tileEntity = entityLivingBase.level.getBlockEntity(positionToCheck);
                    if (tileEntity instanceof DarkAltarTileEntity) {
                        DarkAltarTileEntity darkAltarTileEntity = (DarkAltarTileEntity) tileEntity;
                        if (darkAltarTileEntity.getCurrentRitualRecipe() != null && darkAltarTileEntity.getCurrentRitualRecipe().getRitual().isValidSacrifice(entityLivingBase)) {
                            darkAltarTileEntity.notifySacrifice(entityLivingBase);
                        }
                    }
                }
            }
        }
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public VoxelShape getOcclusionShape(BlockState pState, IBlockReader pLevel, BlockPos pPos) {
        return SHAPE;
    }

    public VoxelShape getCollisionShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return SHAPE;
    }

    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return SHAPE;
    }

    public boolean isPathfindable(BlockState pState, IBlockReader pLevel, BlockPos pPos, PathType pType) {
        return false;
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LIT);
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new DarkAltarTileEntity();
    }
}

/*
 * MIT License
 *
 * Copyright 2020 klikli-dev, MrRiegel, Sam Bassett
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */