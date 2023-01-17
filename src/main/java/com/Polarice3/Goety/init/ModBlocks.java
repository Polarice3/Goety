package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.render.tileentities.ModItemTERenderer;
import com.Polarice3.Goety.common.blocks.*;
import com.Polarice3.Goety.common.items.SoulFangTotemItem;
import com.Polarice3.Goety.common.items.TallSkullItem;
import com.Polarice3.Goety.common.world.features.trees.GloomTree;
import com.Polarice3.Goety.common.world.features.trees.MurkTree;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.trees.Tree;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
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
    public static final RegistryObject<Block> CURSED_METAL_BLOCK = BLOCKS.register("cursed_block", CursedMetalBlock::new);
    public static final RegistryObject<Block> CURSED_STONE_BLOCK = BLOCKS.register("cursed_stone", CursedStoneBlock::new);
    public static final RegistryObject<Block> CURSED_STONE_BRICK_BLOCK = BLOCKS.register("cursed_stone_bricks", CursedStoneBlock::new);
    public static final RegistryObject<Block> CURSED_BRICK_BLOCK = BLOCKS.register("cursed_bricks", CursedStoneBlock::new);
    public static final RegistryObject<Block> CURSED_STONE_CHISELED_BLOCK = BLOCKS.register("cursed_stone_chiseled", CursedStoneBlock::new);
    public static final RegistryObject<Block> CURSED_TILES_BLOCK = BLOCKS.register("cursed_tiles", CursedStoneBlock::new);
    public static final RegistryObject<Block> CURSED_GLOW_BRICK_BLOCK = BLOCKS.register("cursed_glow_bricks", CursedGlowBricksBlock::new);
    public static final RegistryObject<Block> CURSED_GLOW_PILLAR_BLOCK = BLOCKS.register("cursed_glow_pillar", CursedGlowPillarBlock::new);
    public static final RegistryObject<Block> CURSED_CAGE_BLOCK = BLOCKS.register("cursed_cage", CursedCageBlock::new);
    public static final RegistryObject<Block> CURSED_TOTEM_BLOCK = BLOCKS.register("cursed_totem", CursedStoneBlock::new);
    public static final RegistryObject<Block> FANG_TOTEM = BLOCKS.register("fang_totem", FangTotemBlock::new);
    public static final RegistryObject<Block> MUTATE_TOTEM = BLOCKS.register("mutation_totem", MutateTotemBlock::new);
    public static final RegistryObject<Block> UNDEAD_TOTEM = BLOCKS.register("undead_totem", UndeadTotemBlock::new);
    public static final RegistryObject<Block> WIND_TOTEM = BLOCKS.register("wind_totem", WindTotemBlock::new);
    public static final RegistryObject<Block> SOUL_FANG_TOTEM = BLOCKS.register("soul_fang_totem", SoulFangTotemBlock::new);
    public static final RegistryObject<Block> OBELISK = BLOCKS.register("obelisk", ObeliskBlock::new);
    public static final RegistryObject<Block> GUARDIAN_OBELISK = BLOCKS.register("guardian_obelisk", GuardianObeliskBlock::new);
    public static final RegistryObject<Block> DRY_OBELISK = BLOCKS.register("dry_obelisk", DryObeliskBlock::new);
    public static final RegistryObject<Block> CURSED_BURNER = BLOCKS.register("cursed_burner", CursedBurnerBlock::new);
    public static final RegistryObject<Block> CURSED_KILN = BLOCKS.register("cursed_kiln", CursedKilnBlock::new);
    public static final RegistryObject<Block> DARK_ALTAR = BLOCKS.register("dark_altar", DarkAltarBlock::new);
    public static final RegistryObject<Block> PEDESTAL = BLOCKS.register("pedestal", PedestalBlock::new);
    public static final RegistryObject<Block> SOUL_ABSORBER = BLOCKS.register("soul_absorber", SoulAbsorberBlock::new);
    public static final RegistryObject<Block> DEAD_SAND = BLOCKS.register("dead_sand", DeadSandBlock::new);
    public static final RegistryObject<Block> DEAD_SANDSTONE = BLOCKS.register("dead_sandstone", DeadSandStoneBlock::new);
    public static final RegistryObject<Block> DEAD_SANDSTONE_CHISELED = BLOCKS.register("dead_sandstone_chiseled", DeadSandStoneBlock::new);
    public static final RegistryObject<Block> DEAD_SANDSTONE_CUT = BLOCKS.register("dead_sandstone_cut", DeadSandStoneBlock::new);
    public static final RegistryObject<Block> DEAD_SANDSTONE_SMOOTH = BLOCKS.register("dead_sandstone_smooth", DeadSandStoneBlock::new);
    public static final RegistryObject<Block> DEAD_PILE = BLOCKS.register("dead_pile", DeadPileBlock::new);
    public static final RegistryObject<Block> DEAD_BLOCK = BLOCKS.register("dead_block", DeadBlock::new);
    public static final RegistryObject<Block> TEMP_WEB = BLOCKS.register("temp_web", TempWebBlock::new);
    public static final RegistryObject<Block> DARK_CLOUD = BLOCKS.register("dark_cloud", DarkCloudBlock::new);
    public static final RegistryObject<Block> ARCA_BLOCK = BLOCKS.register("arca", ArcaBlock::new);
    public static final RegistryObject<Block> SOUL_LIGHT_BLOCK = BLOCKS.register("soul_light", SoulLightBlock::new);
    public static final RegistryObject<Block> GLOW_LIGHT_BLOCK = BLOCKS.register("glow_light", GlowLightBlock::new);
    public static final RegistryObject<Block> PITHOS_BLOCK = BLOCKS.register("pithos", PithosBlock::new);
    public static final RegistryObject<Block> TALL_SKULL_BLOCK = BLOCKS.register("tall_skull", TallSkullBlock::new);
    public static final RegistryObject<Block> WALL_TALL_SKULL_BLOCK = BLOCKS.register("wall_tall_skull", WallTallSkullBlock::new);
    public static final RegistryObject<Block> FALSE_PORTAL = BLOCKS.register("false_portal", FalsePortalBlock::new);
    public static final RegistryObject<Block> DEAD_TNT = BLOCKS.register("dead_tnt", DeadTNTBlock::new);
    public static final RegistryObject<Block> FROST_BLOCK = BLOCKS.register("frost_block", CursedMetalBlock::new);
    public static final RegistryObject<Block> FORBIDDEN_GRASS = BLOCKS.register("forbidden_grass", ForbiddenGrassBlock::new);
    public static final RegistryObject<Block> GRAND_TORCH = BLOCKS.register("grand_torch", () -> new BigTorchBlock(ParticleTypes.FLAME));
    public static final RegistryObject<Block> WALL_GRAND_TORCH = BLOCKS.register("wall_grand_torch", () -> new WallBigTorchBlock(ParticleTypes.FLAME));

    //Plants
    public static final RegistryObject<Block> HAUNTED_CACTUS = BLOCKS.register("haunted_cactus", HauntedCactusBlock::new);
    public static final RegistryObject<Block> HAUNTED_BUSH = BLOCKS.register("haunted_bush", HauntedBushBlock::new);
    public static final RegistryObject<Block> IRON_FINGER = BLOCKS.register("iron_finger", IronFingerBlock::new);
    public static final RegistryObject<Block> ROTTEN_PUMPKIN = BLOCKS.register("rotten_pumpkin", RottenPumpkinBlock::new);

    //Remnant
    public static final RegistryObject<Block> REMNANT_BLOCK = BLOCKS.register("remnant_block",
            () -> new Block(AbstractBlock.Properties.of(Material.SAND, MaterialColor.TERRACOTTA_PURPLE).strength(0.6F).sound(SoundType.GRAVEL)));
    public static final RegistryObject<Block> REMNANT_ROCK = BLOCKS.register("remnant_rock",
            () -> new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_PURPLE).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));
    public static final RegistryObject<Block> REMNANT_STONE = BLOCKS.register("remnant_stone",
            () -> new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_PURPLE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final RegistryObject<Block> REMNANT_SMOOTH_STONE = BLOCKS.register("remnant_smooth_stone",
            () -> new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_PURPLE).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final RegistryObject<Block> REMNANT_BRICKS = BLOCKS.register("remnant_bricks",
            () -> new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_PURPLE).requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.NETHER_BRICKS)));
    public static final RegistryObject<Block> REMNANT_CHISELED_BRICKS = BLOCKS.register("remnant_chiseled_bricks",
            () -> new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_PURPLE).requiresCorrectToolForDrops().strength(2.0F, 6.0F).sound(SoundType.NETHER_BRICKS)));

    //Haunted
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

    //Gloom
    public static final RegistryObject<Block> GLOOM_PLANKS = BLOCKS.register("gloom_planks",
            () -> new Block(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_GRAY).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> GLOOM_LOG = BLOCKS.register("gloom_log", () -> log(MaterialColor.COLOR_BROWN, MaterialColor.COLOR_LIGHT_GRAY));
    public static final RegistryObject<Block> STRIPPED_GLOOM_LOG = BLOCKS.register("stripped_gloom_log", () -> log(MaterialColor.COLOR_BROWN, MaterialColor.COLOR_LIGHT_GRAY));
    public static final RegistryObject<Block> GLOOM_WOOD = BLOCKS.register("gloom_wood",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_GRAY).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> STRIPPED_GLOOM_WOOD = BLOCKS.register("stripped_gloom_wood",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_GRAY).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> GLOOM_PRESSURE_PLATE = BLOCKS.register("gloom_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, AbstractBlock.Properties.of(Material.WOOD, GLOOM_PLANKS.get().defaultMaterialColor()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> GLOOM_TRAPDOOR = BLOCKS.register("gloom_trapdoor",
            () -> new TrapDoorBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_GRAY).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Block> GLOOM_BUTTON = BLOCKS.register("gloom_button",
            () -> new WoodButtonBlock(AbstractBlock.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> GLOOM_STAIRS = BLOCKS.register("gloom_stairs",
            () -> new StairsBlock(GLOOM_PLANKS.get().defaultBlockState(), AbstractBlock.Properties.copy(GLOOM_PLANKS.get())));
    public static final RegistryObject<Block> GLOOM_SLAB = BLOCKS.register("gloom_slab",
            () -> new SlabBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_LIGHT_GRAY).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> GLOOM_FENCE_GATE = BLOCKS.register("gloom_fence_gate",
            () -> new FenceGateBlock(AbstractBlock.Properties.of(Material.WOOD, GLOOM_PLANKS.get().defaultMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> GLOOM_FENCE = BLOCKS.register("gloom_fence",
            () -> new FenceBlock(AbstractBlock.Properties.of(Material.WOOD, GLOOM_PLANKS.get().defaultMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> GLOOM_DOOR = BLOCKS.register("gloom_door",
            () -> new DoorBlock(AbstractBlock.Properties.of(Material.WOOD, GLOOM_PLANKS.get().defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Block> GLOOM_SIGN = BLOCKS.register("gloom_sign",
            () -> new ModStandSignBlock(AbstractBlock.Properties.of(Material.WOOD, GLOOM_PLANKS.get().defaultMaterialColor()).noCollission().strength(1.0F).sound(SoundType.WOOD), ModWoodType.GLOOM));
    public static final RegistryObject<Block> GLOOM_WALL_SIGN = BLOCKS.register("gloom_wall_sign",
            () -> new ModWallSignBlock(AbstractBlock.Properties.of(Material.WOOD, GLOOM_PLANKS.get().defaultMaterialColor()).noCollission().strength(1.0F).sound(SoundType.WOOD).dropsLike(GLOOM_SIGN.get()), ModWoodType.GLOOM));
    public static final RegistryObject<Block> GLOOM_LEAVES = BLOCKS.register("gloom_leaves", ModBlocks::leaves);
    public static final RegistryObject<Block> GLOOM_SAPLING = BLOCKS.register("gloom_sapling", () -> sapling(new GloomTree()));

    //Murk
    public static final RegistryObject<Block> MURK_PLANKS = BLOCKS.register("murk_planks",
            () -> new Block(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> MURK_LOG = BLOCKS.register("murk_log", () -> log(MaterialColor.COLOR_BROWN, MaterialColor.COLOR_BROWN));
    public static final RegistryObject<Block> STRIPPED_MURK_LOG = BLOCKS.register("stripped_murk_log", () -> log(MaterialColor.COLOR_BROWN, MaterialColor.COLOR_BROWN));
    public static final RegistryObject<Block> MURK_WOOD = BLOCKS.register("murk_wood",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> STRIPPED_MURK_WOOD = BLOCKS.register("stripped_murk_wood",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> MURK_PRESSURE_PLATE = BLOCKS.register("murk_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, AbstractBlock.Properties.of(Material.WOOD, MURK_PLANKS.get().defaultMaterialColor()).noCollission().strength(0.5F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> MURK_TRAPDOOR = BLOCKS.register("murk_trapdoor",
            () -> new TrapDoorBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Block> MURK_BUTTON = BLOCKS.register("murk_button",
            () -> new WoodButtonBlock(AbstractBlock.Properties.of(Material.DECORATION).noCollission().strength(0.5F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> MURK_STAIRS = BLOCKS.register("murk_stairs",
            () -> new StairsBlock(MURK_PLANKS.get().defaultBlockState(), AbstractBlock.Properties.copy(MURK_PLANKS.get())));
    public static final RegistryObject<Block> MURK_SLAB = BLOCKS.register("murk_slab",
            () -> new SlabBlock(AbstractBlock.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> MURK_FENCE_GATE = BLOCKS.register("murk_fence_gate",
            () -> new FenceGateBlock(AbstractBlock.Properties.of(Material.WOOD, MURK_PLANKS.get().defaultMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> MURK_FENCE = BLOCKS.register("murk_fence",
            () -> new FenceBlock(AbstractBlock.Properties.of(Material.WOOD, MURK_PLANKS.get().defaultMaterialColor()).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> MURK_DOOR = BLOCKS.register("murk_door",
            () -> new DoorBlock(AbstractBlock.Properties.of(Material.WOOD, MURK_PLANKS.get().defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
    public static final RegistryObject<Block> MURK_SIGN = BLOCKS.register("murk_sign",
            () -> new ModStandSignBlock(AbstractBlock.Properties.of(Material.WOOD, MURK_PLANKS.get().defaultMaterialColor()).noCollission().strength(1.0F).sound(SoundType.WOOD), ModWoodType.MURK));
    public static final RegistryObject<Block> MURK_WALL_SIGN = BLOCKS.register("murk_wall_sign",
            () -> new ModWallSignBlock(AbstractBlock.Properties.of(Material.WOOD, MURK_PLANKS.get().defaultMaterialColor()).noCollission().strength(1.0F).sound(SoundType.WOOD).dropsLike(MURK_SIGN.get()), ModWoodType.MURK));
    public static final RegistryObject<Block> MURK_LEAVES = BLOCKS.register("murk_leaves", ModBlocks::leaves);
    public static final RegistryObject<Block> MURK_SAPLING = BLOCKS.register("murk_sapling", () -> sapling(new MurkTree()));

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
            () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_PURPLE)
                    .strength(2.0F, 6.0F)
                    .harvestLevel(0)
                    .harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> DEAD_SANDSTONE_CUT_SLAB_BLOCK = BLOCKS.register("dead_sandstone_cut_slab",
            () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_PURPLE)
                    .strength(2.0F, 6.0F)
                    .harvestLevel(0)
                    .harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> DEAD_SANDSTONE_SMOOTH_SLAB_BLOCK = BLOCKS.register("dead_sandstone_smooth_slab",
            () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_PURPLE)
                    .strength(2.0F, 6.0F)
                    .harvestLevel(0)
                    .harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> REMNANT_ROCK_SLAB_BLOCK = BLOCKS.register("remnant_rock_slab",
            () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_PURPLE)
                    .strength(2.0F, 6.0F)
                    .harvestLevel(0)
                    .harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> REMNANT_STONE_SLAB_BLOCK = BLOCKS.register("remnant_stone_slab",
            () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_PURPLE)
                    .strength(2.0F, 6.0F)
                    .harvestLevel(0)
                    .harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> REMNANT_SMOOTH_STONE_SLAB_BLOCK = BLOCKS.register("remnant_smooth_stone_slab",
            () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_PURPLE)
                    .strength(2.0F, 6.0F)
                    .harvestLevel(0)
                    .harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> REMNANT_BRICKS_SLAB_BLOCK = BLOCKS.register("remnant_bricks_slab",
            () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_PURPLE)
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
    public static final RegistryObject<Block> REMNANT_ROCK_STAIRS_BLOCK = BLOCKS.register("remnant_rock_stairs",
            () -> new StairsBlock(REMNANT_ROCK.get().defaultBlockState(), AbstractBlock.Properties.copy(REMNANT_ROCK.get())));
    public static final RegistryObject<Block> REMNANT_STONE_STAIRS_BLOCK = BLOCKS.register("remnant_stone_stairs",
            () -> new StairsBlock(REMNANT_STONE.get().defaultBlockState(), AbstractBlock.Properties.copy(REMNANT_STONE.get())));
    public static final RegistryObject<Block> REMNANT_BRICKS_STAIRS_BLOCK = BLOCKS.register("remnant_bricks_stairs",
            () -> new StairsBlock(REMNANT_BRICKS.get().defaultBlockState(), AbstractBlock.Properties.copy(REMNANT_BRICKS.get())));
    //Walls
    public static final RegistryObject<Block> CURSED_BRICK_WALL_BLOCK = BLOCKS.register("cursed_bricks_wall",
            () -> new WallBlock(AbstractBlock.Properties.copy(CURSED_BRICK_BLOCK.get())));
    public static final RegistryObject<Block> GRAVE_BRICK_WALL_BLOCK = BLOCKS.register("grave_bricks_wall", GraveWallBlock::new);
    public static final RegistryObject<Block> CURSED_STONE_BRICK_WALL_BLOCK = BLOCKS.register("cursed_stone_bricks_wall",
            () -> new WallBlock(AbstractBlock.Properties.copy(CURSED_STONE_BRICK_BLOCK.get())));
    public static final RegistryObject<Block> DEAD_SANDSTONE_WALL_BLOCK = BLOCKS.register("dead_sandstone_wall",
            () -> new WallBlock(AbstractBlock.Properties.copy(DEAD_SANDSTONE.get())));
    public static final RegistryObject<Block> REMNANT_ROCK_WALL_BLOCK = BLOCKS.register("remnant_rock_wall",
            () -> new WallBlock(AbstractBlock.Properties.copy(REMNANT_ROCK.get())));
    public static final RegistryObject<Block> REMNANT_BRICKS_WALL_BLOCK = BLOCKS.register("remnant_bricks_wall",
            () -> new WallBlock(AbstractBlock.Properties.copy(REMNANT_BRICKS.get())));
    //Panes
    public static final RegistryObject<Block> CURSED_BARS_BLOCK = BLOCKS.register("cursed_bars",
            () -> new PaneBlock(AbstractBlock.Properties.of(Material.METAL, MaterialColor.NONE)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()));
    //Block Items
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
    public static final RegistryObject<Item> CURSED_GLOW_BRICK_ITEM = BLOCK_ITEMS.register("cursed_glow_bricks",
            () -> new BlockItemBase(CURSED_GLOW_BRICK_BLOCK.get()));
    public static final RegistryObject<Item> CURSED_GLOW_PILLAR_ITEM = BLOCK_ITEMS.register("cursed_glow_pillar",
            () -> new BlockItemBase(CURSED_GLOW_PILLAR_BLOCK.get()));
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
            () -> new SoulFangTotemItem(SOUL_FANG_TOTEM.get()));
    public static final RegistryObject<Item> OBELISK_ITEM = BLOCK_ITEMS.register("obelisk",
            () -> new BlockItemBase(OBELISK.get()));
    public static final RegistryObject<Item> GUARDIAN_OBELISK_ITEM = BLOCK_ITEMS.register("guardian_obelisk",
            () -> new BlockItemBase(GUARDIAN_OBELISK.get()));
    public static final RegistryObject<Item> DRY_OBELISK_ITEM = BLOCK_ITEMS.register("dry_obelisk",
            () -> new BlockItemBase(DRY_OBELISK.get()));
    public static final RegistryObject<Item> CURSED_BURNER_ITEM = BLOCK_ITEMS.register("cursed_burner",
            () -> new BlockItemBase(CURSED_BURNER.get()));
    public static final RegistryObject<Item> CURSED_KILN_ITEM = BLOCK_ITEMS.register("cursed_kiln",
            () -> new BlockItemBase(CURSED_KILN.get()));
    public static final RegistryObject<Item> SOUL_ABSORBER_ITEM = BLOCK_ITEMS.register("soul_absorber",
            () -> new BlockItemBase(SOUL_ABSORBER.get()));
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
    public static final RegistryObject<Item> DEAD_PILE_ITEM = BLOCK_ITEMS.register("dead_pile",
            () -> new BlockItemBase(DEAD_PILE.get()));
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
    public static final RegistryObject<Item> DEAD_TNT_ITEM = BLOCK_ITEMS.register("dead_tnt",
            () -> new BlockItemBase(DEAD_TNT.get()));
    public static final RegistryObject<Item> TALL_SKULL_ITEM = BLOCK_ITEMS.register("tall_skull",
            () -> new TallSkullItem(ModBlocks.TALL_SKULL_BLOCK.get(), ModBlocks.WALL_TALL_SKULL_BLOCK.get(), (new Item.Properties()).tab(Goety.TAB).rarity(Rarity.UNCOMMON).setISTER(() -> ModItemTERenderer::new)));
    public static final RegistryObject<Item> FROST_BLOCK_ITEM = BLOCK_ITEMS.register("frost_block",
            () -> new BlockItemBase(FROST_BLOCK.get()));
    public static final RegistryObject<Item> FORBIDDEN_GRASS_BLOCK_ITEM = BLOCK_ITEMS.register("forbidden_grass",
            () -> new BlockItemBase(FORBIDDEN_GRASS.get()));
    public static final RegistryObject<Item> ROTTEN_PUMPKIN_BLOCK_ITEM = BLOCK_ITEMS.register("rotten_pumpkin",
            () -> new BlockItemBase(ROTTEN_PUMPKIN.get()));
    public static final RegistryObject<Item> GRAND_TORCH_ITEM = BLOCK_ITEMS.register("grand_torch",
            () -> new WallOrFloorItem(ModBlocks.GRAND_TORCH.get(), WALL_GRAND_TORCH.get(), (new Item.Properties()).tab(Goety.TAB)));

    //RemnantItems
    public static final RegistryObject<Item> REMNANT_BLOCK_ITEM = BLOCK_ITEMS.register("remnant_block",
            () -> new BlockItemBase(REMNANT_BLOCK.get()));
    public static final RegistryObject<Item> REMNANT_ROCK_ITEM = BLOCK_ITEMS.register("remnant_rock",
            () -> new BlockItemBase(REMNANT_ROCK.get()));
    public static final RegistryObject<Item> REMNANT_ROCK_SLAB_ITEM = BLOCK_ITEMS.register("remnant_rock_slab",
            () -> new BlockItemBase(REMNANT_ROCK_SLAB_BLOCK.get()));
    public static final RegistryObject<Item> REMNANT_ROCK_STAIRS_ITEM = BLOCK_ITEMS.register("remnant_rock_stairs",
            () -> new BlockItemBase(REMNANT_ROCK_STAIRS_BLOCK.get()));
    public static final RegistryObject<Item> REMNANT_ROCK_WALL_ITEM = BLOCK_ITEMS.register("remnant_rock_wall",
            () -> new BlockItemBase(REMNANT_ROCK_WALL_BLOCK.get()));
    public static final RegistryObject<Item> REMNANT_STONE_ITEM = BLOCK_ITEMS.register("remnant_stone",
            () -> new BlockItemBase(REMNANT_STONE.get()));
    public static final RegistryObject<Item> REMNANT_STONE_SLAB_ITEM = BLOCK_ITEMS.register("remnant_stone_slab",
            () -> new BlockItemBase(REMNANT_STONE_SLAB_BLOCK.get()));
    public static final RegistryObject<Item> REMNANT_STONE_STAIRS_ITEM = BLOCK_ITEMS.register("remnant_stone_stairs",
            () -> new BlockItemBase(REMNANT_STONE_STAIRS_BLOCK.get()));
    public static final RegistryObject<Item> REMNANT_SMOOTH_STONE_ITEM = BLOCK_ITEMS.register("remnant_smooth_stone",
            () -> new BlockItemBase(REMNANT_SMOOTH_STONE.get()));
    public static final RegistryObject<Item> REMNANT_SMOOTH_STONE_SLAB_ITEM = BLOCK_ITEMS.register("remnant_smooth_stone_slab",
            () -> new BlockItemBase(REMNANT_SMOOTH_STONE_SLAB_BLOCK.get()));
    public static final RegistryObject<Item> REMNANT_BRICKS_ITEM = BLOCK_ITEMS.register("remnant_bricks",
            () -> new BlockItemBase(REMNANT_BRICKS.get()));
    public static final RegistryObject<Item> REMNANT_BRICKS_SLAB_ITEM = BLOCK_ITEMS.register("remnant_bricks_slab",
            () -> new BlockItemBase(REMNANT_BRICKS_SLAB_BLOCK.get()));
    public static final RegistryObject<Item> REMNANT_BRICKS_STAIRS_ITEM = BLOCK_ITEMS.register("remnant_bricks_stairs",
            () -> new BlockItemBase(REMNANT_BRICKS_STAIRS_BLOCK.get()));
    public static final RegistryObject<Item> REMNANT_BRICKS_WALL_ITEM = BLOCK_ITEMS.register("remnant_bricks_wall",
            () -> new BlockItemBase(REMNANT_BRICKS_WALL_BLOCK.get()));
    public static final RegistryObject<Item> REMNANT_CHISELED_BRICKS_ITEM = BLOCK_ITEMS.register("remnant_chiseled_bricks",
            () -> new BlockItemBase(REMNANT_CHISELED_BRICKS.get()));

    //HauntedItems
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

    //GloomItems
    public static final RegistryObject<Item> GLOOM_PLANKS_ITEM = BLOCK_ITEMS.register("gloom_planks",
            () -> new BlockItemBase(GLOOM_PLANKS.get()));
    public static final RegistryObject<Item> GLOOM_LOG_ITEM = BLOCK_ITEMS.register("gloom_log",
            () -> new BlockItemBase(GLOOM_LOG.get()));
    public static final RegistryObject<Item> STRIPPED_GLOOM_LOG_ITEM = BLOCK_ITEMS.register("stripped_gloom_log",
            () -> new BlockItemBase(STRIPPED_GLOOM_LOG.get()));
    public static final RegistryObject<Item> GLOOM_WOOD_ITEM = BLOCK_ITEMS.register("gloom_wood",
            () -> new BlockItemBase(GLOOM_WOOD.get()));
    public static final RegistryObject<Item> STRIPPED_GLOOM_WOOD_ITEM = BLOCK_ITEMS.register("stripped_gloom_wood",
            () -> new BlockItemBase(STRIPPED_GLOOM_WOOD.get()));
    public static final RegistryObject<Item> GLOOM_PRESSURE_PLATE_ITEM = BLOCK_ITEMS.register("gloom_pressure_plate",
            () -> new BlockItemBase(GLOOM_PRESSURE_PLATE.get()));
    public static final RegistryObject<Item> GLOOM_BUTTON_ITEM = BLOCK_ITEMS.register("gloom_button",
            () -> new BlockItemBase(GLOOM_BUTTON.get()));
    public static final RegistryObject<Item> GLOOM_DOOR_ITEM = BLOCK_ITEMS.register("gloom_door",
            () -> new TallBlockItem(GLOOM_DOOR.get(), (new Item.Properties()).tab(Goety.TAB)));
    public static final RegistryObject<Item> GLOOM_TRAPDOOR_ITEM = BLOCK_ITEMS.register("gloom_trapdoor",
            () -> new BlockItemBase(GLOOM_TRAPDOOR.get()));
    public static final RegistryObject<Item> GLOOM_FENCE_ITEM = BLOCK_ITEMS.register("gloom_fence",
            () -> new BlockItemBase(GLOOM_FENCE.get()));
    public static final RegistryObject<Item> GLOOM_FENCE_GATE_ITEM = BLOCK_ITEMS.register("gloom_fence_gate",
            () -> new BlockItemBase(GLOOM_FENCE_GATE.get()));
    public static final RegistryObject<Item> GLOOM_SLAB_ITEM = BLOCK_ITEMS.register("gloom_slab",
            () -> new BlockItemBase(GLOOM_SLAB.get()));
    public static final RegistryObject<Item> GLOOM_STAIRS_ITEM = BLOCK_ITEMS.register("gloom_stairs",
            () -> new BlockItemBase(GLOOM_STAIRS.get()));
    public static final RegistryObject<Item> GLOOM_LEAVES_ITEM = BLOCK_ITEMS.register("gloom_leaves",
            () -> new BlockItemBase(GLOOM_LEAVES.get()));
    public static final RegistryObject<Item> GLOOM_SIGN_ITEM = BLOCK_ITEMS.register("gloom_sign",
            () -> new SignItem((new Item.Properties()).stacksTo(16).tab(Goety.TAB), ModBlocks.GLOOM_SIGN.get(), ModBlocks.GLOOM_WALL_SIGN.get()));
    public static final RegistryObject<Item> GLOOM_SAPLING_ITEM = BLOCK_ITEMS.register("gloom_sapling",
            () -> new BlockItemBase(GLOOM_SAPLING.get()));

    //MurkItems
    public static final RegistryObject<Item> MURK_PLANKS_ITEM = BLOCK_ITEMS.register("murk_planks",
            () -> new BlockItemBase(MURK_PLANKS.get()));
    public static final RegistryObject<Item> MURK_LOG_ITEM = BLOCK_ITEMS.register("murk_log",
            () -> new BlockItemBase(MURK_LOG.get()));
    public static final RegistryObject<Item> STRIPPED_MURK_LOG_ITEM = BLOCK_ITEMS.register("stripped_murk_log",
            () -> new BlockItemBase(STRIPPED_MURK_LOG.get()));
    public static final RegistryObject<Item> MURK_WOOD_ITEM = BLOCK_ITEMS.register("murk_wood",
            () -> new BlockItemBase(MURK_WOOD.get()));
    public static final RegistryObject<Item> STRIPPED_MURK_WOOD_ITEM = BLOCK_ITEMS.register("stripped_murk_wood",
            () -> new BlockItemBase(STRIPPED_MURK_WOOD.get()));
    public static final RegistryObject<Item> MURK_PRESSURE_PLATE_ITEM = BLOCK_ITEMS.register("murk_pressure_plate",
            () -> new BlockItemBase(MURK_PRESSURE_PLATE.get()));
    public static final RegistryObject<Item> MURK_BUTTON_ITEM = BLOCK_ITEMS.register("murk_button",
            () -> new BlockItemBase(MURK_BUTTON.get()));
    public static final RegistryObject<Item> MURK_DOOR_ITEM = BLOCK_ITEMS.register("murk_door",
            () -> new TallBlockItem(MURK_DOOR.get(), (new Item.Properties()).tab(Goety.TAB)));
    public static final RegistryObject<Item> MURK_TRAPDOOR_ITEM = BLOCK_ITEMS.register("murk_trapdoor",
            () -> new BlockItemBase(MURK_TRAPDOOR.get()));
    public static final RegistryObject<Item> MURK_FENCE_ITEM = BLOCK_ITEMS.register("murk_fence",
            () -> new BlockItemBase(MURK_FENCE.get()));
    public static final RegistryObject<Item> MURK_FENCE_GATE_ITEM = BLOCK_ITEMS.register("murk_fence_gate",
            () -> new BlockItemBase(MURK_FENCE_GATE.get()));
    public static final RegistryObject<Item> MURK_SLAB_ITEM = BLOCK_ITEMS.register("murk_slab",
            () -> new BlockItemBase(MURK_SLAB.get()));
    public static final RegistryObject<Item> MURK_STAIRS_ITEM = BLOCK_ITEMS.register("murk_stairs",
            () -> new BlockItemBase(MURK_STAIRS.get()));
    public static final RegistryObject<Item> MURK_LEAVES_ITEM = BLOCK_ITEMS.register("murk_leaves",
            () -> new BlockItemBase(MURK_LEAVES.get()));
    public static final RegistryObject<Item> MURK_SIGN_ITEM = BLOCK_ITEMS.register("murk_sign",
            () -> new SignItem((new Item.Properties()).stacksTo(16).tab(Goety.TAB), ModBlocks.MURK_SIGN.get(), ModBlocks.MURK_WALL_SIGN.get()));
    public static final RegistryObject<Item> MURK_SAPLING_ITEM = BLOCK_ITEMS.register("murk_sapling",
            () -> new BlockItemBase(MURK_SAPLING.get()));

    private static LeavesBlock leaves() {
        return new LeavesBlock(AbstractBlock.Properties.of(Material.CACTUS).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(ModBlocks::ocelotOrParrot).isSuffocating(ModBlocks::never).isViewBlocking(ModBlocks::never));
    }

    private static SaplingBlock sapling(Tree tree){
        return new SaplingBlock(tree, AbstractBlock.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS));
    }

    private static boolean ocelotOrParrot(BlockState blockState, IBlockReader iBlockReader, BlockPos blockPos, EntityType<?> entityType) {
        return entityType == EntityType.OCELOT || entityType == EntityType.PARROT;
    }

    private static RotatedPillarBlock log(MaterialColor pTopColor, MaterialColor pBarkColor) {
        return new RotatedPillarBlock(AbstractBlock.Properties.of(Material.WOOD, (p_235431_2_) -> {
            return p_235431_2_.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? pTopColor : pBarkColor;
        }).strength(2.0F).sound(SoundType.WOOD));
    }

    private static boolean never(BlockState blockState, IBlockReader iBlockReader, BlockPos blockPos) {
        return false;
    }

    public static Boolean never(BlockState p_235427_0_, IBlockReader p_235427_1_, BlockPos p_235427_2_, EntityType<?> p_235427_3_) {
        return false;
    }

}
