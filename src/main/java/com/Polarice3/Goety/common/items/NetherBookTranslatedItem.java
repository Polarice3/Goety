package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.utils.ConstantPaths;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class NetherBookTranslatedItem extends Item {
    public NetherBookTranslatedItem(){
        super(new Properties()
                .tab(Goety.TAB)
                .rarity(Rarity.EPIC)
                .setNoRepair()
                .stacksTo(1)
                .fireResistant()
        );
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        playerIn.startUsingItem(handIn);
        return ActionResult.consume(itemstack);
    }

    public void onUseTick(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        if (!worldIn.isClientSide){
            ServerWorld serverWorld = (ServerWorld) worldIn;
            serverWorld.sendParticles(ParticleTypes.ENCHANT, livingEntityIn.getRandomX(0.5F), livingEntityIn.getY(), livingEntityIn.getRandomZ(0.5F), 1, 0.0F, 0.0F, 0.0F, 0);
            serverWorld.sendParticles(ParticleTypes.WITCH, livingEntityIn.getX(), livingEntityIn.getY(), livingEntityIn.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
        }
    }

    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        super.finishUsingItem(stack, worldIn, entityLiving);
        if (!worldIn.isClientSide){
            if (entityLiving instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entityLiving;
                CompoundNBT playerData = player.getPersistentData();
                CompoundNBT data;

                if (!playerData.contains(PlayerEntity.PERSISTED_NBT_TAG)) {
                    data = new CompoundNBT();
                } else {
                    data = playerData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
                }

                if (!data.getBoolean(ConstantPaths.readNetherBook())) {
                    data.putBoolean(ConstantPaths.readNetherBook(), true);
                    playerData.put(PlayerEntity.PERSISTED_NBT_TAG, data);
                    if (player instanceof ServerPlayerEntity) {
                        CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity) player, stack);
                    }
                    player.displayClientMessage(new TranslationTextComponent("info.goety.research.nether"), true);
                    stack.shrink(1);
                }
            }
        }
        return stack;
    }

    public int getUseDuration(ItemStack stack) {
        return 25;
    }

    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }
}
