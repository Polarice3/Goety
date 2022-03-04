package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.Goety;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public class BlockItemBase extends BlockItem {
    public BlockItemBase(Block blockIn) {
        super(blockIn, new Properties().tab(Goety.TAB));
    }
}
