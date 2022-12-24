package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class LavaballEntity extends ExplosiveProjectileEntity {
    private static final DataParameter<Boolean> DATA_UPGRADED = EntityDataManager.defineId(LavaballEntity.class, DataSerializers.BOOLEAN);
    public float explosionPower = 1.0F;

    public LavaballEntity(EntityType<? extends LavaballEntity> p_i50163_1_, World p_i50163_2_) {
        super(p_i50163_1_, p_i50163_2_);
    }

    public LavaballEntity(World p_i1768_1_, double p_i1768_2_, double p_i1768_4_, double p_i1768_6_, double p_i1768_8_, double p_i1768_10_, double p_i1768_12_) {
        super(ModEntityType.LAVABALL.get(), p_i1768_2_, p_i1768_4_, p_i1768_6_, p_i1768_8_, p_i1768_10_, p_i1768_12_, p_i1768_1_);
    }

    public LavaballEntity(World p_i1769_1_, LivingEntity p_i1769_2_, double p_i1769_3_, double p_i1769_5_, double p_i1769_7_) {
        super(ModEntityType.LAVABALL.get(), p_i1769_2_, p_i1769_3_, p_i1769_5_, p_i1769_7_, p_i1769_1_);
    }

    public boolean defaultDangerous(){
        return true;
    }

    protected void onHit(RayTraceResult pResult) {
        super.onHit(pResult);
        if (!this.level.isClientSide) {
            Entity owner = this.getOwner();
            float enchantment = 0;
            if (owner instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) owner;
                if (WandUtil.enchantedFocus(player)){
                    enchantment = WandUtil.getLevels(ModEnchantments.RADIUS.get(), player)/2.5F;
                }
            }
            boolean flag = this.isDangerous();
            this.level.explode(owner, this.getX(), this.getY(), this.getZ(), this.explosionPower + enchantment, flag, flag ? Explosion.Mode.DESTROY : Explosion.Mode.NONE);
            this.remove();
        }

    }

    protected void onHitEntity(EntityRayTraceResult pResult) {
        super.onHitEntity(pResult);
        if (!this.level.isClientSide) {
            Entity entity = pResult.getEntity();
            Entity entity1 = this.getOwner();
            float enchantment = 0;
            int flaming = 0;
            if (entity1 instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) entity1;
                if (WandUtil.enchantedFocus(player)){
                    enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                    flaming = WandUtil.getLevels(ModEnchantments.BURNING.get(), player);
                }
            }
            entity.hurt(ModDamageSource.modFireball(this.getOwner(), this.level), 6.0F + enchantment);

            if (flaming != 0){
                entity.setSecondsOnFire(5 + flaming);
            }
            if (entity1 instanceof LivingEntity) {
                this.doEnchantDamageEffects((LivingEntity)entity1, entity);
            }
        }
    }

    protected boolean canHitEntity(Entity pEntity) {
        if (this.getOwner() instanceof OwnedEntity){
            OwnedEntity owner = (OwnedEntity) this.getOwner();
            if (pEntity instanceof OwnedEntity){
                OwnedEntity entity = (OwnedEntity) pEntity;
                if (owner.getTrueOwner() == entity.getTrueOwner()){
                    return false;
                }
            }
            if (owner.getTrueOwner() == pEntity){
                return false;
            }
        }
        if (this.isUpgraded()){
            if (pEntity instanceof DamagingProjectileEntity){
                return false;
            }
        }
        return super.canHitEntity(pEntity);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_UPGRADED, false);
    }

    @Override
    public void setExplosionPower(float pExplosionPower) {
        this.explosionPower = pExplosionPower;
    }

    @Override
    public float getExplosionPower() {
        return this.explosionPower;
    }

    @Override
    public boolean ignoreExplosion() {
        return this.isUpgraded();
    }

    public boolean isUpgraded() {
        return this.entityData.get(DATA_UPGRADED);
    }

    public void setUpgraded(boolean pInvulnerable) {
        this.entityData.set(DATA_UPGRADED, pInvulnerable);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
