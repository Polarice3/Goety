package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.ally.SkeletonMinionEntity;
import com.Polarice3.Goety.common.entities.ally.ZombieMinionEntity;
import com.Polarice3.Goety.common.entities.hostile.BoneLordEntity;
import com.Polarice3.Goety.common.entities.hostile.SkullLordEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.utils.*;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

public class SoulSkullEntity extends ExplosiveProjectileEntity {
    public float explosionPower = 1.0F;

    public SoulSkullEntity(EntityType<? extends SoulSkullEntity> p_i50147_1_, World p_i50147_2_) {
        super(p_i50147_1_, p_i50147_2_);
    }

    public SoulSkullEntity(World p_i1794_1_, LivingEntity p_i1794_2_, double p_i1794_3_, double p_i1794_5_, double p_i1794_7_) {
        super(ModEntityType.SOUL_SKULL.get(), p_i1794_2_, p_i1794_3_, p_i1794_5_, p_i1794_7_, p_i1794_1_);
    }

    public SoulSkullEntity(World pWorld, double pX, double pY, double pZ, double pAccelX, double pAccelY, double pAccelZ) {
        super(ModEntityType.SOUL_SKULL.get(), pX, pY, pZ, pAccelX, pAccelY, pAccelZ, pWorld);
    }

    protected float getInertia() {
        return this.isDangerous() ? 0.73F : super.getInertia();
    }

