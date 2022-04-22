package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.blocks.CursedCageBlock;
import com.Polarice3.Goety.common.tileentities.ArcaTileEntity;
import com.Polarice3.Goety.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import java.util.List;

public class SoulTransferItem extends Item {
    public SoulTransferItem() {
        super(new Properties().tab(Goety.TAB).stacksTo(1));
    }

    public ActionResultType useOn(ItemUseContext pContext) {
        World world = pContext.getLevel();
        ItemStack stack = pContext.getItemInHand();
        if (!stack.isEmpty()) {
            if (stack.getItem() instanceof SoulTransferItem) {
                BlockPos blockpos = pContext.getClickedPos();
                TileEntity tileEntity = world.getBlockEntity(blockpos);
                if (tileEntity instanceof ArcaTileEntity){
                    CompoundNBT nbt = new CompoundNBT();
                    stack.setTag(nbt);
                    nbt.putInt("X", tileEntity.getBlockPos().getX());
                    nbt.putInt("Y", tileEntity.getBlockPos().getY());
                    nbt.putInt("Z", tileEntity.getBlockPos().getZ());
                    return ActionResultType.sidedSuccess(world.isClientSide);
                }
                BlockState blockstate = world.getBlockState(blockpos);
                if (blockstate.is(ModBlocks.CURSED_CAGE_BLOCK.get()) && !blockstate.getValue(CursedCageBlock.POWERED)) {
                    ItemStack itemstack = pContext.getItemInHand();
                    if (!world.isClientSide) {
                        ((CursedCageBlock) ModBlocks.CURSED_CAGE_BLOCK.get()).setItem(world, blockpos, blockstate, itemstack);
                        world.levelEvent(null, 1010, blockpos, Item.getId(this));
                        itemstack.shrink(1);
                    }

                    return ActionResultType.sidedSuccess(world.isClientSide);
                }
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.hasTag()) {
            tooltip.add(new TranslationTextComponent("tooltip." + Goety.MOD_ID + ".arca").setStyle(Style.EMPTY.applyFormat((TextFormatting.GRAY))));
            tooltip.add(new TranslationTextComponent("tooltip." + Goety.MOD_ID + ".arcaX").setStyle(Style.EMPTY.applyFormat((TextFormatting.GRAY))).append(new StringTextComponent("" + stack.getTag().getInt("X")).setStyle(Style.EMPTY.applyFormat((TextFormatting.GRAY)))));
            tooltip.add(new TranslationTextComponent("tooltip." + Goety.MOD_ID + ".arcaY").setStyle(Style.EMPTY.applyFormat((TextFormatting.GRAY))).append(new StringTextComponent("" + stack.getTag().getInt("Y")).setStyle(Style.EMPTY.applyFormat((TextFormatting.GRAY)))));
            tooltip.add(new TranslationTextComponent("tooltip." + Goety.MOD_ID + ".arcaZ").setStyle(Style.EMPTY.applyFormat((TextFormatting.GRAY))).append(new StringTextComponent("" + stack.getTag().getInt("Z")).setStyle(Style.EMPTY.applyFormat((TextFormatting.GRAY)))));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip." + Goety.MOD_ID + ".arca_not_bound").setStyle(Style.EMPTY.applyFormat((TextFormatting.GRAY))));
        }
    }

}
