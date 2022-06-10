package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.blocks.TotemHeadBlock;
import com.Polarice3.Goety.common.entities.ally.LoyalSpiderEntity;
import com.Polarice3.Goety.common.entities.neutral.FlyingPhaseEntity;
import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.common.entities.projectiles.FangEntity;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModTileEntityType;
import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class SoulFangTotemTileEntity extends TileEntity implements ITickableTileEntity {
    public int activated;
    public int levels;
    @Nullable
    public LivingEntity target;
    @Nullable
    public UUID targetUuid;
    private UUID ownerUUID;

    public SoulFangTotemTileEntity() {
        super(ModTileEntityType.SOUL_FANG_TOTEM.get());
    }

    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    public World getLevel() {
        return SoulFangTotemTileEntity.this.level;
    }

    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        if (nbt.hasUUID("Target")) {
            this.targetUuid = nbt.getUUID("Target");
        } else {
            this.targetUuid = null;
        }
        UUID uuid;
        if (nbt.hasUUID("Owner")) {
            uuid = nbt.getUUID("Owner");
        } else {
            String s = nbt.getString("Owner");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.level.getServer(), s);
        }
        if (uuid != null) {
            try {
                this.setOwnerId(uuid);
            } catch (Throwable ignored) {
            }
        }

    }

    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        if (this.target != null) {
            compound.putUUID("Target", this.target.getUUID());
        }
        if (this.getOwnerId() != null) {
            compound.putUUID("Owner", this.getOwnerId());
        }

        return compound;
    }

    private void updateClientTarget() {
        this.target = this.findExistingTarget();
    }

    @Nullable
    public LivingEntity findExistingTarget() {
        if (this.getTrueOwner() != null) {
            int i = this.worldPosition.getX();
            int j = this.worldPosition.getY();
            int k = this.worldPosition.getZ();
            assert this.level != null;
            for (LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(10.0D, 10.0D, 10.0D))){
                if (livingEntity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) livingEntity;
                    if (MobUtil.playerValidity(player, false)) {
                        if (this.getPlayer() != player) {
                            return livingEntity;
                        }
                    }
                } else {
                    if (livingEntity instanceof OwnedEntity) {
                        OwnedEntity summonedEntity = (OwnedEntity) livingEntity;
                        if (summonedEntity.getTrueOwner() != this.getTrueOwner()) {
                            return livingEntity;
                        }
                    } else if (livingEntity instanceof TameableEntity) {
                        TameableEntity tameableEntity = (TameableEntity) livingEntity;
                        if (tameableEntity.getOwner() != this.getTrueOwner()){
                            return livingEntity;
                        }
                    } else if (livingEntity instanceof LoyalSpiderEntity) {
                        LoyalSpiderEntity loyalSpiderEntity = (LoyalSpiderEntity) livingEntity;
                        if (loyalSpiderEntity.getTrueOwner() != this.getTrueOwner()){
                            return livingEntity;
                        }
                    } else {
                        if (livingEntity.isOnGround()) {
                            return livingEntity;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void tick() {
        assert this.level != null;
        if (!this.level.isClientSide()) {
            int i = this.worldPosition.getX();
            int j = this.worldPosition.getY();
            int k = this.worldPosition.getZ();
            int j1 = this.levels;
            this.checkBeaconLevel(i, j, k);
            if (j1 >= 3) {
                this.updateClientTarget();
                this.SpawnParticles();
                long t = this.level.getGameTime();
                if (t % 40L == 0L && this.target != null){
                    this.activated = 20;
                    this.SpecialEffect();
                }
                if (this.activated != 0){
                    --this.activated;
                    this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(TotemHeadBlock.POWERED, true), 3);
                } else {
                    this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(TotemHeadBlock.POWERED, false), 3);
                }
            } else {
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(TotemHeadBlock.POWERED, false), 3);
            }
        }
    }

    private void checkBeaconLevel(int beaconXIn, int beaconYIn, int beaconZIn) {
        this.levels = 0;

        for(int i = 1; i <= 3; this.levels = i++) {
            int j = beaconYIn - i;
            if (j < 0) {
                break;
            }

            assert this.level != null;
            boolean flag = this.level.getBlockState(new BlockPos(beaconXIn, j, beaconZIn)).is(ModBlocks.CURSED_TOTEM_BLOCK.get());

            if (!flag) {
                break;
            }
        }

    }

    public void playSound(SoundEvent sound) {
        this.level.playSound(null, this.worldPosition, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public void setRemoved() {
        super.setRemoved();
    }

    private void SpawnParticles(){
        BlockPos blockpos = this.getBlockPos();
        Minecraft MINECRAFT = Minecraft.getInstance();

        if (MINECRAFT.level != null) {
            long t = MINECRAFT.level.getGameTime();
            double d0 = (double)blockpos.getX() + MINECRAFT.level.random.nextDouble();
            double d1 = (double)blockpos.getY() + MINECRAFT.level.random.nextDouble();
            double d2 = (double)blockpos.getZ() + MINECRAFT.level.random.nextDouble();
            if (this.activated != 0) {
                for (int p = 0; p < 4; ++p) {
                    MINECRAFT.level.addParticle(ModParticleTypes.TOTEM_EFFECT.get(), d0, d1, d2, 0.7, 0.7, 0.7);
                    MINECRAFT.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0, d1, d2, 0, 0, 0);
                }
            } else {
                if (t % 40L == 0L) {
                    for (int p = 0; p < 4; ++p) {
                        MINECRAFT.level.addParticle(ModParticleTypes.TOTEM_EFFECT.get(), d0, d1, d2, 0.45, 0.45, 0.45);
                    }
                }
            }
        }
    }

    public void SpecialEffect() {
        if (this.getTrueOwner() != null) {
            int i = this.worldPosition.getX();
            int j = this.worldPosition.getY();
            int k = this.worldPosition.getZ();
            this.playSound(SoundEvents.EVOKER_PREPARE_ATTACK);
            assert this.getLevel() != null;
            for (LivingEntity entity : this.getLevel().getEntitiesOfClass(LivingEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(10.0D, 10.0D, 10.0D))) {
                float f = (float) MathHelper.atan2(entity.getZ() - this.getBlockPos().getZ(), entity.getX() - this.getBlockPos().getX());
                if (entity instanceof PlayerEntity) {
                    if (this.getPlayer() != entity) {
                        if (MobUtil.playerValidity((PlayerEntity) entity, false)) {
                            this.spawnFangs(entity.getX(), entity.getZ(), entity.getY(), entity.getY() + 1.0D, f);
                        }
                    }
                } else {
                    if (entity instanceof OwnedEntity) {
                        OwnedEntity summonedEntity = (OwnedEntity) entity;
                        if (summonedEntity.getTrueOwner() != this.getTrueOwner()) {
                            this.spawnFangs(entity.getX(), entity.getZ(), entity.getY(), entity.getY() + 1.0D, f);
                        }
                    } else if (entity instanceof TameableEntity) {
                        TameableEntity tameableEntity = (TameableEntity) entity;
                        if (tameableEntity.getOwner() != this.getTrueOwner()){
                            this.spawnFangs(entity.getX(), entity.getZ(), entity.getY(), entity.getY() + 1.0D, f);
                        }
                    } else if (entity instanceof LoyalSpiderEntity) {
                        LoyalSpiderEntity loyalSpiderEntity = (LoyalSpiderEntity) entity;
                        if (loyalSpiderEntity.getTrueOwner() != this.getTrueOwner()){
                            this.spawnFangs(entity.getX(), entity.getZ(), entity.getY(), entity.getY() + 1.0D, f);
                        }
                    } else {
                        if (entity.isOnGround()) {
                            this.spawnFangs(entity.getX(), entity.getZ(), entity.getY(), entity.getY() + 1.0D, f);
                        }
                    }
                }
            }
        }
    }

    private void spawnFangs(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_) {
        BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            assert this.getLevel() != null;
            BlockState blockstate = this.getLevel().getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(this.getLevel(), blockpos1, Direction.UP)) {
                if (!this.getLevel().isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = this.getLevel().getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(this.getLevel(), blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= MathHelper.floor(p_190876_5_) - 1);

        if (flag) {
            this.getLevel().addFreshEntity(new FangEntity(this.getLevel(), p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, 1, this.getTrueOwner()));
        }

    }


    @Nullable
    public UUID getOwnerId() {
        return this.ownerUUID;
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.ownerUUID = p_184754_1_;
    }

    public LivingEntity getTrueOwner() {
        try {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : this.level.getPlayerByUUID(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    public PlayerEntity getPlayer(){
        return (PlayerEntity) this.getTrueOwner();
    }

}
