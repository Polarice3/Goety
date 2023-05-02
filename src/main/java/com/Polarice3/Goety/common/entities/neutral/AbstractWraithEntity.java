package com.Polarice3.Goety.common.entities.neutral;

import com.Polarice3.Goety.AttributesConfig;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.ai.FloatSwimGoal;
import com.Polarice3.Goety.common.entities.ai.SummonTargetGoal;
import com.Polarice3.Goety.common.entities.ally.SummonedEntity;
import com.Polarice3.Goety.common.entities.hostile.WraithEntity;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.pathfinding.Path;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class AbstractWraithEntity extends SummonedEntity implements ICustomAttributes{
    private static final DataParameter<Integer> BURNING_LEVEL = EntityDataManager.defineId(AbstractWraithEntity.class, DataSerializers.INT);
    private static final DataParameter<Byte> FLAGS = EntityDataManager.defineId(AbstractWraithEntity.class, DataSerializers.BYTE);
    public int fireTick;
    public int firingTick;
    public int firingTick2;
    public int teleportCooldown;
    public int teleportTime = 20;
    public int teleportTime2;
    public double prevX;
    public double prevY;
    public double prevZ;

    public AbstractWraithEntity(EntityType<? extends SummonedEntity> p_i48553_1_, World p_i48553_2_) {
        super(p_i48553_1_, p_i48553_2_);
        ICustomAttributes.applyAttributesForEntity(p_i48553_1_, this);
        this.moveControl = new MobUtil.WraithMoveController(this);
        this.maxUpStep = 1.0F;
        this.fireTick = 0;
        this.firingTick = 0;
        this.firingTick2 = 0;
        this.teleportTime2 = 0;
        this.teleportCooldown = 0;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatSwimGoal(this));
        this.goalSelector.addGoal(9, new WraithLookGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new WraithLookGoal(this, MobEntity.class, 8.0F));
        this.goalSelector.addGoal(10, new WraithLookRandomlyGoal(this));
    }

    public void targetSelectGoal(){
        this.targetSelector.addGoal(1, new SummonTargetGoal<>(this, false, false));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, AttributesConfig.WraithHealth.get())
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, AttributesConfig.WraithDamage.get());
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FLAGS, (byte)0);
        this.entityData.define(BURNING_LEVEL, 0);
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("fireTick", this.fireTick);
        pCompound.putInt("firingTick", this.firingTick);
        pCompound.putInt("firingTick2", this.firingTick2);
        pCompound.putInt("teleportTime2", this.teleportTime2);
        pCompound.putInt("teleportCooldown", this.teleportCooldown);
        pCompound.putInt("burningLevel", this.getBurningLevel());
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.fireTick = pCompound.getInt("fireTick");
        this.firingTick = pCompound.getInt("firingTick");
        this.firingTick2 = pCompound.getInt("firingTick2");
        this.teleportTime2 = pCompound.getInt("teleportTime2");
        this.teleportCooldown = pCompound.getInt("teleportCooldown");
        this.setBurningLevel(pCompound.getInt("burningLevel"));
    }

    private boolean geFlags(int mask) {
        int i = this.entityData.get(FLAGS);
        return (i & mask) != 0;
    }

    private void setFlags(int mask, boolean value) {
        int i = this.entityData.get(FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(FLAGS, (byte)(i & 255));
    }

    public boolean isFiring() {
        return this.geFlags(1);
    }

    public void setIsFiring(boolean charging) {
        this.setFlags(1, charging);
    }

    public boolean isTeleporting() {
        return this.geFlags(2);
    }

    public void setIsTeleporting(boolean charging) {
        this.setFlags(2, charging);
    }

    public int getBurningLevel(){
        return this.entityData.get(BURNING_LEVEL);
    }

    public void setBurningLevel(int level){
        this.entityData.set(BURNING_LEVEL, level);
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.WRAITH_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.WRAITH_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.WRAITH_DEATH.get();
    }

    protected SoundEvent getStepSound() {
        return ModSounds.WRAITH_FLY.get();
    }

    protected SoundEvent getAttackSound(){
        return ModSounds.WRAITH_ATTACK.get();
    }

    protected SoundEvent getTeleportInSound(){
        return ModSounds.WRAITH_TELEPORT.get();
    }

    protected SoundEvent getTeleportOutSound(){
        return ModSounds.WRAITH_TELEPORT.get();
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        if (this.getStepSound() != null) {
            this.playSound(this.getStepSound(), 0.5F, 1.0F);
        }
    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    public boolean canBeAffected(EffectInstance potioneffectIn) {
        return potioneffectIn.getEffect() != ModEffects.HOSTED.get() && super.canBeAffected(potioneffectIn);
    }

    protected boolean isAffectedByFluids() {
        return false;
    }

    public double getFollowRange(){
        return this.getAttributeValue(Attributes.FOLLOW_RANGE);
    }

    public float getFloatFollowRange(){
        return (float) this.getFollowRange();
    }

    public float halfFollowRange(){
        return this.getFloatFollowRange()/2.0F;
    }

    @Override
    public int xpReward() {
        return 10;
    }

    @Override
    public void tick() {
        this.setGravity();
        super.tick();
    }

    public void setGravity(){
        this.setNoGravity(this.isUnderWater());
    }

    public void aiStep() {
        super.aiStep();

        boolean flag = this.isSunBurnTick();
        if (flag) {
            ItemStack itemstack = this.getItemBySlot(EquipmentSlotType.HEAD);
            if (!itemstack.isEmpty()) {
                if (itemstack.isDamageableItem()) {
                    itemstack.setDamageValue(itemstack.getDamageValue() + this.random.nextInt(2));
                    if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                        this.broadcastBreakEvent(EquipmentSlotType.HEAD);
                        this.setItemSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
                    }
                }

                flag = false;
            }

            if (flag) {
                this.setSecondsOnFire(8);
            }
        }

        Vector3d vector3d = this.getDeltaMovement();
        if (!this.onGround && vector3d.y < 0.0D && !this.isNoGravity()) {
            this.setDeltaMovement(vector3d.multiply(1.0D, 0.6D, 1.0D));
        }

        if (this.teleportCooldown > 0){
            --this.teleportCooldown;
        }

        if (this.isAlive()) {
            this.attackAI();
            this.teleportAI();
        }

    }

    public void teleportAI(){
        if (!this.level.isClientSide){
            if (this.isTeleporting()) {
                --this.teleportTime;
                if (this.teleportTime <= 2){
                    this.prevX = this.getX();
                    this.prevY = this.getY();
                    this.prevZ = this.getZ();
                }
                if (this.teleportTime <= 0){
                    this.teleport();
                }
            } else {
                this.teleportTime = 20;
            }
        } else {
            if (this.isTeleporting()) {
                --this.teleportTime;
                ++this.teleportTime2;
                if (this.teleportTime <= 2){
                    this.prevX = this.getX();
                    this.prevY = this.getY();
                    this.prevZ = this.getZ();
                }
            } else {
                this.teleportTime = 20;
                this.teleportTime2 = 0;
            }
        }
    }

    public void attackAI(){
        if (!this.level.isClientSide) {
            if (this.getTarget() != null) {
                if (this.getSensing().canSee(this.getTarget())) {
                    if ((this.getTarget().distanceToSqr(this) >= MathHelper.square(4.0F) || this.isStaying())
                            && this.getTarget().distanceToSqr(this) < MathHelper.square(this.halfFollowRange())) {
                        ++this.fireTick;
                        this.getNavigation().stop();
                        this.getLookControl().setLookAt(this.getTarget(), 100.0F, this.getMaxHeadXRot());
                        double d2 = this.getTarget().getX() - this.getX();
                        double d1 = this.getTarget().getZ() - this.getZ();
                        this.yRot = -((float) MathHelper.atan2(d2, d1)) * (180F / (float) Math.PI);
                        this.yBodyRot = this.yRot;
                        if (this.fireTick > 10) {
                            this.startFiring();
                        } else {
                            this.movement();
                            this.stopFiring();
                        }
                        if (this.fireTick > 20) {
                            this.fireTick = -30;
                            this.magicFire(this.getTarget());
                        }
                    } else {
                        if (this.fireTick > 0) {
                            this.fireTick = 0;
                        }
                        this.stopFiring();
                        if (!this.isStaying() && this.getTarget().distanceToSqr(this) <= MathHelper.square(4.0F)) {
                            this.setIsTeleporting(true);
                        } else {
                            this.setIsTeleporting(false);
                            this.movement();
                        }
                    }
                } else {
                    if (MainConfig.WraithAggressiveTeleport.get()) {
                        if (!this.isStaying()) {
                            this.setIsTeleporting(true);
                        }
                    }
                }
            } else {
                this.setIsTeleporting(false);
                this.stopFiring();
            }
        } else {
            if (this.isFiring()){
                this.firingTick = 20;
                ++this.firingTick2;
            } else {
                this.firingTick2 = 0;
            }
            if (this.firingTick > 0){
                --this.firingTick;
            }
        }
    }

    public void magicFire(LivingEntity livingEntity){
        if (this.level.random.nextFloat() <= 0.05F) {
            WandUtil.spawnCrossGhostFires(this.level, livingEntity.position(), this);
        } else {
            WandUtil.spawnGhostFires(this.level, livingEntity.position(), this);
        }
    }

    public void movement(){
        if (this.getTarget() != null && !this.isStaying()) {
            Vector3d vector3d2 = null;
            if (this.getTarget().distanceToSqr(this) > MathHelper.square(this.halfFollowRange())){
                vector3d2 = this.getTarget().position();
            }
            if (vector3d2 != null) {
                Path path = this.getNavigation().createPath(vector3d2.x, vector3d2.y, vector3d2.z, 0);
                if (path != null) {
                    this.getNavigation().moveTo(path, 1.25F);
                }
            }
        }
    }

    protected void teleport() {
        if (!this.level.isClientSide() && this.isAlive() && this.getTarget() != null) {
            if (this.getSensing().canSee(this.getTarget())) {
                for (int i = 0; i < 128; ++i) {
                    double d3 = this.getTarget().getX() + (this.getRandom().nextDouble() - 0.5D) * this.getFollowRange();
                    double d4 = this.getTarget().getY();
                    double d5 = this.getTarget().getZ() + (this.getRandom().nextDouble() - 0.5D) * this.getFollowRange();
                    BlockPos blockPos1 = new BlockPos(d3, d4, d5);
                    if (!(this.level.canSeeSky(blockPos1) && this.level.isDay()
                            && !(this.fireImmune() || this.hasEffect(Effects.FIRE_RESISTANCE)))) {
                        AbstractWraithEntity wraith = new WraithEntity(ModEntityType.WRAITH.get(), this.level);
                        wraith.setPos(d3, d4, d5);
                        wraith.getLookControl().setLookAt(this.getTarget(), 100.0F, 100.0F);
                        if (wraith.canSee(this.getTarget())) {
                            if (this.randomTeleport(d3, d4, d5, false)) {
                                this.teleportHits();
                                this.setIsTeleporting(false);
                                wraith.remove();
                                this.getLookControl().setLookAt(this.getTarget(), 100.0F, 100.0F);
                                break;
                            }
                        } else {
                            wraith.remove();
                        }
                    }
                }
            } else {
                this.teleportTowardsEntity(this.getTarget());
            }
        }
    }

    public void teleportTowardsEntity(LivingEntity livingEntity){
        for(int i = 0; i < 128; ++i) {
            Vector3d vector3d = new Vector3d(this.getX() - livingEntity.getX(), this.getY(0.5D) - livingEntity.getEyeY(), this.getZ() - livingEntity.getZ());
            vector3d = vector3d.normalize();
            double d0 = 16.0D;
            double d1 = this.getX() + (this.random.nextDouble() - 0.5D) * 8.0D - vector3d.x * d0;
            double d2 = this.getY() + (double)(this.random.nextInt(16) - 8) - vector3d.y * d0;
            double d3 = this.getZ() + (this.random.nextDouble() - 0.5D) * 8.0D - vector3d.z * d0;
            if (this.randomTeleport(d1, d2, d3, false)) {
                this.teleportHits();
                this.teleportCooldown = 100;
                this.setIsTeleporting(false);
                this.getLookControl().setLookAt(livingEntity, 100.0F, 100.0F);
                break;
            }
        }
    }

    public void teleportHits(){
        this.level.broadcastEntityEvent(this, (byte) 100);
        this.level.broadcastEntityEvent(this, (byte) 101);
        if (!this.isSilent()) {
            this.level.playSound(null, this.prevX, this.prevY, this.prevZ, this.getTeleportInSound(), this.getSoundSource(), 1.0F, 1.0F);
            this.playSound(this.getTeleportOutSound(), 1.0F, 1.0F);
        }
    }

    public void startFiring(){
        if (!this.isFiring()) {
            this.setIsFiring(true);
            this.level.broadcastEntityEvent(this, (byte) 4);
            this.level.broadcastEntityEvent(this, (byte) 100);
            if (!this.isSilent()) {
                this.level.playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), this.getAttackSound(), this.getSoundSource(), 1.0F, 1.0F);
                this.playSound(this.getAttackSound(), 1.0F, 1.0F);
            }
        }
    }

    public void stopFiring(){
        if (this.isFiring()) {
            this.setIsFiring(false);
            this.level.broadcastEntityEvent(this, (byte) 5);
        }
    }

    public IParticleData getFireParticles(){
        return ModParticleTypes.WRAITH.get();
    }

    public IParticleData getBurstParticles(){
        return ModParticleTypes.WRAITH_BURST.get();
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte pId) {
        super.handleEntityEvent(pId);
        if (pId == 4) {
            this.setIsFiring(true);
        }
        if (pId == 5) {
            this.setIsFiring(false);
        }
        if (pId == 100){
            for(int j = 0; j < 6; ++j) {
                double d1 = this.getX() + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth() * 2.0D;
                double d2 = this.getY() + (this.random.nextDouble() + 0.5D);
                double d3 = this.getZ() + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth() * 2.0D;
                this.level.addParticle(this.getFireParticles(), d1, d2, d3, 0.0D, 0.0D, 0.0D);
            }
            for(int j = 0; j < 4; ++j) {
                double d1 = this.getX() + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth() * 2.0D;
                double d2 = this.getY() + (this.random.nextDouble() + 0.5D);
                double d3 = this.getZ() + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth() * 2.0D;
                this.level.addParticle(this.getBurstParticles(), d1, d2, d3, 0.0D, 0.0D, 0.0D);
            }
        }
        if (pId == 101){
            if (!this.isSilent()) {
                this.level.playSound(null, this.prevX, this.prevY, this.prevZ, this.getTeleportInSound(), this.getSoundSource(), 1.0F, 1.0F);
                this.playSound(this.getTeleportOutSound(), 1.0F, 1.0F);
            }
        }

    }

    public float getAnimationProgress(float pPartialTicks) {
        if (this.teleportTime <= 12 && this.isAlive()) {
            int i = this.teleportTime - 2;
            return i <= 0 ? 1.0F : 1.0F - ((float) i - pPartialTicks) / 20.0F;
        } else {
            return 0.0F;
        }
    }

    public static class WraithLookGoal extends LookAtGoal{
        public AbstractWraithEntity wraith;

        public WraithLookGoal(AbstractWraithEntity p_i1631_1_, Class<? extends LivingEntity> p_i1631_2_, float p_i1631_3_) {
            super(p_i1631_1_, p_i1631_2_, p_i1631_3_);
            this.wraith = p_i1631_1_;
        }

        public WraithLookGoal(AbstractWraithEntity p_i1632_1_, Class<? extends LivingEntity> p_i1632_2_, float p_i1632_3_, float p_i1632_4_) {
            super(p_i1632_1_, p_i1632_2_, p_i1632_3_, p_i1632_4_);
            this.wraith = p_i1632_1_;
        }

        public boolean canUse() {
            return super.canUse() && this.wraith.fireTick < 0 && this.wraith.getTarget() == null;
        }
    }

    public static class WraithLookRandomlyGoal extends LookRandomlyGoal {
        public AbstractWraithEntity wraith;

        public WraithLookRandomlyGoal(AbstractWraithEntity p_i1631_1_) {
            super(p_i1631_1_);
            this.wraith = p_i1631_1_;
        }

        public boolean canUse() {
            return super.canUse() && this.wraith.fireTick < 0 && this.wraith.getTarget() == null;
        }
    }
}
