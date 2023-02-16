package com.Polarice3.Goety.common.entities.hostile;

import com.Polarice3.Goety.AttributesConfig;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.neutral.ICustomAttributes;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.EntityFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.ModDamageSource;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UrbhadhachEntity extends MonsterEntity implements ICustomAttributes {
    private static final DataParameter<Boolean> DATA_STANDING_ID = EntityDataManager.defineId(UrbhadhachEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_THRALLING_ID = EntityDataManager.defineId(UrbhadhachEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> DATA_ROAR_ID = EntityDataManager.defineId(UrbhadhachEntity.class, DataSerializers.INT);
    private static final DataParameter<Optional<UUID>> THRALL_UUID = EntityDataManager.defineId(UrbhadhachEntity.class, DataSerializers.OPTIONAL_UUID);
    private float clientSideStandAnimationO;
    private float clientSideStandAnimation;
    private int warningSoundTicks;
    private int roarCooldown;
    private int thrallCooldown;
    private int healTime;

    public UrbhadhachEntity(EntityType<? extends MonsterEntity> p_i48553_1_, World p_i48553_2_) {
        super(p_i48553_1_, p_i48553_2_);
        ICustomAttributes.applyAttributesForEntity(p_i48553_1_, this);
        this.maxUpStep = 1.0F;
        this.xpReward = 10;
        this.moveControl = new MoveHelperController(this);
        this.setPathfindingMalus(PathNodeType.WATER, 0.0F);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new JumpAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(2, new AttackGoal());
        this.goalSelector.addGoal(3, new RestrictSunGoal(this));
        this.goalSelector.addGoal(4, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, AgeableEntity.class, 10, true, false, LivingEntity::isBaby));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, AttributesConfig.UrbhadhachHealth.get())
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, AttributesConfig.UrbhadhachDamage.get())
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.25D);
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.URBHADHACH_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.URBHADHACH_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.URBHADHACH_DEATH.get();
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(ModSounds.URBHADHACH_STEP.get(), 0.15F, 1.0F);
    }

    protected void playWarningSound() {
        if (this.warningSoundTicks <= 0) {
            this.playSound(ModSounds.URBHADHACH_ROAR.get(), 1.0F, 1.0F);
            this.warningSoundTicks = 40;
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_THRALLING_ID, false);
        this.entityData.define(DATA_STANDING_ID, false);
        this.entityData.define(DATA_ROAR_ID, 0);
        this.entityData.define(THRALL_UUID, Optional.empty());
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        UUID uuid;
        if (pCompound.hasUUID("Thrall")) {
            uuid = pCompound.getUUID("Thrall");
        } else {
            String s = pCompound.getString("Thrall");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setThrallUuid(uuid);
            } catch (Throwable ignored) {
            }
        }
        pCompound.putInt("ThrallCooldown", this.thrallCooldown);
        pCompound.putInt("RoarCooldown", this.roarCooldown);
        pCompound.putInt("HealTime", this.healTime);
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (this.getThrallUUID() != null) {
            pCompound.putUUID("Thrall", this.getThrallUUID());
        }
        this.thrallCooldown = pCompound.getInt("ThrallCooldown");
        this.roarCooldown = pCompound.getInt("RoarCooldown");
        this.healTime = pCompound.getInt("HealTime");
    }

    @Nullable
    public AgeableEntity getThrall() {
        try {
            UUID uuid = this.getThrallUUID();
            if (uuid != null){
                if (EntityFinder.getLivingEntityByUuiD(uuid) instanceof AgeableEntity){
                    if (EntityFinder.getLivingEntityByUuiD(uuid).isBaby()) {
                        return (AgeableEntity) EntityFinder.getLivingEntityByUuiD(uuid);
                    }
                }
            }
            return null;
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    @Nullable
    public UUID getThrallUUID() {
        return this.entityData.get(THRALL_UUID).orElse(null);
    }

    public void setThrallUuid(UUID uuid){
        this.entityData.set(THRALL_UUID, Optional.ofNullable(uuid));
    }

    public void setThrall(AgeableEntity ageableEntity){
        this.setThrallUuid(ageableEntity.getUUID());
    }

    public void aiStep() {
        super.aiStep();

        if (this.thrallCooldown > 0){
            --this.thrallCooldown;
        }

        if (MainConfig.UrbhadhachThrall.get()) {
            if (this.getLastHurtByMob() == null
                    && this.lastHurtByPlayer == null
                    && !this.isUnderWater()
                    && this.thrallCooldown <= 0
                    && (!this.level.isDay() || this.level.isRainingAt(this.blockPosition()))) {
                this.setThralling(true);
            }
        }

        if (this.isThralling()) {
            if (this.getThrall() == null){
                List<AgeableEntity> list = this.level.getEntitiesOfClass(AgeableEntity.class, this.getBoundingBox().inflate(64.0D, 8.0D, 64.0D));
                if (!list.isEmpty()) {
                    for (AgeableEntity ageableEntity : list) {
                        if (ageableEntity.isBaby() && !ageableEntity.isDeadOrDying() && !ageableEntity.hasEffect(Effects.NIGHT_VISION)) {
                            this.setThrall(ageableEntity);
                        }
                    }
                }
            }
            if (this.getThrall() != null) {
                if (this.getThrall().distanceToSqr(this) > 16 && !this.getThrall().isUnderWater()) {
                    if (this.getThrall().isSleeping()) {
                        this.getThrall().stopSleeping();
                    } else {
                        float speed = 0.5F;
                        if (this.getThrall().getAttribute(Attributes.FOLLOW_RANGE) != null) {
                            if (this.getThrall().getAttributeValue(Attributes.MOVEMENT_SPEED) < 0.5F) {
                                speed = 1.0F;
                            }
                            if (this.pathToUrbhadhach(this.getThrall()) != null) {
                                this.getThrall().getNavigation().moveTo(this.pathToUrbhadhach(this.getThrall()), speed);
                            }
                        } else {
                            this.thrallCooldown = 600;
                            this.setThralling(false);
                        }
                    }
                    if (!this.level.isClientSide) {
                        this.getThrall().addEffect(new EffectInstance(Effects.NIGHT_VISION, 20));
                        this.addEffect(new EffectInstance(Effects.INVISIBILITY, 4));
                    }
                    if (this.tickCount % 100 == 0) {
                        this.playSound(SoundEvents.BELL_BLOCK, 2.0F, 0.25F);
                        this.getThrall().playSound(SoundEvents.BELL_RESONATE, 1.0F, 0.5F);
                    }
                } else {
                    this.thrallCooldown = 600;
                    this.setThralling(false);
                }
                if (this.getThrall().isDeadOrDying()) {
                    this.setThrallUuid(null);
                }
            }
        }

        if (this.hurtTime > 0){
            this.thrallCooldown = 600;
            this.setThralling(false);
        }

        boolean flag0 = false;
        for (LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8), EntityPredicates.NO_CREATIVE_OR_SPECTATOR)){
            if (livingEntity instanceof MobEntity){
                if (((MobEntity) livingEntity).getTarget() == this){
                    flag0 = true;
                    this.setTarget(livingEntity);
                }
            } else if (livingEntity instanceof PlayerEntity){
                flag0 = true;
                this.setTarget(livingEntity);
            }
        }
        if (flag0){
            this.thrallCooldown = 600;
            this.setThralling(false);
        }

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

        if (this.healTime > 0){
            --this.healTime;
            if (this.tickCount % 20 == 0){
                this.heal(1.0F);
            }
        }

        if (this.roarCooldown > 0){
            --this.roarCooldown;
        }

        if (this.roarCooldown <= 0){
            if (this.getTarget() != null && !this.getTarget().isBaby() && !this.isStanding() && !this.isThralling() && this.isAlive()){
                this.getLookControl().setLookAt(this.getTarget().position());
                if (this.hasEffect(Effects.INVISIBILITY)){
                    this.removeEffect(Effects.INVISIBILITY);
                }
                this.roarCooldown = 300;
                this.setRoarTick(20);
                this.level.broadcastEntityEvent(this, (byte) 104);
            }
        }

        if (this.getRoarTick() > 0) {
            this.decreaseRoarTick();
            if (this.getRoarTick() == 10) {
                this.roar();
            }
        }

        if (this.getHealth() <= this.getMaxHealth()/2) {
            for (AgeableEntity livingEntity : this.level.getEntitiesOfClass(AgeableEntity.class, this.getBoundingBox().inflate(32))) {
                if (livingEntity.isBaby() && this.getTarget() != livingEntity) {
                    this.setTarget(livingEntity);
                }
            }
            if (this.getTarget() != null && this.getTarget().isBaby()){
                this.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 20, 1, false, false));
            }
        }
    }

    public Path pathToUrbhadhach(AgeableEntity ageableEntity){
        PathNavigator pathNavigator = ageableEntity.getNavigation();
        return pathNavigator.createPath(this, 0);
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (this.clientSideStandAnimation != this.clientSideStandAnimationO) {
                this.refreshDimensions();
            }

            this.clientSideStandAnimationO = this.clientSideStandAnimation;
            if (this.isStanding()) {
                this.clientSideStandAnimation = MathHelper.clamp(this.clientSideStandAnimation + 1.0F, 0.0F, 6.0F);
            } else {
                this.clientSideStandAnimation = MathHelper.clamp(this.clientSideStandAnimation - 1.0F, 0.0F, 6.0F);
            }
        }

        if (this.warningSoundTicks > 0) {
            --this.warningSoundTicks;
        }

    }

    public int getRoarTick() {
        return this.entityData.get(DATA_ROAR_ID);
    }

    public void setRoarTick(int roarTick){
        this.entityData.set(DATA_ROAR_ID, roarTick);
    }

    public void decreaseRoarTick(){
        this.setRoarTick(this.getRoarTick() - 1);
    }

    public EntitySize getDimensions(Pose pPose) {
        if (this.clientSideStandAnimation > 0.0F) {
            float f = this.clientSideStandAnimation / 6.0F;
            float f1 = 1.0F + f;
            return super.getDimensions(pPose).scale(1.0F, f1);
        } else {
            return super.getDimensions(pPose);
        }
    }

    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = pEntity.hurt(DamageSource.mobAttack(this), (int)this.getAttributeValue(Attributes.ATTACK_DAMAGE));
        if (flag) {
            this.level.broadcastEntityEvent(this, (byte)105);
            this.playSound(ModSounds.URBHADHACH_ATTACK.get(), 1.0F, 1.0F);
            if (pEntity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) pEntity;
                if (this.random.nextFloat() < 0.25F) {
                    if (livingEntity instanceof PlayerEntity) {
                        PlayerEntity player = (PlayerEntity) livingEntity;
                        if (player.isUsingItem() && player.getUseItem().getItem() instanceof ShieldItem) {
                            player.getCooldowns().addCooldown(Items.SHIELD, 100);
                            this.level.broadcastEntityEvent(player, (byte) 30);
                        }
                    }
                    livingEntity.knockback(4.0F, (double) MathHelper.sin(livingEntity.yRot * ((float) Math.PI / 180F)), (double) (-MathHelper.cos(livingEntity.yRot * ((float) Math.PI / 180F))));
                    livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                }
                if (livingEntity.isBaby()){
                    livingEntity.hurt(DamageSource.mobAttack(this), livingEntity.getMaxHealth());
                }
            }
            this.doEnchantDamageEffects(this, pEntity);
        }

        return flag;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerable()){
            return false;
        } else {
            float damage = pAmount;

            if (pSource.isFire()){
                damage = pAmount * 2;
            }

            if (this.isStanding()){
                damage = pAmount * 1.5F;
            }

            return super.hurt(pSource, damage);
        }
    }

    public void killed(ServerWorld world, LivingEntity killedEntity) {
        super.killed(world, killedEntity);
        if (killedEntity.isBaby() && killedEntity.getMobType() != CreatureAttribute.UNDEAD){
            this.playSound(SoundEvents.GENERIC_EAT, 2.0F, 0.25F);
            this.playSound(SoundEvents.PLAYER_BURP, 2.0F, 0.25F);
            this.heal(killedEntity.getMaxHealth() * 2);
            this.healTime = 600;
        }
    }

    private void roar() {
        if (this.isAlive() && !this.isSilent()) {
            for(Entity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D), EntityPredicates.NO_CREATIVE_OR_SPECTATOR)) {
                if (entity.hurt(ModDamageSource.directFrost(this), 6.0F)){
                    this.knockBack(entity);
                }
            }

            if (this.isOnFire()){
                this.clearFire();
            }

            if (!this.level.isClientSide) {
                ServerWorld serverWorld = (ServerWorld) this.level;
                for (LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(16.0D), EntityPredicates.NO_CREATIVE_OR_SPECTATOR)) {
                    if (MobUtil.notImmuneToFrost(livingEntity) && livingEntity.getMaxHealth() < this.getMaxHealth()) {
                        livingEntity.addEffect(new EffectInstance(Effects.WEAKNESS, 300));
                    }
                }
                this.heal(2.0F);
                Vector3d vector3d = this.getBoundingBox().getCenter();

                for(int i = 0; i < 40; ++i) {
                    double d0 = this.random.nextGaussian() * 0.2D;
                    double d1 = this.random.nextGaussian() * 0.2D;
                    double d2 = this.random.nextGaussian() * 0.2D;
                    serverWorld.sendParticles(ParticleTypes.CLOUD, vector3d.x, vector3d.y, vector3d.z, 0, d0, d1, d2, 0.5F);
                }
            }
        }

    }

    private void knockBack(Entity p_213688_1_) {
        double d0 = p_213688_1_.getX() - this.getX();
        double d1 = p_213688_1_.getZ() - this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        p_213688_1_.push(d0 / d2 * 2.0D, 0.1D, d1 / d2 * 2.0D);
    }

    protected boolean isImmobile() {
        return super.isImmobile() || this.getRoarTick() > 0;
    }

    public boolean isThralling() {
        return this.entityData.get(DATA_THRALLING_ID);
    }

    public void setThralling(boolean pThralling) {
        this.entityData.set(DATA_THRALLING_ID, pThralling);
    }

    public boolean isStanding() {
        return this.entityData.get(DATA_STANDING_ID);
    }

    public void setStanding(boolean pStanding) {
        this.entityData.set(DATA_STANDING_ID, pStanding);
    }

    @OnlyIn(Dist.CLIENT)
    public float getStandingAnimationScale(float p_189795_1_) {
        return MathHelper.lerp(p_189795_1_, this.clientSideStandAnimationO, this.clientSideStandAnimation) / 6.0F;
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte pId) {
        if (pId == 104) {
            this.roarCooldown = 300;
            this.setRoarTick(20);
            this.playSound(ModSounds.URBHADHACH_STRONG_ROAR.get(), 5.0F, 1.0F);
        } else {
            super.handleEntityEvent(pId);
        }

    }

    protected float getWaterSlowDown() {
        return 0.98F;
    }

    class AttackGoal extends MeleeAttackGoal {
        private int attackSpeed;

        public AttackGoal() {
            super(UrbhadhachEntity.this, 1.25D, true);
        }

        protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
            double d0 = this.getAttackReachSqr(pEnemy);
            if (pDistToEnemySqr <= d0 && this.attackTime()) {
                this.resetAttack();
                this.mob.doHurtTarget(pEnemy);
                UrbhadhachEntity.this.setStanding(false);
            } else if (pDistToEnemySqr <= d0 * 2.0D) {
                if (this.attackTime()) {
                    UrbhadhachEntity.this.setStanding(false);
                    this.resetAttack();
                }

                if (this.ticksUntilNextAttack() <= 10) {
                    UrbhadhachEntity.this.setStanding(true);
                    UrbhadhachEntity.this.playWarningSound();
                }
            } else {
                this.resetAttack();
                UrbhadhachEntity.this.setStanding(false);
            }
        }

        public void start() {
            super.start();
            this.attackSpeed = 0;
        }

        public void stop() {
            UrbhadhachEntity.this.setStanding(false);
            super.stop();
        }

        @Override
        public void tick() {
            this.attackSpeed = Math.max(this.attackSpeed - 1, 0);
            super.tick();
        }

        protected void resetAttack() {
            this.attackSpeed = this.mob.level.random.nextFloat() <= 0.25 ? 10 : 20;
        }

        protected boolean attackTime() {
            return this.attackSpeed <= 0;
        }

        protected int ticksUntilNextAttack() {
            return this.attackSpeed;
        }
    }

    static class JumpAtTargetGoal extends Goal{
        private final UrbhadhachEntity mob;
        private LivingEntity target;
        private final float yd;

        public JumpAtTargetGoal(UrbhadhachEntity p_i1630_1_, float p_i1630_2_) {
            this.mob = p_i1630_1_;
            this.yd = p_i1630_2_;
            this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        public boolean canUse() {
            if (this.mob.isVehicle()) {
                return false;
            } else {
                this.target = this.mob.getTarget();
                if (target == null) {
                    return false;
                } else {
                    double d0 = this.mob.distanceToSqr(target);
                    if (d0 >= MathHelper.square(4.0F) && d0 < MathHelper.square(8.0F)) {
                        if (!this.mob.isOnGround() || this.mob.isUnderWater()) {
                            return false;
                        } else {
                            return this.mob.getRandom().nextFloat() < 0.25F;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }

        public boolean canContinueToUse() {
            return !this.mob.isOnGround();
        }

        public void start() {
            this.mob.setStanding(true);
            Vector3d vector3d = this.mob.getDeltaMovement();
            Vector3d vector3d1 = new Vector3d(this.target.getX() - this.mob.getX(), 0.0D, this.target.getZ() - this.mob.getZ());
            if (vector3d1.lengthSqr() > 1.0E-7D) {
                vector3d1 = vector3d1.normalize().add(vector3d);
            }
            double d2 = this.target.getX() - this.mob.getX();
            double d1 = this.target.getZ() - this.mob.getZ();
            this.mob.yRot = -((float)MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
            this.mob.yBodyRot = this.mob.yRot;

            this.mob.setDeltaMovement(vector3d1.x, (double)this.yd, vector3d1.z);
        }

        public void stop() {
            this.mob.setStanding(false);
        }
    }

    static class MoveHelperController extends MovementController {
        private final UrbhadhachEntity urbhadhach;

        public MoveHelperController(UrbhadhachEntity p_i48909_1_) {
            super(p_i48909_1_);
            this.urbhadhach = p_i48909_1_;
        }

        public void tick() {
            LivingEntity livingentity = this.urbhadhach.getTarget();
            if (this.urbhadhach.isInWater()) {
                if (livingentity != null && livingentity.getY() > this.urbhadhach.getY()) {
                    this.urbhadhach.setDeltaMovement(this.urbhadhach.getDeltaMovement().add(0.0D, 0.002D, 0.0D));
                }

                if (this.operation != MovementController.Action.MOVE_TO || this.urbhadhach.getNavigation().isDone()) {
                    this.urbhadhach.setSpeed(0.0F);
                    return;
                }

                double d0 = this.wantedX - this.urbhadhach.getX();
                double d1 = this.wantedY - this.urbhadhach.getY();
                double d2 = this.wantedZ - this.urbhadhach.getZ();
                double d3 = (double)MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d1 = d1 / d3;
                float f = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.urbhadhach.yRot = this.rotlerp(this.urbhadhach.yRot, f, 90.0F);
                this.urbhadhach.yBodyRot = this.urbhadhach.yRot;
                float f1 = (float)(this.speedModifier * this.urbhadhach.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float f2 = MathHelper.lerp(0.125F, this.urbhadhach.getSpeed(), f1);
                this.urbhadhach.setSpeed(f2);
                this.urbhadhach.setDeltaMovement(this.urbhadhach.getDeltaMovement().add((double)f2 * d0 * 0.005D, (double)f2 * d1 * 0.1D, (double)f2 * d2 * 0.005D));
            } else {
                if (!this.urbhadhach.onGround) {
                    this.urbhadhach.setDeltaMovement(this.urbhadhach.getDeltaMovement().add(0.0D, -0.008D, 0.0D));
                }

                super.tick();
            }

        }
    }
}
