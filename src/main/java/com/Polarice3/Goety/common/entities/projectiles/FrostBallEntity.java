package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.EffectsUtil;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Map;

public class FrostBallEntity extends DamagingProjectileEntity {
    private static final DataParameter<Integer> DATA_TYPE_ID = EntityDataManager.defineId(FrostBallEntity.class, DataSerializers.INT);
    public static final Map<Integer, ResourceLocation> TEXTURE_BY_TYPE = Util.make(Maps.newHashMap(), (map) -> {
        map.put(0, Goety.location("textures/entity/projectiles/frost/frost_0.png"));
        map.put(1, Goety.location("textures/entity/projectiles/frost/frost_1.png"));
        map.put(2, Goety.location("textures/entity/projectiles/frost/frost_2.png"));
        map.put(3, Goety.location("textures/entity/projectiles/frost/frost_3.png"));
        map.put(4, Goety.location("textures/entity/projectiles/frost/frost_4.png"));
        map.put(5, Goety.location("textures/entity/projectiles/frost/frost_5.png"));
        map.put(6, Goety.location("textures/entity/projectiles/frost/frost_6.png"));
        map.put(7, Goety.location("textures/entity/projectiles/frost/frost_7.png"));
    });
    public FrostBallEntity(EntityType<? extends FrostBallEntity> p_i50160_1_, World p_i50160_2_) {
        super(p_i50160_1_, p_i50160_2_);
    }

    public FrostBallEntity(World p_i1771_1_, LivingEntity p_i1771_2_, double p_i1771_3_, double p_i1771_5_, double p_i1771_7_) {
        super(ModEntityType.FROST_BALL.get(), p_i1771_2_, p_i1771_3_, p_i1771_5_, p_i1771_7_, p_i1771_1_);
    }

    public FrostBallEntity(World p_i1772_1_, double p_i1772_2_, double p_i1772_4_, double p_i1772_6_, double p_i1772_8_, double p_i1772_10_, double p_i1772_12_) {
        super(ModEntityType.FROST_BALL.get(), p_i1772_2_, p_i1772_4_, p_i1772_6_, p_i1772_8_, p_i1772_10_, p_i1772_12_, p_i1772_1_);
    }

    public ResourceLocation getResourceLocation() {
        return TEXTURE_BY_TYPE.getOrDefault(this.getAnimation(), TEXTURE_BY_TYPE.get(0));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TYPE_ID, 0);
    }

    public int getAnimation() {
        return this.entityData.get(DATA_TYPE_ID);
    }

    public void setAnimation(int pType) {
        this.entityData.set(DATA_TYPE_ID, pType);
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Animation", this.getAnimation());
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setAnimation(pCompound.getInt("Animation"));
    }

    public void tick() {
        super.tick();
        if (this.getAnimation() < 7) {
            this.setAnimation(this.getAnimation() + 1);
        } else {
            this.setAnimation(0);
        }
    }

    protected void onHitEntity(EntityRayTraceResult pResult) {
        super.onHitEntity(pResult);
        if (!this.level.isClientSide) {
            Entity entity = pResult.getEntity();
            Entity entity1 = this.getOwner();
            boolean flag = entity.hurt(ModDamageSource.indirectFrost(this, entity1), 4.0F);
            if (entity1 instanceof LivingEntity) {
                flag = entity.hurt(ModDamageSource.indirectFrost(this, entity1), (float) ((LivingEntity) entity1).getAttributeValue(Attributes.ATTACK_DAMAGE));
                this.doEnchantDamageEffects((LivingEntity)entity1, entity);
            }
            if (flag && entity instanceof LivingEntity){
                LivingEntity livingEntity = (LivingEntity) entity;
                if (!livingEntity.hasEffect(Effects.MOVEMENT_SLOWDOWN)) {
                    livingEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 300));
                } else {
                    if (this.random.nextFloat() <= 0.01F) {
                        EffectsUtil.amplifyEffect(livingEntity, Effects.MOVEMENT_SLOWDOWN, 300);
                    } else {
                        EffectsUtil.resetDuration(livingEntity, Effects.MOVEMENT_SLOWDOWN, 300);
                    }
                }
            }

        }
    }

    protected void onHitBlock(BlockRayTraceResult p_230299_1_) {
        super.onHitBlock(p_230299_1_);
        if (!this.level.isClientSide) {
            Entity entity = this.getOwner();
            if (!(entity instanceof MobEntity) || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.getEntity())) {
                BlockPos blockpos = p_230299_1_.getBlockPos().relative(p_230299_1_.getDirection());
                BlockState blockstate = Blocks.SNOW.defaultBlockState();
                if (this.level.isEmptyBlock(blockpos) && blockstate.canSurvive(this.level, blockpos)) {
                    this.level.setBlockAndUpdate(blockpos, blockstate);
                }
            }

        }
    }

    protected void onHit(RayTraceResult pResult) {
        super.onHit(pResult);
        this.playSound(SoundEvents.SNOW_BREAK, 1.0F, 1.0F);
        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)102);
            this.remove();
        }

    }

    protected IParticleData getTrailParticle() {
        return ModParticleTypes.WHITE_EFFECT.get();
    }

    public boolean isPickable() {
        return false;
    }

    protected boolean shouldBurn() {
        return false;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte pId) {
        if (pId == 102) {

            for(int i = 0; i < 16; ++i) {
                this.level.addParticle(ModParticleTypes.WHITE_EFFECT.get(), this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
