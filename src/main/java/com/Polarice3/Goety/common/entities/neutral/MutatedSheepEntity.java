package com.Polarice3.Goety.common.entities.neutral;

import com.Polarice3.Goety.init.ModRegistryHandler;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IForgeShearable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class MutatedSheepEntity extends MutatedEntity implements IForgeShearable {
    private static final DataParameter<Byte> DATA_WOOL_ID = EntityDataManager.defineId(MutatedSheepEntity.class, DataSerializers.BYTE);
    private static final Map<DyeColor, IItemProvider> ITEM_BY_DYE = Util.make(Maps.newEnumMap(DyeColor.class), (p_203402_0_) -> {
        p_203402_0_.put(DyeColor.WHITE, Blocks.WHITE_WOOL);
        p_203402_0_.put(DyeColor.ORANGE, Blocks.ORANGE_WOOL);
        p_203402_0_.put(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL);
        p_203402_0_.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL);
        p_203402_0_.put(DyeColor.YELLOW, Blocks.YELLOW_WOOL);
        p_203402_0_.put(DyeColor.LIME, Blocks.LIME_WOOL);
        p_203402_0_.put(DyeColor.PINK, Blocks.PINK_WOOL);
        p_203402_0_.put(DyeColor.GRAY, Blocks.GRAY_WOOL);
        p_203402_0_.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL);
        p_203402_0_.put(DyeColor.CYAN, Blocks.CYAN_WOOL);
        p_203402_0_.put(DyeColor.PURPLE, Blocks.PURPLE_WOOL);
        p_203402_0_.put(DyeColor.BLUE, Blocks.BLUE_WOOL);
        p_203402_0_.put(DyeColor.BROWN, Blocks.BROWN_WOOL);
        p_203402_0_.put(DyeColor.GREEN, Blocks.GREEN_WOOL);
        p_203402_0_.put(DyeColor.RED, Blocks.RED_WOOL);
        p_203402_0_.put(DyeColor.BLACK, Blocks.BLACK_WOOL);
    });
    private static final Map<DyeColor, float[]> COLORARRAY_BY_COLOR = Maps.newEnumMap(Arrays.stream(DyeColor.values())
            .collect(Collectors.toMap((DyeColor p_200204_0_) -> p_200204_0_, MutatedSheepEntity::createSheepColor)));
    private int sheepTimer;
    private EatGrassGoal eatGrassGoal;

    @OnlyIn(Dist.CLIENT)
    public static float[] getColorArray(DyeColor pDyeColor) {
        return COLORARRAY_BY_COLOR.get(pDyeColor);
    }

    private static float[] createSheepColor(DyeColor p_192020_0_) {
        if (p_192020_0_ == DyeColor.WHITE) {
            return new float[]{0.9019608F, 0.9019608F, 0.9019608F};
        } else {
            float[] afloat = p_192020_0_.getTextureDiffuseColors();
            float f = 0.75F;
            return new float[]{afloat[0] * f, afloat[1] * f, afloat[2] * f};
        }
    }

    public MutatedSheepEntity(EntityType<? extends MutatedSheepEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        this.eatGrassGoal = new EatGrassGoal(this);
        this.goalSelector.addGoal(5, this.eatGrassGoal);
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(Items.WHEAT), false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
    }

    protected void customServerAiStep() {
        this.sheepTimer = this.eatGrassGoal.getEatAnimationTick();
        super.customServerAiStep();
    }

    public void aiStep() {
        if (this.level.isClientSide) {
            this.sheepTimer = Math.max(0, this.sheepTimer - 1);
        }

        super.aiStep();
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_WOOL_ID, (byte)0);
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte id) {
        if (id == 10) {
            this.sheepTimer = 40;
        } else {
            super.handleEntityEvent(id);
        }

    }

    @OnlyIn(Dist.CLIENT)
    public float getHeadEatPositionScale(float p_70894_1_) {
        if (this.sheepTimer <= 0) {
            return 0.0F;
        } else if (this.sheepTimer >= 4 && this.sheepTimer <= 36) {
            return 1.0F;
        } else {
            return this.sheepTimer < 4 ? ((float)this.sheepTimer - p_70894_1_) / 4.0F : -((float)(this.sheepTimer - 40) - p_70894_1_) / 4.0F;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public float getHeadEatAngleScale(float p_70890_1_) {
        if (this.sheepTimer > 4 && this.sheepTimer <= 36) {
            float f = ((float)(this.sheepTimer - 4) - p_70890_1_) / 32.0F;
            return ((float)Math.PI / 5F) + 0.21991149F * MathHelper.sin(f * 28.7F);
        } else {
            return this.sheepTimer > 0 ? ((float)Math.PI / 5F) : this.xRot * ((float)Math.PI / 180F);
        }
    }

    public boolean readyForShearing() {
        return this.isAlive() && !this.isSheared();
    }

    public ActionResultType mobInteract(PlayerEntity pPlayer, Hand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.getItem() instanceof DyeItem){
            DyeItem dyeItem = (DyeItem) itemstack.getItem();
            if (!this.isSheared() && this.getColor() != dyeItem.getDyeColor()){
                this.setColor(dyeItem.getDyeColor());
                itemstack.shrink(1);
                return ActionResultType.SUCCESS;
            }
        } else {
            return super.mobInteract(pPlayer, pHand);
        }
        if (itemstack.getItem() == Items.SHEARS) {
            if (!this.level.isClientSide && !this.readyForShearing()) {
                this.shear(SoundCategory.PLAYERS);
                itemstack.hurtAndBreak(1, pPlayer, (p_213613_1_) -> {
                    p_213613_1_.broadcastBreakEvent(pHand);
                });
                return ActionResultType.SUCCESS;
            } else {
                return ActionResultType.CONSUME;
            }
        } else {
            return super.mobInteract(pPlayer, pHand);
        }
    }

    public void shear(SoundCategory pCategory) {
        this.level.playSound(null, this, SoundEvents.SHEEP_SHEAR, pCategory, 1.0F, 1.0F);
        this.setSheared(true);
        int i = 8 + this.random.nextInt(8);
        int random = this.level.random.nextInt(8);
        if (random == 0){
            this.ignite();
        }
        for(int j = 0; j < i; ++j) {
            ItemEntity itementity = this.spawnAtLocation(ITEM_BY_DYE.get(this.getColor()), 1);
            if (itementity != null) {
                itementity.setDeltaMovement(itementity.getDeltaMovement().add((double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (double)(this.random.nextFloat() * 0.05F), (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)));
            }
        }

    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("Sheared", this.isSheared());
        pCompound.putByte("Color", (byte)this.getColor().getId());
    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setSheared(pCompound.getBoolean("Sheared"));
        this.setColor(DyeColor.byId(pCompound.getByte("Color")));
    }

    public DyeColor getColor() {
        return DyeColor.byId(this.entityData.get(DATA_WOOL_ID) & 15);
    }

    public void setColor(DyeColor pColor) {
        byte b0 = this.entityData.get(DATA_WOOL_ID);
        this.entityData.set(DATA_WOOL_ID, (byte)(b0 & 240 | pColor.getId() & 15));
    }

    public boolean isSheared() {
        return (this.entityData.get(DATA_WOOL_ID) & 16) != 0;
    }

    public void setSheared(boolean pSheared) {
        byte b0 = this.entityData.get(DATA_WOOL_ID);
        if (pSheared) {
            this.entityData.set(DATA_WOOL_ID, (byte)(b0 | 16));
        } else {
            this.entityData.set(DATA_WOOL_ID, (byte)(b0 & -17));
        }

    }

    public static DyeColor getRandomSheepColor(Random pRandom) {
        return DyeColor.WHITE;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld pLevel, DifficultyInstance pDifficulty, SpawnReason pReason, @Nullable ILivingEntityData pSpawnData, @Nullable CompoundNBT pDataTag) {
        this.setColor(getRandomSheepColor(pLevel.getRandom()));
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SHEEP_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.SHEEP_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SHEEP_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.SHEEP_STEP, 0.15F, 1.0F);
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 1.3F;
    }

    public void ate() {
        this.setSheared(false);
    }

    @Override
    public boolean isShearable(@Nonnull ItemStack item, World world, BlockPos pos) {
        return readyForShearing();
    }

    @Nonnull
    public List<ItemStack> onSheared(@Nullable PlayerEntity player, @Nonnull ItemStack item, World world, BlockPos pos, int fortune) {
        world.playSound(null, this, SoundEvents.SHEEP_SHEAR, player == null ? SoundCategory.BLOCKS : SoundCategory.PLAYERS, 1.0F, 1.0F);
        if (!world.isClientSide && !this.isSheared()) {
            this.setSheared(true);
            int i = 8 + this.random.nextInt(8);

            List<ItemStack> items = new ArrayList<>();
            for (int j = 0; j < i; ++j) {
                items.add(new ItemStack(ITEM_BY_DYE.get(this.getColor())));
            }
            int random = this.level.random.nextInt(8);
            if (random == 0){
                this.ignite();
            }
            return items;
        }
        return Collections.emptyList();
    }

    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
        super.dropCustomDeathLoot(source, looting, recentlyHitIn);
        int random = this.level.random.nextInt(4);
        if (random == 1 || looting > 2) {
            this.spawnAtLocation(ModRegistryHandler.MUTATED_MUTTON_UNCOOKED.get());
        } else {
            for (int i = 0; i < 4 + this.level.random.nextInt(8); ++i) {
                this.spawnAtLocation(Items.ROTTEN_FLESH);
            }
        }
        if (!this.isSheared()) {
            for (int i = 0; i < 4 + this.level.random.nextInt(4); ++i) {
                this.spawnAtLocation(ITEM_BY_DYE.get(this.getColor()));
            }
        }
    }

}
