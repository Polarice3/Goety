package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.AttributesConfig;
import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.init.ModSounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.EnumSet;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FrozenZombieMinionEntity extends ZombieMinionEntity implements IRangedAttackMob {
    public int throwCooldown;

    public FrozenZombieMinionEntity(EntityType<? extends SummonedEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new ThrowSnowballGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, AttributesConfig.FrozenZombieServantHealth.get())
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.23F)
                .add(Attributes.ATTACK_DAMAGE, AttributesConfig.FrozenZombieServantDamage.get())
                .add(Attributes.ARMOR, AttributesConfig.FrozenZombieServantArmor.get());
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("throwCooldown", this.throwCooldown);
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.throwCooldown = pCompound.getInt("throwCooldown");
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.FROZEN_ZOMBIE_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.FROZEN_ZOMBIE_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.FROZEN_ZOMBIE_DEATH.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.throwCooldown > 0){
            --this.throwCooldown;
        }
    }

    @Override
    public void performRangedAttack(LivingEntity p_33317_, float p_33318_) {
        SnowballEntity snowball = new SnowballEntity(this.level, this);
        double d0 = p_33317_.getEyeY() - (double)1.1F;
        double d1 = p_33317_.getX() - this.getX();
        double d2 = d0 - snowball.getY();
        double d3 = p_33317_.getZ() - this.getZ();
        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.2F;
        snowball.shoot(d1, d2 + d4, d3, 1.6F, 12.0F);
        this.playSound(SoundEvents.SNOWBALL_THROW, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(snowball);
        this.throwCooldown = 40;
    }

    @SubscribeEvent
    public static void FrozenAttack(LivingAttackEvent event){
        Entity victim = event.getEntity();
        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof FrozenZombieMinionEntity && victim instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) victim;
            living.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 60));
        }
    }

    static class ThrowSnowballGoal extends Goal {
        public FrozenZombieMinionEntity zombie;
        public int start;

        public ThrowSnowballGoal(FrozenZombieMinionEntity zombie){
            this.zombie = zombie;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity living = this.zombie.getTarget();
            if (living != null){
                return living.distanceTo(this.zombie) >= 6.0F
                        && living.distanceTo(this.zombie) <= 16.0F
                        && this.zombie.throwCooldown <= 0;
            }
            return false;
        }

        @Override
        public void start() {
            this.start = 10;
            if (this.zombie.getMainHandItem().isEmpty()){
                this.zombie.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.SNOWBALL));
            } else if (this.zombie.getOffhandItem().isEmpty()){
                this.zombie.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(Items.SNOWBALL));
            }
            super.start();
        }

        @Override
        public void stop() {
            this.start = 0;
            if (this.zombie.getMainHandItem().getItem() == Items.SNOWBALL){
                this.zombie.setItemSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
            } else if (this.zombie.getOffhandItem().getItem() == Items.SNOWBALL){
                this.zombie.setItemSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
            }
            super.stop();
        }

        @Override
        public void tick() {
            super.tick();
            LivingEntity living = this.zombie.getTarget();
            if (living != null){
                if (this.start <= 0){
                    this.zombie.performRangedAttack(living, 0);
                } else {
                    --this.start;
                    this.zombie.lookControl.setLookAt(living.position());
                    this.zombie.navigation.stop();
                }
            }
        }
    }
}
