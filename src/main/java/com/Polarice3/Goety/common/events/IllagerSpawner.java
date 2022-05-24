package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.ai.MoveTowardsTargetGoal;
import com.Polarice3.Goety.common.entities.hostile.illagers.ConquillagerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.EnviokerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.InquillagerEntity;
import com.Polarice3.Goety.common.infamy.IInfamy;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.InfamyHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.PatrollerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;

import java.util.Random;

public class IllagerSpawner {
    private int ticksUntilSpawn;

    public int tick(ServerWorld world) {
        Random random = world.random;
        if (!MainConfig.InfamySpawn.get()) {
            return 0;
        } else {
            --this.ticksUntilSpawn;
            if (this.ticksUntilSpawn > 0) {
                return 0;
            } else {
                this.ticksUntilSpawn += MainConfig.InfamySpawnFreq.get();
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
                        if (random.nextInt(MainConfig.InfamySpawnChance.get()) != 0) {
                            return 0;
                        } else {
                            IInfamy infamy = InfamyHelper.getCapability(playerentity);
                            int j1 = infamy.getInfamy();
                            if (j1 > MainConfig.InfamyThreshold.get()) {
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
                                        int e1 = MathHelper.clamp(j1 / MainConfig.InfamyThreshold.get(), 1, 3) + 1;
                                        int e15 = world.random.nextInt(e1);
                                        for (int k1 = 0; k1 < e15; ++k1) {
                                            ++i1;
                                            blockpos$mutable.setY(world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
                                            if (k1 == 0) {
                                                if (!this.spawnEnvioker(world, blockpos$mutable, random)) {
                                                    break;
                                                }
                                            } else {
                                                this.spawnEnvioker(world, blockpos$mutable, random);
                                            }

                                            blockpos$mutable.setX(blockpos$mutable.getX() + random.nextInt(5) - random.nextInt(5));
                                            blockpos$mutable.setZ(blockpos$mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                                        }
                                        if (j1 >= MainConfig.InfamyThreshold.get() * 2) {
                                            int j2 = j1/2;
                                            int e2 = MathHelper.clamp(j2 / MainConfig.InfamyThreshold.get(), 1, 5) + 1;
                                            int e25 = world.random.nextInt(e2);
                                            for (int k1 = 0; k1 < e25; ++k1) {
                                                ++i1;
                                                int random1 = world.random.nextInt(16);
                                                blockpos$mutable.setY(world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
                                                if (k1 == 0) {
                                                    if (!this.spawnRandomIllager(world, blockpos$mutable, random, random1)) {
                                                        break;
                                                    }
                                                } else {
                                                    this.spawnRandomIllager(world, blockpos$mutable, random, random1);
                                                }

                                                blockpos$mutable.setX(blockpos$mutable.getX() + random.nextInt(5) - random.nextInt(5));
                                                blockpos$mutable.setZ(blockpos$mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                                            }
                                        }
                                        if (j1 >= MainConfig.InfamyThreshold.get() * 3) {
                                            int j2 = j1/3;
                                            int e2 = MathHelper.clamp(j2 / MainConfig.InfamyThreshold.get(), 1, 5) + 1;
                                            int e25 = world.random.nextInt(e2);
                                            for (int k1 = 0; k1 < e25; ++k1) {
                                                ++i1;
                                                blockpos$mutable.setY(world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
                                                if (k1 == 0) {
                                                    if (!this.spawnInquillager(world, blockpos$mutable, random)) {
                                                        break;
                                                    }
                                                } else {
                                                    this.spawnInquillager(world, blockpos$mutable, random);
                                                }

                                                blockpos$mutable.setX(blockpos$mutable.getX() + random.nextInt(5) - random.nextInt(5));
                                                blockpos$mutable.setZ(blockpos$mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                                            }
                                        }
                                        if (j1 >= MainConfig.InfamyThreshold.get() * 3) {
                                            int j2 = j1/3;
                                            int e2 = MathHelper.clamp(j2 / MainConfig.InfamyThreshold.get(), 1, 5) + 1;
                                            int e25 = world.random.nextInt(e2);
                                            for (int k1 = 0; k1 < e25; ++k1) {
                                                ++i1;
                                                blockpos$mutable.setY(world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
                                                if (k1 == 0) {
                                                    if (!this.spawnConquillager(world, blockpos$mutable, random)) {
                                                        break;
                                                    }
                                                } else {
                                                    this.spawnConquillager(world, blockpos$mutable, random);
                                                }

                                                blockpos$mutable.setX(blockpos$mutable.getX() + random.nextInt(5) - random.nextInt(5));
                                                blockpos$mutable.setZ(blockpos$mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                                            }
                                        }
                                        if (j1 >= MainConfig.InfamyThreshold.get() * 12) {
                                            if (!playerentity.hasEffect(Effects.BAD_OMEN)) {
                                                playerentity.addEffect(new EffectInstance(Effects.BAD_OMEN, 120000, 0, false, false));
                                            }
                                        }
                                        return i1;
                                    }
                                }
                            } else {
                                return 0;
                            }
                        }
                    }
                }
            }
        }
    }


    private boolean spawnEnvioker(ServerWorld worldIn, BlockPos p_222695_2_, Random random) {
        BlockState blockstate = worldIn.getBlockState(p_222695_2_);
        if (!WorldEntitySpawner.isValidEmptySpawnBlock(worldIn, p_222695_2_, blockstate, blockstate.getFluidState(), ModEntityType.ENVIOKER.get())) {
            return false;
        } else if (!PatrollerEntity.checkPatrollingMonsterSpawnRules(ModEntityType.ENVIOKER.get(), worldIn, SpawnReason.PATROL, p_222695_2_, random)) {
            return false;
        } else {
            EnviokerEntity illager = ModEntityType.ENVIOKER.get().create(worldIn);
            if (illager != null) {
                illager.setPos((double)p_222695_2_.getX(), (double)p_222695_2_.getY(), (double)p_222695_2_.getZ());
                if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(illager, worldIn, p_222695_2_.getX(), p_222695_2_.getY(), p_222695_2_.getZ(), null, SpawnReason.PATROL) == -1) return false;
                illager.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(p_222695_2_), SpawnReason.PATROL, (ILivingEntityData)null, (CompoundNBT)null);
                illager.goalSelector.addGoal(0, new MoveTowardsTargetGoal<>(illager));
                worldIn.addFreshEntityWithPassengers(illager);
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean spawnInquillager(ServerWorld worldIn, BlockPos p_222695_2_, Random random) {
        BlockState blockstate = worldIn.getBlockState(p_222695_2_);
        if (!WorldEntitySpawner.isValidEmptySpawnBlock(worldIn, p_222695_2_, blockstate, blockstate.getFluidState(), ModEntityType.INQUILLAGER.get())) {
            return false;
        } else if (!PatrollerEntity.checkPatrollingMonsterSpawnRules(ModEntityType.INQUILLAGER.get(), worldIn, SpawnReason.PATROL, p_222695_2_, random)) {
            return false;
        } else {
            InquillagerEntity illager = ModEntityType.INQUILLAGER.get().create(worldIn);
            if (illager != null) {
                illager.setPos((double)p_222695_2_.getX(), (double)p_222695_2_.getY(), (double)p_222695_2_.getZ());
                if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(illager, worldIn, p_222695_2_.getX(), p_222695_2_.getY(), p_222695_2_.getZ(), null, SpawnReason.PATROL) == -1) return false;
                illager.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(p_222695_2_), SpawnReason.PATROL, (ILivingEntityData)null, (CompoundNBT)null);
                illager.goalSelector.addGoal(0, new MoveTowardsTargetGoal<>(illager));
                worldIn.addFreshEntityWithPassengers(illager);
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean spawnConquillager(ServerWorld worldIn, BlockPos p_222695_2_, Random random) {
        BlockState blockstate = worldIn.getBlockState(p_222695_2_);
        if (!WorldEntitySpawner.isValidEmptySpawnBlock(worldIn, p_222695_2_, blockstate, blockstate.getFluidState(), ModEntityType.CONQUILLAGER.get())) {
            return false;
        } else if (!PatrollerEntity.checkPatrollingMonsterSpawnRules(ModEntityType.CONQUILLAGER.get(), worldIn, SpawnReason.PATROL, p_222695_2_, random)) {
            return false;
        } else {
            ConquillagerEntity illager = ModEntityType.CONQUILLAGER.get().create(worldIn);
            if (illager != null) {
                illager.setPos((double)p_222695_2_.getX(), (double)p_222695_2_.getY(), (double)p_222695_2_.getZ());
                if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(illager, worldIn, p_222695_2_.getX(), p_222695_2_.getY(), p_222695_2_.getZ(), null, SpawnReason.PATROL) == -1) return false;
                illager.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(p_222695_2_), SpawnReason.PATROL, (ILivingEntityData)null, (CompoundNBT)null);
                illager.goalSelector.addGoal(0, new MoveTowardsTargetGoal<>(illager));
                worldIn.addFreshEntityWithPassengers(illager);
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean spawnRandomIllager(ServerWorld worldIn, BlockPos p_222695_2_, Random random, int r) {
        BlockState blockstate = worldIn.getBlockState(p_222695_2_);
        if (!WorldEntitySpawner.isValidEmptySpawnBlock(worldIn, p_222695_2_, blockstate, blockstate.getFluidState(), EntityType.PILLAGER)) {
            return false;
        } else if (!PatrollerEntity.checkPatrollingMonsterSpawnRules(EntityType.PILLAGER, worldIn, SpawnReason.PATROL, p_222695_2_, random)) {
            return false;
        } else {
            PatrollerEntity illager = null;
            int i = 0;
            switch (r){
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    illager = EntityType.PILLAGER.create(worldIn);
                    break;
                case 11:
                case 12:
                case 13:
                    illager = EntityType.VINDICATOR.create(worldIn);
                    break;
                case 14:
                    illager = EntityType.RAVAGER.create(worldIn);
                    break;
                case 15:
                    illager = EntityType.RAVAGER.create(worldIn);
                    ++i;
                    break;
            }
            if (illager != null) {
                illager.setPos(p_222695_2_.getX(), p_222695_2_.getY(), p_222695_2_.getZ());
                if(net.minecraftforge.common.ForgeHooks.canEntitySpawn(illager, worldIn, p_222695_2_.getX(), p_222695_2_.getY(), p_222695_2_.getZ(), null, SpawnReason.PATROL) == -1) return false;
                illager.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(p_222695_2_), SpawnReason.PATROL, (ILivingEntityData)null, (CompoundNBT)null);
                illager.goalSelector.addGoal(0, new MoveTowardsTargetGoal<>(illager));
                worldIn.addFreshEntityWithPassengers(illager);
                if (i > 0){
                    PatrollerEntity rider = null;
                    int riding = random.nextInt(5);
                    switch (riding){
                        case 0:
                            rider = EntityType.PILLAGER.create(worldIn);
                            break;
                        case 1:
                            rider = EntityType.VINDICATOR.create(worldIn);
                            break;
                        case 2:
                            rider = ModEntityType.CONQUILLAGER.get().create(worldIn);
                            break;
                        case 3:
                            rider = ModEntityType.INQUILLAGER.get().create(worldIn);
                            break;
                        case 4:
                            rider = ModEntityType.ENVIOKER.get().create(worldIn);
                            break;
                    }
                    if (rider != null){
                        rider.setPos(p_222695_2_.getX(), p_222695_2_.getY(), p_222695_2_.getZ());
                        rider.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(p_222695_2_), SpawnReason.PATROL, (ILivingEntityData)null, (CompoundNBT)null);
                        rider.startRiding(illager);
                        worldIn.addFreshEntity(rider);
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }
}
