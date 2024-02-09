package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.AttributesConfig;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.ai.CreatureZombieAttackGoal;
import com.Polarice3.Goety.common.items.magic.SoulWand;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.EntityFinder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Objects;
import java.util.UUID;

public class ZombieMinionEntity extends SummonedEntity {
    private static final UUID SPEED_MODIFIER_BABY_UUID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
    private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_UUID, "Baby speed boost", 0.5D, AttributeModifier.Operation.MULTIPLY_BASE);
    private static final DataParameter<Boolean> DATA_BABY_ID = EntityDataManager.defineId(ZombieMinionEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> DATA_DROWNED_CONVERSION_ID = EntityDataManager.defineId(ZombieMinionEntity.class, DataSerializers.BOOLEAN);
    private int inWaterTime;
    private int conversionTime;

    public ZombieMinionEntity(EntityType<? extends SummonedEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected boolean isSunSensitive() {
        return true;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new CreatureZombieAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(8, new WanderGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, AttributesConfig.ZombieServantHealth.get())
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.23F)
                .add(Attributes.ATTACK_DAMAGE, AttributesConfig.ZombieServantDamage.get())
                .add(Attributes.ARMOR, AttributesConfig.ZombieServantArmor.get());
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_BABY_ID, false);
        this.getEntityData().define(DATA_DROWNED_CONVERSION_ID, false);
    }

    public boolean isUnderWaterConverting() {
        return this.getEntityData().get(DATA_DROWNED_CONVERSION_ID);
    }

    public boolean isBaby() {
        return this.getEntityData().get(DATA_BABY_ID);
    }

    public void setBaby(boolean pChildZombie) {
        this.getEntityData().set(DATA_BABY_ID, pChildZombie);
        if (this.level != null && !this.level.isClientSide) {
            ModifiableAttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            modifiableattributeinstance.removeModifier(SPEED_MODIFIER_BABY);
            if (pChildZombie) {
                modifiableattributeinstance.addTransientModifier(SPEED_MODIFIER_BABY);
            }
        }

    }

    public void onSyncedDataUpdated(DataParameter<?> pKey) {
        if (DATA_BABY_ID.equals(pKey)) {
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(pKey);
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("IsBaby", this.isBaby());
        pCompound.putInt("InWaterTime", this.isInWater() ? this.inWaterTime : -1);
        pCompound.putInt("DrownedConversionTime", this.isUnderWaterConverting() ? this.conversionTime : -1);
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setBaby(pCompound.getBoolean("IsBaby"));
        this.inWaterTime = pCompound.getInt("InWaterTime");
        if (pCompound.contains("DrownedConversionTime", 99) && pCompound.getInt("DrownedConversionTime") > -1) {
            this.startUnderWaterConversion(pCompound.getInt("DrownedConversionTime"));
        }
    }

    protected float getStandingEyeHeight(Pose pPose, EntitySize pSize) {
        return this.isBaby() ? 0.93F : 1.74F;
    }

    public double getMyRidingOffset() {
        return this.isBaby() ? 0.0D : -0.45D;
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

    protected boolean convertsInWater() {
        return true;
    }

    public void tick() {
        if (!this.level.isClientSide && this.isAlive() && !this.isNoAi()) {
            if (this.isUnderWaterConverting()) {
                --this.conversionTime;

                if (this.conversionTime < 0 && net.minecraftforge.event.ForgeEventFactory.canLivingConvert(this, ModEntityType.ZOMBIE_MINION.get(), (timer) -> this.conversionTime = timer)) {
                    this.doUnderWaterConversion();
                }
            } else if (this.convertsInWater()) {
                if (this.isEyeInFluid(FluidTags.WATER)) {
                    ++this.inWaterTime;
                    if (this.inWaterTime >= 600) {
                        this.startUnderWaterConversion(300);
                    }
                } else {
                    this.inWaterTime = -1;
                }
            }
        }

        super.tick();
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

    public EntityType<?> getVariant(World level, BlockPos blockPos){
        EntityType<?> entityType = ModEntityType.ZOMBIE_MINION.get();
        if (level instanceof ServerWorld) {
            ServerWorld serverLevel = (ServerWorld) level;
            if (level.isWaterAt(blockPos)) {
                entityType = ModEntityType.DROWNED_MINION.get();
            } else if (level.getBiome(blockPos).getBiomeCategory() == Biome.Category.DESERT && level.canSeeSky(blockPos)) {
                entityType = ModEntityType.HUSK_MINION.get();
            } else if (level.dimension() == World.NETHER) {
                EntityType<?> entityType1 = ModEntityType.ZPIGLIN_MINION.get();
                if (level.random.nextFloat() <= 0.25F && BlockFinder.findStructure(serverLevel, blockPos, Structure.BASTION_REMNANT)) {
                    entityType1 = ModEntityType.ZPIGLIN_BRUTE_MINION.get();
                }
                entityType = entityType1;
            } else if (BlockFinder.findStructure(serverLevel, blockPos, Structure.WOODLAND_MANSION)) {
                entityType = ModEntityType.ZOMBIE_VINDICATOR.get();
            } else if (level.getBiome(blockPos).getTemperature(blockPos) < 0.15F) {
                entityType = ModEntityType.FROZEN_ZOMBIE_MINION.get();
            } else if (level.getBiome(blockPos).getBiomeCategory() == Biome.Category.JUNGLE && level.random.nextBoolean()) {
                entityType = ModEntityType.JUNGLE_ZOMBIE_MINION.get();
            }
        }
        return entityType;
    }

    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        float f = difficultyIn.getSpecialMultiplier();
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
        float random = this.level.random.nextFloat();
        if (this.isUpgraded() && killedEntity instanceof ZombieEntity && random <= 0.5F && net.minecraftforge.event.ForgeEventFactory.canLivingConvert(killedEntity, ModEntityType.ZOMBIE_MINION.get(), (timer) -> {})) {
            ZombieEntity zombieEntity = (ZombieEntity)killedEntity;
            EntityType<? extends MobEntity> entityType = ModEntityType.ZOMBIE_MINION.get();
            if (zombieEntity instanceof HuskEntity){
                entityType = ModEntityType.HUSK_MINION.get();
            } else if (zombieEntity instanceof DrownedEntity){
                entityType = ModEntityType.DROWNED_MINION.get();
            }
            ZombieMinionEntity zombieMinionEntity = (ZombieMinionEntity) zombieEntity.convertTo(entityType, false);
            if (zombieMinionEntity != null) {
                zombieMinionEntity.finalizeSpawn(world, level.getCurrentDifficultyAt(zombieMinionEntity.blockPosition()), SpawnReason.CONVERSION, null, (CompoundNBT) null);
                zombieMinionEntity.setLimitedLife(10 * (15 + this.level.random.nextInt(45)));
                if (this.getTrueOwner() != null){
                    zombieMinionEntity.setTrueOwner(this.getTrueOwner());
                }
                net.minecraftforge.event.ForgeEventFactory.onLivingConvert(killedEntity, zombieMinionEntity);
                if (!this.isSilent()) {
                    world.levelEvent((PlayerEntity) null, 1026, this.blockPosition(), 0);
                }
            }
        }

    }

    public void die(DamageSource pCause) {
        if (!this.level.isClientSide && this.hasCustomName() && this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getTrueOwner() instanceof ServerPlayerEntity) {
            this.getTrueOwner().sendMessage(this.getCombatTracker().getDeathMessage(), Util.NIL_UUID);
        }
        super.die(pCause);
    }

    private void startUnderWaterConversion(int p_204704_1_) {
        this.conversionTime = p_204704_1_;
        this.getEntityData().set(DATA_DROWNED_CONVERSION_ID, true);
    }

    protected void doUnderWaterConversion() {
        this.convertToZombieType(ModEntityType.DROWNED_MINION.get());
        if (!this.isSilent()) {
            this.level.levelEvent((PlayerEntity)null, 1040, this.blockPosition(), 0);
        }

    }

    protected void convertToZombieType(EntityType<? extends ZombieMinionEntity> p_234341_1_) {
        ZombieMinionEntity zombieentity = this.convertTo(p_234341_1_, true);
        if (zombieentity != null) {
            zombieentity.handleAttributes(zombieentity.level.getCurrentDifficultyAt(zombieentity.blockPosition()).getSpecialMultiplier());
            if (this.getTrueOwner() != null) {
                zombieentity.setTrueOwner(this.getTrueOwner());
            }
            net.minecraftforge.event.ForgeEventFactory.onLivingConvert(this, zombieentity);
        }

    }

    public ActionResultType mobInteract(PlayerEntity pPlayer, Hand p_230254_2_) {
        if (!this.level.isClientSide){
            ItemStack itemstack = pPlayer.getItemInHand(p_230254_2_);
            Item item = itemstack.getItem();
            ItemStack itemstack2 = this.getMainHandItem();
            if (this.getTrueOwner() != null && pPlayer == this.getTrueOwner() && !pPlayer.isShiftKeyDown() && !pPlayer.isCrouching()) {
                if (item == Items.ROTTEN_FLESH && this.getHealth() < this.getMaxHealth()) {
                    if (!pPlayer.abilities.instabuild) {
                        itemstack.shrink(1);
                    }
                    this.playSound(SoundEvents.GENERIC_EAT, 1.0F, 1.0F);
                    this.heal(2.0F);
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        this.level.addParticle(ModParticleTypes.HEAL_EFFECT.get(), this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                    }
                    return ActionResultType.CONSUME;
                }
                if (!(pPlayer.getOffhandItem().getItem() instanceof SoulWand)) {
                    if (item instanceof SwordItem) {
                        this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
                        this.setItemSlot(EquipmentSlotType.MAINHAND, itemstack.copy());
                        this.setGuaranteedDrop(EquipmentSlotType.MAINHAND);
                        this.spawnAtLocation(itemstack2);
                        for (int i = 0; i < 7; ++i) {
                            double d0 = this.random.nextGaussian() * 0.02D;
                            double d1 = this.random.nextGaussian() * 0.02D;
                            double d2 = this.random.nextGaussian() * 0.02D;
                            this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                        }
                        if (!pPlayer.abilities.instabuild) {
                            itemstack.shrink(1);
                        }
                        EntityFinder.sendEntityUpdatePacket(pPlayer, this);
                        return ActionResultType.CONSUME;
                    }
                    if (item instanceof AxeItem) {
                        this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
                        this.setItemSlot(EquipmentSlotType.MAINHAND, itemstack.copy());
                        this.setGuaranteedDrop(EquipmentSlotType.MAINHAND);
                        this.spawnAtLocation(itemstack2);
                        for (int i = 0; i < 7; ++i) {
                            double d0 = this.random.nextGaussian() * 0.02D;
                            double d1 = this.random.nextGaussian() * 0.02D;
                            double d2 = this.random.nextGaussian() * 0.02D;
                            this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                        }
                        if (!pPlayer.abilities.instabuild) {
                            itemstack.shrink(1);
                        }
                        EntityFinder.sendEntityUpdatePacket(pPlayer, this);
                        return ActionResultType.CONSUME;
                    }
                    if (item instanceof TridentItem && this instanceof DrownedMinionEntity) {
                        this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
                        this.setItemSlot(EquipmentSlotType.MAINHAND, itemstack.copy());
                        this.setGuaranteedDrop(EquipmentSlotType.MAINHAND);
                        this.spawnAtLocation(itemstack2);
                        for (int i = 0; i < 7; ++i) {
                            double d0 = this.random.nextGaussian() * 0.02D;
                            double d1 = this.random.nextGaussian() * 0.02D;
                            double d2 = this.random.nextGaussian() * 0.02D;
                            this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                        }
                        if (!pPlayer.abilities.instabuild) {
                            itemstack.shrink(1);
                        }
                        EntityFinder.sendEntityUpdatePacket(pPlayer, this);
                        return ActionResultType.CONSUME;
                    }
                }
                if (item instanceof ArmorItem) {
                    ItemStack helmet = this.getItemBySlot(EquipmentSlotType.HEAD);
                    ItemStack chestplate = this.getItemBySlot(EquipmentSlotType.CHEST);
                    ItemStack legging = this.getItemBySlot(EquipmentSlotType.LEGS);
                    ItemStack boots = this.getItemBySlot(EquipmentSlotType.FEET);
                    this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
                    if (((ArmorItem) item).getSlot() == EquipmentSlotType.HEAD) {
                        this.setItemSlot(EquipmentSlotType.HEAD, itemstack.copy());
                        this.setGuaranteedDrop(EquipmentSlotType.HEAD);
                        this.spawnAtLocation(helmet);
                    }
                    if (((ArmorItem) item).getSlot() == EquipmentSlotType.CHEST) {
                        this.setItemSlot(EquipmentSlotType.CHEST, itemstack.copy());
                        this.setGuaranteedDrop(EquipmentSlotType.CHEST);
                        this.spawnAtLocation(chestplate);
                    }
                    if (((ArmorItem) item).getSlot() == EquipmentSlotType.LEGS) {
                        this.setItemSlot(EquipmentSlotType.LEGS, itemstack.copy());
                        this.setGuaranteedDrop(EquipmentSlotType.LEGS);
                        this.spawnAtLocation(legging);
                    }
                    if (((ArmorItem) item).getSlot() == EquipmentSlotType.FEET) {
                        this.setItemSlot(EquipmentSlotType.FEET, itemstack.copy());
                        this.setGuaranteedDrop(EquipmentSlotType.FEET);
                        this.spawnAtLocation(boots);
                    }
                    for (int i = 0; i < 7; ++i) {
                        double d0 = this.random.nextGaussian() * 0.02D;
                        double d1 = this.random.nextGaussian() * 0.02D;
                        double d2 = this.random.nextGaussian() * 0.02D;
                        this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
                    }
                    if (!pPlayer.abilities.instabuild) {
                        itemstack.shrink(1);
                    }
                    EntityFinder.sendEntityUpdatePacket(pPlayer, this);
                    return ActionResultType.CONSUME;
                } else {
                    return ActionResultType.PASS;
                }
            } else {
                return ActionResultType.PASS;
            }
        } else {
            return ActionResultType.PASS;
        }
    }
}
