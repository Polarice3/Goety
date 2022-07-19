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
    public static final ResourceLocation DEAD_MOBS = register("entities/dead_mobs");
    public static final ResourceLocation CULTISTS = register("entities/cultist_extra");

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
