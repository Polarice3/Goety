package com.Polarice3.Goety.common.entities.bosses;

import com.Polarice3.Goety.AttributesConfig;
import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.hostile.cultists.*;
import com.Polarice3.Goety.common.entities.neutral.IOwned;
import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.common.entities.neutral.ZPiglinBruteMinionEntity;
import com.Polarice3.Goety.common.entities.neutral.ZPiglinMinionEntity;
import com.Polarice3.Goety.common.entities.projectiles.*;
import com.Polarice3.Goety.common.entities.utilities.*;
import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.network.ModServerBossInfo;
import com.Polarice3.Goety.common.network.packets.server.SApostleSmitePacket;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.ModLootTables;
import com.Polarice3.Goety.utils.ModMathHelper;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

public class ApostleEntity extends SpellcastingCultistEntity implements IRangedAttackMob {
    private int f;
    private int hitTimes;
    private int coolDown;
    private int tornadoCoolDown;
    private int monolithCoolDown;
    private int spellCycle;
    private int titleNumber;
    private final Predicate<Entity> ALIVE = Entity::isAlive;
    private boolean roarParticles;
    private boolean fireArrows;
    private boolean regen;
    private Effect arrowEffect;
    private static final UUID SPEED_MODIFIER_CASTING_UUID = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
    private static final AttributeModifier SPEED_MODIFIER_CASTING = new AttributeModifier(SPEED_MODIFIER_CASTING_UUID, "Casting speed penalty", -1.0D, AttributeModifier.Operation.ADDITION);
    protected static final DataParameter<Byte> BOSS_FLAGS = EntityDataManager.defineId(ApostleEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Float> SPIN = EntityDataManager.defineId(ApostleEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> HAT = EntityDataManager.defineId(ApostleEntity.class, DataSerializers.BOOLEAN);
    private final ModServerBossInfo bossInfo = new ModServerBossInfo(this.getUUID(), this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS).setDarkenScreen(true).setCreateWorldFog(true);
    public final Predicate<LivingEntity> ZOMBIE_MINIONS = (livingEntity) -> {
        return livingEntity instanceof ZombieVillagerMinionEntity || livingEntity instanceof ZPiglinMinionEntity;
    };
    private final Predicate<LivingEntity> MONOLITHS = (livingEntity) -> {
        return livingEntity instanceof ObsidianMonolithEntity;
    };
    public final Predicate<LivingEntity> SKELETON_MINIONS = (livingEntity) -> {
        return livingEntity instanceof SkeletonVillagerMinionEntity || livingEntity instanceof MalghastEntity;
    };
    public int antiRegen;
    public int antiRegenTotal;
    public int deathTime = 0;
    public DamageSource deathBlow = DamageSource.GENERIC;

    public ApostleEntity(EntityType<? extends SpellcastingCultistEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.maxUpStep = 1.0F;
        this.xpReward = 1000;
        this.f = 0;
        this.coolDown = 100;
        this.spellCycle = 0;
        this.hitTimes = 0;
        this.antiRegen = 0;
        if (this.level.isClientSide){
            Goety.PROXY.addBoss(this);
        }
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SecondPhaseIndicator());
        this.goalSelector.addGoal(2, new BowAttackGoal<>(this));
        this.goalSelector.addGoal(2, new FasterBowAttackGoal<>(this));
        this.goalSelector.addGoal(2, new FastestBowAttackGoal<>(this));
        this.goalSelector.addGoal(3, new CastingSpellGoal());
        this.goalSelector.addGoal(3, new FireballSpellGoal());
        this.goalSelector.addGoal(3, new ZombieSpellGoal());
        this.goalSelector.addGoal(3, new FireRainSpellGoal());
        this.goalSelector.addGoal(3, new SkeletonSpellGoal());
        this.goalSelector.addGoal(3, new FireTornadoSpellGoal());
        this.goalSelector.addGoal(3, new RoarSpellGoal());
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, AttributesConfig.ApostleHealth.get())
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ARMOR, 12.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 6.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BOSS_FLAGS, (byte)0);
        this.entityData.define(SPIN, 0.0F);
        this.entityData.define(HAT, true);
    }

    private boolean getBossFlag(int mask) {
        int i = this.entityData.get(BOSS_FLAGS);
        return (i & mask) != 0;
    }

    private void setBossFlag(int mask, boolean value) {
        int i = this.entityData.get(BOSS_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(BOSS_FLAGS, (byte)(i & 255));
    }

    public int getAmbientSoundInterval() {
        return 200;
    }

    protected SoundEvent getAmbientSound() {
        if (this.getTarget() != null || this.isAggressive()) {
            return ModSounds.APOSTLE_AMBIENT.get();
        } else {
            return null;
        }
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.APOSTLE_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.APOSTLE_PREDEATH.get();
    }

    protected SoundEvent getTrueDeathSound() {
        return ModSounds.APOSTLE_DEATH.get();
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    public boolean canBeAffected(EffectInstance pPotioneffect) {
        return pPotioneffect.getEffect() != ModEffects.BURN_HEX.get() && pPotioneffect.getEffect() != Effects.WITHER && super.canBeAffected(pPotioneffect);
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("firing", this.f);
        pCompound.putInt("coolDown", this.coolDown);
        pCompound.putInt("tornadoCoolDown", this.tornadoCoolDown);
        pCompound.putInt("monolithCoolDown", this.monolithCoolDown);
        pCompound.putInt("spellCycle", this.spellCycle);
        pCompound.putInt("hitTimes", this.hitTimes);
        pCompound.putInt("antiRegen", this.antiRegen);
        pCompound.putInt("antiRegenTotal", this.antiRegenTotal);
        pCompound.putInt("titleNumber", this.titleNumber);
        pCompound.putBoolean("fireArrows", this.fireArrows);
        pCompound.putBoolean("secondPhase", this.isSecondPhase());
        pCompound.putBoolean("settingSecondPhase", this.isSettingUpSecond());
        pCompound.putBoolean("regen", this.regen);
        pCompound.putBoolean("hat", this.hasHat());
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.f = pCompound.getInt("firing");
        this.coolDown = pCompound.getInt("coolDown");
        this.tornadoCoolDown = pCompound.getInt("tornadoCoolDown");
        this.monolithCoolDown = pCompound.getInt("monolithCoolDown");
        this.spellCycle = pCompound.getInt("spellCycle");
        this.hitTimes = pCompound.getInt("hitTimes");
        this.antiRegen = pCompound.getInt("antiRegen");
        this.antiRegenTotal = pCompound.getInt("antiRegenTotal");
        this.titleNumber = pCompound.getInt("titleNumber");
        this.fireArrows = pCompound.getBoolean("fireArrows");
        this.regen = pCompound.getBoolean("regen");
        this.setHat(pCompound.getBoolean("hat"));
        this.setTitleNumber(this.titleNumber);
        this.TitleEffect(this.titleNumber);
        this.setSecondPhase(pCompound.getBoolean("secondPhase"));
        this.setSettingUpSecond(pCompound.getBoolean("settingSecondPhase"));
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

    protected boolean isAffectedByFluids() {
        return false;
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (entityIn instanceof WitherEntity) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else return super.isAlliedTo(entityIn);
    }

    public void die(DamageSource cause) {
        if (this.deathTime > 0) {
            super.die(cause);
        }
    }

    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 1){
            this.antiRegen = 0;
            this.antiRegenTotal = 0;
            for (AbstractTrapEntity trapEntity : this.level.getEntitiesOfClass(AbstractTrapEntity.class, this.getBoundingBox().inflate(64))) {
                if (trapEntity.getOwner() == this) {
                    trapEntity.remove();
                }
            }
            for (FireTornadoEntity fireTornadoEntity : this.level.getEntitiesOfClass(FireTornadoEntity.class, this.getBoundingBox().inflate(64))) {
                fireTornadoEntity.discard();
            }
            this.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
        }
        if (MainConfig.FancierApostleDeath.get() || this.level.dimension() == World.NETHER) {
            this.setNoGravity(true);
            if (this.deathTime < 180) {
                if (this.deathTime > 20) {
                    this.move(MoverType.SELF, new Vector3d(0.0D, 0.1D, 0.0D));
                }
                this.level.explode(this, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), 0.0F, Explosion.Mode.NONE);
            } else if (this.deathTime != 200) {
                this.move(MoverType.SELF, new Vector3d(0.0D, 0.0D, 0.0D));
            }
            if (this.deathTime >= 200) {
                this.move(MoverType.SELF, new Vector3d(0.0D, -4.0D, 0.0D));
                if (this.isOnGround() || this.getY() <= 0) {
                    if (!this.level.isClientSide) {
                        ServerWorld serverWorld = (ServerWorld) this.level;
                        if (serverWorld.getLevelData().isThundering()) {
                            serverWorld.setWeatherParameters(6000, 0, false, false);
                        }
                        for (int k = 0; k < 200; ++k) {
                            float f2 = random.nextFloat() * 4.0F;
                            float f1 = random.nextFloat() * ((float) Math.PI * 2F);
                            double d1 = MathHelper.cos(f1) * f2;
                            double d2 = 0.01D + random.nextDouble() * 0.5D;
                            double d3 = MathHelper.sin(f1) * f2;
                            serverWorld.sendParticles(ParticleTypes.LARGE_SMOKE, this.getX() + d1 * 0.1D, this.getY() + 0.3D, this.getZ() + d3 * 0.1D, 0, d1, d2, d3, 0.5F);
                            serverWorld.sendParticles(ParticleTypes.FLAME, this.getX() + d1 * 0.1D, this.getY() + 0.3D, this.getZ() + d3 * 0.1D, 0, d1, d2, d3, 0.5F);
                        }
                        serverWorld.sendParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 0, 1.0F, 0.0F, 0.0F, 0.5F);
                    }
                    this.playSound(SoundEvents.GENERIC_EXPLODE, 4.0F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F);
                    this.playSound(this.getTrueDeathSound(), 5.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                    this.die(this.deathBlow);
                    this.remove();
                }
            }
        } else {
            this.move(MoverType.SELF, new Vector3d(0.0D, 0.0D, 0.0D));
            if (this.deathTime == 1){
                if (!this.level.isClientSide) {
                    ServerWorld serverWorld = (ServerWorld) this.level;
                    if (serverWorld.getLevelData().isThundering()) {
                        serverWorld.setWeatherParameters(6000, 0, false, false);
                    }
                    for (int k = 0; k < 200; ++k) {
                        float f2 = random.nextFloat() * 4.0F;
                        float f1 = random.nextFloat() * ((float) Math.PI * 2F);
                        double d1 = MathHelper.cos(f1) * f2;
                        double d2 = 0.01D + random.nextDouble() * 0.5D;
                        double d3 = MathHelper.sin(f1) * f2;
                        serverWorld.sendParticles(ParticleTypes.LARGE_SMOKE, this.getX() + d1 * 0.1D, this.getY() + 0.3D, this.getZ() + d3 * 0.1D, 0, d1, d2, d3, 0.5F);
                        serverWorld.sendParticles(ParticleTypes.FLAME, this.getX() + d1 * 0.1D, this.getY() + 0.3D, this.getZ() + d3 * 0.1D, 0, d1, d2, d3, 0.5F);
                    }
                    for (int l = 0; l < 16; ++l){
                        serverWorld.sendParticles(ParticleTypes.FLAME, this.getRandomX(1.0F), this.getRandomY() - 0.25F, this.getRandomZ(1.0F), 1, 0, 0, 0, 0);
                    }
                }
                this.die(this.deathBlow);
                this.playSound(this.getTrueDeathSound(), 5.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }
            if (this.deathTime >= 30) {
                this.remove();
            }
        }

    }

    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public void remove() {
        if (this.level.isClientSide) {
            Goety.PROXY.removeBoss(this);
        } else {
            ServerWorld ServerLevel = (ServerWorld) this.level;
            if (ServerLevel.getLevelData().isThundering()) {
                ServerLevel.setWeatherParameters(6000, 0, false, false);
            }
        }
        super.remove();
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        if (this.level.dimension() == World.NETHER){
            return ModLootTables.APOSTLE_HARD;
        } else {
            return super.getDefaultLootTable();
        }
    }

    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
        ItemEntity itementity = this.spawnAtLocation(ModItems.UNHOLY_BLOOD.get());
        if (itementity != null) {
            itementity.setExtendedLifetime();
        }

    }

    protected float getDamageAfterMagicAbsorb(DamageSource source, float damage) {
        damage = super.getDamageAfterMagicAbsorb(source, damage);
        if (source.getEntity() == this) {
            damage = 0.0F;
        }

        if (source.getDirectEntity() instanceof SoulSkullEntity || source.getDirectEntity() instanceof FireballEntity) {
            damage = (float)((double)damage * 0.15D);
        }

        if (this.level.getDifficulty() == Difficulty.HARD){
            if (source.isMagic()){
                damage = (float)((double)damage * 0.15D);
            }
        }

        return damage;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        this.setHat(true);
        if (!this.hasCustomName()){
            int random = this.random.nextInt(18);
            int random2 = this.random.nextInt(12);
            this.setTitleNumber(random2);
            this.TitleEffect(random2);
            TranslationTextComponent component = new TranslationTextComponent("name.goety.apostle." + random);
            TranslationTextComponent component1 = new TranslationTextComponent("title.goety." + random2);
            if (random2 == 1){
                this.setHealth(this.getMaxHealth());
            }
            this.setCustomName(new TranslationTextComponent(component.getString() + " " + "the" + " " + component1.getString()));
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    public void setTitleNumber(Integer integer){
        this.titleNumber = integer;
    }

    public void TitleEffect(Integer integer){
        switch (integer){
            case 0:
                this.setRegen(true);
                break;
            case 1:
                this.setArrowEffect(Effects.HARM);
                break;
            case 2:
                this.setArrowEffect(Effects.POISON);
                break;
            case 3:
                this.setArrowEffect(Effects.WITHER);
                break;
            case 4:
                this.setArrowEffect(Effects.BLINDNESS);
                break;
            case 5:
                this.setArrowEffect(Effects.WEAKNESS);
                break;
            case 6:
                this.setFireArrow(true);
                this.setArrowEffect(ModEffects.BURN_HEX.get());
                break;
            case 7:
                this.setArrowEffect(Effects.HUNGER);
                break;
            case 8:
                this.setArrowEffect(Effects.MOVEMENT_SLOWDOWN);
                break;
            case 9:
                this.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, Integer.MAX_VALUE, 1, false, false));
                break;
            case 10:
                this.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, false, false));
                break;
            case 11:
                this.setArrowEffect(ModEffects.SAPPED.get());
                break;
        }
    }

    public boolean addEffect(EffectInstance p_182397_) {
        return p_182397_.getEffect().isBeneficial();
    }

    public void setRegen(boolean regen){
        this.regen = regen;
    }

    public boolean Regen(){
        return this.regen;
    }

    public void setFireArrow(boolean fireArrow){
        this.fireArrows = fireArrow;
    }

    public boolean getFireArrow(){
        return this.fireArrows;
    }

    public void setArrowEffect(Effect effect){
        this.arrowEffect = effect;
    }

    public Effect getArrowEffect(){
        return this.arrowEffect;
    }

    public void setSecondPhase(boolean secondPhase){
        this.setBossFlag(1, secondPhase);
    }

    public boolean isSecondPhase(){
        return this.getBossFlag(1);
    }

    public void setSettingUpSecond(boolean settingupSecond){
        this.setBossFlag(2, settingupSecond);
    }

    public boolean isSettingUpSecond(){
        return this.getBossFlag(2);
    }

    public void setCasting(boolean casting){
        this.setBossFlag(4, casting);
    }

    public boolean isCasting(){
        return this.getBossFlag(4);
    }

    public void setSpin(float spin){
        this.entityData.set(SPIN, spin);
    }

    public float getSpin(){
        return this.entityData.get(SPIN);
    }

    public void setHat(boolean hat){
        this.entityData.set(HAT, hat);
    }

    public boolean hasHat(){
        return this.entityData.get(HAT);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
    }

    @OnlyIn(Dist.CLIENT)
    public ArmPose getArmPose() {
        if (this.isDeadOrDying()){
            return ArmPose.DYING;
        } else if (this.getMainHandItem().getItem() instanceof BowItem) {
            if (this.isAggressive() && !this.isSpellcasting() && !this.isSettingUpSecond()){
                return ArmPose.BOW_AND_ARROW;
            } else if (this.isSpellcasting()){
                return ArmPose.SPELL_AND_WEAPON;
            } else if (this.isSettingUpSecond()){
                return ArmPose.SPELL_AND_WEAPON;
            } else {
                return ArmPose.CROSSED;
            }
        } else {
            return ArmPose.CROSSED;
        }
    }

    public boolean isFiring(){
        return this.roarParticles;
    }

    public void setFiring(boolean firing){
        this.roarParticles = firing;
    }

    public int hitTimeTeleport(){
        return this.isSecondPhase() ? 2 : 4;
    }

    public void resetHitTime(){
        this.hitTimes = 0;
    }

    public void increaseHitTime(){
        ++this.hitTimes;
    }

    public int getHitTimes(){
        return this.hitTimes;
    }
    public void resetCoolDown(){
        this.setCoolDown(0);
    }

    public void setCoolDown(int coolDown){
        this.coolDown = coolDown;
    }

    public int getCoolDown(){
        return this.coolDown;
    }

    public void setTornadoCoolDown(int coolDown){
        this.tornadoCoolDown = coolDown;
    }

    public int getTornadoCoolDown(){
        return this.tornadoCoolDown;
    }

    public void setMonolithCoolDown(int coolDown){
        this.monolithCoolDown = coolDown;
    }

    public int getMonolithCoolDown(){
        return this.monolithCoolDown;
    }

    public int getAntiRegen(){
        return this.antiRegen;
    }

    public int getAntiRegenTotal(){
        return this.antiRegenTotal;
    }

    public boolean isSmited(){
        return this.antiRegen > 0;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        LivingEntity livingEntity = this.getTarget();
        if (!this.level.isClientSide) {
            if (livingEntity != null && pSource.getEntity() instanceof LivingEntity) {
                this.increaseHitTime();
            }
        }

        if (pSource.getDirectEntity() instanceof LivingEntity){
            LivingEntity living = (LivingEntity) pSource.getDirectEntity();
            int smite = EnchantmentHelper.getEnchantmentLevel(Enchantments.SMITE, living);
            if (smite > 0){
                int smite2 = MathHelper.clamp(smite, 1, 5);
                int duration = ModMathHelper.secondsToTicks(smite2);
                if (this.Regen()){
                    duration /= 2;
                }
                this.antiRegenTotal = duration;
                this.antiRegen = duration;
                if (this.level instanceof ServerWorld){
                    ModNetwork.INSTANCE.send(PacketDistributor.ALL.noArg(), new SApostleSmitePacket(this.getUUID(), duration));
                    if (this.coolDown < this.coolDownLimit()){
                        this.coolDown += 10;
                    }
                }
            }
        }

        if (pSource == DamageSource.LIGHTNING_BOLT || pSource == DamageSource.FALL || pSource == DamageSource.IN_WALL){
            return false;
        }

        if (pSource.getDirectEntity() instanceof AbstractArrowEntity && ((AbstractArrowEntity) pSource.getDirectEntity()).getOwner() == this){
            return false;
        }

        if (this.isSettingUpSecond()){
            return false;
        }

        if (pSource.getDirectEntity() instanceof NetherMeteorEntity || pSource.getEntity() instanceof NetherMeteorEntity){
            return false;
        }

        float trueAmount = this.level.dimension() == World.NETHER ? pAmount/4 : pAmount;

        if (pSource == DamageSource.OUT_OF_WORLD){
            this.remove();
        }

        if (this.hitTimes >= this.hitTimeTeleport()){
            trueAmount = trueAmount/2;
            this.teleport();
        }

        if (!this.level.getNearbyPlayers(new EntityPredicate().selector(MobUtil.NO_CREATIVE_OR_SPECTATOR), this, this.getBoundingBox().inflate(32)).isEmpty()){
            if (!(pSource.getEntity() instanceof PlayerEntity)){
                trueAmount = trueAmount/2;
            }
        }
        if (this.isDeadOrDying()) {
            this.deathBlow = pSource;
        }

        return super.hurt(pSource, Math.min(trueAmount, (float) AttributesConfig.ApostleDamageCap.get()));
    }

    @Override
    public void kill() {
        this.remove();
    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    protected void teleport() {
        if (!this.level.isClientSide() && this.isAlive() && !this.isSettingUpSecond() && !this.isCasting()) {
            for(int i = 0; i < 128; ++i) {
                double d3 = this.getX() + (this.getRandom().nextDouble() - 0.5D) * 32.0D;
                double d4 = this.getY();
                if (this.getTarget() != null){
                    d4 = this.getTarget().getY();
                }
                double d5 = this.getZ() + (this.getRandom().nextDouble() - 0.5D) * 32.0D;
                if (this.randomTeleport(d3, d4, d5, false)) {
                    this.teleportHits();
                    this.resetHitTime();
                    break;
                }
            }
        }
    }

    private void teleportTowards(Entity entity) {
        if (!this.level.isClientSide() && this.isAlive() && !this.isSettingUpSecond()) {
            for(int i = 0; i < 128; ++i) {
                Vector3d vector3d = new Vector3d(this.getX() - entity.getX(), this.getY(0.5D) - entity.getEyeY(), this.getZ() - entity.getZ());
                vector3d = vector3d.normalize();
                double d0 = 16.0D;
                double d1 = this.getX() + (this.random.nextDouble() - 0.5D) * 8.0D - vector3d.x * d0;
                double d2 = this.getY() + (double)(this.random.nextInt(16) - 8) - vector3d.y * d0;
                double d3 = this.getZ() + (this.random.nextDouble() - 0.5D) * 8.0D - vector3d.z * d0;
                if (this.randomTeleport(d1, d2, d3, false)) {
                    this.teleportHits();
                    break;
                }
            }
        }
    }

    public void teleportHits(){
        this.level.broadcastEntityEvent(this, (byte) 100);
        if (!this.isSilent()) {
            this.level.playSound((PlayerEntity)null, this.xo, this.yo, this.zo, ModSounds.APOSTLE_TELEPORT.get(), this.getSoundSource(), 1.0F, 1.0F);
            this.playSound(ModSounds.APOSTLE_TELEPORT.get(), 1.0F, 1.0F);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte pId) {
        if (pId == 100){
            int i = 128;

            for(int j = 0; j < i; ++j) {
                double d0 = (double)j / (i - 1);
                float f = (this.random.nextFloat() - 0.5F) * 0.2F;
                float f1 = (this.random.nextFloat() - 0.5F) * 0.2F;
                float f2 = (this.random.nextFloat() - 0.5F) * 0.2F;
                double d1 = MathHelper.lerp(d0, this.xo, this.getX()) + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth() * 2.0D;
                double d2 = MathHelper.lerp(d0, this.yo, this.getY()) + this.random.nextDouble() * (double)this.getBbHeight();
                double d3 = MathHelper.lerp(d0, this.zo, this.getZ()) + (this.random.nextDouble() - 0.5D) * (double)this.getBbWidth() * 2.0D;
                this.level.addParticle(ParticleTypes.SMOKE, d1, d2, d3, (double)f, (double)f1, (double)f2);
            }
        } else {
            super.handleEntityEvent(pId);
        }
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return ModSounds.APOSTLE_CAST_SPELL.get();
    }

    protected boolean canRide(Entity pEntity) {
        return false;
    }

    public void setSpellCycle(int spellCycle){
        this.spellCycle = spellCycle;
    }

    public int getSpellCycle(){
        return this.spellCycle;
    }

    public void aiStep() {
        super.aiStep();
        if (this.level.getDifficulty() == Difficulty.PEACEFUL){
            this.remove();
        }
        if (this.level.isClientSide){
            if (this.isAlive()) {
                if (this.getSpin() < 3.14F) {
                    this.setSpin(this.getSpin() + 0.01F);
                } else {
                    this.setSpin(-3.14F);
                }
            }
            if (this.isSecondPhase() && this.getHealth() < this.getMaxHealth()/2 && this.hasHat()){
                this.setHat(false);
            }
        }
        if (!this.level.isClientSide){
            ModifiableAttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if (attributeinstance != null) {
                if (attributeinstance.hasModifier(SPEED_MODIFIER_CASTING)){
                    attributeinstance.removeModifier(SPEED_MODIFIER_CASTING);
                }
                if (this.isCasting()) {
                    attributeinstance.addTransientModifier(SPEED_MODIFIER_CASTING);
                }
            }
        }
        if (this.tickCount % 100 == 0){
            MobUtil.secretConversion(this);
        }
        if (!this.level.isClientSide){
            ServerWorld serverWorld = (ServerWorld) this.level;
            if (MainConfig.WitchConversion.get()) {
                for (WitchEntity witch : this.level.getEntitiesOfClass(WitchEntity.class, this.getBoundingBox().inflate(8.0D))) {
                    BeldamEntity beldam = witch.convertTo(ModEntityType.BELDAM.get(), true);
                    if (beldam != null) {
                        beldam.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(witch.blockPosition()), SpawnReason.CONVERSION, null, null);
                        net.minecraftforge.event.ForgeEventFactory.onLivingConvert(witch, beldam);
                    }
                }
            }
        }
        if (this.isSettingUpSecond()){
            this.setFiring(false);
            this.f = 0;
            this.heal(1.0F);
            for (Entity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3.0D), ALIVE)) {
                if (!entity.isAlliedTo(this)) {
                    this.barrier(entity, this);
                }
            }

            if (!this.level.isClientSide){
                this.resetHitTime();
                ServerWorld serverWorld = (ServerWorld) this.level;
                for(int i = 0; i < 40; ++i) {
                    double d0 = this.random.nextGaussian() * 0.2D;
                    double d1 = this.random.nextGaussian() * 0.2D;
                    double d2 = this.random.nextGaussian() * 0.2D;
                    serverWorld.sendParticles(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0, d0, d1, d2, 0.5F);
                }
            }
            if (this.getHealth() >= this.getMaxHealth()){
                if (!this.level.isClientSide){
                    ServerWorld serverWorld = (ServerWorld) ApostleEntity.this.level;
                    if (!serverWorld.isThundering()) {
                        serverWorld.setWeatherParameters(0, 6000, true, true);
                    }
                    for(int k = 0; k < 60; ++k) {
                        float f2 = random.nextFloat() * 4.0F;
                        float f1 = random.nextFloat() * ((float)Math.PI * 2F);
                        double d1 = MathHelper.cos(f1) * f2;
                        double d2 = 0.01D + random.nextDouble() * 0.5D;
                        double d3 = MathHelper.sin(f1) * f2;
                        serverWorld.sendParticles(ParticleTypes.FLAME, this.getX() + d1 * 0.1D, this.getY() + 0.3D, this.getZ() + d3 * 0.1D, 0, d1, d2, d3, 0.25F);
                    }
                }
                this.setSettingUpSecond(false);
                this.setSecondPhase(true);
            }
        }
        LivingEntity target = this.getTarget();
        if (this.getMainHandItem().isEmpty() && this.isAlive()){
            this.setItemInHand(Hand.MAIN_HAND, new ItemStack(Items.BOW));
        }
        if (this.isSmited()){
            --this.antiRegen;
        }
        if (this.getTornadoCoolDown() > 0){
            --this.tornadoCoolDown;
        }
        if (this.getMonolithCoolDown() > 0){
            --this.monolithCoolDown;
        }
        if (this.isSecondPhase()) {
            if (!this.isSmited()) {
                if (this.Regen()) {
                    if (this.tickCount % 10 == 0) {
                        if (this.getHealth() < this.getMaxHealth()) {
                            this.heal(1.0F);
                        }
                    }
                } else {
                    if (this.tickCount % 20 == 0) {
                        if (this.getHealth() < this.getMaxHealth()) {
                            this.heal(1.0F);
                        }
                    }
                }
            }
            if (this.tickCount % 100 == 0 && !this.isDeadOrDying()) {
                if (!this.level.isClientSide){
                    ServerWorld serverWorld = (ServerWorld) this.level;
                    serverWorld.setWeatherParameters(0, 6000, true, true);
                }
            }
            if (!this.level.isClientSide) {
                if (this.getCoolDown() < this.coolDownLimit()) {
                    ++this.coolDown;
                } else {
                    this.setSpellCycle(1);
                }
            }
            if (!this.level.isClientSide) {
                if (this.getHealth() <= this.getMaxHealth()/8){
                    if (this.tickCount % 100 == 0){
                        this.teleport();
                    }
                }
            }
            if (this.level.dimension() != World.NETHER){
                if (this.tickCount % 20 == 0) {
                    BlockPos.Mutable blockPos = new BlockPos.Mutable(this.getRandomX(0.2), this.getY(), this.getRandomZ(0.2));

                    while (blockPos.getY() < this.getY() + 64.0D && !this.level.getBlockState(blockPos).isSolidRender(this.level, blockPos)) {
                        blockPos.move(Direction.UP);
                    }
                    if (blockPos.getY() > this.getY() + 32.0D) {
                        int range = this.getHealth() < this.getMaxHealth()/2 ? 450 : 900;
                        int trueRange = this.getHealth() < this.getHealth()/4 ? range/2 : range;
                        Random random = this.level.random;
                        double d = (random.nextBoolean() ? 1 : -1);
                        double e = (random.nextBoolean() ? 1 : -1);
                        double d2 = (random.nextInt(trueRange) * d);
                        double d3 = -900.0D;
                        double d4 = (random.nextInt(trueRange) * e);
                        NetherMeteorEntity fireball = new NetherMeteorEntity(this.level, this, d2, d3, d4);
                        fireball.setDangerous(ForgeEventFactory.getMobGriefingEvent(this.level, this) && MainConfig.ApocalypseMode.get());
                        fireball.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                        this.level.addFreshEntity(fireball);
                    }
                }
            }
        } else {
            if (!this.isSmited()) {
                if (this.Regen()) {
                    if (this.tickCount % 20 == 0) {
                        if (this.getHealth() < this.getMaxHealth()) {
                            this.heal(1.0F);
                        }
                    }
                } else {
                    if (this.tickCount % 40 == 0) {
                        if (this.getHealth() < this.getMaxHealth()) {
                            this.heal(1.0F);
                        }
                    }
                }
            }
            if (!this.level.isClientSide) {
                if (this.getCoolDown() < this.coolDownLimit()) {
                    ++this.coolDown;
                } else {
                    this.setSpellCycle(0);
                }
            }
        }
        if (!this.level.isClientSide) {
            if (this.getSpellCycle() < 2) {
                if (this.level.random.nextBoolean()) {
                    this.setSpellCycle(2);
                } else {
                    this.setSpellCycle(3);
                }
            }
        }
        for (LivingEntity living : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(32))){
            if (!(living instanceof AbstractCultistEntity) && !(living instanceof WitchEntity) && !(living instanceof OwnedEntity && ((OwnedEntity) living).getTrueOwner() == this)){
                if (living.isInWater()){
                    living.hurt(DamageSource.HOT_FLOOR, 1.0F);
                }
            }
            if (living instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) living;
                player.abilities.flying &= player.isCreative();
            }
        }
        if (target == null){
            this.resetHitTime();
            for (PlayerEntity player : this.level.getEntitiesOfClass(PlayerEntity.class, this.getBoundingBox().inflate(64), EntityPredicates.NO_CREATIVE_OR_SPECTATOR)){
                this.setTarget(player);
            }
            for (MobEntity mob : this.level.getEntitiesOfClass(MobEntity.class, this.getBoundingBox().inflate(this.getAttributeValue(Attributes.FOLLOW_RANGE)), EntityPredicates.LIVING_ENTITY_STILL_ALIVE)){
                if (mob.getTarget() == this){
                    this.setTarget(mob);
                }
            }
        } else {
            if (target instanceof IOwned && ((IOwned) target).getTrueOwner() != null){
                IOwned owned = (IOwned) target;
                this.setTarget(owned.getTrueOwner());
            }
            int i = ApostleEntity.this.level.getEntitiesOfClass(OwnedEntity.class, ApostleEntity.this.getBoundingBox().inflate(64.0D), ZOMBIE_MINIONS).size();
            if (this.tickCount % 100 == 0 && i < 16 && this.level.random.nextFloat() <= 0.25F && !this.isSettingUpSecond()){
                if (!this.level.isClientSide){
                    ServerWorld ServerLevel = (ServerWorld) this.level;
                    Random r = this.level.random;
                    int numbers = this.isSecondPhase() ? 4 : 2;
                    if (this.level.dimension() == World.NETHER) {
                        for (ZombifiedPiglinEntity zombifiedPiglin : this.level.getEntitiesOfClass(ZombifiedPiglinEntity.class, this.getBoundingBox().inflate(16))){
                            if (zombifiedPiglin.getTarget() != this.getTarget()){
                                zombifiedPiglin.setTarget(this.getTarget());
                            }
                        }
                        for (int p = 0; p < r.nextInt(numbers) + 1; ++p) {
                            int k = (12 + r.nextInt(12)) * (r.nextBoolean() ? -1 : 1);
                            int l = (12 + r.nextInt(12)) * (r.nextBoolean() ? -1 : 1);
                            BlockPos.Mutable blockpos$mutable = this.blockPosition().mutable().move(k, 0, l);
                            blockpos$mutable.setX(blockpos$mutable.getX() + r.nextInt(5) - r.nextInt(5));
                            blockpos$mutable.setY((int) BlockFinder.moveDownToGround(this));
                            blockpos$mutable.setZ(blockpos$mutable.getZ() + r.nextInt(5) - r.nextInt(5));
                            ZPiglinMinionEntity summonedentity = new ZPiglinMinionEntity(ModEntityType.ZPIGLIN_MINION.get(), this.level);
                            summonedentity.moveTo(blockpos$mutable, 0.0F, 0.0F);
                            summonedentity.setTrueOwner(this);
                            summonedentity.setLimitedLife(60 * (90 + this.level.random.nextInt(180)));
                            summonedentity.finalizeSpawn(ServerLevel, this.level.getCurrentDifficultyAt(blockpos$mutable), SpawnReason.MOB_SUMMONED, null, null);
                            summonedentity.setTarget(this.getTarget());
                            SummonCircleEntity summonCircle = new SummonCircleEntity(this.level, blockpos$mutable, summonedentity, false, true, this);
                            this.level.addFreshEntity(summonCircle);
                        }
                    }
                }
            }
            if (this.isSecondPhase()) {
                if (MobUtil.isInRain(target)) {
                    int count = 100 * (this.random.nextInt(5) + 1);
                    if (this.tickCount % count == 0) {
                        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(target.getX(), target.getY(), target.getZ());

                        while (blockpos$mutable.getY() > 0 && !ApostleEntity.this.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
                            blockpos$mutable.move(Direction.DOWN);
                        }

                        LightningTrapEntity lightningTrap = new LightningTrapEntity(this.level, blockpos$mutable.getX(), blockpos$mutable.getY() + 1, blockpos$mutable.getZ());
                        lightningTrap.setOwner(this);
                        lightningTrap.setDuration(50);
                        this.level.addFreshEntity(lightningTrap);
                    }
                }
            }
            if ((target.distanceToSqr(this) > 1024 || !this.getSensing().canSee(target)) && target.isOnGround() && !this.isSettingUpSecond()){
                this.teleportTowards(target);
            }
        }
        if (this.isFiring() && !this.isSettingUpSecond()) {
            ++this.f;
            if (this.f % 2 == 0 && this.f < 10) {
                for (Entity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D), ALIVE)) {
                    if (!(entity instanceof AbstractCultistEntity) && !(entity instanceof OwnedEntity && ((OwnedEntity) entity).getTrueOwner() == this)) {
                        entity.hurt(DamageSource.mobAttack(this), AttributesConfig.ApostleMagicDamage.get().floatValue());
                        this.launch(entity, this);
                    }
                }
                if (!this.level.isClientSide){
                    this.serverRoarParticles();
                }

            }
            if (this.f >= 10){
                if (this.teleportChance()) {
                    this.teleport();
                }
                this.setFiring(false);
                this.f = 0;
            }
        }
        if (this.isInWater() || this.isInLava() || this.isInWall()){
            this.teleport();
        }
        if (this.level.dimension() == World.NETHER){
            if (target != null){
                target.addEffect(new EffectInstance(ModEffects.BURN_HEX.get(), 100));
            }
        }
    }

    protected boolean isImmobile() {
        return super.isImmobile() || this.isSettingUpSecond();
    }

    public boolean canSee(Entity p_149755_) {
        return !this.isSettingUpSecond() && super.canSee(p_149755_);
    }

    private void serverRoarParticles(){
        ServerWorld serverWorld = (ServerWorld) this.level;
        serverWorld.sendParticles(ParticleTypes.FLASH, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0);
        Vector3d vector3d = this.getBoundingBox().getCenter();
        for(int i = 0; i < 40; ++i) {
            double d0 = this.random.nextGaussian() * 0.2D;
            double d1 = this.random.nextGaussian() * 0.2D;
            double d2 = this.random.nextGaussian() * 0.2D;
            serverWorld.sendParticles(ParticleTypes.POOF, vector3d.x, vector3d.y, vector3d.z, 0, d0, d1, d2, 0.5F);
        }
    }

    private void launch(Entity p_213688_1_, LivingEntity livingEntity) {
        double d0 = p_213688_1_.getX() - livingEntity.getX();
        double d1 = p_213688_1_.getZ() - livingEntity.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        MobUtil.push(p_213688_1_, d0 / d2 * 6.0D, 0.4D, d1 / d2 * 6.0D);
    }

    private void barrier(Entity p_213688_1_, LivingEntity livingEntity) {
        double d0 = p_213688_1_.getX() - livingEntity.getX();
        double d1 = p_213688_1_.getZ() - livingEntity.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        MobUtil.push(p_213688_1_, d0 / d2 * 2.0D, 0.1D, d1 / d2 * 2.0D);
    }

    public void performRangedAttack(LivingEntity pTarget, float pDistanceFactor) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, item -> item instanceof BowItem)));
        AbstractArrowEntity abstractarrowentity = this.getArrow(itemstack, pDistanceFactor * AttributesConfig.ApostleBowDamage.get());
        if (this.getMainHandItem().getItem() instanceof BowItem) {
            abstractarrowentity = ((BowItem) this.getMainHandItem().getItem()).customArrow(abstractarrowentity);
        }
        double d0 = pTarget.getX() - this.getX();
        double d1 = pTarget.getY(0.3333333333333333D) - abstractarrowentity.getY();
        double d2 = pTarget.getZ() - this.getZ();
        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        abstractarrowentity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(abstractarrowentity);
    }

    public AbstractArrowEntity getArrow(ItemStack pArrowStack, float pDistanceFactor) {
        AbstractArrowEntity abstractarrowentity = ProjectileHelper.getMobArrow(this, pArrowStack, pDistanceFactor);
        if (this.getArrowEffect() != null){
            Effect mobEffect = this.getArrowEffect();
            int amp;
            if (this.isSecondPhase() && this.getArrowEffect() != Effects.HARM){
                amp = 1;
            } else {
                amp = 0;
            }
            if (this.getTarget() != null && this.getArrowEffect() == Effects.HARM && this.getTarget().isInvertedHealAndHarm()){
                mobEffect = Effects.HEAL;
            }
            ((ArrowEntity)abstractarrowentity).addEffect(new EffectInstance(mobEffect, mobEffect.isInstantenous() ? 1 : 200, amp));
        }
        if (this.getFireArrow()){
            abstractarrowentity.setRemainingFireTicks(100);
        }
        return abstractarrowentity;
    }

    public boolean canFireProjectileWeapon(ShootableItem p_230280_1_) {
        return p_230280_1_ instanceof BowItem;
    }

    public ItemStack getProjectile(ItemStack shootable) {
        if (shootable.getItem() instanceof ShootableItem) {
            Predicate<ItemStack> predicate = ((ShootableItem)shootable.getItem()).getSupportedHeldProjectiles();
            ItemStack itemstack = ShootableItem.getHeldProjectile(this, predicate);
            return itemstack.isEmpty() ? new ItemStack(Items.ARROW) : itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public boolean teleportChance(){
        return this.level.random.nextFloat() <= 0.25F;
    }

    public int spellStart(){
        return this.isSecondPhase() && this.getHealth() <= this.getMaxHealth() /4 ? 12 : this.isSecondPhase() ? 20 : 40;
    }

    public int coolDownLimit(){
        return this.isSecondPhase() && this.getHealth() <= this.getMaxHealth() /4 ? 20 : this.isSecondPhase() ? 25 : 50;
    }

    public void postSpellCast(){
        if (this.teleportChance()) {
            this.teleport();
        }
        this.resetCoolDown();
        this.setSpellCycle(0);
    }

    class CastingSpellGoal extends CastingASpellGoal {
        private CastingSpellGoal() {
        }

        public void tick() {
            if (ApostleEntity.this.getTarget() != null) {
                ApostleEntity.this.getLookControl().setLookAt(ApostleEntity.this.getTarget(), (float) ApostleEntity.this.getMaxHeadYRot(), (float) ApostleEntity.this.getMaxHeadXRot());
            }
        }
    }

    abstract class CastingGoal extends UseSpellGoal {

        public void start() {
            super.start();
            ApostleEntity.this.setCasting(true);
        }

        public void stop() {
            super.stop();
            ApostleEntity.this.setCasting(false);
        }

        protected int getCastingInterval() {
            return 0;
        }

    }

    class FireballSpellGoal extends CastingGoal {
        private FireballSpellGoal() {
        }

        @Override
        public boolean canUse() {
            LivingEntity livingentity = ApostleEntity.this.getTarget();
            if (!super.canUse()) {
                return false;
            } else if (livingentity == null) {
                return false;
            } else {
                return ApostleEntity.this.getSpellCycle() == 0
                        && !ApostleEntity.this.isSettingUpSecond()
                        && !ApostleEntity.this.isSecondPhase()
                        && ApostleEntity.this.getSensing().canSee(livingentity);
            }
        }

        protected int getCastingTime() {
            return 20;
        }

        public void castSpell() {
            LivingEntity livingentity = ApostleEntity.this.getTarget();
            if (livingentity != null) {
                double d0 = ApostleEntity.this.distanceToSqr(livingentity);
                float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;
                double d1 = livingentity.getX() - ApostleEntity.this.getX();
                double d2 = livingentity.getY(0.5D) - ApostleEntity.this.getY(0.5D);
                double d3 = livingentity.getZ() - ApostleEntity.this.getZ();
                ExplosiveProjectileEntity fireballEntity;
                if (ApostleEntity.this.level.getDifficulty() == Difficulty.HARD){
                    fireballEntity = new GrandLavaballEntity(ApostleEntity.this.level, ApostleEntity.this, d1, d2, d3);
                } else {
                    fireballEntity = new LavaballEntity(ApostleEntity.this.level, ApostleEntity.this, d1, d2, d3);
                }
                fireballEntity.setPos(fireballEntity.getX(), ApostleEntity.this.getY(0.5), fireballEntity.getZ());
                fireballEntity.setDangerous(ForgeEventFactory.getMobGriefingEvent(ApostleEntity.this.level, ApostleEntity.this));
                fireballEntity.setPos(fireballEntity.getX(), ApostleEntity.this.getY(0.5), fireballEntity.getZ());
                ApostleEntity.this.level.addFreshEntity(fireballEntity);
                if (!ApostleEntity.this.isSilent()) {
                    ApostleEntity.this.level.levelEvent(null, 1016, ApostleEntity.this.blockPosition(), 0);
                }
                if (ApostleEntity.this.teleportChance()) {
                    ApostleEntity.this.teleport();
                }
                ApostleEntity.this.coolDown = 0;
                ++ApostleEntity.this.spellCycle;
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return ModSounds.APOSTLE_PREPARE_SPELL.get();
        }

        @Override
        protected SpellType getSpellType() {
            return SpellType.FIRE;
        }
    }

    class ZombieSpellGoal extends CastingGoal {
        private ZombieSpellGoal() {
        }

        @Override
        public boolean canUse() {
            int i = ApostleEntity.this.level.getEntitiesOfClass(OwnedEntity.class, ApostleEntity.this.getBoundingBox().inflate(64.0D), ZOMBIE_MINIONS).size();
            int j = ApostleEntity.this.level.getEntitiesOfClass(OwnedEntity.class, ApostleEntity.this.getBoundingBox().inflate(64.0D), MONOLITHS).size();
            if (!super.canUse()) {
                return false;
            }  else {
                int cool = ApostleEntity.this.spellStart();
                return ApostleEntity.this.getCoolDown() >= cool
                        && ApostleEntity.this.getSpellCycle() == 2
                        && ApostleEntity.this.getMonolithCoolDown() <= 0
                        && !ApostleEntity.this.isSettingUpSecond()
                        && i < 4 && j < 4;
            }
        }

        protected int getCastingTime() {
            return 20;
        }

        public void castSpell() {
            if (!ApostleEntity.this.level.isClientSide) {
                ServerWorld ServerLevel = (ServerWorld) ApostleEntity.this.level;
                LivingEntity livingentity = ApostleEntity.this.getTarget();
                Random r = ApostleEntity.this.random;
                if (livingentity != null) {
                    BlockPos blockpos = ApostleEntity.this.blockPosition();
                    if (!ApostleEntity.this.isSecondPhase()){
                        if (r.nextBoolean()) {
                            ZPiglinMinionEntity summonedentity = new ZPiglinMinionEntity(ModEntityType.ZPIGLIN_MINION.get(), ApostleEntity.this.level);
                            summonedentity.moveTo(blockpos, 0.0F, 0.0F);
                            summonedentity.setOwnerId(ApostleEntity.this.getUUID());
                            summonedentity.setLimitedLife(60 * (90 + ApostleEntity.this.level.random.nextInt(180)));
                            for(EquipmentSlotType equipmentslottype : EquipmentSlotType.values()) {
                                if (equipmentslottype.getType() == EquipmentSlotType.Group.ARMOR) {
                                    ItemStack itemstack = summonedentity.getItemBySlot(equipmentslottype);
                                    if (itemstack.isEmpty()) {
                                        Item item = getEquipmentForSlot(equipmentslottype, 1);
                                        if (item != null) {
                                            summonedentity.setItemSlot(equipmentslottype, new ItemStack(item));
                                        }
                                    }
                                }
                                summonedentity.setDropChance(equipmentslottype, 0.0F);
                            }
                            summonedentity.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, Integer.MAX_VALUE, 1, false, false));
                            summonedentity.finalizeSpawn(ServerLevel, ApostleEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, null, null);
                            summonedentity.setTarget(livingentity);
                            SummonCircleEntity summonCircle = new SummonCircleEntity(ApostleEntity.this.level, blockpos, summonedentity, false, true, ApostleEntity.this);
                            ApostleEntity.this.level.addFreshEntity(summonCircle);
                        } else {
                            if (ServerLevel.dimension() == World.NETHER) {
                                for (int p = 0; p < 3 + r.nextInt(6); ++p) {
                                    int k = (12 + r.nextInt(12)) * (r.nextBoolean() ? -1 : 1);
                                    int l = (12 + r.nextInt(12)) * (r.nextBoolean() ? -1 : 1);
                                    BlockPos.Mutable blockpos$mutable = ApostleEntity.this.blockPosition().mutable().move(k, 0, l);
                                    blockpos$mutable.setX(blockpos$mutable.getX() + r.nextInt(5) - r.nextInt(5));
                                    blockpos$mutable.setY((int) BlockFinder.moveDownToGround(ApostleEntity.this));
                                    blockpos$mutable.setZ(blockpos$mutable.getZ() + r.nextInt(5) - r.nextInt(5));
                                    ZPiglinMinionEntity summonedentity = new ZPiglinMinionEntity(ModEntityType.ZPIGLIN_MINION.get(), ApostleEntity.this.level);
                                    summonedentity.moveTo(blockpos$mutable, 0.0F, 0.0F);
                                    summonedentity.setTrueOwner(ApostleEntity.this);
                                    summonedentity.setLimitedLife(60 * (90 + ApostleEntity.this.level.random.nextInt(180)));
                                    summonedentity.finalizeSpawn(ServerLevel, ApostleEntity.this.level.getCurrentDifficultyAt(blockpos$mutable), SpawnReason.MOB_SUMMONED, null, null);
                                    summonedentity.setTarget(livingentity);
                                    SummonCircleEntity summonCircle = new SummonCircleEntity(ApostleEntity.this.level, blockpos$mutable, summonedentity, false, true, ApostleEntity.this);
                                    ApostleEntity.this.level.addFreshEntity(summonCircle);
                                }
                            } else {
                                int k = (12 + r.nextInt(12)) * (r.nextBoolean() ? -1 : 1);
                                int l = (12 + r.nextInt(12)) * (r.nextBoolean() ? -1 : 1);
                                BlockPos.Mutable blockpos$mutable = ApostleEntity.this.blockPosition().mutable().move(k, 0, l);
                                blockpos$mutable.setX(blockpos$mutable.getX() + r.nextInt(5) - r.nextInt(5));
                                blockpos$mutable.setY((int) BlockFinder.moveDownToGround(ApostleEntity.this));
                                blockpos$mutable.setZ(blockpos$mutable.getZ() + r.nextInt(5) - r.nextInt(5));
                                ObsidianMonolithEntity summonedentity = new ObsidianMonolithEntity(ModEntityType.OBSIDIAN_MONOLITH.get(), ApostleEntity.this.level);
                                summonedentity.moveTo(blockpos$mutable, 0.0F, 0.0F);
                                summonedentity.setTrueOwner(ApostleEntity.this);
                                summonedentity.finalizeSpawn(ServerLevel, ApostleEntity.this.level.getCurrentDifficultyAt(blockpos$mutable), SpawnReason.MOB_SUMMONED, null, null);
                                ApostleEntity.this.level.addFreshEntity(summonedentity);
                            }
                        }
                    } else {
                        if (r.nextBoolean()) {
                            ZPiglinBruteMinionEntity summonedentity = new ZPiglinBruteMinionEntity(ModEntityType.ZPIGLIN_BRUTE_MINION.get(), ApostleEntity.this.level);
                            summonedentity.moveTo(blockpos, 0.0F, 0.0F);
                            summonedentity.setTrueOwner(ApostleEntity.this);
                            summonedentity.setLimitedLife(60 * (90 + ApostleEntity.this.level.random.nextInt(180)));
                            summonedentity.finalizeSpawn(ServerLevel, ApostleEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, null, null);
                            for(EquipmentSlotType equipmentslottype : EquipmentSlotType.values()) {
                                if (equipmentslottype.getType() == EquipmentSlotType.Group.ARMOR) {
                                    ItemStack itemstack = summonedentity.getItemBySlot(equipmentslottype);
                                    if (itemstack.isEmpty()) {
                                        Item item = getEquipmentForSlot(equipmentslottype, 1);
                                        if (ApostleEntity.this.level.getDifficulty() == Difficulty.HARD){
                                            item = netheriteArmor(equipmentslottype);
                                        }
                                        if (item != null) {
                                            summonedentity.setItemSlot(equipmentslottype, new ItemStack(item));
                                        }
                                    }
                                }
                                if (equipmentslottype == EquipmentSlotType.MAINHAND){
                                    if (ApostleEntity.this.level.getDifficulty() == Difficulty.HARD){
                                        summonedentity.setItemSlot(equipmentslottype, new ItemStack(Items.NETHERITE_AXE));
                                    }
                                }
                                summonedentity.setDropChance(equipmentslottype, 0.0F);
                            }
                            summonedentity.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, Integer.MAX_VALUE, 1, false, false));
                            summonedentity.setTarget(livingentity);
                            SummonCircleEntity summonCircle = new SummonCircleEntity(ApostleEntity.this.level, blockpos, summonedentity, false, true, ApostleEntity.this);
                            ApostleEntity.this.level.addFreshEntity(summonCircle);
                        } else {
                            if (ServerLevel.dimension() == World.NETHER) {
                                for (int p = 0; p < 2 + r.nextInt(ApostleEntity.this.level.getDifficulty().getId()); ++p) {
                                    int k = (12 + r.nextInt(12)) * (r.nextBoolean() ? -1 : 1);
                                    int l = (12 + r.nextInt(12)) * (r.nextBoolean() ? -1 : 1);
                                    BlockPos.Mutable blockpos$mutable = ApostleEntity.this.blockPosition().mutable().move(k, 0, l);
                                    blockpos$mutable.setX(blockpos$mutable.getX() + r.nextInt(5) - r.nextInt(5));
                                    blockpos$mutable.setY((int) BlockFinder.moveDownToGround(ApostleEntity.this));
                                    blockpos$mutable.setZ(blockpos$mutable.getZ() + r.nextInt(5) - r.nextInt(5));
                                    ZPiglinBruteMinionEntity summonedentity = new ZPiglinBruteMinionEntity(ModEntityType.ZPIGLIN_BRUTE_MINION.get(), ApostleEntity.this.level);
                                    summonedentity.moveTo(blockpos$mutable, 0.0F, 0.0F);
                                    summonedentity.setTrueOwner(ApostleEntity.this);
                                    summonedentity.setLimitedLife(60 * (90 + ApostleEntity.this.level.random.nextInt(180)));
                                    summonedentity.finalizeSpawn(ServerLevel, ApostleEntity.this.level.getCurrentDifficultyAt(blockpos$mutable), SpawnReason.MOB_SUMMONED, null, null);
                                    summonedentity.setTarget(livingentity);
                                    SummonCircleEntity summonCircle = new SummonCircleEntity(ApostleEntity.this.level, blockpos$mutable, summonedentity, false, true, ApostleEntity.this);
                                    ApostleEntity.this.level.addFreshEntity(summonCircle);
                                }
                            } else {
                                for (int p = 0; p < 1 + r.nextInt(2); ++p) {
                                    int k = (12 + r.nextInt(12)) * (r.nextBoolean() ? -1 : 1);
                                    int l = (12 + r.nextInt(12)) * (r.nextBoolean() ? -1 : 1);
                                    BlockPos.Mutable blockpos$mutable = ApostleEntity.this.blockPosition().mutable().move(k, 0, l);
                                    blockpos$mutable.setX(blockpos$mutable.getX() + r.nextInt(5) - r.nextInt(5));
                                    blockpos$mutable.setY((int) BlockFinder.moveDownToGround(ApostleEntity.this));
                                    blockpos$mutable.setZ(blockpos$mutable.getZ() + r.nextInt(5) - r.nextInt(5));
                                    ObsidianMonolithEntity summonedentity = new ObsidianMonolithEntity(ModEntityType.OBSIDIAN_MONOLITH.get(), ApostleEntity.this.level);
                                    summonedentity.moveTo(blockpos$mutable, 0.0F, 0.0F);
                                    summonedentity.setTrueOwner(ApostleEntity.this);
                                    summonedentity.finalizeSpawn(ServerLevel, ApostleEntity.this.level.getCurrentDifficultyAt(blockpos$mutable), SpawnReason.MOB_SUMMONED, null, null);
                                    ApostleEntity.this.level.addFreshEntity(summonedentity);
                                }
                            }
                        }
                    }
                    ApostleEntity.this.postSpellCast();
                }
            }
        }

        @Nullable
        public Item netheriteArmor(EquipmentSlotType pSlot) {
            switch(pSlot) {
                case HEAD:
                    return Items.NETHERITE_HELMET;
                case CHEST:
                    return Items.NETHERITE_CHESTPLATE;
                case LEGS:
                    return Items.NETHERITE_LEGGINGS;
                case FEET:
                    return Items.NETHERITE_BOOTS;
                default:
                    return null;
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return ModSounds.APOSTLE_PREPARE_SUMMON.get();
        }

        @Override
        protected SpellType getSpellType() {
            return SpellType.ZOMBIE;
        }
    }

    class FireRainSpellGoal extends CastingGoal {
        private FireRainSpellGoal() {
        }

        @Override
        public boolean canUse() {
            int i = ApostleEntity.this.level.getEntitiesOfClass(OwnedEntity.class, ApostleEntity.this.getBoundingBox().inflate(64.0D), ZOMBIE_MINIONS).size();
            int i2 = ApostleEntity.this.level.getEntitiesOfClass(AbstractTrapEntity.class, ApostleEntity.this.getBoundingBox().inflate(64.0D)).size();
            if (!super.canUse()) {
                return false;
            } else {
                int cool = ApostleEntity.this.spellStart();
                return ApostleEntity.this.coolDown >= cool
                        && ApostleEntity.this.spellCycle == 2
                        && !ApostleEntity.this.isSettingUpSecond()
                        && i > 4
                        && i2 < 2;
            }
        }

        protected int getCastingTime() {
            return 20;
        }

        public void castSpell() {
            if (!ApostleEntity.this.level.isClientSide) {
                LivingEntity livingentity = ApostleEntity.this.getTarget();
                if (livingentity != null) {
                    BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(livingentity.getX(), livingentity.getY(), livingentity.getZ());

                    while (blockpos$mutable.getY() > 0 && !ApostleEntity.this.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
                        blockpos$mutable.move(Direction.DOWN);
                    }
                    if (ApostleEntity.this.isSecondPhase()){
                        ArrowRainTrapEntity fireRainTrap = new ArrowRainTrapEntity(ApostleEntity.this.level, blockpos$mutable.getX(), blockpos$mutable.getY() + 1, blockpos$mutable.getZ());
                        fireRainTrap.setOwner(ApostleEntity.this);
                        fireRainTrap.setDuration(100);
                        ApostleEntity.this.level.addFreshEntity(fireRainTrap);
                    } else {
                        FireRainTrapEntity fireRainTrap = new FireRainTrapEntity(ApostleEntity.this.level, blockpos$mutable.getX(), blockpos$mutable.getY() + 1, blockpos$mutable.getZ());
                        fireRainTrap.setOwner(ApostleEntity.this);
                        fireRainTrap.setDuration(1200);
                        ApostleEntity.this.level.addFreshEntity(fireRainTrap);
                    }
                    ApostleEntity.this.postSpellCast();
                }
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return ModSounds.APOSTLE_PREPARE_SUMMON.get();
        }

        @Override
        protected SpellType getSpellType() {
            return SpellType.FIRE;
        }
    }

    class SkeletonSpellGoal extends CastingGoal {
        private SkeletonSpellGoal() {
        }

        @Override
        public boolean canUse() {
            int i = ApostleEntity.this.level.getEntitiesOfClass(OwnedEntity.class, ApostleEntity.this.getBoundingBox().inflate(64.0D), SKELETON_MINIONS).size();
            if (!super.canUse()) {
                return false;
            } else {
                int cool = ApostleEntity.this.spellStart();
                return ApostleEntity.this.coolDown >= cool
                        && ApostleEntity.this.spellCycle == 3
                        && !ApostleEntity.this.isSettingUpSecond()
                        && i < 2;
            }
        }

        protected int getCastingTime() {
            return 60;
        }

        public void castSpell() {
            if (!ApostleEntity.this.level.isClientSide) {
                ServerWorld serverWorld = (ServerWorld) ApostleEntity.this.level;
                LivingEntity livingentity = ApostleEntity.this.getTarget();
                Random r = ApostleEntity.this.random;
                if (livingentity != null) {
                    if (r.nextBoolean()) {
                        if (ApostleEntity.this.isSecondPhase()) {
                            int k = (12 + r.nextInt(12)) * (r.nextBoolean() ? -1 : 1);
                            int l = (12 + r.nextInt(12)) * (r.nextBoolean() ? -1 : 1);
                            BlockPos.Mutable blockpos$mutable = ApostleEntity.this.blockPosition().mutable().move(k, 0, l);
                            blockpos$mutable.setX(blockpos$mutable.getX() + r.nextInt(5) - r.nextInt(5));
                            blockpos$mutable.setY((int) BlockFinder.moveDownToGround(ApostleEntity.this) + 3);
                            blockpos$mutable.setZ(blockpos$mutable.getZ() + r.nextInt(5) - r.nextInt(5));
                            MalghastEntity summonedentity = new MalghastEntity(ModEntityType.MALGHAST.get(), ApostleEntity.this.level);
                            if (serverWorld.noCollision(summonedentity, summonedentity.getBoundingBox().move(blockpos$mutable.above(2)).inflate(0.25F))){
                                summonedentity.setPos(blockpos$mutable.getX(), blockpos$mutable.getY() + 2, blockpos$mutable.getZ());
                            } else {
                                blockpos$mutable = ApostleEntity.this.blockPosition().mutable();
                                summonedentity.setPos(blockpos$mutable.getX(), blockpos$mutable.getY() + 2, blockpos$mutable.getZ());
                            }
                            summonedentity.setTrueOwner(ApostleEntity.this);
                            summonedentity.setLimitedLife(60 * (90 + ApostleEntity.this.level.random.nextInt(180)));
                            summonedentity.finalizeSpawn(serverWorld, ApostleEntity.this.level.getCurrentDifficultyAt(blockpos$mutable), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                            summonedentity.setTarget(livingentity);
                            SummonCircleEntity summonCircleEntity = new SummonCircleEntity(ApostleEntity.this.level, blockpos$mutable, summonedentity, false, false, ApostleEntity.this);
                            ApostleEntity.this.level.addFreshEntity(summonCircleEntity);
                        } else {
                            BlockPos blockpos = ApostleEntity.this.blockPosition();
                            SkeletonVillagerMinionEntity summonedentity = new SkeletonVillagerMinionEntity(ModEntityType.SKELETON_VILLAGER_MINION.get(), ApostleEntity.this.level);
                            summonedentity.moveTo(blockpos, 0.0F, 0.0F);
                            summonedentity.setTrueOwner(ApostleEntity.this);
                            summonedentity.setLimitedLife(60 * (90 + ApostleEntity.this.level.random.nextInt(180)));
                            for (EquipmentSlotType equipmentslottype : EquipmentSlotType.values()) {
                                if (equipmentslottype.getType() == EquipmentSlotType.Group.ARMOR) {
                                    ItemStack itemstack = summonedentity.getItemBySlot(equipmentslottype);
                                    if (itemstack.isEmpty()) {
                                        Item item = getEquipmentForSlot(equipmentslottype, 3);
                                        if (item != null) {
                                            summonedentity.setItemSlot(equipmentslottype, new ItemStack(item));
                                        }
                                    }
                                }
                                summonedentity.setDropChance(equipmentslottype, 0.0F);
                            }
                            summonedentity.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, Integer.MAX_VALUE, 1, false, false));
                            summonedentity.finalizeSpawn(serverWorld, ApostleEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                            summonedentity.setTarget(livingentity);
                            SummonCircleEntity summonCircleEntity = new SummonCircleEntity(ApostleEntity.this.level, blockpos, summonedentity, false, ApostleEntity.this);
                            ApostleEntity.this.level.addFreshEntity(summonCircleEntity);
                        }
                    } else {
                        for (int p = 0; p < 1 + r.nextInt(2); ++p) {
                            int k = (12 + r.nextInt(12)) * (r.nextBoolean() ? -1 : 1);
                            int l = (12 + r.nextInt(12)) * (r.nextBoolean() ? -1 : 1);
                            BlockPos.Mutable blockpos$mutable = ApostleEntity.this.blockPosition().mutable().move(k, 0, l);
                            blockpos$mutable.setX(blockpos$mutable.getX() + r.nextInt(5) - r.nextInt(5));
                            blockpos$mutable.setY((int) BlockFinder.moveDownToGround(ApostleEntity.this));
                            blockpos$mutable.setZ(blockpos$mutable.getZ() + r.nextInt(5) - r.nextInt(5));
                            SkeletonVillagerMinionEntity summonedentity = new SkeletonVillagerMinionEntity(ModEntityType.SKELETON_VILLAGER_MINION.get(), ApostleEntity.this.level);
                            summonedentity.moveTo(blockpos$mutable, 0.0F, 0.0F);
                            summonedentity.setTrueOwner(ApostleEntity.this);
                            summonedentity.setLimitedLife(60 * (90 + ApostleEntity.this.level.random.nextInt(180)));
                            summonedentity.finalizeSpawn(serverWorld, ApostleEntity.this.level.getCurrentDifficultyAt(blockpos$mutable), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                            summonedentity.setTarget(livingentity);
                            SummonCircleEntity summonCircleEntity = new SummonCircleEntity(ApostleEntity.this.level, blockpos$mutable, summonedentity, false, ApostleEntity.this);
                            ApostleEntity.this.level.addFreshEntity(summonCircleEntity);
                        }
                    }
                    ApostleEntity.this.postSpellCast();
                }
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return ModSounds.APOSTLE_PREPARE_SUMMON.get();
        }

        @Override
        protected SpellType getSpellType() {
            return SpellType.SKELETON;
        }
    }

    class FireTornadoSpellGoal extends CastingGoal {
        private FireTornadoSpellGoal() {
        }

        @Override
        public boolean canUse() {
            int i = ApostleEntity.this.level.getEntitiesOfClass(OwnedEntity.class, ApostleEntity.this.getBoundingBox().inflate(64.0D), SKELETON_MINIONS).size();
            int i2 = ApostleEntity.this.level.getEntitiesOfClass(FireTornadoEntity.class, ApostleEntity.this.getBoundingBox().inflate(64.0D)).size();
            int cool = ApostleEntity.this.spellStart();
            if (!super.canUse()) {
                return false;
            } else if (ApostleEntity.this.isSettingUpSecond()){
                return false;
            } else if (ApostleEntity.this.getHitTimes() >= 6){
                return i2 < 1;
            } else {
                return ApostleEntity.this.getCoolDown() >= cool
                        && ApostleEntity.this.getTornadoCoolDown() <= 0
                        && ApostleEntity.this.getSpellCycle() == 3
                        && i >= 2
                        && i2 < 1;
            }
        }

        protected int getCastingTime() {
            return 60;
        }

        public void castSpell() {
            if (!ApostleEntity.this.level.isClientSide) {
                LivingEntity livingentity = ApostleEntity.this.getTarget();
                if (livingentity != null) {
                    BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(livingentity.getX(), livingentity.getY(), livingentity.getZ());

                    while (blockpos$mutable.getY() > 0 && !ApostleEntity.this.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
                        blockpos$mutable.move(Direction.DOWN);
                    }

                    FireTornadoTrapEntity fireTornadoTrapEntity = new FireTornadoTrapEntity(ModEntityType.FIRE_TORNADO_TRAP.get(), ApostleEntity.this.level);
                    fireTornadoTrapEntity.setPos(blockpos$mutable.getX(), blockpos$mutable.getY() + 1, blockpos$mutable.getZ());
                    fireTornadoTrapEntity.setOwner(ApostleEntity.this);
                    fireTornadoTrapEntity.setDuration(60);
                    ApostleEntity.this.level.addFreshEntity(fireTornadoTrapEntity);
                    ApostleEntity.this.postSpellCast();
                }
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return ModSounds.APOSTLE_PREPARE_SUMMON.get();
        }

        @Override
        protected SpellType getSpellType() {
            return SpellType.TORNADO;
        }
    }

    class RoarSpellGoal extends CastingGoal {
        private RoarSpellGoal() {
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else if (ApostleEntity.this.getTarget() == null) {
                return false;
            } else return ApostleEntity.this.distanceTo(ApostleEntity.this.getTarget()) < 4.0F
                    && !ApostleEntity.this.isSettingUpSecond();
        }

        protected int getCastingTime() {
            return 200;
        }

        protected int getCastingInterval() {
            return 120;
        }

        public void castSpell() {
            ApostleEntity apostleEntity = ApostleEntity.this;
            apostleEntity.hitTimes = 0;
            if (!apostleEntity.isSecondPhase()){
                apostleEntity.setFiring(true);
                apostleEntity.coolDown = 0;
                apostleEntity.playSound(ModSounds.ROAR_SPELL.get(), 1.0F, 1.0F);
            } else {
                double d0 = Math.min(apostleEntity.getTarget().getY(), apostleEntity.getY());
                double d1 = Math.max(apostleEntity.getTarget().getY(), apostleEntity.getY()) + 1.0D;
                for(int i = 0; i < 5; ++i) {
                    float f1 = f + (float)i * (float)Math.PI * 0.4F;
                    spawnBlasts(apostleEntity,apostleEntity.getX() + (double)MathHelper.cos(f1) * 1.5D, apostleEntity.getZ() + (double)MathHelper.sin(f1) * 1.5D, d0, d1);
                }

                for(int k = 0; k < 8; ++k) {
                    float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + 1.2566371F;
                    spawnBlasts(apostleEntity,apostleEntity.getX() + (double)MathHelper.cos(f2) * 2.5D, apostleEntity.getZ() + (double)MathHelper.sin(f2) * 2.5D, d0, d1);
                }
                if (apostleEntity.getHealth() < apostleEntity.getMaxHealth()/2){
                    for(int k = 0; k < 11; ++k) {
                        float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + 1.2566371F;
                        spawnBlasts(apostleEntity,apostleEntity.getX() + (double)MathHelper.cos(f2) * 2.5D, apostleEntity.getZ() + (double)MathHelper.sin(f2) * 2.5D, d0, d1);
                    }
                }

                if (apostleEntity.getHealth() < apostleEntity.getMaxHealth()/4){
                    for(int k = 0; k < 14; ++k) {
                        float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + 1.2566371F;
                        spawnBlasts(apostleEntity,apostleEntity.getX() + (double)MathHelper.cos(f2) * 2.5D, apostleEntity.getZ() + (double)MathHelper.sin(f2) * 2.5D, d0, d1);
                    }
                }
            }
        }

        protected SoundEvent getSpellPrepareSound() {
            return ModSounds.APOSTLE_PREPARE_SPELL.get();
        }

        @Override
        protected SpellType getSpellType() {
            return SpellType.ROAR;
        }

        public void spawnBlasts(LivingEntity livingEntity, double pPosX, double pPosZ, double PPPosY, double pOPosY) {
            BlockPos blockpos = new BlockPos(pPosX, pOPosY, pPosZ);
            boolean flag = false;
            double d0 = 0.0D;

            do {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate = livingEntity.level.getBlockState(blockpos1);
                if (blockstate.isFaceSturdy(livingEntity.level, blockpos1, Direction.UP)) {
                    if (!livingEntity.level.isEmptyBlock(blockpos)) {
                        BlockState blockstate1 = livingEntity.level.getBlockState(blockpos);
                        VoxelShape voxelshape = blockstate1.getCollisionShape(livingEntity.level, blockpos);
                        if (!voxelshape.isEmpty()) {
                            d0 = voxelshape.max(Direction.Axis.Y);
                        }
                    }

                    flag = true;
                    break;
                }

                blockpos = blockpos.below();
            } while(blockpos.getY() >= MathHelper.floor(PPPosY) - 1);

            if (flag) {
                FireBlastTrapEntity fireBlastTrap = new FireBlastTrapEntity(livingEntity.level, pPosX, (double)blockpos.getY() + d0, pPosZ);
                fireBlastTrap.setOwner(livingEntity);
                livingEntity.level.addFreshEntity(fireBlastTrap);
            }

        }
    }

    class SecondPhaseIndicator extends Goal{
        private SecondPhaseIndicator() {
        }

        @Override
        public boolean canUse() {
            return ApostleEntity.this.getHealth() <= ApostleEntity.this.getMaxHealth()/2
                    && ApostleEntity.this.getTarget() != null
                    && !ApostleEntity.this.isSecondPhase()
                    && !ApostleEntity.this.isSettingUpSecond();
        }

        @Override
        public void tick() {
            ApostleEntity.this.setSettingUpSecond(true);
        }
    }

    static class BowAttackGoal<T extends ApostleEntity & IRangedAttackMob> extends RangedBowAttackGoal<T> {
        private final T mob;

        public BowAttackGoal(T p_i47515_1_) {
            super(p_i47515_1_, 1.0D, 40, 30.0F);
            this.mob = p_i47515_1_;
        }

        @Override
        public boolean canUse() {
            return !this.mob.isSecondPhase() && this.mob.getTarget() != null && this.HaveBow() && !this.mob.isSettingUpSecond();
        }

        protected boolean HaveBow() {
            return this.mob.isHolding(item -> item instanceof BowItem);
        }
    }

    static class FasterBowAttackGoal<T extends ApostleEntity & IRangedAttackMob> extends RangedBowAttackGoal<T> {
        private final T mob;

        public FasterBowAttackGoal(T p_i47515_1_) {
            super(p_i47515_1_, 1.0D, 20, 30.0F);
            this.mob = p_i47515_1_;
        }

        @Override
        public boolean canUse() {
            return this.mob.isSecondPhase() && this.mob.getHealth() > this.mob.getMaxHealth()/4 && this.mob.getTarget() != null && this.HaveBow() && !this.mob.isSettingUpSecond();
        }

        protected boolean HaveBow() {
            return this.mob.isHolding(item -> item instanceof BowItem);
        }
    }

    static class FastestBowAttackGoal<T extends ApostleEntity & IRangedAttackMob> extends RangedBowAttackGoal<T> {
        private final T mob;

        public FastestBowAttackGoal(T p_i47515_1_) {
            super(p_i47515_1_, 1.0D, 5, 30.0F);
            this.mob = p_i47515_1_;
        }

        @Override
        public boolean canUse() {
            return this.mob.isSecondPhase() && this.mob.getHealth() <= this.mob.getMaxHealth()/4 && this.mob.getTarget() != null && this.HaveBow() && !this.mob.isSettingUpSecond();
        }

        protected boolean HaveBow() {
            return this.mob.isHolding(item -> item instanceof BowItem);
        }
    }

    public boolean canChangeDimensions() {
        return false;
    }

}
