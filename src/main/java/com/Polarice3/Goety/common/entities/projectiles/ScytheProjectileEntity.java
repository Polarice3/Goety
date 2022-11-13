package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.utils.EntityFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.SEHelper;
import com.google.common.collect.Maps;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.*;

public class ScytheProjectileEntity extends DamagingProjectileEntity {
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.defineId(ScytheProjectileEntity.class, DataSerializers.OPTIONAL_UUID);
    private static final DataParameter<Integer> DATA_TYPE_ID = EntityDataManager.defineId(ScytheProjectileEntity.class, DataSerializers.INT);
    public static final Map<Integer, ResourceLocation> TEXTURE_BY_TYPE = Util.make(Maps.newHashMap(), (map) -> {
        map.put(0, Goety.location("textures/entity/projectiles/scythe/scythe_0.png"));
        map.put(1, Goety.location("textures/entity/projectiles/scythe/scythe_1.png"));
        map.put(2, Goety.location("textures/entity/projectiles/scythe/scythe_2.png"));
        map.put(3, Goety.location("textures/entity/projectiles/scythe/scythe_3.png"));
        map.put(4, Goety.location("textures/entity/projectiles/scythe/scythe_4.png"));
        map.put(5, Goety.location("textures/entity/projectiles/scythe/scythe_5.png"));
        map.put(6, Goety.location("textures/entity/projectiles/scythe/scythe_6.png"));
        map.put(7, Goety.location("textures/entity/projectiles/scythe/scythe_7.png"));
    });
    private ItemStack weapon = new ItemStack(ModItems.DEATH_SCYTHE.get());
    private float damage;
    private int lifespan;
    private int totallife;

    public ScytheProjectileEntity(EntityType<? extends DamagingProjectileEntity> p_i50173_1_, World p_i50173_2_) {
        super(p_i50173_1_, p_i50173_2_);
        this.damage = 7.5F;
        this.lifespan = 0;
        this.totallife = 60;
    }

    public ScytheProjectileEntity(ItemStack itemStack, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(ModEntityType.SCYTHE.get(), x, y, z, xSpeed, ySpeed, zSpeed, world);
        this.weapon = itemStack;
    }

    public ScytheProjectileEntity(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(ModEntityType.SCYTHE.get(), x, y, z, xSpeed, ySpeed, zSpeed, world);
    }

    public ResourceLocation getResourceLocation() {
        return TEXTURE_BY_TYPE.getOrDefault(this.getAnimation(), TEXTURE_BY_TYPE.get(0));
    }

    public int getAnimation() {
        return this.entityData.get(DATA_TYPE_ID);
    }

    public void setAnimation(int pType) {
        this.entityData.set(DATA_TYPE_ID, pType);
    }

    public float getDamage() {
        return this.damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public int getTotallife() {
        return totallife;
    }

    public void setTotallife(int totallife) {
        this.totallife = totallife;
    }

    public int getLifespan() {
        return lifespan;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }

    protected void defineSynchedData() {
        this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
        this.entityData.define(DATA_TYPE_ID, 0);
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        UUID uuid;
        if (compound.hasUUID("Owner")) {
            uuid = compound.getUUID("Owner");
        } else {
            String s = compound.getString("Owner");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (uuid != null) {
            try {
                this.setOwnerId(uuid);
            } catch (Throwable ignored) {
            }
        }
        this.setAnimation(compound.getInt("Animation"));

        if (compound.contains("Damage")) {
            this.setLifespan(compound.getInt("Damage"));
        }
        if (compound.contains("Lifespan")) {
            this.setLifespan(compound.getInt("Lifespan"));
        }
        if (compound.contains("TotalLife")) {
            this.setTotallife(compound.getInt("TotalLife"));
        }

    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        if (this.getOwnerId() != null) {
            compound.putUUID("Owner", this.getOwnerId());
        }
        compound.putInt("Animation", this.getAnimation());
        compound.putFloat("Damage", this.getDamage());
        compound.putInt("Lifespan", this.getLifespan());
        compound.putInt("TotalLife", this.getTotallife());
    }

    public LivingEntity getTrueOwner() {
        try {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : EntityFinder.getLivingEntityByUuiD(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    @Nullable
    public UUID getOwnerId() {
        return this.entityData.get(OWNER_UNIQUE_ID).orElse((UUID)null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }

    public void tick() {
        super.tick();
        if (this.lifespan < getTotallife()){
            ++this.lifespan;
        } else {
            this.remove();
        }
        if (this.getAnimation() < 7) {
            this.setAnimation(this.getAnimation() + 1);
        } else {
            this.setAnimation(0);
        }
        List<LivingEntity> targets = new ArrayList<>();
        for (LivingEntity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1.0F))) {
            if (this.getTrueOwner() != null) {
                if (entity != this.getTrueOwner() && !entity.isAlliedTo(this.getTrueOwner()) && !this.getTrueOwner().isAlliedTo(entity)) {
                    targets.add(entity);
                }
            } else {
                targets.add(entity);
            }
        }
        if (!targets.isEmpty()){
            for (LivingEntity entity: targets){
                if (MobUtil.validEntity(entity)) {
                    float f = this.getDamage();
                    if (this.getTrueOwner() != null) {
                        f += EnchantmentHelper.getDamageBonus(this.weapon, entity.getMobType());
                        if (entity.hurt(DamageSource.mobAttack(this.getTrueOwner()), f)) {
                            if (this.getTrueOwner() instanceof PlayerEntity) {
                                int enchantment = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SOULEATER.get(), this.weapon);
                                int soulEater = MathHelper.clamp(enchantment + 1, 1, 10);
                                PlayerEntity player = (PlayerEntity) this.getTrueOwner();
                                SEHelper.increaseSouls(player, MainConfig.DarkScytheSouls.get() * soulEater);
                            }
                        }
                    } else {
                        entity.hurt(DamageSource.MAGIC, f);
                    }
                }
            }
        }
    }

    protected void onHitBlock(BlockRayTraceResult p_230299_1_) {
        super.onHitBlock(p_230299_1_);
        this.remove();
    }

    public boolean isOnFire() {
        return false;
    }

    public boolean isPickable() {
        return false;
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    protected IParticleData getTrailParticle() {
        return ParticleTypes.CRIT;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
