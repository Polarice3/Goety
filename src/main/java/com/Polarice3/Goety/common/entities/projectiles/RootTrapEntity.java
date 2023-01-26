package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.ModDamageSource;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class RootTrapEntity extends GroundProjectileEntity {

    public RootTrapEntity(EntityType<?> pType, World pLevel) {
        super(pType, pLevel);
        this.lifeTicks = 100;
    }

    public RootTrapEntity(World world, double pPosX, double pPosY, double pPosZ, int pWarmUp, LivingEntity owner) {
        this(ModEntityType.ROOTS.get(), world);
        this.warmupDelayTicks = pWarmUp;
        this.setOwner(owner);
        this.setPos(pPosX, pPosY, pPosZ);
    }

    public RootTrapEntity(World world, BlockPos blockPos, int pWarmUp, LivingEntity owner) {
        this(ModEntityType.ROOTS.get(), world);
        this.warmupDelayTicks = pWarmUp;
        this.setOwner(owner);
        this.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (this.sentTrapEvent) {
                --this.lifeTicks;
                if (this.animationTicks > 9){
                    --this.animationTicks;
                }
            }
        } else if (--this.warmupDelayTicks < 0) {
            for(LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.2D, 0.0D, 0.2D))) {
                this.dealDamageTo(livingentity);
            }

            if (!this.playSound){
                this.level.broadcastEntityEvent(this, (byte)5);
                this.playSound = true;
            }

            if (!this.sentTrapEvent) {
                this.level.broadcastEntityEvent(this, (byte)4);
                this.sentTrapEvent = true;
            }

            if (--this.lifeTicks < 0) {
                this.remove();
            }
        }

    }

    private void dealDamageTo(LivingEntity target) {
        LivingEntity livingentity = this.getOwner();
        if (target.isAlive() && !target.isInvulnerable() && target != livingentity) {
            if (livingentity == null) {
                if (target.hurt(ModDamageSource.ROOTS, 1.0F)){
                    this.level.broadcastEntityEvent(target, (byte) 33);
                    target.addEffect(new EffectInstance(ModEffects.TRAPPED.get(), 20, 0, false, false, false));
                }
            } else {
                if (target.isAlliedTo(livingentity)){
                    return;
                }
                if (livingentity.isAlliedTo(target)) {
                    return;
                }
                if (target.hurt(ModDamageSource.roots(this, livingentity), 1.0F)){
                    this.level.broadcastEntityEvent(target, (byte) 33);
                    target.addEffect(new EffectInstance(ModEffects.TRAPPED.get(), 20, 0, false, false, false));
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte pId) {
        super.handleEntityEvent(pId);
        if (pId == 5) {
            this.level.playLocalSound(this.getX(), this.getY(), this.getZ(), ModSounds.ROT_TREE_ROOTS.get(), this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.2F + 0.85F, false);
            int i = MathHelper.floor(this.getX());
            int j = MathHelper.floor(this.getY() - (double)0.2F);
            int k = MathHelper.floor(this.getZ());
            BlockPos pos = new BlockPos(i, j, k);
            BlockState blockstate = this.level.getBlockState(pos);
            if (!blockstate.isAir(this.level, pos)) {
                this.level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, blockstate).setPos(pos), this.getX() + ((double)this.random.nextFloat() - 0.5D) * (double)this.getBbWidth(), this.getY() + 0.1D, this.getZ() + ((double)this.random.nextFloat() - 0.5D) * (double)this.getBbWidth(), 4.0D * ((double)this.random.nextFloat() - 0.5D), 0.5D, ((double)this.random.nextFloat() - 0.5D) * 4.0D);
            }
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
