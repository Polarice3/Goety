package com.Polarice3.Goety.common.entities.hostile;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.projectiles.SoulSkullEntity;
import com.Polarice3.Goety.common.entities.utilities.LaserEntity;
import com.Polarice3.Goety.common.tileentities.PithosTileEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SkullLordEntity extends MonsterEntity{
    protected static final DataParameter<Byte> FLAGS = EntityDataManager.defineId(SkullLordEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Optional<UUID>> BONE_LORD = EntityDataManager.defineId(SkullLordEntity.class, DataSerializers.OPTIONAL_UUID);
    private static final DataParameter<Optional<UUID>> LASER = EntityDataManager.defineId(SkullLordEntity.class, DataSerializers.OPTIONAL_UUID);
    @Nullable
    private BlockPos boundOrigin;
    private int shootTime;
    private int spawnDelay = 100;
    private int spawnNumber = 0;
    private int chargeTime = 0;
    private int laserTime = 0;
    public float explosionRadius = 1.5F;
    public int boneLordRegen;
    private int hitTimes;

    public SkullLordEntity(EntityType<? extends SkullLordEntity> p_i50190_1_, World p_i50190_2_) {
        super(p_i50190_1_, p_i50190_2_);
        this.navigation = this.createNavigation(p_i50190_2_);
        this.shootTime = 0;
        this.moveControl = new MobUtil.MoveHelperController(this);
        this.hitTimes = 0;
        this.xpReward = 10;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new SoulSkullGoal());
        this.goalSelector.addGoal(4, new SkullLordEntity.ChargeAttackGoal());
        this.goalSelector.addGoal(8, new SkullLordEntity.MoveRandomGoal());
        this.goalSelector.addGoal(9, new SkullLordLookGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.MAX_HEALTH, 100.0D);
    }

    public void move(MoverType typeIn, Vector3d pos) {
        super.move(typeIn, pos);
        this.checkInsideBlocks();
    }

    protected PathNavigator createNavigation(World worldIn) {
        FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, worldIn);
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanPassDoors(false);
        return flyingpathnavigator;
    }

    public TileEntity getPithos(){
        if (this.getBoundOrigin() != null){
            return this.level.getBlockEntity(this.getBoundOrigin());
        }
        return null;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerable()){
            return false;
        } else {
            if (this.isCharging()){
                this.chargeTime = this.chargeTime + 100;
            }
            ++this.hitTimes;
            if (this.hitTimes > 3 || pAmount >= 20){
                return super.hurt(pSource, pAmount/2);
            } else {
                return super.hurt(pSource, pAmount);
            }
        }
    }

    @Override
    public boolean fireImmune() {
        return this.isInvulnerable();
    }

    protected boolean isAffectedByFluids() {
        return false;
    }

    public void tick() {
        super.tick();
        this.setNoGravity(true);
        int delay = 400;
        switch (this.level.getDifficulty()){
            case NORMAL:
                delay = 200;
                break;
            case HARD:
                delay = 100;
        }
        Vector3d vector3d = this.getDeltaMovement();
        double d0 = this.getX() + vector3d.x;
        double d1 = this.getY() + vector3d.y;
        double d2 = this.getZ() + vector3d.z;
        IParticleData particleData = ParticleTypes.SMOKE;
        if (this.isInvulnerable()){
            particleData = ParticleTypes.POOF;
        } else if (this.isCharging() || this.isLasering()){
            particleData = ParticleTypes.SOUL_FIRE_FLAME;
        }
        this.level.addParticle(particleData, d0 + this.random.nextGaussian() * (double)0.3F, d1 + this.random.nextGaussian() * (double)0.3F, d2 + this.random.nextGaussian() * (double)0.3F, 0.0D, 0.0D, 0.0D);
        if (this.shootTime > 0){
            --this.shootTime;
        }
        if (this.isInvulnerable()){
            if (this.tickCount % 20 == 0){
                this.heal(1.0F);
            }
            if (this.getLaser() != null) {
                this.getLaser().remove();
            }
        }
        if (this.isOnFire()){
            if (this.tickCount % 100 == 0 || this.isInvulnerable()){
                this.clearFire();
            }
        }
        if (this.getLaser() != null){
            this.lookControl.setLookAt(this.getLaser(), 90.0F, 90.0F);
        }
        if (this.getPithos() != null){
            BlockPos blockPos = this.getPithos().getBlockPos();
            if (this.distanceToSqr(blockPos.getX(), blockPos.getY(), blockPos.getZ()) > 1024){
                this.moveTo(blockPos, 0, 0);
                if (this.getBoneLord() != null){
                    this.getBoneLord().moveTo(blockPos, 0, 0);
                }
            }
        }
        if (this.getTarget() != null) {
            Vector3d vector3d1 = this.getTarget().getEyePosition(1.0F);
            if (this.isOnGround() || this.getTarget().getY() > this.getY()) {
                this.moveControl.setWantedPosition(this.getX(), this.getTarget().getY() + 1, this.getZ(), 1.0F);
            }

            if (!this.isInvulnerable()) {
                if (!this.isLasering()) {
                    int cooldown = this.isHalfHealth() ? 350 : 500;
                    if (this.tickCount % cooldown == 0) {
                        this.setLaserTime(true);
                    }
                }
            } else {
                this.setLaserTime(false);
            }

            if (this.isLaserTime()){
                if (!this.isCharging()) {
                    ++this.laserTime;
                    if (this.distanceToSqr(this.getTarget()) < 9.0D) {
                        double nx = this.getTarget().getX() - this.getX();
                        double nz = this.getTarget().getZ() - this.getZ();
                        this.moveControl.setWantedPosition(nx, vector3d1.y, nz, 1.0F);
                    }
                    if (this.laserTime == 30) {
                        this.playSound(SoundEvents.BEACON_ACTIVATE, 2.0F, 1.0F);
                        if (this.level.getDifficulty() == Difficulty.HARD){
                            this.spawnMobs();
                        }
                    }
                    if (this.laserTime >= 30){
                        if (this.level.isClientSide){
                            new ParticleUtil(ModParticleTypes.LASER_GATHER.get(), this, this.level);
                        }
                        if (!this.level.isClientSide) {
                            ServerWorld serverWorld = (ServerWorld) this.level;
                            new ServerParticleUtil(ModParticleTypes.LASER_GATHER.get(), this, serverWorld);
                        }
                    }
                    if (this.laserTime >= 60) {
                        LaserEntity laserEntity = ModEntityType.LASER.get().create(this.level);
                        if (laserEntity != null) {
                            this.playSound(SoundEvents.END_PORTAL_SPAWN, 3.0F, 1.0F);
                            this.setLaserTime(false);
                            laserEntity.setSkullLord(this);
                            laserEntity.setDuration(200);
                            laserEntity.setPos(this.getX(), this.getY(), this.getZ());
                            laserEntity.setTarget(this.getTarget());
                            this.setLaser(laserEntity);
                            this.level.addFreshEntity(laserEntity);
                        }
                    }
                }
            } else {
                this.laserTime = 0;
            }

            boolean flag = false;

            if (this.tickCount % 20 == 0){
                if (this.xOld == this.getX() && this.yOld == this.getY() && this.zOld == this.getZ()){
                    flag = true;
                    if (!this.isInvulnerable() && !this.isLasering() && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                        this.level.explode(this, this.getX(), this.getY(), this.getZ(), 2.0F, Explosion.Mode.DESTROY);
                    }
                }
            }

            if (!this.level.getBlockState(this.blockPosition().relative(Direction.WEST)).isAir()
            && !this.level.getBlockState(this.blockPosition().relative(Direction.NORTH)).isAir()) {
                flag = true;
            }
            if (!this.level.getBlockState(this.blockPosition().relative(Direction.WEST)).isAir()
                    && !this.level.getBlockState(this.blockPosition().relative(Direction.SOUTH)).isAir()) {
                flag = true;
            }
            if (!this.level.getBlockState(this.blockPosition().relative(Direction.EAST)).isAir()
                    && !this.level.getBlockState(this.blockPosition().relative(Direction.SOUTH)).isAir()) {
                flag = true;
            }
            if (!this.level.getBlockState(this.blockPosition().relative(Direction.EAST)).isAir()
                    && !this.level.getBlockState(this.blockPosition().relative(Direction.SOUTH)).isAir()) {
                flag = true;
            }

            if (flag){
                if (!this.isInvulnerable() || this.isLaserTime() || this.isLasering()) {
                    if (this.distanceToSqr(this.getTarget()) > 4.0F) {
                        this.moveControl.setWantedPosition(vector3d1.x, vector3d1.y, vector3d1.z, 1.0F);
                    } else {
                        double nx = this.getTarget().getX() - this.getX();
                        double nz = this.getTarget().getZ() - this.getZ();
                        this.moveControl.setWantedPosition(nx, vector3d1.y, nz, 1.0F);
                    }
                } else {
                    this.moveControl.setWantedPosition(vector3d1.x, vector3d1.y, vector3d1.z, 1.0F);
                }
            }
        } else {
            if (this.getPithos() != null) {
                if (this.getPithos() instanceof PithosTileEntity) {
                    PithosTileEntity pithosTile = (PithosTileEntity) this.getPithos();
                    if (this.tickCount % 100 == 0) {
                        this.setisDespawn(true);
                        pithosTile.lock();
                        if (this.getBoneLord() != null) {
                            this.getBoneLord().remove();
                        }
                        if (this.getLaser() != null){
                            this.getLaser().remove();
                        }
                        this.remove();
                    }
                }
            }
        }
        if (!this.level.isClientSide){
            ServerWorld serverWorld = (ServerWorld) this.level;
            int i = this.blockPosition().getX();
            int j = this.blockPosition().getY();
            int k = this.blockPosition().getZ();
            BlockPos spawn = new BlockPos(i, j, k);
            List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(8.0D, 8.0D, 8.0D));
            if (list.size() < 8 && this.spawnNumber < 10 && this.getTarget() != null && MonsterEntity.isDarkEnoughToSpawn(serverWorld, spawn, this.random)){
                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                } else {
                    this.spawnDelay = this.level.random.nextInt(delay) + delay;
                    this.spawnMobs();
                }
            }
            if (this.getBoneLord() == null){
                if (!this.isLaserTime()){
                    --this.boneLordRegen;
                }
                this.setisInvulnerable(false);
                for (BoneLordEntity boneLordEntity : this.level.getEntitiesOfClass(BoneLordEntity.class, this.getBoundingBox().inflate(32))){
                    if (boneLordEntity.getSkullLord() == this){
                        this.setBoneLord(boneLordEntity);
                    }
                }
                if (this.boneLordRegen <= 0 && !this.isLasering()){
                    BoneLordEntity boneLord = ModEntityType.BONE_LORD.get().create(this.level);
                    if (boneLord != null){
                        boneLord.finalizeSpawn(serverWorld, this.level.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                        boneLord.setPos(this.getX(), this.getY(), this.getZ());
                        boneLord.setSkullLord(this);
                        this.setBoneLord(boneLord);
                        this.level.addFreshEntity(boneLord);
                    }
                }
            } else {
                if (this.getBoneLord().isDeadOrDying()){
                    this.setBoneLordUUID(null);
                } else {
                    if (this.distanceToSqr(this.getBoneLord()) > 400) {
                        this.moveTo(this.getBoneLord().position());
                    }
                }
                this.setisInvulnerable(true);
                this.hitTimes = 0;
                this.boneLordRegen = delay * 2;
            }
            if (this.isCharging()){
                ++this.chargeTime;
                for (LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2.0F))){
                    if (!(livingEntity instanceof BoneLordEntity) && livingEntity != this && livingEntity != this.getTarget()) {
                        if (this.getBoundingBox().intersects(livingEntity.getBoundingBox())) {
                            this.level.explode(this, this.getX(), this.getY(), this.getZ(), explosionRadius, Explosion.Mode.NONE);
                            if (this.random.nextFloat() < 0.25F){
                                this.setIsCharging(false);
                            }
                        }
                    }
                }
                if (this.horizontalCollision || this.verticalCollision){
                    this.level.explode(this, this.getX(), this.getY(), this.getZ(), explosionRadius, Explosion.Mode.NONE);
                    this.setIsCharging(false);
                }
                if (this.chargeTime >= 200){
                    this.setIsCharging(false);
                }
            } else {
                this.chargeTime = 0;
            }
        }
    }

    public void spawnMobs(){
        int spawnRange = 2;
        boolean random = this.level.random.nextBoolean();
        for (int p = 0; p < 1 + this.level.random.nextInt(2); ++p) {
            double d3 = (double)this.blockPosition().getX() + (this.level.random.nextDouble() - this.level.random.nextDouble()) * (double) spawnRange + 0.5D;
            double d4 = (double)(this.blockPosition().getY() + this.level.random.nextInt(3));
            double d5 = (double)this.blockPosition().getZ() + (this.level.random.nextDouble() - this.level.random.nextDouble()) * (double) spawnRange + 0.5D;
            MonsterEntity monster;
            if (this.isUnderWater()){
                monster = EntityType.DROWNED.create(this.level);
            } else {
                if (random){
                    monster = EntityType.ZOMBIE.create(this.level);
                } else {
                    monster = EntityType.SKELETON.create(this.level);
                }
            }
            if (monster != null) {
                BlockPos blockPos = new BlockPos(d3, d4, d5);
                if (!this.level.getBlockState(blockPos).isAir()){
                    monster.moveTo(this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ());
                } else {
                    monster.moveTo(d3, d4, d5);
                }
                monster.addTag(ConstantPaths.skullLordMinion());
                monster.finalizeSpawn((IServerWorld) this.level, this.level.getCurrentDifficultyAt(monster.blockPosition()), SpawnReason.SPAWNER, (ILivingEntityData) null, (CompoundNBT) null);
                monster.spawnAnim();
                ((IServerWorld) this.level).addFreshEntityWithPassengers(monster);
                ++this.spawnNumber;
            }
        }
    }

    public void die(DamageSource cause) {
        super.die(cause);
        SoundEvent soundevent = SoundEvents.ENDERMAN_DEATH;
        this.playSound(soundevent, 2.0F, 0.15F);
        for(int i = 0; i < this.level.random.nextInt(35) + 10; ++i) {
            float f11 = (this.random.nextFloat() - 0.5F);
            float f13 = (this.random.nextFloat() - 0.5F);
            float f14 = (this.random.nextFloat() - 0.5F);
            this.level.addParticle(ParticleTypes.EXPLOSION, this.getX() + (double)f11, this.getY() + 2.0D + (double)f13, this.getZ() + (double)f14, 0.0D, 0.0D, 0.0D);
        }
        if (this.getBoneLord() != null){
            this.getBoneLord().die(cause);
        }
        if (this.getLaser() != null){
            this.getLaser().remove();
        }
        if (this.getPithos() != null){
            if (this.getPithos() instanceof PithosTileEntity){
                PithosTileEntity pithosTile = (PithosTileEntity) this.getPithos();
                pithosTile.unlock();
            }
        }
    }

    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (this.getBoneLord() != null){
            this.getBoneLord().remove();
        }
        if (this.getLaser() != null){
            this.getLaser().remove();
        }
        if (!this.isDespawn()) {
            if (this.getPithos() != null) {
                if (this.getPithos() instanceof PithosTileEntity) {
                    PithosTileEntity pithosTile = (PithosTileEntity) this.getPithos();
                    pithosTile.unlock();
                }
            }
        }
    }

    public void makeStuckInBlock(BlockState pState, Vector3d pMotionMultiplier) {
        if (!pState.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(pState, pMotionMultiplier);
        }

    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITHER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.WITHER_HURT;
    }

    public void playAmbientSound() {
        SoundEvent soundevent = this.getAmbientSound();
        if (soundevent != null) {
            this.playSound(soundevent, 1.0F, 0.5F);
        }
    }

    protected void playHurtSound(DamageSource pSource) {
        SoundEvent soundevent = this.getHurtSound(pSource);
        if (soundevent != null) {
            this.playSound(soundevent, 1.0F, 0.5F);
        }
    }

    protected SoundEvent getDeathSound() {
        return null;
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof MonsterEntity && ((MonsterEntity) entityIn).getMobType() == CreatureAttribute.UNDEAD) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FLAGS, (byte)0);
        this.entityData.define(BONE_LORD, Optional.empty());
        this.entityData.define(LASER, Optional.empty());
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
    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos pBoundOrigin) {
        this.boundOrigin = pBoundOrigin;
    }

    @Nullable
    public BoneLordEntity getBoneLord() {
        try {
            UUID uuid = this.getBoneLordUUID();
            if (uuid != null){
                if (EntityFinder.getLivingEntityByUuiD(uuid) instanceof BoneLordEntity){
                    return (BoneLordEntity) EntityFinder.getLivingEntityByUuiD(uuid);
                }
            }
            return null;
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    @Nullable
    public UUID getBoneLordUUID() {
        return this.entityData.get(BONE_LORD).orElse(null);
    }

    public void setBoneLordUUID(UUID uuid){
        this.entityData.set(BONE_LORD, Optional.ofNullable(uuid));
    }

    public void setBoneLord(BoneLordEntity boneLord){
        this.setBoneLordUUID(boneLord.getUUID());
    }

    public boolean isLasering(){
        return this.getLaser() != null;
    }

    @Nullable
    public LaserEntity getLaser() {
        try {
            UUID uuid = this.getLaserUUID();
            if (uuid != null){
                if (EntityFinder.getLivingEntityByUuiD(uuid) instanceof LaserEntity){
                    return (LaserEntity) EntityFinder.getLivingEntityByUuiD(uuid);
                }
            }
            return null;
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    @Nullable
    public UUID getLaserUUID() {
        return this.entityData.get(LASER).orElse(null);
    }

    public void setLaserUUID(UUID uuid){
        this.entityData.set(LASER, Optional.ofNullable(uuid));
    }

    public void setLaser(LaserEntity laser){
        this.setLaserUUID(laser.getUUID());
    }

    public boolean isCharging() {
        return this.geFlags(1);
    }

    public void setIsCharging(boolean charging) {
        this.setFlags(1, charging);
    }

    public boolean isInvulnerable() {
        return this.geFlags(2);
    }

    public void setisInvulnerable(boolean invulnerable) {
        this.setFlags(2, invulnerable);
    }

    public boolean isDespawn() {
        return this.geFlags(4);
    }

    public void setisDespawn(boolean invulnerable) {
        this.setFlags(4, invulnerable);
    }

    public boolean isLaserTime() {
        return this.geFlags(8);
    }

    public void setLaserTime(boolean laserTime) {
        this.setFlags(8, laserTime);
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("BoundX")) {
            this.boundOrigin = new BlockPos(pCompound.getInt("BoundX"), pCompound.getInt("BoundY"), pCompound.getInt("BoundZ"));
        }
        UUID uuid;
        if (pCompound.hasUUID("boneLord")) {
            uuid = pCompound.getUUID("boneLord");
        } else {
            String s = pCompound.getString("boneLord");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setBoneLordUUID(uuid);
            } catch (Throwable ignored) {
            }
        }
        if (pCompound.hasUUID("laser")) {
            uuid = pCompound.getUUID("laser");
        } else {
            String s = pCompound.getString("laser");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setLaserUUID(uuid);
            } catch (Throwable ignored) {
            }
        }
        this.shootTime = pCompound.getInt("shootTime");
        this.hitTimes = pCompound.getInt("hitTimes");
        this.boneLordRegen = pCompound.getInt("boneLordRegen");
        this.spawnDelay = pCompound.getInt("spawnDelay");
        this.spawnNumber = pCompound.getInt("spawnNumber");
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.boundOrigin != null) {
            pCompound.putInt("BoundX", this.boundOrigin.getX());
            pCompound.putInt("BoundY", this.boundOrigin.getY());
            pCompound.putInt("BoundZ", this.boundOrigin.getZ());
        }
        if (this.getBoneLordUUID() != null) {
            pCompound.putUUID("boneLord", this.getBoneLordUUID());
        }
        if (this.getLaserUUID() != null) {
            pCompound.putUUID("laser", this.getLaserUUID());
        }
        pCompound.putInt("shootTime", this.shootTime);
        pCompound.putInt("hitTimes", this.hitTimes);
        pCompound.putInt("boneLordRegen", this.boneLordRegen);
        pCompound.putInt("spawnDelay", this.spawnDelay);
        pCompound.putInt("spawnNumber", this.spawnNumber);
    }

    public float getBrightness() {
        return 1.0F;
    }

    public boolean isHalfHealth(){
        return this.getHealth() <= this.getMaxHealth()/2;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld pLevel, DifficultyInstance pDifficulty, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
        this.populateDefaultEquipmentSlots(pDifficulty);
        this.populateDefaultEquipmentEnchantments(pDifficulty);
        BoneLordEntity boneLord = ModEntityType.BONE_LORD.get().create((World) pLevel);
        if (boneLord != null){
            boneLord.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
            boneLord.setPos(this.getX(), this.getY(), this.getZ());
            boneLord.setSkullLord(this);
            this.setBoneLord(boneLord);
            pLevel.addFreshEntity(boneLord);
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    class SoulSkullGoal extends Goal {
        public SoulSkullGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            if (SkullLordEntity.this.getTarget() != null
                    && !SkullLordEntity.this.getMoveControl().hasWanted()
                    && SkullLordEntity.this.isInvulnerable()) {
                return !SkullLordEntity.this.getTarget().isAlliedTo(SkullLordEntity.this);
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return SkullLordEntity.this.getMoveControl().hasWanted()
                    && SkullLordEntity.this.getTarget() != null
                    && SkullLordEntity.this.getTarget().isAlive()
                    && SkullLordEntity.this.getBoneLord() != null;
        }

        public void tick() {
            LivingEntity livingentity = SkullLordEntity.this.getTarget();
            if (livingentity != null) {
                int shoot = 20;
                if (SkullLordEntity.this.isHalfHealth()){
                    shoot = 15;
                }
                if (SkullLordEntity.this.shootTime == 0) {
                    double d1 = livingentity.getX() - SkullLordEntity.this.getX();
                    double d2 = livingentity.getY(0.5D) - SkullLordEntity.this.getY(0.5D);
                    double d3 = livingentity.getZ() - SkullLordEntity.this.getZ();
                    SoulSkullEntity soulSkull = new SoulSkullEntity(SkullLordEntity.this.level, SkullLordEntity.this, d1, d2, d3);
                    soulSkull.setPos(soulSkull.getX(), SkullLordEntity.this.getY(0.75D), soulSkull.getZ());
                    if (SkullLordEntity.this.level.random.nextFloat() <= 0.05F){
                        soulSkull.setDangerous(true);
                        SkullLordEntity.this.shootTime = shoot + 40;
                    } else {
                        SkullLordEntity.this.shootTime = shoot;
                    }
                    SkullLordEntity.this.level.addFreshEntity(soulSkull);
                    SkullLordEntity.this.playSound(SoundEvents.WITHER_SHOOT, 1.0F, 1.0F);
                }
                double d2 = SkullLordEntity.this.getTarget().getX() - SkullLordEntity.this.getX();
                double d1 = SkullLordEntity.this.getTarget().getZ() - SkullLordEntity.this.getZ();
                SkullLordEntity.this.yRot = -((float) MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
                SkullLordEntity.this.yBodyRot = SkullLordEntity.this.yRot;
            }
        }
    }

    class ChargeAttackGoal extends Goal {
        public ChargeAttackGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            if (SkullLordEntity.this.getTarget() != null
                    && !SkullLordEntity.this.getMoveControl().hasWanted()
                    && SkullLordEntity.this.getBoneLord() == null
                    && SkullLordEntity.this.distanceToSqr(SkullLordEntity.this.getTarget()) > 4.0D
                    && !SkullLordEntity.this.isLaserTime()
                    && !SkullLordEntity.this.isLasering()) {
                if (!SkullLordEntity.this.isHalfHealth()){
                    return SkullLordEntity.this.random.nextInt(7) == 0;
                } else {
                    return SkullLordEntity.this.random.nextInt(3) == 0;
                }
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return SkullLordEntity.this.getMoveControl().hasWanted()
                    && SkullLordEntity.this.isCharging()
                    && SkullLordEntity.this.getTarget() != null
                    && SkullLordEntity.this.getBoneLord() == null
                    && SkullLordEntity.this.getTarget().isAlive()
                    && !SkullLordEntity.this.isLaserTime()
                    && !SkullLordEntity.this.isLasering();
        }

        public void start() {
            LivingEntity livingentity = SkullLordEntity.this.getTarget();
            assert livingentity != null;
            Vector3d vector3d = livingentity.getEyePosition(1.0F);
            SkullLordEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, SkullLordEntity.this.isHalfHealth() ? 1.25D : 1.0D);
            SkullLordEntity.this.setIsCharging(true);
            SkullLordEntity.this.playSound(SoundEvents.ENDERMAN_SCREAM, 1.0F, 0.5F);
        }

        public void stop() {
            SkullLordEntity.this.setIsCharging(false);
        }

        public void tick() {
            SkullLordEntity skullLord = SkullLordEntity.this;
            LivingEntity livingentity = SkullLordEntity.this.getTarget();
            assert livingentity != null;
            if (skullLord.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                skullLord.doHurtTarget(livingentity);
                if (skullLord.isOnFire()){
                    livingentity.setSecondsOnFire(5);
                }
                skullLord.level.explode(skullLord, skullLord.getX(), skullLord.getY(), skullLord.getZ(), skullLord.explosionRadius, Explosion.Mode.NONE);
                skullLord.setIsCharging(false);
            } else {
                double d0 = skullLord.distanceToSqr(livingentity);
                if (d0 < 9.0D) {
                    Vector3d vector3d = livingentity.getEyePosition(1.0F);
                    skullLord.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, SkullLordEntity.this.isHalfHealth() ? 1.25D : 1.0D);
                }
            }

        }
    }

    class MoveRandomGoal extends Goal {
        public MoveRandomGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return !SkullLordEntity.this.getMoveControl().hasWanted() && SkullLordEntity.this.random.nextInt(7) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = SkullLordEntity.this.blockPosition();
            if (SkullLordEntity.this.getBoundOrigin() != null) {
                blockpos = SkullLordEntity.this.getBoundOrigin();
            } else if (SkullLordEntity.this.getBoneLord() != null){
                blockpos = SkullLordEntity.this.getBoneLord().blockPosition();
            }

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.offset(SkullLordEntity.this.random.nextInt(15) - 7, SkullLordEntity.this.random.nextInt(11) - 5, SkullLordEntity.this.random.nextInt(15) - 7);
                if (SkullLordEntity.this.level.isEmptyBlock(blockpos1)) {
                    SkullLordEntity.this.moveControl.setWantedPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
                    if (SkullLordEntity.this.getTarget() == null) {
                        SkullLordEntity.this.getLookControl().setLookAt((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }

    static class SkullLordLookGoal extends LookAtGoal{
        private final SkullLordEntity skullLord;

        public SkullLordLookGoal(SkullLordEntity p_i1631_1_) {
            super(p_i1631_1_, PlayerEntity.class, 3.0F, 1.0F);
            this.skullLord = p_i1631_1_;
        }

        public boolean canUse() {
            if (this.skullLord.getLaser() != null){
                return false;
            } else {
                return super.canUse();
            }
        }
    }

}
