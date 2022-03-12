package com.Polarice3.Goety.common.command;

import com.Polarice3.Goety.common.infamy.IInfamy;
import com.Polarice3.Goety.utils.InfamyHelper;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

public class InfamyCommand {
    private static final SimpleCommandExceptionType ERROR_SET_POINTS_INVALID = new SimpleCommandExceptionType(new TranslationTextComponent("commands.infamy.set.points.invalid"));

    public static void register(CommandDispatcher<CommandSource> pDispatcher) {
        pDispatcher.register(Commands.literal("infamy").requires((p_198442_0_) -> {
            return p_198442_0_.hasPermission(2);
        }).then(Commands.literal("add").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("amount", IntegerArgumentType.integer()).executes((p_198445_0_) -> {
            return addInfamy(p_198445_0_.getSource(), EntityArgument.getPlayers(p_198445_0_, "targets"), IntegerArgumentType.getInteger(p_198445_0_, "amount"));
        })))).then(Commands.literal("set").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("amount", IntegerArgumentType.integer(0)).executes((p_198439_0_) -> {
            return setInfamy(p_198439_0_.getSource(), EntityArgument.getPlayers(p_198439_0_, "targets"), IntegerArgumentType.getInteger(p_198439_0_, "amount"));
        })))).then(Commands.literal("query").then(Commands.argument("targets", EntityArgument.player()).executes((p_198435_0_) -> {
            return queryInfamy(p_198435_0_.getSource(), EntityArgument.getPlayer(p_198435_0_, "targets"));
        }))));
    }

    private static int queryInfamy(CommandSource pSource, ServerPlayerEntity pPlayer) {
        IInfamy infamy = InfamyHelper.getCapability(pPlayer);
        int i = infamy.getInfamy();
        pSource.sendSuccess(new TranslationTextComponent("commands.infamy.query", pPlayer.getDisplayName(), i), false);
        return i;
    }

    private static int addInfamy(CommandSource pSource, Collection<? extends ServerPlayerEntity> pTargets, int pAmount) {
        for(ServerPlayerEntity serverplayerentity : pTargets) {
            IInfamy infamy = InfamyHelper.getCapability(serverplayerentity);
            infamy.increaseInfamy(pAmount);
        }

        if (pTargets.size() == 1) {
            pSource.sendSuccess(new TranslationTextComponent("commands.infamy.add"+ ".success.single", pAmount, pTargets.iterator().next().getDisplayName()), true);
        } else {
            pSource.sendSuccess(new TranslationTextComponent("commands.infamy.add" + ".success.multiple", pAmount, pTargets.size()), true);
        }

        return pTargets.size();
    }

    private static int setInfamy(CommandSource pSource, Collection<? extends ServerPlayerEntity> pTargets, int pAmount) throws CommandSyntaxException {
        int i = 0;

        for(ServerPlayerEntity serverplayerentity : pTargets) {
            IInfamy infamy = InfamyHelper.getCapability(serverplayerentity);
            infamy.setInfamy(pAmount);
            ++i;
        }

        if (i == 0) {
            throw ERROR_SET_POINTS_INVALID.create();
        } else {
            if (pTargets.size() == 1) {
                pSource.sendSuccess(new TranslationTextComponent("commands.infamy.set" + ".success.single", pAmount, pTargets.iterator().next().getDisplayName()), true);
            } else {
                pSource.sendSuccess(new TranslationTextComponent("commands.infamy.set" + ".success.multiple", pAmount, pTargets.size()), true);
            }

            return pTargets.size();
        }
    }
}
