package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.tileentities.CursedCageTileEntity;
import com.Polarice3.Goety.init.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
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
import net.minecraft.world.server.ServerWorld;
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
                        stack.shrink(1);

                        return ActionResultType.sidedSuccess(level.isClientSide());
                    }
                }
            }
        } else {
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
                return ActionResultType.sidedSuccess(level.isClientSide());
            }
        }

        return super.useOn(context);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable World level, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag) {
        if (level != null && getEntity(stack, level) != null)  {
            Entity entity = this.getEntity(stack, level);

            if (entity == null) {
                return;
            }

            IFormattableTextComponent textComponent = new TranslationTextComponent("tooltip.goety.entity")
                    .append(": ")
                    .append(new StringTextComponent(Objects.requireNonNull(ForgeRegistries.ENTITIES.getKey(entity.getType())).toString()));

            if (this.getEntityName(stack) != null)  {
                textComponent.append(" (").append(Objects.requireNonNull(this.getEntityName(stack))).append(")");
            }

            textComponent.withStyle(TextFormatting.GRAY);

            tooltip.add(textComponent);
        }
    }

    public static boolean hasEntity(ItemStack itemStack){
        return itemStack.getTag() != null;
    }

    private void setEntity(Entity entity, ItemStack stack) {
        entity.stopRiding();
        entity.ejectPassengers();

        CompoundNBT entityTag = new CompoundNBT();
        ResourceLocation name = ForgeRegistries.ENTITIES.getKey(entity.getType());

        if (name == null) {
            return;
        }

        entityTag.putString("entity", name.toString());
        if (entity.hasCustomName()) {
            entityTag.putString("name", Objects.requireNonNull(entity.getCustomName()).getString());
        }
        entity.save(entityTag);

        CompoundNBT itemNBT = stack.getOrCreateTag();
        itemNBT.put("entity", entityTag);
    }

    private Entity getEntity(ItemStack stack, World level) {
        CompoundNBT itemTag = stack.getTag();

        if (itemTag == null) {
            return null;
        }

        CompoundNBT entityTag = itemTag.getCompound("entity");
        EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityTag.getString("entity")));

        if (entityType == null) {
            return null;
        }

        Entity entity = entityType.create(level);

        if (level instanceof ServerWorld && entity != null) {
            entity.load(entityTag);
        }

        return entity;
    }

    private ITextComponent getEntityName(ItemStack stack) {
        CompoundNBT itemTag = stack.getTag();

        if (itemTag == null) {
            return null;
        }

        if (itemTag.contains("entity")) {
            CompoundNBT entityTag = itemTag.getCompound("entity");

            if (entityTag.contains("name")) {
                return new StringTextComponent(entityTag.getString("name"));
            }
        }
        return null;
    }

    private void clearEntity(ItemStack stack) {
        stack.setTag(null);
    }

}
