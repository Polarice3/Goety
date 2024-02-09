package com.Polarice3.Goety.common.magic;

import com.Polarice3.Goety.api.magic.ISummonSpell;
import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;

public abstract class SummonSpells extends Spells implements ISummonSpell {
    private final EntityPredicate summonCountTargeting = (new EntityPredicate()).range(64.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();
    public int enchantment = 0;
    public int duration = 1;

    public abstract int SummonDownDuration();

    public boolean NecroPower(LivingEntity entityLiving){
        return RobeArmorFinder.FindNecroArmor(entityLiving);
    }

    public boolean NecroMastery(LivingEntity entityLiving){
        return RobeArmorFinder.FindNecroHelm(entityLiving);
    }

    public boolean SummonMastery(LivingEntity entityLiving){
        return RobeArmorFinder.FindLeggings(entityLiving);
    }

    public abstract void commonResult(ServerWorld worldIn, LivingEntity entityLiving);

    public void SummonDown(LivingEntity entityLiving){
        EffectInstance effectinstance1 = entityLiving.getEffect(ModEffects.SUMMONDOWN.get());
        int i = 1;
        if (effectinstance1 != null) {
            i += effectinstance1.getAmplifier();
            entityLiving.removeEffectNoUpdate(ModEffects.SUMMONDOWN.get());
        } else {
            --i;
        }

        i = MathHelper.clamp(i, 0, 4);
        int s = SummonDownDuration();
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                s = (int) (SummonDownDuration() * 1.5);
            }
        }
        if (SummonMastery(entityLiving)){
            if (entityLiving.level.random.nextBoolean()){
                s = SummonDownDuration()/2;
            }
        }
        EffectInstance effectinstance = new EffectInstance(ModEffects.SUMMONDOWN.get(), s, i, false, false, true);
        entityLiving.addEffect(effectinstance);
    }

    public int SummonLimit(LivingEntity entityLiving){
        return entityLiving.level.getNearbyEntities(OwnedEntity.class, this.summonCountTargeting, entityLiving, entityLiving.getBoundingBox().inflate(64.0D)).size();
    }

    public void summonAdvancement(LivingEntity summoner, LivingEntity summoned){
        if(summoner instanceof ServerPlayerEntity){
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) summoner;
            CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, summoned);
        }
    }
}
