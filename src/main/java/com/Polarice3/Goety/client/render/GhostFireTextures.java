package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

import java.util.Map;

public class GhostFireTextures {
    public static final Map<Integer, ResourceLocation> TEXTURES = Util.make(Maps.newHashMap(), (map) -> {
        map.put(0, location("pre/frost_prefire1.png"));
        map.put(1, location("pre/frost_prefire2.png"));
        map.put(2, location("pre/frost_prefire3.png"));
        map.put(3, location("pre/frost_prefire4.png"));
        map.put(4, location("pre/frost_prefire5.png"));
        map.put(5, location("pre/frost_prefire6.png"));
        map.put(6, location("pre/frost_prefire7.png"));
        map.put(7, location("pre/frost_prefire8.png"));
        map.put(8, location("pre/frost_prefire9.png"));
        map.put(9, location("pre/frost_prefire10.png"));
        map.put(10, location("pre/frost_prefire11.png"));
        map.put(11, location("pre/frost_prefire12.png"));
        map.put(12, location("pre/frost_prefire13.png"));
        map.put(13, location("frost_fire1.png"));
        map.put(14, location("frost_fire2.png"));
        map.put(15, location("frost_fire3.png"));
        map.put(16, location("frost_fire4.png"));
        map.put(17, location("frost_fire5.png"));
        map.put(18, location("frost_fire6.png"));
        map.put(19, location("frost_fire7.png"));
        map.put(20, location("frost_fire8.png"));
        map.put(21, location("frost_fire9.png"));
        map.put(22, location("frost_fire10.png"));
        map.put(23, location("frost_fire11.png"));
        map.put(24, location("frost_fire12.png"));
        map.put(25, location("frost_fire13.png"));
        map.put(26, location("frost_fire14.png"));
        map.put(27, location("frost_fire15.png"));
        map.put(28, location("frost_fire16.png"));
        map.put(29, location("frost_fire17.png"));
        map.put(30, location("frost_fire18.png"));
        map.put(31, location("frost_fire19.png"));
        map.put(32, location("frost_fire20.png"));
        map.put(33, location("frost_fire21.png"));
        map.put(34, location("frost_fire22.png"));
        map.put(35, location("frost_fire23.png"));
        map.put(36, location("frost_fire24.png"));
        map.put(37, location("frost_fire25.png"));
        map.put(38, location("frost_fire26.png"));
        map.put(39, location("frost_fire27.png"));
        map.put(40, location("frost_fire28.png"));
        map.put(41, location("frost_fire29.png"));
        map.put(42, location("frost_fire30.png"));
        map.put(43, location("frost_fire31.png"));
        map.put(44, location("frost_fire32.png"));
    });

    public static ResourceLocation location(String path) {
        return Goety.location("textures/entity/projectiles/ghost_fire/" + path);
    }
}
