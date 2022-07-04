package com.Polarice3.Goety.common.entities.hostile.cultists;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.projectiles.PitchforkEntity;
import com.Polarice3.Goety.common.entities.projectiles.WitchBombEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModItems;
import com.google.common.collect.Maps;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Map;

public class FanaticEntity extends AbstractCultistEntity implements IRangedAttackMob{
    private static final DataParameter<Integer> DATA_TYPE_ID = EntityDataManager.defineId(FanaticEntity.class, DataSerializers.INT);
    public static final Map<Integer, ResourceLocation> TEXTURE_BY_TYPE = Util.make(Maps.newHashMap(), (map) -> {
        map.put(0, Goety.location("textures/entity/cultist/fanatic/fanatic_0.png"));
        map.put(1, Goety.location("textures/entity/cultist/fanatic/fanatic_1.png"));
        map.put(2, Goety.location("textures/entity/cultist/fanatic/fanatic_2.png"));
        map.put(3, Goety.location("textures/entity/cultist/fanatic/fanatic_3.png"));
        map.put(4, Goety.location("textures/entity/cultist/fanatic/fanatic_4.png"));
    });

    public FanaticEntity(EntityType<? extends FanaticEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public ResourceLocation getResourceLocation() {
        return TEXTURE_BY_TYPE.getOrDefault(this.getOutfitType(), TEXTURE_BY_TYPE.get(0));
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0F, false));
        this.goalSelector.addGoal(2, new PitchforkAttackGoal(this, 1.0D, 40, 10.0F));
        this.goalSelector.addGoal(2, new ThrowBombsGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    public void aiStep() {
        super.aiStep();
        this.conversion();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_TYPE_ID, 1);
    }

    public int getOutfitType() {
        return this.entityData.get(DATA_TYPE_ID);
    }

    public void setOutfitType(int pType) {
        if (pType < 0 || pType >= this.OutfitTypeNumber() + 1) {
            pType = this.random.nextInt(this.OutfitTypeNumber());
        }

        this.entityData.set(DATA_TYPE_ID, pType);
    }

    public int OutfitTypeNumber(){
        return TEXTURE_BY_TYPE.size();
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Outfit", this.getOutfitType());
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setOutfitType(pCompound.getInt("Outfit"));
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.EVOKER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.EVOKER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.EVOKER_DEATH;
    }

    public boolean canBeLeashed(PlayerEntity player) {
        return false;
    }

