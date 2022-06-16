package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.utils.ConstantPaths;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class NetherBookItem extends Item {
    public NetherBookItem(){
        super(new Properties()
                .tab(Goety.TAB)
                .rarity(Rarity.EPIC)
                .setNoRepair()
                .stacksTo(1)
                .fireResistant()
        );
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (!worldIn.isClientSide){
            CompoundNBT playerData = playerIn.getPersistentData();
            CompoundNBT data;

            if (!playerData.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
                data = new CompoundNBT();
            } else {
                data = playerData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
            }

            if (!data.getBoolean(ConstantPaths.readNetherBook())) {
                data.putBoolean(ConstantPaths.readNetherBook(), true);
                playerData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
                CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) playerIn, itemstack);
                playerIn.displayClientMessage(new TranslationTextComponent("info.goety.research.nether"), true);
                itemstack.shrink(1);
                return ActionResult.consume(playerIn.getItemInHand(handIn));
            } else {
                return ActionResult.pass(playerIn.getItemInHand(handIn));
            }
        } else {
            return ActionResult.pass(playerIn.getItemInHand(handIn));
        }
    }
}
