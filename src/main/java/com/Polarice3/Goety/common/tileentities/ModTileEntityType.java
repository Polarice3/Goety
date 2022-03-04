package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.init.ModRegistryHandler;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntityType {
    public static DeferredRegister<TileEntityType<?>> TILEENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Goety.MOD_ID);

    public static final RegistryObject<TileEntityType<FangTotemTileEntity>> FANG_TOTEM = TILEENTITY_TYPES.register("fang_totem",
            () -> TileEntityType.Builder.of(FangTotemTileEntity::new, ModRegistryHandler.FANG_TOTEM.get()).build(null));

    public static final RegistryObject<TileEntityType<MutateTotemTileEntity>> MUTATE_TOTEM = TILEENTITY_TYPES.register("mutation_totem",
            () -> TileEntityType.Builder.of(MutateTotemTileEntity::new, ModRegistryHandler.MUTATE_TOTEM.get()).build(null));

    public static final RegistryObject<TileEntityType<UndeadTotemTileEntity>> UNDEAD_TOTEM = TILEENTITY_TYPES.register("undead_totem",
            () -> TileEntityType.Builder.of(UndeadTotemTileEntity::new, ModRegistryHandler.UNDEAD_TOTEM.get()).build(null));

    public static final RegistryObject<TileEntityType<WindTotemTileEntity>> WIND_TOTEM = TILEENTITY_TYPES.register("wind_totem",
            () -> TileEntityType.Builder.of(WindTotemTileEntity::new, ModRegistryHandler.WIND_TOTEM.get()).build(null));

/*    public static final RegistryObject<TileEntityType<SoulForgeTileEntity>> SOULFORGE = TILEENTITY_TYPES.register("soulforge",
            () -> TileEntityType.Builder.create(SoulForgeTileEntity::new, RegistryHandler.SOULFORGE.get()).build(null));*/
}
