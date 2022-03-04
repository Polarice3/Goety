package com.Polarice3.Goety.common.items;


import com.Polarice3.Goety.Goety;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Random;

public class FlameGunItem extends Item {

    public FlameGunItem() {
        super(new Properties().tab(Goety.TAB).durability(256));
    }

    private static float getCharge(int useTime, ItemStack stack) {
        float f = (float)useTime / (float)25;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public static int getChargeDuration(ItemStack stack) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
        return i == 0 ? 25 : 25 - 5 * i;
    }

    public static boolean isPowered(ItemStack stack) {
        CompoundNBT compoundnbt = stack.getTag();
        return compoundnbt != null && compoundnbt.getBoolean("Charged");
    }

    public static void setCharged(ItemStack stack, boolean chargedIn) {
        CompoundNBT compoundnbt = stack.getOrCreateTag();
        compoundnbt.putBoolean("Charged", chargedIn);
    }

    public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft){
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity) entityLiving;
            int i = this.getUseDuration(stack) - timeLeft;
            float f = getCharge(i, stack);
            if (f >= 1.0) {
                setCharged(stack, true);
                if (!worldIn.isClientSide && isPowered(stack)) {
                    Vector3d vector3d = playerentity.getViewVector( 1.0F);
                    Random random = worldIn.random;
                    double d2 = random.nextGaussian() * 0.01D + vector3d.x;
                    double d3 = random.nextGaussian() * 0.01D + vector3d.y;
                    double d4 = random.nextGaussian() * 0.01D + vector3d.z;
                    FireballEntity fireballEntity = new FireballEntity(worldIn, playerentity, d2, d3, d4);
                    fireballEntity.setPos(playerentity.getX() + vector3d.x * 2.0D, playerentity.getY(0.5D) + 0.5D, playerentity.getZ() + vector3d.z * 2.0D);
                    worldIn.addFreshEntity(fireballEntity);
                    if (!playerentity.isSilent()) {
                        playerentity.level.levelEvent(null, 1016, playerentity.blockPosition(), 0);
                    }
                    stack.hurtAndBreak(1, playerentity, (player) -> {
                        player.broadcastBreakEvent(playerentity.getUsedItemHand());
                    });
                }
            } else {
                if (!worldIn.isClientSide && !isPowered(stack)) {
                    Vector3d vector3d = playerentity.getViewVector( 1.0F);
                    Random random = worldIn.random;
                    double d2 = random.nextGaussian() * 0.05D + (double) vector3d.x;
                    double d3 = random.nextGaussian() * 0.05D + (double) vector3d.y;
                    double d4 = random.nextGaussian() * 0.05D + (double) vector3d.z;
                    SmallFireballEntity fireballEntity = new SmallFireballEntity(worldIn, playerentity, d2, d3, d4);
                    fireballEntity.setPos(playerentity.getX() + vector3d.x * 2.0D, playerentity.getY(0.5D) + 0.5D, playerentity.getZ() + vector3d.z * 2.0D);
                    worldIn.addFreshEntity(fireballEntity);
                    if (!playerentity.isSilent()) {
                        playerentity.level.levelEvent(null, 1016, playerentity.blockPosition(), 0);
                    }
                    stack.hurtAndBreak(1, playerentity, (player) -> {
                        player.broadcastBreakEvent(playerentity.getUsedItemHand());
                    });
                }
            }
        }
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.startUsingItem(handIn);
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (isPowered(itemstack)) {
            setCharged(itemstack, false);
        }
        return ActionResult.consume(itemstack);
    }

}
