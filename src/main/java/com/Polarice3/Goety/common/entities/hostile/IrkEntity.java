package com.Polarice3.Goety.common.entities.hostile;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class IrkEntity extends MonsterEntity implements ICrossbowUser {
    protected static final DataParameter<Byte> IRK_FLAGS = EntityDataManager.defineId(IrkEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> DATA_CHARGING_STATE = EntityDataManager.defineId(IrkEntity.class, DataSerializers.BOOLEAN);
    private MobEntity owner;
    @Nullable
    private BlockPos boundOrigin;
    private boolean limitedLifespan;
    private int limitedLifeTicks;
    private final Inventory inventory = new Inventory(5);

    public IrkEntity(EntityType<? extends IrkEntity> p_i50190_1_, World p_i50190_2_) {
        super(p_i50190_1_, p_i50190_2_);
        this.moveControl = new MoveHelperController(this);
        this.xpReward = 3;
    }

    public void move(MoverType typeIn, Vector3d pos) {
        super.move(typeIn, pos);
        this.checkInsideBlocks();
    }

    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
        if (this.limitedLifespan && --this.limitedLifeTicks <= 0) {
            this.limitedLifeTicks = 20;
            this.hurt(DamageSource.STARVE, 1.0F);
        }

    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new AttackMoveGoal());
        this.goalSelector.addGoal(4, new RangedCrossbowAttackGoal<>(this, 1.0F, 8.0F));
        this.goalSelector.addGoal(8, new MoveRandomGoal());
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(2, new CopyOwnerTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes(){
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 14.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IRK_FLAGS, (byte)0);
        this.entityData.define(DATA_CHARGING_STATE, false);
    }

    public boolean canFireProjectileWeapon(ShootableItem p_230280_1_) {
        return p_230280_1_ == Items.CROSSBOW;
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        ListNBT listnbt = compound.getList("Inventory", 10);

        for(int i = 0; i < listnbt.size(); ++i) {
            ItemStack itemstack = ItemStack.of(listnbt.getCompound(i));
            if (!itemstack.isEmpty()) {
                this.inventory.addItem(itemstack);
            }
        }

        this.setCanPickUpLoot(true);
        if (compound.contains("BoundX")) {
            this.boundOrigin = new BlockPos(compound.getInt("BoundX"), compound.getInt("BoundY"), compound.getInt("BoundZ"));
        }

        if (compound.contains("LifeTicks")) {
            this.setLimitedLife(compound.getInt("LifeTicks"));
        }

    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        ListNBT listnbt = new ListNBT();

        for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                listnbt.add(itemstack.save(new CompoundNBT()));
            }
        }

        compound.put("Inventory", listnbt);
        if (this.boundOrigin != null) {
            compound.putInt("BoundX", this.boundOrigin.getX());
            compound.putInt("BoundY", this.boundOrigin.getY());
            compound.putInt("BoundZ", this.boundOrigin.getZ());
        }

        if (this.limitedLifespan) {
            compound.putInt("LifeTicks", this.limitedLifeTicks);
        }

    }

    @OnlyIn(Dist.CLIENT)
    public boolean isCharging() {
        return this.entityData.get(DATA_CHARGING_STATE);
    }

    public void setChargingCrossbow(boolean isCharging) {
        this.entityData.set(DATA_CHARGING_STATE, isCharging);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity p_230284_1_, ItemStack p_230284_2_, ProjectileEntity p_230284_3_, float p_230284_4_) {
        this.shootCrossbowProjectile(this, p_230284_1_, p_230284_3_, p_230284_4_, 1.6F);
    }

    @OnlyIn(Dist.CLIENT)
    public AbstractIllagerEntity.ArmPose getArmPose() {
        if (this.isCharging()) {
            return AbstractIllagerEntity.ArmPose.CROSSBOW_CHARGE;
        } else if (this.isHolding(Items.CROSSBOW)) {
            return AbstractIllagerEntity.ArmPose.CROSSBOW_HOLD;
        } else {
            return this.isAggressive() ? AbstractIllagerEntity.ArmPose.ATTACKING : AbstractIllagerEntity.ArmPose.NEUTRAL;
        }
    }

    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getEntity();
        if (entity instanceof IrkEntity){
            return false;
        } else {
            return super.hurt(source, amount);
        }
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (entityIn == this) {
            return true;
        } else if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof IrkEntity) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else if (entityIn instanceof LivingEntity && ((LivingEntity)entityIn).getMobType() == CreatureAttribute.ILLAGER) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    public MobEntity getOwner() {
        return this.owner;
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return this.boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos boundOriginIn) {
        this.boundOrigin = boundOriginIn;
    }

    private boolean getIrkFlag(int mask) {
        int i = this.entityData.get(IRK_FLAGS);
        return (i & mask) != 0;
    }

    private void setIrkFlag(int mask, boolean value) {
        int i = this.entityData.get(IRK_FLAGS);
        if (value) {
            i = i | mask;
        } else {
            i = i & ~mask;
        }

        this.entityData.set(IRK_FLAGS, (byte)(i & 255));
    }


    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    public void setOwner(MobEntity ownerIn) {
        this.owner = ownerIn;
    }

    public void setLimitedLife(int limitedLifeTicksIn) {
        this.limitedLifespan = true;
        this.limitedLifeTicks = limitedLifeTicksIn;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.VEX_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.VEX_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.VEX_HURT;
    }

    public float getBrightness() {
        return 1.0F;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.populateDefaultEquipmentSlots(difficultyIn);
        this.populateDefaultEquipmentEnchantments(difficultyIn);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.CROSSBOW));
        this.setDropChance(EquipmentSlotType.MAINHAND, 0.0F);
    }

    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        this.performCrossbowAttack(this, 1.6F);
    }

    public boolean setSlot(int inventorySlot, ItemStack itemStackIn) {
        if (super.setSlot(inventorySlot, itemStackIn)) {
            return true;
        } else {
            int i = inventorySlot - 300;
            if (i >= 0 && i < this.inventory.getContainerSize()) {
                this.inventory.setItem(i, itemStackIn);
                return true;
            } else {
                return false;
            }
        }
    }

    class AttackMoveGoal extends Goal{

        public boolean canUse() {
            return IrkEntity.this.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return IrkEntity.this.getTarget() != null && !IrkEntity.this.isCharging();
        }

        public void tick() {
            LivingEntity livingEntity = IrkEntity.this.getTarget();
            assert livingEntity != null;
            Vector3d vector3d = livingEntity.getEyePosition(1.0F);
            int random = IrkEntity.this.level.random.nextInt(2);
            if (random == 1) {
                IrkEntity.this.moveControl.setWantedPosition(vector3d.x - IrkEntity.this.random.nextInt(8), livingEntity.getY() + IrkEntity.this.random.nextInt(4), vector3d.z - IrkEntity.this.random.nextInt(8), 1.0D);
            } else {
                IrkEntity.this.moveControl.setWantedPosition(vector3d.x + IrkEntity.this.random.nextInt(8), livingEntity.getY() + IrkEntity.this.random.nextInt(4), vector3d.z + IrkEntity.this.random.nextInt(8), 1.0D);
            }
        }
    }

    class CopyOwnerTargetGoal extends TargetGoal {
        private final EntityPredicate field_220803_b = (new EntityPredicate()).allowUnseeable().ignoreInvisibilityTesting();

        public CopyOwnerTargetGoal(CreatureEntity creature) {
            super(creature, false);
        }

        public boolean canUse() {
            return IrkEntity.this.owner != null && IrkEntity.this.owner.getTarget() != null && this.canAttack(IrkEntity.this.owner.getTarget(), this.field_220803_b);
        }

        public void start() {
            IrkEntity.this.setTarget(IrkEntity.this.owner.getTarget());
            super.start();
        }
    }

    class MoveHelperController extends MovementController {
        public MoveHelperController(IrkEntity irk) {
            super(irk);
        }

        public void tick() {
            if (this.operation == Action.MOVE_TO) {
                Vector3d vector3d = new Vector3d(this.wantedX - IrkEntity.this.getX(), this.wantedY - IrkEntity.this.getY(), this.wantedZ - IrkEntity.this.getZ());
                double d0 = vector3d.length();
                if (d0 < IrkEntity.this.getBoundingBox().getSize()) {
                    this.operation = Action.WAIT;
                    IrkEntity.this.setDeltaMovement(IrkEntity.this.getDeltaMovement().scale(0.5D));
                } else {
                    IrkEntity.this.setDeltaMovement(IrkEntity.this.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.05D / d0)));
                    if (IrkEntity.this.getTarget() == null) {
                        Vector3d vector3d1 = IrkEntity.this.getDeltaMovement();
                        IrkEntity.this.yRot = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float)Math.PI);
                        IrkEntity.this.yBodyRot = IrkEntity.this.yRot;
                    } else {
                        double d2 = IrkEntity.this.getTarget().getX() - IrkEntity.this.getX();
                        double d1 = IrkEntity.this.getTarget().getZ() - IrkEntity.this.getZ();
                        IrkEntity.this.yRot = -((float)MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
                        IrkEntity.this.yBodyRot = IrkEntity.this.yRot;
                    }
                }

            }
        }
    }

    class MoveRandomGoal extends Goal {
        public MoveRandomGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return !IrkEntity.this.getMoveControl().hasWanted() && IrkEntity.this.random.nextInt(7) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = IrkEntity.this.getBoundOrigin();
            if (blockpos == null) {
                blockpos = IrkEntity.this.blockPosition();
            }

            for(int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.offset(IrkEntity.this.random.nextInt(15) - 7, IrkEntity.this.getY() + 1.0D, IrkEntity.this.random.nextInt(15) - 7);
                if (IrkEntity.this.level.isEmptyBlock(blockpos1)) {
                    IrkEntity.this.moveControl.setWantedPosition((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 0.25D);
                    if (IrkEntity.this.getTarget() == null) {
                        IrkEntity.this.getLookControl().setLookAt((double)blockpos1.getX() + 0.5D, (double)blockpos1.getY() + 0.5D, (double)blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }

}
