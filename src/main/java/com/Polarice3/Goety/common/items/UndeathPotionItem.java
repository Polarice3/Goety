package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.utils.ParticleUtil;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class UndeathPotionItem extends Item {
    public UndeathPotionItem() {
        super(new Item.Properties()
                .tab(Goety.TAB)
                .rarity(Rarity.EPIC)
                .setNoRepair()
                .stacksTo(1)
        );
    }

    public ItemStack finishUsingItem(ItemStack pStack, World pLevel, LivingEntity pEntityLiving) {
        super.finishUsingItem(pStack, pLevel, pEntityLiving);
        if (pEntityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) pEntityLiving;
            if (!pLevel.isClientSide) {
                ServerWorld serverWorld = (ServerWorld) pLevel;
                CompoundNBT playerData = player.getPersistentData();
                CompoundNBT data;

                if (!playerData.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
                    data = new CompoundNBT();
                } else {
                    data = playerData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
                }
                if (serverWorld.getMoonBrightness() > 0.9F && RobeArmorFinder.FindNecroSet(pEntityLiving)){
                    if (!data.getBoolean("goety:isLich")) {
                        data.putBoolean("goety:isLich", true);
                        playerData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
                        player.displayClientMessage(new TranslationTextComponent("info.goety.lichdom.success"), true);
                        player.addEffect(new EffectInstance(Effects.BLINDNESS, 20, 1));
                        player.addEffect(new EffectInstance(Effects.CONFUSION, 20, 1));
                        player.playSound(SoundEvents.WITHER_DEATH, 1.0F, 0.5F);
                        player.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE, 1.0F, 0.5F);
                        for (int i = 0; i < pLevel.random.nextInt(35) + 10; ++i) {
                            double d = pLevel.random.nextGaussian() * 0.2D;
                            new ParticleUtil(ParticleTypes.LARGE_SMOKE, player.getX(), player.getEyeY(), player.getZ(), d, d, d);
                        }
                        player.hurt(DamageSource.WITHER, 50.0F);
                    } else {
                        for (int i = 0; i < pLevel.random.nextInt(35) + 10; ++i) {
                            double d = pLevel.random.nextGaussian() * 0.2D;
                            new ParticleUtil(ParticleTypes.WITCH, player.getX(), player.getEyeY(), player.getZ(), d, d, d);
                        }
                        player.heal(20.0F);
                    }
                } else {
                    if (!data.getBoolean("goety:isLich")) {
                        player.displayClientMessage(new TranslationTextComponent("info.goety.lichdom.fail"), true);
                        player.hurt(DamageSource.MAGIC, 50.0F);
                    } else {
                        for (int i = 0; i < pLevel.random.nextInt(35) + 10; ++i) {
                            double d = pLevel.random.nextGaussian() * 0.2D;
                            new ParticleUtil(ParticleTypes.WITCH, player.getX(), player.getEyeY(), player.getZ(), d, d, d);
                        }
                        player.heal(20.0F);
                    }
                }
            }
        }

        if (pStack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        } else {
            if (pEntityLiving instanceof PlayerEntity && !((PlayerEntity)pEntityLiving).abilities.instabuild) {
                ItemStack itemstack = new ItemStack(Items.GLASS_BOTTLE);
                PlayerEntity playerentity = (PlayerEntity)pEntityLiving;
                if (!playerentity.inventory.add(itemstack)) {
                    playerentity.drop(itemstack, false);
                }
            }

            return pStack;
        }
    }

    public int getUseDuration(ItemStack pStack) {
        return 40;
    }

    public UseAction getUseAnimation(ItemStack pStack) {
        return UseAction.DRINK;
    }

    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    public ActionResult<ItemStack> use(World pLevel, PlayerEntity pPlayer, Hand pHand) {
        for (int i = 0; i < pLevel.random.nextInt(35) + 10; ++i) {
            double d = pLevel.random.nextGaussian() * 0.2D;
            new ParticleUtil(ParticleTypes.SMOKE, pPlayer.getX(), pPlayer.getEyeY(), pPlayer.getZ(), d, d, d);
        }
        return DrinkHelper.useDrink(pLevel, pPlayer, pHand);
    }
}
