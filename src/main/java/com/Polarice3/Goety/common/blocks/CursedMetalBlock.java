package com.Polarice3.Goety.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class CursedMetalBlock extends Block {

    public CursedMetalBlock() {
        super(Properties.of(Material.STONE)
                .strength(5.0F)
                .sound(SoundType.METAL)
                .harvestLevel(0)
                .requiresCorrectToolForDrops()
                .harvestTool(ToolType.PICKAXE)
        );
    }
}
