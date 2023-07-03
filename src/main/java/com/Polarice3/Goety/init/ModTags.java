package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ModTags {
    public static void init ()
    {
        ModTags.Blocks.init();
        ModTags.Fluids.init();
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
        public static final Tags.IOptionalNamedTag<Block> RECALL_BLOCKS = tag("recall_blocks");

        private static Tags.IOptionalNamedTag<Block> tag(String name)
        {
            return BlockTags.createOptional(Goety.location(name));
        }
    }

    public static class Fluids
    {
        private static void init(){}
        public static final Tags.IOptionalNamedTag<Fluid> QUICKSAND = tag("quicksand");

        private static Tags.IOptionalNamedTag<Fluid> tag(String name)
        {
            return FluidTags.createOptional(Goety.location(name));
        }
    }

    public static class Items
    {
        private static void init(){}

        public static final Tags.IOptionalNamedTag<Item> RAW_MUTATED_MEAT = tag("raw_mutated_meats");
        public static final Tags.IOptionalNamedTag<Item> LICH_WITHER_ITEMS = tag("lich_wither_items");

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
        public static final Tags.IOptionalNamedTag<EntityType<?>> BOSSES = tag("bosses");

        public static ITag.INamedTag<EntityType<?>> tag(ResourceLocation id) {
            return EntityTypeTags.bind(id.toString());
        }

        private static Tags.IOptionalNamedTag<EntityType<?>> tag(String name) {
            return EntityTypeTags.createOptional(Goety.location(name));
        }
    }
}
