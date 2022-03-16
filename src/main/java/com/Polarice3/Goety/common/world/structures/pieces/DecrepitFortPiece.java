package com.Polarice3.Goety.common.world.structures.pieces;

import com.Polarice3.Goety.common.entities.hostile.cultists.AbstractCultistEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.ConstantPaths;
import com.Polarice3.Goety.utils.ModLootTables;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class DecrepitFortPiece extends ModStructurePiece{
    public DecrepitFortPiece(TemplateManager mgr, BlockPos templatePosition, Rotation pRotation) {
        super(IModPieceType.DECREPIT_FORT_PIECE, mgr, templatePosition, pRotation);
        this.setYOffset(-1);
    }

    public DecrepitFortPiece(TemplateManager mgr, CompoundNBT nbt) {
        super(IModPieceType.DECREPIT_FORT_PIECE, mgr, nbt);
        this.setYOffset(-1);
    }

    @Override
    public ResourceLocation getStructureName() {
        return ConstantPaths.getDecrepitFort();
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pPos, IServerWorld pLevel, Random pRandom, MutableBoundingBox pSbb) {
        if (function.startsWith("chest")) {
            Rotation rotation = this.placeSettings.getRotation();
            BlockState blockstate = Blocks.CHEST.defaultBlockState();
            ResourceLocation loot = LootTables.EMPTY;
            switch (function) {
                case "chesttomb":
                    blockstate = blockstate.setValue(ChestBlock.FACING, rotation.rotate(Direction.NORTH));
                    loot = LootTables.STRONGHOLD_CORRIDOR;
                    break;
                case "chestnorth":
                    blockstate = blockstate.setValue(ChestBlock.FACING, rotation.rotate(Direction.NORTH));
                    loot = LootTables.SIMPLE_DUNGEON;
                    break;
                case "chestsouth":
                    blockstate = blockstate.setValue(ChestBlock.FACING, rotation.rotate(Direction.SOUTH));
                    loot = LootTables.SIMPLE_DUNGEON;
                    break;
            }

            this.createChest(pLevel, pSbb, pRandom, pPos, loot, blockstate);
        } else {
            if (function.startsWith("spawner")){
                pLevel.setBlock(pPos, Blocks.SPAWNER.defaultBlockState(), 2);
                TileEntity tileentity = pLevel.getBlockEntity(pPos);
                if (tileentity instanceof MobSpawnerTileEntity) {
                    ((MobSpawnerTileEntity)tileentity).getSpawner().setEntityId(this.randomEntityId(pRandom));
                }
            }
        }
    }

    private EntityType<?> randomEntityId(Random pRandom) {
        return net.minecraftforge.common.DungeonHooks.getRandomDungeonMob(pRandom);
    }
}
