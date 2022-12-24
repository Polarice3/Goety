package com.Polarice3.Goety.common.entities.hostile.cultists;

import com.Polarice3.Goety.init.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ThugEntity extends AbstractCultistEntity {
    protected static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.defineId(ThugEntity.class, DataSerializers.BYTE);
    private int attackTick;

    public ThugEntity(EntityType<? extends AbstractCultistEntity> type, World worldIn) {
        super(type, worldIn);
        this.maxUpStep = 1.0F;
        this.xpReward = 15;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0F, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("AttackTick", this.attackTick);
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.attackTick = compound.getInt("AttackTick");
    }

    private boolean getThugFlag(int mask) {
        int i = this.entityData.get(DATA_FLAGS_ID);
        return (i & mask) != 0;
    }

    private void setThugFlag(int mask, boolean value) {
        int i = this.entityData.get(DATA_FLAGS_ID);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(DATA_FLAGS_ID, (byte)(i & 255));
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.THUG_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.THUG_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.THUG_DEATH.get();
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(ModSounds.THUG_STEP.get(), 0.15F, 1.0F);
    }

    public boolean canBeLeashed(PlayerEntity player) {
        return false;
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return ModSounds.THUG_CELEBRATE.get();
    }

    public void aiStep() {
        super.aiStep();
        if (this.isAlive()) {
            if (this.isImmobile()) {
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);
            } else {
                double d0 = this.getTarget() != null ? 0.3D : 0.23D;
                double d1 = this.getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue();
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(MathHelper.lerp(0.1D, d1, d0));
            }
            if (this.level.isClientSide){
                if (this.isRaging()){
                    if (this.tickCount % 10 == 0) {
                        this.addParticlesAroundSelf(ParticleTypes.ANGRY_VILLAGER);
                    }
                }
            }
            if (this.getHealth() < this.getMaxHealth() / 1.5) {
                if (this.isAggressive() || this.getTarget() != null) {
                    this.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 20, 1));
                    this.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 20, 1));
                    if (this.tickCount % 20 == 0){
                        this.hurt(DamageSource.GENERIC, 1.0F);
                    }
                    this.setIsRaging(true);
                } else {
                    if (this.tickCount % 20 == 0) {
                        this.heal(2.0F);
                    }
                    this.setIsRaging(false);
                }

            }
            if (this.attackTick > 0) {
                --this.attackTick;
            }
            if (getHorizontalDistanceSqr(this.getDeltaMovement()) > (double)2.5000003E-7F && this.random.nextInt(5) == 0) {
                int i = MathHelper.floor(this.getX());
                int j = MathHelper.floor(this.getY() - (double)0.2F);
                int k = MathHelper.floor(this.getZ());
                BlockPos pos = new BlockPos(i, j, k);
                BlockState blockstate = this.level.getBlockState(pos);
                if (!blockstate.isAir(this.level, pos)) {
                    this.level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockstate).setPos(pos), this.getX() + ((double)this.random.nextFloat() - 0.5D) * (double)this.getBbWidth(), this.getY() + 0.1D, this.getZ() + ((double)this.random.nextFloat() - 0.5D) * (double)this.getBbWidth(), 4.0D * ((double)this.random.nextFloat() - 0.5D), 0.5D, ((double)this.random.nextFloat() - 0.5D) * 4.0D);
                }
            }
            if (this.isAggressive()) {
                if (this.horizontalCollision && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                    boolean flag = false;
                    AxisAlignedBB axisalignedbb = this.getBoundingBox().inflate(0.2D);

                    for (BlockPos blockpos : BlockPos.betweenClosed(MathHelper.floor(axisalignedbb.minX), MathHelper.floor(axisalignedbb.minY), MathHelper.floor(axisalignedbb.minZ), MathHelper.floor(axisalignedbb.maxX), MathHelper.floor(axisalignedbb.maxY), MathHelper.floor(axisalignedbb.maxZ))) {
                        BlockState blockstate = this.level.getBlockState(blockpos);
                        Block block = blockstate.getBlock();
                        if ((block instanceof LeavesBlock || blockstate.getMaterial() == Material.WOOD) && !blockstate.hasTileEntity()) {
                            flag = this.level.destroyBlock(blockpos, true, this) || flag;
                        }
                    }

                    if (!flag && this.onGround) {
                        this.jumpFromGround();
                    }
                }
            }
        }
    }

    public boolean isRaging() {
        return this.getThugFlag(1);
    }

    public void setIsRaging(boolean charging) {
        this.setThugFlag(1, charging);
    }

    @OnlyIn(Dist.CLIENT)
    protected void addParticlesAroundSelf(IParticleData pParticleData) {
        for(int i = 0; i < 5; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(pParticleData, this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), d0, d1, d2);
        }

    }

    private float getAttackDamage() {
        return (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 4) {
            this.attackTick = 10;
            this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        }
        super.handleEntityEvent(id);
    }

    @OnlyIn(Dist.CLIENT)
    public int getAttackTick() {
        return this.attackTick;
    }

    public boolean doHurtTarget(Entity pEntity) {
        this.attackTick = 10;
        this.level.broadcastEntityEvent(this, (byte)4);
        float f = this.getAttackDamage();
        float f1 = (int)f > 0 ? f / 2.0F + (float)this.random.nextInt((int)f) : f;
        boolean flag = pEntity.hurt(DamageSource.mobAttack(this), f1);
        if (flag) {
            pEntity.setDeltaMovement(pEntity.getDeltaMovement().add(0.0D, (double)0.4F, 0.0D));
            this.doEnchantDamageEffects(this, pEntity);
        }

        this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        return flag;
    }

    protected PathNavigator createNavigation(World pLevel) {
        return new ThugEntity.Navigator(this, pLevel);
    }

    static class Navigator extends GroundPathNavigator {
        public Navigator(MobEntity p_i50754_1_, World p_i50754_2_) {
            super(p_i50754_1_, p_i50754_2_);
        }

        protected PathFinder createPathFinder(int p_179679_1_) {
            this.nodeEvaluator = new ThugEntity.Processor();
            return new PathFinder(this.nodeEvaluator, p_179679_1_);
        }
    }

    static class Processor extends WalkNodeProcessor {
        private Processor() {
        }

        protected PathNodeType evaluateBlockPathType(IBlockReader pLevel, boolean pCanOpenDoors, boolean pCanEnterDoors, BlockPos pPos, PathNodeType pNodeType) {
            return pNodeType == PathNodeType.LEAVES || pNodeType == PathNodeType.DOOR_WOOD_CLOSED || pNodeType == PathNodeType.FENCE ? PathNodeType.OPEN : super.evaluateBlockPathType(pLevel, pCanOpenDoors, pCanEnterDoors, pPos, pNodeType);
        }
    }

}
