package com.Polarice3.Goety.common.world.structures.pieces;

import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.ConstantPaths;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class PortalOutpostPiece extends ModStructurePiece{
    public PortalOutpostPiece(TemplateManager mgr, BlockPos templatePosition, Rotation pRotation) {
        super(IModPieceType.PORTAL_OUTPOST_PIECE, mgr, templatePosition, pRotation);
        this.setYOffset(-1);
    }

    public PortalOutpostPiece(TemplateManager mgr, CompoundNBT nbt) {
        super(IModPieceType.PORTAL_OUTPOST_PIECE, mgr, nbt);
        this.setYOffset(-1);
    }

    @Override
    public ResourceLocation getStructureName() {
        return ConstantPaths.getPortalOutpost();
    }

    @Override
    protected void handleDataMarker(String pFunction, BlockPos pPos, IServerWorld pLevel, Random pRandom, MutableBoundingBox pSbb) {
        if (pFunction.startsWith("barrel")){
            Rotation rotation = this.placeSettings.getRotation();
            BlockState blockstate = Blocks.BARREL.defaultBlockState();
            ResourceLocation lootTable = LootTables.VILLAGE_PLAINS_HOUSE;
            switch (pFunction){
                case "barrel_east":
                    blockstate = blockstate.setValue(BarrelBlock.FACING, rotation.rotate(Direction.EAST));
                    break;
                case "barrel_south":
                    blockstate = blockstate.setValue(BarrelBlock.FACING, rotation.rotate(Direction.SOUTH));
                    lootTable = LootTables.VILLAGE_ARMORER;
                    break;
                case "barrel_south_up":
                    blockstate = blockstate.setValue(BarrelBlock.FACING, rotation.rotate(Direction.SOUTH));
                    lootTable = LootTables.VILLAGE_WEAPONSMITH;
                    break;
                case "barrel_west":
                    blockstate = blockstate.setValue(BarrelBlock.FACING, rotation.rotate(Direction.WEST));
                    lootTable = LootTables.VILLAGE_FLETCHER;
                    break;
            }
            BlockFinder.createBarrel(pLevel, pSbb, pRandom, pPos, lootTable, blockstate);
        } else if (pFunction.startsWith("chest")) {
            Rotation rotation = this.placeSettings.getRotation();
            BlockState blockstate = Blocks.CHEST.defaultBlockState();
            blockstate = blockstate.setValue(ChestBlock.FACING, rotation.rotate(Direction.WEST));

            this.createChest(pLevel, pSbb, pRandom, pPos, LootTables.VILLAGE_BUTCHER, blockstate);
        } else {
            MonsterEntity cultist;
            switch (pFunction) {
                case "fanatic":
                    cultist = ModEntityType.FANATIC.get().create(pLevel.getLevel());
                    break;
                case "zealot":
                    cultist = ModEntityType.ZEALOT.get().create(pLevel.getLevel());
                    break;
                case "disciple":
                    cultist = ModEntityType.DISCIPLE.get().create(pLevel.getLevel());
                    break;
                default:
                    return;
            }
            if (cultist != null) {
                cultist.setPersistenceRequired();
                cultist.moveTo(pPos, 0.0F, 0.0F);
                cultist.finalizeSpawn(pLevel, pLevel.getCurrentDifficultyAt(cultist.blockPosition()), SpawnReason.STRUCTURE, null, null);
                pLevel.addFreshEntityWithPassengers(cultist);
                pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 2);
            }
        }
    }
}
