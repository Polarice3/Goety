package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.init.ModTags;
import com.Polarice3.Goety.init.ModTileEntityType;
import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ForbiddenGrassTileEntity extends TileEntity implements ITickableTileEntity {
    private static ArrayList<MonsterMob> monsterMobs = new ArrayList<>();

    public ForbiddenGrassTileEntity(TileEntityType<?> p_i48289_1_) {
        super(p_i48289_1_);
    }

    public ForbiddenGrassTileEntity(){
        this(ModTileEntityType.FORBIDDEN_GRASS.get());
    }

    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    public World getOriginalLevel(){
        return this.level;
    }

    public ServerWorld getLevel() {
        if (this.getOriginalLevel() != null && !this.getOriginalLevel().isClientSide) {
            return (ServerWorld) this.getOriginalLevel();
        } else {
            return null;
        }
    }

    public void tick() {
        if (this.getLevel() != null) {
            if (this.getLevel().isLoaded(this.worldPosition)) {
                BlockPos above = this.getBlockPos().above();
                if (!this.getLevel().getBlockState(above).isSolidRender(this.getLevel(), above)) {
                    if (this.getLevel().isEmptyBlock(above)) {
                        float f = this.getLevel().getBrightness(above);
                        if (f > 0.5F && this.getLevel().canSeeSky(above) && this.getLevel().isDay()) {
                            this.getLevel().setBlockAndUpdate(above, AbstractFireBlock.getState(this.getLevel(), above));
                        }
                        for (int j1 = -2; j1 < 2; ++j1) {
                            for (int k1 = -2; k1 <= 2; ++k1) {
                                for (int l1 = -2; l1 < 2; ++l1) {
                                    BlockPos blockPos1 = this.getBlockPos().offset(j1, k1, l1);
                                    BlockPos blockPos2 = blockPos1.above();
                                    BlockState blockState = this.getLevel().getBlockState(blockPos2);
                                    if (blockState.getBlock() instanceof AbstractFireBlock) {
                                        if (this.getLevel().random.nextFloat() <= 0.01F) {
                                            this.getLevel().setBlockAndUpdate(above, AbstractFireBlock.getState(this.getLevel(), above));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    this.getMobs();
                    AxisAlignedBB alignedBB = new AxisAlignedBB(above.getX() - 1, above.getY(), above.getZ() - 1, above.getX() + 1, above.getY() + 1, above.getZ() + 1);
                    int k = this.getLevel().getEntitiesOfClass(LivingEntity.class, alignedBB.inflate(4)).size();
                    if (this.getLevel().random.nextFloat() <= 0.005F) {
                        if (this.getLevel().getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(above).inflate(1)).isEmpty()) {
                            EntityType<?> entityType = getRandomMonsterMob(this.getLevel().random);
                            if (k <= 16) {
                                if (EntitySpawnPlacementRegistry.checkSpawnRules(entityType, this.getLevel(), SpawnReason.SPAWNER, above, this.getLevel().random)) {
                                    Entity entity = entityType.create(this.getLevel());
                                    if (entity instanceof MobEntity) {
                                        MobEntity mob = (MobEntity) entity;
                                        mob.setPos(above.getX() + 0.5F, above.getY(), above.getZ() + 0.5F);
                                        if (this.getLevel().noCollision(entity) && this.getLevel().isUnobstructed(entity, this.getLevel().getBlockState(above).getShape(this.getLevel(), above))) {
                                            if (this.getLevel().getBlockState(this.getBlockPos().below()).is(ModTags.Blocks.DEAD_SANDS)){
                                                MobUtil.deadSandConvert(mob, true);
                                            }
                                            mob.finalizeSpawn(this.getLevel(), this.getLevel().getCurrentDifficultyAt(this.worldPosition), SpawnReason.SPAWNER, null, null);
                                            this.getLevel().addFreshEntity(mob);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static float addMonsterMob(EntityType<?> type, int rarity) {
        if (rarity <= 0) {
            rarity = 1;
        }

        Iterator<MonsterMob> itr = monsterMobs.iterator();
        while (itr.hasNext()) {
            MonsterMob mob = itr.next();
            if (type == mob.type) {
                itr.remove();
                rarity = mob.weight + rarity;
                break;
            }
        }

        monsterMobs.add(new MonsterMob(rarity, type));
        return rarity;
    }

    public static EntityType<?> getRandomMonsterMob(Random rand) {
        MonsterMob mob = WeightedRandom.getRandomItem(rand, monsterMobs);
        return mob.type;
    }


    public static class MonsterMob extends WeightedRandom.Item {
        public final EntityType<?> type;
        public MonsterMob(int weight, EntityType<?> type) {
            super(weight);
            this.type = type;
        }

        @Override
        public boolean equals(Object target) {
            return target instanceof MonsterMob && type.equals(((MonsterMob)target).type);
        }
    }


    public void getMobs(){
        List<MobSpawnInfo.Spawners> spawners = this.getLevel().getBiome(this.worldPosition).getMobSettings().getMobs(EntityClassification.MONSTER);
        if (monsterMobs.size() < spawners.size()) {
            for (MobSpawnInfo.Spawners spawners1 : spawners) {
                addMonsterMob(spawners1.type, spawners1.weight);
            }
        }
    }

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, -1, this.getUpdateTag());
    }
}
