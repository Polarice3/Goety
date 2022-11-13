package com.Polarice3.Goety.common.entities.bosses;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.hostile.IrkEntity;
import com.Polarice3.Goety.common.entities.projectiles.SpikeEntity;
import com.Polarice3.Goety.common.network.ModServerBossInfo;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.Polarice3.Goety.utils.ServerParticleUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpellcastingIllagerEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

@OnlyIn(
        value = Dist.CLIENT,
        _interface = IChargeableMob.class
)
public class VizierEntity extends SpellcastingIllagerEntity implements IChargeableMob {
    private static final Predicate<Entity> field_213690_b = (p_213685_0_) -> {
        return p_213685_0_.isAlive() && !(p_213685_0_ instanceof VizierEntity);
    };
    protected static final DataParameter<Byte> VIZIER_FLAGS = EntityDataManager.defineId(VizierEntity.class, DataSerializers.BYTE);
    protected static final DataParameter<Integer> CAST_TIMES = EntityDataManager.defineId(VizierEntity.class, DataSerializers.INT);
    protected static final DataParameter<Integer> CASTING = EntityDataManager.defineId(VizierEntity.class, DataSerializers.INT);
    private static final DataParameter<Integer> CONFUSED = EntityDataManager.defineId(VizierEntity.class, DataSerializers.INT);
    private final ModServerBossInfo bossInfo = new ModServerBossInfo(this.getUUID(), this.getDisplayName(), BossInfo.Color.YELLOW, BossInfo.Overlay.PROGRESS);
    public float oBob;
    public float bob;
    public double xCloakO;
    public double yCloakO;
    public double zCloakO;
    public double xCloak;
    public double yCloak;
    public double zCloak;

    public VizierEntity(EntityType<? extends VizierEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveControl = new MobUtil.MoveHelperController(this);
        this.xpReward = 50;
        if (this.level.isClientSide){
            Goety.PROXY.addBoss(this);
        }
    }

    public void move(MoverType typeIn, Vector3d pos) {
        super.move(typeIn, pos);
        this.checkInsideBlocks();
    }

