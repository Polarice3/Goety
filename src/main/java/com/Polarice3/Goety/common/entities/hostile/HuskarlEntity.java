package com.Polarice3.Goety.common.entities.hostile;

import com.Polarice3.Goety.common.entities.hostile.dead.FallenEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModSounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class HuskarlEntity extends ZombieEntity {
    public boolean shade;

    public HuskarlEntity(EntityType<? extends HuskarlEntity> p_i48549_1_, World p_i48549_2_) {
        super(p_i48549_1_, p_i48549_2_);
    }

    @Override
    protected void addBehaviourGoals() {
        super.addBehaviourGoals();
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, FallenEntity.class, false));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.23F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }

    protected PathNavigator createNavigation(World pLevel) {
        return new HuskarlEntity.Navigator(this, pLevel);
    }

    protected boolean isSunSensitive() {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.HUSKARL_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.HUSKARL_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.HUSKARL_DEATH.get();
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.ZOMBIE_STEP;
    }

    public void setShade(boolean shade){
        this.shade = shade;
    }

    public boolean isShade(){
        return this.shade;
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("shade", this.isShade());
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.shade = compound.getBoolean("shade");
        this.setShade(this.shade);
    }

    public void die(DamageSource cause) {
        super.die(cause);
        if (!this.level.isClientSide) {
            ServerWorld serverWorld = (ServerWorld) this.level;
            if (this.isShade()) {
                ShadeEntity shade = this.convertTo(ModEntityType.SHADE.get(), false);
                shade.setPos(this.getX(), this.getY(), this.getZ());
                if (this.hasCustomName()) {
                    shade.setCustomName(this.getCustomName());
                }
                shade.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.CONVERSION, null, null);
                serverWorld.addFreshEntity(shade);
            }
        }
    }

    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = super.doHurtTarget(pEntity);
        if (flag && this.getMainHandItem().isEmpty() && pEntity instanceof LivingEntity) {
            float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            ((LivingEntity)pEntity).addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 140 * (int)f));
        }

        return flag;
    }

    public boolean hurt(DamageSource source, float amount) {
        if (source.isProjectile()){
            return false;
        } else {
            return super.hurt(source, amount);
        }
    }

    protected boolean convertsInWater() {
        return true;
    }

    protected void doUnderWaterConversion() {
        this.convertToZombieType(EntityType.DROWNED);
        if (!this.isSilent()) {
            this.level.levelEvent((PlayerEntity)null, 1040, this.blockPosition(), 0);
        }

    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance pDifficulty) {
        super.populateDefaultEquipmentSlots(pDifficulty);
        int random = this.random.nextInt(12);
        int random2 = this.random.nextInt(2);
        switch (random){
            case 0:
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_SWORD));
                break;
            case 1:
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_AXE));
                break;
            case 2:
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
                break;
            case 3:
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
                break;
        }
        switch (random2){
            case 0:
                this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.LEATHER_HELMET));
                break;
            case 1:
                this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
        }
    }

    protected ItemStack getSkull() {
        return ItemStack.EMPTY;
    }

    static class Navigator extends GroundPathNavigator {
        public Navigator(MobEntity p_i50754_1_, World p_i50754_2_) {
            super(p_i50754_1_, p_i50754_2_);
        }

        protected PathFinder createPathFinder(int p_179679_1_) {
            this.nodeEvaluator = new HuskarlEntity.Processor();
            return new PathFinder(this.nodeEvaluator, p_179679_1_);
        }
    }

    static class Processor extends WalkNodeProcessor {
        private Processor() {
        }

        protected PathNodeType evaluateBlockPathType(IBlockReader pLevel, boolean pCanOpenDoors, boolean pCanEnterDoors, BlockPos pPos, PathNodeType pNodeType) {
            return pNodeType == PathNodeType.FENCE ? PathNodeType.OPEN : super.evaluateBlockPathType(pLevel, pCanOpenDoors, pCanEnterDoors, pPos, pNodeType);
        }
    }

}
