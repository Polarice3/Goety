package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ModTags {
    public static void init ()
    {
        ModTags.Blocks.init();
        ModTags.Items.init();
        ModTags.EntityTypes.init();
    }

    public static class Blocks
    {
        private static void init(){}

        public static final Tags.IOptionalNamedTag<Block> DEAD_SAND_SPREADABLE = tag("dead_sand_spreadable");
        public static final Tags.IOptionalNamedTag<Block> DEAD_BLOCK_SPREADABLE = tag("dead_block_spreadable");
        public static final Tags.IOptionalNamedTag<Block> CURSED_BLOCKS = tag("cursed_blocks");
        public static final Tags.IOptionalNamedTag<Block> DEAD_SANDS = tag("dead_sands_blocks");
        public static final Tags.IOptionalNamedTag<Block> HAUNTED_WOOD = tag("haunted_wood_blocks");
        public static final Tags.IOptionalNamedTag<Block> TERRACOTTAS = tag("terracottas");

        private static Tags.IOptionalNamedTag<Block> tag(String name)
        {
            return BlockTags.createOptional(Goety.location(name));
        }
    }

    public static class Items
    {
        private static void init(){}

        public static final Tags.IOptionalNamedTag<Item> RAW_MUTATED_MEAT = tag("raw_mutated_meats");

        private static Tags.IOptionalNamedTag<Item> tag(String name)
        {
            return ItemTags.createOptional(Goety.location(name));
        }
    }

    public static class EntityTypes
    {
        private static void init(){}

        public static final ITag.INamedTag<EntityType<?>> VILLAGERS = tag(new ResourceLocation("forge", "villagers"));
        public static final Tags.IOptionalNamedTag<EntityType<?>> FROST_EXTRA_DAMAGE = tag("frost_extra_damage");
        public static final Tags.IOptionalNamedTag<EntityType<?>> FROST_IMMUNE = tag("frost_immune");

        public static ITag.INamedTag<EntityType<?>> tag(ResourceLocation id) {
            return EntityTypeTags.bind(id.toString());
        }

        private static Tags.IOptionalNamedTag<EntityType<?>> tag(String name) {
            return EntityTypeTags.createOptional(Goety.location(name));
        }
    }
}
