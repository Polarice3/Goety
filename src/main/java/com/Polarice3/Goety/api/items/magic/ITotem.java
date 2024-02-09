package com.Polarice3.Goety.api.items.magic;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.utils.GoldTotemFinder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public interface ITotem {
    String SOULS_AMOUNT = "Souls";
    String MAX_SOUL_AMOUNT = "Max Souls";
    int MAX_SOULS = MainConfig.MaxSouls.get();

    int getMaxSouls();

    default void setTagTick(ItemStack stack){
        if (stack.getTag() == null){
            CompoundNBT compound = stack.getOrCreateTag();
            compound.putInt(SOULS_AMOUNT, 0);
            compound.putInt(MAX_SOUL_AMOUNT, this.getMaxSouls());
        }
        if (!stack.getTag().contains(MAX_SOUL_AMOUNT)){
            CompoundNBT compound = stack.getOrCreateTag();
            compound.putInt(MAX_SOUL_AMOUNT, this.getMaxSouls());
        }
        if (stack.getTag().getInt(SOULS_AMOUNT) > stack.getTag().getInt(MAX_SOUL_AMOUNT)){
            stack.getTag().putInt(SOULS_AMOUNT, stack.getTag().getInt(MAX_SOUL_AMOUNT));
        }
        if (stack.getTag().getInt(SOULS_AMOUNT) < 0){
            stack.getTag().putInt(SOULS_AMOUNT, 0);
        }
    }

    static boolean isFull(ItemStack itemStack) {
        if (itemStack.getTag() == null){
            return false;
        }
        int Soulcount = itemStack.getTag().getInt(SOULS_AMOUNT);
        int MaxSouls = itemStack.getTag().getInt(MAX_SOUL_AMOUNT);
        return Soulcount == MaxSouls;
    }

    static boolean isEmpty(ItemStack itemStack) {
        if (itemStack.getTag() == null){
            return true;
        }
        int Soulcount = itemStack.getTag().getInt(SOULS_AMOUNT);
        return Soulcount == 0;
    }

    static boolean UndyingEffect(PlayerEntity player){
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

    static int currentSouls(ItemStack itemStack){
        if (itemStack.getTag() != null){
            return itemStack.getTag().getInt(SOULS_AMOUNT);
        } else {
            return 0;
        }
    }

    static int maximumSouls(ItemStack itemStack){
        if (itemStack.getTag() != null){
            return itemStack.getTag().getInt(MAX_SOUL_AMOUNT);
        } else {
            return 0;
        }
    }

    static void setSoulsamount(ItemStack itemStack, int souls){
        if (!(itemStack.getItem() instanceof ITotem)) {
            return;
        }
        itemStack.getOrCreateTag().putInt(SOULS_AMOUNT, souls);
    }

    static void setMaxSoulAmount(ItemStack itemStack, int souls){
        if (!(itemStack.getItem() instanceof ITotem)) {
            return;
        }
        itemStack.getOrCreateTag().putInt(MAX_SOUL_AMOUNT, souls);
    }

    static void increaseSouls(ItemStack itemStack, int souls) {
        if (!(itemStack.getItem() instanceof ITotem)) {
            return;
        }
        assert itemStack.getTag() != null;
        int Soulcount = itemStack.getTag().getInt(SOULS_AMOUNT);
        if (!isFull(itemStack)) {
            Soulcount += souls;
            itemStack.getOrCreateTag().putInt(SOULS_AMOUNT, Soulcount);
        }
    }

    static void decreaseSouls(ItemStack itemStack, int souls) {
        if (!(itemStack.getItem() instanceof ITotem)) {
            return;
        }
        assert itemStack.getTag() != null;
        int Soulcount = itemStack.getTag().getInt(SOULS_AMOUNT);
        if (!isEmpty(itemStack)) {
            Soulcount -= souls;
            itemStack.getOrCreateTag().putInt(SOULS_AMOUNT, Soulcount);
        }
    }
}
