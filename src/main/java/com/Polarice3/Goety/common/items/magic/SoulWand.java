package com.Polarice3.Goety.common.items.magic;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.ally.RottreantEntity;
import com.Polarice3.Goety.common.entities.ally.SummonedEntity;
import com.Polarice3.Goety.common.entities.ally.UndeadWolfEntity;
import com.Polarice3.Goety.common.items.capability.SoulUsingItemCapability;
import com.Polarice3.Goety.common.items.handler.SoulUsingItemHandler;
import com.Polarice3.Goety.common.magic.ChargingSpells;
import com.Polarice3.Goety.common.magic.InstantCastSpells;
import com.Polarice3.Goety.common.magic.Spells;
import com.Polarice3.Goety.common.magic.SpewingSpell;
import com.Polarice3.Goety.common.magic.spells.BreathSpell;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import com.Polarice3.Goety.utils.SEHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.village.GossipType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Learned item capabilities from codes made by @vemerion & @MrCrayfish
 */
public class SoulWand extends Item{
    private static final String SOULUSE = "Soul Use";
    private static final String CASTTIME = "Cast Time";
    private static final String SOULCOST = "Soul Cost";
    private static final String DURATION = "Duration";
    private static final String COOLDOWN = "Cooldown";
    private static final String COOL = "Cool";

    public SoulWand() {
        super(new Properties().tab(Goety.TAB).stacksTo(1).setNoRepair().rarity(Rarity.RARE));
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entityIn;
            CompoundNBT compound = stack.getOrCreateTag();
            if (stack.getTag() == null) {
                compound.putInt(SOULUSE, SoulUse(livingEntity, stack));
                compound.putInt(SOULCOST, 0);
                compound.putInt(CASTTIME, CastTime(livingEntity, stack));
                compound.putInt(COOL, 0);
            }
            if (this.getSpell(stack) != null) {
                this.setSpellConditions(this.getSpell(stack), stack);
            } else {
                this.setSpellConditions(null, stack);
            }
            compound.putInt(SOULUSE, SoulUse(livingEntity, stack));
            compound.putInt(CASTTIME, CastTime(livingEntity, stack));
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void onCraftedBy(ItemStack pStack, World pLevel, PlayerEntity pPlayer) {
        CompoundNBT compound = pStack.getOrCreateTag();
        compound.putInt(SOULUSE, SoulUse(pPlayer, pStack));
        compound.putInt(SOULCOST, 0);
        compound.putInt(CASTTIME, CastTime(pPlayer, pStack));
        compound.putInt(COOL, 0);
        this.setSpellConditions(null, pStack);
    }

    public boolean SoulDiscount(LivingEntity entityLiving){
        return RobeArmorFinder.FindArmor(entityLiving);
    }

    public boolean SoulCostUp(LivingEntity entityLiving){
        return entityLiving.hasEffect(ModEffects.SUMMONDOWN.get());
    }

    public boolean ReduceCastTime(LivingEntity entityLiving){
        return RobeArmorFinder.FindHelm(entityLiving);
    }

    public int SoulCalculation(LivingEntity entityLiving, ItemStack stack){
        if (SoulCostUp(entityLiving)){
            int amp = Objects.requireNonNull(entityLiving.getEffect(ModEffects.SUMMONDOWN.get())).getAmplifier() + 2;
            return SoulCost(stack) * amp;
        } else if (SoulDiscount(entityLiving)){
            return SoulCost(stack)/2;
        } else {
            return SoulCost(stack);
        }
    }

    public int SoulUse(LivingEntity entityLiving, ItemStack stack){
        if (getFocus(stack).isEnchanted()){
            return SoulCalculation(entityLiving, stack) * 2;
        } else {
            return SoulCalculation(entityLiving, stack);
        }
    }

    public int CastTime(LivingEntity entityLiving, ItemStack stack){
        if (ReduceCastTime(entityLiving)){
            return CastDuration(stack)/2;
        } else {
            return CastDuration(stack);
        }
    }

    @Nonnull
    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand){
        if (target instanceof SummonedEntity){
            SummonedEntity summonedEntity = (SummonedEntity) target;
            if (summonedEntity.getTrueOwner() == player){
                if (player.isShiftKeyDown() || player.isCrouching()){
                    summonedEntity.kill();
                } else {
                    if (summonedEntity.getMobType() == CreatureAttribute.UNDEAD && !(summonedEntity instanceof UndeadWolfEntity)) {
                        summonedEntity.updateMoveMode(player);
                    }
                    if (summonedEntity instanceof RottreantEntity){
                        summonedEntity.updateMoveMode(player);
                    }
                }
                return ActionResultType.SUCCESS;
            }
        }
        return super.interactLivingEntity(stack, player, target, hand);

    }

