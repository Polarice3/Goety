package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.ai.MoveTowardsTargetGoal;
import com.Polarice3.Goety.common.entities.hostile.illagers.ConquillagerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.EnviokerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.InquillagerEntity;
import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.network.packets.server.SPlayPlayerSoundPacket;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.SEHelper;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.PatrollerEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;

import java.util.Map;
import java.util.Random;

public class IllagerSpawner {
    private int ticksUntilSpawn;

    public int tick(ServerWorld p_64570_) {
        if (!MainConfig.IllagerAssault.get()) {
            return 0;
        } else {
            Random randomsource = p_64570_.random;
            --this.ticksUntilSpawn;
            if (this.ticksUntilSpawn > 0) {
                return 0;
            } else {
                this.ticksUntilSpawn += MainConfig.IllagerAssaultSpawnFreq.get();
                if (randomsource.nextInt(MainConfig.IllagerAssaultSpawnChance.get()) != 0) {
                    return 0;
                } else {
                    int j = p_64570_.players().size();
                    if (j < 1) {
                        return 0;
                    } else {
                        PlayerEntity player = p_64570_.players().get(randomsource.nextInt(j));
                        int soulEnergy = MathHelper.clamp(SEHelper.getSoulAmountInt(player), 0, MainConfig.IllagerAssaultSELimit.get());
                        if (player.isSpectator()) {
                            return 0;
                        } else if (p_64570_.isCloseToVillage(player.blockPosition(), 2) && soulEnergy < MainConfig.IllagerAssaultSELimit.get()) {
                            return 0;
                        } else if (soulEnergy > MainConfig.IllagerAssaultSEThreshold.get()) {
                            int k = (24 + randomsource.nextInt(24)) * (randomsource.nextBoolean() ? -1 : 1);
                            int l = (24 + randomsource.nextInt(24)) * (randomsource.nextBoolean() ? -1 : 1);
                            BlockPos.Mutable blockpos$mutable = player.blockPosition().mutable().move(k, 0, l);
                            if (!p_64570_.isAreaLoaded(blockpos$mutable, 10)) {
                                return 0;
                            } else if (!p_64570_.dimensionType().hasRaids()){
                                return 0;
                            } else {
                                Biome biome = p_64570_.getBiome(blockpos$mutable);
                                Biome.Category biome$category = biome.getBiomeCategory();
                                if (biome$category == Biome.Category.MUSHROOM) {
                                    return 0;
                                } else if (player.blockPosition().getY() < p_64570_.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY() - 32 && !p_64570_.canSeeSky(player.blockPosition())){
                                    return 0;
                                } else {
                                    int i1 = 0;
                                    int e1 = MathHelper.clamp(soulEnergy / MainConfig.IllagerAssaultSEThreshold.get(), 1, 3) + 1;
                                    int e15 = p_64570_.random.nextInt(e1);
                                    for (int k1 = 0; k1 < e15; ++k1) {
                                        ++i1;
                                        blockpos$mutable.setY(p_64570_.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
                                        if (k1 == 0) {
                                            if (!this.spawnEnvioker(p_64570_, blockpos$mutable, randomsource, soulEnergy, player)) {
                                                break;
                                            }
                                        } else {
                                            this.spawnEnvioker(p_64570_, blockpos$mutable, randomsource, soulEnergy, player);
                                        }

                                        blockpos$mutable.setX(blockpos$mutable.getX() + randomsource.nextInt(5) - randomsource.nextInt(5));
                                        blockpos$mutable.setZ(blockpos$mutable.getZ() + randomsource.nextInt(5) - randomsource.nextInt(5));
                                    }
                                    if (soulEnergy >= MainConfig.IllagerAssaultSEThreshold.get() * 2) {
                                        int j2 = soulEnergy/2;
                                        int e2 = MathHelper.clamp(j2 / MainConfig.IllagerAssaultSEThreshold.get(), 1, 5) + 1;
                                        int e25 = p_64570_.random.nextInt(e2);
                                        for (int k1 = 0; k1 < e25; ++k1) {
                                            ++i1;
                                            int random1 = p_64570_.random.nextInt(16);
                                            blockpos$mutable.setY(p_64570_.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
                                            if (k1 == 0) {
                                                if (!this.spawnRandomIllager(p_64570_, blockpos$mutable, randomsource, random1, soulEnergy, player)) {
                                                    break;
                                                }
                                            } else {
                                                this.spawnRandomIllager(p_64570_, blockpos$mutable, randomsource, random1, soulEnergy, player);
                                            }

                                            blockpos$mutable.setX(blockpos$mutable.getX() + randomsource.nextInt(5) - randomsource.nextInt(5));
                                            blockpos$mutable.setZ(blockpos$mutable.getZ() + randomsource.nextInt(5) - randomsource.nextInt(5));
                                        }
                                    }
                                    if (soulEnergy >= MainConfig.IllagerAssaultSEThreshold.get() * 2.5) {
                                        int j2 = (int) (soulEnergy/2.5);
                                        int e2 = MathHelper.clamp(j2 / MainConfig.IllagerAssaultSEThreshold.get(), 1, 5) + 1;
                                        int e25 = p_64570_.random.nextInt(e2);
                                        for (int k1 = 0; k1 < e25; ++k1) {
                                            ++i1;
                                            blockpos$mutable.setY(p_64570_.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
                                            if (k1 == 0) {
                                                if (!this.spawnInquillager(p_64570_, blockpos$mutable, randomsource, soulEnergy, player)) {
                                                    break;
                                                }
                                            } else {
                                                this.spawnInquillager(p_64570_, blockpos$mutable, randomsource, soulEnergy, player);
                                            }

                                            blockpos$mutable.setX(blockpos$mutable.getX() + randomsource.nextInt(5) - randomsource.nextInt(5));
                                            blockpos$mutable.setZ(blockpos$mutable.getZ() + randomsource.nextInt(5) - randomsource.nextInt(5));
                                        }
                                    }
                                    if (soulEnergy >= MainConfig.IllagerAssaultSEThreshold.get() * 2.5) {
                                        int j2 = (int) (soulEnergy/2.5);
                                        int e2 = MathHelper.clamp(j2 / MainConfig.IllagerAssaultSEThreshold.get(), 1, 5) + 1;
                                        int e25 = p_64570_.random.nextInt(e2);
                                        for (int k1 = 0; k1 < e25; ++k1) {
                                            ++i1;
                                            blockpos$mutable.setY(p_64570_.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
                                            if (k1 == 0) {
                                                if (!this.spawnConquillager(p_64570_, blockpos$mutable, randomsource, soulEnergy, player)) {
                                                    break;
                                                }
                                            } else {
                                                this.spawnConquillager(p_64570_, blockpos$mutable, randomsource, soulEnergy, player);
                                            }

                                            blockpos$mutable.setX(blockpos$mutable.getX() + randomsource.nextInt(5) - randomsource.nextInt(5));
                                            blockpos$mutable.setZ(blockpos$mutable.getZ() + randomsource.nextInt(5) - randomsource.nextInt(5));
                                        }
                                    }
                                    if (soulEnergy >= MainConfig.IllagerAssaultSELimit.get() && MainConfig.SoulEnergyBadOmen.get()) {
                                        if (!player.hasEffect(Effects.BAD_OMEN)) {
                                            player.addEffect(new EffectInstance(Effects.BAD_OMEN, 120000, 0, false, false));
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


    public boolean spawnEnvioker(ServerWorld worldIn, BlockPos p_222695_2_, Random random, int infamy, PlayerEntity player) {
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
                if (random.nextInt(4) == 0){
                    illager.setRider(true);
                }
                illager.setTarget(player);
                this.upgradeIllagers(illager, infamy);
                worldIn.addFreshEntityWithPassengers(illager);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean spawnInquillager(ServerWorld worldIn, BlockPos p_222695_2_, Random random, int infamy, PlayerEntity player) {
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
                if (random.nextInt(4) == 0){
                    illager.setRider(true);
                }
                illager.setTarget(player);
                this.upgradeIllagers(illager, infamy);
                worldIn.addFreshEntityWithPassengers(illager);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean spawnConquillager(ServerWorld worldIn, BlockPos p_222695_2_, Random random, int infamy, PlayerEntity player) {
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
                if (random.nextInt(4) == 0){
                    illager.setRider(true);
                }
                illager.setTarget(player);
                this.upgradeIllagers(illager, infamy);
                worldIn.addFreshEntityWithPassengers(illager);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean spawnRandomIllager(ServerWorld worldIn, BlockPos p_222695_2_, Random random, int r, int infamy, PlayerEntity player) {
        BlockState blockstate = worldIn.getBlockState(p_222695_2_);
        if (!WorldEntitySpawner.isValidEmptySpawnBlock(worldIn, p_222695_2_, blockstate, blockstate.getFluidState(), EntityType.PILLAGER)) {
            return false;
        } else if (!PatrollerEntity.checkPatrollingMonsterSpawnRules(EntityType.PILLAGER, worldIn, SpawnReason.PATROL, p_222695_2_, random)) {
            return false;
        } else {
            AbstractRaiderEntity illager = null;
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
                this.upgradeIllagers(illager, infamy);
                illager.setTarget(player);
                worldIn.addFreshEntityWithPassengers(illager);
                if (i > 0){
                    AbstractRaiderEntity rider = null;
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
                        this.upgradeIllagers(rider, infamy);
                        rider.startRiding(illager);
                        rider.setTarget(player);
                        worldIn.addFreshEntity(rider);
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public void upgradeIllagers(AbstractRaiderEntity raider, int infamy){
        World world = raider.level;
        if (infamy >= MainConfig.IllagerAssaultSEThreshold.get() * 5) {
            if (raider instanceof PillagerEntity){
                ItemStack itemstack = new ItemStack(Items.CROSSBOW);
                Map<Enchantment, Integer> map = Maps.newHashMap();
                if (world.getDifficulty() == Difficulty.HARD) {
                    map.put(Enchantments.QUICK_CHARGE, 2);
                } else {
                    map.put(Enchantments.QUICK_CHARGE, 1);
                }

                map.put(Enchantments.MULTISHOT, 1);
                EnchantmentHelper.setEnchantments(map, itemstack);
                raider.setItemSlot(EquipmentSlotType.MAINHAND, itemstack);
            }
            if (raider instanceof VindicatorEntity){
                ItemStack itemstack = new ItemStack(Items.IRON_AXE);
                Map<Enchantment, Integer> map = Maps.newHashMap();
                int i = 1;
                if (world.getDifficulty() == Difficulty.HARD) {
                    i = 2;
                }
                map.put(Enchantments.SHARPNESS, i);
                EnchantmentHelper.setEnchantments(map, itemstack);
                raider.setItemSlot(EquipmentSlotType.MAINHAND, itemstack);
            }
            if (raider instanceof ConquillagerEntity){
                ItemStack itemstack = new ItemStack(Items.CROSSBOW);
                Map<Enchantment, Integer> map = Maps.newHashMap();
                if (world.getDifficulty() == Difficulty.HARD) {
                    map.put(Enchantments.QUICK_CHARGE, 3);
                } else {
                    map.put(Enchantments.QUICK_CHARGE, 2);
                }

                map.put(Enchantments.MULTISHOT, 1);
                EnchantmentHelper.setEnchantments(map, itemstack);
                raider.setItemSlot(EquipmentSlotType.MAINHAND, itemstack);
            }
            if (raider instanceof InquillagerEntity){
                ItemStack itemstack = new ItemStack(Items.IRON_SWORD);
                if (world.random.nextFloat() <= 0.25F){
                    itemstack = new ItemStack(Items.DIAMOND_SWORD);
                }
                Map<Enchantment, Integer> map = Maps.newHashMap();
                int i = 2;
                if (world.getDifficulty() == Difficulty.HARD) {
                    i = 4;
                }
                map.put(Enchantments.SHARPNESS, i);
                map.put(Enchantments.FIRE_ASPECT, 2);
                EnchantmentHelper.setEnchantments(map, itemstack);
                raider.setItemSlot(EquipmentSlotType.MAINHAND, itemstack);
            }
            if (raider instanceof EnviokerEntity){
                ItemStack itemstack = new ItemStack(Items.IRON_SWORD);
                if (world.random.nextFloat() <= 0.25F){
                    itemstack = new ItemStack(Items.DIAMOND_SWORD);
                }
                Map<Enchantment, Integer> map = Maps.newHashMap();
                int i = 3;
                if (world.getDifficulty() == Difficulty.HARD) {
                    i = 5;
                }
                map.put(Enchantments.SHARPNESS, i);
                map.put(Enchantments.KNOCKBACK, 2);
                EnchantmentHelper.setEnchantments(map, itemstack);
                raider.setItemSlot(EquipmentSlotType.MAINHAND, itemstack);
            }
        }
    }

    public void forceSpawn(ServerWorld world, PlayerEntity player, CommandSource pSource){
        Random random = world.random;
        int j1 = SEHelper.getSoulAmountInt(player);
        if (j1 > MainConfig.IllagerAssaultSEThreshold.get()) {
            int k = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
            int l = (24 + random.nextInt(24)) * (random.nextBoolean() ? -1 : 1);
            BlockPos.Mutable blockpos$mutable = player.blockPosition().mutable().move(k, 0, l);
            if (world.isAreaLoaded(blockpos$mutable, 10)) {
                if (player.blockPosition().getY() < world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY() - 32 && !world.canSeeSky(player.blockPosition())){
                    ModNetwork.sendToALL(new SPlayPlayerSoundPacket(SoundEvents.FIRE_EXTINGUISH, 1.0F, 1.0F));
                    pSource.sendFailure(new TranslationTextComponent("commands.goety.illager.spawn.failure_location", player.getDisplayName()));
                } else {
                    int e1 = MathHelper.clamp(j1 / MainConfig.IllagerAssaultSEThreshold.get(), 1, 3) + 1;
                    int e15 = world.random.nextInt(e1);
                    for (int k1 = 0; k1 < e15; ++k1) {
                        blockpos$mutable.setY(world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
                        if (k1 == 0) {
                            if (!this.spawnEnvioker(world, blockpos$mutable, random, j1, player)) {
                                break;
                            }
                        } else {
                            this.spawnEnvioker(world, blockpos$mutable, random, j1, player);
                        }

                        blockpos$mutable.setX(blockpos$mutable.getX() + random.nextInt(5) - random.nextInt(5));
                        blockpos$mutable.setZ(blockpos$mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                    }
                    if (j1 >= MainConfig.IllagerAssaultSEThreshold.get() * 2) {
                        int j2 = j1/2;
                        int e2 = MathHelper.clamp(j2 / MainConfig.IllagerAssaultSEThreshold.get(), 1, 5) + 1;
                        int e25 = world.random.nextInt(e2);
                        for (int k1 = 0; k1 < e25; ++k1) {
                            int random1 = world.random.nextInt(16);
                            blockpos$mutable.setY(world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
                            if (k1 == 0) {
                                if (!this.spawnRandomIllager(world, blockpos$mutable, random, random1, j1, player)) {
                                    break;
                                }
                            } else {
                                this.spawnRandomIllager(world, blockpos$mutable, random, random1, j1, player);
                            }

                            blockpos$mutable.setX(blockpos$mutable.getX() + random.nextInt(5) - random.nextInt(5));
                            blockpos$mutable.setZ(blockpos$mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                        }
                    }
                    if (j1 >= MainConfig.IllagerAssaultSEThreshold.get() * 2.5) {
                        int j2 = (int) (j1/2.5);
                        int e2 = MathHelper.clamp(j2 / MainConfig.IllagerAssaultSEThreshold.get(), 1, 5) + 1;
                        int e25 = world.random.nextInt(e2);
                        for (int k1 = 0; k1 < e25; ++k1) {
                            blockpos$mutable.setY(world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
                            if (k1 == 0) {
                                if (!this.spawnInquillager(world, blockpos$mutable, random, j1, player)) {
                                    break;
                                }
                            } else {
                                this.spawnInquillager(world, blockpos$mutable, random, j1, player);
                            }

                            blockpos$mutable.setX(blockpos$mutable.getX() + random.nextInt(5) - random.nextInt(5));
                            blockpos$mutable.setZ(blockpos$mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                        }
                    }
                    if (j1 >= MainConfig.IllagerAssaultSEThreshold.get() * 2.5) {
                        int j2 = (int) (j1/2.5);
                        int e2 = MathHelper.clamp(j2 / MainConfig.IllagerAssaultSEThreshold.get(), 1, 5) + 1;
                        int e25 = world.random.nextInt(e2);
                        for (int k1 = 0; k1 < e25; ++k1) {
                            blockpos$mutable.setY(world.getHeightmapPos(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, blockpos$mutable).getY());
                            if (k1 == 0) {
                                if (!this.spawnConquillager(world, blockpos$mutable, random, j1, player)) {
                                    break;
                                }
                            } else {
                                this.spawnConquillager(world, blockpos$mutable, random, j1, player);
                            }

                            blockpos$mutable.setX(blockpos$mutable.getX() + random.nextInt(5) - random.nextInt(5));
                            blockpos$mutable.setZ(blockpos$mutable.getZ() + random.nextInt(5) - random.nextInt(5));
                        }
                    }
                    this.ticksUntilSpawn += MainConfig.IllagerAssaultSpawnFreq.get();
                    ModNetwork.sendToALL(new SPlayPlayerSoundPacket(SoundEvents.PLAYER_LEVELUP, 1.0F, 1.0F));
                    pSource.sendSuccess(new TranslationTextComponent("commands.goety.illager.spawn.success", player.getDisplayName()), false);
                }
            } else {
                ModNetwork.sendToALL(new SPlayPlayerSoundPacket(SoundEvents.FIRE_EXTINGUISH, 1.0F, 1.0F));
                pSource.sendFailure(new TranslationTextComponent("commands.goety.illager.spawn.failure_location", player.getDisplayName()));
            }
        }
    }

}
