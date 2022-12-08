package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.MainConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class DeadSandStoneBlock extends Block implements IDeadBlock {

    public DeadSandStoneBlock() {
        super(Properties.of(Material.STONE, MaterialColor.COLOR_PURPLE)
                .strength(0.8F)
                .sound(SoundType.STONE)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)
                .randomTicks()
        );
    }

    public void randomTick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom) {
        if (MainConfig.DeadSandStoneSpread.get()){
            if (pRandom.nextFloat() <= 0.05F) {
                this.spreadSand(pLevel, pPos, pRandom);
            } else {
                this.spreadSandOnly(pLevel, pPos, pRandom);
            }
        } else {
            this.spreadSandOnly(pLevel, pPos, pRandom);
        }
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

}
