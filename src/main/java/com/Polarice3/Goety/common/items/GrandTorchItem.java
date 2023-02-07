package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.WallOrFloorItem;

public class GrandTorchItem extends WallOrFloorItem {

    public GrandTorchItem(Block pStandingBlock, Block pWallBlock) {
        super(pStandingBlock, pWallBlock, new Item.Properties().tab(Goety.TAB));
    }
}
