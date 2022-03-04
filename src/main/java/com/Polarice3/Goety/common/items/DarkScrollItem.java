package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.bosses.VizierEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class DarkScrollItem extends Item {
    public DarkScrollItem() {
        super(new Properties().tab(Goety.TAB).stacksTo(1));
    }

    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        super.finishUsingItem(stack, worldIn, entityLiving);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.RAID_HORN, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        VizierEntity vizier = new VizierEntity(ModEntityType.VIZIER.get(), worldIn);
        vizier.setPos(entityLiving.getX(), entityLiving.getY(), entityLiving.getZ());
        vizier.setItemInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
        worldIn.addFreshEntity(vizier);
        stack.setCount(0);
        return stack;
    }

    public int getUseDuration(ItemStack stack) {
        return 25;
    }

    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        playerIn.startUsingItem(handIn);
        new ParticleUtil(ParticleTypes.ANGRY_VILLAGER, playerIn.getX(), playerIn.getY(), playerIn.getZ(), 0.0F, 0.0F, 0.0F);
        return ActionResult.consume(itemstack);
    }
}
