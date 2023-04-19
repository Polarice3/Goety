package com.Polarice3.Goety.common.ritual;

import com.Polarice3.Goety.common.blocks.IDeadBlock;
import com.Polarice3.Goety.common.tileentities.RitualTileEntity;
import net.minecraft.block.*;
import net.minecraft.tileentity.LecternTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualStructures {

    public static final int RANGE = 8;

    public static boolean getProperStructure(String craftType, RitualTileEntity pTileEntity, BlockPos pPos, World pLevel){
        findStructures(craftType, pTileEntity, pPos, pLevel);
        switch (craftType){
            case "animalis":
            case "necroturgy":
            case "lich":
            case "minor_nether":
            case "forge":
            case "magic":
            case "sabbath":
                return RitualStructures.checkRequirements(craftType, pTileEntity);
            case "adept_nether":
                return pLevel.dimensionType().ultraWarm();
            case "expert_nether":
                return RitualStructures.checkRequirements(craftType, pTileEntity) && pLevel.dimensionType().ultraWarm();
            case "air":
                return pPos.getY() >= 128;
            case "storm":
                return RitualStructures.checkRequirements(craftType, pTileEntity) && pPos.getY() >= 128 && pLevel.isThundering() && pLevel.canSeeSky(pPos);
        }
        return false;
    }

    public static void findStructures(String craftType, RitualTileEntity pTileEntity, BlockPos pPos, World pLevel) {
        pTileEntity.first.clear();
        pTileEntity.second.clear();
        pTileEntity.third.clear();
        for (int i = -RANGE; i <= RANGE; ++i) {
            for (int j = -RANGE; j <= RANGE; ++j) {
                for (int k = -RANGE; k <= RANGE; ++k) {
                    BlockPos blockpos1 = pPos.offset(i, j, k);
                    assert pLevel != null;
                    BlockState blockstate = pLevel.getBlockState(blockpos1);
                    getBlocks(craftType, pTileEntity, blockstate, blockpos1, pLevel);
                }
            }
        }
    }

    public static void getBlocks(String craftType, RitualTileEntity pTileEntity, BlockState pState, BlockPos pPos, World pLevel){
        switch (craftType){
            case "animalis":
                if (pState.getBlock() instanceof LadderBlock) {
                    pTileEntity.first.add(pPos);
                }
                if (pState.getBlock() instanceof RailBlock) {
                    pTileEntity.second.add(pPos);
                }
                if (pState.getBlock() instanceof CarvedPumpkinBlock) {
                    pTileEntity.third.add(pPos);
                }
            case "necroturgy":
            case "lich":
                if (pState.getBlock() instanceof IDeadBlock) {
                    pTileEntity.first.add(pPos);
                }
                if (pState.getBlock() instanceof SlabBlock) {
                    pTileEntity.second.add(pPos);
                }
                if (pState.getBlock() instanceof FlowerPotBlock) {
                    if (((FlowerPotBlock) pState.getBlock()).getContent() != Blocks.AIR){
                        pTileEntity.third.add(pPos);
                    }
                }
            case "minor_nether":
                if (pState.getBlock() == Blocks.NETHER_PORTAL) {
                    pTileEntity.first.add(pPos);
                }
            case "forge":
                if (pState.getBlock() instanceof SmithingTableBlock) {
                    pTileEntity.first.add(pPos);
                }
                if (pState.getBlock() instanceof FurnaceBlock || pState.getBlock() instanceof BlastFurnaceBlock) {
                    pTileEntity.second.add(pPos);
                }
                if (pState.getBlock() instanceof AnvilBlock) {
                    pTileEntity.third.add(pPos);
                }
            case "magic":
                if (pState.getBlock().getDescriptionId().contains("bookshelf")) {
                    pTileEntity.first.add(pPos);
                }
                if (pState.getBlock() instanceof LecternBlock) {
                    if (pState.hasTileEntity() && pLevel.getBlockEntity(pPos) instanceof LecternTileEntity){
                        LecternTileEntity lecternTileEntity = (LecternTileEntity) pLevel.getBlockEntity(pPos);
                        if (lecternTileEntity != null) {
                            if (!lecternTileEntity.getBook().isEmpty()) {
                                pTileEntity.second.add(pPos);
                            }
                        }
                    }
                }
                if (pState.getBlock() instanceof EnchantingTableBlock) {
                    pTileEntity.third.add(pPos);
                }
            case "sabbath":
                if (pState.getBlock() == Blocks.CRYING_OBSIDIAN) {
                    pTileEntity.first.add(pPos);
                }
                if (pState.getBlock() == Blocks.OBSIDIAN) {
                    pTileEntity.second.add(pPos);
                }
                if (pState.getBlock() == Blocks.SOUL_FIRE) {
                    pTileEntity.third.add(pPos);
                }
            case "expert_nether":
                if (pState.getBlock() == Blocks.WITHER_SKELETON_SKULL || pState.getBlock() == Blocks.WITHER_SKELETON_WALL_SKULL) {
                    pTileEntity.first.add(pPos);
                }
                if (pState.getBlock() == Blocks.NETHER_BRICKS || pState.getBlock() == Blocks.RED_NETHER_BRICKS) {
                    pTileEntity.second.add(pPos);
                }
                if (pState.getBlock() == Blocks.NETHER_WART) {
                    pTileEntity.third.add(pPos);
                }
            case "storm":
                if (pState.getBlock() == Blocks.IRON_BLOCK) {
                    pTileEntity.first.add(pPos);
                }
                if (pState.getBlock() == Blocks.GLOWSTONE) {
                    pTileEntity.second.add(pPos);
                }
                if (pState.getBlock() instanceof ChainBlock) {
                    pTileEntity.third.add(pPos);
                }
        }
    }

    public static boolean checkRequirements(String craftType, RitualTileEntity pTileEntity){
        int first = 0;
        int second = 0;
        int third = 0;
        switch (craftType){
            case "animalis":
                first = 15;
                second = 15;
                third = 1;
                break;
            case "necroturgy":
            case "lich":
                first = 16;
                second = 16;
                third = 8;
                break;
            case "minor_nether":
                first = 1;
                break;
            case "forge":
                first = 1;
                second = 3;
                third = 4;
                break;
            case "magic":
                first = 16;
                second = 1;
                third = 2;
                break;
            case "sabbath":
                first = 8;
                second = 16;
                third = 4;
                break;
            case "expert_nether":
                first = 4;
                second = 32;
                third = 8;
                break;
            case "storm":
                first = 12;
                second = 4;
                third = 20;
        }
        return pTileEntity.first.size() >= first && pTileEntity.second.size() >= second && pTileEntity.third.size() >= third;
    }
}
