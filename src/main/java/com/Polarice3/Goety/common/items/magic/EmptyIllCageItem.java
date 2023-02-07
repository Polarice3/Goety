package com.Polarice3.Goety.common.items.magic;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.init.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.Objects;

public class EmptyIllCageItem extends Item {
    public EmptyIllCageItem() {
        super(new Properties()
                .tab(Goety.TAB)
                .rarity(Rarity.UNCOMMON)
                .stacksTo(1)
        );
    }

    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        World world = player.level;
        if (target instanceof AbstractVillagerEntity && !target.isBaby()) {
            if (target.isAlive()) {
                player.playSound(SoundEvents.IRON_TRAPDOOR_CLOSE, 1.0F, 1.0F);
                if (target instanceof WanderingTraderEntity){
                    target.playSound(SoundEvents.WANDERING_TRADER_NO, 1.0F, 1.0F);
                } else {
                    target.playSound(SoundEvents.VILLAGER_NO, 1.0F, 1.0F);
                }
                ItemStack newStack = new ItemStack(ModItems.FILLED_ILL_CAGE.get());
                this.setVillager((AbstractVillagerEntity) target, newStack);
                if (!player.addItem(newStack)) {
                    player.drop(newStack, false);
                }
                target.remove(true);
                stack.shrink(1);
                return ActionResultType.sidedSuccess(world.isClientSide);
            }
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }

    private void setVillager(AbstractVillagerEntity entity, ItemStack stack) {
        entity.stopRiding();
        entity.ejectPassengers();
        CompoundNBT entityNBT = new CompoundNBT();
        if (entity.getType().getRegistryName() == null) {
            return;
        }
        entityNBT.putString("entity", entity.getType().getRegistryName().toString());
        if (entity.hasCustomName()) {
            entityNBT.putString("name", Objects.requireNonNull(entity.getCustomName()).getString());
        }
        entity.save(entityNBT);
        CompoundNBT itemNBT = stack.getOrCreateTag();
        itemNBT.put("entity", entityNBT);
    }

}
