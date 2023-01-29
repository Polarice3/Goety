package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.hostile.cultists.AbstractCultistEntity;
import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;

import java.util.Random;

public class PilgrimSpawner {
    private int nextTick;

    public int tick(ServerWorld pLevel) {
        if (!MainConfig.CultistPilgrimage.get()) {
            return 0;
        } else {
            Random random = pLevel.random;
            --this.nextTick;
            if (this.nextTick > 0) {
                return 0;
            } else {
                this.nextTick += 12000 + random.nextInt(1200);
                if (pLevel.dimension() == World.NETHER) {
                    int j = pLevel.players().size();
                    if (j < 1) {
                        return 0;
                    } else if (random.nextInt(5) != 0) {
                        return 0;
                    } else {
                        PlayerEntity playerentity = pLevel.players().get(random.nextInt(j));
                        if (playerentity.isSpectator()) {
                            return 0;
                        } else {
                            int i = playerentity.blockPosition().getX();
                            int k = playerentity.blockPosition().getZ();
                            int m = i + random.nextInt(16);
                            int n = k + random.nextInt(16);
                            float f = ModEntityType.FANATIC.get().getWidth();
                            int d0 = (int) MathHelper.clamp((double)m, (double)i + (double)f, (double)i + 32.0D - (double)f);
                            int d1 = (int) MathHelper.clamp((double)n, (double)k + (double)f, (double)k + 32.0D - (double)f);
                            BlockPos blockpos = getTopNonCollidingPos(pLevel, ModEntityType.FANATIC.get(), d0, d1);
                            if (!pLevel.hasChunksAt(blockpos.getX() - 10, blockpos.getY() - 10, blockpos.getZ() - 10, blockpos.getX() + 10, blockpos.getY() + 10, blockpos.getZ() + 10)) {
                                return 0;
                            } else {
                                int i1 = 0;
                                int j1 = (int)Math.ceil((double)pLevel.getCurrentDifficultyAt(blockpos).getEffectiveDifficulty()) + 1;
                                for(int k1 = 0; k1 < j1; ++k1) {
                                    ++i1;
                                    if (k1 == 0) {
                                        if (!this.spawnPilgrimageMember(pLevel, blockpos, random, true)) {
                                            break;
                                        }
                                    } else {
                                        this.spawnPilgrimageMember(pLevel, blockpos, random, false);
                                    }

                                    blockpos = new BlockPos(blockpos.getX() + random.nextInt(5) - random.nextInt(5), blockpos.getY(), blockpos.getZ() + random.nextInt(5) - random.nextInt(5));
                                }

                                return i1;
                            }
                        }
                    }
                } else {
                    return 0;
                }
            }
        }
    }

    private static BlockPos getTopNonCollidingPos(IWorldReader pLevel, EntityType<?> pEntityType, int pX, int pZ) {
        int i = pLevel.getHeight(EntitySpawnPlacementRegistry.getHeightmapType(pEntityType), pX, pZ);
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(pX, i, pZ);
        if (pLevel.dimensionType().hasCeiling()) {
            do {
                blockpos$mutable.move(Direction.DOWN);
            } while(!pLevel.getBlockState(blockpos$mutable).isAir());

            do {
                blockpos$mutable.move(Direction.DOWN);
            } while(pLevel.getBlockState(blockpos$mutable).isAir() && blockpos$mutable.getY() > 0);
        }

        if (EntitySpawnPlacementRegistry.getPlacementType(pEntityType) == EntitySpawnPlacementRegistry.PlacementType.ON_GROUND) {
            BlockPos blockpos = blockpos$mutable.below();
            if (pLevel.getBlockState(blockpos).isPathfindable(pLevel, blockpos, PathType.LAND)) {
                return blockpos.above();
            }
        }

        return blockpos$mutable.immutable().above();
    }

    private boolean spawnPilgrimageMember(ServerWorld pLevel, BlockPos pPos, Random pRandom, boolean pLeader) {
        BlockState blockstate = pLevel.getBlockState(pPos);
        if (pLevel.getBlockState(pPos.below()).getBlock() == Blocks.BEDROCK){
            return false;
        } else {
            AbstractCultistEntity cultist = ModEntityType.FANATIC.get().create(pLevel);
            if (!pLeader) {
                int random = pRandom.nextInt(13);
                switch (random) {
                    case 4:
                        cultist = ModEntityType.FANATIC.get().create(pLevel);
                        break;
                    case 9:
                        cultist = ModEntityType.ZEALOT.get().create(pLevel);
                        break;
                    case 10:
                        cultist = ModEntityType.THUG.get().create(pLevel);
                        break;
                    case 11:
                        cultist = ModEntityType.DISCIPLE.get().create(pLevel);
                        break;
                    case 12:
                        cultist = ModEntityType.BELDAM.get().create(pLevel);
                        break;
                }
            } else {
                int random = pRandom.nextInt(32);
                switch (random) {
                    case 9:
                        cultist = ModEntityType.FANATIC.get().create(pLevel);
                        break;
                    case 18:
                        cultist = ModEntityType.DISCIPLE.get().create(pLevel);
                        break;
                    case 24:
                        cultist = ModEntityType.BELDAM.get().create(pLevel);
                        break;
                    case 31:
                        cultist = ModEntityType.CHANNELLER.get().create(pLevel);
                        break;
                }
            }
            if (cultist != null) {
                if (pLeader && cultist.canBePilgrimLeader()) {
                    cultist.setPilgrimLeader(true);
                    cultist.findNewPilgrimTarget();
                }

                int above = pPos.getY();

                if (!WorldEntitySpawner.isValidEmptySpawnBlock(pLevel, pPos, blockstate, blockstate.getFluidState(), cultist.getType())){
                    if (pLevel.getBlockState(pPos.below()).isAir()) {
                        above = pPos.getY() - 1;
                    } else {
                        above = pPos.getY() + 1;
                    }
                }

                cultist.setPos(pPos.getX(), above, pPos.getZ());
                if (pLevel.noCollision(cultist, cultist.getBoundingBox())) {
                    if (net.minecraftforge.common.ForgeHooks.canEntitySpawn(cultist, pLevel, pPos.getX(), above, pPos.getZ(), null, SpawnReason.EVENT) == -1)
                        return false;
                    cultist.finalizeSpawn(pLevel, pLevel.getCurrentDifficultyAt(pPos), SpawnReason.EVENT, (ILivingEntityData) null, (CompoundNBT) null);
                    cultist.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
                    pLevel.addFreshEntityWithPassengers(cultist);
                }
                return true;
            } else {
                return false;
            }
        }
    }
}
