package com.Polarice3.Goety.common.entities.hostile;

import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.ParticleUtil;
import com.Polarice3.Goety.utils.SoundUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class ShadeEntity extends MonsterEntity {
    private boolean hasLimitedLife;
    private int limitedLifeTicks;
    @Nullable
    private BlockPos boundOrigin;
    private static final Predicate<LivingEntity> ONLY_REGULAR_ZOMBIES =
            (mob) -> mob.getType() == EntityType.ZOMBIE;

    public ShadeEntity(EntityType<? extends ShadeEntity> p_i50190_1_, World p_i50190_2_) {
        super(p_i50190_1_, p_i50190_2_);
        this.navigation = this.createNavigation(p_i50190_2_);
        this.moveControl = new MoveHelperController(this);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(4, new AttackGoal());
        this.goalSelector.addGoal(8, new MoveRandomGoal());
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, ShadeEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, ZombieEntity.class, 10, false, false, ONLY_REGULAR_ZOMBIES));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.MAX_HEALTH, 2.0D);
    }

    public void move(MoverType typeIn, Vector3d pos) {
        super.move(typeIn, pos);
        this.checkInsideBlocks();
    }

    protected PathNavigator createNavigation(World worldIn) {
        FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, worldIn);
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanPassDoors(true);
        return flyingpathnavigator;
    }

    public void tick() {
        if (!this.isLeashed()){
            this.noPhysics = true;
            super.tick();
            this.noPhysics = false;
            this.setNoGravity(true);
        } else {
            this.noPhysics = this.isInWall();
            super.tick();
            this.setNoGravity(false);
            this.setTarget(null);
        }
        if (this.hasLimitedLife && --this.limitedLifeTicks <= 0) {
            this.die(DamageSource.STARVE);
        }
        if (!this.level.isClientSide) {
            ServerWorld serverWorld = (ServerWorld) this.level;
            LivingEntity livingEntity = this.getTarget();
            if (livingEntity instanceof ZombieEntity) {
                if (this.getBoundingBox().inflate(1.2F).intersects(livingEntity.getBoundingBox())) {
                    HuskarlEntity huskarlEntity = ((ZombieEntity) livingEntity).convertTo(ModEntityType.HUSKARL.get(), false);
                    if (huskarlEntity != null) {
                        huskarlEntity.setPos(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                        if (this.hasCustomName()) {
                            huskarlEntity.setCustomName(this.getCustomName());
                        }
                        huskarlEntity.setShade(true);
                        huskarlEntity.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(livingEntity.blockPosition()), SpawnReason.CONVERSION, null, null);
                        serverWorld.addFreshEntity(huskarlEntity);
                        this.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.0F, 1.0F);
                        this.remove();
                    }
                }
            }
        }
    }

    public void die(DamageSource cause) {
        super.die(cause);
        if (this.level.isClientSide) {
            for (int i = 0; i < this.level.random.nextInt(35) + 10; ++i) {
                new ParticleUtil(ParticleTypes.POOF, this.getX(), this.getEyeY(), this.getZ(), 0.0F, 0.0F, 0.0F);
            }
        }
        if (!this.level.isClientSide){
            ServerWorld serverWorld = (ServerWorld) this.level;
            for (int i = 0; i < this.level.random.nextInt(35) + 10; ++i) {
                serverWorld.sendParticles(ParticleTypes.POOF, this.getX(), this.getEyeY(), this.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
            }
        }
    }

    public boolean canBeLeashed(PlayerEntity pPlayer) {
        return !this.isLeashed();
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.STRAY_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.STRAY_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.STRAY_DEATH;
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    public boolean doHurtTarget(Entity pEntity) {
        if (!super.doHurtTarget(pEntity)) {
            return false;
        } else {
            if (pEntity instanceof LivingEntity) {
                ((LivingEntity)pEntity).addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200));
            }
            float f = (float)this.getAttributeValue(Attributes.ATTACK_DAMAGE);

            return pEntity.hurt(DamageSource.indirectMagic(this, null), f);
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos pBoundOrigin) {
        this.boundOrigin = pBoundOrigin;
    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    public boolean hurt(@Nonnull DamageSource source, float amount) {
        if (source.isMagic() || source.isBypassInvul() || source.isBypassMagic() || source.isFire()){
            return super.hurt(source, amount);
        } else {
            return false;
        }
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

    public void setLimitedLife(int pLimitedLifeTicks) {
        this.hasLimitedLife = true;
        this.limitedLifeTicks = pLimitedLifeTicks;
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

    class AttackGoal extends Goal {
        public AttackGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            if (ShadeEntity.this.getTarget() != null
                    && !ShadeEntity.this.getTarget().isAlliedTo(ShadeEntity.this)
                    && !ShadeEntity.this.getMoveControl().hasWanted()) {
                return ShadeEntity.this.distanceToSqr(ShadeEntity.this.getTarget()) > 4.0D;
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return ShadeEntity.this.getMoveControl().hasWanted()
                    && ShadeEntity.this.getTarget() != null
                    && ShadeEntity.this.getTarget().isAlive();
        }

        public void start() {
            LivingEntity livingentity = ShadeEntity.this.getTarget();
            assert livingentity != null;
            Vector3d vector3d = livingentity.position();
            ShadeEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
        }

        public void tick() {
            LivingEntity livingentity = ShadeEntity.this.getTarget();
            assert livingentity != null;
            if (ShadeEntity.this.getBoundingBox().inflate(1.0F).intersects(livingentity.getBoundingBox())) {
                ShadeEntity.this.swing(Hand.MAIN_HAND);
                ShadeEntity.this.doHurtTarget(livingentity);
            } else {
                double d0 = ShadeEntity.this.distanceToSqr(livingentity);
                if (d0 < 9.0D) {
                    Vector3d vector3d = livingentity.position();
                    ShadeEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
                }
            }

        }
    }

    class MoveRandomGoal extends Goal {
        public MoveRandomGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return !ShadeEntity.this.getMoveControl().hasWanted() && ShadeEntity.this.random.nextInt(7) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = ShadeEntity.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = ShadeEntity.this.blockPosition();
            }

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.offset(ShadeEntity.this.random.nextInt(15) - 7, ShadeEntity.this.random.nextInt(11) - 5, ShadeEntity.this.random.nextInt(15) - 7);
                if (ShadeEntity.this.level.isEmptyBlock(blockpos1)) {
                    ShadeEntity.this.moveControl.setWantedPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
                    if (ShadeEntity.this.getTarget() == null) {
                        ShadeEntity.this.getLookControl().setLookAt((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }

    class MoveHelperController extends MovementController {
        public MoveHelperController(ShadeEntity vex) {
            super(vex);
        }

        public void tick() {
            if (!ShadeEntity.this.isLeashed()) {
                if (this.operation == Action.MOVE_TO) {
                    Vector3d vector3d = new Vector3d(this.wantedX - ShadeEntity.this.getX(), this.wantedY - ShadeEntity.this.getY(), this.wantedZ - ShadeEntity.this.getZ());
                    double d0 = vector3d.length();
                    if (d0 < ShadeEntity.this.getBoundingBox().getSize()) {
                        this.operation = Action.WAIT;
                        ShadeEntity.this.setDeltaMovement(ShadeEntity.this.getDeltaMovement().scale(0.5D));
                    } else {
                        ShadeEntity.this.setDeltaMovement(ShadeEntity.this.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.05D / d0)));
                        if (ShadeEntity.this.getTarget() == null) {
                            Vector3d vector3d1 = ShadeEntity.this.getDeltaMovement();
                            ShadeEntity.this.yRot = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float) Math.PI);
                        } else {
                            double d2 = ShadeEntity.this.getTarget().getX() - ShadeEntity.this.getX();
                            double d1 = ShadeEntity.this.getTarget().getZ() - ShadeEntity.this.getZ();
                            ShadeEntity.this.yRot = -((float) MathHelper.atan2(d2, d1)) * (180F / (float) Math.PI);
                        }
                        ShadeEntity.this.yBodyRot = ShadeEntity.this.yRot;
                    }

                }
            } else {
                super.tick();
            }
        }
    }
}
