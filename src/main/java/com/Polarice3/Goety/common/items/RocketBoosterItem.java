package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class RocketBoosterItem extends Item {
    public RocketBoosterItem() {
        super(new Properties().tab(Goety.TAB).durability(256));
    }

    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == Items.FIRE_CHARGE;
    }

    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        PlayerEntity playerIn = context.getPlayer();
        if (!world.isClientSide) {
            ItemStack itemstack = context.getItemInHand();
            Vector3d vector3d = context.getClickLocation();
            Direction direction = context.getClickedFace();
            FireworkRocketEntity fireworkrocketentity = new FireworkRocketEntity(world, context.getPlayer(), vector3d.x + (double)direction.getStepX() * 0.15D, vector3d.y + (double)direction.getStepY() * 0.15D, vector3d.z + (double)direction.getStepZ() * 0.15D, itemstack);
            world.addFreshEntity(fireworkrocketentity);
        }

        return ActionResultType.sidedSuccess(world.isClientSide);
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (playerIn.isFallFlying()) {
            if (!worldIn.isClientSide) {
                worldIn.addFreshEntity(new FireworkRocketEntity(worldIn, itemstack, playerIn));
            }
            itemstack.hurtAndBreak(1, playerIn, (player) ->
                    player.broadcastBreakEvent(player.getUsedItemHand()));
            return ActionResult.sidedSuccess(playerIn.getItemInHand(handIn), worldIn.isClientSide());
        } else {
            itemstack.hurtAndBreak(1, playerIn, (player) ->
                    player.broadcastBreakEvent(player.getUsedItemHand()));
            return ActionResult.pass(playerIn.getItemInHand(handIn));
        }
    }
}
