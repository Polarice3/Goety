package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.ModDamageSource;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class SwordProjectileEntity extends AbstractArrowEntity implements IRendersAsItem {
    private static final DataParameter<ItemStack> DATA_ITEM_STACK = EntityDataManager.defineId(SwordProjectileEntity.class, DataSerializers.ITEM_STACK);

    public SwordProjectileEntity(EntityType<? extends AbstractArrowEntity> p_i48546_1_, World p_i48546_2_) {
        super(p_i48546_1_, p_i48546_2_);
    }

    public SwordProjectileEntity(double p_i48547_2_, double p_i48547_4_, double p_i48547_6_, World p_i48547_8_) {
        super(ModEntityType.SWORD.get(), p_i48547_2_, p_i48547_4_, p_i48547_6_, p_i48547_8_);
    }

    public SwordProjectileEntity(LivingEntity p_i48548_2_, World p_i48548_3_, ItemStack p_i48790_3_) {
        super(ModEntityType.SWORD.get(), p_i48548_2_.getX(), p_i48548_2_.getY(0.5F), p_i48548_2_.getZ(), p_i48548_3_);
        this.setOwner(p_i48548_2_);
        this.setItem(p_i48790_3_.copy());
    }

    public void setItem(ItemStack pStack) {
        if (pStack.getItem() != this.getDefaultItem() || pStack.hasTag()) {
            this.getEntityData().set(DATA_ITEM_STACK, Util.make(pStack.copy(), (p_213883_0_) -> {
                p_213883_0_.setCount(1);
            }));
        }

    }

    protected Item getDefaultItem() {
        return Items.IRON_SWORD;
    }

    protected ItemStack getItemRaw() {
        return this.getEntityData().get(DATA_ITEM_STACK);
    }

    public ItemStack getItem() {
        ItemStack itemstack = this.getItemRaw();
        return itemstack.isEmpty() ? new ItemStack(this.getDefaultItem()) : itemstack;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }

    public void addAdditionalSaveData(CompoundNBT pCompound) {
        super.addAdditionalSaveData(pCompound);
        ItemStack itemstack = this.getItemRaw();
        if (!itemstack.isEmpty()) {
            pCompound.put("Item", itemstack.save(new CompoundNBT()));
        }

    }

    public void readAdditionalSaveData(CompoundNBT pCompound) {
        super.readAdditionalSaveData(pCompound);
        ItemStack itemstack = ItemStack.of(pCompound.getCompound("Item"));
        this.setItem(itemstack);
    }

    @Override
    protected ItemStack getPickupItem() {
        return this.getItem().copy();
    }

    protected void onHitEntity(EntityRayTraceResult pResult) {
        Entity target = pResult.getEntity();
        float f = 6.0F;
        if (this.getItem().getItem() instanceof SwordItem){
            SwordItem swordItem = (SwordItem) this.getItem().getItem();
            f = swordItem.getDamage();
        }
        Entity entity1 = this.getOwner();
        if (target instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)target;
            f += EnchantmentHelper.getDamageBonus(this.getItem(), livingentity.getMobType());
        }
        DamageSource damagesource = ModDamageSource.sword(this, (Entity)(entity1 == null ? this : entity1));
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        if (target.hurt(damagesource, f)) {
            if (target instanceof LivingEntity) {
                LivingEntity livingentity1 = (LivingEntity)target;
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity1, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, livingentity1);
                }

                this.doPostHurtEffects(livingentity1);
            }
        }

        float f1 = 1.0F;
        this.playSound(soundevent, f1, 1.0F);
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
