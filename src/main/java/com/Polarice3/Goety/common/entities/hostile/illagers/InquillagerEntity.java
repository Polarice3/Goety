package com.Polarice3.Goety.common.entities.hostile.illagers;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.utils.ParticleUtil;
import com.google.common.collect.Maps;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class InquillagerEntity extends HuntingIllagerEntity {
    public InquillagerEntity(EntityType<? extends InquillagerEntity> p_i48556_1_, World p_i48556_2_) {
        super(p_i48556_1_, p_i48556_2_);
        this.xpReward = 20;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new CastingSpellGoal());
        this.goalSelector.addGoal(2, new HealingSelfSpellGoal());
        this.goalSelector.addGoal(2, new ThrowPotionGoal(this));
        this.goalSelector.addGoal(2, new AttackGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld pLevel, DifficultyInstance pDifficulty, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
        ILivingEntityData ilivingentitydata = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        ((GroundPathNavigator)this.getNavigation()).setCanOpenDoors(true);
        this.populateDefaultEquipmentSlots(pDifficulty);
        this.populateDefaultEquipmentEnchantments(pDifficulty);
        return ilivingentitydata;
    }

    public boolean hurt(@Nonnull DamageSource source, float amount) {
        if (source == DamageSource.MAGIC){
            return false;
        } else {
            return super.hurt(source, amount);
        }
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance pDifficulty) {
        if (this.getCurrentRaid() == null) {
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
        }
    }

    protected void enchantSpawnedWeapon(float p_241844_1_) {
        super.enchantSpawnedWeapon(p_241844_1_);
        ItemStack itemstack = this.getMainHandItem();
        if (itemstack.getItem() == Items.IRON_SWORD) {
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack);
            map.putIfAbsent(Enchantments.FIRE_ASPECT, 2);
            EnchantmentHelper.setEnchantments(map, itemstack);
            this.setItemSlot(EquipmentSlotType.MAINHAND, itemstack);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ArmPose getArmPose() {
        if (this.isCastingSpell()) {
            return ArmPose.SPELLCASTING;
        } else if (this.isAggressive()) {
            return ArmPose.ATTACKING;
        } else {
            return this.isCelebrating() ? ArmPose.CELEBRATING : ArmPose.CROSSED;
        }
    }

    public boolean isAlliedTo(Entity pEntity) {
        if (pEntity == this) {
            return true;
        } else if (super.isAlliedTo(pEntity)) {
            return true;
        } else if (pEntity instanceof VexEntity) {
            return this.isAlliedTo(((VexEntity)pEntity).getOwner());
        } else if (pEntity instanceof LivingEntity && ((LivingEntity)pEntity).getMobType() == CreatureAttribute.ILLAGER) {
            return this.getTeam() == null && pEntity.getTeam() == null;
        } else {
            return false;
        }
    }

    public void playAmbientSound() {
        SoundEvent soundevent = this.getAmbientSound();
        if (soundevent != null) {
            this.playSound(soundevent, 1.0F, 1.5F);
        }
    }

    protected void playHurtSound(DamageSource pSource) {
        SoundEvent soundevent = this.getHurtSound(pSource);
        if (soundevent != null) {
            this.playSound(soundevent, 1.0F, 1.5F);
        }
    }

    public void die(DamageSource pCause) {
        SoundEvent soundevent = SoundEvents.VINDICATOR_DEATH;
        this.playSound(soundevent, 1.0F, 1.5F);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATOR_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.VINDICATOR_HURT;
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.VINDICATOR_CELEBRATE;
    }

    @Override
    public void applyRaidBuffs(int pWave, boolean p_213660_2_) {
        ItemStack itemstack = new ItemStack(Items.IRON_SWORD);
        Raid raid = this.getCurrentRaid();
        int i = 2;
        if (pWave > raid.getNumGroups(Difficulty.NORMAL)) {
            i = 4;
        }

        boolean flag = this.random.nextFloat() <= raid.getEnchantOdds();
        if (flag) {
            Map<Enchantment, Integer> map = Maps.newHashMap();
            map.put(Enchantments.SHARPNESS, i);
            EnchantmentHelper.setEnchantments(map, itemstack);
        }

        this.setItemSlot(EquipmentSlotType.MAINHAND, itemstack);
    }

    class CastingSpellGoal extends CastingASpellGoal {
        private CastingSpellGoal() {
        }

        public void tick() {
            if (InquillagerEntity.this.getTarget() != null) {
                InquillagerEntity.this.getLookControl().setLookAt(InquillagerEntity.this.getTarget(), (float)InquillagerEntity.this.getMaxHeadYRot(), (float)InquillagerEntity.this.getMaxHeadXRot());
            }

        }
    }

    class HealingSelfSpellGoal extends UseSpellGoal {

        private HealingSelfSpellGoal() {
        }

        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else {
                return InquillagerEntity.this.getHealth() < InquillagerEntity.this.getMaxHealth()/2;
            }
        }

        protected int getCastingTime() {
            return 5;
        }

        protected int getCastingInterval() {
            return 20;
        }

        protected void performSpellCasting() {
            InquillagerEntity.this.heal(InquillagerEntity.this.getMaxHealth());
            for(int i = 0; i < 5; ++i) {
                double d0 = InquillagerEntity.this.random.nextGaussian() * 0.02D;
                double d1 = InquillagerEntity.this.random.nextGaussian() * 0.02D;
                double d2 = InquillagerEntity.this.random.nextGaussian() * 0.02D;
                new ParticleUtil(ModParticleTypes.HEAL_EFFECT.get(), InquillagerEntity.this.getRandomX(1.0D), InquillagerEntity.this.getRandomY() + 1.0D, InquillagerEntity.this.getRandomZ(1.0D), d0, d1, d2);
            }
            InquillagerEntity.this.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE, 1.0F, 2.0F);
        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        protected SpellType getSpell() {
            return SpellType.SUMMON_VEX;
        }
    }

    static class ThrowPotionGoal extends Goal{
        public int bombTimer;
        public InquillagerEntity inquillager;

        public ThrowPotionGoal(InquillagerEntity inquillager){
            this.inquillager = inquillager;
        }

        @Override
        public boolean canUse() {
            if (this.inquillager.getTarget() != null){
                LivingEntity entity = this.inquillager.getTarget();
                return this.inquillager.distanceTo(entity) > 4.0 && this.inquillager.distanceTo(entity) <= 10;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.inquillager.getTarget() != null && !this.inquillager.getTarget().isDeadOrDying();
        }

        @Override
        public void stop() {
            this.bombTimer = 0;
        }

        @Override
        public void tick() {
            super.tick();
            ++this.bombTimer;
            if (this.bombTimer >= 60) {
                LivingEntity livingEntity = this.inquillager.getTarget();
                if (livingEntity != null) {
                    boolean flag = this.inquillager.getSensing().canSee(livingEntity);
                    if (flag) {
                        Vector3d vector3d = livingEntity.getDeltaMovement();
                        double d0 = livingEntity.getX() + vector3d.x - this.inquillager.getX();
                        double d1 = livingEntity.getEyeY() - (double) 1.1F - this.inquillager.getY();
                        double d2 = livingEntity.getZ() + vector3d.z - this.inquillager.getZ();
                        float f = MathHelper.sqrt(d0 * d0 + d2 * d2);
                        PotionEntity potionentity = new PotionEntity(this.inquillager.level, this.inquillager);
                        potionentity.setItem(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.HARMING));
                        potionentity.xRot -= -20.0F;
                        potionentity.shoot(d0, d1 + (double) (f * 0.2F), d2, 0.75F, 8.0F);
                        if (!this.inquillager.isSilent()) {
                            this.inquillager.level.playSound((PlayerEntity) null, this.inquillager.getX(), this.inquillager.getY(), this.inquillager.getZ(), SoundEvents.WITCH_THROW, this.inquillager.getSoundSource(), 1.0F, 0.8F + this.inquillager.random.nextFloat() * 0.4F);
                        }
                        this.inquillager.level.addFreshEntity(potionentity);
                        this.bombTimer = 0;
                    }
                }
            }
        }
    }

    class AttackGoal extends MeleeAttackGoal {
        public AttackGoal(InquillagerEntity p_i50577_2_) {
            super(p_i50577_2_, 1.0D, false);
        }

        protected double getAttackReachSqr(LivingEntity pAttackTarget) {
            if (this.mob.getVehicle() instanceof RavagerEntity) {
                float f = this.mob.getVehicle().getBbWidth() - 0.1F;
                return (double)(f * 2.0F * f * 2.0F + pAttackTarget.getBbWidth());
            } else {
                return super.getAttackReachSqr(pAttackTarget);
            }
        }
    }
}
