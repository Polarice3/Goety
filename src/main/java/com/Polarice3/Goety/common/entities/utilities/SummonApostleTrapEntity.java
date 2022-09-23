package com.Polarice3.Goety.common.entities.utilities;

import com.Polarice3.Goety.common.entities.bosses.ApostleEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModSounds;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SummonApostleTrapEntity extends Entity {
    public SummonApostleTrapEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {

    }

    public void tick() {
        super.tick();
        if (this.tickCount == 150) {
            this.playSound(SoundEvents.AMBIENT_NETHER_WASTES_MOOD, 1.0F, 1.0F);
            for (PlayerEntity player: this.level.getEntitiesOfClass(PlayerEntity.class, this.getBoundingBox().inflate(32))){
                player.displayClientMessage(new TranslationTextComponent("info.goety.apostle.summon"), true);
            }
        }
        if (this.tickCount == 300) {
            this.playSound(ModSounds.APOSTLE_AMBIENT.get(), 1.0F, 1.0F);
        }
        if (this.tickCount == 450){
            this.playSound(SoundEvents.LIGHTNING_BOLT_THUNDER, 1.0F, 1.0F);
        }
        if (!this.level.isClientSide) {
            ServerWorld serverWorld = (ServerWorld) this.level;
            if (serverWorld.getDifficulty() == Difficulty.PEACEFUL){
                this.remove();
            }
            float f = 3.0F;
            float f5 = (float)Math.PI * f * f;
            for(int k1 = 0; (float)k1 < f5; ++k1) {
                float f6 = this.random.nextFloat() * ((float)Math.PI * 2F);
                float f7 = MathHelper.sqrt(this.random.nextFloat()) * f;
                float f8 = MathHelper.cos(f6) * f7;
                float f9 = MathHelper.sin(f6) * f7;
                serverWorld.sendParticles(ParticleTypes.SMOKE, this.getX() + (double)f8, this.getY(), this.getZ() + (double)f9, 1, (0.5D - this.random.nextDouble()) * 0.15D, (double)0.01F, (0.5D - this.random.nextDouble()) * 0.15D, (0.5D - this.random.nextDouble()) * 0.15D);
            }
            if (this.tickCount == 450){
                for(int k = 0; k < 200; ++k) {
                    float f2 = random.nextFloat() * 4.0F;
                    float f1 = random.nextFloat() * ((float)Math.PI * 2F);
                    double d1 = MathHelper.cos(f1) * f2;
                    double d2 = 0.01D + random.nextDouble() * 0.5D;
                    double d3 = MathHelper.sin(f1) * f2;
                    serverWorld.sendParticles(ParticleTypes.FLAME, this.getX() + d1 * 0.1D, this.getY() + 0.3D, this.getZ() + d3 * 0.1D, 0, d1, d2, d3, 0.5F);
                }
                ApostleEntity apostleEntity = new ApostleEntity(ModEntityType.APOSTLE.get(), this.level);
                apostleEntity.setPos(this.getX(), this.getY(), this.getZ());
                apostleEntity.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(this.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                serverWorld.addFreshEntity(apostleEntity);
                this.remove();
            }
        }
    }

    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return new SSpawnObjectPacket(this);
    }
}
