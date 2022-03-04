package com.Polarice3.Goety.common.entities.neutral;

import com.Polarice3.Goety.common.entities.hostile.ParasiteEntity;
import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class MutatedEntity extends AnimalEntity {
    private static final DataParameter<Integer> STATE = EntityDataManager.defineId(CreeperEntity.class, DataSerializers.INT);
    private static final DataParameter<Boolean> IGNITED = EntityDataManager.defineId(CreeperEntity.class, DataSerializers.BOOLEAN);
    private int timeSinceIgnited;
    private int lastActiveTime;
    private int fuseTime = 20;

    protected MutatedEntity(EntityType<? extends MutatedEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
        return null;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STATE, -1);
        this.entityData.define(IGNITED, false);
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);

        compound.putShort("Fuse", (short)this.fuseTime);
        compound.putBoolean("ignited", this.hasIgnited());
    }

    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Fuse", 99)) {
            this.fuseTime = compound.getShort("Fuse");
        }

        if (compound.getBoolean("ignited")) {
            this.ignite();
        }

    }

    public boolean hasIgnited() {
        return this.entityData.get(IGNITED);
    }

    public void ignite() {
        this.entityData.set(IGNITED, true);
    }

    public int getMutatedState() {
        return this.entityData.get(STATE);
    }

    public void setMutatedState(int state) {
        this.entityData.set(STATE, state);
    }

    @OnlyIn(Dist.CLIENT)
    public float getMutatedFlashIntensity(float partialTicks) {
        return MathHelper.lerp(partialTicks, (float)this.lastActiveTime, (float)this.timeSinceIgnited) / (float)(this.fuseTime - 2);
    }

    public void tick() {
        if (this.isAlive()) {
            if (!this.hasIgnited()){
                int random = this.level.random.nextInt(36000) + 12000;
                if (this.tickCount >= random){
                    this.ignite();
                }
            }
            this.lastActiveTime = this.timeSinceIgnited;
            if (this.hasIgnited()) {
                this.setMutatedState(1);
            }

            int i = this.getMutatedState();
            if (i > 0 && this.timeSinceIgnited == 0) {
                this.playSound(SoundEvents.SKELETON_HORSE_DEATH, 1.0F, 0.5F);
            }

            this.timeSinceIgnited += i;
            if (this.timeSinceIgnited < 0) {
                this.timeSinceIgnited = 0;
            }

            if (this.timeSinceIgnited >= this.fuseTime) {
                this.timeSinceIgnited = this.fuseTime;
                this.explode();
            }
        }

        super.tick();
    }

    private void explode() {
        if (!this.level.isClientSide) {
            this.dead = true;
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), 1.0F, Explosion.Mode.NONE);
            this.remove();
            for (int i = 0; i < 8 + this.level.random.nextInt(8); ++i) {
                ParasiteEntity parasiteEntity = new ParasiteEntity(ModEntityType.PARASITE.get(), level);
                parasiteEntity.setPos(this.getX(), this.getY(), this.getZ());
                parasiteEntity.setAttackAll(true);
                level.addFreshEntity(parasiteEntity);
            }
        }

    }


    public void die(DamageSource cause) {
        if (cause.getEntity() instanceof MutatedRabbitEntity) {
            ParasiteEntity parasiteEntity = new ParasiteEntity(ModEntityType.PARASITE.get(), level);
            parasiteEntity.setPos(this.getX(), this.getY(), this.getZ());
            parasiteEntity.setAttackAll(true);
            level.addFreshEntity(parasiteEntity);
        } else {
            int random = this.level.random.nextInt(8);
            if (random == 0) {
                for (int i = 0; i < 4 + this.level.random.nextInt(4); ++i) {
                    ParasiteEntity parasiteEntity = new ParasiteEntity(ModEntityType.PARASITE.get(), level);
                    parasiteEntity.setPos(this.getX(), this.getY(), this.getZ());
                    parasiteEntity.setAttackAll(true);
                    level.addFreshEntity(parasiteEntity);
                }
            }
        }
        super.die(cause);
    }

}
