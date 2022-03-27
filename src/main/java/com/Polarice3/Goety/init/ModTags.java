package com.Polarice3.Goety.init;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ModTags {
    public static void init ()
    {
        ModTags.Blocks.init();
    }

    public static class Blocks
    {
        private static void init(){}

        public static final Tags.IOptionalNamedTag<Block> DEAD_SAND_IMMUNE = tag("dead_sand_immune");
        public static final Tags.IOptionalNamedTag<Block> CURSED_BLOCKS = tag("cursed_blocks");
        public static final Tags.IOptionalNamedTag<Block> DEAD_SANDS = tag("dead_sands_blocks");
        public static final Tags.IOptionalNamedTag<Block> HAUNTED_WOOD = tag("haunted_wood_blocks");

        private static Tags.IOptionalNamedTag<Block> tag(String name)
        {
            return BlockTags.createOptional(new ResourceLocation("goety", name));
        }
    }
}
