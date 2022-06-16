package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.DeadSandExplosion;
import com.Polarice3.Goety.utils.ExplosionUtil;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class CorruptStaff extends Item {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public CorruptStaff() {
        super(new Properties().tab(Goety.TAB).stacksTo(1).setNoRepair().rarity(Rarity.RARE));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 5.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)-2.4F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlotType.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    @Override
    public void inventoryTick(ItemStack pStack, World pLevel, Entity pEntity, int pInventorySlot, boolean pIsCurrentItem) {
        if (pIsCurrentItem){
            if (pEntity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) pEntity;
                livingEntity.addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 1200));
            }
        }
        super.inventoryTick(pStack, pLevel, pEntity, pInventorySlot, pIsCurrentItem);
    }

    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity) {
            DeadSandExplosion.Mode mode = MainConfig.DeadSandSpread.get() ? DeadSandExplosion.Mode.SPREAD: DeadSandExplosion.Mode.NONE;
            ExplosionUtil.deadSandExplode(worldIn, entityLiving,
                    entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(),
                    7.0F, mode);
            stack.shrink(1);
        }
        return stack;
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.startUsingItem(handIn);
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        return ActionResult.consume(itemstack);
    }

    public void onUseTick(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        if (!worldIn.isClientSide) {
            int CastTime = stack.getUseDuration() - count;
            if (CastTime == 1) {
                worldIn.playSound(null, livingEntityIn.getX(), livingEntityIn.getY(), livingEntityIn.getZ(), SoundEvents.EVOKER_PREPARE_ATTACK, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }
        }
    }

    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }
}
