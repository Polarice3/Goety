package com.Polarice3.Goety.common.entities.hostile;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.ally.FriendlyVexEntity;
import com.Polarice3.Goety.common.entities.neutral.MinionEntity;
import com.Polarice3.Goety.common.entities.neutral.MutatedRabbitEntity;
import com.Polarice3.Goety.common.entities.projectiles.SoulBulletEntity;
import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.*;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

public class IrkEntity extends MonsterEntity {
    protected static final DataParameter<Byte> VEX_FLAGS = EntityDataManager.defineId(IrkEntity.class, DataSerializers.BYTE);
    public MobEntity owner;
    @Nullable
    private BlockPos boundOrigin;
    private boolean limitedLifespan;
    private int limitedLifeTicks;
    private int shootTime;

    public IrkEntity(EntityType<? extends IrkEntity> p_i50190_1_, World p_i50190_2_) {
        super(p_i50190_1_, p_i50190_2_);
        this.xpReward = 3;
        this.shootTime = 0;
        this.navigation = this.createNavigation(p_i50190_2_);
        this.moveControl = new IrkEntity.MoveHelperController(this);
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
        if (this.limitedLifespan && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.hurt(DamageSource.STARVE, 1.0F);
        }
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
        if (this.shootTime > 0){
            --this.shootTime;
        }
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new IrkEntity.OutofBoundsGoal());
        this.goalSelector.addGoal(2, new IrkEntity.FollowOwnerGoal(this, 0.5D, 6.0f, 3.0f, true));
        this.goalSelector.addGoal(4, new IrkEntity.ChargeAttackGoal());
        this.goalSelector.addGoal(8, new IrkEntity.MoveRandomGoal());
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new CopyOwnerTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    public void die(DamageSource cause) {
        if (cause.getEntity() instanceof LivingEntity) {
            ((LivingEntity) cause.getEntity()).heal(2.0F);
        }
        super.die(cause);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VEX_FLAGS, (byte)0);
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("BoundX")) {
            this.boundOrigin = new BlockPos(compound.getInt("BoundX"), compound.getInt("BoundY"), compound.getInt("BoundZ"));
        }

        if (compound.contains("LifeTicks")) {
            this.setLimitedLife(compound.getInt("LifeTicks"));
        }

        compound.getInt("shootTime");

    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (this.boundOrigin != null) {
            compound.putInt("BoundX", this.boundOrigin.getX());
            compound.putInt("BoundY", this.boundOrigin.getY());
            compound.putInt("BoundZ", this.boundOrigin.getZ());
        }

        if (this.limitedLifespan) {
            compound.putInt("LifeTicks", this.limitedLifeTicks);
        }

        compound.putInt("shootTime", this.shootTime);

    }

    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getEntity();
        if (entity instanceof IrkEntity){
            return false;
        } else {
            return super.hurt(source, amount);
        }
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (entityIn == this) {
            return true;
        } else if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof IrkEntity) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else if (entityIn instanceof LivingEntity && ((LivingEntity)entityIn).getMobType() == CreatureAttribute.ILLAGER) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    private boolean getVexFlag(int mask) {
        int i = this.entityData.get(VEX_FLAGS);
        return (i & mask) != 0;
    }

    private void setVexFlag(int mask, boolean value) {
        int i = this.entityData.get(VEX_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(VEX_FLAGS, (byte)(i & 255));
    }

    public boolean isCharging() {
        return this.getVexFlag(1);
    }

    public void setIsCharging(boolean charging) {
        this.setVexFlag(1, charging);
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos boundOriginIn) {
        this.boundOrigin = boundOriginIn;
    }

    public void setOwner(MobEntity ownerIn) {
        this.owner = ownerIn;
    }

    public void setLimitedLife(int limitedLifeTicksIn) {
        this.limitedLifespan = true;
        this.limitedLifeTicks = limitedLifeTicksIn;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.VEX_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.VEX_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.VEX_HURT;
    }

    public float getBrightness() {
        return 1.0F;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    class OutofBoundsGoal extends Goal {
        public OutofBoundsGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return IrkEntity.this.isInWall() && !IrkEntity.this.getMoveControl().hasWanted();
        }

        public boolean canContinueToUse() {
            return IrkEntity.this.isInWall() && !IrkEntity.this.getMoveControl().hasWanted();
        }

        public void tick() {
            BlockPos.Mutable blockpos$mutable = IrkEntity.this.blockPosition().mutable();
            blockpos$mutable.setY(IrkEntity.this.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
            IrkEntity.this.getMoveControl().setWantedPosition(blockpos$mutable.getX(), blockpos$mutable.getY(), blockpos$mutable.getZ(), 1.0F);
        }

    }

    class ChargeAttackGoal extends Goal {
        public ChargeAttackGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            if (IrkEntity.this.getTarget() != null && !IrkEntity.this.getMoveControl().hasWanted()) {
                return !IrkEntity.this.getTarget().isAlliedTo(IrkEntity.this);
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return IrkEntity.this.getMoveControl().hasWanted()
                    && IrkEntity.this.isCharging()
                    && IrkEntity.this.getTarget() != null
                    && IrkEntity.this.getTarget().isAlive();
        }

        public void start() {
            LivingEntity livingentity = IrkEntity.this.getTarget();
            assert livingentity != null;
            Vector3d vector3d = livingentity.getEyePosition(1.0F);
            if (IrkEntity.this.distanceTo(livingentity) > 4.0F) {
                IrkEntity.this.getMoveControl().setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0F);
            }
        }

        public void tick() {
            LivingEntity livingentity = IrkEntity.this.getTarget();
            if (livingentity != null) {
                Vector3d vector3d = livingentity.getEyePosition(1.0F);
                if (IrkEntity.this.shootTime == 10) {
                    double d1 = livingentity.getX() - IrkEntity.this.getX();
                    double d2 = livingentity.getY(0.5D) - IrkEntity.this.getY(0.5D);
                    double d3 = livingentity.getZ() - IrkEntity.this.getZ();
                    SoulBulletEntity smallFireballEntity = new SoulBulletEntity(IrkEntity.this.level, IrkEntity.this, d1, d2, d3);
                    smallFireballEntity.setPos(smallFireballEntity.getX(), IrkEntity.this.getY(0.5D), smallFireballEntity.getZ());
                    IrkEntity.this.level.addFreshEntity(smallFireballEntity);
                    IrkEntity.this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 2.0F);
                }
                IrkEntity.this.setIsCharging(IrkEntity.this.shootTime <= 10);
                if (IrkEntity.this.shootTime == 0){
                    IrkEntity.this.shootTime = 30;
                } else {
                    int k = (4 + random.nextInt(4)) * (random.nextBoolean() ? -1 : 1);
                    int l = (4 + random.nextInt(4)) * (random.nextBoolean() ? -1 : 1);
                    if (IrkEntity.this.distanceTo(livingentity) > 8.0F) {
                        IrkEntity.this.getMoveControl().setWantedPosition(vector3d.x + k, vector3d.y, vector3d.z + l, 1.0F);
                    } else {
                        IrkEntity.this.getMoveControl().setWantedPosition(IrkEntity.this.getX(), vector3d.y, IrkEntity.this.getZ(), 1.0F);
                    }
                }
                double d2 = IrkEntity.this.getTarget().getX() - IrkEntity.this.getX();
                double d1 = IrkEntity.this.getTarget().getZ() - IrkEntity.this.getZ();
                IrkEntity.this.yRot = -((float)MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
                IrkEntity.this.yBodyRot = IrkEntity.this.yRot;
            } else {
                IrkEntity.this.setIsCharging(false);
            }
        }
    }

    class CopyOwnerTargetGoal extends TargetGoal {
        private final EntityPredicate copyOwnerTargeting = (new EntityPredicate()).allowUnseeable().ignoreInvisibilityTesting();

        public CopyOwnerTargetGoal(CreatureEntity p_i47231_2_) {
            super(p_i47231_2_, false);
        }

        public boolean canUse() {
            return IrkEntity.this.owner != null && IrkEntity.this.owner.getTarget() != null && this.canAttack(IrkEntity.this.owner.getTarget(), this.copyOwnerTargeting);
        }

        public void start() {
            IrkEntity.this.setTarget(IrkEntity.this.owner.getTarget());
            super.start();
        }
    }

    class MoveRandomGoal extends Goal {
        public MoveRandomGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return !IrkEntity.this.getMoveControl().hasWanted()
                    && IrkEntity.this.random.nextInt(7) == 0
                    && !IrkEntity.this.isCharging();
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = IrkEntity.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = IrkEntity.this.blockPosition();
            }

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.offset(IrkEntity.this.random.nextInt(8) - 4, IrkEntity.this.random.nextInt(6) - 2, IrkEntity.this.random.nextInt(8) - 4);
                if (IrkEntity.this.level.isEmptyBlock(blockpos1)) {
                    IrkEntity.this.moveControl.setWantedPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
                    if (IrkEntity.this.getTarget() == null) {
                        IrkEntity.this.getLookControl().setLookAt((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }

    static class FollowOwnerGoal extends Goal {
        private final IrkEntity summonedEntity;
        private LivingEntity owner;
        private final IWorldReader level;
        private final double followSpeed;
        private final PathNavigator navigation;
        private int timeToRecalcPath;
        private final float maxDist;
        private final float minDist;
        private float oldWaterCost;
        private final boolean teleportToLeaves;

        public FollowOwnerGoal(IrkEntity summonedEntity, double speed, float minDist, float maxDist, boolean teleportToLeaves) {
            this.summonedEntity = summonedEntity;
            this.level = summonedEntity.level;
            this.followSpeed = speed;
            this.navigation = summonedEntity.getNavigation();
            this.minDist = minDist;
            this.maxDist = maxDist;
            this.teleportToLeaves = teleportToLeaves;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            if (!(summonedEntity.getNavigation() instanceof GroundPathNavigator) && !(summonedEntity.getNavigation() instanceof FlyingPathNavigator)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        public boolean canUse() {
            LivingEntity livingentity = this.summonedEntity.owner;
            if (livingentity == null) {
                return false;
            } else if (livingentity.isSpectator()) {
                return false;
            } else if (this.summonedEntity.distanceToSqr(livingentity) < (double)(this.minDist * this.minDist)) {
                return false;
            } else if (this.summonedEntity.getTarget() != null) {
                return false;
            } else {
                this.owner = livingentity;
                return true;
            }
        }

        public boolean canContinueToUse() {
            if (this.summonedEntity.getTarget() != null) {
                return false;
            } else if (this.navigation.isDone()){
                return false;
            } else {
                return !(this.summonedEntity.distanceToSqr(this.owner) <= (double)(this.maxDist * this.maxDist));
            }
        }

        public void start() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.summonedEntity.getPathfindingMalus(PathNodeType.WATER);
            this.summonedEntity.setPathfindingMalus(PathNodeType.WATER, 0.0F);
        }

        public void stop() {
            this.navigation.stop();
            this.summonedEntity.setPathfindingMalus(PathNodeType.WATER, this.oldWaterCost);
        }

        public void tick() {
            this.summonedEntity.getLookControl().setLookAt(this.owner, 10.0F, (float)this.summonedEntity.getMaxHeadXRot());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                if (this.summonedEntity.distanceTo(this.owner) > 8.0D) {
                    double x = MathHelper.floor(this.owner.getX()) - 2;
                    double y = MathHelper.floor(this.owner.getBoundingBox().minY);
                    double z = MathHelper.floor(this.owner.getZ()) - 2;
                    for(int l = 0; l <= 4; ++l) {
                        for(int i1 = 0; i1 <= 4; ++i1) {
                            if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.ValidPosition(new BlockPos(x + l, y + 2, z + i1))){
                                float a = (float) ((x + l) + 0.5F);
                                float b = (float) ((z + i1) + 0.5F);
                                this.summonedEntity.getMoveControl().setWantedPosition(a, y, b, this.followSpeed);
                                this.navigation.stop();
                            }
                        }
                    }
                }
                if (this.summonedEntity.distanceToSqr(this.owner) > 144.0 && MainConfig.VexTeleport.get()){
                    this.tryToTeleportNearEntity();
                }
            }
        }

        private void tryToTeleportNearEntity() {
            BlockPos blockpos = this.owner.blockPosition();

            for(int i = 0; i < 10; ++i) {
                int j = this.getRandomNumber(-3, 3);
                int k = this.getRandomNumber(-1, 1);
                int l = this.getRandomNumber(-3, 3);
                boolean flag = this.tryToTeleportToLocation(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
                if (flag) {
                    return;
                }
            }

        }

        private boolean tryToTeleportToLocation(int x, int y, int z) {
            if (Math.abs((double)x - this.owner.getX()) < 2.0D && Math.abs((double)z - this.owner.getZ()) < 2.0D) {
                return false;
            } else if (!this.isTeleportFriendlyBlock(new BlockPos(x, y, z))) {
                return false;
            } else {
                this.summonedEntity.moveTo((double)x + 0.5D, (double)y, (double)z + 0.5D, this.summonedEntity.yRot, this.summonedEntity.xRot);
                this.navigation.stop();
                return true;
            }
        }

        private boolean isTeleportFriendlyBlock(BlockPos pos) {
            PathNodeType pathnodetype = WalkNodeProcessor.getBlockPathTypeStatic(this.level, pos.mutable());
            if (pathnodetype != PathNodeType.WALKABLE) {
                return false;
            } else {
                BlockState blockstate = this.level.getBlockState(pos.below());
                if (!this.teleportToLeaves && blockstate.getBlock() instanceof LeavesBlock) {
                    return false;
                } else {
                    BlockPos blockpos = pos.subtract(this.summonedEntity.blockPosition());
                    return this.level.noCollision(this.summonedEntity, this.summonedEntity.getBoundingBox().move(blockpos));
                }
            }
        }

        protected boolean ValidPosition(BlockPos pos) {
            BlockState blockstate = this.level.getBlockState(pos);
            return (blockstate.canSurvive(this.level, pos) && this.level.isEmptyBlock(pos.above()) && this.level.isEmptyBlock(pos.above(2)));
        }

        private int getRandomNumber(int min, int max) {
            return this.summonedEntity.getRandom().nextInt(max - min + 1) + min;
        }
    }

    class MoveHelperController extends MovementController {
        public MoveHelperController(IrkEntity vex) {
            super(vex);
        }

        public void tick() {
            if (this.operation == Action.MOVE_TO) {
                Vector3d vector3d = new Vector3d(this.wantedX - IrkEntity.this.getX(), this.wantedY - IrkEntity.this.getY(), this.wantedZ - IrkEntity.this.getZ());
                double d0 = vector3d.length();
                if (d0 < IrkEntity.this.getBoundingBox().getSize()) {
                    this.operation = Action.WAIT;
                    IrkEntity.this.setDeltaMovement(IrkEntity.this.getDeltaMovement().scale(0.5D));
                } else {
                    IrkEntity.this.setDeltaMovement(IrkEntity.this.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.05D / d0)));
                    if (IrkEntity.this.getTarget() == null) {
                        Vector3d vector3d1 = IrkEntity.this.getDeltaMovement();
                        IrkEntity.this.yRot = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float)Math.PI);
                    } else {
                        double d2 = IrkEntity.this.getTarget().getX() - IrkEntity.this.getX();
                        double d1 = IrkEntity.this.getTarget().getZ() - IrkEntity.this.getZ();
                        IrkEntity.this.yRot = -((float)MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
                    }
                    IrkEntity.this.yBodyRot = IrkEntity.this.yRot;
                }

            }
        }
    }

}
