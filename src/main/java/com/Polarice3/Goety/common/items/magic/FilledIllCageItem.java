package com.Polarice3.Goety.common.items.magic;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.init.ModItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class FilledIllCageItem extends Item{
    public FilledIllCageItem() {
        super(new Properties()
                .rarity(Rarity.UNCOMMON)
                .stacksTo(1)
        );
    }

    @Override
    public void fillItemCategory(ItemGroup pGroup, NonNullList<ItemStack> pItems) {
    }

    @Nonnull
    @Override
    public ActionResultType useOn(ItemUseContext context) {
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        World world = context.getLevel();
        PlayerEntity player = context.getPlayer();

        if (this.getVillager(stack, world) != null && player != null) {
            Entity entity = this.getVillager(stack, world);
            if (!world.getBlockState(pos).canBeReplaced(new BlockItemUseContext(context))) {
                pos = pos.relative(context.getClickedFace());
            }

            if (entity == null || !world.getBlockState(pos).canBeReplaced(new BlockItemUseContext(context))) {
                return ActionResultType.FAIL;
            }

            if (!world.isClientSide) {
                entity.setPos(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
                if (context.getPlayer() != null) {
                    entity.lookAt(EntityAnchorArgument.Type.EYES, context.getPlayer().position());
                }
                world.addFreshEntity(entity);
            }

            player.playSound(SoundEvents.IRON_TRAPDOOR_OPEN, 1.0F, 1.0F);
            if (entity instanceof WanderingTraderEntity){
                entity.playSound(SoundEvents.WANDERING_TRADER_YES, 1.0F, 1.0F);
            } else {
                entity.playSound(SoundEvents.VILLAGER_CELEBRATE, 1.0F, 1.0F);
            }

            if (!player.abilities.instabuild) {
                ItemStack newStack = new ItemStack(ModItems.EMPTY_ILL_CAGE.get());
                if (!player.addItem(newStack)) {
                    player.drop(newStack, false);
                }
            }
            stack.shrink(1);
            return ActionResultType.sidedSuccess(world.isClientSide());
        }

        return super.useOn(context);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag) {
        if (world != null)  {
            Entity entity = this.getVillager(stack, world);
            if (entity != null) {
                IFormattableTextComponent textComponent = new TranslationTextComponent("tooltip." + Goety.MOD_ID + ".cage").append(": ");
                if (this.getVillagerName(stack) != null)  {
                    textComponent.append(Objects.requireNonNull(this.getVillagerName(stack)));
                } else {
                    textComponent.append("Villager");
                }
                textComponent.withStyle(TextFormatting.GRAY);
                tooltip.add(textComponent);
            }
        }
    }

    private Entity getVillager(ItemStack stack, World world) {
        CompoundNBT itemNBT = stack.getTag();
        if (itemNBT != null) {
            CompoundNBT entityNBT = itemNBT.getCompound("entity");
            EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(entityNBT.getString("entity")));
            if (entityType != null) {
                Entity entity = entityType.create(world);
                if (world instanceof ServerWorld && entity != null) {
                    entity.load(entityNBT);
                }
                return entity;
            }
        }

        return null;
    }

    private ITextComponent getVillagerName(ItemStack stack) {
        CompoundNBT itemNBT = stack.getTag();
        if (itemNBT != null) {
            if (itemNBT.contains("entity")) {
                CompoundNBT entityNBT = itemNBT.getCompound("entity");

                if (entityNBT.contains("name")) {
                    return new StringTextComponent(entityNBT.getString("name"));
                }
            }
        }
        return null;
    }

}
