package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.projectiles.PitchforkEntity;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PitchforkItem extends Item implements IVanishable {
    private final Multimap<Attribute, AttributeModifier> pitchforkattribute;

    public PitchforkItem() {
        super(new Properties().durability(128).tab(Goety.TAB));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 6.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)-2.9F, AttributeModifier.Operation.ADDITION));
        this.pitchforkattribute = builder.build();
    }

    public boolean canAttackBlock(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return !player.isCreative();
    }

    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.SPEAR;
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity)entityLiving;
            int i = this.getUseDuration(stack) - timeLeft;
            if (i >= 10) {
                stack.hurtAndBreak(1, playerentity, (player) -> {
                    player.broadcastBreakEvent(entityLiving.getUsedItemHand());
                });
                PitchforkEntity pitchfork = new PitchforkEntity(worldIn, playerentity, stack);
                pitchfork.shootFromRotation(playerentity, playerentity.xRot, playerentity.yRot, 0.0F, 2.5F + (float)3 * 0.5F, 1.0F);
                if (playerentity.abilities.instabuild) {
                    pitchfork.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                }

                worldIn.addFreshEntity(pitchfork);
                worldIn.playSound((PlayerEntity)null, pitchfork, SoundEvents.TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                if (!playerentity.abilities.instabuild) {
                    playerentity.inventory.removeItem(stack);
                }
            }
        }
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return ActionResult.fail(itemstack);
        } else {
            playerIn.startUsingItem(handIn);
            return ActionResult.consume(itemstack);
        }
    }

    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        if (pState.is(Blocks.HAY_BLOCK)) {
            return 15.0F;
        } else {
            Material material = pState.getMaterial();
            return material != Material.PLANT && material != Material.REPLACEABLE_PLANT && material != Material.CORAL && !pState.is(BlockTags.LEAVES) && material != Material.VEGETABLE ? 1.0F : 2.0F;
        }
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (entity) -> {
            entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
        });
        return true;
    }

    public boolean mineBlock(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if ((double)state.getDestroySpeed(worldIn, pos) == 1.0D) {
            stack.hurtAndBreak(2, entityLiving, (entity) -> {
                entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
            });
        } else {
            stack.hurtAndBreak(1, entityLiving, (entity) -> {
                entity.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
            });
        }

        return true;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment.category == EnchantmentType.WEAPON || enchantment.category == EnchantmentType.BREAKABLE || enchantment == Enchantments.LOYALTY;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return equipmentSlot == EquipmentSlotType.MAINHAND ? this.pitchforkattribute : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    public int getEnchantmentValue() {
        return 1;
    }
}