    public void onUseTick(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        if (!(this.getSpell(stack) instanceof InstantCastSpells)) {
            SoundEvent soundevent = this.CastingSound(stack);
            int CastTime = stack.getUseDuration() - count;
            if (CastTime == 1) {
                if (soundevent != null) {
                    worldIn.playSound(null, livingEntityIn.getX(), livingEntityIn.getY(), livingEntityIn.getZ(), soundevent, SoundCategory.PLAYERS, 0.5F, 1.0F);
                } else {
                    worldIn.playSound(null, livingEntityIn.getX(), livingEntityIn.getY(), livingEntityIn.getZ(), SoundEvents.EVOKER_PREPARE_ATTACK, SoundCategory.PLAYERS, 0.5F, 1.0F);
                }
            }
            if (this.getSpell(stack) instanceof ChargingSpells) {
                if (stack.getTag() != null) {
                    stack.getTag().putInt(COOL, stack.getTag().getInt(COOL) + 1);
                    if (stack.getTag().getInt(COOL) > Cooldown(stack)) {
                        stack.getTag().putInt(COOL, 0);
                        this.MagicResults(stack, worldIn, livingEntityIn);
                    }
                }
            }
        }
    }

    public int getUseDuration(ItemStack stack) {
        if (stack.getTag() != null) {
            return stack.getTag().getInt(CASTTIME);
        } else {
            return this.CastDuration(stack);
        }
    }

