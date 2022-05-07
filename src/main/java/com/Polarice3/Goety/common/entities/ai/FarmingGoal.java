package com.Polarice3.Goety.common.entities.ai;

import com.Polarice3.Goety.common.entities.ally.FarmerMinionEntity;
import com.google.common.collect.Lists;
import net.minecraft.block.*;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class FarmingGoal<T extends FarmerMinionEntity> extends Goal {
    @Nullable
    private BlockPos aboveFarmlandPos;
    private BlockPos nearestFarmlandPos;
    private long nextOkStartTime;
    private final List<BlockPos> validFarmlandAroundMob = Lists.newArrayList();
    private final List<BlockPos> nearestFarmlandAroundMob = Lists.newArrayList();
    private final T mob;

    public FarmingGoal(T p_i50572_2_) {
        this.mob = p_i50572_2_;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.mob.getTarget() == null && this.mob.getLastHurtByMob() == null && this.mob.getMainHandItem().getItem() instanceof HoeItem && this.mob.isWandering();
    }

    private void setAboveFarmlandPos(World pLevel){
        BlockPos.Mutable blockpos$mutable = this.mob.blockPosition().mutable();
        this.validFarmlandAroundMob.clear();

        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                for(int k = -1; k <= 1; ++k) {
                    blockpos$mutable.set(this.mob.getX() + (double)i, this.mob.getY() + (double)j, this.mob.getZ() + (double)k);
                    if (this.validPos(blockpos$mutable, pLevel)) {
                        this.validFarmlandAroundMob.add(new BlockPos(blockpos$mutable));
                    }
                }
            }
        }

        this.aboveFarmlandPos = this.getValidFarmland(pLevel);
    }

    private void findFarmlandPos(World pLevel){
        BlockPos.Mutable blockpos$mutable = this.mob.blockPosition().mutable();
        this.nearestFarmlandAroundMob.clear();
        int r = 8;

        for(int i = -r; i <= r; ++i) {
            for(int j = -r; j <= r; ++j) {
                for(int k = -r; k <= r; ++k) {
                    blockpos$mutable.set(this.mob.getX() + (double)i, this.mob.getY() + (double)j, this.mob.getZ() + (double)k);
                    if (this.validPos(blockpos$mutable, pLevel)) {
                        this.nearestFarmlandAroundMob.add(new BlockPos(blockpos$mutable));
                    }
                }
            }
        }

        this.nearestFarmlandPos = this.getNearestFarmland(pLevel);
    }

    @Nullable
    private BlockPos getValidFarmland(World pServerLevel) {
        return this.validFarmlandAroundMob.isEmpty() ? null : this.validFarmlandAroundMob.get(pServerLevel.getRandom().nextInt(this.validFarmlandAroundMob.size()));
    }

    @Nullable
    private BlockPos getNearestFarmland(World pServerLevel) {
        return this.nearestFarmlandAroundMob.isEmpty() ? null : this.nearestFarmlandAroundMob.get(pServerLevel.getRandom().nextInt(this.nearestFarmlandAroundMob.size()));
    }

    private boolean validPos(BlockPos pPos, World pServerLevel) {
        BlockState blockstate = pServerLevel.getBlockState(pPos);
        Block block = blockstate.getBlock();
        Block block1 = pServerLevel.getBlockState(pPos.below()).getBlock();
        return block instanceof CropsBlock && ((CropsBlock)block).isMaxAge(blockstate) || blockstate.isAir() && block1 instanceof FarmlandBlock;
    }

    public void tick() {
        super.tick();
        World pLevel = this.mob.level;
        long pGameTime = pLevel.getGameTime();
        this.setAboveFarmlandPos(pLevel);
        this.findFarmlandPos(pLevel);
        if (this.nearestFarmlandPos != null){
            BlockPos blockPos = this.nearestFarmlandPos;
            this.mob.getNavigation().moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0D);
            if (this.aboveFarmlandPos != null){
                if (pGameTime > this.nextOkStartTime) {
                    BlockState blockstate = pLevel.getBlockState(this.aboveFarmlandPos);
                    Block block = blockstate.getBlock();
                    Block block1 = pLevel.getBlockState(this.aboveFarmlandPos.below()).getBlock();
                    if (block instanceof CropsBlock && ((CropsBlock) block).isMaxAge(blockstate)) {
                        pLevel.destroyBlock(this.aboveFarmlandPos, true, this.mob);
                    }

                    if (blockstate.isAir() && block1 instanceof FarmlandBlock && !this.mob.inventory.isEmpty()) {
                        Inventory inventory = this.mob.inventory;

                        for (int i = 0; i < inventory.getContainerSize(); ++i) {
                            ItemStack itemstack = inventory.getItem(i);
                            boolean flag = false;
                            if (!itemstack.isEmpty()) {
                                if (itemstack.getItem() == Items.WHEAT_SEEDS) {
                                    pLevel.setBlock(this.aboveFarmlandPos, Blocks.WHEAT.defaultBlockState(), 3);
                                    flag = true;
                                } else if (itemstack.getItem() == Items.POTATO) {
                                    pLevel.setBlock(this.aboveFarmlandPos, Blocks.POTATOES.defaultBlockState(), 3);
                                    flag = true;
                                } else if (itemstack.getItem() == Items.CARROT) {
                                    pLevel.setBlock(this.aboveFarmlandPos, Blocks.CARROTS.defaultBlockState(), 3);
                                    flag = true;
                                } else if (itemstack.getItem() == Items.BEETROOT_SEEDS) {
                                    pLevel.setBlock(this.aboveFarmlandPos, Blocks.BEETROOTS.defaultBlockState(), 3);
                                    flag = true;
                                } else if (itemstack.getItem() instanceof net.minecraftforge.common.IPlantable) {
                                    if (((net.minecraftforge.common.IPlantable) itemstack.getItem()).getPlantType(pLevel, aboveFarmlandPos) == net.minecraftforge.common.PlantType.CROP) {
                                        pLevel.setBlock(aboveFarmlandPos, ((net.minecraftforge.common.IPlantable) itemstack.getItem()).getPlant(pLevel, aboveFarmlandPos), 3);
                                        flag = true;
                                    }
                                }
                            }

                            if (flag) {
                                pLevel.playSound(null, this.aboveFarmlandPos.getX(), this.aboveFarmlandPos.getY(), this.aboveFarmlandPos.getZ(), SoundEvents.CROP_PLANTED, SoundCategory.BLOCKS, 1.0F, 1.0F);
                                itemstack.shrink(1);
                                if (itemstack.isEmpty()) {
                                    inventory.setItem(i, ItemStack.EMPTY);
                                }
                                ItemStack held = this.mob.getMainHandItem();
                                if (held.isDamageableItem()) {
                                    held.setDamageValue(held.getDamageValue() + pLevel.random.nextInt(2));
                                    if (held.getDamageValue() >= itemstack.getMaxDamage()) {
                                        this.mob.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
                                        this.mob.setItemSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                                    }
                                }
                                break;
                            }
                        }
                    }

                    if (block instanceof CropsBlock && !((CropsBlock) block).isMaxAge(blockstate)) {
                        this.validFarmlandAroundMob.remove(this.aboveFarmlandPos);
                        this.aboveFarmlandPos = this.getValidFarmland(pLevel);
                        if (this.aboveFarmlandPos != null) {
                            this.nextOkStartTime = pGameTime + 20L;
                        }
                    }
                }
            }
        }
    }

}
