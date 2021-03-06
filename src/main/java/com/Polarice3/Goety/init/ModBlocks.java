package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.blocks.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SignItem;
import net.minecraft.item.TallBlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Goety.MOD_ID);
    public static DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Goety.MOD_ID);

    public static void init(){
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCK_ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    //Blocks
    public static final RegistryObject<Block> BLAZE_CORE_BLOCK = BLOCKS.register("blazecoreblock", TankCoreBlock::new);
    public static final RegistryObject<Block> CURSED_METAL_BLOCK = BLOCKS.register("cursed_block", CursedMetalBlock::new);
    public static final RegistryObject<Block> CURSED_STONE_BLOCK = BLOCKS.register("cursed_stone", CursedStoneBlock::new);
    public static final RegistryObject<Block> CURSED_STONE_BRICK_BLOCK = BLOCKS.register("cursed_stone_bricks", CursedStoneBlock::new);
    public static final RegistryObject<Block> CURSED_BRICK_BLOCK = BLOCKS.register("cursed_bricks", CursedStoneBlock::new);
    public static final RegistryObject<Block> CURSED_STONE_CHISELED_BLOCK = BLOCKS.register("cursed_stone_chiseled", CursedStoneBlock::new);
    public static final RegistryObject<Block> CURSED_TILES_BLOCK = BLOCKS.register("cursed_tiles", CursedStoneBlock::new);
    public static final RegistryObject<Block> CURSED_CAGE_BLOCK = BLOCKS.register("cursed_cage", CursedCageBlock::new);
    public static final RegistryObject<Block> CURSED_TOTEM_BLOCK = BLOCKS.register("cursed_totem", CursedStoneBlock::new);
    public static final RegistryObject<Block> FANG_TOTEM = BLOCKS.register("fang_totem", FangTotemBlock::new);
    public static final RegistryObject<Block> MUTATE_TOTEM = BLOCKS.register("mutation_totem", MutateTotemBlock::new);
    public static final RegistryObject<Block> UNDEAD_TOTEM = BLOCKS.register("undead_totem", UndeadTotemBlock::new);
    public static final RegistryObject<Block> WIND_TOTEM = BLOCKS.register("wind_totem", WindTotemBlock::new);
    public static final RegistryObject<Block> SOUL_FANG_TOTEM = BLOCKS.register("soul_fang_totem", SoulFangTotemBlock::new);
    public static final RegistryObject<Block> OBELISK = BLOCKS.register("obelisk", ObeliskBlock::new);
    public static final RegistryObject<Block> GUARDIAN_OBELISK = BLOCKS.register("guardian_obelisk", GuardianObeliskBlock::new);
    public static final RegistryObject<Block> CURSED_BURNER = BLOCKS.register("cursed_burner", CursedBurnerBlock::new);
    public static final RegistryObject<Block> CURSED_KILN = BLOCKS.register("cursed_kiln", CursedKilnBlock::new);
    public static final RegistryObject<Block> DARK_ALTAR = BLOCKS.register("dark_altar", DarkAltarBlock::new);
    public static final RegistryObject<Block> PEDESTAL = BLOCKS.register("pedestal", PedestalBlock::new);
    public static final RegistryObject<Block> DEAD_SAND = BLOCKS.register("dead_sand", DeadSandBlock::new);
    public static final RegistryObject<Block> DEAD_SANDSTONE = BLOCKS.register("dead_sandstone", DeadSandStoneBlock::new);
    public static final RegistryObject<Block> DEAD_SANDSTONE_CHISELED = BLOCKS.register("dead_sandstone_chiseled", DeadSandStoneBlock::new);
    public static final RegistryObject<Block> DEAD_SANDSTONE_CUT = BLOCKS.register("dead_sandstone_cut", DeadSandStoneBlock::new);
    public static final RegistryObject<Block> DEAD_SANDSTONE_SMOOTH = BLOCKS.register("dead_sandstone_smooth", DeadSandStoneBlock::new);
    public static final RegistryObject<Block> DEAD_BLOCK = BLOCKS.register("dead_block", DeadBlock::new);
    public static final RegistryObject<Block> TEMP_WEB = BLOCKS.register("temp_web", TempWebBlock::new);
    public static final RegistryObject<Block> DARK_CLOUD = BLOCKS.register("dark_cloud", DarkCloudBlock::new);
    public static final RegistryObject<Block> ARCA_BLOCK = BLOCKS.register("arca", ArcaBlock::new);
    public static final RegistryObject<Block> SOUL_LIGHT_BLOCK = BLOCKS.register("soul_light", SoulLightBlock::new);
    public static final RegistryObject<Block> GLOW_LIGHT_BLOCK = BLOCKS.register("glow_light", GlowLightBlock::new);
    public static final RegistryObject<Block> PITHOS_BLOCK = BLOCKS.register("pithos", PithosBlock::new);

    //Plants
    public static final RegistryObject<Block> HAUNTED_CACTUS = BLOCKS.register("haunted_cactus", HauntedCactusBlock::new);
    public static final RegistryObject<Block> HAUNTED_BUSH = BLOCKS.register("haunted_bush", HauntedBushBlock::new);
    public static final RegistryObject<Block> IRON_FINGER = BLOCKS.register("iron_finger", IronFingerBlock::new);

    //Tree
    public static final RegistryObject<Block> HAUNTED_PLANKS = BLOCKS.register("haunted_planks",
            () -> new Block(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_GRAY).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> HAUNTED_LOG = BLOCKS.register("haunted_log", HauntedLogBlock::new);
    public static final RegistryObject<Block> STRIPPED_HAUNTED_LOG = BLOCKS.register("stripped_haunted_log", HauntedLogBlock::new);
    public static final RegistryObject<Block> HAUNTED_WOOD = BLOCKS.register("haunted_wood",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_GRAY).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> STRIPPED_HAUNTED_WOOD = BLOCKS.register("stripped_haunted_wood",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_GRAY).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> HAUNTED_PRESSURE_PLATE = BLOCKS.register("haunted_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, AbstractBlock.Properties.of(Material.WOOD, HAUNTED_PLANKS.get().defaultMaterialColor()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> DARK_PRESSURE_PLATE = BLOCKS.register("dark_pressure_plate", () -> new SmartPressurePlateBlock(AbstractBlock.Properties.of(Material.WOOD, HAUNTED_PLANKS.get().defaultMaterialColor()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> HAUNTED_TRAPDOOR = BLOCKS.register("haunted_trapdoor",
            () -> new TrapDoorBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_GRAY).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Block> HAUNTED_BUTTON = BLOCKS.register("haunted_button",
            () -> new WoodButtonBlock(AbstractBlock.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> HAUNTED_STAIRS = BLOCKS.register("haunted_stairs",
            () -> new StairsBlock(HAUNTED_PLANKS.get().defaultBlockState(), AbstractBlock.Properties.copy(HAUNTED_PLANKS.get())));
    public static final RegistryObject<Block> HAUNTED_SLAB = BLOCKS.register("haunted_slab",
            () -> new SlabBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_GRAY).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> HAUNTED_FENCE_GATE = BLOCKS.register("haunted_fence_gate",
            () -> new FenceGateBlock(AbstractBlock.Properties.of(Material.WOOD, HAUNTED_PLANKS.get().defaultMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> HAUNTED_FENCE = BLOCKS.register("haunted_fence",
            () -> new FenceBlock(AbstractBlock.Properties.of(Material.WOOD, HAUNTED_PLANKS.get().defaultMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> HAUNTED_DOOR = BLOCKS.register("haunted_door",
            () -> new DoorBlock(AbstractBlock.Properties.of(Material.WOOD, HAUNTED_PLANKS.get().defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Block> HAUNTED_SIGN = BLOCKS.register("haunted_sign",
            () -> new ModStandSignBlock(AbstractBlock.Properties.of(Material.WOOD, HAUNTED_PLANKS.get().defaultMaterialColor()).noCollission().strength(1.0F).sound(SoundType.WOOD), ModWoodType.HAUNTED));
    public static final RegistryObject<Block> HAUNTED_WALL_SIGN = BLOCKS.register("haunted_wall_sign",
            () -> new ModWallSignBlock(AbstractBlock.Properties.of(Material.WOOD, HAUNTED_PLANKS.get().defaultMaterialColor()).noCollission().strength(1.0F).sound(SoundType.WOOD).dropsLike(HAUNTED_SIGN.get()), ModWoodType.HAUNTED));
    //Slabs
    public static final RegistryObject<Block> CURSED_STONE_SLAB_BLOCK = BLOCKS.register("cursed_stone_slab",
            () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(3.0F, 9.0F)
                    .sound(SoundType.STONE)
                    .harvestLevel(0)
                    .harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> CURSED_STONE_BRICK_SLAB_BLOCK = BLOCKS.register("cursed_stone_bricks_slab",
            () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(3.0F, 9.0F)
                    .sound(SoundType.STONE)
                    .harvestLevel(0)
                    .harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> CURSED_BRICK_SLAB_BLOCK = BLOCKS.register("cursed_bricks_slab",
            () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(3.0F, 9.0F)
                    .sound(SoundType.STONE)
                    .harvestLevel(0)
                    .harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> CURSED_TILES_SLAB_BLOCK = BLOCKS.register("cursed_tiles_slab",
            () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(3.0F, 9.0F)
                    .sound(SoundType.STONE)
                    .harvestLevel(0)
                    .harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> DEAD_SANDSTONE_SLAB_BLOCK = BLOCKS.register("dead_sandstone_slab",
            () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE)
                    .strength(2.0F, 6.0F)
                    .harvestLevel(0)
                    .harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> DEAD_SANDSTONE_CUT_SLAB_BLOCK = BLOCKS.register("dead_sandstone_cut_slab",
            () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE)
                    .strength(2.0F, 6.0F)
                    .harvestLevel(0)
                    .harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> DEAD_SANDSTONE_SMOOTH_SLAB_BLOCK = BLOCKS.register("dead_sandstone_smooth_slab",
            () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE)
                    .strength(2.0F, 6.0F)
                    .harvestLevel(0)
                    .harvestTool(ToolType.PICKAXE)));


    //Stairs
    public static final RegistryObject<Block> CURSED_STONE_STAIRS_BLOCK = BLOCKS.register("cursed_stone_stairs",
            () -> new StairsBlock(CURSED_STONE_BLOCK.get().defaultBlockState(), AbstractBlock.Properties.copy(CURSED_STONE_BLOCK.get())));
    public static final RegistryObject<Block> CURSED_STONE_BRICKS_STAIRS_BLOCK = BLOCKS.register("cursed_stone_bricks_stairs",
            () -> new StairsBlock(CURSED_STONE_BRICK_BLOCK.get().defaultBlockState(), AbstractBlock.Properties.copy(CURSED_STONE_BRICK_BLOCK.get())));
    public static final RegistryObject<Block> CURSED_BRICK_STAIRS_BLOCK = BLOCKS.register("cursed_bricks_stairs",
            () -> new StairsBlock(CURSED_BRICK_BLOCK.get().defaultBlockState(), AbstractBlock.Properties.copy(CURSED_BRICK_BLOCK.get())));
    public static final RegistryObject<Block> CURSED_TILES_STAIRS_BLOCK = BLOCKS.register("cursed_tiles_stairs",
            () -> new StairsBlock(CURSED_TILES_BLOCK.get().defaultBlockState(), AbstractBlock.Properties.copy(CURSED_TILES_BLOCK.get())));
    public static final RegistryObject<Block> DEAD_SANDSTONE_STAIRS_BLOCK = BLOCKS.register("dead_sandstone_stairs",
            () -> new StairsBlock(DEAD_SANDSTONE.get().defaultBlockState(), AbstractBlock.Properties.copy(DEAD_SANDSTONE.get())));
    public static final RegistryObject<Block> DEAD_SANDSTONE_SMOOTH_STAIRS_BLOCK = BLOCKS.register("dead_sandstone_smooth_stairs",
            () -> new StairsBlock(DEAD_SANDSTONE_SMOOTH.get().defaultBlockState(), AbstractBlock.Properties.copy(DEAD_SANDSTONE_SMOOTH.get())));
    //Walls
    public static final RegistryObject<Block> CURSED_BRICK_WALL_BLOCK = BLOCKS.register("cursed_bricks_wall",
            () -> new WallBlock(AbstractBlock.Properties.copy(CURSED_BRICK_BLOCK.get())));
    public static final RegistryObject<Block> GRAVE_BRICK_WALL_BLOCK = BLOCKS.register("grave_bricks_wall", GraveWallBlock::new);
    public static final RegistryObject<Block> CURSED_STONE_BRICK_WALL_BLOCK = BLOCKS.register("cursed_stone_bricks_wall",
            () -> new WallBlock(AbstractBlock.Properties.copy(CURSED_STONE_BRICK_BLOCK.get())));
    public static final RegistryObject<Block> DEAD_SANDSTONE_WALL_BLOCK = BLOCKS.register("dead_sandstone_wall",
            () -> new WallBlock(AbstractBlock.Properties.copy(DEAD_SANDSTONE.get())));
    //Panes
    public static final RegistryObject<Block> CURSED_BARS_BLOCK = BLOCKS.register("cursed_bars",
            () -> new PaneBlock(AbstractBlock.Properties.of(Material.METAL, MaterialColor.NONE)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));
    //Block Items
    public static final RegistryObject<Item> BLAZE_CORE_ITEM = BLOCK_ITEMS.register("blazecoreblock",
            () -> new BlockItemBase(BLAZE_CORE_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_METAL_BLOCK_ITEM = BLOCK_ITEMS.register("cursed_block",
            () -> new BlockItemBase(CURSED_METAL_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_STONE_ITEM = BLOCK_ITEMS.register("cursed_stone",
            () -> new BlockItemBase(CURSED_STONE_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_STONE_SLAB_ITEM = BLOCK_ITEMS.register("cursed_stone_slab",
            () -> new BlockItemBase(CURSED_STONE_SLAB_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_STONE_STAIRS_ITEM = BLOCK_ITEMS.register("cursed_stone_stairs",
            () -> new BlockItemBase(CURSED_STONE_STAIRS_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_STONE_BRICKS_ITEM = BLOCK_ITEMS.register("cursed_stone_bricks",
            () -> new BlockItemBase(CURSED_STONE_BRICK_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_STONE_BRICKS_SLAB_ITEM = BLOCK_ITEMS.register("cursed_stone_bricks_slab",
            () -> new BlockItemBase(CURSED_STONE_BRICK_SLAB_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_STONE_BRICKS_STAIRS_ITEM = BLOCK_ITEMS.register("cursed_stone_bricks_stairs",
            () -> new BlockItemBase(CURSED_STONE_BRICKS_STAIRS_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_STONE_BRICK_WALL_ITEM = BLOCK_ITEMS.register("cursed_stone_bricks_wall",
            () -> new BlockItemBase(CURSED_STONE_BRICK_WALL_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_BRICK_ITEM = BLOCK_ITEMS.register("cursed_bricks",
            () -> new BlockItemBase(CURSED_BRICK_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_BRICK_SLAB_ITEM = BLOCK_ITEMS.register("cursed_bricks_slab",
            () -> new BlockItemBase(CURSED_BRICK_SLAB_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_BRICK_STAIRS_ITEM = BLOCK_ITEMS.register("cursed_bricks_stairs",
            () -> new BlockItemBase(CURSED_BRICK_STAIRS_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_BRICK_WALL_ITEM = BLOCK_ITEMS.register("cursed_bricks_wall",
            () -> new BlockItemBase(CURSED_BRICK_WALL_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_STONE_CHISELED_ITEM = BLOCK_ITEMS.register("cursed_stone_chiseled",
            () -> new BlockItemBase(CURSED_STONE_CHISELED_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_TILES_ITEM = BLOCK_ITEMS.register("cursed_tiles",
            () -> new BlockItemBase(CURSED_TILES_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_TILES_SLAB_ITEM = BLOCK_ITEMS.register("cursed_tiles_slab",
            () -> new BlockItemBase(CURSED_TILES_SLAB_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_TILES_STAIRS_ITEM = BLOCK_ITEMS.register("cursed_tiles_stairs",
            () -> new BlockItemBase(CURSED_TILES_STAIRS_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_BARS_ITEM = BLOCK_ITEMS.register("cursed_bars",
            () -> new BlockItemBase(CURSED_BARS_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_CAGE_ITEM = BLOCK_ITEMS.register("cursed_cage",
            () -> new BlockItemBase(CURSED_CAGE_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_TOTEM_ITEM = BLOCK_ITEMS.register("cursed_totem",
            () -> new BlockItemBase(CURSED_TOTEM_BLOCK.get()));
    public static final RegistryObject<Item> FANG_TOTEM_ITEM = BLOCK_ITEMS.register("fang_totem",
            () -> new BlockItemBase(FANG_TOTEM.get()));
    public static final RegistryObject<Item> MUTATE_TOTEM_ITEM = BLOCK_ITEMS.register("mutation_totem",
            () -> new BlockItemBase(MUTATE_TOTEM.get()));
    public static final RegistryObject<Item> UNDEAD_TOTEM_ITEM = BLOCK_ITEMS.register("undead_totem",
            () -> new BlockItemBase(UNDEAD_TOTEM.get()));
    public static final RegistryObject<Item> WIND_TOTEM_ITEM = BLOCK_ITEMS.register("wind_totem",
            () -> new BlockItemBase(WIND_TOTEM.get()));
    public static final RegistryObject<Item> SOUL_FANG_TOTEM_ITEM = BLOCK_ITEMS.register("soul_fang_totem",
            () -> new BlockItemBase(SOUL_FANG_TOTEM.get()));
    public static final RegistryObject<Item> OBELISK_ITEM = BLOCK_ITEMS.register("obelisk",
            () -> new BlockItemBase(OBELISK.get()));
    public static final RegistryObject<Item> GUARDIAN_OBELISK_ITEM = BLOCK_ITEMS.register("guardian_obelisk",
            () -> new BlockItemBase(GUARDIAN_OBELISK.get()));
    public static final RegistryObject<Item> CURSED_BURNER_ITEM = BLOCK_ITEMS.register("cursed_burner",
            () -> new BlockItemBase(CURSED_BURNER.get()));
    public static final RegistryObject<Item> CURSED_KILN_ITEM = BLOCK_ITEMS.register("cursed_kiln",
            () -> new BlockItemBase(CURSED_KILN.get()));
    public static final RegistryObject<Item> DARK_ALTAR_ITEM = BLOCK_ITEMS.register("dark_altar",
            () -> new BlockItemBase(DARK_ALTAR.get()));
    public static final RegistryObject<Item> PEDESTAL_ITEM = BLOCK_ITEMS.register("pedestal",
            () -> new BlockItemBase(PEDESTAL.get()));
    public static final RegistryObject<Item> DEAD_SAND_ITEM = BLOCK_ITEMS.register("dead_sand",
            () -> new BlockItemBase(DEAD_SAND.get()));
    public static final RegistryObject<Item> DEAD_SANDSTONE_ITEM = BLOCK_ITEMS.register("dead_sandstone",
            () -> new BlockItemBase(DEAD_SANDSTONE.get()));
    public static final RegistryObject<Item> DEAD_SANDSTONE_CHISELED_ITEM = BLOCK_ITEMS.register("dead_sandstone_chiseled",
            () -> new BlockItemBase(DEAD_SANDSTONE_CHISELED.get()));
    public static final RegistryObject<Item> DEAD_SANDSTONE_CUT_ITEM = BLOCK_ITEMS.register("dead_sandstone_cut",
            () -> new BlockItemBase(DEAD_SANDSTONE_CUT.get()));
    public static final RegistryObject<Item> DEAD_SANDSTONE_SMOOTH_ITEM = BLOCK_ITEMS.register("dead_sandstone_smooth",
            () -> new BlockItemBase(DEAD_SANDSTONE_SMOOTH.get()));
    public static final RegistryObject<Item> DEAD_SANDSTONE_SLAB_ITEM = BLOCK_ITEMS.register("dead_sandstone_slab",
            () -> new BlockItemBase(DEAD_SANDSTONE_SLAB_BLOCK.get()));
    public static final RegistryObject<Item> DEAD_SANDSTONE_CUT_SLAB_ITEM = BLOCK_ITEMS.register("dead_sandstone_cut_slab",
            () -> new BlockItemBase(DEAD_SANDSTONE_CUT_SLAB_BLOCK.get()));
    public static final RegistryObject<Item> DEAD_SANDSTONE_SMOOTH_SLAB_ITEM = BLOCK_ITEMS.register("dead_sandstone_smooth_slab",
            () -> new BlockItemBase(DEAD_SANDSTONE_SMOOTH_SLAB_BLOCK.get()));
    public static final RegistryObject<Item> DEAD_SANDSTONE_STAIRS_ITEM = BLOCK_ITEMS.register("dead_sandstone_stairs",
            () -> new BlockItemBase(DEAD_SANDSTONE_STAIRS_BLOCK.get()));
    public static final RegistryObject<Item> DEAD_SANDSTONE_SMOOTH_STAIRS_ITEM = BLOCK_ITEMS.register("dead_sandstone_smooth_stairs",
            () -> new BlockItemBase(DEAD_SANDSTONE_SMOOTH_STAIRS_BLOCK.get()));
    public static final RegistryObject<Item> DEAD_SANDSTONE_WALL_ITEM = BLOCK_ITEMS.register("dead_sandstone_wall",
            () -> new BlockItemBase(DEAD_SANDSTONE_WALL_BLOCK.get()));
    public static final RegistryObject<Item> DEAD_BLOCK_ITEM = BLOCK_ITEMS.register("dead_block",
            () -> new BlockItemBase(DEAD_BLOCK.get()));
    public static final RegistryObject<Item> HAUNTED_CACTUS_ITEM = BLOCK_ITEMS.register("haunted_cactus",
            () -> new BlockItemBase(HAUNTED_CACTUS.get()));
    public static final RegistryObject<Item> HAUNTED_BUSH_ITEM = BLOCK_ITEMS.register("haunted_bush",
            () -> new BlockItemBase(HAUNTED_BUSH.get()));
    public static final RegistryObject<Item> IRON_FINGER_ITEM = BLOCK_ITEMS.register("iron_finger",
            () -> new BlockItemBase(IRON_FINGER.get()));
    public static final RegistryObject<Item> DARK_CLOUD_ITEM = BLOCK_ITEMS.register("dark_cloud",
            () -> new BlockItemBase(DARK_CLOUD.get()));
    public static final RegistryObject<Item> ARCA_ITEM = BLOCK_ITEMS.register("arca",
            () -> new BlockItemBase(ARCA_BLOCK.get()));
    public static final RegistryObject<Item> SOUL_LIGHT_ITEM = BLOCK_ITEMS.register("soul_light",
            () -> new BlockItemBase(SOUL_LIGHT_BLOCK.get()));
    public static final RegistryObject<Item> GLOW_LIGHT_ITEM = BLOCK_ITEMS.register("glow_light",
            () -> new BlockItemBase(GLOW_LIGHT_BLOCK.get()));
    public static final RegistryObject<Item> GRAVE_BRICK_WALL_ITEM = BLOCK_ITEMS.register("grave_bricks_wall",
            () -> new BlockItem(GRAVE_BRICK_WALL_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> PITHOS_ITEM = BLOCK_ITEMS.register("pithos",
            () -> new BlockItemBase(PITHOS_BLOCK.get()));
    //TreeItems
    public static final RegistryObject<Item> HAUNTED_PLANKS_ITEM = BLOCK_ITEMS.register("haunted_planks",
            () -> new BlockItemBase(HAUNTED_PLANKS.get()));
    public static final RegistryObject<Item> HAUNTED_LOG_ITEM = BLOCK_ITEMS.register("haunted_log",
            () -> new BlockItemBase(HAUNTED_LOG.get()));
    public static final RegistryObject<Item> STRIPPED_HAUNTED_LOG_ITEM = BLOCK_ITEMS.register("stripped_haunted_log",
            () -> new BlockItemBase(STRIPPED_HAUNTED_LOG.get()));
    public static final RegistryObject<Item> HAUNTED_WOOD_ITEM = BLOCK_ITEMS.register("haunted_wood",
            () -> new BlockItemBase(HAUNTED_WOOD.get()));
    public static final RegistryObject<Item> STRIPPED_HAUNTED_WOOD_ITEM = BLOCK_ITEMS.register("stripped_haunted_wood",
            () -> new BlockItemBase(STRIPPED_HAUNTED_WOOD.get()));
    public static final RegistryObject<Item> HAUNTED_PRESSURE_PLATE_ITEM = BLOCK_ITEMS.register("haunted_pressure_plate",
            () -> new BlockItemBase(HAUNTED_PRESSURE_PLATE.get()));
    public static final RegistryObject<Item> HAUNTED_BUTTON_ITEM = BLOCK_ITEMS.register("haunted_button",
            () -> new BlockItemBase(HAUNTED_BUTTON.get()));
    public static final RegistryObject<Item> HAUNTED_DOOR_ITEM = BLOCK_ITEMS.register("haunted_door",
            () -> new TallBlockItem(HAUNTED_DOOR.get(), (new Item.Properties()).tab(Goety.TAB)));
    public static final RegistryObject<Item> HAUNTED_TRAPDOOR_ITEM = BLOCK_ITEMS.register("haunted_trapdoor",
            () -> new BlockItemBase(HAUNTED_TRAPDOOR.get()));
    public static final RegistryObject<Item> HAUNTED_FENCE_ITEM = BLOCK_ITEMS.register("haunted_fence",
            () -> new BlockItemBase(HAUNTED_FENCE.get()));
    public static final RegistryObject<Item> HAUNTED_FENCE_GATE_ITEM = BLOCK_ITEMS.register("haunted_fence_gate",
            () -> new BlockItemBase(HAUNTED_FENCE_GATE.get()));
    public static final RegistryObject<Item> HAUNTED_SLAB_ITEM = BLOCK_ITEMS.register("haunted_slab",
            () -> new BlockItemBase(HAUNTED_SLAB.get()));
    public static final RegistryObject<Item> HAUNTED_STAIRS_ITEM = BLOCK_ITEMS.register("haunted_stairs",
            () -> new BlockItemBase(HAUNTED_STAIRS.get()));
    public static final RegistryObject<Item> DARK_PRESSURE_PLATE_ITEM = BLOCK_ITEMS.register("dark_pressure_plate",
            () -> new BlockItemBase(DARK_PRESSURE_PLATE.get()));
    public static final RegistryObject<Item> HAUNTED_SIGN_ITEM = BLOCK_ITEMS.register("haunted_sign",
            () -> new SignItem((new Item.Properties()).stacksTo(16).tab(Goety.TAB), ModBlocks.HAUNTED_SIGN.get(), ModBlocks.HAUNTED_WALL_SIGN.get()));


    public static Boolean never(BlockState p_235427_0_, IBlockReader p_235427_1_, BlockPos p_235427_2_, EntityType<?> p_235427_3_) {
        return false;
    }

}
