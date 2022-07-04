package com.Polarice3.Goety.common.world.structures.pieces;

import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.ConstantPaths;
import com.Polarice3.Goety.utils.ModLootTables;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

/**
 * This class is based on a part of the Astral Sorcery Mod
 * The source code that is based for this mod can be found on Astral Sorcery github.
 * Code based made by HellFirePvP
 */

public class SalvagedFortPiece extends ModStructurePiece {

    public SalvagedFortPiece(TemplateManager mgr, BlockPos templatePosition, Rotation pRotation) {
        super(IModPieceType.SALVAGED_FORT_PIECE, mgr, templatePosition, pRotation);
        this.setYOffset(-1);
    }

    public SalvagedFortPiece(TemplateManager mgr, CompoundNBT nbt) {
        super(IModPieceType.SALVAGED_FORT_PIECE, mgr, nbt);
        this.setYOffset(-1);
    }

    @Override
    public ResourceLocation getStructureName() {
        return ConstantPaths.getSalvagedFort();
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pPos, IServerWorld pLevel, Random pRandom, MutableBoundingBox pSbb) {
        if (function.startsWith("chest")) {
            Rotation rotation = this.placeSettings.getRotation();
            BlockState blockstate = Blocks.CHEST.defaultBlockState();
            if ("chesttreasure".equals(function)) {
                blockstate = blockstate.setValue(ChestBlock.FACING, rotation.rotate(Direction.EAST));
            }

            this.createChest(pLevel, pSbb, pRandom, pPos, ModLootTables.SALVAGED_FORT_TREASURE, blockstate);
        } else {
            MonsterEntity cultist = null;
            switch (function) {
                case "fanatic":
                    cultist = ModEntityType.FANATIC.get().create(pLevel.getLevel());
                    break;
                case "zealot":
                    cultist = ModEntityType.ZEALOT.get().create(pLevel.getLevel());
                    break;
                case "jugger":
                    if (pRandom.nextFloat() <= 0.025F){
                        cultist = ModEntityType.BELDAM.get().create(pLevel.getLevel());
                    } else {
                        cultist = ModEntityType.THUG.get().create(pLevel.getLevel());
                    }
                    break;
                case "disciple":
                    cultist = ModEntityType.DISCIPLE.get().create(pLevel.getLevel());
                    break;
                case "apostle":
                    int random = pRandom.nextInt(3);
                    switch (random){
                        case 0:
                            cultist = ModEntityType.FANATIC.get().create(pLevel.getLevel());
                            break;
                        case 1:
                            cultist = ModEntityType.ZEALOT.get().create(pLevel.getLevel());
                            break;
                        case 2:
                            cultist = ModEntityType.BELDAM.get().create(pLevel.getLevel());
                            break;
                    }
                    break;
                case "chan":
                    cultist = ModEntityType.CHANNELLER.get().create(pLevel.getLevel());
                    break;
                default:
                    return;
            }

            assert cultist != null;
            cultist.setPersistenceRequired();
            cultist.moveTo(pPos, 0.0F, 0.0F);
            cultist.finalizeSpawn(pLevel, pLevel.getCurrentDifficultyAt(cultist.blockPosition()), SpawnReason.STRUCTURE, null, null);
            pLevel.addFreshEntityWithPassengers(cultist);
            pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 2);
        }
    }
}
