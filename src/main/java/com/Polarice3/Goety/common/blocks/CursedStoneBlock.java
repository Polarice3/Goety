package com.Polarice3.Goety.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class CursedStoneBlock extends Block {

    public CursedStoneBlock() {
        super(Properties.of(Material.STONE)
                .strength(3.0F, 9.0F)
                .sound(SoundType.STONE)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)
        );
    }


}
