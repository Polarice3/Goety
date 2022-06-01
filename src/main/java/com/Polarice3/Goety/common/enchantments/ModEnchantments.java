package com.Polarice3.Goety.common.enchantments;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.items.curios.RingItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEnchantments {
    public static DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Goety.MOD_ID);

    public static final EnchantmentType RINGS = EnchantmentType.create("rings", (item) -> (item instanceof RingItem));

    public static final RegistryObject<Enchantment> SOULEATER = ENCHANTMENTS.register("souleater",
            () -> new SoulEaterEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlotType.MAINHAND));

    public static final RegistryObject<Enchantment> WANTING = ENCHANTMENTS.register("wanting",
            () -> new LootingEnchantment(Enchantment.Rarity.RARE, RINGS, EquipmentSlotType.MAINHAND));

}
