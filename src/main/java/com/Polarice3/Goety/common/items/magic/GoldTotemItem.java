package com.Polarice3.Goety.common.items.magic;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.api.items.magic.ITotem;
import com.Polarice3.Goety.common.blocks.CursedCageBlock;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.utils.GoldTotemFinder;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Learned how to make Gold Totem gain Soul Energy from codes by @Ipsis
 */
public class GoldTotemItem extends Item implements ITotem {
    public int maxSouls;

    public GoldTotemItem(int maxSouls) {
        super(new Properties().tab(Goety.TAB).stacksTo(1).rarity(Rarity.RARE));
        this.maxSouls = maxSouls;
    }

    public int getMaxSouls(){
        return this.maxSouls;
    }

    @Override
    public void fillItemCategory(ItemGroup pGroup, NonNullList<ItemStack> pItems) {
        if (this.allowdedIn(pGroup)){
            ItemStack emptySouls = new ItemStack(this);
            ITotem.setSoulsamount(emptySouls, 0);
            pItems.add(emptySouls);

            ItemStack maxSouls = new ItemStack(this);
            ITotem.setMaxSoulAmount(maxSouls, this.getMaxSouls());
            pItems.add(maxSouls);
        }
    }

    public int getRGBDurabilityForDisplay(ItemStack stack)
    {
        return MathHelper.hsvToRgb(Math.max(0.0F, (float) (1.0F - getDurabilityForDisplay(stack))/2.0F), 1.0F, 1.0F);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public void onCraftedBy(ItemStack pStack, World pLevel, PlayerEntity pPlayer) {
        ITotem.setSoulsamount(pStack, 0);
        ITotem.setMaxSoulAmount(pStack, this.getMaxSouls());
        super.onCraftedBy(pStack, pLevel, pPlayer);
    }

    @Nonnull
    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack container = itemStack.copy();
        if (container.getTag() != null) {
            if (container.getTag().getInt(SOULS_AMOUNT) > MainConfig.CraftingSouls.get()) {
                ITotem.decreaseSouls(container, MainConfig.CraftingSouls.get());
                return container;
            } else {
                return new ItemStack(ModItems.SPENT_TOTEM.get());
            }
        } else {
            return new ItemStack(ModItems.SPENT_TOTEM.get());
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        this.setTagTick(stack);
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    public static boolean isActivated(ItemStack itemStack){
        return itemStack.getTag() != null;
    }

    private static boolean isFull(ItemStack itemStack) {
        assert itemStack.getTag() != null;
        int Soulcount = itemStack.getTag().getInt(SOULS_AMOUNT);
        return Soulcount == MAX_SOULS;
    }

    private static boolean isEmpty(ItemStack itemStack) {
        assert itemStack.getTag() != null;
        int Soulcount = itemStack.getTag().getInt(SOULS_AMOUNT);
        return Soulcount == 0;
    }

    public static boolean UndyingEffect(PlayerEntity player){
        ItemStack itemStack = GoldTotemFinder.FindTotem(player);
        if (!itemStack.isEmpty()) {
            if (itemStack.getTag() != null) {
                if (MainConfig.TotemUndying.get()) {
                    return itemStack.getTag().getInt(SOULS_AMOUNT) == MAX_SOULS;
                }
            }
        }
        return false;
    }

    public static int currentSouls(ItemStack itemStack){
        if (itemStack.getTag() != null){
            return itemStack.getTag().getInt(SOULS_AMOUNT);
        } else {
            return 0;
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.getTag() != null;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack){
        if (stack.getTag() != null) {
            int Soulcount = stack.getTag().getInt(SOULS_AMOUNT);
            return 1.0D - (Soulcount / (double) MAX_SOULS);
        } else {
            return 1.0D;
        }
    }

    public ActionResultType useOn(ItemUseContext pContext) {
        World world = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);
        if (blockstate.is(ModBlocks.CURSED_CAGE_BLOCK.get()) && !blockstate.getValue(CursedCageBlock.POWERED)) {
            ItemStack itemstack = pContext.getItemInHand();
            if (!world.isClientSide) {
                ((CursedCageBlock) ModBlocks.CURSED_CAGE_BLOCK.get()).setItem(world, blockpos, blockstate, itemstack);
                world.levelEvent(null, 1010, blockpos, Item.getId(this));
                itemstack.shrink(1);
            }

            return ActionResultType.sidedSuccess(world.isClientSide);
        } else {
            return ActionResultType.PASS;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (stack.getTag() != null) {
            int Soulcounts = stack.getTag().getInt(SOULS_AMOUNT);
            int MaxSouls = stack.getTag().getInt(MAX_SOUL_AMOUNT);
            tooltip.add(new TranslationTextComponent("info.goety.goldtotem.souls", Soulcounts, MaxSouls));
        }
    }

}
