package com.Polarice3.Goety.common.entities.hostile.illagers;

import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class TormentorEntity extends AbstractIllagerEntity {
    protected static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.defineId(TormentorEntity.class, DataSerializers.BYTE);
    private MobEntity owner;
    @Nullable
    private BlockPos boundOrigin;
    private boolean hasLimitedLife;
    private int limitedLifeTicks;

    public TormentorEntity(EntityType<? extends AbstractIllagerEntity> p_i50190_1_, World p_i50190_2_) {
        super(p_i50190_1_, p_i50190_2_);
        this.moveControl = new MobUtil.MoveHelperController(this);
        this.xpReward = 6;
    }

    public void move(MoverType typeIn, Vector3d pos) {
        super.move(typeIn, pos);
        this.checkInsideBlocks();
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(4, new TormentorEntity.ChargeAttackGoal());
        this.goalSelector.addGoal(8, new TormentorEntity.MoveRandomGoal());
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new TormentorEntity.CopyOwnerTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
        if (this.hasLimitedLife && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.hurt(DamageSource.STARVE, 1.0F);
        }
        if (this.hasActiveRaid()){
            if (this.getOwner() == null || this.getOwner().isDeadOrDying()){
                if (this.tickCount % 20 == 0) {
                    this.hurt(DamageSource.STARVE, 5.0F);
                }
            }
        }
        if (!this.isCharging()){
            this.addEffect(new EffectInstance(Effects.INVISIBILITY, 20, 0, false, false));
        } else {
            this.removeEffect(Effects.INVISIBILITY);
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
    }

    @Override
    public void applyRaidBuffs(int pWave, boolean p_213660_2_) {

    }

    private boolean getVexFlag(int mask) {
        int i = this.entityData.get(DATA_FLAGS_ID);
        return (i & mask) != 0;
    }

    private void setVexFlag(int mask, boolean value) {
        int i = this.entityData.get(DATA_FLAGS_ID);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(DATA_FLAGS_ID, (byte)(i & 255));
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("BoundX")) {
            this.boundOrigin = new BlockPos(pCompound.getInt("BoundX"), pCompound.getInt("BoundY"), pCompound.getInt("BoundZ"));
        }

        if (pCompound.contains("LifeTicks")) {
            this.setLimitedLife(pCompound.getInt("LifeTicks"));
        }

    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.boundOrigin != null) {
            pCompound.putInt("BoundX", this.boundOrigin.getX());
            pCompound.putInt("BoundY", this.boundOrigin.getY());
            pCompound.putInt("BoundZ", this.boundOrigin.getZ());
        }

        if (this.hasLimitedLife) {
            pCompound.putInt("LifeTicks", this.limitedLifeTicks);
        }

    }

    public MobEntity getOwner() {
        return this.owner;
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos pBoundOrigin) {
        this.boundOrigin = pBoundOrigin;
    }

    public boolean isCharging() {
        return this.getVexFlag(1);
    }

    public void setIsCharging(boolean charging) {
        this.setVexFlag(1, charging);
    }

    public void setOwner(MobEntity pOwner) {
        this.owner = pOwner;
    }

    public void setLimitedLife(int pLimitedLifeTicks) {
        this.hasLimitedLife = true;
        this.limitedLifeTicks = pLimitedLifeTicks;
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.TORMENTOR_AMBIENT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.TORMENTOR_DEATH.get();
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.TORMENTOR_HURT.get();
    }

    public float getBrightness() {
        return 1.0F;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld pLevel, DifficultyInstance pDifficulty, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
        this.populateDefaultEquipmentSlots(pDifficulty);
        this.populateDefaultEquipmentEnchantments(pDifficulty);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return ModSounds.TORMENTOR_CELEBRATE.get();
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance pDifficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
        this.setDropChance(EquipmentSlotType.MAINHAND, 0.0F);
    }

    public boolean canBeLeader() {
        return false;
    }

    class ChargeAttackGoal extends Goal {
        public ChargeAttackGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            if (TormentorEntity.this.getTarget() != null && !TormentorEntity.this.getMoveControl().hasWanted() && TormentorEntity.this.random.nextInt(7) == 0) {
                return TormentorEntity.this.distanceToSqr(TormentorEntity.this.getTarget()) > 4.0D;
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return TormentorEntity.this.getMoveControl().hasWanted() && TormentorEntity.this.isCharging() && TormentorEntity.this.getTarget() != null && TormentorEntity.this.getTarget().isAlive();
        }

        public void start() {
            LivingEntity livingentity = TormentorEntity.this.getTarget();
            assert livingentity != null;
            Vector3d vector3d = livingentity.position();
            TormentorEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            TormentorEntity.this.setIsCharging(true);
            TormentorEntity.this.playSound(ModSounds.TORMENTOR_CHARGE.get(), 1.0F, 1.0F);
        }

        public void stop() {
            TormentorEntity.this.setIsCharging(false);
        }

        public void tick() {
            LivingEntity livingentity = TormentorEntity.this.getTarget();
            assert livingentity != null;
            if (TormentorEntity.this.getBoundingBox().inflate(1.0F).intersects(livingentity.getBoundingBox())) {
                TormentorEntity.this.doHurtTarget(livingentity);
                TormentorEntity.this.setIsCharging(false);
            } else {
                double d0 = TormentorEntity.this.distanceToSqr(livingentity);
                if (d0 < 9.0D) {
                    Vector3d vector3d = livingentity.getEyePosition(1.0F);
                    TormentorEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
                }
            }

        }
    }

    class CopyOwnerTargetGoal extends TargetGoal {
        private final EntityPredicate copyOwnerTargeting = (new EntityPredicate()).allowUnseeable().ignoreInvisibilityTesting();

        public CopyOwnerTargetGoal(CreatureEntity p_i47231_2_) {
            super(p_i47231_2_, false);
        }

        public boolean canUse() {
            return TormentorEntity.this.owner != null && TormentorEntity.this.owner.getTarget() != null && this.canAttack(TormentorEntity.this.owner.getTarget(), this.copyOwnerTargeting);
        }

        public void start() {
            TormentorEntity.this.setTarget(TormentorEntity.this.owner.getTarget());
            super.start();
        }
    }

    class MoveRandomGoal extends Goal {
        public MoveRandomGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            return !TormentorEntity.this.getMoveControl().hasWanted() && TormentorEntity.this.random.nextInt(7) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = TormentorEntity.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = TormentorEntity.this.blockPosition();
            }

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.offset(TormentorEntity.this.random.nextInt(15) - 7, TormentorEntity.this.random.nextInt(11) - 5, TormentorEntity.this.random.nextInt(15) - 7);
                if (TormentorEntity.this.level.isEmptyBlock(blockpos1)) {
                    TormentorEntity.this.moveControl.setWantedPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
                    if (TormentorEntity.this.getTarget() == null) {
                        TormentorEntity.this.getLookControl().setLookAt((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }
}
