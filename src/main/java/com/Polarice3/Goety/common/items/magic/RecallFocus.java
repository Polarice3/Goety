package com.Polarice3.Goety.common.items.magic;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.blocks.ArcaBlock;
import com.Polarice3.Goety.common.blocks.tiles.ArcaTileEntity;
import com.Polarice3.Goety.common.events.ArcaTeleporter;
import com.Polarice3.Goety.common.magic.spells.ender.RecallSpell;
import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.network.packets.server.SPlayWorldSoundPacket;
import com.Polarice3.Goety.init.ModTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class RecallFocus extends MagicFocusItem{
    public static final String TAG_POS = "RecallPos";
    public static final String TAG_DIMENSION = "RecallDimension";

    public RecallFocus() {
        super(new RecallSpell());
    }

    public ActionResultType useOn(ItemUseContext pContext) {
        World world = pContext.getLevel();
        ItemStack stack = pContext.getItemInHand();
        PlayerEntity player = pContext.getPlayer();
        if (player != null) {
            if (!stack.isEmpty() && !hasRecall(stack)) {
                CompoundNBT compoundTag = stack.getOrCreateTag();
                if (stack.getItem() instanceof RecallFocus) {
                    BlockPos blockpos = pContext.getClickedPos();
                    TileEntity tileEntity = world.getBlockEntity(blockpos);
                    if (tileEntity instanceof ArcaTileEntity) {
                        ArcaTileEntity arcaTile = (ArcaTileEntity) tileEntity;
                        if (pContext.getPlayer() == arcaTile.getPlayer() && arcaTile.getLevel() != null) {
                            this.addRecallTags(arcaTile.getLevel().dimension(), arcaTile.getBlockPos(), compoundTag);
                            stack.setTag(compoundTag);
                            player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                            return ActionResultType.sidedSuccess(world.isClientSide);
                        }
                    }
                    BlockState blockstate = world.getBlockState(blockpos);
                    if (blockstate.is(ModTags.Blocks.RECALL_BLOCKS)) {
                        this.addRecallTags(world.dimension(), blockpos, compoundTag);
                        stack.setTag(compoundTag);
                        player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                        return ActionResultType.sidedSuccess(world.isClientSide);
                    }
                }
            }
        }
        return ActionResultType.PASS;
    }

    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (player.isShiftKeyDown() || player.isCrouching()){
            if (itemstack.getItem() instanceof RecallFocus){
                if (hasRecall(itemstack) && itemstack.getTag() != null){
                    itemstack.getTag().remove(TAG_DIMENSION);
                    itemstack.getTag().remove(TAG_POS);
                }
            }
            return ActionResult.sidedSuccess(itemstack, level.isClientSide());
        }
        return ActionResult.pass(itemstack);
    }

    public static boolean recall(LivingEntity player, ItemStack stack){
        if (hasRecall(stack) && stack.getTag() != null) {
            if (getDimension(stack.getTag()).isPresent() && getRecallBlockPos(stack.getTag()) != null) {
                BlockPos blockPos = getRecallBlockPos(stack.getTag());
                if (blockPos != null) {
                    if (getDimension(stack.getTag()).get() == player.level.dimension()) {
                        Optional<Vector3d> optional = RespawnAnchorBlock.findStandUpPosition(EntityType.PLAYER, player.level, blockPos);
                        if (optional.isPresent()) {
                            player.teleportTo(optional.get().x, optional.get().y, optional.get().z);
                            ModNetwork.INSTANCE.send(PacketDistributor.ALL.noArg(), new SPlayWorldSoundPacket(new BlockPos(player.xo, player.yo, player.zo), SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F));
                            ModNetwork.INSTANCE.send(PacketDistributor.ALL.noArg(), new SPlayWorldSoundPacket(new BlockPos(optional.get()), SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F));
                            return true;
                        }
                    } else {
                        if (player.getServer() != null) {
                            ServerWorld serverWorld = player.getServer().getLevel(getDimension(stack.getTag()).get());
                            if (serverWorld != null) {
                                Optional<Vector3d> optional = RespawnAnchorBlock.findStandUpPosition(EntityType.PLAYER, serverWorld, blockPos);
                                if (optional.isPresent()) {
                                    player.changeDimension(serverWorld, new ArcaTeleporter(optional.get()));
                                    player.teleportTo(optional.get().x, optional.get().y, optional.get().z);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasRecall(ItemStack p_40737_) {
        CompoundNBT compoundtag = p_40737_.getTag();
        return compoundtag != null && (compoundtag.contains(TAG_DIMENSION) || compoundtag.contains(TAG_POS));
    }

    public static BlockPos getRecallBlockPos(CompoundNBT compoundTag){
        boolean flag = compoundTag.contains(TAG_POS);
        boolean flag1 = compoundTag.contains(TAG_DIMENSION);
        if (flag && flag1) {
            Optional<RegistryKey<World>> optional = getDimension(compoundTag);
            if (optional.isPresent()) {
                return NBTUtil.readBlockPos(compoundTag.getCompound(TAG_POS));
            }
        }
        return null;
    }

    public static Optional<RegistryKey<World>> getDimension(CompoundNBT p_40728_) {
        return World.RESOURCE_KEY_CODEC.parse(NBTDynamicOps.INSTANCE, p_40728_.get(TAG_DIMENSION)).result();
    }

    public void addRecallTags(RegistryKey<World> p_40733_, BlockPos p_40734_, CompoundNBT p_40735_) {
        p_40735_.put(TAG_POS, NBTUtil.writeBlockPos(p_40734_));
        World.RESOURCE_KEY_CODEC.encodeStart(NBTDynamicOps.INSTANCE, p_40733_).resultOrPartial(Goety.LOGGER::error).ifPresent((p_40731_) -> {
            p_40735_.put(TAG_DIMENSION, p_40731_);
        });
    }

    public static boolean isValid(ServerWorld serverLevel, ItemStack stack){
        if (hasRecall(stack) && stack.getTag() != null) {
            if (getDimension(stack.getTag()).isPresent()) {
                ServerWorld serverLevel1 = serverLevel.getServer().getLevel(getDimension(stack.getTag()).get());
                if (serverLevel1 != null) {
                    if (getRecallBlockPos(stack.getTag()) != null) {
                        BlockPos blockPos = getRecallBlockPos(stack.getTag());
                        if (blockPos != null) {
                            BlockState blockState = serverLevel1.getBlockState(blockPos);
                            Block block = blockState.getBlock();
                            return block instanceof ArcaBlock || blockState.is(ModTags.Blocks.RECALL_BLOCKS);
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        addRecallText(stack, tooltip);
    }

    public static void addRecallText(ItemStack stack, List<ITextComponent> tooltip){
        if (stack.getTag() != null) {
            if (!hasRecall(stack)) {
                tooltip.add(new TranslationTextComponent("info.goety.focus.noPos"));
            } else {
                BlockPos blockPos = getRecallBlockPos(stack.getTag());
                if (blockPos != null && getDimension(stack.getTag()).isPresent()) {
                    tooltip.add(new TranslationTextComponent("info.goety.focus.Pos").append(" ").append(new TranslationTextComponent("info.goety.focus.PosNum", blockPos.getX(), blockPos.getY(), blockPos.getZ())));
                    tooltip.add(new TranslationTextComponent("info.goety.focus.PosDim", getDimension(stack.getTag()).get().location().toString()));
                }
            }
        }
    }

}
