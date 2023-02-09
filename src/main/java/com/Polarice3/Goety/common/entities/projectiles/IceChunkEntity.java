package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.Polarice3.Goety.utils.ModMathHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class IceChunkEntity extends Entity {
    private LivingEntity owner;
    private UUID ownerUUID;
    private LivingEntity target;
    private UUID targetUUID;
    private final int distance = 4;
    private boolean isDropping;
    public float extraDamage = 0.0F;

    public IceChunkEntity(EntityType<? extends Entity> p_i50170_1_, World p_i50170_2_) {
        super(p_i50170_1_, p_i50170_2_);
    }

    public IceChunkEntity(World pLevel, LivingEntity pOwner, LivingEntity pTarget){
        this(ModEntityType.ICE_CHUNK.get(), pLevel);
        if (pTarget != null){
            this.setPos(pTarget.getX(), pTarget.getY() + distance, pTarget.getZ());
        }
        this.owner = pOwner;
        this.target = pTarget;
    }

    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT pCompound) {
        UUID ownerUUID;
        if (pCompound.hasUUID("Owner")) {
            ownerUUID = pCompound.getUUID("Owner");
        } else {
            String s = pCompound.getString("Owner");
            ownerUUID = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (ownerUUID != null) {
            this.ownerUUID = ownerUUID;
        }

        UUID targetUUID;
        if (pCompound.hasUUID("Target")) {
            targetUUID = pCompound.getUUID("Target");
        } else {
            String s = pCompound.getString("Target");
            targetUUID = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }

        if (targetUUID != null) {
            this.targetUUID = targetUUID;
        }
        this.extraDamage = pCompound.getFloat("extraDamage");
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT pCompound) {
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }
        if (this.targetUUID != null) {
            pCompound.putUUID("Target", this.targetUUID);
        }
        pCompound.putFloat("extraDamage", this.extraDamage);
    }

    public void setOwner(@Nullable LivingEntity p_190549_1_) {
        this.owner = p_190549_1_;
        this.ownerUUID = p_190549_1_ == null ? null : p_190549_1_.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.level).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }

        return this.owner;
    }

    public void setTarget(@Nullable LivingEntity p_190549_1_) {
        this.target = p_190549_1_;
        this.targetUUID = p_190549_1_ == null ? null : p_190549_1_.getUUID();
    }

    @Nullable
    public LivingEntity getTarget() {
        if (this.target == null && this.targetUUID != null && this.level instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.level).getEntity(this.targetUUID);
            if (entity instanceof LivingEntity) {
                this.target = (LivingEntity)entity;
            }
        }

        return this.target;
    }

    public void setExtraDamage(float extraDamage){
        this.extraDamage = extraDamage;
    }

    private void onHit() {
        if (!this.level.isClientSide()) {
            ServerWorld serverWorld = (ServerWorld) this.level;
            BlockState blockState = Blocks.PACKED_ICE.defaultBlockState();
            this.playSound(ModSounds.ICE_CHUNK_HIT.get(), 2.0F, 1.0F);
            serverWorld.sendParticles(new BlockParticleData(ParticleTypes.BLOCK, blockState), this.getX(), this.getY() + (this.getBbHeight()/2.0D), this.getZ(), 256, this.getBbWidth()/2.0D, this.getBbHeight()/2.0D, this.getBbWidth()/2.0D, 1.0D);
            if (this.isDropping){
                for (LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2.0D, 0.0D, 2.0D), this::canHitEntity)){
                    this.damageTargets(livingEntity);
                }
            }
        }
        this.remove();
    }

    public void damageTargets(LivingEntity livingEntity){
        float damage = SpellConfig.IceChunkDamage.get().floatValue() * SpellConfig.SpellDamageMultiplier.get();
        damage += this.extraDamage;
        if (livingEntity != null) {
            if (livingEntity.hurt(ModDamageSource.indirectFrost(this, this.getOwner()), damage)) {
                livingEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, ModMathHelper.ticksToSeconds(5), 4));
            }
        }
    }

    public void setParticleAura(IParticleData particleAura, float radius, double pX, double pY, double pZ){
        if (!this.level.isClientSide){
            ServerWorld serverWorld = (ServerWorld) this.level;
            float f5 = (float) Math.PI * radius * radius;
            for (int k1 = 0; (float) k1 < f5; ++k1) {
                float f6 = this.random.nextFloat() * ((float) Math.PI * 2F);
                float f7 = MathHelper.sqrt(this.random.nextFloat()) * radius;
                float f8 = MathHelper.cos(f6) * f7;
                float f9 = MathHelper.sin(f6) * f7;
                serverWorld.sendParticles(particleAura, pX + (double) f8, pY, pZ + (double) f9, 1, 0, 0, 0, 0);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide){
            ServerWorld serverWorld = (ServerWorld) this.level;
            RayTraceResult result = ProjectileHelper.getHitResult(this, this::canHitEntity);
            if (result.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, result)) {
                if (result.getType() == RayTraceResult.Type.ENTITY){
                    EntityRayTraceResult result1 = (EntityRayTraceResult) result;
                    if (result1.getEntity() instanceof LivingEntity){
                        this.damageTargets((LivingEntity) result1.getEntity());
                    }
                    for (int i = 0; (float) i < 8; ++i) {
                        this.setParticleAura(ParticleTypes.POOF, 3.0F, result1.getEntity().getX(), result1.getEntity().getY(), result1.getEntity().getZ());
                    }
                } else if (result.getType() == RayTraceResult.Type.BLOCK){
                    for (int i = 0; (float) i < 8; ++i) {
                        this.setParticleAura(ParticleTypes.POOF, 3.0F, this.getX(), this.getY(), this.getZ());
                    }
                }
                this.onHit();
            }
            if (this.isOnGround() || this.isInWall() || this.verticalCollision || this.horizontalCollision){
                this.onHit();
            }
            if (!this.isDropping){
                this.setParticleAura(ParticleTypes.CLOUD, 1.5F, this.getX(), this.getY(), this.getZ());
            }
            BlockPos blockpos = this.blockPosition().below();
            BlockState blockState = Blocks.SNOW_BLOCK.defaultBlockState();
            if (serverWorld.isEmptyBlock(blockpos)) {
                this.setParticleAura(new BlockParticleData(ParticleTypes.FALLING_DUST, blockState), 1.5F, this.getX(), this.getY(), this.getZ());
            }
        }
        if (this.tickCount == 1 && !(this.isInWall() || this.isOnGround())){
            this.playSound(ModSounds.ICE_CHUNK_IDLE.get(), 1.0F, 1.0F);
        }
        int hoverTime = ModMathHelper.ticksToSeconds(5);
        boolean isHovering = this.tickCount < hoverTime;
        this.isDropping = this.tickCount > hoverTime;
        if (isHovering){
            if (this.getTarget() != null){
                this.setPos(this.getTarget().getX(), this.getTarget().getY() + distance, this.getTarget().getZ());
            }
        } else {
            if (!this.isDropping){
                this.setDeltaMovement(Vector3d.ZERO);
            } else {
                this.setDeltaMovement(this.getDeltaMovement().subtract(0.0D, 0.25D, 0.0D));
            }
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    public boolean isAttackable() {
        return false;
    }

    @Override
    protected boolean isMovementNoisy() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return !this.removed;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    protected boolean canHitEntity(Entity entity) {
        if (!entity.isSpectator() && entity.isAlive() && entity.isPickable() && !entity.noPhysics) {
            Entity owner = this.getOwner();
            return owner == null || !owner.isPassengerOfSameVehicle(entity);
        } else {
            return false;
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
