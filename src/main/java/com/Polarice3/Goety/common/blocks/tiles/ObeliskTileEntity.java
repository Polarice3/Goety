package com.Polarice3.Goety.common.blocks.tiles;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.blocks.ObeliskBlock;
import com.Polarice3.Goety.common.blocks.TotemHeadBlock;
import com.Polarice3.Goety.common.entities.hostile.HuskarlEntity;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModTileEntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

public class ObeliskTileEntity extends TileEntity implements ITickableTileEntity {
    public int activated;
    private int spawnDelay = 20;

    public ObeliskTileEntity() {
        this(ModTileEntityType.OBELISK.get());
    }

    public ObeliskTileEntity(TileEntityType<?> p_i48929_1_) {
        super(p_i48929_1_);
    }

    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    public World getLevel() {
        return ObeliskTileEntity.this.level;
    }
    
    @Override
    public void tick() {
        assert this.level != null;
        if (!this.level.isClientSide()) {
            if (this.level.getBlockState(this.worldPosition.below()).getBlock() == ModBlocks.CURSED_TOTEM_BLOCK.get()){
                this.serverParticles();
                int i = this.worldPosition.getX();
                int j = this.worldPosition.getY();
                int k = this.worldPosition.getZ();
                List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(8.0D, 8.0D, 8.0D));
                if (list.size() < 8){
                    if (this.spawnDelay > 0) {
                        --this.spawnDelay;
                    } else {
                        this.spawnDelay = this.level.random.nextInt(400) + 200;
                        this.activated = 20;
                        int spawnRange = 4;
                        for (int p = 0; p < 1 + this.level.random.nextInt(2); ++p) {
                            double d0 = (double)this.getBlockPos().getX() + (this.level.random.nextDouble() - this.level.random.nextDouble()) * (double) spawnRange + 0.5D;
                            double d1 = (double)(this.getBlockPos().getY() + this.level.random.nextInt(3));
                            double d2 = (double)this.getBlockPos().getZ() + (this.level.random.nextDouble() - this.level.random.nextDouble()) * (double) spawnRange + 0.5D;
                            HuskarlEntity zombieEntity = ModEntityType.HUSKARL.get().create(this.level);
                            assert zombieEntity != null;
                            zombieEntity.moveTo(d0, d1, d2);
                            zombieEntity.finalizeSpawn((IServerWorld) this.level, this.level.getCurrentDifficultyAt(zombieEntity.blockPosition()), SpawnReason.SPAWNER, (ILivingEntityData)null, (CompoundNBT)null);
                            zombieEntity.spawnAnim();
                            ((IServerWorld) this.level).addFreshEntityWithPassengers(zombieEntity);
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
                    serverWorld.sendParticles(ParticleTypes.FLAME, d0, d1, d2, 1, 0, 0, 0, 0);
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
}
