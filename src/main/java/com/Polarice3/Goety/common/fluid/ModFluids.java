package com.Polarice3.Goety.common.fluid;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.blocks.QuickSandBlock;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModFluids {
    public static final ResourceLocation QUICKSAND_STILL_TEXTURE = Goety.location("blocks/quicksand_still");
    public static final ResourceLocation QUICKSAND_FLOWING_TEXTURE = Goety.location("blocks/quicksand_flow");

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Goety.MOD_ID);

    public static final RegistryObject<FlowingFluid> QUICKSAND_SOURCE
            = FLUIDS.register("quicksand", QuickSandFluid.Source::new);

    public static final RegistryObject<FlowingFluid> QUICKSAND_FLOWING
            = FLUIDS.register("quicksand_flowing", QuickSandFluid.Flowing::new);

    public static final ForgeFlowingFluid.Properties QUICKSAND_PROPERTIES = new ForgeFlowingFluid.Properties(
            () -> QUICKSAND_SOURCE.get(), () -> QUICKSAND_FLOWING.get(), FluidAttributes.builder(QUICKSAND_STILL_TEXTURE, QUICKSAND_FLOWING_TEXTURE)
            .density(3000).viscosity(6000).temperature(300).sound(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA).translationKey("block.goety.quicksand"))
            .slopeFindDistance(4).levelDecreasePerBlock(1).tickRate(30)
            .block(() -> ModFluids.QUICKSAND_BLOCK.get()).bucket(() -> ModItems.QUICKSAND_BUCKET.get());

    public static final RegistryObject<FlowingFluidBlock> QUICKSAND_BLOCK = ModBlocks.BLOCKS.register("quicksand", QuickSandBlock::new);

    public static void init(){
        ModFluids.FLUIDS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
