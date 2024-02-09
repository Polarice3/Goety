package com.Polarice3.Goety.common.blocks.tiles;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.init.ModTileEntityType;
import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

public class HookBellTileEntity extends TileEntity implements ITickableTileEntity {
    private long lastRingTimestamp;
    public int ticks;
    public boolean shaking;
    public Direction clickDirection;
    private List<LivingEntity> nearbyEntities;
    private boolean resonating;
    private int resonationTicks;
    private static final double minRange = 16.0D;
    private static final double maxRange = 128.0D;

    public HookBellTileEntity() {
        super(ModTileEntityType.HOOK_BELL.get());
    }

    public boolean triggerEvent(int p_58837_, int p_58838_) {
        if (p_58837_ == 1) {
            this.updateEntities();
            this.resonationTicks = 0;
            this.clickDirection = Direction.from3DDataValue(p_58838_);
            this.ticks = 0;
            this.shaking = true;
            return true;
        } else {
            return super.triggerEvent(p_58837_, p_58838_);
        }
    }

    public void tick() {
        if (this.shaking) {
            ++this.ticks;
            if (this.level instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) this.level;
                for (int i = 0; i < 2; ++i) {
                    serverWorld.sendParticles(ParticleTypes.PORTAL, this.getBlockPos().getX() + 0.5D, this.getBlockPos().getY() + 0.5D, this.getBlockPos().getZ() + 0.5D, 0, (this.level.random.nextDouble() - 0.5D) * 2.0D, -this.level.random.nextDouble(), (this.level.random.nextDouble() - 0.5D) * 2.0D, 0.5F);
                }
            }
        }

        if (this.ticks >= 50) {
            this.shaking = false;
            this.ticks = 0;
        }

        if (this.ticks >= 5 && this.resonationTicks == 0 && areRaidersNearby()) {
            this.resonating = true;
            this.playResonateSound();
        }

        if (this.resonating) {
            if (this.resonationTicks < 40) {
                ++this.resonationTicks;
            } else {
                teleportRaiders();
                this.resonating = false;
            }
        }

    }

    private void playResonateSound() {
        this.level.playSound((PlayerEntity)null, this.getBlockPos(), SoundEvents.BELL_RESONATE, SoundCategory.BLOCKS, 1.0F, 0.5F);
    }

    public void onHit(Direction p_58835_) {
        BlockPos blockpos = this.getBlockPos();
        this.clickDirection = p_58835_;
        if (this.shaking) {
            this.ticks = 0;
        } else {
            this.shaking = true;
        }

        if (this.level != null) {
            this.level.blockEvent(blockpos, this.getBlockState().getBlock(), 1, p_58835_.get3DDataValue());
        }
    }

    private void updateEntities() {
        if (this.level != null) {
            BlockPos blockpos = this.getBlockPos();
            if (this.level.getGameTime() > this.lastRingTimestamp + 60L || this.nearbyEntities == null) {
                this.lastRingTimestamp = this.level.getGameTime();
                AxisAlignedBB aabb = (new AxisAlignedBB(blockpos)).inflate(maxRange);
                this.nearbyEntities = this.level.getEntitiesOfClass(LivingEntity.class, aabb);
            }

            if (!this.level.isClientSide) {
                for (LivingEntity livingentity : this.nearbyEntities) {
                    if (livingentity.isAlive() && !livingentity.removed && blockpos.closerThan(livingentity.position(), maxRange)) {
                        livingentity.getBrain().setMemory(MemoryModuleType.HEARD_BELL_TIME, this.level.getGameTime());
                    }
                }
            }
        }

    }

    private boolean areRaidersNearby() {
        for(LivingEntity livingentity : this.nearbyEntities) {
            if (isRaiderWithinRange(livingentity)) {
                return true;
            }
        }

        return false;
    }

    private boolean areRaidersClose(LivingEntity living){
        return this.worldPosition.closerThan(living.position(), maxRange) && !this.worldPosition.closerThan(living.position(), minRange);
    }

    private void teleportRaiders() {
        this.nearbyEntities.stream().filter(this::isRaiderWithinRange).forEach(this::teleport);
    }

    private boolean isRaiderWithinRange(LivingEntity p_155198_) {
        return p_155198_.isAlive() && !p_155198_.removed && areRaidersClose(p_155198_) && p_155198_.getType().is(EntityTypeTags.RAIDERS) && !MobUtil.hasEntityTypesConfig(MainConfig.HookBellBlackList.get(), p_155198_.getType());
    }

    private void teleport(LivingEntity p_58841_) {
        for (int i = 0; i < 128; ++i) {
            BlockPos blockPos = this.worldPosition;
            double d3 = blockPos.getX() + (p_58841_.getRandom().nextDouble() - 0.5D) * 8;
            double d4 = blockPos.getY() + (double) (p_58841_.getRandom().nextInt(16) - 8);
            double d5 = blockPos.getZ() + (p_58841_.getRandom().nextDouble() - 0.5D) * 8;
            if (p_58841_.randomTeleport(d3, d4, d5, true)) {
                p_58841_.level.playSound((PlayerEntity) null, p_58841_.xo, p_58841_.yo, p_58841_.zo, SoundEvents.ENDERMAN_TELEPORT, p_58841_.getSoundSource(), 1.0F, 1.0F);
                p_58841_.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                break;
            }
        }
    }
}
