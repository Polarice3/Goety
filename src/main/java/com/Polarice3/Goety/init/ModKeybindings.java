package com.Polarice3.Goety.init;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ModKeybindings {
    public static KeyBinding[] keyBindings;

    public static void init(){
        keyBindings = new KeyBinding[6];

        keyBindings[0] = new KeyBinding("key.goety.wand", 90, "key.goety.category");
        keyBindings[1] = new KeyBinding("key.goety.focusCircle", 88, "key.goety.category");
        keyBindings[2] = new KeyBinding("key.goety.bag", 67, "key.goety.category");
        keyBindings[3] = new KeyBinding("key.goety.ceaseFire", 86, "key.goety.category");
        keyBindings[4] = new KeyBinding("key.goety.lichKiss", 82, "key.goety.category");
        keyBindings[5] = new KeyBinding("key.goety.magnet", 77, "key.goety.category");

        for (int i = 0; i < keyBindings.length; ++i)
        {
            ClientRegistry.registerKeyBinding(keyBindings[i]);
        }
    }

    public static KeyBinding wandCircle(){
        return keyBindings[1];
    }
}
