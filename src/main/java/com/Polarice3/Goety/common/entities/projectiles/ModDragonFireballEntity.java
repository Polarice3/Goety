package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class ModDragonFireballEntity extends DamagingProjectileEntity {
    public ModDragonFireballEntity(EntityType<? extends ModDragonFireballEntity> p_i50171_1_, World p_i50171_2_) {
        super(p_i50171_1_, p_i50171_2_);
    }

    public ModDragonFireballEntity(World p_i46775_1_, double p_i46775_2_, double p_i46775_4_, double p_i46775_6_, double p_i46775_8_, double p_i46775_10_, double p_i46775_12_) {
        super(ModEntityType.MOD_DRAGON_FIREBALL.get(), p_i46775_2_, p_i46775_4_, p_i46775_6_, p_i46775_8_, p_i46775_10_, p_i46775_12_, p_i46775_1_);
    }

    public ModDragonFireballEntity(World p_i46776_1_, LivingEntity p_i46776_2_, double p_i46776_3_, double p_i46776_5_, double p_i46776_7_) {
        super(ModEntityType.MOD_DRAGON_FIREBALL.get(), p_i46776_2_, p_i46776_3_, p_i46776_5_, p_i46776_7_, p_i46776_1_);
    }

    protected void onHit(RayTraceResult pResult) {
        super.onHit(pResult);
        Entity owner = this.getOwner();
        float radius = 0;
        int duration = 1;
        if (pResult.getType() != RayTraceResult.Type.ENTITY || !((EntityRayTraceResult)pResult).getEntity().is(owner)) {
            if (!this.level.isClientSide) {
                List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D));
                AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.level, this.getX(), this.getY(), this.getZ());
                if (owner instanceof LivingEntity) {
                    if (owner instanceof PlayerEntity){
                        PlayerEntity player = (PlayerEntity) owner;
                        if (WandUtil.enchantedFocus(player)){
                            radius = WandUtil.getLevels(ModEnchantments.RADIUS.get(), player);
                            duration = WandUtil.getLevels(ModEnchantments.DURATION.get(), player) + 1;
                        }
                    }
                    areaeffectcloudentity.setOwner((LivingEntity)owner);
                }

                areaeffectcloudentity.setParticle(ParticleTypes.DRAGON_BREATH);
                areaeffectcloudentity.setRadius(3.0F + radius);
                areaeffectcloudentity.setDuration(600 * duration);
                areaeffectcloudentity.setRadiusPerTick((7.0F - areaeffectcloudentity.getRadius()) / (float)areaeffectcloudentity.getDuration());
                areaeffectcloudentity.addEffect(new EffectInstance(Effects.HARM, 1, 1));
                if (!list.isEmpty()) {
                    for(LivingEntity livingentity : list) {
                        double d0 = this.distanceToSqr(livingentity);
                        if (d0 < 16.0D) {
                            areaeffectcloudentity.setPos(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                            break;
                        }
                    }
                }

                this.level.levelEvent(2006, this.blockPosition(), this.isSilent() ? -1 : 1);
                this.level.addFreshEntity(areaeffectcloudentity);
                this.remove();
            }

        }
    }

    public boolean isPickable() {
        return false;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    protected IParticleData getTrailParticle() {
        return ParticleTypes.DRAGON_BREATH;
    }

    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
