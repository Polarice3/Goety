package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.Goety;
import com.google.common.collect.Sets;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.Set;

public class ModLootTables {
    private static final Set<ResourceLocation> LOCATIONS = Sets.newHashSet();
    private static final Set<ResourceLocation> IMMUTABLE_LOCATIONS = Collections.unmodifiableSet(LOCATIONS);
    public static final ResourceLocation EMPTY = new ResourceLocation("empty");
    public static final ResourceLocation SALVAGED_FORT_TREASURE = register("chests/salvaged_fort_treasure");
    public static final ResourceLocation SALVAGED_FORT_ARCHERY = register("chests/salvaged_fort_archery");
    public static final ResourceLocation DECREPIT_TOMB = register("chests/decrepit_tomb");
    public static final ResourceLocation RUINED_RITUAL = register("chests/ruined_ritual");
    public static final ResourceLocation RUINED_RITUAL_BARREL = register("chests/ruined_ritual_barrel");
    public static final ResourceLocation CRIMSON_SHRINE_TREASURE = register("chests/crimson_shrine_treasure");
    public static final ResourceLocation WARPED_SHRINE_BARREL = register("chests/warped_shrine_barrel");
    public static final ResourceLocation VALLEY_SHRINE_BARREL = register("chests/valley_shrine_barrel");
    public static final ResourceLocation VALLEY_SHRINE_BARREL_MILK = register("chests/valley_shrine_barrel_milk");
    public static final ResourceLocation VALLEY_SHRINE_BARREL_BLAZE = register("chests/valley_shrine_barrel_blaze");
    public static final ResourceLocation VALLEY_SHRINE_BONE_CHEST = register("chests/valley_shrine_bone_chest");
    public static final ResourceLocation VALLEY_SHRINE_WEAPONS = register("chests/valley_shrine_weapons");
    public static final ResourceLocation VALLEY_SHRINE_TREASURE = register("chests/valley_shrine_treasure");

    public static final ResourceLocation DEAD_MOBS = register("entities/dead_mobs");
    public static final ResourceLocation DEAD_MOBS_2 = register("entities/dead_mobs_2");
    public static final ResourceLocation TALL_SKULL = register("entities/tall_skull_mobs");
    public static final ResourceLocation CULTISTS = register("entities/cultist_extra");
    public static final ResourceLocation ROTTREANT_HAUNTED = register("entities/rottreant_haunted");
    public static final ResourceLocation ROTTREANT_MURK = register("entities/rottreant_murk");
    public static final ResourceLocation ROTTREANT_GLOOM = register("entities/rottreant_gloom");

    private static ResourceLocation register(String pId) {
        return register(Goety.location(pId));
    }

    private static ResourceLocation register(ResourceLocation pId) {
        if (LOCATIONS.add(pId)) {
            return pId;
        } else {
            throw new IllegalArgumentException(pId + " is already a registered built-in loot table");
        }
    }

    public static Set<ResourceLocation> all() {
        return IMMUTABLE_LOCATIONS;
    }

}
