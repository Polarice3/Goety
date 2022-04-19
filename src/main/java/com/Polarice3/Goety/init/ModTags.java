package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.tags.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ModTags {
    public static void init ()
    {
        ModTags.Blocks.init();
        ModTags.EntityTypes.init();
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
            return BlockTags.createOptional(Goety.location(name));
        }
    }

    public static class EntityTypes
    {
        private static void init(){}

        public static final ITag.INamedTag<EntityType<?>> VILLAGERS = tag(new ResourceLocation("forge", "villagers"));

        public static ITag.INamedTag<EntityType<?>> tag(ResourceLocation id) {
            return EntityTypeTags.bind(id.toString());
        }
    }
}
