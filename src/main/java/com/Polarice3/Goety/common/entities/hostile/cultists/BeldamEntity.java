package com.Polarice3.Goety.common.entities.hostile.cultists;

import com.Polarice3.Goety.MobConfig;
import com.Polarice3.Goety.common.entities.projectiles.BurningPotionEntity;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.LichdomHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.ToggleableNearestAttackableTargetGoal;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.*;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class BeldamEntity extends AbstractCultistEntity implements IRangedAttackMob, ICultist{
    private static final UUID SPEED_MODIFIER_DRINKING_UUID = UUID.fromString("5e3d9fa2-5930-4763-8b2e-778024f4a3a0");
    private static final AttributeModifier SPEED_MODIFIER_DRINKING = new AttributeModifier(SPEED_MODIFIER_DRINKING_UUID, "Drinking speed penalty", -0.25D, AttributeModifier.Operation.ADDITION);
    private static final DataParameter<Boolean> DATA_USING_ITEM = EntityDataManager.defineId(BeldamEntity.class, DataSerializers.BOOLEAN);
    private int usingTime;
    private TargetExpiringGoal<AbstractRaiderEntity> healRaidersGoal;
    private ToggleableNearestAttackableTargetGoal<PlayerEntity> attackPlayersGoal;

    public BeldamEntity(EntityType<? extends BeldamEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.healRaidersGoal = new TargetExpiringGoal<>(this, AbstractRaiderEntity.class, true,
                (raider) -> raider instanceof AbstractRaiderEntity
                        && ((AbstractRaiderEntity) raider).getTarget() != null
                        && ((AbstractRaiderEntity) raider).getTarget().isAlive()
                        && raider.distanceToSqr(((AbstractRaiderEntity) raider).getTarget()) >= 36.0D
                        && !(raider instanceof WitchEntity)
                        && !(raider instanceof BeldamEntity));
        this.attackPlayersGoal = new ToggleableNearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, (Predicate<LivingEntity>)null);
        this.goalSelector.addGoal(2, new PotionAttackGoal(this));
        this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(2, this.healRaidersGoal);
        this.targetSelector.addGoal(3, this.attackPlayersGoal);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_USING_ITEM, false);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.WITCH_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.WITCH_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.WITCH_DEATH;
    }

    public void setUsingItem(boolean pDrinkingPotion) {
        this.getEntityData().set(DATA_USING_ITEM, pDrinkingPotion);
    }

    public boolean isDrinkingPotion() {
        return this.getEntityData().get(DATA_USING_ITEM);
    }

    protected ResourceLocation getDefaultLootTable() {
        return EntityType.WITCH.getDefaultLootTable();
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, MobConfig.BeldamHealth.get())
                .add(Attributes.MOVEMENT_SPEED, 0.35D);
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        if ((double)worldIn.getRandom().nextFloat() < 0.05D) {
            CrimsonSpiderEntity spider = new CrimsonSpiderEntity(ModEntityType.CRIMSON_SPIDER.get(), level);
            if (this.isPersistenceRequired()){
                spider.setPersistenceRequired();
            }
            spider.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0F);
            spider.finalizeSpawn(worldIn, difficultyIn, SpawnReason.JOCKEY, null, null);
            this.startRiding(spider);
            worldIn.addFreshEntity(spider);
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public void die(DamageSource pCause) {
        if (pCause.getEntity() != null && pCause.getEntity() instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) pCause.getEntity();
            livingEntity.addEffect(new EffectInstance(ModEffects.CURSED.get(), 300));
        }
        super.die(pCause);
    }

    public void aiStep() {
        if (!this.level.isClientSide && this.isAlive()) {
            this.healRaidersGoal.decrementCooldown();
            this.attackPlayersGoal.setCanAttack(this.healRaidersGoal.getCooldown() <= 0);

            if (this.isDrinkingPotion()) {
                if (this.usingTime-- <= 0) {
                    this.setUsingItem(false);
                    ItemStack itemstack = this.getMainHandItem();
                    this.setItemSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                    if (itemstack.getItem() == Items.POTION) {
                        List<EffectInstance> list = PotionUtils.getMobEffects(itemstack);
                        if (list != null) {
                            for(EffectInstance effectinstance : list) {
                                this.addEffect(new EffectInstance(effectinstance));
                            }
                        }
                    } else if (itemstack.getItem() == Items.MILK_BUCKET){
                        this.curePotionEffects(itemstack);
                    }

                    this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_DRINKING);
                }
            } else {
                Potion potion = null;
                if (this.getTarget() != null && !(this.getTarget() instanceof AbstractRaiderEntity) && !this.hasEffect(Effects.REGENERATION)){
                    potion = Potions.REGENERATION;
                } else if (this.random.nextFloat() < 0.15F && this.isEyeInFluid(FluidTags.WATER) && !this.hasEffect(Effects.WATER_BREATHING)) {
                    potion = Potions.WATER_BREATHING;
                } else if (this.random.nextFloat() < 0.15F && (this.isOnFire() || this.getLastDamageSource() != null && this.getLastDamageSource().isFire()) && !this.hasEffect(Effects.FIRE_RESISTANCE)) {
                    potion = Potions.FIRE_RESISTANCE;
                } else if (this.random.nextFloat() < 0.05F && this.getHealth() < this.getMaxHealth()) {
                    potion = Potions.HEALING;
                } else if (this.random.nextFloat() < 0.5F && this.getTarget() != null && !(this.getTarget() instanceof AbstractRaiderEntity) && !this.hasEffect(Effects.INVISIBILITY)) {
                    potion = Potions.INVISIBILITY;
                }

                if (potion != null) {
                    this.setItemSlot(EquipmentSlotType.MAINHAND, PotionUtils.setPotion(new ItemStack(Items.POTION), potion));
                    this.usingTime = this.getMainHandItem().getUseDuration();
                    this.setUsingItem(true);
                    if (!this.isSilent()) {
                        this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_DRINK, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
                    }

                    ModifiableAttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
                    modifiableattributeinstance.removeModifier(SPEED_MODIFIER_DRINKING);
                    modifiableattributeinstance.addTransientModifier(SPEED_MODIFIER_DRINKING);
                } else {
                    if (this.random.nextFloat() < 0.5F){
                        for (EffectInstance effectInstance : this.getActiveEffects()){
                            if (!effectInstance.getEffect().isBeneficial()){
                                this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.MILK_BUCKET));
                                this.usingTime = this.getMainHandItem().getUseDuration();
                                this.setUsingItem(true);
                                if (!this.isSilent()) {
                                    this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.WANDERING_TRADER_DRINK_MILK, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
                                }

                                ModifiableAttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
                                modifiableattributeinstance.removeModifier(SPEED_MODIFIER_DRINKING);
                                modifiableattributeinstance.addTransientModifier(SPEED_MODIFIER_DRINKING);
                            }
                        }
                    }
                }
            }

            if (this.random.nextFloat() < 7.5E-4F) {
                this.level.broadcastEntityEvent(this, (byte)15);
            }
        }

        super.aiStep();
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.WITCH_CELEBRATE;
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte pId) {
        if (pId == 15) {
            for(int i = 0; i < this.random.nextInt(35) + 10; ++i) {
                this.level.addParticle(ParticleTypes.WITCH, this.getX() + this.random.nextGaussian() * (double)0.13F, this.getBoundingBox().maxY + 0.5D + this.random.nextGaussian() * (double)0.13F, this.getZ() + this.random.nextGaussian() * (double)0.13F, 0.0D, 0.0D, 0.0D);
            }
        } else {
            super.handleEntityEvent(pId);
        }

    }

    protected float getDamageAfterMagicAbsorb(DamageSource pSource, float pDamage) {
        pDamage = super.getDamageAfterMagicAbsorb(pSource, pDamage);
        if (pSource.getEntity() == this) {
            pDamage = 0.0F;
        }

        if (pSource.isMagic()) {
            pDamage = (float)((double)pDamage * 0.15D);
        }

        return pDamage;
    }

    public void performRangedAttack(LivingEntity pTarget, float pDistanceFactor) {
        if (!this.isDrinkingPotion()) {
            Vector3d vector3d = pTarget.getDeltaMovement();
            double d0 = pTarget.getX() + vector3d.x - this.getX();
            double d1 = pTarget.getEyeY() - (double)1.1F - this.getY();
            double d2 = pTarget.getZ() + vector3d.z - this.getZ();
            float f = MathHelper.sqrt(d0 * d0 + d2 * d2);
            if (!(pTarget instanceof AbstractRaiderEntity) && !pTarget.isOnFire() && !pTarget.fireImmune() && this.random.nextFloat() <= 0.05F){
                BurningPotionEntity potionentity = new BurningPotionEntity(this.level, this);
                potionentity.xRot -= -20.0F;
                potionentity.shoot(d0, d1 + (double)(f * 0.2F), d2, 0.75F, 8.0F);
                if (!this.isSilent()) {
                    this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_THROW, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
                }

                this.level.addFreshEntity(potionentity);
            } else {
                Potion potion = Potions.HARMING;
                if (pTarget instanceof AbstractRaiderEntity) {
                    if (pTarget.isOnFire()){
                        potion = Potions.FIRE_RESISTANCE;
                    } else if (pTarget.getHealth() <= pTarget.getMaxHealth()/2) {
                        if (!pTarget.isInvertedHealAndHarm()){
                            potion = Potions.HEALING;
                        }
                    } else {
                        if (!(pTarget.getMainHandItem().getItem() instanceof ShootableItem)){
                            if (!pTarget.hasEffect(Effects.DAMAGE_BOOST)){
                                potion = Potions.STRENGTH;
                            } else if (!pTarget.hasEffect(Effects.MOVEMENT_SPEED)){
                                potion = Potions.SWIFTNESS;
                            } else if (!pTarget.hasEffect(Effects.REGENERATION) && !pTarget.isInvertedHealAndHarm()){
                                potion = Potions.REGENERATION;
                            } else {
                                if (!pTarget.isInvertedHealAndHarm()) {
                                    potion = Potions.HEALING;
                                }
                            }
                        } else if (!pTarget.hasEffect(Effects.WEAKNESS)){
                            potion = Potions.WEAKNESS;
                        } else if (!pTarget.hasEffect(Effects.REGENERATION) && !pTarget.isInvertedHealAndHarm()){
                            potion = Potions.REGENERATION;
                        } else {
                            if (!pTarget.isInvertedHealAndHarm()) {
                                potion = Potions.HEALING;
                            }
                        }
                    }

                    this.setTarget((LivingEntity)null);
                } else if (pTarget.getMobType() == CreatureAttribute.UNDEAD){
                    potion = Potions.HEALING;
                } else if (f >= 8.0F && !pTarget.hasEffect(Effects.MOVEMENT_SLOWDOWN)) {
                    potion = Potions.SLOWNESS;
                } else if (pTarget.getHealth() >= 8.0F && !pTarget.hasEffect(Effects.POISON) && pTarget.getMobType() != CreatureAttribute.UNDEAD
                        && !(pTarget instanceof PlayerEntity && LichdomHelper.isLich((PlayerEntity) pTarget))) {
                    potion = Potions.POISON;
                } else if (f <= 3.0F && !pTarget.hasEffect(Effects.WEAKNESS) && this.random.nextFloat() < 0.25F) {
                    potion = Potions.WEAKNESS;
                }

                PotionEntity potionentity = new PotionEntity(this.level, this);
                potionentity.setItem(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), potion));
                potionentity.xRot -= -20.0F;
                potionentity.shoot(d0, d1 + (double)(f * 0.2F), d2, 0.75F, 8.0F);
                if (!this.isSilent()) {
                    this.level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_THROW, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
                }

                this.level.addFreshEntity(potionentity);
            }
        }
    }

    protected float getStandingEyeHeight(Pose pPose, EntitySize pSize) {
        return 1.62F;
    }

    public void applyRaidBuffs(int pWave, boolean p_213660_2_) {
    }

    static class PotionAttackGoal extends Goal{
        private final BeldamEntity beldam;
        private LivingEntity target;
        private int seeTime;
        private int attackTime = -1;
        private final float attackRadius = 10.0F;
        private final float attackRadiusSqr = attackRadius * attackRadius;
        private boolean strafingClockwise;
        private boolean strafingBackwards;
        private int strafingTime = -1;

        public PotionAttackGoal(BeldamEntity p_i1650_1_) {
            this.beldam = p_i1650_1_;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.beldam.getTarget();
            if (livingentity != null && livingentity.isAlive()) {
                this.target = livingentity;
                return true;
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return this.canUse() || !this.beldam.getNavigation().isDone();
        }

        public void stop() {
            this.target = null;
            this.seeTime = 0;
            this.attackTime = -1;
        }

        public void tick() {
            double d0 = this.beldam.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
            boolean flag = this.beldam.getSensing().canSee(this.target);
            if (flag) {
                ++this.seeTime;
            } else {
                this.seeTime = 0;
            }

            if (!(this.target instanceof AbstractRaiderEntity)) {
                if ((d0 > (double) this.attackRadiusSqr * 2.0F) && this.seeTime < 20) {
                    this.beldam.getNavigation().moveTo(this.target, 1.0F);
                    this.strafingTime = -1;
                } else {
                    this.beldam.getNavigation().stop();
                    ++this.strafingTime;
                }

                if (this.strafingTime >= 20) {
                    if ((double) this.beldam.getRandom().nextFloat() < 0.3D) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    this.strafingTime = 0;
                }

                if (this.strafingTime > -1) {
                    this.strafingBackwards = d0 < (double) (this.attackRadiusSqr * 1.25F);

                    float forward = 0.75F;
                    float clockWise = 0.5F;
                    this.beldam.getMoveControl().strafe(this.strafingBackwards ? -forward : 0.25F, this.strafingClockwise ? clockWise : -clockWise);
                    this.beldam.lookAt(this.target, 30.0F, 30.0F);
                } else {
                    this.beldam.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
                }
            } else {
                if (d0 > (double)this.attackRadiusSqr && this.seeTime < 5) {
                    this.beldam.getNavigation().moveTo(this.target, 1.0F);
                } else {
                    this.beldam.getNavigation().stop();
                }

                this.beldam.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
            }

            float f = MathHelper.sqrt(d0) / this.attackRadius;
            if (--this.attackTime == 0) {
                if (!flag) {
                    return;
                }

                float distanceFactor = MathHelper.clamp(f, 0.1F, 1.0F);
                this.beldam.performRangedAttack(this.target, distanceFactor);
                this.attackTime = MathHelper.floor(f * 0.0F + 60.0F);
            } else if (this.attackTime < 0) {
                this.attackTime = MathHelper.floor(f * 0.0F + 60.0F);
            }
        }

    }

    static class TargetExpiringGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T>{
        private int cooldown = 0;

        public TargetExpiringGoal(AbstractRaiderEntity p_i50311_1_, Class<T> p_i50311_2_, boolean p_i50311_3_, @Nullable Predicate<LivingEntity> p_i50311_4_) {
            super(p_i50311_1_, p_i50311_2_, 500, p_i50311_3_, false, p_i50311_4_);
            this.targetConditions = (new EntityPredicate()).allowSameTeam().range(this.getFollowDistance()).selector(p_i50311_4_);
        }

        public int getCooldown() {
            return this.cooldown;
        }

        public void decrementCooldown() {
            --this.cooldown;
        }

        public boolean canUse() {
            if (this.cooldown <= 0 && this.mob.getRandom().nextBoolean()) {
                this.findTarget();
                return this.target != null;
            } else {
                return false;
            }
        }

        public void start() {
            this.cooldown = 100;
            super.start();
        }
    }

}
