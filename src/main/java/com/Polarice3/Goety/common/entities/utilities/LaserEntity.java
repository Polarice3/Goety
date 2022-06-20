package com.Polarice3.Goety.common.entities.utilities;

import com.Polarice3.Goety.common.entities.hostile.SkullLordEntity;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.EntityFinder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class LaserEntity extends MobEntity {
    private static final DataParameter<Optional<UUID>> SKULL_LORD = EntityDataManager.defineId(LaserEntity.class, DataSerializers.OPTIONAL_UUID);
    private int duration = 600;
    private int durationOnUse;

    public LaserEntity(EntityType<? extends MobEntity> p_i48576_1_, World p_i48576_2_) {
        super(p_i48576_1_, p_i48576_2_);
        this.maxUpStep = 1.0F;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23F);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SKULL_LORD, Optional.empty());
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        this.duration = pCompound.getInt("Duration");
        this.durationOnUse = pCompound.getInt("DurationOnUse");

        UUID uuid;
        if (pCompound.hasUUID("skullLord")) {
            uuid = pCompound.getUUID("skullLord");
        } else {
            String s = pCompound.getString("skullLord");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setSkullLordUUID(uuid);
            } catch (Throwable ignored) {
            }
        }

    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        pCompound.putInt("Duration", this.duration);
        pCompound.putInt("DurationOnUse", this.durationOnUse);

        if (this.getSkullLordUUID() != null) {
            pCompound.putUUID("skullLord", this.getSkullLordUUID());
        }
    }

    @Nullable
    public SkullLordEntity getSkullLord() {
        try {
            UUID uuid = this.getSkullLordUUID();
            if (uuid != null){
                if (EntityFinder.getLivingEntityByUuiD(uuid) instanceof SkullLordEntity){
                    return (SkullLordEntity) EntityFinder.getLivingEntityByUuiD(uuid);
                }
            }
            return null;
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    @Nullable
    public UUID getSkullLordUUID() {
        return this.entityData.get(SKULL_LORD).orElse(null);
    }

    public void setSkullLordUUID(UUID uuid){
        this.entityData.set(SKULL_LORD, Optional.ofNullable(uuid));
    }

    public void setSkullLord(SkullLordEntity skullLord){
        this.setSkullLordUUID(skullLord.getUUID());
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int durationIn) {
        this.duration = durationIn;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getSkullLord() == null || this.getSkullLord().isDeadOrDying() || this.getSkullLord().isInvulnerable()){
            if (this.tickCount % 20 == 0){
                this.remove();
            }
        } else {
            ModifiableAttributeInstance gravity = this.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
            if (gravity != null){
                gravity.setBaseValue(0.8D);
            }
            if (this.getSkullLord().isHalfHealth()){
                Objects.requireNonNull(this.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.3D);
                if (this.level.getDifficulty() == Difficulty.HARD){
                    this.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 20, 1, false, false));
                }
            }
            if (this.getSkullLord().getTarget() != null){
                this.setTarget(this.getSkullLord().getTarget());
            } else {
                if (this.tickCount % 20 == 0){
                    this.remove();
                }
            }
            if (this.getTarget() != null){
                if (this.tickCount >= 20) {
                    if (this.tickCount % 2 == 0) {
                        this.level.explode(this, this.getX(), this.getY(), this.getZ(), 0.25F, Explosion.Mode.NONE);
                    }
                    this.moveControl.setWantedPosition(this.getTarget().getX(), this.getY(), this.getTarget().getZ(), 1.0F);
                    for (LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2.0F))) {
                        if (livingEntity != this.getSkullLord() && livingEntity != this) {
                            if (this.getBoundingBox().intersects(livingEntity.getBoundingBox())) {
                                this.getTarget().hurt(DamageSource.indirectMagic(this, this.getSkullLord()), (float) this.getSkullLord().getAttributeBaseValue(Attributes.ATTACK_DAMAGE));
                                this.getTarget().addEffect(new EffectInstance(ModEffects.CURSED.get(), 100));
                            }
                        }
                    }
                }
            }
        }
        if (this.tickCount >= this.getDuration()) {
            if (this.getSkullLord() != null){
                this.getSkullLord().boneLordRegen = (50 + this.random.nextInt(100));
            }
            this.remove();
        }
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    public boolean isPushable() {
        return false;
    }

    protected void doPush(Entity pEntity) {
    }

    protected void pushEntities() {
    }

    protected boolean isAffectedByFluids() {
        return false;
    }

}
