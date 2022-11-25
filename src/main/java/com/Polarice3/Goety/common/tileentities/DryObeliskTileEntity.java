package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.blocks.DeadSandBlock;
import com.Polarice3.Goety.common.blocks.IDeadBlock;
import com.Polarice3.Goety.common.blocks.ObeliskBlock;
import com.Polarice3.Goety.common.blocks.TotemHeadBlock;
import com.Polarice3.Goety.init.ModTileEntityType;
import com.Polarice3.Goety.utils.BlockFinder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DryObeliskTileEntity extends TileEntity implements ITickableTileEntity {
    public int activated;

    public DryObeliskTileEntity() {
        this(ModTileEntityType.DRY_OBELISK.get());
    }

    public DryObeliskTileEntity(TileEntityType<?> p_i48929_1_) {
        super(p_i48929_1_);
    }

    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    public World getLevel() {
        return DryObeliskTileEntity.this.level;
    }
    
    @Override
    public void tick() {
        assert this.level != null;
        if (!this.level.isClientSide()) {
            ServerWorld serverWorld = (ServerWorld) this.level;
            this.serverParticles();
            List<BlockPos> deadSandsPos = new ArrayList<>();
            List<Block> result = new ArrayList<>();
            for (int j1 = -4; j1 < 4; ++j1) {
                for (int k1 = -4; k1 <= 4; ++k1) {
                    for (int l1 = -4; l1 < 4; ++l1) {
                        BlockPos blockpos2 = this.worldPosition.offset(j1, k1, l1);
                        BlockState blockState2 = this.level.getBlockState(blockpos2);
                        if (blockState2.getBlock() instanceof DeadSandBlock) {
                            int radius = 8;
                            Iterable<BlockPos> blocksToCheck = BlockPos.betweenClosed(
                                    blockpos2.offset(-radius, -radius, -radius),
                                    blockpos2.offset(radius, radius, radius));
                            for (BlockPos blockToCheck : blocksToCheck) {
                                BlockState blockState = this.level.getBlockState(blockToCheck);
                                if (blockState.getBlock() instanceof IDeadBlock){
                                    for(Direction direction : Direction.values()) {
                                        BlockPos blockpos1 = blockToCheck.relative(direction);
                                        BlockState blockstate = this.level.getBlockState(blockpos1);
                                        if (BlockFinder.ActivateDeadSand(blockstate)) {
                                            result.add(blockState.getBlock());
                                        }
                                    }
                                }
                            }
                            if (result.size() < 32) {
                                deadSandsPos.add(blockpos2);
                            }
                        }
                    }
                }
            }
            if (!deadSandsPos.isEmpty()){
                if (MainConfig.DeadSandSpread.get()) {
                    this.activated = 20;
                    long t = this.level.getGameTime();
                    if (t % 40 == 0) {
                        for (BlockPos blockPos1 : deadSandsPos) {
                            if (this.level.getBlockState(blockPos1).getBlock() instanceof DeadSandBlock) {
                                DeadSandBlock deadSandBlock = (DeadSandBlock) this.level.getBlockState(blockPos1).getBlock();
                                deadSandBlock.forceSpread(serverWorld, blockPos1, this.level.random);
                            }
                        }
                    }
                }
            }
            if (this.activated != 0){
                --this.activated;
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(ObeliskBlock.POWERED, true), 3);
            } else {
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(ObeliskBlock.POWERED, false), 3);
            }
        }
    }

    public void playSound(SoundEvent sound) {
        this.level.playSound(null, this.worldPosition, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    private void serverParticles(){
        ServerWorld serverWorld = (ServerWorld) this.level;
        BlockPos blockpos = this.getBlockPos();
        if (serverWorld != null) {
            long t = serverWorld.getGameTime();
            double d0 = (double)blockpos.getX() + serverWorld.random.nextDouble();
            double d1 = (double)blockpos.getY() + serverWorld.random.nextDouble();
            double d2 = (double)blockpos.getZ() + serverWorld.random.nextDouble();
            if (serverWorld.getBlockState(blockpos).getValue(TotemHeadBlock.POWERED)) {
                for (int p = 0; p < 4; ++p) {
                    serverWorld.sendParticles(ModParticleTypes.TOTEM_EFFECT.get(), d0, d1, d2, 0, 0.7, 0.7, 0.7, 0.5F);
                    serverWorld.sendParticles(ParticleTypes.SMOKE, d0, d1, d2, 1, 0, 0, 0, 0);
                }
            } else {
                if (t % 40L == 0L) {
                    for (int p = 0; p < 4; ++p) {
                        serverWorld.sendParticles(ModParticleTypes.TOTEM_EFFECT.get(), d0, d1, d2, 0, 0.45, 0.45, 0.45, 0.5F);
                    }
                }
            }
        }
    }

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, -1, this.getUpdateTag());
    }
}
