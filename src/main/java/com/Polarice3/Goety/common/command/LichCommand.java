package com.Polarice3.Goety.common.command;

import com.Polarice3.Goety.common.lichdom.ILichdom;
import com.Polarice3.Goety.utils.LichdomHelper;
import com.Polarice3.Goety.utils.LichdomUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

public class LichCommand {
    private static final SimpleCommandExceptionType ERROR_ALREADY_LICH = new SimpleCommandExceptionType(new TranslationTextComponent("commands.lich.turnlich.failed"));
    private static final SimpleCommandExceptionType ERROR_ALREADY_NOT_LICH = new SimpleCommandExceptionType(new TranslationTextComponent("commands.lich.delich.failed"));

    public static void register(CommandDispatcher<CommandSource> pDispatcher) {
        pDispatcher.register(Commands.literal("lichdom").requires((p_198442_0_) -> {
            return p_198442_0_.hasPermission(2);
        }).then(Commands.literal("grant").then(Commands.argument("targets", EntityArgument.players()).executes((p_198445_0_) -> {
            return grantLichdom(p_198445_0_.getSource(), EntityArgument.getPlayers(p_198445_0_, "targets"));
        }))).then(Commands.literal("revoke").then(Commands.argument("targets", EntityArgument.players()).executes((p_198445_0_) -> {
            return revokeLichdom(p_198445_0_.getSource(), EntityArgument.getPlayers(p_198445_0_, "targets"));
        }))).then(Commands.literal("query").then(Commands.argument("targets", EntityArgument.player()).executes((p_198435_0_) -> {
            return queryLich(p_198435_0_.getSource(), EntityArgument.getPlayer(p_198435_0_, "targets"));
        }))));
    }

    private static int grantLichdom(CommandSource pSource, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException {
        for (ServerPlayerEntity player : targets){
            ILichdom lichdom = LichdomHelper.getCapability(player);
            boolean isLich = lichdom.getLichdom();
            if (!isLich) {
                lichdom.setLichdom(true);
                LichdomHelper.sendLichUpdatePacket(player);
                pSource.sendSuccess(new TranslationTextComponent("commands.lich.turnlich.success", player.getDisplayName()), false);
            } else {
                throw ERROR_ALREADY_LICH.create();
            }
        }
        return 0;
    }

    private static int revokeLichdom(CommandSource pSource, Collection<ServerPlayerEntity> targets) throws CommandSyntaxException {
        for (ServerPlayerEntity player : targets){
            ILichdom lichdom = LichdomHelper.getCapability(player);
            boolean isLich = lichdom.getLichdom();
            if (isLich) {
                lichdom.setLichdom(false);
                LichdomHelper.sendLichUpdatePacket(player);
                pSource.sendSuccess(new TranslationTextComponent("commands.lich.delich.success", player.getDisplayName()), false);
            } else {
                throw ERROR_ALREADY_NOT_LICH.create();
            }
        }
        return 0;
    }

    private static int queryLich(CommandSource pSource, ServerPlayerEntity pPlayer) {
        ILichdom lichdom = LichdomHelper.getCapability(pPlayer);
        boolean isLich = lichdom.getLichdom();
        if (isLich){
            pSource.sendSuccess(new TranslationTextComponent("commands.lich.query.true", pPlayer.getDisplayName()), false);
        } else {
            pSource.sendSuccess(new TranslationTextComponent("commands.lich.query.false", pPlayer.getDisplayName()), false);
        }
        return 0;
    }
}
