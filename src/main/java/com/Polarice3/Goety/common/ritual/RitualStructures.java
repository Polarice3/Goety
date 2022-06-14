package com.Polarice3.Goety.common.ritual;

import com.Polarice3.Goety.common.blocks.IDeadBlock;
import com.Polarice3.Goety.common.tileentities.RitualTileEntity;
import net.minecraft.block.*;
import net.minecraft.tileentity.LecternTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualStructures {

    public static final int RANGE = 8;

    public static void findAnimaStructure(RitualTileEntity pTileEntity, BlockPos pPos, World pLevel) {
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

    public static boolean checkAnimaRequirements(RitualTileEntity pTileEntity){
        return pTileEntity.first.size() >= 15 && pTileEntity.second.size() >= 15 && pTileEntity.third.size() >= 1;
    }

    public static void findNecroStructure(RitualTileEntity pTileEntity, BlockPos pPos, World pLevel) {
        pTileEntity.first.clear();
        pTileEntity.second.clear();
        pTileEntity.third.clear();
        for (int i = -RANGE; i <= RANGE; ++i) {
            for (int j = -RANGE; j <= RANGE; ++j) {
                for (int k = -RANGE; k <= RANGE; ++k) {
                    BlockPos blockpos1 = pPos.offset(i, j, k);
                    assert pLevel != null;
                    BlockState blockstate = pLevel.getBlockState(blockpos1);
                    if (blockstate.getBlock() instanceof IDeadBlock) {
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

    public static boolean checkNecroRequirements(RitualTileEntity pTileEntity){
        return pTileEntity.first.size() >= 16 && pTileEntity.second.size() >= 16 && pTileEntity.third.size() >= 8;
    }

    public static void findMinorNetherStructure(RitualTileEntity pTileEntity, BlockPos pPos, World pLevel) {
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

    public static boolean checkMinorNetherRequirements(RitualTileEntity pTileEntity){
        return pTileEntity.first.size() >= 1 && pTileEntity.second.size() == 0 && pTileEntity.third.size() == 0;
    }

    public static void findForgeStructure(RitualTileEntity pTileEntity, BlockPos pPos, World pLevel) {
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

    public static boolean checkForgeRequirements(RitualTileEntity pTileEntity){
        return pTileEntity.first.size() >= 1 && pTileEntity.second.size() >= 3 && pTileEntity.third.size() >= 4;
    }

    public static void findMagicStructure(RitualTileEntity pTileEntity, BlockPos pPos, World pLevel) {
        pTileEntity.first.clear();
        pTileEntity.second.clear();
        pTileEntity.third.clear();
        for (int i = -RANGE; i <= RANGE; ++i) {
            for (int j = -RANGE; j <= RANGE; ++j) {
                for (int k = -RANGE; k <= RANGE; ++k) {
                    BlockPos blockpos1 = pPos.offset(i, j, k);
                    assert pLevel != null;
                    BlockState blockstate = pLevel.getBlockState(blockpos1);
                    if (blockstate.getBlock() == Blocks.BOOKSHELF) {
                        pTileEntity.first.add(blockpos1);
                    }
                    if (blockstate.getBlock() instanceof LecternBlock) {
                        if (blockstate.hasTileEntity() && pLevel.getBlockEntity(blockpos1) instanceof LecternTileEntity){
                            LecternTileEntity lecternTileEntity = (LecternTileEntity) pLevel.getBlockEntity(blockpos1);
                            if (lecternTileEntity.hasBook()){
                                pTileEntity.second.add(blockpos1);
                            }
                        }
                    }
                    if (blockstate.getBlock() instanceof EnchantingTableBlock) {
                        pTileEntity.third.add(blockpos1);
                    }
                }
            }
        }
    }

    public static boolean checkMagicRequirements(RitualTileEntity pTileEntity){
        return pTileEntity.first.size() >= 16 && pTileEntity.second.size() >= 1 && pTileEntity.third.size() >= 2;
    }

    public static void findSabbathStructure(RitualTileEntity pTileEntity, BlockPos pPos, World pLevel) {
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
                    if (blockstate.getBlock() == Blocks.SOUL_FIRE) {
                        pTileEntity.third.add(blockpos1);
                    }
                }
            }
        }
    }

    public static boolean checkSabbathRequirements(RitualTileEntity pTileEntity){
        return pTileEntity.first.size() >= 8 && pTileEntity.second.size() >= 16 && pTileEntity.third.size() >= 4;
    }

    public static void findExpertNetherStructure(RitualTileEntity pTileEntity, BlockPos pPos, World pLevel) {
        pTileEntity.first.clear();
        pTileEntity.second.clear();
        pTileEntity.third.clear();
        for (int i = -RANGE; i <= RANGE; ++i) {
            for (int j = -RANGE; j <= RANGE; ++j) {
                for (int k = -RANGE; k <= RANGE; ++k) {
                    BlockPos blockpos1 = pPos.offset(i, j, k);
                    assert pLevel != null;
                    BlockState blockstate = pLevel.getBlockState(blockpos1);
                    if (blockstate.getBlock() == Blocks.CHISELED_NETHER_BRICKS) {
                        pTileEntity.first.add(blockpos1);
                    }
                    if (blockstate.getBlock() == Blocks.NETHER_BRICKS || blockstate.getBlock() == Blocks.RED_NETHER_BRICKS) {
                        pTileEntity.second.add(blockpos1);
                    }
                    if (blockstate.getBlock() == Blocks.NETHER_WART) {
                        pTileEntity.third.add(blockpos1);
                    }
                }
            }
        }
    }

    public static boolean checkExpertNetherRequirements(RitualTileEntity pTileEntity){
        return pTileEntity.first.size() >= 8 && pTileEntity.second.size() >= 32 && pTileEntity.third.size() >= 8;
    }
}
