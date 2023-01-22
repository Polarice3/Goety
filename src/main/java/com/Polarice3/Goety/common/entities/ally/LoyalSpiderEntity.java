package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.capabilities.spider.ISpiderLevels;
import com.Polarice3.Goety.common.entities.ai.SummonTargetGoal;
import com.Polarice3.Goety.common.entities.ai.SpiderBreedGoal;
import com.Polarice3.Goety.common.entities.neutral.IOwned;
import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.utils.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class LoyalSpiderEntity extends AnimalEntity implements IJumpingMount, IOwned {
    protected static final DataParameter<Byte> STATUS = EntityDataManager.defineId(LoyalSpiderEntity.class, DataSerializers.BYTE);
    protected static final DataParameter<Byte> SITTING = EntityDataManager.defineId(LoyalSpiderEntity.class, DataSerializers.BYTE);
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.defineId(LoyalSpiderEntity.class, DataSerializers.OPTIONAL_UUID);
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.defineId(LoyalSpiderEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Integer> DATA_COLLAR_COLOR = EntityDataManager.defineId(LoyalSpiderEntity.class, DataSerializers.INT);
    protected boolean Jumping;
    protected float jumpPower;

    public LoyalSpiderEntity(EntityType<? extends AnimalEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public void tick() {
        ISpiderLevels spiderLevels = SpiderLevelsHelper.getCapability(this);
        if (!this.level.isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }
        if (!this.isRoyal()){
            if (spiderLevels.getSpiderLevel() >= 10){
                this.setRoyal(true);
            }
        } else {
            for (SpiderEntity spiderEntity : this.level.getEntitiesOfClass(SpiderEntity.class, this.getBoundingBox().inflate(16))){
                if (this.getTarget() != null && this.getTarget() != this.getTrueOwner()){
                    if (this.getTarget() != spiderEntity){
                        spiderEntity.setTarget(this.getTarget());
                    }
                } else {
                    if (MainConfig.RoyalSpiderMinions.get()) {
                        spiderEntity.getLookControl().setLookAt(this, 10.0F, (float) spiderEntity.getMaxHeadXRot());
                        if (spiderEntity.distanceTo(this) > 2.0F) {
                            spiderEntity.getNavigation().moveTo(this, 1.0F);
                        }
                    }
                }
            }
        }
        if (this.getTarget() instanceof OwnedEntity){
            OwnedEntity summonedEntity = (OwnedEntity) this.getTarget();
            if (summonedEntity.getTrueOwner() == this.getTrueOwner()){
                this.setTarget(null);
            }
        }
        if (this.isSitting()) {
            if (!this.level.isClientSide) {
                ServerWorld serverWorld = (ServerWorld) this.level;
                if (this.tickCount % 40 == 0) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    serverWorld.sendParticles(ParticleTypes.NOTE, this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), 0, d0, d1, d2, 0.5F);
                }
            }
        }
        if (this.getTrueOwner() != null && this.getTrueOwner() instanceof PlayerEntity) {
            if (RobeArmorFinder.FindFelBootsofWander(this.getTrueOwner())) {
                if (this.tickCount % 40 == 0) {
                    this.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 200, 0, false, false));
                }
            }
        }
        if (!this.isBaby()){
            if (spiderLevels.getSpiderLevel() > 4 && !this.isRideable()){
                this.setRideable(true);
            }
        }
        if (this.isRideable()){
            this.maxUpStep = 1.0F;
        }
        if (MainConfig.TamedSpiderHeal.get() && this.getHealth() < this.getMaxHealth()){
            if (this.getTrueOwner() != null && this.getTrueOwner() instanceof PlayerEntity) {
                if (RobeArmorFinder.FindFelHelm(this.getTrueOwner())) {
                    PlayerEntity owner = (PlayerEntity) this.getTrueOwner();
                    int SoulCost = MainConfig.TamedSpiderHealCost.get();
                    if (RobeArmorFinder.FindLeggings(owner)){
                        int random = this.random.nextInt(2);
                        if (random == 0){
                            SoulCost = 0;
                        }
                    }
                    if (SEHelper.getSoulsAmount(owner, SoulCost)){
                        if (this.tickCount % 20 == 0) {
                            this.heal(1.0F);
                            Vector3d vector3d = this.getDeltaMovement();
                            SEHelper.decreaseSouls(owner, SoulCost);
                            if (!this.level.isClientSide){
                                ServerWorld serverWorld = (ServerWorld) this.level;
                                serverWorld.sendParticles(ParticleTypes.SOUL, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0, vector3d.x * -0.2D, 0.1D, vector3d.z * -0.2D, 0.5F);
                            }
                        }
                    }
                }
            }
        }
        super.tick();
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(0, new SitGoal(this));
        this.targetSelector.addGoal(1, new SummonTargetGoal<>(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new SpiderBreedGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(8, new FollowOwnerGoal(this, 1.5D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.3F)
                .add(Attributes.ARMOR, 0.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.0F)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    protected void updateControlFlags() {
        boolean flag = !(this.getControllingPassenger() instanceof MobEntity);
        boolean flag1 = !(this.getVehicle() instanceof BoatEntity);
        this.goalSelector.setControlFlag(Goal.Flag.MOVE, flag);
        this.goalSelector.setControlFlag(Goal.Flag.JUMP, flag && flag1);
        this.goalSelector.setControlFlag(Goal.Flag.LOOK, flag);
        this.goalSelector.setControlFlag(Goal.Flag.TARGET, flag);
    }

    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.5F;
    }

    protected PathNavigator createNavigation(World worldIn) {
        return new ClimberPathNavigator(this, worldIn);
    }

    public Team getTeam() {
        if (this.getOwnerId() != null) {
            LivingEntity livingentity = this.getTrueOwner();
            if (livingentity != null) {
                return livingentity.getTeam();
            }
        }

        return super.getTeam();
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (this.getOwnerId() != null) {
            LivingEntity livingentity = this.getTrueOwner();
            if (entityIn == livingentity) {
                return true;
            }

            if (livingentity != null) {
                return livingentity.isAlliedTo(entityIn);
            }
        }
        if (entityIn instanceof OwnedEntity && ((OwnedEntity) entityIn).getTrueOwner() == this.getTrueOwner()){
            return true;
        }
        return super.isAlliedTo(entityIn);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
        this.entityData.define(DATA_COLLAR_COLOR, DyeColor.RED.getId());
        this.entityData.define(CLIMBING, (byte)0);
        this.entityData.define(STATUS, (byte)0);
        this.entityData.define(SITTING, (byte)0);
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Poison", this.isPoison());
        compound.putBoolean("Rideable", this.isRideable());
        compound.putBoolean("FallImmune", this.isFallImmune());
        compound.putByte("CollarColor", (byte)this.getCollarColor().getId());
        if (this.getOwnerId() != null) {
            compound.putUUID("Owner", this.getOwnerId());
        }
        compound.putBoolean("Sitting", this.isSitting());
        compound.putBoolean("Saddled", this.isSaddled());
        compound.putBoolean("Frost", this.isFrost());
        compound.putBoolean("Royal", this.isRoyal());
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        UUID uuid;
        if (compound.hasUUID("Owner")) {
            uuid = compound.getUUID("Owner");
        } else {
            String s = compound.getString("Owner");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setOwnerId(uuid);
            } catch (Throwable ignored) {
            }
        }
        if (compound.contains("CollarColor", 99)) {
            this.setCollarColor(DyeColor.byId(compound.getInt("CollarColor")));
        }
        this.setSitting(compound.getBoolean("Sitting"));
        this.setRideable(compound.getBoolean("Rideable"));
        this.setPoison(compound.getBoolean("Poison"));
        this.setFallImmune(compound.getBoolean("FallImmune"));
        this.setSaddled(compound.getBoolean("Saddled"));
        this.setFrost(compound.getBoolean("Frost"));
        this.setRoyal(compound.getBoolean("Royal"));
    }

    private boolean getStatusFlag(int mask) {
        int i = this.entityData.get(STATUS);
        return (i & mask) != 0;
    }

    private void setStatusFlag(int mask, boolean value) {
        int i = this.entityData.get(STATUS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(STATUS, (byte)(i & 255));
    }

    public LivingEntity getTrueOwner() {
        try {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : this.level.getPlayerByUUID(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    public PlayerEntity getPlayer(){
        return (PlayerEntity) this.getTrueOwner();
    }

    @Nullable
    public UUID getOwnerId() {
        return this.entityData.get(OWNER_UNIQUE_ID).orElse((UUID)null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        } else {
            Entity entity = pSource.getEntity();
            this.setSitting(false);
            if (entity != null && !(entity instanceof PlayerEntity) && !(entity instanceof AbstractArrowEntity)) {
                pAmount = (pAmount + 1.0F) / 2.0F;
            }
            if (this.isFrost()){
                if (ModDamageSource.frostAttacks(pSource)){
                    pAmount = pAmount / 2.0F;
                }
                if (pSource.isFire()){
                    pAmount = pAmount * 1.5F;
                }
            }

            return super.hurt(pSource, pAmount);
        }
    }

    public boolean doHurtTarget(Entity pEntity) {
        if (super.doHurtTarget(pEntity)) {
            if (pEntity instanceof LivingEntity) {
                if (this.isPoison()) {
                    if (this.isRoyal()) {
                        ((LivingEntity) pEntity).addEffect(new EffectInstance(Effects.POISON, 400, 1));
                    } else {
                        ((LivingEntity) pEntity).addEffect(new EffectInstance(Effects.POISON, 200));
                    }
                }
                if (this.isFrost()) {
                    if (this.isRoyal()) {
                        ((LivingEntity) pEntity).addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 400, 1));
                    } else {
                        ((LivingEntity) pEntity).addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200));
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SPIDER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.SPIDER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SPIDER_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }

    public boolean onClimbable() {
        return this.isBesideClimbableBlock();
    }

    public void makeStuckInBlock(BlockState state, Vector3d motionMultiplierIn) {
        if (!state.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(state, motionMultiplierIn);
        }

    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return this.isFallImmune();
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.ARTHROPOD;
    }

    public boolean canBeAffected(EffectInstance potioneffectIn) {
        if (potioneffectIn.getEffect() == Effects.POISON) {
            net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent event = new net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent(this, potioneffectIn);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
            return event.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW;
        }
        if (this.isFrost()){
            if (potioneffectIn.getEffect() == Effects.MOVEMENT_SLOWDOWN) {
                net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent event = new net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent(this, potioneffectIn);
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
                return event.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW;
            }
        }
        return super.canBeAffected(potioneffectIn);
    }

    public boolean isBesideClimbableBlock() {
        return (this.entityData.get(CLIMBING) & 1) != 0;
    }

    public void setClimbing(boolean climbing) {
        byte b0 = this.entityData.get(CLIMBING);
        if (climbing) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(CLIMBING, b0);
    }

    public boolean canBeControlledByRider() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }

    @Override
    public boolean isControlledByLocalInstance() {
        return super.isControlledByLocalInstance() && this.canBeControlledByRider();
    }

    public double getCustomJump() {
        return RobeArmorFinder.FindFelBootsofWander(this.getTrueOwner()) ? 1.0D : 0.7D;
    }

    public boolean isJumping() {
        return this.Jumping;
    }

    public void setJumping(boolean jumping) {
        this.Jumping = jumping;
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    protected void onChangedBlock(BlockPos pPos) {
        super.onChangedBlock(pPos);
        if (this.isFrost()){
            FrostWalkerEnchantment.onEntityMoved(this, this.level, pPos, 2);
        }
    }

    public void levelBonus() {
        ISpiderLevels spiderLevels = SpiderLevelsHelper.getCapability(this);
        if (!this.level.isClientSide()) {
            ServerWorld serverWorld = (ServerWorld) this.level;
            if (spiderLevels.getSpiderLevel() < 10) {
                spiderLevels.increaseSpiderLevel(1);
                SpiderLevelsHelper.sendSpiderLevelsUpdatePacket(this.getPlayer(), this);
                this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getAttributeBaseValue(Attributes.MAX_HEALTH) * 1.3D);
                this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(this.getAttributeBaseValue(Attributes.ATTACK_DAMAGE) * 1.25D);
                this.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F, 0.5F);
                for (int i = 0; i < 5; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    serverWorld.sendParticles(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), 0, d0, d1, d2, 0.5F);
                }
                if (spiderLevels.getSpiderLevel() % 20 == 0) {
                    this.ArmorBonus();
                }
                if (spiderLevels.getSpiderLevel() > 4 && !this.isBaby() && !this.isRideable()) {
                    this.setRideable(true);
                    this.playSound(SoundEvents.PLAYER_LEVELUP, 1.0F, 1.0F);
                }
            }
        }
    }

    public void ArmorBonus(){
        this.getAttribute(Attributes.ARMOR).setBaseValue(this.getAttributeBaseValue(Attributes.ARMOR) + 1.0D);
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(this.getAttributeBaseValue(Attributes.KNOCKBACK_RESISTANCE) + 0.15D);
    }

    public boolean isPoison() {
        return this.getStatusFlag(1);
    }

    public void setPoison(boolean poison) {
        this.setStatusFlag(1,poison);
    }

    public boolean isRideable() {
        return this.getStatusFlag(2) && !this.isBaby();
    }

    public void setRideable(boolean rideable){
        this.setStatusFlag(2,rideable);
    }

    public boolean isFallImmune() {
        return this.getStatusFlag(4);
    }

    public void setFallImmune(boolean fallImmune) {
        this.setStatusFlag(4, fallImmune);
    }

    public boolean isSaddled() {
        return this.getStatusFlag(8);
    }

    public void setSaddled(boolean saddled) {
        this.setStatusFlag(8, saddled);
    }

    public DyeColor getCollarColor() {
        return DyeColor.byId(this.entityData.get(DATA_COLLAR_COLOR));
    }

    public void setCollarColor(DyeColor pCollarcolor) {
        this.entityData.set(DATA_COLLAR_COLOR, pCollarcolor.getId());
    }

    public boolean isRoyal(){
        return this.getStatusFlag(16);
    }

    public void setRoyal(boolean royal){
        this.setStatusFlag(16, royal);
    }

    public boolean isFrost(){
        return this.getStatusFlag(32);
    }

    public void setFrost(boolean frost){
        this.setStatusFlag(32, frost);
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        return spawnDataIn;
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
        LoyalSpiderEntity partner = (LoyalSpiderEntity) p_241840_2_;
        LoyalSpiderEntity loyalSpiderEntity = new LoyalSpiderEntity(ModEntityType.TAMED_SPIDER.get(), p_241840_1_);
        UUID uuid = this.getOwnerId();
        if (uuid != null) {
            loyalSpiderEntity.setOwnerId(uuid);
        }
        if (this.isPoison() || partner.isPoison()){
            loyalSpiderEntity.setPoison(true);
        }
        if (this.isFrost() || partner.isFrost()){
            loyalSpiderEntity.setFrost(true);
        }
        if (this.isRoyal() || partner.isRoyal()){
            loyalSpiderEntity.setRoyal(true);
        }
        if (this.isFallImmune() || partner.isFallImmune()){
            loyalSpiderEntity.setFallImmune(true);
        }

        return loyalSpiderEntity;
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return this.isBaby() ? 0.325F : 0.65F;
    }

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public boolean isSitting() {
        return (this.entityData.get(SITTING) & 1) != 0;
    }

    public void setSitting(boolean sitting) {
        byte b0 = this.entityData.get(SITTING);
        if (sitting) {
            this.entityData.set(SITTING, (byte)(b0 | 1));
        } else {
            this.entityData.set(SITTING, (byte)(b0 & -2));
        }
    }

    protected void doPlayerRide(PlayerEntity player) {
        if (!this.level.isClientSide) {
            player.yRot = this.yRot;
            player.xRot = this.xRot;
            player.startRiding(this);
        }

    }

    public boolean isFood(ItemStack pStack) {
        Item item = pStack.getItem();
        return item == ModItems.DEADBAT.get();
    }

    public void die(DamageSource cause) {
        if (this.isSaddled()){
            this.spawnAtLocation(new ItemStack(Items.SADDLE));
        }
        if (!this.level.isClientSide && this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getTrueOwner() instanceof ServerPlayerEntity) {
            this.getTrueOwner().sendMessage(this.getCombatTracker().getDeathMessage(), Util.NIL_UUID);
        }
        super.die(cause);
    }

    public ActionResultType mobInteract(PlayerEntity pPlayer, Hand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        Item item = itemstack.getItem();
        int a = this.getAge();
        if (isFood(itemstack) && !this.level.isClientSide && a == 0 && this.canFallInLove()){
            if (!pPlayer.abilities.instabuild) {
                itemstack.shrink(1);
            }
            this.usePlayerItem(pPlayer, itemstack);
            this.setInLove(pPlayer);
            return ActionResultType.SUCCESS;
        }
        if (RawFoodFinder.findRawFood(item)){
            this.heal(1.0F);
            this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
            if (!this.level.isClientSide) {
                ServerWorld serverWorld = (ServerWorld) this.level;
                for (int i = 0; i < 5; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    serverWorld.sendParticles(ModParticleTypes.HEAL_EFFECT.get(), this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), 0, d0, d1, d2, 0.5F);
                }
            }
            if (this.isBaby()) {
                this.usePlayerItem(pPlayer, itemstack);
                this.ageUp((int)((float)(-a / 20) * 0.1F), true);
                return ActionResultType.sidedSuccess(this.level.isClientSide);
            }
            if (!pPlayer.abilities.instabuild) {
                itemstack.shrink(1);
            }
            return ActionResultType.SUCCESS;
        }
        if (RawFoodFinder.findMutatedRaw(item)){
            this.heal(10.0F);
            this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
            this.levelBonus();
            if (!this.level.isClientSide) {
                ServerWorld serverWorld = (ServerWorld) this.level;
                for (int i = 0; i < 5; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    serverWorld.sendParticles(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), 0, d0, d1, d2, 0.5F);
                }
            }
            if (!pPlayer.abilities.instabuild) {
                itemstack.shrink(1);
            }
            return ActionResultType.SUCCESS;
        }
        if (PotionUtils.getPotion(itemstack) == Potions.POISON){
            if (!this.isPoison()) {
                this.playSound(SoundEvents.GENERIC_DRINK, 1.0F, 1.0F);
                this.setPoison(true);
                this.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE, 1.0F, 1.0F);
                if (!pPlayer.abilities.instabuild) {
                    itemstack.shrink(1);
                }
                this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getAttributeBaseValue(Attributes.MAX_HEALTH) - 4.0D);
                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.PASS;
            }
        }
        if (PotionUtils.getPotion(itemstack) == Potions.SLOW_FALLING){
            if (!this.isFallImmune()) {
                this.playSound(SoundEvents.GENERIC_DRINK, 1.0F, 1.0F);
                this.setFallImmune(true);
                this.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE, 1.0F, 1.0F);
                if (!pPlayer.abilities.instabuild) {
                    itemstack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.PASS;
            }
        }
        if (item == ModItems.COLD_HEART.get()){
            if (!this.isFrost()) {
                this.playSound(SoundEvents.GENERIC_DRINK, 1.0F, 1.0F);
                this.setFrost(true);
                this.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE, 1.0F, 1.0F);
                if (!pPlayer.abilities.instabuild) {
                    itemstack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.PASS;
            }
        }
        if (item instanceof DyeItem) {
            DyeColor dyecolor = ((DyeItem) item).getDyeColor();
            if (dyecolor != this.getCollarColor()) {
                this.setCollarColor(dyecolor);
                if (!pPlayer.abilities.instabuild) {
                    itemstack.shrink(1);
                }

                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.PASS;
            }
        }
        if (item instanceof SaddleItem) {
            if (!this.isSaddled() && this.isRideable() && !this.isBaby()) {
                this.setSaddled(true);
                if (!pPlayer.abilities.instabuild) {
                    itemstack.shrink(1);
                }
                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.PASS;
            }
        }
        if (this.isRideable() && this.isSaddled() && !pPlayer.isCrouching() && pPlayer.getMainHandItem().isEmpty()){
            this.doPlayerRide(pPlayer);
            this.setSitting(false);
        } else {
            this.setSitting(!this.isSitting());
            this.jumping = false;
            this.navigation.stop();
            this.setTarget(null);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.SUCCESS;
    }

    public void travel(Vector3d pTravelVector) {
        if (this.isAlive()) {
            if (!this.isSitting()) {
                if (this.isVehicle() && this.canBeControlledByRider()) {
                    LivingEntity livingentity = (LivingEntity) this.getControllingPassenger();
                    assert livingentity != null;
                    this.yRot = livingentity.yRot;
                    this.yRotO = this.yRot;
                    this.xRot = livingentity.xRot * 0.5F;
                    this.setRot(this.yRot, this.xRot);
                    this.yBodyRot = this.yRot;
                    this.yHeadRot = this.yBodyRot;
                    float f = livingentity.xxa * 0.5F;
                    float f1 = livingentity.zza;
                    if (f1 <= 0.0F) {
                        f1 *= 0.25F;
                    }

                    if (this.jumpPower > 0.0F && !this.isJumping() && this.onGround) {
                        double d0 = this.getCustomJump() * (double) this.jumpPower * (double) this.getBlockJumpFactor();
                        double d1;
                        if (this.hasEffect(Effects.JUMP)) {
                            d1 = d0 + (double) ((float) (Objects.requireNonNull(this.getEffect(Effects.JUMP)).getAmplifier() + 1) * 0.1F);
                        } else {
                            d1 = d0;
                        }

                        Vector3d vector3d = this.getDeltaMovement();
                        this.setDeltaMovement(vector3d.x, d1, vector3d.z);
                        this.setJumping(true);
                        this.hasImpulse = true;
                        net.minecraftforge.common.ForgeHooks.onLivingJump(this);
                        if (f1 > 0.0F) {
                            float f2 = MathHelper.sin(this.yRot * ((float) Math.PI / 180F));
                            float f3 = MathHelper.cos(this.yRot * ((float) Math.PI / 180F));
                            this.setDeltaMovement(this.getDeltaMovement().add((double) (-0.4F * f2 * this.jumpPower), 0.0D, (double) (0.4F * f3 * this.jumpPower)));
                        }

                        this.jumpPower = 0.0F;
                    }

                    this.flyingSpeed = this.getSpeed() * 0.1F;
                    if (this.isControlledByLocalInstance()) {
                        this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                        super.travel(new Vector3d(f, pTravelVector.y, f1));
                        this.lerpSteps = 0;
                        this.setClimbing(this.horizontalCollision);
                    } else if (livingentity instanceof PlayerEntity) {
                        this.setDeltaMovement(Vector3d.ZERO);
                    }

                    if (this.onGround) {
                        this.jumpPower = 0.0F;
                        this.setJumping(false);
                    }

                    this.calculateEntityAnimation(this, false);
                } else {
                    this.flyingSpeed = 0.02F;
                    super.travel(pTravelVector);
                }
            }
        }
    }

    @Override
    public void onPlayerJump(int jumpPowerIn) {
        if (this.isVehicle()) {
            if (jumpPowerIn < 0) {
                jumpPowerIn = 0;
            }
            if (jumpPowerIn >= 90) {
                this.jumpPower = 1.0F;
            } else {
                this.jumpPower = 0.4F + 0.4F * (float)jumpPowerIn / 90.0F;
            }

        }
    }

    @Override
    public boolean canJump() {
        return true;
    }

    @Override
    public void handleStartJump(int jumpPower) {

    }

    @Override
    public void handleStopJump() {

    }

    public class SitGoal extends Goal {
        private final LoyalSpiderEntity tamedSpider;

        public SitGoal(LoyalSpiderEntity entityIn) {
            this.tamedSpider = entityIn;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        public boolean canContinueToUse() {
            return this.tamedSpider.isSitting();
        }

        public boolean canUse() {
            if (this.tamedSpider.isInWaterOrBubble()) {
                return false;
            } else if (!this.tamedSpider.isOnGround()) {
                return false;
            } else {
                LivingEntity livingentity = this.tamedSpider.getTrueOwner();
                if (livingentity == null) {
                    return true;
                } else {
                    return (!(this.tamedSpider.distanceToSqr(livingentity) < 144.0D) || livingentity.getLastHurtByMob() == null) && this.tamedSpider.isSitting();
                }
            }
        }

        public void start() {
            this.tamedSpider.getNavigation().stop();
            this.tamedSpider.setSitting(true);
        }

        public void stop() {
            this.tamedSpider.setSitting(false);
        }
    }

    class OwnerHurtTargetGoal extends TargetGoal {
        private LivingEntity attacker;
        private int timestamp;

        public OwnerHurtTargetGoal(LoyalSpiderEntity loyalSpiderEntity) {
            super(loyalSpiderEntity, false);
            this.setFlags(EnumSet.of(Flag.TARGET));
        }

        public boolean canUse() {
            LivingEntity livingentity = LoyalSpiderEntity.this.getTrueOwner();
            if (livingentity == null) {
                return false;
            } else {
                this.attacker = livingentity.getLastHurtMob();
                int i = livingentity.getLastHurtMobTimestamp();
                return i != this.timestamp && this.canAttack(this.attacker, EntityPredicate.DEFAULT) && this.attacker != LoyalSpiderEntity.this.getTrueOwner();
            }
        }

        public void start() {
            this.mob.setTarget(this.attacker);
            LivingEntity livingentity = LoyalSpiderEntity.this.getTrueOwner();
            if (livingentity != null) {
                this.timestamp = livingentity.getLastHurtMobTimestamp();
            }

            super.start();
        }
    }

    class OwnerHurtByTargetGoal extends TargetGoal {
        private LivingEntity attacker;
        private int timestamp;

        public OwnerHurtByTargetGoal(LoyalSpiderEntity loyalSpiderEntity) {
            super(loyalSpiderEntity, false);
            this.setFlags(EnumSet.of(Flag.TARGET));
        }

        public boolean canUse() {
            LivingEntity livingentity = LoyalSpiderEntity.this.getTrueOwner();
            if (livingentity == null) {
                return false;
            } else {
                this.attacker = livingentity.getLastHurtByMob();
                int i = livingentity.getLastHurtByMobTimestamp();
                return i != this.timestamp && this.canAttack(this.attacker, EntityPredicate.DEFAULT) && this.attacker != LoyalSpiderEntity.this.getTrueOwner();
            }
        }

        public void start() {
            this.mob.setTarget(this.attacker);
            LivingEntity livingentity = LoyalSpiderEntity.this.getTrueOwner();
            if (livingentity != null) {
                this.timestamp = livingentity.getLastHurtByMobTimestamp();
            }

            super.start();
        }
    }

    public static class FollowOwnerGoal extends Goal {
        private final LoyalSpiderEntity loyalSpiderEntity;
        private LivingEntity owner;
        private final IWorldReader level;
        private final double followSpeed;
        private final PathNavigator navigation;
        private int timeToRecalcPath;
        private final float maxDist;
        private final float minDist;
        private float oldWaterCost;
        private final boolean teleportToLeaves;

        public FollowOwnerGoal(LoyalSpiderEntity loyalSpiderEntity, double speed, float minDist, float maxDist, boolean teleportToLeaves) {
            this.loyalSpiderEntity = loyalSpiderEntity;
            this.level = loyalSpiderEntity.level;
            this.followSpeed = speed;
            this.navigation = loyalSpiderEntity.getNavigation();
            this.minDist = minDist;
            this.maxDist = maxDist;
            this.teleportToLeaves = teleportToLeaves;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            if (!(loyalSpiderEntity.getNavigation() instanceof GroundPathNavigator) && !(loyalSpiderEntity.getNavigation() instanceof FlyingPathNavigator)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        public boolean canUse() {
            LivingEntity livingentity = this.loyalSpiderEntity.getTrueOwner();
            if (livingentity == null) {
                return false;
            } else if (livingentity.isSpectator()) {
                return false;
            } else if (this.loyalSpiderEntity.distanceToSqr(livingentity) < (double)(this.minDist * this.minDist)) {
                return false;
            } else {
                this.owner = livingentity;
                return !this.loyalSpiderEntity.isSitting();
            }
        }

        public boolean canContinueToUse() {
            if (this.navigation.isDone()) {
                return false;
            } else {
                return !(this.loyalSpiderEntity.distanceToSqr(this.owner) <= (double)(this.maxDist * this.maxDist));
            }
        }

        public void start() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.loyalSpiderEntity.getPathfindingMalus(PathNodeType.WATER);
            this.loyalSpiderEntity.setPathfindingMalus(PathNodeType.WATER, 0.0F);
        }

        public void stop() {
            this.owner = null;
            this.navigation.stop();
            this.loyalSpiderEntity.setPathfindingMalus(PathNodeType.WATER, this.oldWaterCost);
        }

        public void tick() {
            this.loyalSpiderEntity.getLookControl().setLookAt(this.owner, 10.0F, (float)this.loyalSpiderEntity.getMaxHeadXRot());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                if (!this.loyalSpiderEntity.isLeashed() && !this.loyalSpiderEntity.isPassenger()) {
                    if (this.loyalSpiderEntity.distanceToSqr(this.owner) >= 144.0D) {
                        this.tryToTeleportNearEntity();
                    } else {
                        this.navigation.moveTo(this.owner, this.followSpeed);
                    }

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
                this.loyalSpiderEntity.moveTo((double)x + 0.5D, (double)y, (double)z + 0.5D, this.loyalSpiderEntity.yRot, this.loyalSpiderEntity.xRot);
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
                    BlockPos blockpos = pos.subtract(this.loyalSpiderEntity.blockPosition());
                    return this.level.noCollision(this.loyalSpiderEntity, this.loyalSpiderEntity.getBoundingBox().move(blockpos));
                }
            }
        }

        private int getRandomNumber(int min, int max) {
            return this.loyalSpiderEntity.getRandom().nextInt(max - min + 1) + min;
        }
    }
}
