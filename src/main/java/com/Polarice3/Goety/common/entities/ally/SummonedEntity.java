package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.api.entities.ally.IServant;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.ai.SummonTargetGoal;
import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.utils.ItemHelper;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import com.Polarice3.Goety.utils.SEHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.UUID;
import java.util.function.Predicate;

public class SummonedEntity extends OwnedEntity implements IServant {
    protected static final DataParameter<Byte> SUMMONED_FLAGS = EntityDataManager.defineId(SummonedEntity.class, DataSerializers.BYTE);
    private static final UUID SPEED_MODIFIER_UUID = UUID.fromString("9c47949c-b896-4802-8e8a-f08c50791a8a");
    private static final AttributeModifier SPEED_MODIFIER = new AttributeModifier(SPEED_MODIFIER_UUID, "Staying speed penalty", -1.0D, AttributeModifier.Operation.ADDITION);
    public boolean upgraded;

    protected SummonedEntity(EntityType<? extends OwnedEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(8, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelectGoal();
    }

    public void targetSelectGoal(){
        this.targetSelector.addGoal(1, new SummonTargetGoal<>(this));
    }

    public void checkDespawn() {
        if (this.isHostile()){
            super.checkDespawn();
        }
    }

    protected boolean isSunBurnTick() {
        if (this.level.isDay() && !this.level.isClientSide) {
            float f = this.getBrightness();
            BlockPos blockpos = this.getVehicle() instanceof BoatEntity ? (new BlockPos(this.getX(), (double)Math.round(this.getY()), this.getZ())).above() : new BlockPos(this.getX(), (double)Math.round(this.getY()), this.getZ());
            return f > 0.5F && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.level.canSeeSky(blockpos);
        }

        return false;
    }

    public ItemStack getProjectile(ItemStack pShootable) {
        if (pShootable.getItem() instanceof ShootableItem) {
            Predicate<ItemStack> predicate = ((ShootableItem)pShootable.getItem()).getSupportedHeldProjectiles();
            ItemStack itemstack = ShootableItem.getHeldProjectile(this, predicate);
            return itemstack.isEmpty() ? new ItemStack(Items.ARROW) : itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public void tick(){
        super.tick();
        ModifiableAttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (this.isStaying()){
            if (this.navigation.getPath() != null) {
                this.navigation.stop();
            }
            if (modifiableattributeinstance != null && this.getAttribute(Attributes.MOVEMENT_SPEED) != null) {
                modifiableattributeinstance.removeModifier(SPEED_MODIFIER);
                modifiableattributeinstance.addTransientModifier(SPEED_MODIFIER);
            }
            this.stayingPosition();
            if (this.isWandering()) {
                this.setWandering(false);
            }
        } else {
            if (modifiableattributeinstance != null && modifiableattributeinstance.hasModifier(SPEED_MODIFIER)) {
                modifiableattributeinstance.removeModifier(SPEED_MODIFIER);
            }
        }
        if (this.isWandering()){
            if (this.isStaying()) {
                this.setStaying(false);
            }
        }
        if (this.getTrueOwner() != null){
            if (RobeArmorFinder.FindNecroHelm(this.getTrueOwner()) && this.getMobType() == CreatureAttribute.UNDEAD){
                this.limitedLifespan = false;
            } else if (this.limitedLifeTicks > 0){
                this.limitedLifespan = true;
            }
            if (this.getTrueOwner().getItemBySlot(EquipmentSlotType.FEET).getItem() == ModItems.NECRO_BOOTS_OF_WANDER.get()){
                if (this.getMobType() == CreatureAttribute.UNDEAD){
                    this.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 100, 0, false, false, false));
                }
            }
            if (this.getMobType() == CreatureAttribute.UNDEAD) {
                if (!this.isOnFire()) {
                    if (SpellConfig.UndeadMinionHeal.get() && this.getHealth() < this.getMaxHealth()) {
                        if (this.getTrueOwner() instanceof PlayerEntity) {
                            if (RobeArmorFinder.FindNecroSet(this.getTrueOwner())) {
                                PlayerEntity owner = (PlayerEntity) this.getTrueOwner();
                                int SoulCost = SpellConfig.UndeadMinionHealCost.get();
                                if (RobeArmorFinder.FindLeggings(owner)){
                                    if (this.random.nextBoolean()){
                                        SoulCost = 0;
                                    }
                                }
                                if (SEHelper.getSoulsAmount(owner, SpellConfig.UndeadMinionHealCost.get())){
                                    if (this.tickCount % 20 == 0) {
                                        this.heal(1.0F);
                                        Vector3d vector3d = this.getDeltaMovement();
                                        if (!this.level.isClientSide){
                                            ServerWorld serverWorld = (ServerWorld) this.level;
                                            SEHelper.decreaseSouls(owner, SoulCost);
                                            serverWorld.sendParticles(ParticleTypes.SOUL, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), 0, vector3d.x * -0.2D, 0.1D, vector3d.z * -0.2D, 0.5F);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        boolean flag = this.isSunSensitive() && this.isSunBurnTick();
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
    }

    public void stayingPosition(){
        if (this.getTarget() != null){
            this.getLookControl().setLookAt(this.getTarget(), this.getMaxHeadYRot(), this.getMaxHeadXRot());
            double d2 = this.getTarget().getX() - this.getX();
            double d1 = this.getTarget().getZ() - this.getZ();
            this.yRot = -((float) MathHelper.atan2(d2, d1)) * (180F / (float) Math.PI);
            this.yBodyRot = this.yRot;
        }
    }

    protected boolean isSunSensitive() {
        return false;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld pLevel, DifficultyInstance pDifficulty, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        if (pReason == SpawnReason.MOB_SUMMONED && this.getTrueOwner() != null && this.getMobType() == CreatureAttribute.UNDEAD){
            for (int i = 0; i < pLevel.getLevel().random.nextInt(10) + 10; ++i) {
                pLevel.getLevel().sendParticles(ModParticleTypes.SUMMON.get(), this.getRandomX(1.5D), this.getRandomY(), this.getRandomZ(1.5D), 0, 0.0F, 1.0F, 0.0F, 1.0F);
            }
            pLevel.getLevel().sendParticles(ModParticleTypes.SOUL_EXPLODE.get(), this.getX(), this.getY(), this.getZ(), 0, 0, 2.0D, 0, 1.0F);
        }
        this.setWandering(false);
        this.setStaying(false);
        return pSpawnData;
    }

    public void die(DamageSource pCause) {
        if (!this.level.isClientSide && this.hasCustomName() && this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getTrueOwner() instanceof ServerPlayerEntity) {
            this.getTrueOwner().sendMessage(this.getCombatTracker().getDeathMessage(), Util.NIL_UUID);
        }
        super.die(pCause);
    }

    public boolean hurt(DamageSource source, float amount) {
        if (SpellConfig.MinionsMasterImmune.get()) {
            if (source.getEntity() instanceof SummonedEntity) {
                SummonedEntity summoned = (SummonedEntity) source.getEntity();
                if (!summoned.isHostile() && !this.isHostile()) {
                    if (summoned.getTrueOwner() == this.getTrueOwner() && this.getTrueOwner() != null) {
                        return false;
                    }
                }
            }
        }
        return super.hurt(source, amount);
    }

    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (flag) {
            if (this.getMobType() == CreatureAttribute.UNDEAD){
                float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
                if (this.getMainHandItem().isEmpty() && this.isOnFire() && this.random.nextFloat() < f * 0.3F) {
                    entityIn.setSecondsOnFire(2 * (int)f);
                }
            }
            if (!this.getMainHandItem().isEmpty() && this.getMainHandItem().isDamageableItem()){
                ItemHelper.hurtAndBreak(this.getMainHandItem(), 1, this);
            }
        }

        return flag;
    }

    protected void hurtArmor(DamageSource pDamageSource, float pDamage) {
        if (!(pDamage <= 0.0F)) {
            pDamage = pDamage / 4.0F;
            if (pDamage < 1.0F) {
                pDamage = 1.0F;
            }

            for(EquipmentSlotType equipmentSlotType : EquipmentSlotType.values()) {
                if (equipmentSlotType.getType() == EquipmentSlotType.Group.ARMOR) {
                    ItemStack itemstack = this.getItemBySlot(equipmentSlotType);
                    if ((!pDamageSource.isFire() || !itemstack.getItem().isFireResistant()) && itemstack.getItem() instanceof ArmorItem) {
                        itemstack.hurtAndBreak((int) pDamage, this, (p_214023_1_) -> {
                            p_214023_1_.broadcastBreakEvent(equipmentSlotType);
                        });
                    }
                }
            }

        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SUMMONED_FLAGS, (byte)0);
    }

    private boolean getFlag(int mask) {
        int i = this.entityData.get(SUMMONED_FLAGS);
        return (i & mask) != 0;
    }

    private void setFlags(int mask, boolean value) {
        int i = this.entityData.get(SUMMONED_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(SUMMONED_FLAGS, (byte)(i & 255));
    }

    public boolean isWandering() {
        return this.getFlag(1);
    }

    public void setWandering(boolean wandering) {
        this.setFlags(1, wandering);
    }

    public boolean isStaying(){
        return this.getFlag(2);
    }

    public void setStaying(boolean staying){
        this.setFlags(2, staying);
    }

    @Override
    public boolean canUpdateMove() {
        return this.getMobType() == CreatureAttribute.UNDEAD;
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.setUpgraded(compound.getBoolean("Upgraded"));
        this.setWandering(compound.getBoolean("wandering"));
        this.setStaying(compound.getBoolean("staying"));
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Upgraded", this.upgraded);

        if (this.isWandering()) {
            compound.putBoolean("wandering", true);
        }

        if (this.isStaying()) {
            compound.putBoolean("staying", true);
        }
    }

    public void updateMoveMode(PlayerEntity player){
        if (!this.isWandering() && !this.isStaying()){
            this.setWandering(true);
            this.setStaying(false);
            player.displayClientMessage(new TranslationTextComponent("info.goety.minion.wander", this.getDisplayName()), true);
        } else if (!this.isStaying()){
            this.setWandering(false);
            this.setStaying(true);
            player.displayClientMessage(new TranslationTextComponent("info.goety.minion.staying", this.getDisplayName()), true);
        } else {
            this.setWandering(false);
            this.setStaying(false);
            player.displayClientMessage(new TranslationTextComponent("info.goety.minion.follow", this.getDisplayName()), true);
        }
        this.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.0f, 1.0f);

    }

    public boolean isUpgraded() {
        return this.upgraded;
    }

    public void setUpgraded(boolean attackAll) {
        this.upgraded = attackAll;
    }

    public static class FollowOwnerGoal extends Goal {
        private final SummonedEntity summonedEntity;
        private LivingEntity owner;
        private final IWorldReader level;
        private final double followSpeed;
        private final PathNavigator navigation;
        private int timeToRecalcPath;
        private final float stopDistance;
        private final float startDistance;
        private float oldWaterCost;

        public FollowOwnerGoal(SummonedEntity summonedEntity, double speed, float startDistance, float stopDistance) {
            this.summonedEntity = summonedEntity;
            this.level = summonedEntity.level;
            this.followSpeed = speed;
            this.navigation = summonedEntity.getNavigation();
            this.startDistance = startDistance;
            this.stopDistance = stopDistance;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
            if (!(summonedEntity.getNavigation() instanceof GroundPathNavigator) && !(summonedEntity.getNavigation() instanceof FlyingPathNavigator)) {
                throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
            }
        }

        public boolean canUse() {
            LivingEntity livingentity = this.summonedEntity.getTrueOwner();
            if (livingentity == null) {
                return false;
            } else if (livingentity.isSpectator()) {
                return false;
            } else if (this.summonedEntity.distanceToSqr(livingentity) < (double)(MathHelper.square(this.startDistance))) {
                return false;
            } else if (this.summonedEntity.isWandering() || this.summonedEntity.isStaying()) {
                return false;
            } else if (this.summonedEntity.getTarget() != null) {
                return false;
            } else {
                this.owner = livingentity;
                return true;
            }
        }

        public boolean canContinueToUse() {
            if (this.navigation.isDone()) {
                return false;
            } else if (this.summonedEntity.getTarget() != null){
                return false;
            } else {
                return !(this.summonedEntity.distanceToSqr(this.owner) <= (double)(MathHelper.square(this.stopDistance)));
            }
        }

        public void start() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.summonedEntity.getPathfindingMalus(PathNodeType.WATER);
            this.summonedEntity.setPathfindingMalus(PathNodeType.WATER, 0.0F);
        }

        public void stop() {
            this.owner = null;
            this.navigation.stop();
            this.summonedEntity.setPathfindingMalus(PathNodeType.WATER, this.oldWaterCost);
        }

        public void tick() {
            if (this.owner != null) {
                this.summonedEntity.getLookControl().setLookAt(this.owner, 10.0F, (float) this.summonedEntity.getMaxHeadXRot());
                if (--this.timeToRecalcPath <= 0) {
                    this.timeToRecalcPath = 10;
                    if (!this.summonedEntity.isLeashed() && !this.summonedEntity.isPassenger()) {
                        if (this.summonedEntity.distanceToSqr(this.owner) >= 144.0D && SpellConfig.UndeadTeleport.get()) {
                            this.tryToTeleportNearEntity();
                        } else {
                            this.navigation.moveTo(this.owner, this.followSpeed);
                        }

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
                if (blockstate.getBlock() instanceof LeavesBlock) {
                    return false;
                } else {
                    BlockPos blockpos = pos.subtract(this.summonedEntity.blockPosition());
                    return this.level.noCollision(this.summonedEntity, this.summonedEntity.getBoundingBox().move(blockpos));
                }
            }
        }

        private int getRandomNumber(int min, int max) {
            return this.summonedEntity.getRandom().nextInt(max - min + 1) + min;
        }
    }

    public class WanderGoal extends WaterAvoidingRandomWalkingGoal{

        public WanderGoal(CreatureEntity p_i47301_1_, double p_i47301_2_) {
            this(p_i47301_1_, p_i47301_2_, 0.001F);
        }

        public WanderGoal(CreatureEntity entity, double speedModifier, float probability) {
            super(entity, speedModifier, probability);
        }

        public boolean canUse() {
            if (super.canUse()){
                return !SummonedEntity.this.isStaying() || SummonedEntity.this.getTrueOwner() == null || SummonedEntity.this.getNavigation() instanceof SwimmerPathNavigator;
            } else {
                return false;
            }
        }
    }
}
