package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.lichdom.ILichdom;
import com.Polarice3.Goety.common.soulenergy.ISoulEnergy;
import com.Polarice3.Goety.utils.LichdomHelper;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import com.Polarice3.Goety.utils.SEHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class UndeathPotionItem extends Item {
    public UndeathPotionItem() {
        super(new Item.Properties()
                .tab(Goety.TAB)
                .rarity(Rarity.EPIC)
                .craftRemainder(Items.GLASS_BOTTLE)
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
                ISoulEnergy soulEnergy = SEHelper.getCapability(player);
                boolean isLich = lichdom.getLichdom();
                ServerWorld serverWorld = (ServerWorld) pLevel;
                if (serverWorld.getMoonBrightness() > 0.9F && RobeArmorFinder.FindAnySet(pEntityLiving)){
                    if (!isLich && soulEnergy.getSEActive() && soulEnergy.getArcaBlock() != null) {
                        if (pEntityLiving instanceof ServerPlayerEntity) {
                            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)pEntityLiving;
                            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayerentity, pStack);
                            serverplayerentity.awardStat(Stats.ITEM_USED.get(this));
                        }
                        lichdom.setLichdom(true);
                        LichdomHelper.sendLichUpdatePacket(player);
                        player.displayClientMessage(new TranslationTextComponent("info.goety.lichdom.success"), true);
                        player.addEffect(new EffectInstance(Effects.BLINDNESS, 20, 1));
                        player.addEffect(new EffectInstance(Effects.CONFUSION, 20, 1));
                        player.playSound(SoundEvents.WITHER_DEATH, 1.0F, 0.5F);
                        player.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE, 1.0F, 0.5F);
                        player.hurt(DamageSource.WITHER, 50.0F);
                    } else {
                        player.heal(20.0F);
                    }
                } else {
                    if (!isLich) {
                        player.displayClientMessage(new TranslationTextComponent("info.goety.lichdom.fail"), true);
                        player.hurt(DamageSource.MAGIC, 50.0F);
                    } else {
                        player.heal(20.0F);
                    }
                }
                if(!player.abilities.instabuild) {
                    pStack.shrink(1);
                }
            }
        }
        return pStack.isEmpty() ? new ItemStack(Items.GLASS_BOTTLE) : pStack;
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
        if (!pLevel.isClientSide){
            ServerWorld serverWorld = (ServerWorld) pLevel;
            for (int i = 0; i < pLevel.random.nextInt(35) + 10; ++i) {
                double d = pLevel.random.nextGaussian() * 0.2D;
                serverWorld.sendParticles(ParticleTypes.SMOKE, pPlayer.getX(), pPlayer.getEyeY(), pPlayer.getZ(), 0, d, d, d, 0.5F);
            }
        }
        return DrinkHelper.useDrink(pLevel, pPlayer, pHand);
    }
}
