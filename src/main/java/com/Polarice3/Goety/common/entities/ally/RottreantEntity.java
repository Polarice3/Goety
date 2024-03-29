package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.AttributesConfig;
import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.entities.projectiles.RootTrapEntity;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.*;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class RottreantEntity extends SummonedEntity {
    private static final DataParameter<Integer> DATA_WOOD_TYPE = EntityDataManager.defineId(RottreantEntity.class, DataSerializers.INT);
    private static final DataParameter<Byte> FLAGS = EntityDataManager.defineId(RottreantEntity.class, DataSerializers.BYTE);
    public List<SummonedEntity> summoned = new ArrayList<>();
    public int mushroomGrow;

    public RottreantEntity(EntityType<? extends SummonedEntity> type, World worldIn) {
        super(type, worldIn);
        this.maxUpStep = 1.0F;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new RotAttackGoal(this));
        this.goalSelector.addGoal(8, new WanderGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(9, new RotLookGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new RotLookGoal(this, MobEntity.class, 8.0F));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, AttributesConfig.RottreantHealth.get())
                .add(Attributes.ARMOR, 0.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, AttributesConfig.RottreantDamage.get())
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D);
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_WOOD_TYPE, 0);
        this.entityData.define(FLAGS, (byte)0);
    }

    protected int decreaseAirSupply(int pAir) {
        return pAir;
    }

    public int getIntWoodType() {
        return this.entityData.get(DATA_WOOD_TYPE);
    }

    public RotTreeWoodType getWoodType(){
        return RotTreeWoodType.getType(this.getIntWoodType());
    }

    public void setWoodType(RotTreeWoodType rotTreeWoodType){
        this.setIntWoodType(rotTreeWoodType.id);
    }

    public void setIntWoodType(int pType) {
        this.entityData.set(DATA_WOOD_TYPE, pType);
    }

    private boolean getFlag(int mask) {
        int i = this.entityData.get(FLAGS);
        return (i & mask) != 0;
    }

    private void setFlags(int mask, boolean value) {
        int i = this.entityData.get(FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(FLAGS, (byte)(i & 255));
    }

    public boolean isMycelium() {
        return this.getFlag(1);
    }

    public void setMycelium(boolean mycelium) {
        this.setFlags(1, mycelium);
    }

    public boolean isReadyHarvest(){
        return this.getFlag(2);
    }

    public void setReadyHarvest(boolean harvest){
        this.setFlags(2, harvest);
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("MushroomGrow", this.mushroomGrow);
        pCompound.putInt("WoodType", this.getIntWoodType());
        pCompound.putBoolean("Mycelium", this.isMycelium());
        pCompound.putBoolean("ReadyHarvest", this.isReadyHarvest());
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.mushroomGrow = pCompound.getInt("MushroomGrow");
        this.setIntWoodType(pCompound.getInt("WoodType"));
        this.setMycelium(pCompound.getBoolean("Mycelium"));
        this.setReadyHarvest(pCompound.getBoolean("ReadyHarvest"));
    }

    public boolean causeFallDamage(float p_225503_1_, float p_225503_2_) {
        return false;
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return ModSounds.ROT_TREE_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        if (pDamageSource.getDirectEntity() instanceof AbstractArrowEntity){
            return SoundEvents.SHIELD_BLOCK;
        }
        return ModSounds.ROT_TREE_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.ROT_TREE_DEATH.get();
    }

    protected ResourceLocation getDefaultLootTable() {
        switch (this.getWoodType()){
            case MURK:
                return ModLootTables.ROTTREANT_MURK;
            case GLOOM:
                return ModLootTables.ROTTREANT_GLOOM;
            default:
                return ModLootTables.ROTTREANT_HAUNTED;
        }
    }

    public boolean summonLimit(){
        return this.summoned.size() < SpellConfig.RottreantBugLimit.get();
    }

    @Override
    public boolean hurt(@Nonnull DamageSource source, float amount) {
        boolean felChance = this.level.random.nextFloat() <= 0.25F;
        boolean spiderChance = false;
        if (this.getTrueOwner() != null) {
            if (RobeArmorFinder.FindFelArmor(this.getTrueOwner())) {
                felChance = this.level.random.nextBoolean();
                spiderChance = this.level.random.nextBoolean();
            }
        }
        if (this.getHealth() < this.getMaxHealth()/2){
            felChance = this.level.random.nextFloat() <= 0.75F;
            if (this.isStaying()){
                this.setStaying(false);
                this.setWandering(false);
            }
        }
        if (source.getDirectEntity() instanceof AbstractArrowEntity || this.isStaying()){
            amount /= 2.0F;
        }
        if (ModDamageSource.physicalAttacks(source)){
            LivingEntity livingEntity = (LivingEntity) source.getDirectEntity();
            if (livingEntity != null) {
                if (livingEntity.getMainHandItem() != ItemStack.EMPTY && livingEntity.getMainHandItem().getItem() instanceof AxeItem) {
                    amount *= 2.0F;
                    felChance = true;
                    this.playSound(ModSounds.ROT_TREE_HEAVY_HURT.get(), 1.0F, 1.0F);
                    if (this.isReadyHarvest()) {
                        this.harvestMushrooms();
                    }
                }
            }
        }
        if (this.summonLimit()) {
            if (felChance) {
                FelFlyEntity felFly = ModEntityType.FEL_FLY.get().create(this.level);
                if (felFly != null) {
                    felFly.setPos(this.getX(), this.getY(), this.getZ());
                    felFly.setTrueOwner(this);
                    if (source.getEntity() != null && source.getEntity() instanceof LivingEntity && source.getEntity() != this.getTrueOwner() && source.getEntity() != this) {
                        felFly.setTarget((LivingEntity) source.getEntity());
                    }
                    felFly.setLimitedLife(200);
                    this.playSound(ModSounds.ROT_TREE_EXIT.get(), 1.0F, 1.0F);
                    this.level.addFreshEntity(felFly);
                }
            }
            if (spiderChance) {
                SpiderlingMinionEntity spiderlingMinion = ModEntityType.SPIDERLING_MINION.get().create(this.level);
                if (spiderlingMinion != null) {
                    spiderlingMinion.setPos(this.getX(), this.getY(), this.getZ());
                    spiderlingMinion.setTrueOwner(this);
                    if (source.getEntity() != null && source.getEntity() instanceof LivingEntity && source.getEntity() != this.getTrueOwner() && source.getEntity() != this) {
                        spiderlingMinion.setTarget((LivingEntity) source.getEntity());
                    }
                    spiderlingMinion.setLimitedLife(200);
                    this.playSound(ModSounds.ROT_TREE_EXIT.get(), 1.0F, 1.0F);
                    this.level.addFreshEntity(spiderlingMinion);
                }
            }
        }
        if (this.level.random.nextFloat() <= 0.25F){
            if (this.isReadyHarvest()){
                this.harvestMushrooms();
            }
        }
        return super.hurt(source, amount);
    }

    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = super.doHurtTarget(pEntity);
        if (flag){
            this.playSound(ModSounds.ROT_TREE_ATTACK.get(), 1.0F, 1.0F);
            double area = 1.0D;
            for (LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(area, 0.25D, area))) {
                if (livingentity != this && livingentity != pEntity && !this.isAlliedTo(livingentity) && (!(livingentity instanceof ArmorStandEntity) || !((ArmorStandEntity) livingentity).isMarker()) && this.distanceToSqr(livingentity) < 16.0D) {
                    livingentity.knockback(0.4F, (double) MathHelper.sin(this.yRot * ((float) Math.PI / 180F)), (double) (-MathHelper.cos(this.yRot * ((float) Math.PI / 180F))));
                    livingentity.hurt(DamageSource.mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                }
            }
        }
        return flag;
    }

    @Override
    public boolean canUpdateMove() {
        return true;
    }

    public void push(Entity pEntity) {
        if (!this.isStaying()){
            super.push(pEntity);
        }
    }

    public void knockback(float p_233627_1_, double p_233627_2_, double p_233627_4_) {
        if (!this.isStaying()){
            super.knockback(p_233627_1_, p_233627_2_, p_233627_4_);
        }
    }

    public void aiStep() {
        super.aiStep();
        boolean felArmor = this.getTrueOwner() != null && RobeArmorFinder.FindFelArmor(this.getTrueOwner());
        if (this.getTrueOwner() != null && !this.isStaying()){
            for (MobEntity mobEntity : this.level.getEntitiesOfClass(MobEntity.class, this.getBoundingBox().inflate(16))){
                if (mobEntity.getTarget() != null && mobEntity.getTarget() == this.getTrueOwner() && mobEntity.getTarget().isAlive()){
                    mobEntity.setTarget(this);
                }
            }
            if (RobeArmorFinder.FindFelBootsofWander(this.getTrueOwner())){
                this.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 100, 0, false, false, false));
            }
        }
        if (this.isMycelium()){
            this.addEffect(new EffectInstance(ModEffects.SAPPED.get(), 100, 0, false, false, false));
            if (!this.isReadyHarvest()) {
                ++this.mushroomGrow;
                int minutes = felArmor ? 2 : 5;
                if (this.mushroomGrow >= ModMathHelper.minutesToTicks(minutes)) {
                    this.setReadyHarvest(true);
                    this.mushroomGrow = 0;
                    this.playSound(SoundEvents.ROOTS_PLACE, 1.0F, 1.0F);
                    if (this.level.isClientSide) {
                        this.addParticlesAroundSelf(ParticleTypes.MYCELIUM);
                    }
                }
            }
            if (this.level.random.nextInt(25) == 0 && this.tickCount % ModMathHelper.secondsToTicks(30) == 0) {
                int i = 5;
                int j = 4;
                BlockPos pPos = this.blockPosition();
                BlockState blockState = Blocks.RED_MUSHROOM.defaultBlockState();
                if (this.level.random.nextBoolean()) {
                    blockState = Blocks.BROWN_MUSHROOM.defaultBlockState();
                }

                for (BlockPos blockpos : BlockPos.betweenClosed(pPos.offset(-j, -1, -j), pPos.offset(j, 1, j))) {
                    if (this.level.getBlockState(blockpos).is(blockState.getBlock())) {
                        --i;
                        if (i <= 0) {
                            return;
                        }
                    }
                }

                BlockPos blockpos1 = pPos.offset(this.level.random.nextInt(3) - 1, this.level.random.nextInt(2) - this.level.random.nextInt(2), this.level.random.nextInt(3) - 1);

                for (int k = 0; k < 4; ++k) {
                    if (this.level.isEmptyBlock(blockpos1) && blockState.canSurvive(this.level, blockpos1)) {
                        pPos = blockpos1;
                    }

                    blockpos1 = pPos.offset(this.level.random.nextInt(3) - 1, this.level.random.nextInt(2) - this.level.random.nextInt(2), this.level.random.nextInt(3) - 1);
                }

                if (this.level.isEmptyBlock(blockpos1) && blockState.canSurvive(this.level, blockpos1)) {
                    this.level.setBlock(blockpos1, blockState, 2);
                }
            }
        }
        for (SummonedEntity summonedEntity : this.level.getEntitiesOfClass(SummonedEntity.class, this.getBoundingBox().inflate(16))){
            if (summonedEntity.getTrueOwner() == this && !summonedEntity.isDeadOrDying() && !summonedEntity.removed && !this.summoned.contains(summonedEntity)){
                this.summoned.add(summonedEntity);
            }
        }
        if (!this.summoned.isEmpty()) {
            this.summoned.removeIf(summonedEntity -> summonedEntity.removed || summonedEntity.isDeadOrDying() || !summonedEntity.isAlive());
        }
        if (this.isStaying()){
            this.setDeltaMovement(0, this.getDeltaMovement().y(), 0);
            if (!this.level.isClientSide) {
                if (this.getTarget() != null && this.getTarget().isAlive()) {
                    if (this.tickCount % 100 == 0){
                        if (this.level.random.nextFloat() <= 0.25F && this.getTarget().distanceTo(this) <= 16.0F){
                            RootTrapEntity rootTrap = new RootTrapEntity(this.level, BlockFinder.fangSpawnPosition(this.getTarget()), 1, this);
                            this.level.addFreshEntity(rootTrap);
                        }
                    }
                    if (this.summonLimit()) {
                        if (this.tickCount % 20 == 0) {
                            if (this.level.random.nextBoolean()) {
                                FelFlyEntity felFly = ModEntityType.FEL_FLY.get().create(this.level);
                                if (felFly != null) {
                                    felFly.setPos(this.getX(), this.getY(), this.getZ());
                                    felFly.setTrueOwner(this);
                                    if (this.getTarget() != null) {
                                        felFly.setTarget(this.getTarget());
                                    }
                                    felFly.setLimitedLife(200);
                                    this.playSound(ModSounds.ROT_TREE_EXIT.get(), 1.0F, 1.0F);
                                    this.level.addFreshEntity(felFly);
                                }
                            }
                        }
                        if (this.tickCount % 25 == 0) {
                            if (felArmor) {
                                if (this.level.random.nextBoolean()) {
                                    SpiderlingMinionEntity spiderlingMinion = ModEntityType.SPIDERLING_MINION.get().create(this.level);
                                    if (spiderlingMinion != null) {
                                        spiderlingMinion.setPos(this.getX(), this.getY(), this.getZ());
                                        spiderlingMinion.setTrueOwner(this);
                                        if (this.getTarget() != null) {
                                            spiderlingMinion.setTarget(this.getTarget());
                                        }
                                        spiderlingMinion.setLimitedLife(200);
                                        this.playSound(ModSounds.ROT_TREE_EXIT.get(), 1.0F, 1.0F);
                                        this.level.addFreshEntity(spiderlingMinion);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (this.getWoodType() == RotTreeWoodType.HAUNTED){
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(8.0F);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.23F);
            this.getAttribute(Attributes.ARMOR).setBaseValue(4.0F);
        }
        if (getHorizontalDistanceSqr(this.getDeltaMovement()) > (double)2.5000003E-7F && this.random.nextInt(5) == 0) {
            int i = MathHelper.floor(this.getX());
            int j = MathHelper.floor(this.getY() - (double)0.2F);
            int k = MathHelper.floor(this.getZ());
            BlockPos pos = new BlockPos(i, j, k);
            BlockState blockstate = this.level.getBlockState(pos);
            if (!blockstate.isAir(this.level, pos)) {
                this.level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockstate).setPos(pos), this.getX() + ((double)this.random.nextFloat() - 0.5D) * (double)this.getBbWidth(), this.getY() + 0.1D, this.getZ() + ((double)this.random.nextFloat() - 0.5D) * (double)this.getBbWidth(), 4.0D * ((double)this.random.nextFloat() - 0.5D), 0.5D, ((double)this.random.nextFloat() - 0.5D) * 4.0D);
            }
        }
        float healAmount = felArmor ? 5.0F : 2.0F;
        int time = this.isStaying() ? ModMathHelper.secondsToTicks(5) : ModMathHelper.secondsToTicks(15);
        if ((this.level.isDay() && (this.level.canSeeSky(this.blockPosition()) || this.getBlockStateOn() instanceof IGrowable))) {
            if (this.tickCount % time == 0) {
                if (this.getHealth() < this.getMaxHealth()) {
                    this.heal(healAmount);
                    this.addParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER);
                }
            }
        } else if (this.level.isRainingAt(this.blockPosition())){
            if (this.tickCount % time == 0){
                if (this.getHealth() < this.getMaxHealth()){
                    this.heal(healAmount);
                    this.addParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER);
                }
            }
        } else if (isNearWater(this.level, this.blockPosition())) {
            if (this.tickCount % time == 0) {
                if (this.getHealth() < this.getMaxHealth()) {
                    this.heal(healAmount);
                    this.addParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER);
                }
            }
        } else if (this.isInWater() || this.isUnderWater()){
            if (this.tickCount % ModMathHelper.secondsToTicks(15) == 0){
                if (this.getHealth() < this.getMaxHealth()){
                    this.heal(healAmount);
                    this.addParticlesAroundSelf(ParticleTypes.HAPPY_VILLAGER);
                }
            }
        }

    }

    private static boolean isNearWater(IWorldReader pLevel, BlockPos pPos) {
        if (!pLevel.isClientSide()) {
            for (BlockPos blockpos : BlockPos.betweenClosed(pPos.offset(-4, 0, -4), pPos.offset(4, 1, 4))) {
                if (pLevel.getFluidState(blockpos).is(FluidTags.WATER)) {
                    return true;
                }
            }

            return net.minecraftforge.common.FarmlandWaterManager.hasBlockWaterTicket(pLevel, pPos);
        }
        return false;
    }

    public void stayingPosition() {
    }

    @OnlyIn(Dist.CLIENT)
    protected void addParticlesAroundSelf(IParticleData pParticleData) {
        for(int i = 0; i < 5; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(pParticleData, this.getRandomX(1.0D), this.getRandomY() + 1.0D, this.getRandomZ(1.0D), d0, d1, d2);
        }

    }

    public boolean getWoodRepair(Item item, RotTreeWoodType rotTreeWoodType){
        switch (rotTreeWoodType){
            case MURK:
                return item == ModBlocks.MURK_LOG_ITEM.get() || item == ModBlocks.MURK_WOOD_ITEM.get() || item == ModBlocks.STRIPPED_MURK_LOG_ITEM.get() || item == ModBlocks.STRIPPED_MURK_WOOD_ITEM.get();
            case GLOOM:
                return item == ModBlocks.GLOOM_LOG_ITEM.get() || item == ModBlocks.GLOOM_WOOD_ITEM.get() || item == ModBlocks.STRIPPED_GLOOM_LOG_ITEM.get() || item == ModBlocks.STRIPPED_GLOOM_LOG_ITEM.get();
            default:
                return item == ModBlocks.HAUNTED_LOG_ITEM.get() || item == ModBlocks.HAUNTED_WOOD_ITEM.get() || item == ModBlocks.STRIPPED_HAUNTED_LOG_ITEM.get() || item == ModBlocks.STRIPPED_HAUNTED_WOOD_ITEM.get();
        }
    }

    protected ActionResultType mobInteract(PlayerEntity pPlayer, Hand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        Item item = itemstack.getItem();
        if (this.getWoodRepair(item, this.getWoodType())) {
            float f = this.getHealth();
            this.heal(10.0F);
            if (this.getHealth() == f) {
                return ActionResultType.PASS;
            } else {
                float f1 = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
                this.playSound(ModSounds.ROT_TREE_REPAIR.get(), 1.0F, f1);
                if (!pPlayer.abilities.instabuild) {
                    itemstack.shrink(1);
                }

                return ActionResultType.sidedSuccess(this.level.isClientSide);
            }
        }
        if (item instanceof BoneMealItem){
            if (this.isMycelium()){
                if (!this.isReadyHarvest()) {
                    this.mushroomGrow -= ModMathHelper.minutesToTicks(1);
                    if (this.level.isClientSide) {
                        this.addParticlesAroundSelf(ParticleTypes.MYCELIUM);
                    }
                    if (!pPlayer.abilities.instabuild) {
                        itemstack.shrink(1);
                    }

                    return ActionResultType.sidedSuccess(this.level.isClientSide);
                }
            }
        }
        if (item == Items.ROTTEN_FLESH) {
            float f = this.getHealth();
            this.heal(1.0F);
            if (this.getHealth() == f) {
                return ActionResultType.PASS;
            } else {
                float f1 = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
                this.playSound(ModSounds.ROT_TREE_ROT_REPAIR.get(), 1.0F, f1);
                if (!pPlayer.abilities.instabuild) {
                    itemstack.shrink(1);
                }

                return ActionResultType.sidedSuccess(this.level.isClientSide);
            }
        }
        if (item instanceof ShearsItem){
            if (this.isReadyHarvest()){
                this.harvestMushrooms();
                this.playSound(SoundEvents.MOOSHROOM_SHEAR, 1.0F, 1.0F);
                ItemHelper.hurtAndBreak(itemstack, 1, pPlayer);
                return ActionResultType.sidedSuccess(this.level.isClientSide);
            }
        }
        if (item == Items.MYCELIUM){
            if (!this.isMycelium()){
                this.setMycelium(true);
                this.playSound(SoundEvents.GRASS_BREAK, 1.0F, 1.0F);
                if (!pPlayer.abilities.instabuild) {
                    itemstack.shrink(1);
                }
                return ActionResultType.sidedSuccess(this.level.isClientSide);
            }
        }
        return ActionResultType.PASS;
    }

    public void harvestMushrooms(){
        this.setReadyHarvest(false);
        for(int i = 0; i < 2; ++i) {
            this.spawnAtLocation(new ItemStack(Items.RED_MUSHROOM), 0.5F);
            this.spawnAtLocation(new ItemStack(Items.BROWN_MUSHROOM), 0.5F);
        }
    }

    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(ModSounds.ROT_TREE_STEP.get(), 0.5F, 1.0F);
    }

    public RottreantEntity.Cracks getCracks() {
        return RottreantEntity.Cracks.byFraction(this.getHealth() / this.getMaxHealth());
    }

    static class RotLookGoal extends LookAtGoal{
        public RottreantEntity rottreantEntity;

        public RotLookGoal(RottreantEntity p_i1631_1_, Class<? extends LivingEntity> p_i1631_2_, float p_i1631_3_) {
            super(p_i1631_1_, p_i1631_2_, p_i1631_3_);
            this.rottreantEntity = p_i1631_1_;
        }

        public RotLookGoal(RottreantEntity p_i1632_1_, Class<? extends LivingEntity> p_i1632_2_, float p_i1632_3_, float p_i1632_4_) {
            super(p_i1632_1_, p_i1632_2_, p_i1632_3_, p_i1632_4_);
            this.rottreantEntity = p_i1632_1_;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.rottreantEntity.isStaying();
        }
    }

    static class RotAttackGoal extends MeleeAttackGoal{
        public RottreantEntity rottreantEntity;

        public RotAttackGoal(RottreantEntity p_i1631_1_) {
            super(p_i1631_1_, 1.0D, false);
            this.rottreantEntity = p_i1631_1_;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.rottreantEntity.isStaying();
        }
    }

    public enum RotTreeWoodType {
        HAUNTED(0),
        MURK(1),
        GLOOM(2);
        private final int id;

        RotTreeWoodType(int id){
            this.id = id;
        }

        public static RotTreeWoodType getType(int idIn){
            for(RottreantEntity.RotTreeWoodType rotTreeWoodType : values()) {
                if (idIn == rotTreeWoodType.id) {
                    return rotTreeWoodType;
                }
            }

            return HAUNTED;
        }
    }

    public enum Cracks {
        NONE(1.0F),
        LOW(0.75F),
        MEDIUM(0.5F),
        HIGH(0.25F);

        private static final List<RottreantEntity.Cracks> BY_DAMAGE = Stream.of(values()).sorted(Comparator.comparingDouble((p_226516_0_) -> {
            return (double)p_226516_0_.fraction;
        })).collect(ImmutableList.toImmutableList());
        private final float fraction;

        Cracks(float p_i225732_3_) {
            this.fraction = p_i225732_3_;
        }

        public static RottreantEntity.Cracks byFraction(float p_226515_0_) {
            for(RottreantEntity.Cracks irongolementity$cracks : BY_DAMAGE) {
                if (p_226515_0_ < irongolementity$cracks.fraction) {
                    return irongolementity$cracks;
                }
            }

            return NONE;
        }
    }
}
