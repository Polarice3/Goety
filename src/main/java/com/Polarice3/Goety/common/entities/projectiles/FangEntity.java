package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class FangEntity extends Entity {
    private static final DataParameter<Boolean> ABSORBING = EntityDataManager.defineId(FangEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> TOTEM = EntityDataManager.defineId(FangEntity.class, DataSerializers.BOOLEAN);
    private int warmupDelayTicks;
    private boolean sentSpikeEvent;
    private int lifeTicks = 22;
    private boolean clientSideAttackStarted;
    private int damage = 0;
    private int burning = 0;
    private int soulEater = 0;
    private LivingEntity owner;
    private UUID ownerUUID;

    public FangEntity(EntityType<? extends FangEntity> p_i50170_1_, World p_i50170_2_) {
        super(p_i50170_1_, p_i50170_2_);
    }

    public FangEntity(World world, double pPosX, double pPosY, double pPosZ, float pYRot, int pWarmUp, LivingEntity owner) {
        this(ModEntityType.FANG.get(), world);
        this.warmupDelayTicks = pWarmUp;
        this.setOwner(owner);
        this.yRot = pYRot * (180F / (float)Math.PI);
        this.setPos(pPosX, pPosY, pPosZ);
    }

    public FangEntity(World world, double pPosX, double pPosY, double pPosZ, float pYRot, int pWarmUp, int damage, int burning, int soulEater, LivingEntity owner) {
        this(ModEntityType.FANG.get(), world);
        this.warmupDelayTicks = pWarmUp;
        this.setOwner(owner);
        this.yRot = pYRot * (180F / (float)Math.PI);
        this.damage = damage;
        this.burning = burning;
        this.soulEater = soulEater;
        this.setTotemSpawned(true);
        this.setPos(pPosX, pPosY, pPosZ);
    }

    protected void defineSynchedData() {
        this.entityData.define(ABSORBING, false);
        this.entityData.define(TOTEM, false);
    }

    public boolean isAbsorbing() {
        return this.entityData.get(ABSORBING);
    }

    public void setAbsorbing(boolean absorbing) {
        this.entityData.set(ABSORBING, absorbing);
    }

    public boolean isTotemSpawned() {
        return this.entityData.get(TOTEM);
    }

    public void setTotemSpawned(boolean totemSpawned) {
        this.entityData.set(TOTEM, totemSpawned);
    }

    public int getSoulEater(){
        return this.soulEater;
    }

    public void setOwner(@Nullable LivingEntity p_190549_1_) {
        this.owner = p_190549_1_;
        this.ownerUUID = p_190549_1_ == null ? null : p_190549_1_.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.level).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }

        return this.owner;
    }

    protected void readAdditionalSaveData(CompoundNBT pCompound) {
        this.warmupDelayTicks = pCompound.getInt("Warmup");
        if (pCompound.contains("Damage")){
            this.damage = pCompound.getInt("Damage");
        }
        if (pCompound.contains("Burning")){
            this.burning = pCompound.getInt("Burning");
        }
        if (pCompound.contains("SoulEater")){
            this.soulEater = pCompound.getInt("SoulEater");
        }
        if (pCompound.contains("Absorbing")){
            this.setAbsorbing(pCompound.getBoolean("Absorbing"));
        }
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }

    }

    protected void addAdditionalSaveData(CompoundNBT pCompound) {
        pCompound.putInt("Warmup", this.warmupDelayTicks);
        if (this.isTotemSpawned()){
            pCompound.putInt("Damage", this.damage);
            pCompound.putInt("Burning", this.burning);
            pCompound.putInt("SoulEater", this.soulEater);
        }
        if (this.isAbsorbing()){
            pCompound.putBoolean("Absorbing", this.isAbsorbing());
        }
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }

    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (this.clientSideAttackStarted) {
                --this.lifeTicks;
                if (this.lifeTicks == 14) {
                    for(int i = 0; i < 12; ++i) {
                        double d0 = this.getX() + (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.getBbWidth() * 0.5D;
                        double d1 = this.getY() + 0.05D + this.random.nextDouble();
                        double d2 = this.getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.getBbWidth() * 0.5D;
                        double d3 = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        double d4 = 0.3D + this.random.nextDouble() * 0.3D;
                        double d5 = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        this.level.addParticle(ParticleTypes.CRIT, d0, d1 + 1.0D, d2, d3, d4, d5);
                    }
                }
            }
        } else if (--this.warmupDelayTicks < 0) {
            if (this.warmupDelayTicks == -8) {
                for(LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.2D, 0.0D, 0.2D))) {
                    this.dealDamageTo(livingentity);
                }
            }

            if (!this.sentSpikeEvent) {
                this.level.broadcastEntityEvent(this, (byte)4);
                this.sentSpikeEvent = true;
            }

            if (--this.lifeTicks < 0) {
                this.remove();
            }
        }

    }

    private void dealDamageTo(LivingEntity target) {
        LivingEntity livingentity = this.getOwner();
        float baseDamage = SpellConfig.FangDamage.get().floatValue() * SpellConfig.SpellDamageMultiplier.get();
        if (target.isAlive() && !target.isInvulnerable() && target != livingentity) {
            if (livingentity == null) {
                target.hurt(DamageSource.MAGIC, baseDamage);
            } else {
                if (target.isAlliedTo(livingentity)){
                    return;
                }
                if (livingentity.isAlliedTo(target)) {
                    return;
                }
                if (livingentity instanceof PlayerEntity){
                    PlayerEntity player = (PlayerEntity) livingentity;
                    if (this.isTotemSpawned()){
                        target.hurt(DamageSource.indirectMagic(this, livingentity), baseDamage + damage);
                        if (burning > 0){
                            target.setSecondsOnFire(5 * burning);
                        }
                    } else {
                        float enchantment = 0;
                        int burning = 0;
                        if (WandUtil.enchantedFocus(player)) {
                            enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                            burning = WandUtil.getLevels(ModEnchantments.BURNING.get(), player);
                        }
                        if (target.hurt(DamageSource.indirectMagic(this, livingentity), baseDamage + enchantment)){
                            int soulEater = MathHelper.clamp(this.getSoulEater(), 0, 10);
                            SEHelper.increaseSouls(player, SpellConfig.FangGainSouls.get() * soulEater);
                            if (burning > 0){
                                target.setSecondsOnFire(5 * burning);
                            }
                        }
                    }
                } else {
                    target.hurt(DamageSource.indirectMagic(this, livingentity), baseDamage);
                }
            }
        }
    }

    public void handleEntityEvent(byte pId) {
        super.handleEntityEvent(pId);
        if (pId == 4) {
            this.clientSideAttackStarted = true;
            if (!this.isSilent()) {
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.EVOKER_FANGS_ATTACK, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.2F + 0.85F, false);
            }
        }

    }

    public float getAnimationProgress(float pPartialTicks) {
        if (!this.clientSideAttackStarted) {
            return 0.0F;
        } else {
            int i = this.lifeTicks - 2;
            return i <= 0 ? 1.0F : 1.0F - ((float)i - pPartialTicks) / 20.0F;
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
