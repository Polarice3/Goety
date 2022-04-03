package com.Polarice3.Goety.common.ritual;

import com.Polarice3.Goety.common.tileentities.ModTileEntity;
import com.Polarice3.Goety.init.ModRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualStructures {

    public static final int RANGE = 8;

    public static void findAnimaStructure(ModTileEntity pTileEntity, BlockPos pPos, World pLevel) {
        pTileEntity.first.clear();
        pTileEntity.second.clear();
        pTileEntity.third.clear();
        for (int i = -RANGE; i <= RANGE; ++i) {
            for (int j = -RANGE; j <= RANGE; ++j) {
                for (int k = -RANGE; k <= RANGE; ++k) {
                    BlockPos blockpos1 = pPos.offset(i, j, k);
                    assert pLevel != null;
                    BlockState blockstate = pLevel.getBlockState(blockpos1);

                    if (blockstate.getBlock() == Blocks.LADDER) {
                        pTileEntity.first.add(blockpos1);
                    }
                    if (blockstate.getBlock() == Blocks.RAIL) {
                        pTileEntity.second.add(blockpos1);
                    }
                    if (blockstate.getBlock() == Blocks.CARVED_PUMPKIN) {
                        pTileEntity.third.add(blockpos1);
                    }
                }
            }
        }
    }

    public static boolean checkAnimaRequirements(ModTileEntity pTileEntity){
        return pTileEntity.first.size() >= 15 && pTileEntity.second.size() >= 15 && pTileEntity.third.size() >= 1;
    }

    public static void findNecroStructure(ModTileEntity pTileEntity, BlockPos pPos, World pLevel) {
        pTileEntity.first.clear();
        pTileEntity.second.clear();
        pTileEntity.third.clear();
        for (int i = -RANGE; i <= RANGE; ++i) {
            for (int j = -RANGE; j <= RANGE; ++j) {
                for (int k = -RANGE; k <= RANGE; ++k) {
                    BlockPos blockpos1 = pPos.offset(i, j, k);
                    assert pLevel != null;
                    BlockState blockstate = pLevel.getBlockState(blockpos1);
                    if (blockstate.getBlock() == ModRegistry.DEAD_SAND.get() || blockstate.getBlock() == ModRegistry.DEAD_SANDSTONE.get()) {
                        pTileEntity.first.add(blockpos1);
                    }
                    if (blockstate.getBlock() instanceof SlabBlock) {
                        pTileEntity.second.add(blockpos1);
                    }
                    if (blockstate.getBlock() instanceof FlowerPotBlock) {
                        if (((FlowerPotBlock) blockstate.getBlock()).getContent() != Blocks.AIR){
                            pTileEntity.third.add(blockpos1);
                        }
                    }
                }
            }
        }
    }

    public static boolean checkNecroRequirements(ModTileEntity pTileEntity){
        return pTileEntity.first.size() >= 16 && pTileEntity.second.size() >= 16 && pTileEntity.third.size() >= 8;
    }

    public static void findMinorNetherStructure(ModTileEntity pTileEntity, BlockPos pPos, World pLevel) {
        pTileEntity.first.clear();
        pTileEntity.second.clear();
        pTileEntity.third.clear();
        for (int i = -RANGE; i <= RANGE; ++i) {
            for (int j = -RANGE; j <= RANGE; ++j) {
                for (int k = -RANGE; k <= RANGE; ++k) {
                    BlockPos blockpos1 = pPos.offset(i, j, k);
                    assert pLevel != null;
                    BlockState blockstate = pLevel.getBlockState(blockpos1);
                    if (blockstate.getBlock() == Blocks.NETHER_PORTAL) {
                        pTileEntity.first.add(blockpos1);
                    }
                }
            }
        }
    }

    public static boolean checkMinorNetherRequirements(ModTileEntity pTileEntity){
        return pTileEntity.first.size() >= 1 && pTileEntity.second.size() == 0 && pTileEntity.third.size() == 0;
    }

    public static void findForgeStructure(ModTileEntity pTileEntity, BlockPos pPos, World pLevel) {
        pTileEntity.first.clear();
        pTileEntity.second.clear();
        pTileEntity.third.clear();
        for (int i = -RANGE; i <= RANGE; ++i) {
            for (int j = -RANGE; j <= RANGE; ++j) {
                for (int k = -RANGE; k <= RANGE; ++k) {
                    BlockPos blockpos1 = pPos.offset(i, j, k);
                    assert pLevel != null;
                    BlockState blockstate = pLevel.getBlockState(blockpos1);
                    if (blockstate.getBlock() == Blocks.SMITHING_TABLE) {
                        pTileEntity.first.add(blockpos1);
                    }
                    if (blockstate.getBlock() == Blocks.FURNACE || blockstate.getBlock() == Blocks.BLAST_FURNACE) {
                        pTileEntity.second.add(blockpos1);
                    }
                    if (blockstate.getBlock() == Blocks.ANVIL || blockstate.getBlock() == Blocks.CHIPPED_ANVIL || blockstate.getBlock() == Blocks.DAMAGED_ANVIL) {
                        pTileEntity.third.add(blockpos1);
                    }
                }
            }
        }
    }

    public static boolean checkForgeRequirements(ModTileEntity pTileEntity){
        return pTileEntity.first.size() >= 1 && pTileEntity.second.size() >= 3 && pTileEntity.third.size() >= 4;
    }

    public static void findSabbathStructure(ModTileEntity pTileEntity, BlockPos pPos, World pLevel) {
        pTileEntity.first.clear();
        pTileEntity.second.clear();
        pTileEntity.third.clear();
        for (int i = -RANGE; i <= RANGE; ++i) {
            for (int j = -RANGE; j <= RANGE; ++j) {
                for (int k = -RANGE; k <= RANGE; ++k) {
                    BlockPos blockpos1 = pPos.offset(i, j, k);
                    assert pLevel != null;
                    BlockState blockstate = pLevel.getBlockState(blockpos1);
                    if (blockstate.getBlock() == Blocks.CRYING_OBSIDIAN) {
                        pTileEntity.first.add(blockpos1);
                    }
                    if (blockstate.getBlock() == Blocks.OBSIDIAN) {
                        pTileEntity.second.add(blockpos1);
                    }
                    if (blockstate.getBlock() == Blocks.SOUL_LANTERN) {
                        pTileEntity.third.add(blockpos1);
                    }
                }
            }
        }
    }

    public static boolean checkSabbathRequirements(ModTileEntity pTileEntity){
        return pTileEntity.first.size() >= 8 && pTileEntity.second.size() >= 16 && pTileEntity.third.size() >= 8;
    }
}
