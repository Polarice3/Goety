package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.common.items.GoldTotemItem;
import com.Polarice3.Goety.utils.GoldTotemFinder;
import com.Polarice3.Goety.utils.ParticleUtil;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Objects;

public class ZombieMinionEntity extends SummonedEntity {

    public ZombieMinionEntity(EntityType<? extends SummonedEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public void tick() {
        if (this.limitedLifespan && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.hurt(DamageSource.STARVE, 2.0F);
        }
        if (MainConfig.ZombieLimit.get() > ZombieLimit(this.getTrueOwner())) {
            if (this.tickCount % 20 == 0) {
                this.hurt(DamageSource.STARVE, 5.0F);
            }
        }
        if (MainConfig.UndeadMinionHeal.get() && this.getHealth() < this.getMaxHealth()){
            if (this.getTrueOwner() != null && this.getTrueOwner() instanceof PlayerEntity) {
                if (RobeArmorFinder.FindNecroArmor(this.getTrueOwner())) {
                    PlayerEntity owner = (PlayerEntity) this.getTrueOwner();
                    ItemStack foundStack = GoldTotemFinder.FindTotem(owner);
                    if (!foundStack.isEmpty() && GoldTotemItem.currentSouls(foundStack) > 0) {
                        if (this.tickCount % 20 == 0) {
                            this.heal(1.0F);
                            Vector3d vector3d = this.getDeltaMovement();
                            new ParticleUtil(ParticleTypes.SOUL, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), vector3d.x * -0.2D, 0.1D, vector3d.z * -0.2D);
                            GoldTotemItem.decreaseSouls(foundStack, MainConfig.UndeadMinionHealCost.get());
                        }
                    }
                }
            }
        }
        super.tick();
    }

    public int ZombieLimit(LivingEntity entityLiving){
        return entityLiving.level.getNearbyEntities(ZombieMinionEntity.class, this.summonCountTargeting, entityLiving, entityLiving.getBoundingBox().inflate(64.0D)).size();
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(4, new ZombieAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomWalkingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.23F)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 2.0D);
    }

    public boolean doHurtTarget(Entity entityIn) {
        boolean flag = super.doHurtTarget(entityIn);
        if (flag) {
            float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            if (this.getMainHandItem().isEmpty() && this.isOnFire() && this.random.nextFloat() < f * 0.3F) {
                entityIn.setSecondsOnFire(2 * (int)f);
            }
        }

        return flag;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ZOMBIE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.ZOMBIE_STEP;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(difficulty);
        if (this.random.nextFloat() < (this.isUpgraded() ? 0.05F : 0.01F)) {
            int i = this.random.nextInt(3);
            if (i == 0) {
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
            } else {
                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
            }
        }

    }

    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        float f = difficultyIn.getSpecialMultiplier();
        this.setCanPickUpLoot(this.random.nextFloat() < 0.55F && this.isUpgraded());
        if (this.getItemBySlot(EquipmentSlotType.HEAD).isEmpty()) {
            LocalDate localdate = LocalDate.now();
            int i = localdate.get(ChronoField.DAY_OF_MONTH);
            int j = localdate.get(ChronoField.MONTH_OF_YEAR);
            if (j == 10 && i == 31 && this.random.nextFloat() < 0.25F) {
                this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(this.random.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.armorDropChances[EquipmentSlotType.HEAD.getIndex()] = 0.0F;
            }
        }
        this.handleAttributes(f);
        return spawnDataIn;
    }

    protected void handleAttributes(float difficulty) {
        Objects.requireNonNull(this.getAttribute(Attributes.KNOCKBACK_RESISTANCE)).addPermanentModifier(new AttributeModifier("random spawn bonus", this.random.nextDouble() * (double)0.05F, AttributeModifier.Operation.ADDITION));
        double d0 = this.random.nextDouble() * 1.5D * (double)difficulty;
        if (d0 > 1.0D) {
            Objects.requireNonNull(this.getAttribute(Attributes.FOLLOW_RANGE)).addPermanentModifier(new AttributeModifier("random zombie-spawn bonus", d0, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }

    }

    public void killed(ServerWorld world, LivingEntity killedEntity) {
        super.killed(world, killedEntity);
        int random = this.level.random.nextInt(2);
        if (this.isUpgraded() && killedEntity instanceof ZombieEntity && random == 1 && net.minecraftforge.event.ForgeEventFactory.canLivingConvert(killedEntity, ModEntityType.ZOMBIE_MINION.get(), (timer) -> {})) {
            ZombieEntity zombieEntity = (ZombieEntity)killedEntity;
            ZombieMinionEntity zombieMinionEntity = zombieEntity.convertTo(ModEntityType.ZOMBIE_MINION.get(), false);
            zombieMinionEntity.finalizeSpawn(world, level.getCurrentDifficultyAt(zombieMinionEntity.blockPosition()), SpawnReason.CONVERSION, null, (CompoundNBT)null);
            if (this.getTrueOwner() != null){
                zombieMinionEntity.setOwnerId(this.getTrueOwner().getUUID());
            }
            zombieMinionEntity.setLimitedLife(10 * (15 + this.level.random.nextInt(45)));
            net.minecraftforge.event.ForgeEventFactory.onLivingConvert(killedEntity, zombieMinionEntity);
            if (!this.isSilent()) {
                world.levelEvent((PlayerEntity)null, 1026, this.blockPosition(), 0);
            }
        }

    }

    public ActionResultType mobInteract(PlayerEntity p_230254_1_, Hand p_230254_2_) {
        ItemStack itemstack = p_230254_1_.getItemInHand(p_230254_2_);
        Item item = itemstack.getItem();
        if (item == Items.ROTTEN_FLESH && this.getHealth() < this.getMaxHealth()){
            if (!p_230254_1_.abilities.instabuild) {
                itemstack.shrink(1);
            }
            this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
            this.heal(2.0F);
            for (int i = 0; i < 7; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;
                this.level.addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
            }
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.PASS;
        }
    }


}
