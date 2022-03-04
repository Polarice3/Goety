package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.PatrollerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;

import java.util.Random;


public class CultistsSpawner {
    private int ticksUntilSpawn;

    public int tick(ServerWorld world) {
        Random random = world.random;
        if (!MainConfig.CultistsSpawn.get()) {
            return 0;
        } else {
            --this.ticksUntilSpawn;
            if (this.ticksUntilSpawn > 0) {
                return 0;
            } else {
                this.ticksUntilSpawn += MainConfig.CultistSpawnFreq.get();
                if (!world.isDay()) {
                    int j = world.players().size();
                    if (j < 1) {
                        return 0;
                    } else {
                        PlayerEntity playerentity = world.players().get(random.nextInt(j));
                        if (playerentity.isSpectator()) {
                            return 0;
                        } else if (world.isCloseToVillage(playerentity.blockPosition(), 2)) {
                            return 0;
                        } else {
                            int k = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                            int l = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
                            BlockPos.Mutable blockpos$mutable = playerentity.blockPosition().mutable().move(k, 0, l);
                            if (!world.hasChunksAt(blockpos$mutable.getX() - 10, blockpos$mutable.getY() - 10, blockpos$mutable.getZ() - 10, blockpos$mutable.getX() + 10, blockpos$mutable.getY() + 10, blockpos$mutable.getZ() + 10)) {
                                return 0;
                            } else {
                                Biome biome = world.getBiome(blockpos$mutable);
                                Biome.Category biome$category = biome.getBiomeCategory();
                                if (biome$category == Biome.Category.MUSHROOM) {
                                    return 0;
                                } else {
                                    int i1 = 0;
                                    int j1 = (int) Math.ceil((double) world.getCurrentDifficultyAt(blockpos$mutable).getEffectiveDifficulty()) + 1;

                                    for (int k1 = 0; k1 < j1; ++k1) {
                                        ++i1;
                                        blockpos$mutable.setY(world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
                                        if (k1 == 0) {
                                            if (!this.spawnFanatics(world, blockpos$mutable, random)) {
                                                break;
                                            }
                                        } else {
                                            this.spawnFanatics(world, blockpos$mutable, random);
                                        }

                                        blockpos$mutable.setX(blockpos$mutable.getX() + random.nextInt(5) - random.nextInt(5));
                                        blockpos$mutable.setZ(blockpos$mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                                    }
                                    if (j1 >= 3) {
                                        blockpos$mutable.setY(world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
                                        blockpos$mutable.setX(blockpos$mutable.getX() + random.nextInt(5) - random.nextInt(5));
                                        blockpos$mutable.setZ(blockpos$mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                                        this.spawnWitch(world, blockpos$mutable, random);
                                    }

                                    if (world.getDifficulty() == Difficulty.HARD){
                                        blockpos$mutable.setY(world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
                                        blockpos$mutable.setX(blockpos$mutable.getX() + random.nextInt(5) - random.nextInt(5));
                                        blockpos$mutable.setZ(blockpos$mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                                        this.spawnApostle(world, blockpos$mutable, random);
                                    }

                                    return i1;
                                }
                            }
                        }
                    }
                } else {
                    return 0;
                }
            }
        }
    }

    private boolean spawnFanatics(ServerWorld worldIn, BlockPos p_222695_2_, Random random) {
        BlockState blockstate = worldIn.getBlockState(p_222695_2_);
        if (!WorldEntitySpawner.isValidEmptySpawnBlock(worldIn, p_222695_2_, blockstate, blockstate.getFluidState(), ModEntityType.FANATIC.get())) {
            return false;
        } else if (!PatrollerEntity.checkPatrollingMonsterSpawnRules(ModEntityType.FANATIC.get(), worldIn, SpawnReason.PATROL, p_222695_2_, random)) {
            return false;
        } else {
            PatrollerEntity entity = null;
            int random1 = worldIn.random.nextInt(3);
            switch (random1){
                case 0:
                    entity = ModEntityType.FANATIC.get().create(worldIn);
                    break;
                case 1:
                    entity = ModEntityType.ZEALOT.get().create(worldIn);
                    break;
                case 2:
                    entity = ModEntityType.DISCIPLE.get().create(worldIn);
                    break;
            }
            PatrollerEntity patrollerentity = entity;
            if (patrollerentity != null) {
                patrollerentity.setPos((double)p_222695_2_.getX(), (double)p_222695_2_.getY(), (double)p_222695_2_.getZ());
                if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(patrollerentity, worldIn, p_222695_2_.getX(), p_222695_2_.getY(), p_222695_2_.getZ(), null, SpawnReason.PATROL) == -1) return false;
                patrollerentity.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(p_222695_2_), SpawnReason.PATROL, (ILivingEntityData)null, (CompoundNBT)null);
                worldIn.addFreshEntityWithPassengers(patrollerentity);
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean spawnWitch(ServerWorld worldIn, BlockPos p_222695_2_, Random random) {
        BlockState blockstate = worldIn.getBlockState(p_222695_2_);
        if (!WorldEntitySpawner.isValidEmptySpawnBlock(worldIn, p_222695_2_, blockstate, blockstate.getFluidState(), EntityType.WITCH)) {
            return false;
        } else if (!PatrollerEntity.checkPatrollingMonsterSpawnRules(EntityType.WITCH, worldIn, SpawnReason.PATROL, p_222695_2_, random)) {
            return false;
        } else {
            int random1 = worldIn.random.nextInt(2);
            PatrollerEntity patrollerentity = random1 == 0 ? ModEntityType.CHANNELLER.get().create(worldIn): EntityType.WITCH.create(worldIn);
            if (patrollerentity != null) {
                patrollerentity.setPos((double)p_222695_2_.getX(), (double)p_222695_2_.getY(), (double)p_222695_2_.getZ());
                if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(patrollerentity, worldIn, p_222695_2_.getX(), p_222695_2_.getY(), p_222695_2_.getZ(), null, SpawnReason.PATROL) == -1) return false;
                patrollerentity.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(p_222695_2_), SpawnReason.PATROL, (ILivingEntityData)null, (CompoundNBT)null);
                worldIn.addFreshEntityWithPassengers(patrollerentity);
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean spawnApostle(ServerWorld worldIn, BlockPos p_222695_2_, Random random) {
        BlockState blockstate = worldIn.getBlockState(p_222695_2_);
        if (!WorldEntitySpawner.isValidEmptySpawnBlock(worldIn, p_222695_2_, blockstate, blockstate.getFluidState(), ModEntityType.APOSTLE.get())) {
            return false;
        } else if (!PatrollerEntity.checkPatrollingMonsterSpawnRules(ModEntityType.APOSTLE.get(), worldIn, SpawnReason.PATROL, p_222695_2_, random)) {
            return false;
        } else {
            PatrollerEntity patrollerentity = ModEntityType.APOSTLE.get().create(worldIn);
            if (patrollerentity != null) {
                patrollerentity.setPos((double)p_222695_2_.getX(), (double)p_222695_2_.getY(), (double)p_222695_2_.getZ());
                if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(patrollerentity, worldIn, p_222695_2_.getX(), p_222695_2_.getY(), p_222695_2_.getZ(), null, SpawnReason.PATROL) == -1) return false;
                patrollerentity.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(p_222695_2_), SpawnReason.PATROL, (ILivingEntityData)null, (CompoundNBT)null);
                worldIn.addFreshEntityWithPassengers(patrollerentity);
                return true;
            } else {
                return false;
            }
        }
    }
}
