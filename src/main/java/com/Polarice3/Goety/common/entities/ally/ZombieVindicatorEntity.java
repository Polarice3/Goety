package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.AttributesConfig;
import com.Polarice3.Goety.common.entities.ai.NeutralZombieAttackGoal;
import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Objects;

public class ZombieVindicatorEntity extends ZombieMinionEntity{
    public ZombieVindicatorEntity(EntityType<? extends ZombieMinionEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new ModMeleeAttackGoal(this));
        this.goalSelector.addGoal(8, new WanderGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
    }

    public void targetSelectGoal(){
        super.targetSelectGoal();
        this.targetSelector.addGoal(1, new NaturalAttackGoal<>(this, AbstractVillagerEntity.class));
        this.targetSelector.addGoal(1, new NaturalAttackGoal<>(this, IronGolemEntity.class));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, AttributesConfig.ZombieVindicatorHealth.get())
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.23F)
                .add(Attributes.ATTACK_DAMAGE, AttributesConfig.ZombieVindicatorDamage.get())
                .add(Attributes.ARMOR, AttributesConfig.ZombieVindicatorArmor.get());
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    public boolean isUnderWaterConverting() {
        return false;
    }

    public boolean isBaby() {
        return false;
    }

    public void setBaby(boolean pChildZombie) {
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_VILLAGER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ZOMBIE_VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_VILLAGER_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.ZOMBIE_VILLAGER_STEP;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    protected boolean convertsInWater() {
        return false;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld p_34088_, DifficultyInstance p_34089_, SpawnReason p_34090_, @Nullable ILivingEntityData p_34091_, @Nullable CompoundNBT p_34092_) {
        ILivingEntityData spawngroupdata = super.finalizeSpawn(p_34088_, p_34089_, p_34090_, p_34091_, p_34092_);
        this.populateDefaultEquipmentSlots(p_34089_);
        if (this.isNatural()){
            this.setHostile(true);
        }
        return spawngroupdata;
    }

    protected void handleAttributes(float difficulty) {
        Objects.requireNonNull(this.getAttribute(Attributes.KNOCKBACK_RESISTANCE)).addPermanentModifier(new AttributeModifier("random spawn bonus", this.random.nextDouble() * (double)0.05F, AttributeModifier.Operation.ADDITION));
        double d0 = this.random.nextDouble() * 1.5D * (double)difficulty;
        if (d0 > 1.0D) {
            Objects.requireNonNull(this.getAttribute(Attributes.FOLLOW_RANGE)).addPermanentModifier(new AttributeModifier("random zombie-spawn bonus", d0, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }

    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance p_219150_) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
    }

    public void killed(ServerWorld world, LivingEntity killedEntity) {
        super.killed(world, killedEntity);
        float random = this.level.random.nextFloat();
        if (this.isUpgraded()){
            if (killedEntity instanceof VindicatorEntity){
                VindicatorEntity vindicator = (VindicatorEntity) killedEntity;
                if (random <= 0.5F && net.minecraftforge.event.ForgeEventFactory.canLivingConvert(killedEntity, ModEntityType.ZOMBIE_VINDICATOR.get(), (timer) -> {})) {
                    EntityType<? extends MobEntity> entityType = ModEntityType.ZOMBIE_VINDICATOR.get();
                    ZombieVindicatorEntity zombieVindicator = (ZombieVindicatorEntity) vindicator.convertTo(entityType, false);
                    if (zombieVindicator != null) {
                        zombieVindicator.finalizeSpawn(world, level.getCurrentDifficultyAt(zombieVindicator.blockPosition()), SpawnReason.CONVERSION, null, null);
                        zombieVindicator.setLimitedLife(10 * (15 + this.level.random.nextInt(45)));
                        if (this.getTrueOwner() != null){
                            zombieVindicator.setTrueOwner(this.getTrueOwner());
                        }
                        net.minecraftforge.event.ForgeEventFactory.onLivingConvert(killedEntity, zombieVindicator);
                        if (!this.isSilent()) {
                            world.levelEvent((PlayerEntity) null, 1026, this.blockPosition(), 0);
                        }
                    }
                }
            }
        }
    }

    static class ModMeleeAttackGoal extends NeutralZombieAttackGoal {
        public ModMeleeAttackGoal(ZombieVindicatorEntity p_34123_) {
            super(p_34123_, 1.0D, false);
        }

        protected double getAttackReachSqr(LivingEntity p_34125_) {
            if (this.mob.getVehicle() instanceof RavagerEntity) {
                float f = this.mob.getVehicle().getBbWidth() - 0.1F;
                return (double)(f * 2.0F * f * 2.0F + p_34125_.getBbWidth());
            } else {
                return super.getAttackReachSqr(p_34125_);
            }
        }
    }

    public static class NaturalAttackGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        protected ZombieVindicatorEntity zombieVindicator;

        public NaturalAttackGoal(ZombieVindicatorEntity zombieVindicator, Class<T> p_26061_) {
            super(zombieVindicator, p_26061_, true);
            this.zombieVindicator = zombieVindicator;
        }

        public boolean canUse() {
            return super.canUse() && this.zombieVindicator.isNatural() && (this.zombieVindicator.getTrueOwner() == null || this.zombieVindicator.getTrueOwner() instanceof AbstractIllagerEntity) && this.target != null;
        }
    }
}
