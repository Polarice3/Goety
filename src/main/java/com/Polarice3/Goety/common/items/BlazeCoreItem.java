package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;

public class BlazeCoreItem extends Item {
    public BlazeCoreItem(){
        super(new Properties().tab(Goety.TAB).fireResistant());
    }

    public ActionResultType useOn(ItemUseContext context) {
        PlayerEntity playerentity = context.getPlayer();
        World world = context.getLevel();
        ItemStack itemstack = context.getItemInHand();
        BlockPos blockpos = context.getClickedPos();
        Direction direction = context.getHorizontalDirection();
        BlockState blockstate = world.getBlockState(blockpos);
        if (blockstate.is(Blocks.SPAWNER)) {
            TileEntity tileentity = world.getBlockEntity(blockpos);
            if (tileentity instanceof MobSpawnerTileEntity) {
                AbstractSpawner abstractspawner = ((MobSpawnerTileEntity)tileentity).getSpawner();
                EntityType<?> entitytype1 = EntityType.BLAZE;
                abstractspawner.setEntityId(entitytype1);
                tileentity.setChanged();
                world.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
                itemstack.shrink(1);
                return ActionResultType.CONSUME;
            }
        }
        if (CampfireBlock.canLight(blockstate)) {
            world.playSound(playerentity, blockpos, SoundEvents.FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
            world.setBlock(blockpos, blockstate.setValue(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);
            return ActionResultType.sidedSuccess(world.isClientSide());
        }else {
            BlockPos blockpos1 = blockpos.relative(context.getClickedFace());
            if (AbstractFireBlock.canBePlacedAt(world, blockpos1, context.getHorizontalDirection())) {
                world.playSound(playerentity, blockpos1, SoundEvents.FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
                BlockState blockstate1 = AbstractFireBlock.getState(world, blockpos1);
                world.setBlock(blockpos1, blockstate1, 11);
                return ActionResultType.sidedSuccess(world.isClientSide());
            } else {
                return ActionResultType.FAIL;
            }
        }
    }
}