    public boolean isOnFire() {
        return false;
    }

    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            Entity owner = this.getOwner();
            int flaming = 0;
            if (owner instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) owner;
                if (WandUtil.enchantedFocus(player)){
                    flaming = WandUtil.getLevels(ModEnchantments.BURNING.get(), player);
                }
            }
            if (flaming != 0){
                Vector3d vector3d = this.getDeltaMovement();
                double d0 = this.getX() + vector3d.x;
                double d1 = this.getY() + vector3d.y;
                double d2 = this.getZ() + vector3d.z;
                this.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 + level.random.nextDouble()/2, d1 + 0.5D, d2 + level.random.nextDouble()/2, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    protected void onHitEntity(EntityRayTraceResult pResult) {
        super.onHitEntity(pResult);
        if (!this.level.isClientSide) {
            ServerWorld serverWorld = (ServerWorld) this.level;
            Entity target = pResult.getEntity();
            Entity owner = this.getOwner();
            boolean flag;
            boolean flag2;
            float enchantment = 0;
            int flaming = 0;
            if (owner instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)owner;
                World worldIn = livingentity.level;
                if (livingentity instanceof PlayerEntity){
                    PlayerEntity player = (PlayerEntity) livingentity;
                    if (WandUtil.enchantedFocus(player)){
                        enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                        flaming = WandUtil.getLevels(ModEnchantments.BURNING.get(), player);
                    }
                }
                flag = target.hurt(DamageSource.indirectMagic(this, livingentity), 6.0F + enchantment);
                flag2 = RobeArmorFinder.FindNecroSet(livingentity);
                if (livingentity instanceof SkullLordEntity){
                    if (target instanceof BoneLordEntity){
                        flag = false;
                    }
                }
                if (flag) {
                    if (target.isAlive()) {
                        this.doEnchantDamageEffects(livingentity, target);
                        if (flaming != 0) {
                            target.setSecondsOnFire(5 + flaming);
                        }
                    } else {
                        livingentity.heal(1.0F);
                        if (MainConfig.SoulSkullZombie.get()) {
                            if (target instanceof ZombieEntity) {
                                if (flag2) {
                                    ZombieMinionEntity summonedentity = ((ZombieEntity) target).convertTo(ModEntityType.ZOMBIE_MINION.get(), false);
                                    if (summonedentity != null) {
                                        if (MainConfig.SoulSkullMinionWander.get()) {
                                            summonedentity.setWandering(true);
                                        }
                                        summonedentity.setTrueOwner(livingentity);
                                        summonedentity.finalizeSpawn((IServerWorld) worldIn, worldIn.getCurrentDifficultyAt(target.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData) null, (CompoundNBT) null);
                                        summonedentity.setLimitedLife(20 * (30 + worldIn.random.nextInt(90)));
                                        summonedentity.setUpgraded(false);
                                        net.minecraftforge.event.ForgeEventFactory.onLivingConvert((LivingEntity) target, summonedentity);
                                        summonedentity.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.0F, 1.0F);
                                        for (int i = 0; i < summonedentity.level.random.nextInt(35) + 10; ++i) {
                                            serverWorld.sendParticles(ParticleTypes.POOF, summonedentity.getX(), summonedentity.getEyeY(), summonedentity.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
                                        }
                                    }
                                }
                            }
                        }
                        if (MainConfig.SoulSkullSkeleton.get()) {
                            if (target instanceof SkeletonEntity) {
                                if (flag2) {
                                    SkeletonMinionEntity summonedentity = ((SkeletonEntity) target).convertTo(ModEntityType.SKELETON_MINION.get(), false);
                                    if (summonedentity != null) {
                                        if (MainConfig.SoulSkullMinionWander.get()) {
                                            summonedentity.setWandering(true);
                                        }
                                        summonedentity.setTrueOwner(livingentity);
                                        summonedentity.finalizeSpawn((IServerWorld) worldIn, worldIn.getCurrentDifficultyAt(target.blockPosition()), SpawnReason.CONVERSION, (ILivingEntityData) null, (CompoundNBT) null);
                                        summonedentity.setLimitedLife(20 * (30 + worldIn.random.nextInt(90)));
                                        summonedentity.setUpgraded(false);
                                        net.minecraftforge.event.ForgeEventFactory.onLivingConvert((LivingEntity) target, summonedentity);
                                        summonedentity.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, 1.0F, 1.0F);
                                        for (int i = 0; i < summonedentity.level.random.nextInt(35) + 10; ++i) {
                                            serverWorld.sendParticles(ParticleTypes.POOF, summonedentity.getX(), summonedentity.getEyeY(), summonedentity.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                target.hurt(DamageSource.MAGIC, 6.0F);
            }
        }
    }

    protected void onHit(RayTraceResult pResult) {
        super.onHit(pResult);
        Entity owner = this.getOwner();
        float enchantment = 0;
        boolean flaming = false;
        boolean loot = false;
        if (owner instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) owner;
            if (WandUtil.enchantedFocus(player)){
                enchantment = WandUtil.getLevels(ModEnchantments.RADIUS.get(), player)/2.5F;
                if (WandUtil.getLevels(ModEnchantments.BURNING.get(), player) > 0){
                    flaming = true;
                }
            }
            if (CuriosFinder.findRing(player).getItem() == ModItems.RING_OF_WANT.get()){
                if (CuriosFinder.findRing(player).isEnchanted()){
                    float wanting = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.WANTING.get(), CuriosFinder.findRing(player));
                    if (wanting > 0){
                        loot = true;
                    }
                }
            }
        }
        Explosion.Mode explodeMode = Explosion.Mode.NONE;
        if (this.isDangerous()){
            if (this.getOwner() instanceof PlayerEntity){
                explodeMode = Explosion.Mode.DESTROY;
            } else {
                if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.getOwner())){
                    explodeMode = Explosion.Mode.DESTROY;
                }
            }
        }
        LootingExplosion.Mode lootMode = loot ? LootingExplosion.Mode.LOOT : LootingExplosion.Mode.REGULAR;
        ExplosionUtil.lootExplode(this.level, this, this.getX(), this.getY(), this.getZ(), this.explosionPower + enchantment, flaming, explodeMode, lootMode);
        this.remove();

    }

    public boolean isPickable() {
        return false;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    protected boolean canHitEntity(Entity pEntity) {
        if (this.getOwner() != null && this.getOwner().isAlliedTo(pEntity)){
            return false;
        } else {
            return super.canHitEntity(pEntity);
        }
    }

    @Override
    public void setExplosionPower(float pExplosionPower) {
        this.explosionPower = pExplosionPower;
    }

    @Override
    public float getExplosionPower() {
        return this.explosionPower;
    }

    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
