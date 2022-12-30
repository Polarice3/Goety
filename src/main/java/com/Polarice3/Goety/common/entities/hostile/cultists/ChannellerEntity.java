package com.Polarice3.Goety.common.entities.hostile.cultists;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.common.entities.utilities.MagicBlastTrapEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.EntityFinder;
import com.Polarice3.Goety.utils.ServerParticleUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ChannellerEntity extends AbstractCultistEntity implements ICultist{
    private static final DataParameter<Boolean> IS_PRAYING = EntityDataManager.defineId(ChannellerEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Optional<UUID>> ALLY_UUID = EntityDataManager.defineId(ChannellerEntity.class, DataSerializers.OPTIONAL_UUID);
    private int prayingTick;

    public ChannellerEntity(EntityType<? extends ChannellerEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.CHANNELLER_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.CHANNELLER_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.CHANNELLER_DEATH.get();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_PRAYING, false);
        this.entityData.define(ALLY_UUID, Optional.empty());
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        UUID uuid;
        if (compound.hasUUID("ally")) {
            uuid = compound.getUUID("ally");
        } else {
            String s = compound.getString("ally");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setAllyUUID(uuid);
            } catch (Throwable ignored) {
            }
        }
        this.prayingTick = compound.getInt("prayingTick");
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (this.getAllyUUID() != null) {
            compound.putUUID("ally", this.getAllyUUID());
        }
        compound.putInt("prayingTick", this.prayingTick);
    }

    @Nullable
    public MobEntity getAlly() {
        try {
            UUID uuid = this.getAllyUUID();
            if (uuid != null){
                if (EntityFinder.getLivingEntityByUuiD(uuid) instanceof MobEntity){
                    return (MobEntity) EntityFinder.getLivingEntityByUuiD(uuid);
                }
            }
            return null;
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    @Nullable
    public UUID getAllyUUID() {
        return this.entityData.get(ALLY_UUID).orElse(null);
    }

    public void setAllyUUID(UUID uuid){
        this.entityData.set(ALLY_UUID, Optional.ofNullable(uuid));
    }

    public void setAlly(MobEntity mob){
        this.setAllyUUID(mob.getUUID());
    }

    public boolean hurt(DamageSource source, float amount){
        if (!this.isPraying()) {
            return super.hurt(source, amount);
        } else {
            return super.hurt(source, amount/2);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public ArmPose getArmPose() {
        if (this.prayingTick > 0) {
            return ArmPose.SPELLCASTING;
        } else {
            return ChannellerEntity.ArmPose.CROSSED;
        }
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        ILivingEntityData ilivingentitydata = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        return ilivingentitydata;
    }

    public boolean canBeLeashed(PlayerEntity player) {
        return false;
    }

    public void setIsPraying(boolean praying) {
        this.entityData.set(IS_PRAYING, praying);
    }

    public boolean isPraying() {
        return this.entityData.get(IS_PRAYING);
    }

    public void aiStep() {
        super.aiStep();
        if (this.level.isAreaLoaded(this.blockPosition(), 2)) {
            if (this.getAlly() != null) {
                if (this.prayingTick < 20) {
                    ++this.prayingTick;
                } else {
                    if (this.distanceTo(this.getAlly()) >= 12.0D) {
                        Vector3d vector3d = getAlly().position();
                        this.getNavigation().moveTo(vector3d.x, vector3d.y, vector3d.z, 1.0D);
                    } else {
                        this.setIsPraying(true);
                        this.getLookControl().setLookAt(this.getAlly(), (float) this.getMaxHeadYRot(), (float) this.getMaxHeadXRot());
                        if (!this.level.isClientSide) {
                            this.getNavigation().stop();
                            this.noActionTime = 0;
                            this.getAlly().setTarget(this.getTarget());
                            this.getAlly().addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 60, 1));
                            this.getAlly().addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 60, 1));
                            this.getAlly().addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 60, 1));
                            this.getAlly().setPersistenceRequired();
                            if (this.getHealth() < this.getMaxHealth()) {
                                if (this.tickCount % 10 == 0) {
                                    this.getAlly().hurt(DamageSource.STARVE, 2.0F);
                                    this.heal(2.0F);
                                }
                            }
                            if (this.getTarget() != null && this.canSee(this.getTarget())) {
                                double d = this.distanceToSqr(this.getTarget());
                                float f = MathHelper.sqrt(MathHelper.sqrt(d)) * 0.5F;
                                if (this.tickCount % 100 == 0) {
                                    this.playSound(SoundEvents.EVOKER_CAST_SPELL, 1.0F, 1.0F);
                                    double d0 = Math.min(this.getTarget().getY(), this.getY());
                                    double d1 = Math.max(this.getTarget().getY(), this.getY()) + 1.0D;
                                    spawnBlast(this, this.getTarget().getX(), this.getTarget().getZ(), d0, d1);
                                    for (int i = 0; i < 5; ++i) {
                                        float f1 = f + (float) i * (float) Math.PI * 0.4F;
                                        spawnBlast(this, this.getTarget().getX() + (double) MathHelper.cos(f1) * 1.5D, this.getTarget().getZ() + (double) MathHelper.sin(f1) * 1.5D, d0, d1);
                                    }
                                }
                            }
                        }
                    }
                    if (this.getAlly().isDeadOrDying()) {
                        this.setAllyUUID(null);
                        this.setIsPraying(false);
                    }
                }
            } else {
                List<MobEntity> list = this.level.getEntitiesOfClass(MobEntity.class, this.getBoundingBox().inflate(64.0D, 8.0D, 64.0D));
                if (!list.isEmpty()) {
                    for (MobEntity mob : list) {
                        if (mob.canChangeDimensions() && this.canSee(mob)) {
                            if (mob instanceof MonsterEntity && !(mob instanceof CreeperEntity) && !(mob instanceof ChannellerEntity)) {
                                this.setAlly(mob);
                            }
                            if (mob instanceof OwnedEntity) {
                                OwnedEntity ownedEntity = (OwnedEntity) mob;
                                if (ownedEntity.getTrueOwner() instanceof AbstractCultistEntity) {
                                    this.setAlly(mob);
                                }
                            }
                        }
                    }
                }
                this.prayingTick = 0;
                if (!this.level.isClientSide) {
                    ServerWorld serverWorld = (ServerWorld) this.level;
                    ServerParticleUtil.gatheringParticles(ModParticleTypes.FLAME_GATHER.get(), this, serverWorld);
                    if (this.tickCount % 100 == 0 && !this.isDeadOrDying()) {
                        OwnedEntity minion = null;
                        switch (this.random.nextInt(3)) {
                            case (0):
                                minion = ModEntityType.ZOMBIE_VILLAGER_MINION.get().create(this.level);
                                break;
                            case (1):
                                minion = ModEntityType.SKELETON_VILLAGER_MINION.get().create(this.level);
                                break;
                            case (2):
                                minion = ModEntityType.ZPIGLIN_MINION.get().create(this.level);
                                break;
                        }
                        if (minion != null) {
                            minion.setTrueOwner(this);
                            minion.setPos(this.getX(), this.getY(), this.getZ());
                            minion.spawnAnim();
                            minion.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                            serverWorld.addFreshEntity(minion);
                        }
                        for (int k = 0; k < 60; ++k) {
                            float f2 = random.nextFloat() * 4.0F;
                            float f1 = random.nextFloat() * ((float) Math.PI * 2F);
                            double d1 = MathHelper.cos(f1) * f2;
                            double d2 = 0.01D + random.nextDouble() * 0.5D;
                            double d3 = MathHelper.sin(f1) * f2;
                            serverWorld.sendParticles(ParticleTypes.SMOKE, this.getX() + d1 * 0.1D, this.getY() + 0.3D, this.getZ() + d3 * 0.1D, 0, d1, d2, d3, 0.25F);
                        }
                    }
                }
            }
        }
    }

    public void spawnBlast(LivingEntity livingEntity, double pPosX, double pPosZ, double PPPosY, double pOPosY) {
        BlockPos blockpos = new BlockPos(pPosX, pOPosY, pPosZ);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = livingEntity.level.getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(livingEntity.level, blockpos1, Direction.UP)) {
                if (!livingEntity.level.isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = livingEntity.level.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(livingEntity.level, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= MathHelper.floor(PPPosY) - 1);

        if (flag) {
            MagicBlastTrapEntity fireBlastTrap = new MagicBlastTrapEntity(livingEntity.level, pPosX, (double)blockpos.getY() + d0, pPosZ);
            fireBlastTrap.setOwner(livingEntity);
            livingEntity.level.addFreshEntity(fireBlastTrap);
        }

    }

    protected float getDamageAfterMagicAbsorb(DamageSource source, float damage) {
        damage = super.getDamageAfterMagicAbsorb(source, damage);
        if (source.getEntity() == this) {
            damage = 0.0F;
        }

        if (source.isMagic()) {
            damage = (float)((double)damage * 0.15D);
        }

        return damage;
    }

    @Override
    public void applyRaidBuffs(int wave, boolean p_213660_2_) {
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return ModSounds.CHANNELLER_CELEBRATE.get();
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 1.62F;
    }

    public boolean canBeLeader() {
        return false;
    }

}
