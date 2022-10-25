package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.EntityFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WitchGaleEntity extends DamagingProjectileEntity {
    private static final DataParameter<Boolean> DATA_UPGRADED = EntityDataManager.defineId(WitchGaleEntity.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.defineId(WitchGaleEntity.class, DataSerializers.OPTIONAL_UUID);
    private int lifespan;
    private int totallife;

    public WitchGaleEntity(EntityType<? extends DamagingProjectileEntity> p_i50173_1_, World p_i50173_2_) {
        super(p_i50173_1_, p_i50173_2_);
        this.noPhysics = false;
        this.lifespan = 0;
        this.totallife = 60;
    }

    public WitchGaleEntity(World p_i1794_1_, LivingEntity p_i1794_2_, double p_i1794_3_, double p_i1794_5_, double p_i1794_7_) {
        super(ModEntityType.WITCHGALE.get(), p_i1794_2_, p_i1794_3_, p_i1794_5_, p_i1794_7_, p_i1794_1_);
    }

    @OnlyIn(Dist.CLIENT)
    public WitchGaleEntity(World p_i1795_1_, double p_i1795_2_, double p_i1795_4_, double p_i1795_6_, double p_i1795_8_, double p_i1795_10_, double p_i1795_12_) {
        super(ModEntityType.WITCHGALE.get(), p_i1795_2_, p_i1795_4_, p_i1795_6_, p_i1795_8_, p_i1795_10_, p_i1795_12_, p_i1795_1_);
    }

    protected float getInertia() {
        return 0.75F;
    }

    public int getTotallife() {
        return totallife;
    }

    public void setTotallife(int totallife) {
        this.totallife = totallife;
    }

    public LivingEntity getTrueOwner() {
        try {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : EntityFinder.getLivingEntityByUuiD(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    @Nullable
    public UUID getOwnerId() {
        return this.entityData.get(OWNER_UNIQUE_ID).orElse((UUID)null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    public void tick() {
        super.tick();
        if (this.lifespan < getTotallife()){
            ++this.lifespan;
        } else {
            this.remove();
        }
        int enchantment = 0;
        int duration = 1;
        if (this.getTrueOwner() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) this.getTrueOwner();
            if (WandUtil.enchantedFocus(player)){
                enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                duration = WandUtil.getLevels(ModEnchantments.DURATION.get(), player) + 1;
            }
        }
        List<LivingEntity> targets = new ArrayList<>();
        for (LivingEntity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(AreaofEffect()))) {
            if (this.getTrueOwner() != null) {
                if (entity != this.getTrueOwner() && !entity.isAlliedTo(this.getTrueOwner()) && !this.getTrueOwner().isAlliedTo(entity)) {
                    targets.add(entity);
                }
            } else {
                targets.add(entity);
            }
        }
        if (!targets.isEmpty()){
            for (LivingEntity livingEntity : targets) {
                if (MobUtil.validEntity(livingEntity) && livingEntity != this.getTrueOwner()) {
                    if (!this.level.isClientSide) {
                        if (this.isUpgraded()) {
                            livingEntity.hurt(DamageSource.indirectMagic(this, this.getTrueOwner()), 2.0F + enchantment);
                        }
                        livingEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200 * duration, enchantment));
                        livingEntity.addEffect(new EffectInstance(Effects.WEAKNESS, 200 * duration, enchantment));
                        livingEntity.addEffect(new EffectInstance(Effects.POISON, 200 * duration, enchantment));
                        this.suckInMobs(livingEntity);
                    }
                }
            }
        }
    }

    private void suckInMobs(LivingEntity livingEntity) {
        Vector3d vector3d = new Vector3d(this.getX() + 0.5, this.getY() + 0.5, this.getZ() + 0.5);
        Vector3d vector3d1 = vector3d.subtract(livingEntity.position()).normalize();

        MobUtil.push(livingEntity, vector3d1.x, 0.2, vector3d1.z);
    }

    public double AreaofEffect(){
        if (this.isUpgraded()){
            return 2.0D;
        } else {
            return 1.0D;
        }
    }


    public boolean isOnFire() {
        return false;
    }

    public boolean isPickable() {
        return false;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    protected void defineSynchedData() {
        this.entityData.define(DATA_UPGRADED, false);
        this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
    }


    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        UUID uuid;
        if (compound.hasUUID("Owner")) {
            uuid = compound.getUUID("Owner");
        } else {
            String s = compound.getString("Owner");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setOwnerId(uuid);
            } catch (Throwable ignored) {
            }
        }

    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (this.getOwnerId() != null) {
            compound.putUUID("Owner", this.getOwnerId());
        }

    }

    public boolean isUpgraded() {
        return this.entityData.get(DATA_UPGRADED);
    }

    public void setUpgraded(boolean pInvulnerable) {
        this.entityData.set(DATA_UPGRADED, pInvulnerable);
    }

    protected IParticleData getTrailParticle() {
        return ParticleTypes.WITCH;
    }

    public EntitySize getDimensions(Pose pPose) {
        if (this.isUpgraded()){
            return super.getDimensions(pPose).scale(2.0F);
        } else {
            return super.getDimensions(pPose).scale(1.0F);
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
