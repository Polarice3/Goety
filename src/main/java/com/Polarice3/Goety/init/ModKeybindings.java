package com.Polarice3.Goety.init;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModKeybindings {
    public static KeyBinding[] keyBindings;

    public static void init(){
        keyBindings = new KeyBinding[3];

        keyBindings[0] = new KeyBinding("key.goety.wand", 90, "key.goety.category");
        keyBindings[1] = new KeyBinding("key.goety.wandandbag", 88, "key.goety.category");
        keyBindings[2] = new KeyBinding("key.goety.bag", 67, "key.goety.category");

        for (int i = 0; i < keyBindings.length; ++i)
        {
            ClientRegistry.registerKeyBinding(keyBindings[i]);
        }
    }
}