    @Nonnull
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    @Nonnull
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        super.finishUsingItem(stack, worldIn, entityLiving);
        if (!(this.getSpell(stack) instanceof ChargingSpells) || !(this.getSpell(stack) instanceof InstantCastSpells)){
            this.MagicResults(stack, worldIn, entityLiving);
        }
        if (stack.getTag() != null) {
            if (stack.getTag().getInt(COOL) > 0) {
                stack.getTag().putInt(COOL, 0);
            }
        }
        return stack;
    }

    @Nonnull
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (this.getSpell(itemstack) != null) {
            if (!(this.getSpell(itemstack) instanceof InstantCastSpells)){
                playerIn.startUsingItem(handIn);
                if (worldIn.isClientSide){
                    this.useParticles(worldIn, playerIn);
                }
            } else {
                this.MagicResults(itemstack, worldIn, playerIn);
            }
        }

        return ActionResult.consume(itemstack);

    }

    @OnlyIn(Dist.CLIENT)
    public void useParticles(World worldIn, PlayerEntity playerIn){
        for (int i = 0; i < playerIn.level.random.nextInt(35) + 10; ++i) {
            double d = worldIn.random.nextGaussian() * 0.2D;
            worldIn.addParticle(ParticleTypes.ENTITY_EFFECT, playerIn.getX(), playerIn.getBoundingBox().maxY + 0.5D, playerIn.getZ(), d, d, d);
        }
    }

    public void setSpellConditions(Spells spell, ItemStack stack){
        if (stack.getTag() != null) {
            if (spell != null) {
                stack.getTag().putInt(SOULCOST, spell.SoulCost());
                stack.getTag().putInt(DURATION, spell.CastDuration());
                if (spell instanceof ChargingSpells) {
                    stack.getTag().putInt(COOLDOWN, ((ChargingSpells) spell).Cooldown());
                } else {
                    stack.getTag().putInt(COOLDOWN, 0);
                }
            } else {
                stack.getTag().putInt(SOULCOST, 0);
                stack.getTag().putInt(DURATION, 0);
                stack.getTag().putInt(COOLDOWN, 0);
            }
        }
    }

    public Spells getSpell(ItemStack stack){
        if (getMagicFocus(stack) != null && getMagicFocus(stack).getSpell() != null){
            return getMagicFocus(stack).getSpell();
        } else {
            return null;
        }
    }

    public int SoulCost(ItemStack itemStack) {
        if (itemStack.getTag() == null){
            return 0;
        } else {
            return itemStack.getTag().getInt(SOULCOST);
        }
    }

    public int CastDuration(ItemStack itemStack) {
        if (itemStack.getTag() != null) {
            return itemStack.getTag().getInt(DURATION);
        } else {
            return 0;
        }
    }

    public int Cooldown(ItemStack itemStack) {
        if (itemStack.getTag() != null) {
            return itemStack.getTag().getInt(COOLDOWN);
        } else {
            return 0;
        }
    }

    public SoundEvent CastingSound(ItemStack stack) {
        if (this.getSpell(stack) != null) {
            return this.getSpell(stack).CastingSound();
        } else {
            return null;
        }
    }

    public static ItemStack getFocus(ItemStack itemstack) {
        SoulUsingItemHandler handler = SoulUsingItemHandler.get(itemstack);
        return handler.getSlot();
    }

    public static MagicFocusItem getMagicFocus(ItemStack itemStack){
        if (getFocus(itemStack) != null && !getFocus(itemStack).isEmpty() && getFocus(itemStack).getItem() instanceof MagicFocusItem){
            return (MagicFocusItem) getFocus(itemStack).getItem();
        } else {
            return null;
        }
    }

    public static Map<Enchantment, Integer> getFocusEnchantments(ItemStack itemStack){
        SoulUsingItemHandler handler = SoulUsingItemHandler.get(itemStack);
        return EnchantmentHelper.getEnchantments(handler.getSlot());
    }

    public void MagicResults(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        PlayerEntity playerEntity = (PlayerEntity) entityLiving;
        if (!worldIn.isClientSide) {
            ServerWorld serverWorld = (ServerWorld) worldIn;
            if (this.getSpell(stack) != null) {
                if (playerEntity.isCreative()){
                    assert stack.getTag() != null;
                    if (stack.getItem() instanceof SoulStaff){
                        this.getSpell(stack).StaffResult(serverWorld, entityLiving);
                    } else {
                        this.getSpell(stack).WandResult(serverWorld, entityLiving);
                    }
                } else if (SEHelper.getSoulsAmount(playerEntity, SoulUse(entityLiving, stack))) {
                    boolean spent = true;
                    int random = worldIn.random.nextInt(4);
                    if (this.getSpell(stack) instanceof SpewingSpell || this.getSpell(stack) instanceof BreathSpell) {
                        if (random != 0) {
                            spent = false;
                        }
                    }
                    if (spent){
                        SEHelper.decreaseSouls(playerEntity, SoulUse(entityLiving, stack));
                        SEHelper.sendSEUpdatePacket(playerEntity);
                        if (MainConfig.VillagerHateSpells.get() > 0) {
                            for (VillagerEntity villager : entityLiving.level.getEntitiesOfClass(VillagerEntity.class, entityLiving.getBoundingBox().inflate(16.0D))) {
                                villager.getGossips().add(entityLiving.getUUID(), GossipType.MINOR_NEGATIVE, MainConfig.VillagerHateSpells.get());
                            }
                        }
                    }
                    assert stack.getTag() != null;
                    if (stack.getItem() instanceof SoulStaff){
                        this.getSpell(stack).StaffResult(serverWorld, entityLiving);
                    } else {
                        this.getSpell(stack).WandResult(serverWorld, entityLiving);
                    }
                } else {
                    worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                }
            } else {
                worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
        }
        if (worldIn.isClientSide){
            if (this.getSpell(stack) != null) {
                if (playerEntity.isCreative()){
                    if (this.getSpell(stack) instanceof SpewingSpell){
                        SpewingSpell spewingSpell = (SpewingSpell) this.getSpell(stack);
                        spewingSpell.showWandBreath(entityLiving);
                    }
                } else if (SEHelper.getSoulsAmount(playerEntity, SoulUse(entityLiving, stack))) {
                    if (this.getSpell(stack) instanceof SpewingSpell){
                        SpewingSpell spewingSpell = (SpewingSpell) this.getSpell(stack);
                        spewingSpell.showWandBreath(entityLiving);
                    }
                } else {
                    this.failParticles(worldIn, entityLiving);
                }
            } else {
                this.failParticles(worldIn, entityLiving);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void failParticles(World worldIn, LivingEntity entityLiving){
        for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
            double d = worldIn.random.nextGaussian() * 0.2D;
            worldIn.addParticle(ParticleTypes.CLOUD, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), d, d, d);
        }
    }

    /**
     * Found Creative Server Bug fix from @mraof's Minestuck Music Player Weapon code.
     */
    private static IItemHandler getItemHandler(ItemStack itemStack) {
        return itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseThrow(() ->
                new IllegalArgumentException("Expected an item handler for the Magic Focus item, but " + itemStack + " does not expose an item handler."));
    }

    public CompoundNBT getShareTag(ItemStack stack) {
        IItemHandler iitemHandler = getItemHandler(stack);
        CompoundNBT nbt = stack.getTag() != null ? stack.getTag() : new CompoundNBT();
        if(iitemHandler instanceof ItemStackHandler) {
            ItemStackHandler itemHandler = (ItemStackHandler) iitemHandler;
            nbt.put("cap", itemHandler.serializeNBT());
        }
        return nbt;
    }

    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        if(nbt == null) {
            stack.setTag(null);
        } else {
            IItemHandler iitemHandler = getItemHandler(stack);
            if(iitemHandler instanceof ItemStackHandler) {
                ItemStackHandler itemHandler = (ItemStackHandler) iitemHandler;
                itemHandler.deserializeNBT(nbt.getCompound("cap"));
            }
            stack.setTag(nbt);
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged) && slotChanged;
    }

    @Override
    @Nullable
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundNBT nbt) {
        return new SoulUsingItemCapability(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (stack.getTag() != null) {
            int SoulUse = stack.getTag().getInt(SOULUSE);
            tooltip.add(new TranslationTextComponent("info.goety.soulitems.cost", SoulUse));
        } else {
            tooltip.add(new TranslationTextComponent("info.goety.soulitems.cost", SoulCost(stack)));
        }
        if (!getFocus(stack).isEmpty()){
            tooltip.add(new TranslationTextComponent("info.goety.soulitems.focus", getFocus(stack).getItem().getDescription()));
        } else {
            tooltip.add(new TranslationTextComponent("info.goety.soulitems.focus", "Empty"));
        }
    }
}
