package com.Polarice3.Goety.common.entities.utilities;

import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class BurningGroundEntity extends Entity {
    private int duration = 600;
    private LivingEntity owner;
    private UUID ownerUniqueId;

    public BurningGroundEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.noPhysics = true;
    }

    public BurningGroundEntity(World worldIn, double x, double y, double z) {
        this(ModEntityType.BURNING_GROUND.get(), worldIn);
        this.setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        this.duration = compound.getInt("Duration");

    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
        compound.putInt("Duration", this.duration);

    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int durationIn) {
        this.duration = durationIn;
    }

    public void tick() {
        super.tick();
        this.spawnParticles();
        List<LivingEntity> list1 = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
        if (!list1.isEmpty()){
            for(LivingEntity livingentity : list1) {
                if (!livingentity.fireImmune()) {
                    livingentity.setSecondsOnFire(8);
                }
            }
        }
        if (this.tickCount >= this.getDuration()) {
            this.remove();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void spawnParticles(){
        float f = 3.0F;
        float f5 = (float)Math.PI * f * f;
        for(int k1 = 0; (float)k1 < f5; ++k1) {
            float f6 = this.random.nextFloat() * ((float)Math.PI * 2F);
            float f7 = MathHelper.sqrt(this.random.nextFloat()) * f;
            float f8 = MathHelper.cos(f6) * f7;
            float f9 = MathHelper.sin(f6) * f7;
            new ParticleUtil(this.level, ParticleTypes.FLAME, this.getX() + (double)f8, this.getY(), this.getZ() + (double)f9, (0.5D - this.random.nextDouble()) * 0.15D, (double)0.01F, (0.5D - this.random.nextDouble()) * 0.15D);
        }
    }

    public void setOwner(@Nullable LivingEntity ownerIn) {
        this.owner = ownerIn;
        this.ownerUniqueId = ownerIn == null ? null : ownerIn.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUniqueId != null && this.level instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.level).getEntity(this.ownerUniqueId);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }

        return this.owner;
    }

    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return new SSpawnObjectPacket(this);
    }
}
