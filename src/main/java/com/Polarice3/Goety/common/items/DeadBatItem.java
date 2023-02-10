package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.ally.LoyalSpiderEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class DeadBatItem extends Item {
    public DeadBatItem() {
        super(new Properties()
                .tab(Goety.TAB)
                .food(new Food.Builder()
                        .nutrition(2)
                        .saturationMod(0.2F)
                        .alwaysEat()
                        .meat()
                        .effect(new EffectInstance(Effects.POISON, 300, 1), 1.0F)
                        .effect(new EffectInstance(Effects.HUNGER, 300, 1), 1.0F)
                        .effect(new EffectInstance(Effects.BLINDNESS, 300), 1.0F)
                        .effect(new EffectInstance(Effects.CONFUSION, 300), 1.0F)
                        .build()
                )
        );
    }

    @Nonnull
    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand){
        World world = player.getEntity().level;
        int random = world.random.nextInt(3);
        if (RobeArmorFinder.FindFelSet(player)) {
            if (target instanceof SpiderEntity && target.isAlive()) {
                if (!world.isClientSide) {
                    if (random == 0){
                        target.remove();
                        LoyalSpiderEntity tamedSpider = new LoyalSpiderEntity(ModEntityType.TAMED_SPIDER.get(), world);
                        tamedSpider.moveTo(target.getX(), target.getY(), target.getZ(), target.yRot, target.xRot);
                        tamedSpider.setNoAi(((SpiderEntity) target).isNoAi());
                        if (target.hasCustomName()) {
                            tamedSpider.setCustomName(target.getCustomName());
                            tamedSpider.setCustomNameVisible(target.isCustomNameVisible());
                        }
                        if (target instanceof CaveSpiderEntity){
                            tamedSpider.setPoison(true);
                        }
                        tamedSpider.setOwnerId(player.getUUID());
                        tamedSpider.setPersistenceRequired();
                        tamedSpider.finalizeSpawn((IServerWorld) world, world.getCurrentDifficultyAt(tamedSpider.blockPosition()), SpawnReason.CONVERSION, null, null);
                        world.addFreshEntity(tamedSpider);
                        world.broadcastEntityEvent(tamedSpider, (byte) 7);
                        stack.shrink(1);
                    } else {
                        world.broadcastEntityEvent(target, (byte)6);
                        stack.shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
                target.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
            } else {
                return ActionResultType.CONSUME;
            }
        } else {
            return ActionResultType.CONSUME;
        }
        return super.interactLivingEntity(stack, player, target, hand);

    }

}
