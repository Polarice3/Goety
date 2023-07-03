package com.Polarice3.Goety.init;

import com.Polarice3.Goety.common.command.GoetyCommand;
import com.Polarice3.Goety.common.command.LichCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RegisterCommands {

    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> commandDispatcher = event.getDispatcher();
        GoetyCommand.register(commandDispatcher);
        LichCommand.register(commandDispatcher);
    }
}