    public void tick() {
        if (this.getInvulnerableTicks() > 0) {
            this.setDeltaMovement(Vector3d.ZERO);
            int j1 = this.getInvulnerableTicks() - 1;
            if (j1 == 20){
                for(int i = 0; i < 5; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    this.level.addParticle(ModParticleTypes.CONFUSED.get(), this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), d0, d1, d2);
                }
                this.playSound(SoundEvents.ILLUSIONER_AMBIENT, 1.0F, 0.5F);
            }
            if (j1 <= 0) {
                for(int i = 0; i < 5; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    this.level.addParticle(ParticleTypes.ANGRY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), d0, d1, d2);
                }
                this.playSound(SoundEvents.VILLAGER_NO, 1.0F, 0.5F);
            }
            this.setInvulnerableTicks(j1);
        }
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
        this.oBob = this.bob;
        float f = Math.min(0.1F, MathHelper.sqrt(getHorizontalDistanceSqr(this.getDeltaMovement())));

        this.bob += (f - this.bob) * 0.4F;
        if (this.level.getDifficulty() == Difficulty.PEACEFUL) {
            this.remove();
        }
        if (!this.isSpellcasting()){
            this.setCasting(this.getCasting() + 1);
        } else {
            if (this.getCastTimes() == 3){
                this.setDeltaMovement(Vector3d.ZERO);
            } else {
                Vector3d vector3d = this.getDeltaMovement().multiply(1.0D, 0.6D, 1.0D);
                if (!this.level.isClientSide){
                    if (this.getTarget() != null){
                        double d0 = vector3d.y;
                        if (this.getY() < this.getTarget().getY() + 3.0D) {
                            d0 = Math.max(0.0D, d0);
                            d0 = d0 + (0.3D - d0 * (double)0.6F);
                        }

                        vector3d = new Vector3d(vector3d.x, d0, vector3d.z);
                        Vector3d vector3d1 = new Vector3d(this.getTarget().getX() - this.getX(), 0.0D, this.getTarget().getZ() - this.getZ());
                        if (getHorizontalDistanceSqr(vector3d1) > 9.0D) {
                            Vector3d vector3d2 = vector3d1.normalize();
                            vector3d = vector3d.add(vector3d2.x * 0.3D - vector3d.x * 0.6D, 0.0D, vector3d2.z * 0.3D - vector3d.z * 0.6D);
                        }
                    }
                }
                this.setDeltaMovement(vector3d);
                if (getHorizontalDistanceSqr(vector3d) > 0.05D) {
                    this.yRot = (float)MathHelper.atan2(vector3d.z, vector3d.x) * (180F / (float)Math.PI) - 90.0F;
                }
            }
        }
        if (this.getCasting() >= 300){
            this.setCasting(0);
        }
        int i = VizierEntity.this.level.getEntitiesOfClass(IrkEntity.class, VizierEntity.this.getBoundingBox().inflate(64)).size();
        if (MainConfig.VizierMinion.get()){
            i = VizierEntity.this.level.getEntitiesOfClass(VexEntity.class, VizierEntity.this.getBoundingBox().inflate(64)).size();
        }
        if (this.getCastTimes() == 1){
            if (i >= 2){
                if (this.level.random.nextBoolean()) {
                    this.setCastTimes(2);
                } else {
                    this.setCastTimes(3);
                }
            } else {
                this.setCastTimes(3);
            }
        }
        this.moveCloak();
        if (this.getTarget() == null){
            for (LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(32.0F), EntityPredicates.NO_CREATIVE_OR_SPECTATOR)){
                if (livingEntity instanceof PlayerEntity || livingEntity instanceof AbstractVillagerEntity || livingEntity instanceof IronGolemEntity){
                    this.setTarget(livingEntity);
                }
            }
        } else {
            if (!this.getTarget().canSee(this)){
                BlockPos blockPos = new BlockPos(this.getTarget().getX() - 2, this.getTarget().getY() + 2.0D, this.getTarget().getZ() - 2);
                if (this.isFree(blockPos.getX(), blockPos.getY(), blockPos.getZ())){
                    this.moveControl.setWantedPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0F);
                } else {
                    this.moveControl.setWantedPosition(this.getTarget().getX(), this.getTarget().getY(), this.getTarget().getZ(), 1.0F);
                }
            }
        }
    }

    private void moveCloak() {
        this.xCloakO = this.xCloak;
        this.yCloakO = this.yCloak;
        this.zCloakO = this.zCloak;
        double d0 = this.getX() - this.xCloak;
        double d1 = this.getY() - this.yCloak;
        double d2 = this.getZ() - this.zCloak;
        double d3 = 10.0D;
        if (d0 > d3) {
            this.xCloak = this.getX();
            this.xCloakO = this.xCloak;
        }

        if (d2 > d3) {
            this.zCloak = this.getZ();
            this.zCloakO = this.zCloak;
        }

        if (d1 > d3) {
            this.yCloak = this.getY();
            this.yCloakO = this.yCloak;
        }

        if (d0 < -d3) {
            this.xCloak = this.getX();
            this.xCloakO = this.xCloak;
        }

        if (d2 < -d3) {
            this.zCloak = this.getZ();
            this.zCloakO = this.zCloak;
        }

        if (d1 < -d3) {
            this.yCloak = this.getY();
            this.yCloakO = this.yCloak;
        }

        this.xCloak += d0 * 0.25D;
        this.zCloak += d2 * 0.25D;
        this.yCloak += d1 * 0.25D;
    }

    protected SoundEvent getCastingSoundEvent () {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new VizierEntity.DoNothingGoal());
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new FangsSpellGoal());
        this.goalSelector.addGoal(1, new HealGoal());
        this.goalSelector.addGoal(1, new SpikesGoal());
        this.goalSelector.addGoal(1, new MoveRandomGoal());
        this.goalSelector.addGoal(4, new ChargeAttackGoal());
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VIZIER_FLAGS, (byte)0);
        this.entityData.define(CAST_TIMES, 0);
        this.entityData.define(CASTING, 0);
        this.entityData.define(CONFUSED, 0);
    }

    public void playAmbientSound() {
        SoundEvent soundevent = this.getAmbientSound();
        if (soundevent != null) {
            this.playSound(soundevent, 1.0F, 0.75F);
        }
    }

    protected void playHurtSound(DamageSource pSource) {
        SoundEvent soundevent = this.getHurtSound(pSource);
        if (soundevent != null) {
            this.playSound(soundevent, 1.0F, 0.75F);
        }
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.EVOKER_HURT;
    }

    public int getInvulnerableTicks() {
        return this.entityData.get(CONFUSED);
    }

    public void setInvulnerableTicks(int pTime) {
        this.entityData.set(CONFUSED, pTime);
    }

    public int getCastTimes() {
        return this.entityData.get(CAST_TIMES);
    }

    public void setCastTimes(int pTime) {
        this.entityData.set(CAST_TIMES, pTime);
    }

    public int getCasting() {
        return this.entityData.get(CASTING);
    }

    public void setCasting(int pTime) {
        this.entityData.set(CASTING, pTime);
    }

    public void makeInvulnerable() {
        this.setInvulnerableTicks(40);
    }

    public void die(DamageSource cause) {
        if (!MainConfig.VizierMinion.get()) {
            for (IrkEntity ally : VizierEntity.this.level.getEntitiesOfClass(IrkEntity.class, VizierEntity.this.getBoundingBox().inflate(64.0D), field_213690_b)) {
                ally.hurt(DamageSource.STARVE, 200.0F);
            }
        } else {
            for (VexEntity ally : VizierEntity.this.level.getEntitiesOfClass(VexEntity.class, VizierEntity.this.getBoundingBox().inflate(64.0D), field_213690_b)) {
                ally.hurt(DamageSource.STARVE, 200.0F);
            }
        }
        if (cause.getEntity() != null) {
            if (cause.getEntity() instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) cause.getEntity();
                EffectInstance effectinstance = new EffectInstance(Effects.BAD_OMEN, 120000, 4, false, false, true);
                if (!this.level.getGameRules().getBoolean(GameRules.RULE_DISABLE_RAIDS)) {
                    player.addEffect(effectinstance);
                }
            }
        }

        super.die(cause);
    }

    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public void remove() {
        if (this.level.isClientSide) {
            Goety.PROXY.removeBoss(this);
        }
        super.remove();
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        LivingEntity livingEntity = this.getTarget();
        if (this.getInvulnerableTicks() > 0 && pSource != DamageSource.OUT_OF_WORLD){
            return false;
        }
        if (livingEntity != null){
            if (pSource.getEntity() instanceof IrkEntity){
                return false;
            } else {
                if (!MainConfig.VizierMinion.get()) {
                    int irks = this.level.getEntitiesOfClass(IrkEntity.class, this.getBoundingBox().inflate(32)).size();
                    if ((this.level.random.nextBoolean() || this.getHealth() < this.getMaxHealth()/2) && irks < 16) {
                        IrkEntity irk = new IrkEntity(ModEntityType.IRK.get(), this.level);
                        irk.setPos(this.getX(), this.getY(), this.getZ());
                        irk.setOwner(this);
                        this.level.addFreshEntity(irk);
                    }
                } else {
                    int vexes = this.level.getEntitiesOfClass(VexEntity.class, this.getBoundingBox().inflate(32)).size();
                    if ((this.level.random.nextBoolean() || this.getHealth() < this.getMaxHealth()/2) && vexes < 16) {
                        VexEntity irk = new VexEntity(EntityType.VEX, this.level);
                        irk.setItemInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
                        irk.setDropChance(EquipmentSlotType.MAINHAND, 0.0F);
                        irk.setPos(this.getX(), this.getY(), this.getZ());
                        irk.setOwner(this);
                        this.level.addFreshEntity(irk);
                    }
                }
            }
        }

        if (pAmount > 20.0F){
            return super.hurt(pSource, 20.0F);
        } else {
            if (this.isSpellcasting()){
                return super.hurt(pSource, pAmount/2);
            } else {
                return super.hurt(pSource, pAmount);
            }
        }
    }

    @Override
    public void kill() {
        this.setHealth(0.0F);
    }

    public boolean isAlliedTo(Entity pEntity) {
        if (pEntity == null) {
            return false;
        } else if (pEntity == this) {
            return true;
        } else if (super.isAlliedTo(pEntity)) {
            return true;
        } else if (pEntity instanceof IrkEntity) {
            return this.isAlliedTo(((IrkEntity)pEntity).getOwner());
        } else if (pEntity instanceof LivingEntity && ((LivingEntity)pEntity).getMobType() == CreatureAttribute.ILLAGER) {
            return this.getTeam() == null && pEntity.getTeam() == null;
        } else {
            return false;
        }
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Confused", this.getInvulnerableTicks());
        compound.putInt("Casting", this.getCasting());
        compound.putInt("CastTimes", this.getCastTimes());
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.setInvulnerableTicks(compound.getInt("Confused"));
        this.setCasting(compound.getInt("Casting"));
        this.setCastTimes(compound.getInt("CastTimes"));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
        this.bossInfo.setId(this.getUUID());
    }

    public void setCustomName(@Nullable ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo.setVisible(this.getInvulnerableTicks() <= 0);
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    public void startSeenByPlayer(ServerPlayerEntity pPlayer) {
        super.startSeenByPlayer(pPlayer);
        this.bossInfo.addPlayer(pPlayer);
    }

    public void stopSeenByPlayer(ServerPlayerEntity pPlayer) {
        super.stopSeenByPlayer(pPlayer);
        this.bossInfo.removePlayer(pPlayer);
    }

    private boolean getVizierFlag(int mask) {
        int i = this.entityData.get(VIZIER_FLAGS);
        return (i & mask) != 0;
    }

    private void setVizierFlag(int mask, boolean value) {
        int i = this.entityData.get(VIZIER_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(VIZIER_FLAGS, (byte)(i & 255));
    }

    public boolean isCharging() {
        return this.getVizierFlag(1);
    }

    public void setCharging(boolean charging) {
        this.setVizierFlag(1, charging);
    }

    public boolean isSpellcasting(){
        return this.getVizierFlag(2);
    }

    public void setSpellcasting(boolean spellcasting){
        this.setVizierFlag(2, spellcasting);
    }

    @OnlyIn(Dist.CLIENT)
    public ArmPose getArmPose() {
        if (this.isCharging()) {
            return ArmPose.ATTACKING;
        } else if (this.isSpellcasting()){
            return ArmPose.SPELLCASTING;
        } else {
            return this.isCelebrating() ? ArmPose.CELEBRATING : ArmPose.CROSSED;
        }
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
        this.setDropChance(EquipmentSlotType.MAINHAND, 0.0F);
    }

    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropCustomDeathLoot(source, looting, recentlyHitIn);
        ItemEntity itementity = this.spawnAtLocation(ModItems.SOULRUBY.get());
        if (itementity != null) {
            itementity.setExtendedLifetime();
        }

    }

    public void applyRaidBuffs(int wave, boolean p_213660_2_) {
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    @Override
    public boolean isPowered() {
        return this.isSpellcasting();
    }

    class FangsSpellGoal extends Goal {
        int duration;
        int duration2;
        private FangsSpellGoal() {
        }

        public boolean canUse() {
            return VizierEntity.this.getCasting() >= 200
                    && VizierEntity.this.getTarget() != null
                    && !VizierEntity.this.isCharging()
                    && VizierEntity.this.getCastTimes() == 0;
        }

        public void start() {
            VizierEntity.this.playSound(SoundEvents.EVOKER_PREPARE_ATTACK, 1.0F, 1.0F);
            VizierEntity.this.setSpellcasting(true);
        }

        public void stop() {
            VizierEntity.this.setSpellcasting(false);
            VizierEntity.this.setCasting(0);
            VizierEntity.this.setCastTimes(1);
            this.duration2 = 0;
            this.duration = 0;
        }

        public void tick() {
            LivingEntity livingentity = VizierEntity.this.getTarget();
            if (livingentity != null) {
                ++this.duration;
                ++this.duration2;
                if (VizierEntity.this.getHealth() <= VizierEntity.this.getMaxHealth() / 2) {
                    if (this.duration >= 5) {
                        this.duration = 0;
                        float f = (float) MathHelper.atan2(livingentity.getZ() - VizierEntity.this.getZ(), livingentity.getX() - VizierEntity.this.getX());
                        this.spawnFangs(livingentity.getX(), livingentity.getZ(), livingentity.getY(), livingentity.getY() + 1.0D, f, 1);
                    }
                } else {
                    if (this.duration >= 10) {
                        this.duration = 0;
                        float f = (float) MathHelper.atan2(livingentity.getZ() - VizierEntity.this.getZ(), livingentity.getX() - VizierEntity.this.getX());
                        this.spawnFangs(livingentity.getX(), livingentity.getZ(), livingentity.getY(), livingentity.getY() + 1.0D, f, 1);
                    }
                }
                if (this.duration2 >= 160) {
                    VizierEntity.this.setSpellcasting(false);
                    VizierEntity.this.setCasting(0);
                    VizierEntity.this.setCastTimes(1);
                    this.duration2 = 0;
                    this.duration = 0;
                }
            } else {
                stop();
            }
        }

        private void spawnFangs(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
            BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
            boolean flag = false;
            double d0 = 0.0D;

            do {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate = VizierEntity.this.level.getBlockState(blockpos1);
                if (blockstate.isFaceSturdy(VizierEntity.this.level, blockpos1, Direction.UP)) {
                    if (!VizierEntity.this.level.isEmptyBlock(blockpos)) {
                        BlockState blockstate1 = VizierEntity.this.level.getBlockState(blockpos);
                        VoxelShape voxelshape = blockstate1.getCollisionShape(VizierEntity.this.level, blockpos);
                        if (!voxelshape.isEmpty()) {
                            d0 = voxelshape.max(Direction.Axis.Y);
                        }
                    }

                    flag = true;
                    break;
                }

                blockpos = blockpos.below();
            } while(blockpos.getY() >= MathHelper.floor(p_190876_5_) - 1);

            if (flag) {
                VizierEntity.this.level.addFreshEntity(new EvokerFangsEntity(VizierEntity.this.level, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, VizierEntity.this));
            }

        }
    }

    class HealGoal extends Goal {
        int duration3;

        private HealGoal() {
        }

        public boolean canUse(){
            return VizierEntity.this.getTarget() != null
                    && !VizierEntity.this.isCharging()
                    && VizierEntity.this.getCasting() >= 100
                    && VizierEntity.this.getCastTimes() == 2;
        }

        public void start() {
            int i = 0;
            for (MonsterEntity ally : VizierEntity.this.level.getEntitiesOfClass(MonsterEntity.class, VizierEntity.this.getBoundingBox().inflate(64.0D), field_213690_b)) {
                if (ally instanceof VexEntity || ally instanceof IrkEntity){
                    ++i;
                }
            }
            if (i >= 2) {
                VizierEntity.this.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, 1.0F, 1.0F);
                VizierEntity.this.playSound(SoundEvents.EVOKER_CELEBRATE, 1.0F, 0.75F);
                VizierEntity.this.setSpellcasting(true);
            } else {
                VizierEntity.this.setCastTimes(0);
                VizierEntity.this.setCasting(0);
                this.duration3 = 0;
            }
        }

        public void stop() {
            VizierEntity.this.setSpellcasting(false);
            VizierEntity.this.setCastTimes(0);
            VizierEntity.this.setCasting(0);
            this.duration3 = 0;
        }

        public void tick() {
            int i = 0;
            ++this.duration3;
            if (this.duration3 >= 60) {
                for (MonsterEntity ally : VizierEntity.this.level.getEntitiesOfClass(MonsterEntity.class, VizierEntity.this.getBoundingBox().inflate(64.0D), field_213690_b)) {
                    if (ally instanceof VexEntity || ally instanceof IrkEntity){
                        VizierEntity.this.heal(ally.getHealth());
                        ++i;
                        ally.hurt(DamageSource.STARVE, 200.0F);
                    }
                }
                if (i != 0) {
                    VizierEntity.this.playSound(SoundEvents.TOTEM_USE, 1.0F, 1.0F);
                }
                this.duration3 = 0;
                VizierEntity.this.setSpellcasting(false);
                VizierEntity.this.setCastTimes(0);
                VizierEntity.this.setCasting(0);
            }
        }

    }

    class ChargeAttackGoal extends Goal {
        public ChargeAttackGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            if (VizierEntity.this.getTarget() != null
                    && !VizierEntity.this.getMoveControl().hasWanted()
                    && !VizierEntity.this.isSpellcasting()
                    && VizierEntity.this.random.nextInt(7) == 0) {
                return VizierEntity.this.distanceToSqr(VizierEntity.this.getTarget()) > 4.0D;
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return VizierEntity.this.getMoveControl().hasWanted()
                    && VizierEntity.this.isCharging()
                    && !VizierEntity.this.isSpellcasting()
                    && VizierEntity.this.getTarget() != null
                    && VizierEntity.this.getTarget().isAlive();
        }

        public void start() {
            LivingEntity livingentity = VizierEntity.this.getTarget();
            Vector3d vector3d = livingentity.position();
            VizierEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            VizierEntity.this.setCharging(true);
            VizierEntity.this.playSound(SoundEvents.EVOKER_CELEBRATE, 1.0F, 0.75F);
        }

        public void stop() {
            VizierEntity.this.setCharging(false);
        }

        public void tick() {
            LivingEntity livingentity = VizierEntity.this.getTarget();
            VizierEntity.this.getLookControl().setLookAt(livingentity.position());
            if (VizierEntity.this.getBoundingBox().inflate(1.0D).intersects(livingentity.getBoundingBox())) {
                VizierEntity.this.doHurtTarget(livingentity);
                VizierEntity.this.setCharging(false);
            } else {
                double d0 = VizierEntity.this.distanceToSqr(livingentity);
                if (d0 < 9.0D) {
                    Vector3d vector3d = livingentity.getEyePosition(1.0F);
                    VizierEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
                }
            }

        }
    }

    class SpikesGoal extends Goal {
        int duration;

        @Override
        public boolean canUse() {
            return VizierEntity.this.getTarget() != null
                    && !VizierEntity.this.isCharging()
                    && VizierEntity.this.getCasting() >= 100
                    && VizierEntity.this.getCastTimes() == 3;
        }

        public void start() {
            VizierEntity.this.playSound(SoundEvents.EVOKER_PREPARE_SUMMON, 1.0F, 0.5F);
            VizierEntity.this.setSpellcasting(true);
        }

        public void stop() {
            VizierEntity.this.setSpellcasting(false);
            VizierEntity.this.setCastTimes(0);
            VizierEntity.this.setCasting(0);
            this.duration = 0;
        }

        public void tick() {
            if (!VizierEntity.this.level.isClientSide) {
                LivingEntity livingentity = VizierEntity.this.getTarget();
                if (livingentity != null) {
                    VizierEntity.this.getLookControl().setLookAt(livingentity, VizierEntity.this.getMaxHeadXRot(), VizierEntity.this.getMaxHeadYRot());
                    double d0 = Math.min(livingentity.getY(), VizierEntity.this.getY());
                    double d1 = Math.max(livingentity.getY(), VizierEntity.this.getY()) + 1.0D;
                    float f = (float) MathHelper.atan2(livingentity.getZ() - VizierEntity.this.getZ(), livingentity.getX() - VizierEntity.this.getX());
                    ++this.duration;
                    if (this.duration >= 40) {
                        for (int l = 0; l < 16; ++l) {
                            double d2 = 1.25D * (double) (l + 1);
                            this.createSpellEntity(VizierEntity.this.getX() + (double) MathHelper.cos(f) * d2, VizierEntity.this.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, l * 2);
                        }
                        this.duration = 0;
                        VizierEntity.this.setSpellcasting(false);
                        VizierEntity.this.setCastTimes(0);
                        VizierEntity.this.setCasting(0);
                        VizierEntity.this.playSound(SoundEvents.EVOKER_CAST_SPELL, 1.0F, 0.5F);
                    }
                } else {
                    stop();
                }
            }
        }

        private void createSpellEntity(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
            BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
            boolean flag = false;
            double d0 = 0.0D;

            do {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate = VizierEntity.this.level.getBlockState(blockpos1);
                if (blockstate.isFaceSturdy(VizierEntity.this.level, blockpos1, Direction.UP)) {
                    if (!VizierEntity.this.level.isEmptyBlock(blockpos)) {
                        BlockState blockstate1 = VizierEntity.this.level.getBlockState(blockpos);
                        VoxelShape voxelshape = blockstate1.getCollisionShape(VizierEntity.this.level, blockpos);
                        if (!voxelshape.isEmpty()) {
                            d0 = voxelshape.max(Direction.Axis.Y);
                        }
                    }

                    flag = true;
                    break;
                }

                blockpos = blockpos.below();
            } while(blockpos.getY() >= MathHelper.floor(p_190876_5_) - 1);

            if (flag) {
                SpikeEntity spikeEntity = new SpikeEntity(VizierEntity.this.level, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, VizierEntity.this);
                VizierEntity.this.level.addFreshEntity(spikeEntity);
            }

        }
    }

    class SonicBoomGoal extends Goal {
        int duration;
        public Vector3d vector3d;

        @Override
        public boolean canUse() {
            return VizierEntity.this.getTarget() != null
                    && !VizierEntity.this.isCharging()
                    && VizierEntity.this.getCasting() >= 100
                    && VizierEntity.this.getCastTimes() == 3;
        }

        public void start() {
            VizierEntity.this.playSound(ModSounds.SONIC_CHARGE.get(), 1.0F, 1.0F);
            if (VizierEntity.this.getTarget() != null) {
                this.vector3d = VizierEntity.this.getTarget().position();
            }
            VizierEntity.this.setSpellcasting(true);
        }

        public void stop() {
            VizierEntity.this.setSpellcasting(false);
            VizierEntity.this.setCastTimes(0);
            VizierEntity.this.setCasting(0);
            this.duration = 0;
        }

        public void tick() {
            if (!VizierEntity.this.level.isClientSide) {
                ServerWorld serverWorld = (ServerWorld) VizierEntity.this.level;
                ++this.duration;
                if (this.duration < 30){
                    if (VizierEntity.this.getTarget() != null) {
                        this.vector3d = VizierEntity.this.getTarget().position();
                    } else {
                        stop();
                    }
                } else {
                    BlockPos blockPos = new BlockPos(vector3d);
                    ServerParticleUtil.gatheringBlockParticles(ModParticleTypes.SONIC_GATHER.get(), blockPos, serverWorld);

                }
                VizierEntity.this.getLookControl().setLookAt(vector3d.x, vector3d.y, vector3d.z);
                ServerParticleUtil.gatheringParticles(ModParticleTypes.SONIC_GATHER.get(), VizierEntity.this, serverWorld);
                if (this.duration >= 60) {
                    Vector3d srcVec = new Vector3d(VizierEntity.this.getX(), VizierEntity.this.getEyeY(), VizierEntity.this.getZ());
                    Vector3d vector3d1 = vector3d.subtract(srcVec);
                    Vector3d vector3d2 = vector3d1.normalize();
                    for (int i = 1; i < Math.floor(vector3d1.length()) + 7; ++i) {
                        Vector3d vector3d3 = srcVec.add(vector3d2.scale((double) i));
                        serverWorld.sendParticles(ModParticleTypes.SONIC_BOOM.get(), vector3d3.x, vector3d3.y, vector3d3.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                    }
                    for (Entity target : getTargets(VizierEntity.this, 15, 1.0F)) {
                        if (target instanceof LivingEntity) {
                            LivingEntity target1 = (LivingEntity) target;
                            target1.hurt(ModDamageSource.sonicBoom(VizierEntity.this), 10.0F);
                            double d0 = target1.getX() - VizierEntity.this.getX();
                            double d1 = target1.getZ() - VizierEntity.this.getZ();
                            double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                            MobUtil.push(target1, d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
                        }
                    }
                    this.duration = 0;
                    VizierEntity.this.setSpellcasting(false);
                    VizierEntity.this.setCastTimes(0);
                    VizierEntity.this.setCasting(0);
                    VizierEntity.this.playSound(ModSounds.ROAR_SPELL.get(), 3.0F, 0.25F);
                }
            }
        }

        public List<Entity> getTargets(LivingEntity livingEntity, double range, float size) {
            List<Entity> target = new ArrayList<>();
            Vector3d lookVec = livingEntity.getViewVector(1.0F);
            List<Entity> entities = livingEntity.level.getEntities(livingEntity, livingEntity.getBoundingBox().expandTowards(lookVec.x * range, lookVec.y * range, lookVec.z * range).inflate(size, size, size));

            for (Entity entity : entities) {
                if (entity.isPickable()) {
                    target.add(entity);
                }
            }
            return target;
        }
    }

    class MoveRandomGoal extends Goal {
        public MoveRandomGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return !VizierEntity.this.getMoveControl().hasWanted()
                    && VizierEntity.this.random.nextInt(7) == 0
                    && !VizierEntity.this.isCharging()
                    && VizierEntity.this.getTarget() == null;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = VizierEntity.this.blockPosition();

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.offset(VizierEntity.this.random.nextInt(8) - 4, VizierEntity.this.random.nextInt(6) - 2, VizierEntity.this.random.nextInt(8) - 4);
                if (VizierEntity.this.level.isEmptyBlock(blockpos1)) {
                    VizierEntity.this.moveControl.setWantedPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
                    if (VizierEntity.this.getTarget() == null) {
                        VizierEntity.this.getLookControl().setLookAt((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }

    class DoNothingGoal extends Goal {
        public DoNothingGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return VizierEntity.this.getInvulnerableTicks() > 0;
        }
    }

    public boolean canChangeDimensions() {
        return false;
    }

}
