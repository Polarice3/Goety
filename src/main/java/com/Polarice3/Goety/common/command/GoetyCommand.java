package com.Polarice3.Goety.common.command;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.events.IllagerSpawner;
import com.Polarice3.Goety.utils.SEHelper;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Collection;

public class GoetyCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED = new SimpleCommandExceptionType(new TranslationTextComponent("commands.summonnoai.failed"));
    private static final SimpleCommandExceptionType ERROR_DUPLICATE_UUID = new SimpleCommandExceptionType(new TranslationTextComponent("commands.summonnoai.failed.uuid"));
    private static final SimpleCommandExceptionType INVALID_POSITION = new SimpleCommandExceptionType(new TranslationTextComponent("commands.summonnoai.invalidPosition"));
    private static final SimpleCommandExceptionType ERROR_SET_POINTS_INVALID = new SimpleCommandExceptionType(new TranslationTextComponent("commands.goety.soul.set.points.invalid"));

    public static void register(CommandDispatcher<CommandSource> pDispatcher) {
        pDispatcher.register(Commands.literal("goety").requires((p_198442_0_) -> {
            return p_198442_0_.hasPermission(2);
        }).then(Commands.literal("soul").then(Commands.literal("add").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("amount", IntegerArgumentType.integer()).executes((p_198445_0_) -> {
            return addSoulEnergy(p_198445_0_.getSource(), EntityArgument.getPlayers(p_198445_0_, "targets"), IntegerArgumentType.getInteger(p_198445_0_, "amount"));
        })))).then(Commands.literal("set").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("amount", IntegerArgumentType.integer(0)).executes((p_198439_0_) -> {
            return setSoulEnergy(p_198439_0_.getSource(), EntityArgument.getPlayers(p_198439_0_, "targets"), IntegerArgumentType.getInteger(p_198439_0_, "amount"));
        }))))).then(Commands.literal("illager").then(Commands.literal("spawn").executes((p_198352_0_) -> {
            return spawnIllagers(p_198352_0_.getSource(), p_198352_0_.getSource().getPlayerOrException());
        }).then(Commands.argument("targets", EntityArgument.player()).executes((p_198435_0_) -> {
            return spawnIllagers(p_198435_0_.getSource(), EntityArgument.getPlayer(p_198435_0_, "targets"));
        })))).then(Commands.literal("summonnoai").then(Commands.argument("entity", EntitySummonArgument.id()).suggests(SuggestionProviders.SUMMONABLE_ENTITIES).executes((p_198738_0_) -> {
            return spawnEntity(p_198738_0_.getSource(), EntitySummonArgument.getSummonableEntity(p_198738_0_, "entity"), p_198738_0_.getSource().getPosition(), new CompoundNBT(), true);
        }).then(Commands.argument("pos", Vec3Argument.vec3()).executes((p_198735_0_) -> {
            return spawnEntity(p_198735_0_.getSource(), EntitySummonArgument.getSummonableEntity(p_198735_0_, "entity"), Vec3Argument.getVec3(p_198735_0_, "pos"), new CompoundNBT(), true);
        }).then(Commands.argument("nbt", NBTCompoundTagArgument.compoundTag()).executes((p_198739_0_) -> {
            return spawnEntity(p_198739_0_.getSource(), EntitySummonArgument.getSummonableEntity(p_198739_0_, "entity"), Vec3Argument.getVec3(p_198739_0_, "pos"), NBTCompoundTagArgument.getCompoundTag(p_198739_0_, "nbt"), false);
        }))))));
    }

    private static int addSoulEnergy(CommandSource pSource, Collection<? extends ServerPlayerEntity> pTargets, int pAmount) {
        for(ServerPlayerEntity serverPlayer : pTargets) {
            if (SEHelper.getSoulsContainer(serverPlayer)) {
                SEHelper.increaseSouls(serverPlayer, pAmount);
            } else {
                pSource.sendFailure(new TranslationTextComponent("commands.goety.soul.failed", pAmount, pTargets.iterator().next().getDisplayName()));
            }
        }

        if (pTargets.size() == 1) {
            pSource.sendSuccess(new TranslationTextComponent("commands.goety.soul.add"+ ".success.single", pAmount, pTargets.iterator().next().getDisplayName()), true);
        } else {
            pSource.sendSuccess(new TranslationTextComponent("commands.goety.soul.add" + ".success.multiple", pAmount, pTargets.size()), true);
        }

        return pTargets.size();
    }

    private static int setSoulEnergy(CommandSource pSource, Collection<? extends ServerPlayerEntity> pTargets, int pAmount) throws CommandSyntaxException {
        int i = 0;

        for(ServerPlayerEntity serverPlayer : pTargets) {
            if (SEHelper.getSoulsContainer(serverPlayer)) {
                SEHelper.setSoulsAmount(serverPlayer, pAmount);
                ++i;
            } else {
                pSource.sendFailure(new TranslationTextComponent("commands.goety.soul.failed", pAmount, pTargets.iterator().next().getDisplayName()));
            }
        }

        if (i == 0) {
            throw ERROR_SET_POINTS_INVALID.create();
        } else {
            if (pTargets.size() == 1) {
                pSource.sendSuccess(new TranslationTextComponent("commands.goety.soul.set" + ".success.single", pAmount, pTargets.iterator().next().getDisplayName()), true);
            } else {
                pSource.sendSuccess(new TranslationTextComponent("commands.goety.soul.set" + ".success.multiple", pAmount, pTargets.size()), true);
            }

            return pTargets.size();
        }
    }

    private static int spawnIllagers(CommandSource pSource, ServerPlayerEntity pPlayer) {
        int i = SEHelper.getSoulAmountInt(pPlayer);
        if (i > MainConfig.IllagerAssaultSEThreshold.get()){
            pSource.sendSuccess(new TranslationTextComponent("commands.goety.illager.spawn.success", pPlayer.getDisplayName()), false);
            IllagerSpawner illagerSpawner = new IllagerSpawner();
            illagerSpawner.forceSpawn(pPlayer.getLevel(), pPlayer);
            return 1;
        } else {
            pSource.sendFailure(new TranslationTextComponent("commands.goety.illager.spawn.failure", pPlayer.getDisplayName()));
        }
        return i;
    }

    private static int spawnEntity(CommandSource pSource, ResourceLocation pType, Vector3d pPos, CompoundNBT pNbt, boolean pRandomizeProperties) throws CommandSyntaxException {
        BlockPos blockpos = new BlockPos(pPos);
        if (!World.isInSpawnableBounds(blockpos)) {
            throw INVALID_POSITION.create();
        } else {
            CompoundNBT compoundnbt = pNbt.copy();
            compoundnbt.putString("id", pType.toString());
            ServerWorld serverworld = pSource.getLevel();
            Entity entity = EntityType.loadEntityRecursive(compoundnbt, serverworld, (p_218914_1_) -> {
                p_218914_1_.moveTo(pPos.x, pPos.y, pPos.z, p_218914_1_.yRot, p_218914_1_.xRot);
                return p_218914_1_;
            });
            if (entity == null) {
                throw ERROR_FAILED.create();
            } else {
                if (entity instanceof MobEntity){
                    ((MobEntity) entity).setNoAi(true);
                    ((MobEntity) entity).setPersistenceRequired();
                    if (pRandomizeProperties){
                        ((MobEntity)entity).finalizeSpawn(pSource.getLevel(), pSource.getLevel().getCurrentDifficultyAt(entity.blockPosition()), SpawnReason.COMMAND, (ILivingEntityData)null, (CompoundNBT)null);
                    }
                }

                if (!serverworld.tryAddFreshEntityWithPassengers(entity)) {
                    throw ERROR_DUPLICATE_UUID.create();
                } else {
                    pSource.sendSuccess(new TranslationTextComponent("commands.summonnoai.success", entity.getDisplayName()), true);
                    return 1;
                }
            }
        }
    }
}