    public boolean hasBomb(){
        return this.getItemInHand(Hand.OFF_HAND).getItem() == ModItems.WITCHBOMB.get();
    }

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pDistanceFactor) {
        PitchforkEntity pitchforkEntity = new PitchforkEntity(this.level, this, new ItemStack(ModItems.PITCHFORK.get()));
        double d0 = pTarget.getX() - this.getX();
        double d1 = pTarget.getY(0.3333333333333333D) - pitchforkEntity.getY();
        double d2 = pTarget.getZ() - this.getZ();
        double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
        pitchforkEntity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(pitchforkEntity);
    }

    static class ThrowBombsGoal extends Goal{
        public int bombTimer;
        public FanaticEntity fanatic;

        public ThrowBombsGoal(FanaticEntity fanatic){
            this.fanatic = fanatic;
        }

        @Override
        public boolean canUse() {
            if (this.fanatic.getTarget() != null && this.fanatic.hasBomb()){
                LivingEntity entity = this.fanatic.getTarget();
                return this.fanatic.distanceTo(entity) > 2.0 && this.fanatic.distanceTo(entity) <= 10 && this.fanatic.canSee(entity);
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.fanatic.getTarget() != null && !this.fanatic.getTarget().isDeadOrDying() && this.fanatic.hasBomb();
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
                LivingEntity livingEntity = this.fanatic.getTarget();
                WitchBombEntity snowballentity = new WitchBombEntity(this.fanatic.level, this.fanatic);
                assert livingEntity != null;
                Vector3d vector3d = livingEntity.getDeltaMovement();
                double d0 = livingEntity.getX() + vector3d.x - this.fanatic.getX();
                double d1 = livingEntity.getEyeY() - (double) 1.1F - this.fanatic.getY();
                double d2 = livingEntity.getZ() + vector3d.z - this.fanatic.getZ();
                float f = MathHelper.sqrt(d0 * d0 + d2 * d2);
                snowballentity.shoot(d0, d1 + (double) (f * 0.2F), d2, 0.75F, 2.0F);
                this.fanatic.playSound(SoundEvents.WITCH_THROW, 1.0F, 0.4F / (this.fanatic.getRandom().nextFloat() * 0.4F + 0.8F));
                this.fanatic.level.addFreshEntity(snowballentity);
                this.bombTimer = 0;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public ArmPose getArmPose() {
        if (this.isAggressive()) {
            if (!hasBomb()) {
                return ArmPose.ATTACKING;
            } else {
                return ArmPose.BOMB_AND_WEAPON;
            }
        } else {
            return ArmPose.CROSSED;
        }
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        if ((double)worldIn.getRandom().nextFloat() < 0.05D) {
            CrimsonSpiderEntity spider = new CrimsonSpiderEntity(ModEntityType.CRIMSON_SPIDER.get(), level);
            if (this.isPersistenceRequired()){
                spider.setPersistenceRequired();
            }
            spider.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0F);
            spider.finalizeSpawn(worldIn, difficultyIn, SpawnReason.JOCKEY, (ILivingEntityData)null, (CompoundNBT)null);
            this.startRiding(spider);
            worldIn.addFreshEntity(spider);
        }
        this.setOutfitType(this.random.nextInt(this.OutfitTypeNumber()));
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        int random = this.random.nextInt(12);
        int random2 = this.random.nextInt(5);
        int witchbomb;
        if (MainConfig.FanaticWitchBombChance.get() != 0){
            witchbomb = this.random.nextInt(MainConfig.FanaticWitchBombChance.get());
        } else {
            witchbomb = 1;
        }
        int pitchfork;
        if (MainConfig.FanaticPitchforkChance.get() != 0){
            pitchfork = this.random.nextInt(MainConfig.FanaticPitchforkChance.get());
        } else {
            pitchfork = 1;
        }
        if (pitchfork != 0){
            switch (random){
                case 0:
                    this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_SWORD));
                    break;
                case 1:
                    this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_AXE));
                    break;
                case 2:
                    this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_PICKAXE));
                    break;
                case 3:
                    this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.STONE_SHOVEL));
                    break;
                case 4:
                    this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
                    break;
                case 5:
                    this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_AXE));
                    break;
                case 6:
                    this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_PICKAXE));
                    break;
                case 7:
                    this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
                    break;
                case 8:
                    this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
                    break;
                case 9:
                    this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_AXE));
                    break;
                case 10:
                    this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_PICKAXE));
                    break;
                case 11:
                    this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SHOVEL));
            }
        } else {
            this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.PITCHFORK.get()));
        }
        switch (random2){
            case 0:
            case 1:
            case 2:
                this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
                break;
            case 3:
                this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
                break;
            case 4:
                this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        }
        if (witchbomb == 0) {
            this.setItemSlot(EquipmentSlotType.OFFHAND, new ItemStack(ModItems.WITCHBOMB.get()));
        }
    }

    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
        if (this.getMainHandItem().getItem() == ModItems.PITCHFORK.get()) {
            for (int i = 0; i < this.random.nextInt(3) + (pLooting > 0 ? this.random.nextInt(pLooting) : 0); ++i){
                this.spawnAtLocation(Items.WHEAT);
            }
        }
    }

    static class PitchforkAttackGoal extends RangedAttackGoal {
        private final FanaticEntity fanatic;

        public PitchforkAttackGoal(IRangedAttackMob p_i48907_1_, double p_i48907_2_, int p_i48907_4_, float p_i48907_5_) {
            super(p_i48907_1_, p_i48907_2_, p_i48907_4_, p_i48907_5_);
            this.fanatic = (FanaticEntity)p_i48907_1_;
        }

        public boolean canUse() {
            return super.canUse() && this.fanatic.getMainHandItem().getItem() == ModItems.PITCHFORK.get();
        }

        public void start() {
            super.start();
            this.fanatic.setAggressive(true);
            this.fanatic.startUsingItem(Hand.MAIN_HAND);
        }

        public void stop() {
            super.stop();
            this.fanatic.stopUsingItem();
            this.fanatic.setAggressive(false);
        }
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }
}
