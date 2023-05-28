package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.tileentities.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntityType {
    public static DeferredRegister<TileEntityType<?>> TILEENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Goety.MOD_ID);

    public static final RegistryObject<TileEntityType<FangTotemTileEntity>> FANG_TOTEM = TILEENTITY_TYPES.register("fang_totem",
            () -> TileEntityType.Builder.of(FangTotemTileEntity::new, ModBlocks.FANG_TOTEM.get()).build(null));

    public static final RegistryObject<TileEntityType<MutateTotemTileEntity>> MUTATE_TOTEM = TILEENTITY_TYPES.register("mutation_totem",
            () -> TileEntityType.Builder.of(MutateTotemTileEntity::new, ModBlocks.MUTATE_TOTEM.get()).build(null));

    public static final RegistryObject<TileEntityType<UndeadTotemTileEntity>> UNDEAD_TOTEM = TILEENTITY_TYPES.register("undead_totem",
            () -> TileEntityType.Builder.of(UndeadTotemTileEntity::new, ModBlocks.UNDEAD_TOTEM.get()).build(null));

    public static final RegistryObject<TileEntityType<WindTotemTileEntity>> WIND_TOTEM = TILEENTITY_TYPES.register("wind_totem",
            () -> TileEntityType.Builder.of(WindTotemTileEntity::new, ModBlocks.WIND_TOTEM.get()).build(null));

    public static final RegistryObject<TileEntityType<ObeliskTileEntity>> OBELISK = TILEENTITY_TYPES.register("obelisk",
            () -> TileEntityType.Builder.of(ObeliskTileEntity::new, ModBlocks.OBELISK.get()).build(null));

    public static final RegistryObject<TileEntityType<GuardianObeliskTileEntity>> GUARDIAN_OBELISK = TILEENTITY_TYPES.register("guardian_obelisk",
            () -> TileEntityType.Builder.of(GuardianObeliskTileEntity::new, ModBlocks.GUARDIAN_OBELISK.get()).build(null));

    public static final RegistryObject<TileEntityType<DryObeliskTileEntity>> DRY_OBELISK = TILEENTITY_TYPES.register("dry_obelisk",
            () -> TileEntityType.Builder.of(DryObeliskTileEntity::new, ModBlocks.DRY_OBELISK.get()).build(null));

    public static final RegistryObject<TileEntityType<CursedBurnerTileEntity>> CURSEDBURNER = TILEENTITY_TYPES.register("cursed_burner",
            () -> TileEntityType.Builder.of(CursedBurnerTileEntity::new, ModBlocks.CURSED_BURNER.get()).build(null));

    public static final RegistryObject<TileEntityType<CursedKilnTileEntity>> CURSED_KILN = TILEENTITY_TYPES.register("cursed_kiln",
            () -> TileEntityType.Builder.of(CursedKilnTileEntity::new, ModBlocks.CURSED_KILN.get()).build(null));

    public static final RegistryObject<TileEntityType<SoulAbsorberTileEntity>> SOUL_ABSORBER = TILEENTITY_TYPES.register("soul_absorber",
            () -> TileEntityType.Builder.of(SoulAbsorberTileEntity::new, ModBlocks.SOUL_ABSORBER.get()).build(null));

    public static final RegistryObject<TileEntityType<CursedCageTileEntity>> CURSED_CAGE = TILEENTITY_TYPES.register("cursed_cage",
            () -> TileEntityType.Builder.of(CursedCageTileEntity::new, ModBlocks.CURSED_CAGE_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<ArcaTileEntity>> ARCA = TILEENTITY_TYPES.register("arca",
            () -> TileEntityType.Builder.of(ArcaTileEntity::new, ModBlocks.ARCA_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<DarkAltarTileEntity>> DARK_ALTAR = TILEENTITY_TYPES.register("dark_altar",
            () -> TileEntityType.Builder.of(DarkAltarTileEntity::new, ModBlocks.DARK_ALTAR.get()).build(null));

    public static final RegistryObject<TileEntityType<PedestalTileEntity>> PEDESTAL = TILEENTITY_TYPES.register("pedestal",
            () -> TileEntityType.Builder.of(PedestalTileEntity::new, ModBlocks.PEDESTAL.get()).build(null));

    public static final RegistryObject<TileEntityType<SoulFangTotemTileEntity>> SOUL_FANG_TOTEM = TILEENTITY_TYPES.register("soul_fang_totem",
            () -> TileEntityType.Builder.of(SoulFangTotemTileEntity::new, ModBlocks.SOUL_FANG_TOTEM.get()).build(null));

    public static final RegistryObject<TileEntityType<SoulLightTileEntity>> SOUL_LIGHT = TILEENTITY_TYPES.register("soul_light",
            () -> TileEntityType.Builder.of(SoulLightTileEntity::new, ModBlocks.SOUL_LIGHT_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<GlowLightTileEntity>> GLOW_LIGHT = TILEENTITY_TYPES.register("glow_light",
            () -> TileEntityType.Builder.of(GlowLightTileEntity::new, ModBlocks.GLOW_LIGHT_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<PithosTileEntity>> PITHOS = TILEENTITY_TYPES.register("pithos",
            () -> TileEntityType.Builder.of(PithosTileEntity::new, ModBlocks.PITHOS_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<ForbiddenGrassTileEntity>> FORBIDDEN_GRASS = TILEENTITY_TYPES.register("forbidden_grass",
            () -> TileEntityType.Builder.of(ForbiddenGrassTileEntity::new, ModBlocks.FORBIDDEN_GRASS.get()).build(null));

    public static final RegistryObject<TileEntityType<GhostFireTrapTileEntity>> GHOST_FIRE_TRAP = TILEENTITY_TYPES.register("ghost_fire_trap",
            () -> TileEntityType.Builder.of(GhostFireTrapTileEntity::new, ModBlocks.GHOST_FIRE_TRAP_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<CultStatueTileEntity>> CULT_STATUE = TILEENTITY_TYPES.register("cult_statue",
            () -> TileEntityType.Builder.of(CultStatueTileEntity::new, ModBlocks.CULT_STATUE.get()).build(null));

    public static final RegistryObject<TileEntityType<ModChestTileEntity>> MOD_CHEST = TILEENTITY_TYPES.register("chest",
            () -> TileEntityType.Builder.of(ModChestTileEntity::new,
                    ModBlocks.HAUNTED_CHEST.get(), ModBlocks.GLOOM_CHEST.get(), ModBlocks.MURK_CHEST.get())
                    .build(null));

    public static final RegistryObject<TileEntityType<ModTrappedChestTileEntity>> MOD_TRAPPED_CHEST = TILEENTITY_TYPES.register("trapped_chest",
            () -> TileEntityType.Builder.of(ModTrappedChestTileEntity::new,
                            ModBlocks.TRAPPED_HAUNTED_CHEST.get(), ModBlocks.TRAPPED_GLOOM_CHEST.get(), ModBlocks.TRAPPED_MURK_CHEST.get())
                    .build(null));

    public static final RegistryObject<TileEntityType<ModSignTileEntity>> SIGN_TILE_ENTITIES = TILEENTITY_TYPES.register("sign",
            () -> TileEntityType.Builder.of(ModSignTileEntity::new,
                    ModBlocks.HAUNTED_SIGN.get(), ModBlocks.HAUNTED_WALL_SIGN.get(),
                    ModBlocks.GLOOM_SIGN.get(), ModBlocks.GLOOM_WALL_SIGN.get(),
                    ModBlocks.MURK_SIGN.get(), ModBlocks.MURK_WALL_SIGN.get()).build(null));

    public static final RegistryObject<TileEntityType<TallSkullTileEntity>> TALL_SKULL = TILEENTITY_TYPES.register("tall_skull",
            () -> TileEntityType.Builder.of(TallSkullTileEntity::new, ModBlocks.TALL_SKULL_BLOCK.get(), ModBlocks.WALL_TALL_SKULL_BLOCK.get()).build(null));
}
