package com.Polarice3.Goety.common.entities.bosses;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.hostile.IrkEntity;
import com.Polarice3.Goety.common.entities.neutral.FlyingPhaseEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.utils.EntityHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
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
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

@OnlyIn(
        value = Dist.CLIENT,
        _interface = IChargeableMob.class
)
public class VizierEntity extends SpellcastingIllagerEntity implements IChargeableMob, FlyingPhaseEntity {
    private static final Predicate<Entity> field_213690_b = (p_213685_0_) -> {
        return p_213685_0_.isAlive() && !(p_213685_0_ instanceof VizierEntity);
    };
    protected static final DataParameter<Byte> VIZIER_FLAGS = EntityDataManager.defineId(VizierEntity.class, DataSerializers.BYTE);
    private final ServerBossInfo bossInfo = new ServerBossInfo(this.getDisplayName(), BossInfo.Color.YELLOW, BossInfo.Overlay.PROGRESS);
    public int casting;
    public int finishcasting;
    public int casttimes;

    public VizierEntity(EntityType<? extends VizierEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveControl = new EntityHelper.MoveHelperController(this);
        this.xpReward = 50;
        this.finishcasting = 0;
    }

    public void move(MoverType typeIn, Vector3d pos) {
        super.move(typeIn, pos);
        this.checkInsideBlocks();
    }

    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
        if (!this.isSpellcasting()){
            ++this.casting;
        }
        if (this.finishcasting > 0){
            this.casting = 0;
            this.finishcasting = 0;
        }
    }

    protected SoundEvent getCastingSoundEvent () {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new FangsSpellGoal());
        this.goalSelector.addGoal(1, new HealGoal());
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

    public boolean hurt(DamageSource pSource, float pAmount) {
        LivingEntity livingEntity = this.getTarget();
        if (livingEntity != null){
            if (pSource.getEntity() instanceof IrkEntity){
                return false;
            } else {
                if (!MainConfig.VizierMinion.get()) {
                    int random = this.level.random.nextInt(2);
                    if (random == 1 || this.level.getDifficulty() == Difficulty.HARD) {
                        IrkEntity irk = new IrkEntity(ModEntityType.IRK.get(), this.level);
                        irk.setPos(this.getX(), this.getY(), this.getZ());
                        irk.setOwner(this);
                        this.level.addFreshEntity(irk);
                    }
                } else {
                    int random = this.level.random.nextInt(2);
                    if (random == 1 || this.level.getDifficulty() == Difficulty.HARD) {
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

    public boolean isAlliedTo(Entity entityIn) {
        if (entityIn == this) {
            return true;
        } else if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof IrkEntity) {
            return this.isAlliedTo(((IrkEntity)entityIn).owner);
        } else if (entityIn instanceof LivingEntity && ((LivingEntity)entityIn).getMobType() == CreatureAttribute.ILLAGER) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }

    }

    public void setCustomName(@Nullable ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
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

    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    public void startSeenByPlayer(ServerPlayerEntity player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    public void stopSeenByPlayer(ServerPlayerEntity player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }

    public boolean isNonBoss() {
        return false;
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
            return VizierEntity.this.casting >= 200 && VizierEntity.this.getTarget() != null && !VizierEntity.this.isCharging() && VizierEntity.this.casttimes != 2;
        }

        public void start() {
            VizierEntity.this.playSound(SoundEvents.EVOKER_PREPARE_ATTACK, 1.0F, 1.0F);
            VizierEntity.this.setSpellcasting(true);
        }

        public void stop() {
            VizierEntity.this.setSpellcasting(false);
        }

        public void tick() {
            LivingEntity livingentity = VizierEntity.this.getTarget();
            ++this.duration;
            ++this.duration2;
            if (VizierEntity.this.level.getDifficulty() == Difficulty.HARD) {
                VizierEntity.this.moveControl.setWantedPosition(livingentity.getX() - 4, livingentity.getY() + 2.0D, livingentity.getZ() - 4, 1.0F);
            } else {
                VizierEntity.this.moveControl.setWantedPosition(livingentity.getX() - 2, livingentity.getY() + 2.0D, livingentity.getZ() - 2, 1.0F);
            }
            if (VizierEntity.this.getHealth() <= VizierEntity.this.getMaxHealth()/2) {
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
            if (this.duration2 >= 160){
                VizierEntity.this.setSpellcasting(false);
                VizierEntity.this.finishcasting = 1;
                ++VizierEntity.this.casttimes;
                this.duration2 = 0;
                this.duration = 0;
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
            return VizierEntity.this.getTarget() != null && !VizierEntity.this.isCharging() && VizierEntity.this.casting >= 100 && VizierEntity.this.casttimes == 2;
        }

        public void start() {
            VizierEntity.this.playSound(SoundEvents.EVOKER_PREPARE_ATTACK, 1.0F, 1.0F);
            VizierEntity.this.playSound(SoundEvents.EVOKER_CELEBRATE, 1.0F, 1.0F);
            VizierEntity.this.setSpellcasting(true);
        }

        public void stop() {
            VizierEntity.this.setSpellcasting(false);
        }

        public void tick() {
            LivingEntity livingentity = VizierEntity.this.getTarget();
            ++this.duration3;
            if (VizierEntity.this.level.getDifficulty() == Difficulty.HARD) {
                VizierEntity.this.moveControl.setWantedPosition(livingentity.getX() - 4, livingentity.getY() + 2.0D, livingentity.getZ() - 4, 1.0F);
            } else {
                VizierEntity.this.moveControl.setWantedPosition(livingentity.getX() - 2, livingentity.getY() + 2.0D, livingentity.getZ() - 2, 1.0F);
            }
            if (this.duration3 >= 60) {
                VizierEntity.this.playSound(SoundEvents.TOTEM_USE, 1.0F, 1.0F);
                if (!MainConfig.VizierMinion.get()) {
                    for (IrkEntity ally : VizierEntity.this.level.getEntitiesOfClass(IrkEntity.class, VizierEntity.this.getBoundingBox().inflate(64.0D), field_213690_b)) {
                        ally.hurt(DamageSource.STARVE, 200.0F);
                        VizierEntity.this.heal(5.0F);
                    }
                } else {
                    for (VexEntity ally : VizierEntity.this.level.getEntitiesOfClass(VexEntity.class, VizierEntity.this.getBoundingBox().inflate(64.0D), field_213690_b)) {
                        ally.hurt(DamageSource.STARVE, 200.0F);
                        VizierEntity.this.heal(5.0F);
                    }
                    this.duration3 = 0;
                }
                VizierEntity.this.setSpellcasting(false);
                VizierEntity.this.casttimes = 0;
                VizierEntity.this.finishcasting = 1;
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
                    && VizierEntity.this.casting < 200
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
                    && VizierEntity.this.casting < 200
                    && VizierEntity.this.getTarget() != null
                    && VizierEntity.this.getTarget().isAlive();
        }

        public void start() {
            LivingEntity livingentity = VizierEntity.this.getTarget();
            Vector3d vector3d = livingentity.position();
            VizierEntity.this.moveControl.setWantedPosition(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            VizierEntity.this.setCharging(true);
            VizierEntity.this.playSound(SoundEvents.EVOKER_CELEBRATE, 1.0F, 1.0F);
        }

        public void stop() {
            VizierEntity.this.setCharging(false);
        }

        public void tick() {
            LivingEntity livingentity = VizierEntity.this.getTarget();
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
}
