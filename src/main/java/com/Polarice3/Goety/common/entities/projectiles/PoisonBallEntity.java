package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

@OnlyIn(
        value = Dist.CLIENT,
        _interface = IRendersAsItem.class
)
public class PoisonBallEntity extends ProjectileItemEntity {
    private static final DataParameter<Boolean> DATA_UPGRADED = EntityDataManager.defineId(PoisonBallEntity.class, DataSerializers.BOOLEAN);

    public PoisonBallEntity(EntityType<? extends PoisonBallEntity> p_i50147_1_, World p_i50147_2_) {
        super(p_i50147_1_, p_i50147_2_);
    }

    public PoisonBallEntity(double p_i50156_2_, double p_i50156_4_, double p_i50156_6_, World p_i50156_8_) {
        super(ModEntityType.POISON_BALL.get(), p_i50156_2_, p_i50156_4_, p_i50156_6_, p_i50156_8_);
    }

    public PoisonBallEntity(LivingEntity p_i50157_2_, World p_i50157_3_) {
        super(ModEntityType.POISON_BALL.get(), p_i50157_2_, p_i50157_3_);
    }

    protected Item getDefaultItem() {
        return Items.SLIME_BALL;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            this.spawnParticles();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void spawnParticles(){
        for(int i = 0; i < 8; ++i) {
            this.level.addParticle(ParticleTypes.ITEM_SLIME, this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F, 0.0F);
        }
    }

    protected void onHitEntity(EntityRayTraceResult pResult) {
        super.onHitEntity(pResult);
        if (!this.level.isClientSide) {
            Entity target = pResult.getEntity();
            Entity owner = this.getOwner();
            float damage = SpellConfig.PoisonballDamage.get().floatValue() * SpellConfig.SpellDamageMultiplier.get();
            int enchantment = 0;
            int duration = 1;
            if (owner instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) owner;
                if (WandUtil.enchantedFocus(player)){
                    enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                    duration = WandUtil.getLevels(ModEnchantments.DURATION.get(), player) + 1;
                }
            }
            if (owner instanceof LivingEntity) {
                LivingEntity LivingOwner = (LivingEntity)owner;
                if (target instanceof LivingEntity){
                    LivingEntity livingTarget = (LivingEntity) target;
                    if (livingTarget.isAlive()){
                        if (this.isUpgraded()){
                            livingTarget.hurt(DamageSource.indirectMagic(this, LivingOwner), damage + enchantment);
                        }
                        livingTarget.addEffect(new EffectInstance(Effects.POISON, 432 * duration, enchantment));
                        if (RobeArmorFinder.FindFelSet(LivingOwner)){
                            livingTarget.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 400 * duration, enchantment));
                        }
                    }
                }
            } else {
                if (target instanceof LivingEntity){
                    LivingEntity livingTarget = (LivingEntity) target;
                    if (livingTarget.isAlive()){
                        if (this.isUpgraded()){
                            livingTarget.hurt(DamageSource.MAGIC, damage);
                        }
                        livingTarget.addEffect(new EffectInstance(Effects.POISON, 432));
                    }
                }
            }
        }
        this.level.playLocalSound(pResult.getLocation().x, pResult.getLocation().y, pResult.getLocation().z, SoundEvents.SLIME_ATTACK, SoundCategory.NEUTRAL, 1.0F, 1.0F, false);
    }

    protected void onHit(RayTraceResult pResult) {
        super.onHit(pResult);
        if (!this.level.isClientSide) {
            this.level.playLocalSound(pResult.getLocation().x, pResult.getLocation().y, pResult.getLocation().z, SoundEvents.SLIME_ATTACK, SoundCategory.NEUTRAL, 1.0F, 1.0F, false);
            this.level.broadcastEntityEvent(this, (byte)3);
            this.remove();
        }
    }


    public boolean isOnFire() {
        return false;
    }

    public boolean ignoreExplosion(){return false;}

    public boolean canBeCollidedWith() {
        return false;
    }

    public boolean isUpgraded() {
        return this.entityData.get(DATA_UPGRADED);
    }

    public void setUpgraded(boolean pInvulnerable) {
        this.entityData.set(DATA_UPGRADED, pInvulnerable);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_UPGRADED, false);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
