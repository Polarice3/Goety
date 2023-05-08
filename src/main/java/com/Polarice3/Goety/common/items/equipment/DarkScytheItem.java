package com.Polarice3.Goety.common.items.equipment;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.neutral.IOwned;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.SEHelper;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class DarkScytheItem extends TieredItem implements IVanishable {
    private final float initialDamage;
    private final Multimap<Attribute, AttributeModifier> scytheAttributes;

    public DarkScytheItem(IItemTier itemTier) {
        super(itemTier, new Properties().durability(itemTier.getUses()).tab(Goety.TAB));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        this.initialDamage = MainConfig.ScytheBaseDamage.get().floatValue() + itemTier.getAttackDamageBonus();
        double attackSpeed = 4.0D - MainConfig.ScytheAttackSpeed.get();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", initialDamage - 1.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -attackSpeed, AttributeModifier.Operation.ADDITION));
        this.scytheAttributes = builder.build();
    }

    public float getInitialDamage() {
        return this.initialDamage;
    }

    public boolean getMineBlocks(BlockState pState){
        return pState.isToolEffective(ToolType.HOE) || BlockFinder.isScytheBreak(pState);
    }

    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        return pState.isToolEffective(ToolType.HOE) ? 8.0F : 1.0F;
    }

    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(1, pAttacker, (p_220045_0_) ->
                p_220045_0_.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
        if (pAttacker instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) pAttacker;
            this.attackMobs(pStack, pTarget, player);
        }
        return true;
    }

    public boolean mineBlock(ItemStack pStack, World pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (pState.getDestroySpeed(pLevel, pPos) != 0.0F) {
            pStack.hurtAndBreak(this.getMineBlocks(pState) ? 1 : 2, pEntityLiving, (p_220044_0_) ->
                    p_220044_0_.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
        }
        if (this.getMineBlocks(pState)){
            pLevel.playSound((PlayerEntity) null, pPos.getX(), pPos.getY(), pPos.getZ(), ModSounds.SCYTHE_HIT.get(), pEntityLiving.getSoundSource(), 1.0F, 1.0F);
            for (BlockPos blockPos : BlockFinder.multiBlockBreak(pEntityLiving, pPos, 2, 1, 2)){
                BlockState blockstate = pLevel.getBlockState(blockPos);
                if (this.getMineBlocks(blockstate)){
                    if (pLevel.destroyBlock(blockPos, true, pEntityLiving)){
                        if (blockstate.getDestroySpeed(pLevel, blockPos) != 0) {
                            pStack.hurtAndBreak(1, pEntityLiving, (p_220044_0_)
                                    -> p_220044_0_.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
                        }
                    }
                }
            }
        }

        return true;
    }

    public void attackMobs(ItemStack pStack, LivingEntity pTarget, PlayerEntity pPlayer){
        int enchantment = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SOUL_EATER.get(), pStack);
        int soulEater = MathHelper.clamp(enchantment + 1, 1, 10);
        SEHelper.increaseSouls(pPlayer, MainConfig.DarkScytheSouls.get() * soulEater);

        float f = (float)pPlayer.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float f1 = EnchantmentHelper.getDamageBonus(pPlayer.getMainHandItem(), pTarget.getMobType());
        float f2 = pPlayer.getAttackStrengthScale(0.5F);
        f = f * (0.2F + f2 * f2 * 0.8F);
        f1 = f1 * f2;
        f = f + f1;

        if (f > 0.5F || f1 > 0.5F) {
            float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(pPlayer) * f;
            int j = EnchantmentHelper.getFireAspect(pPlayer);
            double area = 1.0D;
            if (f2 > 0.9F) {
                area = 2.0D;
            }
            for (LivingEntity livingentity : pPlayer.level.getEntitiesOfClass(LivingEntity.class, pTarget.getBoundingBox().inflate(area, 0.25D, area))) {
                if (livingentity != pPlayer && livingentity != pTarget && !pPlayer.isAlliedTo(livingentity) && (!(livingentity instanceof ArmorStandEntity) || !((ArmorStandEntity) livingentity).isMarker()) && pPlayer.distanceToSqr(livingentity) < 16.0D && livingentity != pPlayer.getVehicle()) {
                    livingentity.knockback(0.4F, (double) MathHelper.sin(pPlayer.yRot * ((float) Math.PI / 180F)), (double) (-MathHelper.cos(pPlayer.yRot * ((float) Math.PI / 180F))));
                    if (livingentity.hurt(DamageSource.playerAttack(pPlayer), f3)) {
                        if (j > 0) {
                            livingentity.setSecondsOnFire(j * 4);
                        }
                        pStack.hurtAndBreak(1, pPlayer, (p_220045_0_) ->
                                p_220045_0_.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
                        if (livingentity instanceof IOwned){
                            if (((IOwned) livingentity).getTrueOwner() != pPlayer){
                                SEHelper.increaseSouls(pPlayer, MainConfig.DarkScytheSouls.get() * soulEater);
                            }
                        } else {
                            SEHelper.increaseSouls(pPlayer, MainConfig.DarkScytheSouls.get() * soulEater);
                        }
                        EnchantmentHelper.doPostHurtEffects(livingentity, pPlayer);
                        EnchantmentHelper.doPostDamageEffects(pPlayer, livingentity);
                    }
                }
            }
        }

        pPlayer.level.playSound((PlayerEntity)null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), ModSounds.SCYTHE_SWING.get(), pPlayer.getSoundSource(), 1.0F, 1.0F);
        pPlayer.sweepAttack();
    }

    public boolean isCorrectToolForDrops(BlockState pBlock) {
        return pBlock.getHarvestTool() == ToolType.HOE;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return (enchantment.category == EnchantmentType.VANISHABLE
                || enchantment.category == EnchantmentType.WEAPON
                || enchantment.category == EnchantmentType.BREAKABLE
                || enchantment == Enchantments.MOB_LOOTING
                || enchantment == Enchantments.BLOCK_FORTUNE);
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return equipmentSlot == EquipmentSlotType.MAINHAND ? this.scytheAttributes : super.getDefaultAttributeModifiers(equipmentSlot);
    }
}
