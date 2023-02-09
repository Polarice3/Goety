package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.MobConfig;
import com.Polarice3.Goety.common.entities.ai.SummonTargetGoal;
import com.Polarice3.Goety.common.entities.neutral.OwnedFlyingEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class PhantomMinionEntity extends OwnedFlyingEntity {
    private static final DataParameter<Integer> ID_SIZE = EntityDataManager.defineId(PhantomMinionEntity.class, DataSerializers.INT);
    private static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.defineId(PhantomMinionEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> NETHER = EntityDataManager.defineId(PhantomMinionEntity.class, DataSerializers.BOOLEAN);
    private Vector3d moveTargetPoint = Vector3d.ZERO;
    private PhantomMinionEntity.AttackPhase attackPhase = PhantomMinionEntity.AttackPhase.CIRCLE;

    public PhantomMinionEntity(EntityType<? extends PhantomMinionEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveControl = new PhantomMinionEntity.MoveHelperController(this);
        this.lookControl = new LookHelperController(this);
    }

    protected BodyController createBodyControl() {
        return new PhantomMinionEntity.BodyHelperController(this);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new PickAttackGoal(this));
        this.goalSelector.addGoal(1, new FollowOwnerGoal(this));
        this.goalSelector.addGoal(2, new PhantomMinionEntity.SweepAttackGoal(this));
        this.goalSelector.addGoal(3, new PhantomMinionEntity.OrbitPointGoal(this));
        this.targetSelector.addGoal(1, new TargetingGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, MobConfig.PhantomServantHealth.get())
                .add(Attributes.ATTACK_DAMAGE, MobConfig.PhantomServantDamage.get());
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_SIZE, 0);
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
        this.entityData.define(NETHER, false);
    }

    public void setPhantomSize(int pSize) {
        this.entityData.set(ID_SIZE, MathHelper.clamp(pSize, 0, 64));
    }

    private void updatePhantomSizeInfo() {
        this.refreshDimensions();
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((double)(6 + this.getPhantomSize()));
    }

    public int getPhantomSize() {
        return this.entityData.get(ID_SIZE);
    }

    public void setNether(boolean pNether){
        this.entityData.set(NETHER, pNether);
    }

    public boolean isNether(){
        return this.entityData.get(NETHER);
    }

    private boolean isSwooping() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    private void setSwooping(boolean pSwooping) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (pSwooping) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    protected float getStandingEyeHeight(Pose pPose, EntitySize pSize) {
        return pSize.height * 0.35F;
    }

    public void onSyncedDataUpdated(DataParameter<?> pKey) {
        if (ID_SIZE.equals(pKey)) {
            this.updatePhantomSizeInfo();
        }

        super.onSyncedDataUpdated(pKey);
    }

    public BlockPos getBoundOrigin() {
        if (this.boundOrigin == null){
            return BlockPos.ZERO;
        }
        return this.boundOrigin;
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            float f = MathHelper.cos((float)(this.getId() * 3 + this.tickCount) * 0.13F + (float)Math.PI);
            float f1 = MathHelper.cos((float)(this.getId() * 3 + this.tickCount + 1) * 0.13F + (float)Math.PI);
            if (f > 0.0F && f1 <= 0.0F) {
                this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.PHANTOM_FLAP, this.getSoundSource(), 0.95F + this.random.nextFloat() * 0.05F, 0.95F + this.random.nextFloat() * 0.05F, false);
            }

            int i = this.getPhantomSize();
            float f2 = MathHelper.cos(this.yRot * ((float)Math.PI / 180F)) * (1.3F + 0.21F * (float)i);
            float f3 = MathHelper.sin(this.yRot * ((float)Math.PI / 180F)) * (1.3F + 0.21F * (float)i);
            float f4 = (0.3F + f * 0.45F) * ((float)i * 0.2F + 1.0F);
            this.level.addParticle(ParticleTypes.MYCELIUM, this.getX() + (double)f2, this.getY() + (double)f4, this.getZ() + (double)f3, 0.0D, 0.0D, 0.0D);
            this.level.addParticle(ParticleTypes.MYCELIUM, this.getX() - (double)f2, this.getY() + (double)f4, this.getZ() - (double)f3, 0.0D, 0.0D, 0.0D);
        }

    }

    public void aiStep() {
        if (this.isAlive() && this.isSunBurnTick()) {
            this.setSecondsOnFire(8);
        }

        if (!this.level.isClientSide) {
            PhantomMinionEntity.this.setSwooping(this.attackPhase == AttackPhase.SWOOP);

            if (this.isNether()) {
                if (this.attackPhase == AttackPhase.CIRCLE) {
                    if (this.tickCount % 40 == 0) {
                        LivingEntity livingentity = this.getTarget();
                        if (livingentity != null && livingentity.isAlive() && !livingentity.isDeadOrDying()) {
                            double d1 = livingentity.getX() - this.getX();
                            double d2 = livingentity.getY(0.5D) - this.getY(0.5D);
                            double d3 = livingentity.getZ() - this.getZ();
                            SmallFireballEntity fireball = new SmallFireballEntity(this.level, this, d1, d2, d3);
                            fireball.setPos(fireball.getX(), this.getY(), fireball.getZ());
                            this.level.addFreshEntity(fireball);
                            if (!this.isSilent()) {
                                this.level.levelEvent(1018, this.blockPosition(), 0);
                            }
                        }
                    }
                }
            }
        }

        super.aiStep();
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
    }

    public ILivingEntityData finalizeSpawn(IServerWorld pLevel, DifficultyInstance pDifficulty, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
        this.setBoundOrigin(this.blockPosition().above(5));
        this.setPhantomSize(0);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setPhantomSize(pCompound.getInt("Size"));
        this.setNether(pCompound.getBoolean("isNether"));
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Size", this.getPhantomSize());
        pCompound.putBoolean("isNether", this.isNether());
    }

    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return true;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.PHANTOM_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.PHANTOM_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PHANTOM_DEATH;
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    protected float getSoundVolume() {
        return 1.0F;
    }

    public EntitySize getDimensions(Pose pPose) {
        int i = this.getPhantomSize();
        EntitySize entitysize = super.getDimensions(pPose);
        float f = (entitysize.width + 0.2F * (float)i) / entitysize.width;
        return entitysize.scale(f);
    }

    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = super.doHurtTarget(pEntity);
        if (flag) {
            if (this.isNether()) {
                pEntity.setSecondsOnFire(8);
            }
        }
        return flag;
    }

    public boolean isSensitiveToWater() {
        return this.isNether();
    }

    public boolean isOnFire() {
        if (this.isNether()){
            return this.isSwooping();
        } else {
            return super.isOnFire();
        }
    }

    public boolean fireImmune() {
        return this.isNether();
    }

    enum AttackPhase {
        CIRCLE,
        SWOOP;
    }

    class TargetingGoal extends Goal {
        private final PhantomMinionEntity phantom;
        private final EntityPredicate attackTargeting = (new EntityPredicate()).range(64.0D).selector(SummonTargetGoal.predicate(PhantomMinionEntity.this));
        private final EntityPredicate selector = new EntityPredicate().selector(SummonTargetGoal.predicate(PhantomMinionEntity.this));
        private int nextScanTick = 20;

        private TargetingGoal(PhantomMinionEntity phantom) {
            this.phantom = phantom;
        }

        public boolean canUse() {
            if (this.nextScanTick > 0) {
                --this.nextScanTick;
            } else {
                this.nextScanTick = 60;
                List<LivingEntity> list = this.phantom.level.getNearbyEntities(LivingEntity.class, this.attackTargeting, this.phantom, this.phantom.getBoundingBox().inflate(16.0D, 64.0D, 16.0D));
                if (!list.isEmpty()) {
                    list.sort(Comparator.<Entity, Double>comparing(Entity::getY).reversed());

                    for(LivingEntity livingEntity : list) {
                        if (this.phantom.canAttack(livingEntity, selector)) {
                            this.phantom.setTarget(livingEntity);
                            return true;
                        }
                    }
                }

            }
            return false;
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = this.phantom.getTarget();
            return livingentity != null && this.phantom.canAttack(livingentity, selector);
        }
    }

    class BodyHelperController extends BodyController {
        public BodyHelperController(MobEntity p_i49925_2_) {
            super(p_i49925_2_);
        }

        public void clientTick() {
            PhantomMinionEntity.this.yHeadRot = PhantomMinionEntity.this.yBodyRot;
            PhantomMinionEntity.this.yBodyRot = PhantomMinionEntity.this.yRot;
        }
    }

    static class LookHelperController extends LookController {
        public LookHelperController(MobEntity p_i48802_2_) {
            super(p_i48802_2_);
        }

        public void tick() {
        }
    }

    abstract class MoveGoal extends Goal {
        public MoveGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        protected boolean touchingTarget() {
            return PhantomMinionEntity.this.moveTargetPoint.distanceToSqr(PhantomMinionEntity.this.getX(), PhantomMinionEntity.this.getY(), PhantomMinionEntity.this.getZ()) < 4.0D;
        }
    }

    class MoveHelperController extends MovementController {
        private float speed = 0.1F;

        public MoveHelperController(MobEntity p_i48801_2_) {
            super(p_i48801_2_);
        }

        public void tick() {
            if (PhantomMinionEntity.this.horizontalCollision) {
                PhantomMinionEntity.this.yRot += 180.0F;
                this.speed = 0.1F;
            }

            float f = (float)(PhantomMinionEntity.this.moveTargetPoint.x - PhantomMinionEntity.this.getX());
            float f1 = (float)(PhantomMinionEntity.this.moveTargetPoint.y - PhantomMinionEntity.this.getY());
            float f2 = (float)(PhantomMinionEntity.this.moveTargetPoint.z - PhantomMinionEntity.this.getZ());
            double d0 = (double)MathHelper.sqrt(f * f + f2 * f2);
            double d1 = 1.0D - (double)MathHelper.abs(f1 * 0.7F) / d0;
            f = (float)((double)f * d1);
            f2 = (float)((double)f2 * d1);
            d0 = (double)MathHelper.sqrt(f * f + f2 * f2);
            double d2 = (double)MathHelper.sqrt(f * f + f2 * f2 + f1 * f1);
            float f3 = PhantomMinionEntity.this.yRot;
            float f4 = (float)MathHelper.atan2((double)f2, (double)f);
            float f5 = MathHelper.wrapDegrees(PhantomMinionEntity.this.yRot + 90.0F);
            float f6 = MathHelper.wrapDegrees(f4 * (180F / (float)Math.PI));
            PhantomMinionEntity.this.yRot = MathHelper.approachDegrees(f5, f6, 4.0F) - 90.0F;
            PhantomMinionEntity.this.yBodyRot = PhantomMinionEntity.this.yRot;
            if (MathHelper.degreesDifferenceAbs(f3, PhantomMinionEntity.this.yRot) < 3.0F) {
                this.speed = MathHelper.approach(this.speed, 1.8F, 0.005F * (1.8F / this.speed));
            } else {
                this.speed = MathHelper.approach(this.speed, 0.2F, 0.025F);
            }

            float f7 = (float)(-(MathHelper.atan2((double)(-f1), d0) * (double)(180F / (float)Math.PI)));
            PhantomMinionEntity.this.xRot = f7;
            float f8 = PhantomMinionEntity.this.yRot + 90.0F;
            double d3 = (double)(this.speed * MathHelper.cos(f8 * ((float)Math.PI / 180F))) * Math.abs((double)f / d2);
            double d4 = (double)(this.speed * MathHelper.sin(f8 * ((float)Math.PI / 180F))) * Math.abs((double)f2 / d2);
            double d5 = (double)(this.speed * MathHelper.sin(f7 * ((float)Math.PI / 180F))) * Math.abs((double)f1 / d2);
            Vector3d vector3d = PhantomMinionEntity.this.getDeltaMovement();
            PhantomMinionEntity.this.setDeltaMovement(vector3d.add((new Vector3d(d3, d5, d4)).subtract(vector3d).scale(0.2D)));
        }
    }

    class OrbitPointGoal extends PhantomMinionEntity.MoveGoal {
        private final PhantomMinionEntity phantom;
        private float angle;
        private float distance;
        private float height;
        private float clockwise;

        private OrbitPointGoal(PhantomMinionEntity phantom) {
            this.phantom = phantom;
        }

        public boolean canUse() {
            return this.phantom.getTarget() == null || this.phantom.attackPhase == PhantomMinionEntity.AttackPhase.CIRCLE;
        }

        public void start() {
            this.distance = 5.0F + this.phantom.random.nextFloat() * 10.0F;
            this.height = -4.0F + this.phantom.random.nextFloat() * 9.0F;
            this.clockwise = this.phantom.random.nextBoolean() ? 1.0F : -1.0F;
            this.selectNext();
        }

        public void tick() {
            if (this.phantom.random.nextInt(350) == 0) {
                this.height = -4.0F + this.phantom.random.nextFloat() * 9.0F;
            }

            if (this.phantom.random.nextInt(250) == 0) {
                ++this.distance;
                if (this.distance > 15.0F) {
                    this.distance = 5.0F;
                    this.clockwise = -this.clockwise;
                }
            }

            if (this.phantom.random.nextInt(450) == 0) {
                this.angle = this.phantom.random.nextFloat() * 2.0F * (float)Math.PI;
                this.selectNext();
            }

            if (this.touchingTarget()) {
                this.selectNext();
            }

            if (this.phantom.moveTargetPoint.y < this.phantom.getY() && !this.phantom.level.isEmptyBlock(this.phantom.blockPosition().below(1))) {
                this.height = Math.max(1.0F, this.height);
                this.selectNext();
            }

            if (this.phantom.moveTargetPoint.y > this.phantom.getY() && !this.phantom.level.isEmptyBlock(this.phantom.blockPosition().above(1))) {
                this.height = Math.min(-1.0F, this.height);
                this.selectNext();
            }

        }

        private void selectNext() {
            if (BlockPos.ZERO.equals(this.phantom.getBoundOrigin())) {
                this.phantom.setBoundOrigin(this.phantom.blockPosition());
            }

            this.angle += this.clockwise * 15.0F * ((float)Math.PI / 180F);
            this.phantom.moveTargetPoint = Vector3d.atLowerCornerOf(this.phantom.getBoundOrigin()).add((double)(this.distance * MathHelper.cos(this.angle)), (double)(-4.0F + this.height), (double)(this.distance * MathHelper.sin(this.angle)));
        }
    }

    static class PickAttackGoal extends Goal {
        private final PhantomMinionEntity phantom;
        private int nextSweepTick;

        private PickAttackGoal(PhantomMinionEntity phantom) {
            this.phantom = phantom;
        }

        public boolean canUse() {
            LivingEntity livingentity = this.phantom.getTarget();
            return livingentity != null && this.phantom.canAttack(this.phantom.getTarget(), EntityPredicate.DEFAULT);
        }

        public void start() {
            if (this.phantom.isNether()){
                this.nextSweepTick = (8 + this.phantom.random.nextInt(4)) * 20;
            } else {
                this.nextSweepTick = 10;
            }
            this.phantom.attackPhase = PhantomMinionEntity.AttackPhase.CIRCLE;
            this.setAnchorAboveTarget();
        }

        public void stop() {
            this.phantom.setBoundOrigin(this.phantom.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, this.phantom.getBoundOrigin()).above(10 + this.phantom.random.nextInt(20)));
        }

        public void tick() {
            if (this.phantom.attackPhase == PhantomMinionEntity.AttackPhase.CIRCLE) {
                --this.nextSweepTick;
                if (this.nextSweepTick <= 0) {
                    this.phantom.attackPhase = PhantomMinionEntity.AttackPhase.SWOOP;
                    this.setAnchorAboveTarget();
                    this.nextSweepTick = (8 + this.phantom.random.nextInt(4)) * 20;
                    this.phantom.playSound(SoundEvents.PHANTOM_SWOOP, 10.0F, 0.95F + this.phantom.random.nextFloat() * 0.1F);
                }
            }

        }

        private void setAnchorAboveTarget() {
            this.phantom.setBoundOrigin(this.phantom.getTarget().blockPosition().above(20 + this.phantom.random.nextInt(20)));
            if (this.phantom.getBoundOrigin().getY() < this.phantom.level.getSeaLevel()) {
                this.phantom.setBoundOrigin(new BlockPos(this.phantom.getBoundOrigin().getX(), this.phantom.level.getSeaLevel() + 1, this.phantom.getBoundOrigin().getZ()));
            }

        }
    }

    static class FollowOwnerGoal extends Goal{
        private final PhantomMinionEntity phantom;

        private FollowOwnerGoal(PhantomMinionEntity phantom) {
            this.phantom = phantom;
        }

        public boolean canUse() {
            LivingEntity livingentity = this.phantom.getTrueOwner();
            return livingentity != null && this.phantom.getTarget() == null && this.phantom.attackPhase != AttackPhase.SWOOP;
        }

        public void start() {
            this.phantom.attackPhase = PhantomMinionEntity.AttackPhase.CIRCLE;
            this.setAnchorAboveOwner();
        }

        public void stop() {
            this.phantom.setBoundOrigin(this.phantom.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING, this.phantom.getBoundOrigin()).above(10 + this.phantom.random.nextInt(20)));
        }

        public void tick() {
            if (this.phantom.attackPhase == PhantomMinionEntity.AttackPhase.CIRCLE) {
                this.setAnchorAboveOwner();
            }

        }

        private void setAnchorAboveOwner() {
            this.phantom.setBoundOrigin(this.phantom.getTrueOwner().blockPosition().above(10 + this.phantom.random.nextInt(10)));
        }
    }

    class SweepAttackGoal extends PhantomMinionEntity.MoveGoal {
        private final PhantomMinionEntity phantom;

        private SweepAttackGoal(PhantomMinionEntity phantom) {
            this.phantom = phantom;
        }

        public boolean canUse() {
            return this.phantom.getTarget() != null && this.phantom.attackPhase == PhantomMinionEntity.AttackPhase.SWOOP;
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = this.phantom.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else if (!(livingentity instanceof PlayerEntity) || !((PlayerEntity)livingentity).isSpectator() && !((PlayerEntity)livingentity).isCreative()) {
                if (!this.canUse()) {
                    return false;
                } else {
                    if (this.phantom.tickCount % 20 == 0) {
                        List<CatEntity> list = this.phantom.level.getEntitiesOfClass(CatEntity.class, this.phantom.getBoundingBox().inflate(16.0D), EntityPredicates.ENTITY_STILL_ALIVE);
                        if (!list.isEmpty()) {
                            for(CatEntity catentity : list) {
                                catentity.hiss();
                            }

                            return false;
                        }
                    }

                    return true;
                }
            } else {
                return false;
            }
        }

        public void start() {
        }

        public void stop() {
            this.phantom.setTarget((LivingEntity)null);
            this.phantom.attackPhase = PhantomMinionEntity.AttackPhase.CIRCLE;
        }

        public void tick() {
            LivingEntity livingentity = this.phantom.getTarget();
            this.phantom.moveTargetPoint = new Vector3d(livingentity.getX(), livingentity.getY(0.5D), livingentity.getZ());
            if (this.phantom.getBoundingBox().inflate((double)0.2F).intersects(livingentity.getBoundingBox())) {
                this.phantom.doHurtTarget(livingentity);
                this.phantom.attackPhase = PhantomMinionEntity.AttackPhase.CIRCLE;
                if (!this.phantom.isSilent()) {
                    this.phantom.level.levelEvent(1039, this.phantom.blockPosition(), 0);
                }
            } else if (this.phantom.horizontalCollision || this.phantom.hurtTime > 0) {
                this.phantom.attackPhase = PhantomMinionEntity.AttackPhase.CIRCLE;
            }

        }
    }
}
