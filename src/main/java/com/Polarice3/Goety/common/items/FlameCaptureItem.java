package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.ItemConfig;
import com.Polarice3.Goety.common.blocks.tiles.CursedCageTileEntity;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModSounds;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * Code based off @stal111's Quantum Catcher code
 */
public class FlameCaptureItem extends Item {

    public FlameCaptureItem() {
        super(new Item.Properties().tab(Goety.TAB).stacksTo(1));
    }

    @Nonnull
    @Override
    public ActionResultType useOn(ItemUseContext context) {
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        World level = context.getLevel();
        PlayerEntity player = context.getPlayer();

        if (this.getEntity(stack, level) != null) {
            if (level.getBlockState(pos).getBlock() == ModBlocks.CURSED_CAGE_BLOCK.get()){
                TileEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof CursedCageTileEntity){
                    CursedCageTileEntity cursedCageBlock = (CursedCageTileEntity) blockEntity;
                    if (!cursedCageBlock.getItem().isEmpty()){
                        return ActionResultType.PASS;
                    } else {
                        if (!level.isClientSide) {
                            level.setBlockAndUpdate(pos, Blocks.SPAWNER.defaultBlockState());
                            TileEntity blockentity = level.getBlockEntity(pos);
                            if (blockentity instanceof MobSpawnerTileEntity) {
                                ((MobSpawnerTileEntity) blockentity).getSpawner().setEntityId(this.getEntity(stack, level).getType());
                            }
                        }
                        this.clearEntity(stack);
                        if (player != null) {
                            player.playSound(ModSounds.FLAME_CAPTURE_RELEASE.get(), 1.0F, 1.0F);
                        }
                        stack.shrink(1);

                        return ActionResultType.sidedSuccess(level.isClientSide());
                    }
                }
            }
        } else {
            if (ItemConfig.FireSpawnCage.get()) {
                if (level.getBlockState(pos).getBlock() == Blocks.SPAWNER){
                    if (!level.isClientSide()) {
                        TileEntity blockentity = level.getBlockEntity(pos);
                        if (blockentity instanceof MobSpawnerTileEntity) {
                            Entity entity = ((MobSpawnerTileEntity) blockentity).getSpawner().getOrCreateDisplayEntity();
                            if (entity != null) {
                                this.setEntity(entity, stack);
                                level.destroyBlock(pos, false);
                            }
                        }
                    }
                    if (player != null) {
                        player.playSound(ModSounds.FLAME_CAPTURE_CATCH.get(), 1.0F, 1.0F);
                    }
                    return ActionResultType.sidedSuccess(level.isClientSide());
                }
            }
        }

        return super.useOn(context);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable World level, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag) {
        if (ItemConfig.FireSpawnCage.get()) {
            if (level != null && getEntity(stack, level) != null)  {
                Entity entity = this.getEntity(stack, level);

                if (entity == null) {
                    return;
                }

                IFormattableTextComponent textComponent = new TranslationTextComponent("tooltip.goety.entity")
                        .append(": ")
                        .append(new StringTextComponent(Objects.requireNonNull(ForgeRegistries.ENTITIES.getKey(entity.getType())).toString())
                                .withStyle(TextFormatting.GREEN));

                tooltip.add(textComponent);
            }
        } else {
            IFormattableTextComponent textComponent = new TranslationTextComponent("tooltip.goety.disabled")
                    .withStyle(TextFormatting.DARK_RED);

            tooltip.add(textComponent);
        }

    }

    public static boolean hasEntity(ItemStack itemStack){
        return itemStack.getTag() != null;
    }

    private void setEntity(Entity entity, ItemStack stack) {
        CompoundNBT entityTag = stack.getOrCreateTag();
        ResourceLocation name = ForgeRegistries.ENTITIES.getKey(entity.getType());

        if (name == null) {
            return;
        }

        entityTag.putString("mob", name.toString());
    }

    private Entity getEntity(ItemStack stack, World level) {
        CompoundNBT itemTag = stack.getTag();

        if (itemTag == null) {
            return null;
        }

        EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(itemTag.getString("mob")));

        if (entityType == null) {
            return null;
        }

        return entityType.create(level);
    }

    private void clearEntity(ItemStack stack) {
        stack.setTag(null);
    }

}
