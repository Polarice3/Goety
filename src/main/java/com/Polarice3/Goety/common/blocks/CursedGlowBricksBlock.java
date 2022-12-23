package com.Polarice3.Goety.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class CursedGlowBricksBlock extends Block {

    public CursedGlowBricksBlock() {
        super(Properties.of(Material.STONE)
                .strength(5.0F, 9.0F)
                .sound(SoundType.STONE)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)
                .lightLevel((p_235464_0_) -> 15)
        );
    }

}
