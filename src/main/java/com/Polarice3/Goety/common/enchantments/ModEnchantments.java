package com.Polarice3.Goety.common.enchantments;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.items.curios.RingItem;
import com.Polarice3.Goety.common.items.magic.MagicFocusItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ShootableItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEnchantments {
    public static DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Goety.MOD_ID);

    public static final EnchantmentType RINGS = EnchantmentType.create("rings", (item) -> (item instanceof RingItem));
    public static final EnchantmentType FOCUS = EnchantmentType.create("focus", (item) -> (item instanceof MagicFocusItem));
    public static final EnchantmentType SHOOTABLES = EnchantmentType.create("shootables", (item) -> (item instanceof ShootableItem));

    public static final RegistryObject<Enchantment> SOUL_EATER = ENCHANTMENTS.register("souleater",
            () -> new SoulEaterEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentType.WEAPON, EquipmentSlotType.MAINHAND));

    public static final RegistryObject<Enchantment> SOUL_TAKER = ENCHANTMENTS.register("soultaker",
            () -> new SoulEaterEnchantment(Enchantment.Rarity.UNCOMMON, SHOOTABLES, EquipmentSlotType.MAINHAND));

    public static final RegistryObject<Enchantment> SOUL_FEEDER = ENCHANTMENTS.register("soulfeeder",
            () -> new SoulEaterEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentType.DIGGER, EquipmentSlotType.MAINHAND));

    public static final RegistryObject<Enchantment> WANTING = ENCHANTMENTS.register("wanting",
            () -> new LootingEnchantment(Enchantment.Rarity.RARE, RINGS, EquipmentSlotType.MAINHAND));

    public static final RegistryObject<Enchantment> POTENCY = ENCHANTMENTS.register("potency",
            () -> new PotencyEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));

    public static final RegistryObject<Enchantment> RADIUS = ENCHANTMENTS.register("radius",
            () -> new RadiusEnchantment(Enchantment.Rarity.RARE, EquipmentSlotType.MAINHAND));

    public static final RegistryObject<Enchantment> BURNING = ENCHANTMENTS.register("burning",
            () -> new BurningEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlotType.MAINHAND));

    public static final RegistryObject<Enchantment> RANGE = ENCHANTMENTS.register("range",
            () -> new RangeEnchantment(Enchantment.Rarity.COMMON, EquipmentSlotType.MAINHAND));

    public static final RegistryObject<Enchantment> ABSORB = ENCHANTMENTS.register("absorb",
            () -> new AbsorbEnchantment(Enchantment.Rarity.VERY_RARE, EquipmentSlotType.MAINHAND));

    public static final RegistryObject<Enchantment> DURATION = ENCHANTMENTS.register("duration",
            () -> new DurationEnchantment(Enchantment.Rarity.UNCOMMON, EquipmentSlotType.MAINHAND));

}
