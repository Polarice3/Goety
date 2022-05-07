package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.lichdom.ILichdom;
import com.Polarice3.Goety.utils.LichdomHelper;
import com.Polarice3.Goety.utils.ParticleUtil;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import com.Polarice3.Goety.utils.SoundUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
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
                ILichdom lichdom = LichdomHelper.getCapability(player);
                boolean isLich = lichdom.getLichdom();
                ServerWorld serverWorld = (ServerWorld) pLevel;
                if (serverWorld.getMoonBrightness() > 0.9F && RobeArmorFinder.FindAnySet(pEntityLiving)){
                    if (!isLich) {
                        lichdom.setLichdom(true);
                        LichdomHelper.sendLichUpdatePacket(player);
                        player.displayClientMessage(new TranslationTextComponent("info.goety.lichdom.success"), true);
                        player.addEffect(new EffectInstance(Effects.BLINDNESS, 20, 1));
                        player.addEffect(new EffectInstance(Effects.CONFUSION, 20, 1));
                        new SoundUtil(player.blockPosition(), SoundEvents.WITHER_DEATH, SoundCategory.PLAYERS, 1.0F, 0.5F);
                        new SoundUtil(player.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 1.0F, 0.5F);
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
                    if (!isLich) {
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
                return new ItemStack(Items.GLASS_BOTTLE);
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
