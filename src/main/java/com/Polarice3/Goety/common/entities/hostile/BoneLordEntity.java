package com.Polarice3.Goety.common.entities.hostile;

import com.Polarice3.Goety.common.entities.projectiles.SoulSkullEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BoneLordEntity extends AbstractSkeletonEntity {
    private SkullLordEntity skullLord;

    public BoneLordEntity(EntityType<? extends AbstractSkeletonEntity> p_i48555_1_, World p_i48555_2_) {
        super(p_i48555_1_, p_i48555_2_);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(8, new FollowHeadGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 2.0D);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(pDifficulty);
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
        this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
        this.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
        this.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.DIAMOND_BOOTS));
        this.setItemSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getSkullLord() == null || this.getSkullLord().isDeadOrDying()){
            if (this.tickCount % 20 == 0){
                this.die(DamageSource.STARVE);
            }
        } else {
            if (this.getSkullLord().isHalfHealth()){
                this.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 20, 1));
            }
            if (this.isInWall()){
                this.moveTo(this.getSkullLord().position());
            }
        }
    }

    protected boolean isSunBurnTick() {
        return false;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() instanceof SkullLordEntity && pSource.getDirectEntity() instanceof SoulSkullEntity){
            return false;
        } else {
            return super.hurt(pSource, pAmount);
        }
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        this.setCanPickUpLoot(false);
        for(EquipmentSlotType equipmentslottype : EquipmentSlotType.values()) {
            this.setDropChance(equipmentslottype, 0.0F);
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
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

    public void makeStuckInBlock(BlockState pState, Vector3d pMotionMultiplier) {
        if (!pState.is(Blocks.COBWEB)) {
            super.makeStuckInBlock(pState, pMotionMultiplier);
        }

    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    public SkullLordEntity getSkullLord() {
        return skullLord;
    }

    public void setSkullLord(SkullLordEntity skullLord) {
        this.skullLord = skullLord;
    }

    public static class FollowHeadGoal extends Goal {
        private final BoneLordEntity boneLord;
        private LivingEntity owner;
        private final IWorldReader level;
        private final PathNavigator navigation;
        private int timeToRecalcPath;
        private float oldWaterCost;

        public FollowHeadGoal(BoneLordEntity boneLord) {
            this.boneLord = boneLord;
            this.level = boneLord.level;
            this.navigation = boneLord.getNavigation();
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.boneLord.getSkullLord();
            if (livingentity == null) {
                return false;
            } else if (livingentity.isSpectator()) {
                return false;
            } else if (this.boneLord.distanceToSqr(livingentity) < (double)(100)) {
                return false;
            } else if (this.boneLord.isAggressive()) {
                return false;
            } else {
                this.owner = livingentity;
                return true;
            }
        }

        public boolean canContinueToUse() {
            if (this.navigation.isDone()) {
                return false;
            } else if (this.boneLord.isAggressive()){
                return false;
            } else {
                return !(this.boneLord.distanceToSqr(this.owner) <= (double)(4));
            }
        }

        public void start() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.boneLord.getPathfindingMalus(PathNodeType.WATER);
            this.boneLord.setPathfindingMalus(PathNodeType.WATER, 0.0F);
        }

        public void stop() {
            this.owner = null;
            this.navigation.stop();
            this.boneLord.setPathfindingMalus(PathNodeType.WATER, this.oldWaterCost);
        }

        public void tick() {
            this.boneLord.getLookControl().setLookAt(this.owner, 10.0F, (float)this.boneLord.getMaxHeadXRot());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                if (!this.boneLord.isPassenger()) {
                    this.navigation.moveTo(this.owner, 1.0F);
                }
            }
        }

    }
}
