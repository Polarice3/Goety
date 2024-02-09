package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.blocks.tiles.GuardianObeliskTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeBlock;

import javax.annotation.Nullable;

public class GuardianObeliskBlock extends ContainerBlock implements IForgeBlock {
    public static final VoxelShape SHAPE_BASE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    public static final VoxelShape SHAPE_BASE2 = Block.box(2.0D, 2.0D, 2.0D, 12.0D, 2.0D, 12.0D);
    public static final VoxelShape SHAPE_BASE3 = Block.box(3.0D, 4.0D, 3.0D, 10.0D, 2.0D, 10.0D);
    public static final VoxelShape SHAPE_PILLAR = Block.box(4.0D, 6.0D, 4.0D, 8.0D, 18.0D, 8.0D);
    public static final VoxelShape SHAPE_COMMON = VoxelShapes.or(SHAPE_BASE, SHAPE_BASE2, SHAPE_BASE3);
    public static final VoxelShape SHAPE_COLLISION = VoxelShapes.or(SHAPE_COMMON, SHAPE_PILLAR);
    public static final VoxelShape SHAPE_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 24.0D, 16.0D);

    public GuardianObeliskBlock() {
        super(Properties.of(Material.STONE)
                .strength(5.0F, 9.0F)
                .sound(SoundType.STONE)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)
        );
    }

    @Override
    public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader level, BlockPos pos, int fortune, int silktouch) {
        return 15 + RANDOM.nextInt(15) + RANDOM.nextInt(15);
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public VoxelShape getOcclusionShape(BlockState pState, IBlockReader pLevel, BlockPos pPos) {
        return SHAPE_COLLISION;
    }

    public VoxelShape getCollisionShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return SHAPE_COLLISION;
    }

    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return SHAPE_SHAPE;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new GuardianObeliskTileEntity();
    }
}
