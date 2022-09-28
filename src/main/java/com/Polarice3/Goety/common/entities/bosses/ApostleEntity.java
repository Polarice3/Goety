package com.Polarice3.Goety.common.entities.bosses;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.hostile.cultists.*;
import com.Polarice3.Goety.common.entities.projectiles.FireTornadoEntity;
import com.Polarice3.Goety.common.entities.projectiles.SoulFireballEntity;
import com.Polarice3.Goety.common.entities.projectiles.SoulSkullEntity;
import com.Polarice3.Goety.common.entities.utilities.FireBlastTrapEntity;
import com.Polarice3.Goety.common.entities.utilities.FireRainTrapEntity;
import com.Polarice3.Goety.common.entities.utilities.FireTornadoTrapEntity;
import com.Polarice3.Goety.common.entities.utilities.LightningTrapEntity;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Predicate;

public class ApostleEntity extends SpellcastingCultistEntity implements IRangedAttackMob {
    private int f;
    private int hitTimes;
    private int cooldown;
    private int spellcycle;
    private int titleNumber;
    private final Predicate<Entity> ALIVE = Entity::isAlive;
    private boolean roarparticles;
    private boolean fireArrows;
    private boolean regen;
    private Effect arrowEffect;
    protected static final DataParameter<Byte> BOSS_FLAGS = EntityDataManager.defineId(ApostleEntity.class, DataSerializers.BYTE);
    private final ServerBossInfo bossInfo = new ServerBossInfo(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS);
    private final Predicate<LivingEntity> ZOMBIE_MINIONS = (livingEntity) -> {
        return livingEntity instanceof ICultistMinion && !(livingEntity instanceof SkeletonVillagerMinionEntity);
    };
    private final Predicate<LivingEntity> SKELETON_MINIONS = (livingEntity) -> {
        return livingEntity instanceof SkeletonVillagerMinionEntity;
    };
    private final EntityPredicate zombieCount = (new EntityPredicate()).range(64.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam().selector(ZOMBIE_MINIONS);
    private final EntityPredicate skeletonCount = (new EntityPredicate()).range(64.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam().selector(SKELETON_MINIONS);

    public ApostleEntity(EntityType<? extends SpellcastingCultistEntity> type, World worldIn) {
        super(type, worldIn);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(PathNodeType.DAMAGE_FIRE, 0.0F);
        this.f = 0;
        this.cooldown = 100;
        this.spellcycle = 0;
        this.hitTimes = 0;
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
                .add(Attributes.MAX_HEALTH, 180.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ARMOR, 12.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 6.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75D)
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BOSS_FLAGS, (byte)0);
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

    protected SoundEvent getAmbientSound() {
        return ModSounds.APOSTLE_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.APOSTLE_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.APOSTLE_DEATH.get();
    }

    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    public boolean canBeAffected(EffectInstance pPotioneffect) {
        if (pPotioneffect.getEffect() == ModEffects.APOSTLE_CURSE.get()) {
            net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent event = new net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent(this, pPotioneffect);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
            return event.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW;
        }
        return super.canBeAffected(pPotioneffect);
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("firing", this.f);
        pCompound.putInt("cooldown", this.cooldown);
        pCompound.putInt("spellcycle", this.spellcycle);
        pCompound.putInt("hitTimes", this.hitTimes);
        pCompound.putInt("titleNumber", this.titleNumber);
        pCompound.putBoolean("fireArrows", this.fireArrows);
        pCompound.putBoolean("secondPhase", this.isSecondPhase());
        pCompound.putBoolean("settingSecondPhase", this.isSettingupSecond());
        pCompound.putBoolean("regen", this.regen);
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.f = pCompound.getInt("firing");
        this.cooldown = pCompound.getInt("cooldown");
        this.spellcycle = pCompound.getInt("spellcycle");
        this.hitTimes = pCompound.getInt("hitTimes");
        this.titleNumber = pCompound.getInt("titleNumber");
        this.fireArrows = pCompound.getBoolean("fireArrows");
        this.regen = pCompound.getBoolean("regen");
        this.setTitleNumber(this.titleNumber);
        this.setSecondPhase(pCompound.getBoolean("secondPhase"));
        this.setSettingupSecond(pCompound.getBoolean("settingSecondPhase"));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    public void setCustomName(@Nullable ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
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

    protected boolean isAffectedByFluids() {
        return false;
    }

    public void die(DamageSource cause) {
        if (!this.level.isClientSide){
            ServerWorld serverWorld = (ServerWorld) this.level;
            if (serverWorld.getLevelData().isThundering()){
                serverWorld.setWeatherParameters(6000, 0, false, false);
            }
            for(int k = 0; k < 200; ++k) {
                float f2 = random.nextFloat() * 4.0F;
                float f1 = random.nextFloat() * ((float)Math.PI * 2F);
                double d1 = MathHelper.cos(f1) * f2;
                double d2 = 0.01D + random.nextDouble() * 0.5D;
                double d3 = MathHelper.sin(f1) * f2;
                serverWorld.sendParticles(ParticleTypes.LARGE_SMOKE, this.getX() + d1 * 0.1D, this.getY() + 0.3D, this.getZ() + d3 * 0.1D, 0, d1, d2, d3, 0.5F);
                serverWorld.sendParticles(ParticleTypes.FLAME, this.getX() + d1 * 0.1D, this.getY() + 0.3D, this.getZ() + d3 * 0.1D, 0, d1, d2, d3, 0.5F);
            }
        }
        for (FireRainTrapEntity fireRainTrap: this.level.getEntitiesOfClass(FireRainTrapEntity.class, this.getBoundingBox().inflate(64))){
            fireRainTrap.remove();
        }
        for (FireTornadoTrapEntity fireTornadoTrapEntity: this.level.getEntitiesOfClass(FireTornadoTrapEntity.class, this.getBoundingBox().inflate(64))){
            fireTornadoTrapEntity.remove();
        }
        for (FireTornadoEntity fireTornadoEntity: this.level.getEntitiesOfClass(FireTornadoEntity.class, this.getBoundingBox().inflate(64))){
            fireTornadoEntity.remove();
        }
        super.die(cause);
    }

    protected float getDamageAfterMagicAbsorb(DamageSource source, float damage) {
        damage = super.getDamageAfterMagicAbsorb(source, damage);
        if (source.getEntity() == this) {
            damage = 0.0F;
        }

        if (source.getDirectEntity() instanceof SoulSkullEntity || source.getDirectEntity() instanceof FireballEntity) {
            damage = (float)((double)damage * 0.15D);
        }

        return damage;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        if (!this.hasCustomName()){
            int random = this.random.nextInt(18);
            int random2 = this.random.nextInt(12);
            this.setTitleNumber(random2);
            this.TitleEffect(random2);
            TranslationTextComponent component = new TranslationTextComponent("name.goety.apostle." + random);
            TranslationTextComponent component1 = new TranslationTextComponent("title.goety." + random2);
            this.setCustomName(new TranslationTextComponent(component.getString() + " " + "The" + " " + component1.getString()));
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
                this.addEffect(new EffectInstance(Effects.ABSORPTION, Integer.MAX_VALUE, 1, false, false));
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
                this.setArrowEffect(ModEffects.CURSED.get());
                break;
        }
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

    public void setSettingupSecond(boolean settingupSecond){
        this.setBossFlag(2, settingupSecond);
    }

    public boolean isSettingupSecond(){
        return this.getBossFlag(2);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
    }

    @OnlyIn(Dist.CLIENT)
    public ArmPose getArmPose() {
        if (this.getMainHandItem().getItem() == Items.BOW) {
            if (this.isAggressive() && !this.isSpellcasting() && !this.isSettingupSecond()){
                return ArmPose.BOW_AND_ARROW;
            } else if (this.isSpellcasting()){
                return ArmPose.SPELL_AND_WEAPON;
            } else if (this.isSettingupSecond()){
                return ArmPose.SPELL_AND_WEAPON;
            } else {
                return ArmPose.NEUTRAL;
            }
        } else {
            return ArmPose.NEUTRAL;
        }
    }

    public boolean isFiring(){
        return this.roarparticles;
    }

    public void setFiring(boolean firing){
        this.roarparticles = firing;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        LivingEntity livingEntity = this.getTarget();
        if (livingEntity != null){
            if (pSource.getEntity() == livingEntity){
                if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SMITE, livingEntity) > 3){
                    this.teleport();
                } else {
                    ++this.hitTimes;
                }
            }
        }

        if (pSource == DamageSource.LIGHTNING_BOLT || pSource == DamageSource.FALL || pSource.isExplosion()){
            return false;
        }

        if (this.isSettingupSecond()){
            return false;
        }

        if (pSource.getDirectEntity() instanceof SoulFireballEntity || pSource.getEntity() instanceof SoulFireballEntity){
            return false;
        }

        if (pAmount > 20.0F){
            return super.hurt(pSource, 20.0F);
        } else {
            if (this.level.dimension() == World.NETHER){
                return super.hurt(pSource, pAmount/4);
            } else {
                return super.hurt(pSource, pAmount);
            }
        }
    }

    protected void teleport() {
        if (!this.level.isClientSide() && this.isAlive()) {
            double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 24.0D;
            double d1 = this.getY() + (double)(this.random.nextInt(24) - 12);
            double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 24.0D;
            this.hitTimes = 0;
            this.teleport(d0, d1, d2);
        }
    }

    private void teleport(double pX, double pY, double pZ) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(pX, pY, pZ);

        while(blockpos$mutable.getY() > 0 && !this.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
            blockpos$mutable.move(Direction.DOWN);
        }

        BlockState blockstate = this.level.getBlockState(blockpos$mutable);
        boolean flag = blockstate.getMaterial().blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        if (flag && !flag1) {
            net.minecraftforge.event.entity.living.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory.onEnderTeleport(this, pX, pY, pZ);
            if (event.isCanceled()) return;
            boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (flag2 && !this.isSilent()) {
                this.level.playSound((PlayerEntity)null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
        }
    }

    private void teleportTowards(Entity entity) {
        Vector3d vector3d = new Vector3d(this.getX() - entity.getX(), this.getY(0.5D) - entity.getEyeY(), this.getZ() - entity.getZ());
        vector3d = vector3d.normalize();
        double d0 = 16.0D;
        double d1 = this.getX() + (this.random.nextDouble() - 0.5D) * 8.0D - vector3d.x * d0;
        double d2 = this.getY() + (double)(this.random.nextInt(16) - 8) - vector3d.y * d0;
        double d3 = this.getZ() + (this.random.nextDouble() - 0.5D) * 8.0D - vector3d.z * d0;
        this.teleport(d1, d2, d3);
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return ModSounds.APOSTLE_CAST_SPELL.get();
    }

    protected boolean canRide(Entity pEntity) {
        return false;
    }

    public void aiStep() {
        if (this.tickCount % 100 == 0){
            MobUtil.secretConversion(this);
        }
        if (!this.level.isClientSide){
            ServerWorld serverWorld = (ServerWorld) this.level;
            for (WitchEntity witch : this.level.getEntitiesOfClass(WitchEntity.class, this.getBoundingBox().inflate(8.0D))){
                BeldamEntity beldam = witch.convertTo(ModEntityType.BELDAM.get(), true);
                if (beldam != null) {
                    beldam.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(witch.blockPosition()), SpawnReason.CONVERSION, null, null);
                    net.minecraftforge.event.ForgeEventFactory.onLivingConvert(witch, beldam);
                }
            }
        }
        if (this.isSettingupSecond()){
            this.serverAiStep();
            if (this.tickCount % 20 == 0) {
                this.heal(5.0F);
            }
            for (Entity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D), ALIVE)) {
                if (!entity.isAlliedTo(this)) {
                    this.barrier(entity, this);
                }
            }
            if (!this.level.isClientSide){
                ServerWorld serverWorld = (ServerWorld) this.level;
                Vector3d vector3d = this.getBoundingBox().getCenter();
                for(int i = 0; i < 40; ++i) {
                    double d0 = this.random.nextGaussian() * 0.2D;
                    double d1 = this.random.nextGaussian() * 0.2D;
                    double d2 = this.random.nextGaussian() * 0.2D;
                    serverWorld.sendParticles(ParticleTypes.LARGE_SMOKE, vector3d.x, vector3d.y, vector3d.z, 0, d0, d1, d2, 0.5F);
                    serverWorld.sendParticles(ParticleTypes.FLAME, vector3d.x, vector3d.y, vector3d.z, 1, 0, 0, 0, 0);
                }
            }
            if (this.getHealth() >= this.getMaxHealth()){
                if (!this.level.isClientSide){
                    ServerWorld serverWorld = (ServerWorld) ApostleEntity.this.level;
                    if (!serverWorld.isThundering()) {
                        serverWorld.setWeatherParameters(0, 6000, true, true);
                    }
                    for(int k = 0; k < 200; ++k) {
                        float f2 = random.nextFloat() * 4.0F;
                        float f1 = random.nextFloat() * ((float)Math.PI * 2F);
                        double d1 = MathHelper.cos(f1) * f2;
                        double d2 = 0.01D + random.nextDouble() * 0.5D;
                        double d3 = MathHelper.sin(f1) * f2;
                        serverWorld.sendParticles(ParticleTypes.FLAME, this.getX() + d1 * 0.1D, this.getY() + 0.3D, this.getZ() + d3 * 0.1D, 0, d1, d2, d3, 0.5F);
                    }
                }
                this.setSettingupSecond(false);
                this.setSecondPhase(true);
                this.teleport();
            }
        } else {
            super.aiStep();
        }
        LivingEntity target = this.getTarget();
        if (this.getMainHandItem().isEmpty()){
            this.setItemInHand(Hand.MAIN_HAND, new ItemStack(Items.BOW));
        }
        if (this.isSecondPhase()) {
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
            if (this.tickCount % 100 == 0 && !this.isDeadOrDying()) {
                if (!this.level.isClientSide){
                    ServerWorld serverWorld = (ServerWorld) this.level;
                    serverWorld.setWeatherParameters(0, 6000, true, true);
                }
            }
            if (this.hitTimes >= 1) {
                this.teleport();
            }
            if (this.cooldown < 50) {
                ++this.cooldown;
            } else {
                this.spellcycle = 1;
            }
            if (this.level.isThundering() && this.level.canSeeSky(this.blockPosition())){
                if (this.tickCount % 20 == 0) {
                    BlockPos.Mutable blockPos = new BlockPos.Mutable(this.getRandomX(0.2), this.getY(), this.getRandomZ(0.2));

                    while (blockPos.getY() < this.getY() + 64.0D && !this.level.getBlockState(blockPos).getMaterial().blocksMotion()) {
                        blockPos.move(Direction.UP);
                    }
                    Random random = this.level.random;
                    double d = (random.nextBoolean() ? 1 : -1);
                    double e = (random.nextBoolean() ? 1 : -1);
                    double d2 = (random.nextInt(900) * d);
                    double d3 = -900.0D;
                    double d4 = (random.nextInt(900) * e);
                    SoulFireballEntity fireball = new SoulFireballEntity(this.level, this, d2, d3, d4);
                    fireball.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                    this.level.addFreshEntity(fireball);
                    if (!this.isSilent()) {
                        this.level.levelEvent(null, 1016, fireball.blockPosition(), 0);
                    }
                }
            }
        } else {
            if (this.Regen()) {
                if (this.tickCount % 40 == 0) {
                    if (this.getHealth() < this.getMaxHealth()) {
                        this.heal(1.0F);
                    }
                }
            } else {
                if (this.tickCount % 60 == 0) {
                    if (this.getHealth() < this.getMaxHealth()) {
                        this.heal(1.0F);
                    }
                }
            }
            if (this.hitTimes >= 2) {
                this.teleport();
            }
            if (this.cooldown < 100) {
                ++this.cooldown;
            } else {
                this.spellcycle = 0;
            }
        }
        if (this.spellcycle == 1){
            if (this.level.random.nextBoolean()){
                this.spellcycle = 2;
            } else {
                this.spellcycle = 3;
            }
        }
        for (LivingEntity living : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(32))){
            if (!(living instanceof AbstractCultistEntity) && !(living instanceof ICultistMinion)){
                if (living.isInWater()){
                    living.hurt(DamageSource.HOT_FLOOR, 1.0F);
                }
            }
        }
        if (target == null){
            this.hitTimes = 0;
            for (PlayerEntity player : this.level.getEntitiesOfClass(PlayerEntity.class, this.getBoundingBox().inflate(64), EntityPredicates.NO_CREATIVE_OR_SPECTATOR)){
                this.setTarget(player);
            }
            this.addEffect(new EffectInstance(Effects.GLOWING, 20));
        } else {
            if (MobUtil.isInRain(this) && MobUtil.isInRain(target)){
                if (this.tickCount % 600 == 0){
                    BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(target.getX(), target.getY(), target.getZ());

                    while (blockpos$mutable.getY() > 0 && !ApostleEntity.this.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
                        blockpos$mutable.move(Direction.DOWN);
                    }

                    LightningTrapEntity lightningTrap = new LightningTrapEntity(ModEntityType.LIGHTNINGTRAP.get(), this.level);
                    lightningTrap.moveTo(blockpos$mutable.getX(), blockpos$mutable.getY() + 1, blockpos$mutable.getZ());
                    lightningTrap.setOwner(this);
                    lightningTrap.setDuration(100);
                    this.level.addFreshEntity(lightningTrap);
                }
            }
            if (target.distanceToSqr(this) > 576 && !target.isFallFlying()){
                this.teleportTowards(target);
            }
        }
        if (this.isFiring()) {
            ++this.f;
            if (this.f % 2 == 0 && this.f < 10) {
                for (Entity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D), ALIVE)) {
                    if (!(entity instanceof AbstractCultistEntity) && !(entity instanceof ICultistMinion)) {
                        entity.hurt(DamageSource.mobAttack(this), 6.0F);
                        this.launch(entity, this);
                    }
                }
                if (!this.level.isClientSide){
                    this.serverRoarParticles();
                }

            }
            if (this.f >= 10){
                this.teleport();
                this.setFiring(false);
                this.f = 0;
            }
        }
        if (this.isInWater() || this.isInLava()){
            this.teleport();
        }
        if (this.level.dimension() == World.NETHER){
            if (target != null){
                target.addEffect(new EffectInstance(ModEffects.APOSTLE_CURSE.get(), 100));
            }
        }
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
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileHelper.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.item.BowItem)));
        AbstractArrowEntity abstractarrowentity = this.getArrow(itemstack, pDistanceFactor);
        if (this.getMainHandItem().getItem() instanceof net.minecraft.item.BowItem) {
            abstractarrowentity = ((net.minecraft.item.BowItem) this.getMainHandItem().getItem()).customArrow(abstractarrowentity);
        }
        double d0 = pTarget.getX() - this.getX();
        double d1 = pTarget.getY(0.3333333333333333D) - abstractarrowentity.getY();
        double d2 = pTarget.getZ() - this.getZ();
        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        abstractarrowentity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(abstractarrowentity);
    }

    protected AbstractArrowEntity getArrow(ItemStack pArrowStack, float pDistanceFactor) {
        AbstractArrowEntity abstractarrowentity = ProjectileHelper.getMobArrow(this, pArrowStack, pDistanceFactor);
        if (this.getArrowEffect() != null){
            int amp;
            if (this.isSecondPhase()){
                amp = 1;
            } else {
                amp = 0;
            }
            if (this.getArrowEffect() == ModEffects.CURSED.get()){
                ((ArrowEntity)abstractarrowentity).addEffect(new EffectInstance(this.getArrowEffect(), 50, amp));
            } else {
                ((ArrowEntity)abstractarrowentity).addEffect(new EffectInstance(this.getArrowEffect(), 100, amp));
            }
        }
        if (this.getFireArrow()){
            abstractarrowentity.setRemainingFireTicks(100);
        }
        return abstractarrowentity;
    }

    public boolean canFireProjectileWeapon(ShootableItem p_230280_1_) {
        return p_230280_1_ == Items.BOW;
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

    class CastingSpellGoal extends CastingASpellGoal {
        private CastingSpellGoal() {
        }

        public void tick() {
            if (ApostleEntity.this.getTarget() != null) {
                ApostleEntity.this.getLookControl().setLookAt(ApostleEntity.this.getTarget(), (float) ApostleEntity.this.getMaxHeadYRot(), (float) ApostleEntity.this.getMaxHeadXRot());
            }
        }
    }

    class FireballSpellGoal extends UseSpellGoal {
        private FireballSpellGoal() {
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else {
                return ApostleEntity.this.spellcycle == 0 && !ApostleEntity.this.isSettingupSecond() && !ApostleEntity.this.isSecondPhase();
            }
        }

        protected int getCastingTime() {
            return MainConfig.LavaballDuration.get();
        }

        protected int getCastingInterval() {
            return 0;
        }

        public void castSpell() {
            LivingEntity livingentity = ApostleEntity.this.getTarget();
            if (livingentity != null) {
                double d0 = ApostleEntity.this.distanceToSqr(livingentity);
                float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5F;
                double d1 = livingentity.getX() - ApostleEntity.this.getX();
                double d2 = livingentity.getY(0.5D) - ApostleEntity.this.getY(0.5D);
                double d3 = livingentity.getZ() - ApostleEntity.this.getZ();
                FireballEntity fireballEntity = new FireballEntity(ApostleEntity.this.level, ApostleEntity.this, d1, d2, d3);
                fireballEntity.setPos(fireballEntity.getX(), ApostleEntity.this.getY(0.5), fireballEntity.getZ());
                ApostleEntity.this.level.addFreshEntity(fireballEntity);
                if (!ApostleEntity.this.isSilent()) {
                    ApostleEntity.this.level.levelEvent(null, 1016, ApostleEntity.this.blockPosition(), 0);
                }
                ApostleEntity.this.teleport();
                ApostleEntity.this.cooldown = 0;
                ++ApostleEntity.this.spellcycle;
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

    class ZombieSpellGoal extends UseSpellGoal {
        private ZombieSpellGoal() {
        }

        @Override
        public boolean canUse() {
            int i = ApostleEntity.this.level.getNearbyEntities(MonsterEntity.class, ApostleEntity.this.zombieCount, ApostleEntity.this, ApostleEntity.this.getBoundingBox().inflate(64.0D)).size();
            if (!super.canUse()) {
                return false;
            }  else {
                int cool = ApostleEntity.this.isSecondPhase() ? 45 : 90;
                return ApostleEntity.this.cooldown >= cool && ApostleEntity.this.spellcycle == 2 && !ApostleEntity.this.isSettingupSecond() && i < 4;
            }
        }

        protected int getCastingTime() {
            return MainConfig.ZombieDuration.get();
        }

        protected int getCastingInterval() {
            return 0;
        }

        public void castSpell() {
            if (!ApostleEntity.this.level.isClientSide) {
                ServerWorld serverWorld = (ServerWorld) ApostleEntity.this.level;
                LivingEntity livingentity = ApostleEntity.this.getTarget();
                Random r = ApostleEntity.this.random;
                int random = r.nextInt(2);
                if (livingentity != null) {
                    BlockPos blockpos = ApostleEntity.this.blockPosition();
                    if (!ApostleEntity.this.isSecondPhase()){
                        if (random == 0) {
                            ZombieVillagerMinionEntity summonedentity = new ZombieVillagerMinionEntity(ModEntityType.ZOMBIE_VILLAGER_MINION.get(), ApostleEntity.this.level);
                            summonedentity.moveTo(blockpos, 0.0F, 0.0F);
                            summonedentity.setOwnerId(ApostleEntity.this.getUUID());
                            summonedentity.setLimitedLife(60 * (90 + ApostleEntity.this.level.random.nextInt(180)));
                            summonedentity.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
                            summonedentity.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                            summonedentity.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                            summonedentity.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.IRON_BOOTS));
                            summonedentity.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
                            summonedentity.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, Integer.MAX_VALUE, 1, false, false));
                            for (EquipmentSlotType equipmentslottype : EquipmentSlotType.values()) {
                                summonedentity.setDropChance(equipmentslottype, 0.0F);
                            }
                            summonedentity.finalizeSpawn(serverWorld, ApostleEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                            summonedentity.setTarget(livingentity);
                            ApostleEntity.this.level.addFreshEntity(summonedentity);
                        } else {
                            for (int p = 0; p < 3 + r.nextInt(2); ++p) {
                                int k = (12 + r.nextInt(12)) * (r.nextBoolean() ? -1 : 1);
                                int l = (12 + r.nextInt(12)) * (r.nextBoolean() ? -1 : 1);
                                BlockPos.Mutable blockpos$mutable = blockpos.mutable().move(k, 0, l);
                                blockpos$mutable.setX(blockpos$mutable.getX() + r.nextInt(5) - r.nextInt(5));
                                blockpos$mutable.setY(ApostleEntity.this.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
                                blockpos$mutable.setZ(blockpos$mutable.getZ() + r.nextInt(5) - r.nextInt(5));
                                ZombieVillagerMinionEntity summonedentity = new ZombieVillagerMinionEntity(ModEntityType.ZOMBIE_VILLAGER_MINION.get(), ApostleEntity.this.level);
                                summonedentity.moveTo(blockpos$mutable, 0.0F, 0.0F);
                                summonedentity.setOwnerId(ApostleEntity.this.getUUID());
                                summonedentity.setLimitedLife(60 * (90 + ApostleEntity.this.level.random.nextInt(180)));
                                summonedentity.finalizeSpawn(serverWorld, ApostleEntity.this.level.getCurrentDifficultyAt(blockpos$mutable), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                                summonedentity.setTarget(livingentity);
                                ApostleEntity.this.level.addFreshEntity(summonedentity);
                            }
                        }
                    } else {
                        if (random == 0) {
                            ZPiglinBruteMinionEntity summonedentity = new ZPiglinBruteMinionEntity(ModEntityType.ZPIGLIN_BRUTE_MINION.get(), ApostleEntity.this.level);
                            summonedentity.moveTo(blockpos, 0.0F, 0.0F);
                            summonedentity.setOwnerId(ApostleEntity.this.getUUID());
                            summonedentity.setLimitedLife(60 * (90 + ApostleEntity.this.level.random.nextInt(180)));
                            summonedentity.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, Integer.MAX_VALUE, 1, false, false));
                            for (EquipmentSlotType equipmentslottype : EquipmentSlotType.values()) {
                                summonedentity.setDropChance(equipmentslottype, 0.0F);
                            }
                            summonedentity.finalizeSpawn(serverWorld, ApostleEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                            summonedentity.setTarget(livingentity);
                            ApostleEntity.this.level.addFreshEntity(summonedentity);
                        } else {
                            for (int p = 0; p < 3 + r.nextInt(6); ++p) {
                                int k = (12 + r.nextInt(12)) * (r.nextBoolean() ? -1 : 1);
                                int l = (12 + r.nextInt(12)) * (r.nextBoolean() ? -1 : 1);
                                BlockPos.Mutable blockpos$mutable = ApostleEntity.this.blockPosition().mutable().move(k, 0, l);
                                blockpos$mutable.setX(blockpos$mutable.getX() + r.nextInt(5) - r.nextInt(5));
                                blockpos$mutable.setY(ApostleEntity.this.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
                                blockpos$mutable.setZ(blockpos$mutable.getZ() + r.nextInt(5) - r.nextInt(5));
                                ZPiglinMinionEntity summonedentity = new ZPiglinMinionEntity(ModEntityType.ZPIGLIN_MINION.get(), ApostleEntity.this.level);
                                summonedentity.moveTo(blockpos$mutable, 0.0F, 0.0F);
                                summonedentity.setOwnerId(ApostleEntity.this.getUUID());
                                summonedentity.setLimitedLife(60 * (90 + ApostleEntity.this.level.random.nextInt(180)));
                                summonedentity.finalizeSpawn(serverWorld, ApostleEntity.this.level.getCurrentDifficultyAt(blockpos$mutable), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                                summonedentity.setTarget(livingentity);
                                ApostleEntity.this.level.addFreshEntity(summonedentity);
                            }
                        }
                    }
                    ApostleEntity.this.teleport();
                    ApostleEntity.this.cooldown = 0;
                    ApostleEntity.this.spellcycle = 0;
                }
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

    class FireRainSpellGoal extends UseSpellGoal {
        private FireRainSpellGoal() {
        }

        @Override
        public boolean canUse() {
            int i = ApostleEntity.this.level.getNearbyEntities(MonsterEntity.class, ApostleEntity.this.zombieCount, ApostleEntity.this, ApostleEntity.this.getBoundingBox().inflate(64.0D)).size();
            int i2 = ApostleEntity.this.level.getEntitiesOfClass(FireRainTrapEntity.class, ApostleEntity.this.getBoundingBox().inflate(64.0D)).size();
            if (!super.canUse()) {
                return false;
            }  else {
                return ApostleEntity.this.cooldown >= 45 && ApostleEntity.this.spellcycle == 2 && !ApostleEntity.this.isSettingupSecond() && i > 4 && i2 < 2;
            }
        }

        protected int getCastingTime() {
            return MainConfig.ZombieDuration.get();
        }

        protected int getCastingInterval() {
            return 0;
        }

        public void castSpell() {
            if (!ApostleEntity.this.level.isClientSide) {
                LivingEntity livingentity = ApostleEntity.this.getTarget();
                if (livingentity != null) {
                    BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(livingentity.getX(), livingentity.getY(), livingentity.getZ());

                    while (blockpos$mutable.getY() > 0 && !ApostleEntity.this.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
                        blockpos$mutable.move(Direction.DOWN);
                    }
                    FireRainTrapEntity fireRainTrap = new FireRainTrapEntity(ModEntityType.FIRERAINTRAP.get(), ApostleEntity.this.level);
                    fireRainTrap.setPos(blockpos$mutable.getX(), blockpos$mutable.getY() + 1, blockpos$mutable.getZ());
                    fireRainTrap.setOwner(ApostleEntity.this);
                    fireRainTrap.setDuration(1200);
                    ApostleEntity.this.level.playSound(null, blockpos$mutable, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundCategory.HOSTILE, 1.0F, 0.5F);
                    ApostleEntity.this.level.addFreshEntity(fireRainTrap);
                    ApostleEntity.this.teleport();
                    ApostleEntity.this.cooldown = 0;
                    ApostleEntity.this.spellcycle = 0;
                }
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

    class SkeletonSpellGoal extends UseSpellGoal {
        private SkeletonSpellGoal() {
        }

        @Override
        public boolean canUse() {
            int i = ApostleEntity.this.level.getNearbyEntities(SkeletonVillagerMinionEntity.class, ApostleEntity.this.skeletonCount, ApostleEntity.this, ApostleEntity.this.getBoundingBox().inflate(64.0D)).size();
            if (!super.canUse()) {
                return false;
            } else {
                int c = ApostleEntity.this.isSecondPhase() ? 45 : 90;
                return ApostleEntity.this.cooldown >= c && ApostleEntity.this.spellcycle == 3 && !ApostleEntity.this.isSettingupSecond() && i < 4;
            }
        }

        protected int getCastingTime() {
            return MainConfig.SkeletonDuration.get();
        }

        protected int getCastingInterval() {
            return 0;
        }

        public void castSpell() {
            if (!ApostleEntity.this.level.isClientSide) {
                ServerWorld serverWorld = (ServerWorld) ApostleEntity.this.level;
                LivingEntity livingentity = ApostleEntity.this.getTarget();
                Random r = ApostleEntity.this.random;
                int random = r.nextInt(2);
                if (livingentity != null) {
                    if (random == 0) {
                        BlockPos blockpos = ApostleEntity.this.blockPosition();
                        SkeletonVillagerMinionEntity summonedentity = new SkeletonVillagerMinionEntity(ModEntityType.SKELETON_VILLAGER_MINION.get(), ApostleEntity.this.level);
                        summonedentity.moveTo(blockpos, 0.0F, 0.0F);
                        summonedentity.setOwnerId(ApostleEntity.this.getUUID());
                        summonedentity.setLimitedLife(60 * (90 + ApostleEntity.this.level.random.nextInt(180)));
                        summonedentity.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(Items.IRON_HELMET));
                        summonedentity.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
                        summonedentity.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(Items.IRON_LEGGINGS));
                        summonedentity.setItemSlot(EquipmentSlotType.FEET, new ItemStack(Items.IRON_BOOTS));
                        summonedentity.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, Integer.MAX_VALUE, 1, false, false));
                        for (EquipmentSlotType equipmentslottype : EquipmentSlotType.values()) {
                            summonedentity.setDropChance(equipmentslottype, 0.0F);
                        }
                        summonedentity.finalizeSpawn(serverWorld, ApostleEntity.this.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                        summonedentity.setTarget(livingentity);
                        ApostleEntity.this.level.addFreshEntity(summonedentity);
                    } else {
                        int z;
                        int z1;
                        if (ApostleEntity.this.isSecondPhase()){
                            z = 2;
                            z1 = 2;
                        } else {
                            z = 2;
                            z1 = 1;
                        }
                        for (int p = 0; p < z1 + r.nextInt(z); ++p) {
                            int k = (12 + r.nextInt(12)) * (r.nextBoolean() ? -1 : 1);
                            int l = (12 + r.nextInt(12)) * (r.nextBoolean() ? -1 : 1);
                            BlockPos.Mutable blockpos$mutable = ApostleEntity.this.blockPosition().mutable().move(k, 0, l);
                            blockpos$mutable.setX(blockpos$mutable.getX() + r.nextInt(5) - r.nextInt(5));
                            blockpos$mutable.setY(ApostleEntity.this.level.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
                            blockpos$mutable.setZ(blockpos$mutable.getZ() + r.nextInt(5) - r.nextInt(5));
                            SkeletonVillagerMinionEntity summonedentity = new SkeletonVillagerMinionEntity(ModEntityType.SKELETON_VILLAGER_MINION.get(), ApostleEntity.this.level);
                            summonedentity.moveTo(blockpos$mutable, 0.0F, 0.0F);
                            summonedentity.setOwnerId(ApostleEntity.this.getUUID());
                            summonedentity.setLimitedLife(60 * (90 + ApostleEntity.this.level.random.nextInt(180)));
                            summonedentity.finalizeSpawn(serverWorld, ApostleEntity.this.level.getCurrentDifficultyAt(blockpos$mutable), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                            summonedentity.setTarget(livingentity);
                            ApostleEntity.this.level.addFreshEntity(summonedentity);
                        }
                    }
                    ApostleEntity.this.teleport();
                    ApostleEntity.this.cooldown = 0;
                    ApostleEntity.this.spellcycle = 0;
                }
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

    class FireTornadoSpellGoal extends UseSpellGoal {
        private FireTornadoSpellGoal() {
        }

        @Override
        public boolean canUse() {
            int i = ApostleEntity.this.level.getNearbyEntities(MonsterEntity.class, ApostleEntity.this.skeletonCount, ApostleEntity.this, ApostleEntity.this.getBoundingBox().inflate(64.0D)).size();
            int i2 = ApostleEntity.this.level.getEntitiesOfClass(FireTornadoEntity.class, ApostleEntity.this.getBoundingBox().inflate(64.0D)).size();
            if (!super.canUse()) {
                return false;
            }  else {
                return ApostleEntity.this.cooldown >= 45 && ApostleEntity.this.spellcycle == 3 && !ApostleEntity.this.isSettingupSecond() && i > 4 && i2 < 1;
            }
        }

        protected int getCastingTime() {
            return MainConfig.SkeletonDuration.get();
        }

        protected int getCastingInterval() {
            return 0;
        }

        public void castSpell() {
            if (!ApostleEntity.this.level.isClientSide) {
                LivingEntity livingentity = ApostleEntity.this.getTarget();
                if (livingentity != null) {
                    BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(livingentity.getX(), livingentity.getY(), livingentity.getZ());

                    while (blockpos$mutable.getY() > 0 && !ApostleEntity.this.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
                        blockpos$mutable.move(Direction.DOWN);
                    }

                    FireTornadoTrapEntity fireTornadoTrapEntity = new FireTornadoTrapEntity(ModEntityType.FIRETORNADOTRAP.get(), ApostleEntity.this.level);
                    fireTornadoTrapEntity.setPos(blockpos$mutable.getX(), blockpos$mutable.getY() + 1, blockpos$mutable.getZ());
                    fireTornadoTrapEntity.setOwner(ApostleEntity.this);
                    fireTornadoTrapEntity.setDuration(60);
                    ApostleEntity.this.level.addFreshEntity(fireTornadoTrapEntity);
                    ApostleEntity.this.teleport();
                    ApostleEntity.this.cooldown = 0;
                    ApostleEntity.this.spellcycle = 0;
                }
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

    class RoarSpellGoal extends UseSpellGoal {
        private RoarSpellGoal() {
        }

        @Override
        public boolean canUse() {
            if (!super.canUse()) {
                return false;
            } else if (ApostleEntity.this.getTarget() == null) {
                return false;
            }  else return ApostleEntity.this.distanceTo(ApostleEntity.this.getTarget()) < 4.0F && !ApostleEntity.this.isSettingupSecond();
        }

        protected int getCastingTime() {
            return 200;
        }

        protected int getCastingInterval() {
            return 120;
        }

        public void castSpell() {
            ApostleEntity apostleEntity = ApostleEntity.this;
            if (!ApostleEntity.this.isSecondPhase()){
                apostleEntity.setFiring(true);
                apostleEntity.cooldown = 0;
                apostleEntity.playSound(ModSounds.ROAR_SPELL.get(), 1.0F, 1.0F);
            } else {
                double d0 = Math.min(ApostleEntity.this.getTarget().getY(), apostleEntity.getY());
                double d1 = Math.max(ApostleEntity.this.getTarget().getY(), apostleEntity.getY()) + 1.0D;
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
                fireBlastTrap.setOwner(ApostleEntity.this);
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
                    && !ApostleEntity.this.isSettingupSecond();
        }

        @Override
        public void tick() {
            ApostleEntity.this.teleport();
            ApostleEntity.this.setSettingupSecond(true);
        }
    }

    static class BowAttackGoal<T extends ApostleEntity & IRangedAttackMob> extends RangedBowAttackGoal<T>{
        private final T mob;

        public BowAttackGoal(T p_i47515_1_) {
            super(p_i47515_1_, 1.0D, 40, 30.0F);
            this.mob = p_i47515_1_;
        }

        @Override
        public boolean canUse() {
            return !this.mob.isSecondPhase() && this.mob.getTarget() != null && this.HaveBow() && !this.mob.isSettingupSecond();
        }

        protected boolean HaveBow() {
            return this.mob.isHolding(item -> item instanceof BowItem);
        }
    }

    static class FasterBowAttackGoal<T extends ApostleEntity & IRangedAttackMob> extends RangedBowAttackGoal<T>{
        private final T mob;

        public FasterBowAttackGoal(T p_i47515_1_) {
            super(p_i47515_1_, 1.0D, 20, 30.0F);
            this.mob = p_i47515_1_;
        }

        @Override
        public boolean canUse() {
            return this.mob.isSecondPhase() && this.mob.getHealth() > this.mob.getMaxHealth()/4 && this.mob.getTarget() != null && this.HaveBow() && !this.mob.isSettingupSecond();
        }

        protected boolean HaveBow() {
            return this.mob.isHolding(item -> item instanceof BowItem);
        }
    }

    static class FastestBowAttackGoal<T extends ApostleEntity & IRangedAttackMob> extends RangedBowAttackGoal<T>{
        private final T mob;

        public FastestBowAttackGoal(T p_i47515_1_) {
            super(p_i47515_1_, 1.0D, 5, 30.0F);
            this.mob = p_i47515_1_;
        }

        @Override
        public boolean canUse() {
            return this.mob.isSecondPhase() && this.mob.getHealth() <= this.mob.getMaxHealth()/4 && this.mob.getTarget() != null && this.HaveBow() && !this.mob.isSettingupSecond();
        }

        protected boolean HaveBow() {
            return this.mob.isHolding(item -> item instanceof BowItem);
        }
    }

}
