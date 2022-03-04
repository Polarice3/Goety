package com.Polarice3.Goety.common.enchantments;

import com.Polarice3.Goety.Goety;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEnchantmentsType {
    public static DeferredRegister<Enchantment> ENCHANTMENT_TYPES = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Goety.MOD_ID);

    public static final RegistryObject<Enchantment> SOULEATER = ENCHANTMENT_TYPES.register("souleater",
            () -> new SoulEaterEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlotType.MAINHAND));
}
