package com.Polarice3.Goety.common.world.structures.pieces;

import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.ConstantPaths;
import com.Polarice3.Goety.utils.ModLootTables;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;

public class WarpedShrinePiece extends ModStructurePiece {

    public WarpedShrinePiece(TemplateManager mgr, BlockPos templatePosition, Rotation pRotation) {
        super(IModPieceType.WARPED_SHRINE_PIECE, mgr, templatePosition, pRotation);
        this.setYOffset(-1);
    }

    public WarpedShrinePiece(TemplateManager mgr, CompoundNBT nbt) {
        super(IModPieceType.WARPED_SHRINE_PIECE, mgr, nbt);
        this.setYOffset(-1);
    }

    @Override
    public ResourceLocation getStructureName() {
        return ConstantPaths.getWarpedShrine();
    }

    @Override
    protected void handleDataMarker(String function, BlockPos pPos, IServerWorld pLevel, Random pRandom, MutableBoundingBox pSbb) {
        if (function.startsWith("barrel")) {
            Rotation rotation = this.placeSettings.getRotation();
            BlockState blockstate = Blocks.BARREL.defaultBlockState().setValue(BarrelBlock.FACING, rotation.rotate(Direction.UP));
            BlockFinder.createBarrel(pLevel, pSbb, pRandom, pPos, ModLootTables.WARPED_SHRINE_BARREL, blockstate);
        }
    }
}
