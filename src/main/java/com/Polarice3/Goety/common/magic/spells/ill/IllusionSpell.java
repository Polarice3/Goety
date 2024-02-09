package com.Polarice3.Goety.common.magic.spells.ill;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.entities.ally.IllusionCloneEntity;
import com.Polarice3.Goety.common.magic.Spells;
import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.network.packets.server.SSetPlayerOwnerPacket;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.ColorUtil;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.server.ServerWorld;

public class IllusionSpell extends Spells {
    public int defaultSoulCost() {
        return SpellConfig.IllusionCost.get();
    }

    public int defaultCastDuration() {
        return SpellConfig.IllusionDuration.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
    }

    @Override
    public int defaultSpellCooldown() {
        return SpellConfig.IllusionCoolDown.get();
    }

    @Override
    public ColorUtil particleColors(LivingEntity entityLiving) {
        return new ColorUtil(0.3F, 0.3F, 0.8F);
    }

    public void SpellResult(ServerWorld worldIn, LivingEntity entityLiving, boolean staff) {
        for (Entity entity : worldIn.getAllEntities()) {
            if (entity instanceof IllusionCloneEntity) {
                if (((IllusionCloneEntity) entity).getTrueOwner() == entityLiving) {
                    ((IllusionCloneEntity) entity).die(DamageSource.STARVE);
                }
            }
        }
        int i0 = 4;
        for (int i1 = 0; i1 < i0; ++i1) {
            IllusionCloneEntity summonedentity = new IllusionCloneEntity(ModEntityType.ILLUSION_CLONE.get(), worldIn);
            summonedentity.setTrueOwner(entityLiving);
            if (entityLiving instanceof PlayerEntity) {
                ModNetwork.sendToALL(new SSetPlayerOwnerPacket(summonedentity));
            }
            summonedentity.moveTo(BlockFinder.SummonRadius(entityLiving, worldIn), 0.0F, 0.0F);
            summonedentity.setLimitedLife(1200);
            summonedentity.setPersistenceRequired();
            summonedentity.setUpgraded(RobeArmorFinder.FindIllusionSet(entityLiving));
            summonedentity.finalizeSpawn(worldIn, entityLiving.level.getCurrentDifficultyAt(BlockFinder.SummonRadius(entityLiving, worldIn)), SpawnReason.MOB_SUMMONED, null, null);
            RayTraceResult rayTraceResult = this.rayTrace(worldIn, entityLiving, 16, 3);
            if (rayTraceResult instanceof EntityRayTraceResult) {
                Entity target = ((EntityRayTraceResult) rayTraceResult).getEntity();
                if (target instanceof LivingEntity){
                    double d2 = target.getX() - summonedentity.getX();
                    double d1 = target.getZ() - summonedentity.getZ();
                    summonedentity.yRot = -((float) MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
                }
            }
            MobUtil.moveDownToGround(summonedentity);
            worldIn.addFreshEntity(summonedentity);
            for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                worldIn.sendParticles(ParticleTypes.CLOUD, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0, 0.0F, 0.0F, 0.0F, 0);
            }
        }
        if (RobeArmorFinder.FindIllusionSet(entityLiving)){
            entityLiving.addEffect(new EffectInstance(Effects.INVISIBILITY, 1200));
            RayTraceResult rayTraceResult = this.rayTrace(worldIn, entityLiving, 16, 3);
            if (rayTraceResult instanceof EntityRayTraceResult) {
                Entity target = ((EntityRayTraceResult) rayTraceResult).getEntity();
                if (target instanceof LivingEntity){
                    ((LivingEntity) target).addEffect(new EffectInstance(Effects.BLINDNESS, 400));
                }
            }
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE, this.getSoundSource(), 1.0F, 1.0F);
        for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
            worldIn.sendParticles(ParticleTypes.CLOUD, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0, 0.0F, 0.0F, 0.0F, 0);
        }
    }

}
