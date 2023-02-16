package com.Polarice3.Goety.common.entities.hostile;

import com.Polarice3.Goety.AttributesConfig;
import com.Polarice3.Goety.common.entities.ai.SpewingAttackGoal;
import com.Polarice3.Goety.common.entities.hostile.cultists.AbstractCultistEntity;
import com.Polarice3.Goety.common.entities.neutral.ICustomAttributes;
import com.Polarice3.Goety.common.entities.neutral.ISpewing;
import com.Polarice3.Goety.common.entities.utilities.PoisonGroundEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.EntityFinder;
import com.Polarice3.Goety.utils.ModDamageSource;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.*;

public class HogLordEntity extends MonsterEntity implements IFlinging, ISpewing, ICustomAttributes {
    private int attackAnimationRemainingTicks;
    private int allyCooldown;
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.defineId(HogLordEntity.class, DataSerializers.OPTIONAL_UUID);
    protected static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.defineId(HogLordEntity.class, DataSerializers.BYTE);
    protected static final DataParameter<Boolean> SPEWING = EntityDataManager.defineId(HogLordEntity.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> MURKING = EntityDataManager.defineId(HogLordEntity.class, DataSerializers.BOOLEAN);

    public HogLordEntity(EntityType<? extends HogLordEntity> p_i231569_1_, World p_i231569_2_) {
        super(p_i231569_1_, p_i231569_2_);
        ICustomAttributes.applyAttributesForEntity(p_i231569_1_, this);
        this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
        this.setPathfindingMalus(PathNodeType.LAVA, 0.0F);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.maxUpStep = 1.0F;
        this.xpReward = 25;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(3, new MurkyGroundGoal(this));
        this.goalSelector.addGoal(4, new SpewingAttackGoal<>(this, 15.0F, 15, 0.01F));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 15.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this).setAlertOthers()));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, AbstractCultistEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this, WitchEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, AttributesConfig.HoglordHealth.get())
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75D)
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.ATTACK_DAMAGE, AttributesConfig.HoglordDamage.get());
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
        this.entityData.define(SPEWING, false);
        this.entityData.define(MURKING, false);
    }

    public boolean ignoreExplosion(){
        return this.tickCount < 20;
    }

    private boolean getHogLordFlag(int mask) {
        int i = this.entityData.get(DATA_FLAGS_ID);
        return (i & mask) != 0;
    }

    private void setHogLordFlag(int mask, boolean value) {
        int i = this.entityData.get(DATA_FLAGS_ID);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(DATA_FLAGS_ID, (byte)(i & 255));
    }

    @Override
    public boolean isSpewing() {
        return this.entityData.get(SPEWING);
    }

    @Override
    public void setSpewing(boolean flag) {
        this.entityData.set(SPEWING, flag);
    }

    public boolean isMurking() {
        return this.entityData.get(MURKING);
    }

    public void setMurking(boolean flag) {
        this.entityData.set(MURKING, flag);
    }

    @Override
    public void doSpewing(Entity target) {
        if (!target.fireImmune()) {
            target.setSecondsOnFire(10);
            target.hurt(ModDamageSource.fireBreath(this), 2.0F);
        }
    }

    public boolean doHurtTarget(Entity pEntity) {
        if (!(pEntity instanceof LivingEntity)) {
            return false;
        } else {
            this.attackAnimationRemainingTicks = 10;
            this.level.broadcastEntityEvent(this, (byte)4);
            this.playSound(SoundEvents.HOGLIN_ATTACK, 1.0F, this.getVoicePitch());
            pEntity.setSecondsOnFire(5);
            return IFlinging.hurtAndThrowTarget(this, (LivingEntity)pEntity);
        }
    }

    public boolean canBeAffected(EffectInstance pPotioneffect) {
        if (pPotioneffect.getEffect() == Effects.POISON) {
            net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent event = new net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent(this, pPotioneffect);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
            return event.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW;
        }
        return super.canBeAffected(pPotioneffect);
    }

    public boolean canStandOnFluid(Fluid p_230285_1_) {
        return p_230285_1_.is(FluidTags.LAVA);
    }

    protected void blockedByShield(LivingEntity pEntity) {
        IFlinging.throwTarget(this, pEntity);
    }

    public LivingEntity getTrueOwner() {
        try {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : EntityFinder.getLivingEntityByUuiD(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    @Nullable
    public UUID getOwnerId() {
        return this.entityData.get(OWNER_UNIQUE_ID).orElse((UUID)null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    private void teleport(double pX, double pY, double pZ) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(pX, pY, pZ);

        while(blockpos$mutable.getY() > 0 && !this.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
            blockpos$mutable.move(Direction.DOWN);
        }

        BlockState blockstate = this.level.getBlockState(blockpos$mutable);
        boolean flag = blockstate.getMaterial().blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        if (flag && !flag1) {
            net.minecraftforge.event.entity.living.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory.onEnderTeleport(this, pX, pY, pZ);
            if (event.isCanceled()) return;
            boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (flag2 && !this.isSilent()) {
                this.level.playSound((PlayerEntity)null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
        }
    }

    private void teleportTowards(Entity entity) {
        Vector3d vector3d = new Vector3d(this.getX() - entity.getX(), this.getY(0.5D) - entity.getEyeY(), this.getZ() - entity.getZ());
        vector3d = vector3d.normalize();
        double d0 = 16.0D;
        double d1 = this.getX() + (this.random.nextDouble() - 0.5D) * 8.0D - vector3d.x * d0;
        double d2 = this.getY() + (double)(this.random.nextInt(16) - 8) - vector3d.y * d0;
        double d3 = this.getZ() + (this.random.nextDouble() - 0.5D) * 8.0D - vector3d.z * d0;
        this.teleport(d1, d2, d3);
    }

    public void aiStep() {
        if (this.attackAnimationRemainingTicks > 0 && !this.isSpewing()) {
            --this.attackAnimationRemainingTicks;
        }

        if (this.allyCooldown > 0){
            --this.allyCooldown;
        }

        if (this.getTrueOwner() != null){
            if (!this.getTrueOwner().isDeadOrDying()) {
                if (this.distanceTo(this.getTrueOwner()) >= 32) {
                    this.teleportTowards(this.getTrueOwner());
                }
            }
        }

        if (this.isInWater()){
            this.hurt(DamageSource.DROWN, 1.0F);
        }

        if (this.tickCount % 20 == 0) {
            this.heal(1.0F);
        }

        if (this.isMurking()){
            this.animationSpeed += 0.6F;
        }

        if (isSpewing()) {
            if (this.level.isClientSide) {
                Vector3d lookAngle = this.getLookAngle();

                double distance = 0.9;
                double px = this.getX() + lookAngle.x * distance;
                double py = this.getY() + 1.5F + lookAngle.y * distance;
                double pz = this.getZ() + lookAngle.z * distance;

                for (int i = 0; i < 2; i++) {
                    double dx = lookAngle.x;
                    double dy = lookAngle.y;
                    double dz = lookAngle.z;

                    double spread = 5.0D + this.random.nextDouble() * 2.5D;
                    double velocity = 0.6D + this.random.nextDouble() * 0.6D;

                    dx += this.random.nextGaussian() * 0.007499999832361937D * spread;
                    dy += this.random.nextGaussian() * 0.007499999832361937D * spread;
                    dz += this.random.nextGaussian() * 0.007499999832361937D * spread;
                    dx *= velocity;
                    dy *= velocity;
                    dz *= velocity;

                    this.level.addParticle(ParticleTypes.FLAME, px, py, pz, dx, dy, dz);
                }
            }

            this.attackAnimationRemainingTicks = 2;
            this.playSound(ModSounds.FIRE_BREATH.get(), random.nextFloat() * 0.5F, random.nextFloat() * 0.5F);
        }

        List<MobEntity> minions = new ArrayList<>();

        for (PiglinEntity piglinEntity : this.level.getEntitiesOfClass(PiglinEntity.class, this.getBoundingBox().inflate(32.0D))){
            Brain<PiglinEntity> brain = piglinEntity.getBrain();
            brain.eraseMemory(MemoryModuleType.ATTACK_TARGET);
            brain.eraseMemory(MemoryModuleType.UNIVERSAL_ANGER);
            brain.setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, this, 200L);
        }

        for (ZoglinEntity zoglinEntity : this.level.getEntitiesOfClass(ZoglinEntity.class, this.getBoundingBox().inflate(32.0D))){
            Brain<ZoglinEntity> brain = zoglinEntity.getBrain();
            if (brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET) && brain.getMemory(MemoryModuleType.ATTACK_TARGET).get() == this){
                brain.eraseMemory(MemoryModuleType.ATTACK_TARGET);
            }
            zoglinEntity.setSilent(this.getTarget() == null);
            minions.add(zoglinEntity);
            if (this.getTarget() != null){
                brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
                brain.eraseMemory(MemoryModuleType.BREED_TARGET);
                brain.setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, this.getTarget(), 200L);
            }
        }

        for (HoglinEntity hoglinEntity : this.level.getEntitiesOfClass(HoglinEntity.class, this.getBoundingBox().inflate(32.0D))){
            Brain<HoglinEntity> brain = hoglinEntity.getBrain();
            minions.add(hoglinEntity);
            if (this.getTarget() != null){
                brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
                brain.eraseMemory(MemoryModuleType.BREED_TARGET);
                brain.setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, this.getTarget(), 200L);
            }

        }

        if (minions.size() > 0) {
            if (this.tickCount % 300 == 0){
                this.playSound(ModSounds.HOGLORD_RAGE.get(), 1.0F, 1.0F);
            }
            int amp = MathHelper.clamp(minions.size(), 1, 5) - 1;
            this.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 20, amp));
            this.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 20, amp));
        }

        if (this.getTarget() != null){
            this.setIsRaging(this.distanceTo(this.getTarget()) > 20.0F);
            if (this.horizontalCollision && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                boolean flag = false;
                AxisAlignedBB axisalignedbb = this.getBoundingBox().inflate(0.5D);

                for (BlockPos blockpos : BlockPos.betweenClosed(MathHelper.floor(axisalignedbb.minX), MathHelper.floor(axisalignedbb.minY), MathHelper.floor(axisalignedbb.minZ), MathHelper.floor(axisalignedbb.maxX), MathHelper.floor(axisalignedbb.maxY), MathHelper.floor(axisalignedbb.maxZ))) {
                    BlockState blockstate = this.level.getBlockState(blockpos);
                    Block block = blockstate.getBlock();
                    if (block instanceof LeavesBlock){
                        flag = this.level.destroyBlock(blockpos, true, this) || flag;
                    }
                    if ((blockstate.getMaterial() == Material.WOOD || blockstate.is(BlockTags.WALLS) || blockstate.is(BlockTags.SLABS) || blockstate.is(BlockTags.STAIRS)) && !block.hasTileEntity(blockstate)) {
                        flag = this.level.destroyBlock(blockpos, true, this) || flag;
                        this.playSound(SoundEvents.WITHER_BREAK_BLOCK, 0.75F, 1.0F);
                    }
                }

                if (!flag && this.onGround) {
                    this.jumpFromGround();
                }
            }
        } else {
            this.setIsRaging(false);
        }

        if (this.level.isClientSide) {
            if (this.isRaging()) {
                if (this.tickCount % 20 == 0) {
                    this.addParticlesAroundSelf(ParticleTypes.ANGRY_VILLAGER);
                }
            }
        }

        if (this.isRaging()){
            this.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 20, 2));
            if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                if (getHorizontalDistanceSqr(this.getDeltaMovement()) > (double) 2.5000003E-7F) {
                    this.level.setBlockAndUpdate(this.blockPosition(), Blocks.FIRE.defaultBlockState());
                }
            }
            if (this.tickCount % 600 == 0){
                this.playSound(ModSounds.HOGLORD_RAGE.get(), 1.0F, 1.0F);
            }
        }

        super.aiStep();
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource != DamageSource.DROWN) {
            if (this.level instanceof ServerWorld) {
                ServerWorld serverworld = (ServerWorld) this.level;
                LivingEntity livingentity = this.getTarget();
                if (livingentity == null && pSource.getEntity() instanceof LivingEntity) {
                    livingentity = (LivingEntity) pSource.getEntity();
                }

                int i = MathHelper.floor(this.getX());
                int j = MathHelper.floor(this.getY());
                int k = MathHelper.floor(this.getZ());

                float chance = (float) (this.random.nextDouble() * 0.1D);
                if (livingentity != null && this.allyCooldown == 0 && this.random.nextFloat() < chance && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                    MobEntity minion = EntityType.HOGLIN.create(this.level);
                    if (!this.level.dimensionType().piglinSafe()) {
                        minion = EntityType.ZOGLIN.create(this.level);
                    }
                    if (minion != null) {
                        for (int l = 0; l < 50; ++l) {
                            int i1 = i + MathHelper.nextInt(this.random, 7, 16) * MathHelper.nextInt(this.random, -1, 1);
                            int j1 = j + MathHelper.nextInt(this.random, 0, 4);
                            int k1 = k + MathHelper.nextInt(this.random, 7, 16) * MathHelper.nextInt(this.random, -1, 1);
                            BlockPos blockpos = new BlockPos(i1, j1, k1);
                            EntityType<?> entitytype = minion.getType();
                            EntitySpawnPlacementRegistry.PlacementType entityspawnplacementregistry$placementtype = EntitySpawnPlacementRegistry.getPlacementType(entitytype);
                            if (WorldEntitySpawner.isSpawnPositionOk(entityspawnplacementregistry$placementtype, this.level, blockpos, entitytype) && EntitySpawnPlacementRegistry.checkSpawnRules(entitytype, serverworld, SpawnReason.REINFORCEMENT, blockpos, this.level.random)) {
                                minion.setPos((double) i1, (double) j1, (double) k1);
                                if (!this.level.hasNearbyAlivePlayer((double) i1, (double) j1, (double) k1, 7.0D) && this.level.isUnobstructed(minion) && this.level.noCollision(minion) && !this.level.containsAnyLiquid(minion.getBoundingBox())) {
                                    this.playSound(ModSounds.HOGLORD_SUMMON.get(), 1.0F, 1.0F);
                                    minion.setTarget(livingentity);
                                    minion.finalizeSpawn(serverworld, this.level.getCurrentDifficultyAt(minion.blockPosition()), SpawnReason.REINFORCEMENT, (ILivingEntityData) null, (CompoundNBT) null);
                                    serverworld.addFreshEntityWithPassengers(minion);
                                    break;
                                }
                            }
                        }
                        if (random.nextFloat() <= 0.1F) {
                            this.allyCooldown = 20;
                        } else {
                            this.allyCooldown = 200 + random.nextInt(200);
                        }
                    }
                }
            }
        }
        return super.hurt(pSource, pAmount);
    }

    public boolean isRaging() {
        return this.getHogLordFlag(1);
    }

    public void setIsRaging(boolean charging) {
        this.setHogLordFlag(1, charging);
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

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte pId) {
        if (pId == 4) {
            this.attackAnimationRemainingTicks = 10;
            this.playSound(SoundEvents.HOGLIN_ATTACK, 1.0F, this.getVoicePitch());
        } else {
            super.handleEntityEvent(pId);
        }

    }

    @OnlyIn(Dist.CLIENT)
    public int getAttackAnimationRemainingTicks() {
        return this.attackAnimationRemainingTicks;
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.HOGLORD_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.HOGLORD_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.HOGLORD_DEATH.get();
    }

    protected SoundEvent getSwimSound() {
        return SoundEvents.HOSTILE_SWIM;
    }

    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.HOSTILE_SPLASH;
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.HOGLIN_STEP, 0.15F, 1.0F);
    }

    static class MurkyGroundGoal extends Goal {
        public HogLordEntity hogLord;
        protected int warmup;
        protected int cooldown = 300;

        public MurkyGroundGoal(HogLordEntity hogLord) {
            this.hogLord = hogLord;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            return this.hogLord.tickCount >= cooldown
                    && !this.hogLord.isInWater()
                    && !this.hogLord.isInLava()
                    && this.hogLord.getTarget() != null
                    && !this.hogLord.isSpewing()
                    && !this.hogLord.isRaging();
        }

        @Override
        public void start() {
            this.warmup = 60;
            this.cooldown = this.hogLord.tickCount + 300;
            this.hogLord.setMurking(true);
        }

        @Override
        public void stop() {
            this.hogLord.setMurking(false);
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = this.hogLord.getTarget();
            return livingentity != null && livingentity.isAlive() && this.warmup > 0;
        }

        @Override
        public void tick() {
            --this.warmup;
            if (this.warmup > 0) {
                this.hogLord.getJumpControl().jump();
                this.hogLord.playSound(SoundEvents.HOGLIN_ANGRY, 1.0F, 1.5F);
            }

            if (this.warmup == 0){
                BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(this.hogLord.getX(), this.hogLord.getY(), this.hogLord.getZ());
                while (blockpos$mutable.getY() > 0 && !this.hogLord.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
                    blockpos$mutable.move(Direction.DOWN);
                }
                if (!this.hogLord.level.isClientSide) {
                    PoisonGroundEntity poisonGround = ModEntityType.POISON_GROUND.get().create(this.hogLord.level);
                    if (poisonGround != null) {
                        poisonGround.setOwner(this.hogLord);
                        poisonGround.setPos(blockpos$mutable.getX(), blockpos$mutable.getY() + 1, blockpos$mutable.getZ());
                        this.hogLord.level.addFreshEntity(poisonGround);
                    }
                }
            }
        }

    }

    protected PathNavigator createNavigation(World pLevel) {
        return new HogLordEntity.Navigator(this, pLevel);
    }

    static class Navigator extends GroundPathNavigator {
        public Navigator(MobEntity p_i50754_1_, World p_i50754_2_) {
            super(p_i50754_1_, p_i50754_2_);
        }

        protected PathFinder createPathFinder(int p_179679_1_) {
            this.nodeEvaluator = new HogLordEntity.Processor();
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
