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
            () -> TileEntityType.Builder.of(FangTotemTileEntity::new, ModRegistry.FANG_TOTEM.get()).build(null));

    public static final RegistryObject<TileEntityType<MutateTotemTileEntity>> MUTATE_TOTEM = TILEENTITY_TYPES.register("mutation_totem",
            () -> TileEntityType.Builder.of(MutateTotemTileEntity::new, ModRegistry.MUTATE_TOTEM.get()).build(null));

    public static final RegistryObject<TileEntityType<UndeadTotemTileEntity>> UNDEAD_TOTEM = TILEENTITY_TYPES.register("undead_totem",
            () -> TileEntityType.Builder.of(UndeadTotemTileEntity::new, ModRegistry.UNDEAD_TOTEM.get()).build(null));

    public static final RegistryObject<TileEntityType<WindTotemTileEntity>> WIND_TOTEM = TILEENTITY_TYPES.register("wind_totem",
            () -> TileEntityType.Builder.of(WindTotemTileEntity::new, ModRegistry.WIND_TOTEM.get()).build(null));

    public static final RegistryObject<TileEntityType<ObeliskTileEntity>> OBELISK = TILEENTITY_TYPES.register("obelisk",
            () -> TileEntityType.Builder.of(ObeliskTileEntity::new, ModRegistry.OBELISK.get()).build(null));

    public static final RegistryObject<TileEntityType<GuardianObeliskTileEntity>> GUARDIAN_OBELISK = TILEENTITY_TYPES.register("guardian_obelisk",
            () -> TileEntityType.Builder.of(GuardianObeliskTileEntity::new, ModRegistry.GUARDIAN_OBELISK.get()).build(null));

    public static final RegistryObject<TileEntityType<CursedBurnerTileEntity>> CURSEDBURNER = TILEENTITY_TYPES.register("cursed_burner",
            () -> TileEntityType.Builder.of(CursedBurnerTileEntity::new, ModRegistry.CURSED_BURNER.get()).build(null));

    public static final RegistryObject<TileEntityType<CursedCageTileEntity>> CURSED_CAGE = TILEENTITY_TYPES.register("cursed_cage",
            () -> TileEntityType.Builder.of(CursedCageTileEntity::new, ModRegistry.CURSED_CAGE_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<DarkAltarTileEntity>> DARK_ALTAR = TILEENTITY_TYPES.register("dark_altar",
            () -> TileEntityType.Builder.of(DarkAltarTileEntity::new, ModRegistry.DARK_ALTAR.get()).build(null));

    public static final RegistryObject<TileEntityType<PedestalTileEntity>> PEDESTAL = TILEENTITY_TYPES.register("pedestal",
            () -> TileEntityType.Builder.of(PedestalTileEntity::new, ModRegistry.PEDESTAL.get()).build(null));

}
