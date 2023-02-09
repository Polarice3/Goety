package com.Polarice3.Goety.common.items.magic;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.ally.RottreantEntity;
import com.Polarice3.Goety.common.entities.ally.SummonedEntity;
import com.Polarice3.Goety.common.entities.ally.UndeadWolfEntity;
import com.Polarice3.Goety.common.items.capability.SoulUsingItemCapability;
import com.Polarice3.Goety.common.items.handler.SoulUsingItemHandler;
import com.Polarice3.Goety.common.spells.*;
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
    private static final String SPELL = "Spell";
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
            if (getFocus(stack) != null && !getFocus(stack).isEmpty()) {
                this.ChangeFocus(stack);
            } else {
                compound.putInt(SPELL, -1);
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
        compound.putInt(SPELL, -1);
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

    public void ChangeFocus(ItemStack itemStack){
        if (!getFocus(itemStack).isEmpty() && getFocus(itemStack) != null) {
            String spell = getFocus(itemStack).getDescriptionId();
            if (spell.contains("vexing")) {
                this.setSpellConditions(new VexSpell(), itemStack);
                this.setSpell(0, itemStack);
            } else if (spell.contains("biting")) {
                this.setSpellConditions(new FangSpell(), itemStack);
                this.setSpell(1, itemStack);
            } else if (spell.contains("roaring")) {
                this.setSpellConditions(new RoarSpell(), itemStack);
                this.setSpell(2, itemStack);
            } else if (spell.contains("rotting")) {
                this.setSpellConditions(new ZombieSpell(), itemStack);
                this.setSpell(3, itemStack);
            } else if (spell.contains("osseous")) {
                this.setSpellConditions(new SkeletonSpell(), itemStack);
                this.setSpell(4, itemStack);
            } else if (spell.contains("witchgale")) {
                this.setSpellConditions(new WitchGaleSpell(), itemStack);
                this.setSpell(5, itemStack);
            } else if (spell.contains("spiderling")) {
                this.setSpellConditions(new SpiderlingSpell(), itemStack);
                this.setSpell(6, itemStack);
            } else if (spell.contains("brain")) {
                this.setSpellConditions(new BrainEaterSpell(), itemStack);
                this.setSpell(7, itemStack);
            } else if (spell.contains("teleport")) {
                this.setSpellConditions(new TeleportSpell(), itemStack);
                this.setSpell(8, itemStack);
            } else if (spell.contains("soulskull")) {
                this.setSpellConditions(new SoulSkullSpell(), itemStack);
                this.setSpell(9, itemStack);
            } else if (spell.contains("feast")) {
                this.setSpellConditions(new FeastSpell(), itemStack);
                this.setSpell(10, itemStack);
            } else if (spell.contains("tempting")) {
                this.setSpellConditions(new TemptingSpell(), itemStack);
                this.setSpell(11, itemStack);
            } else if (spell.contains("dragon")) {
                this.setSpellConditions(new DragonFireballSpell(), itemStack);
                this.setSpell(12, itemStack);
            } else if (spell.contains("creeperling")) {
                this.setSpellConditions(new CreeperlingSpell(), itemStack);
                this.setSpell(13, itemStack);
            } else if (spell.contains("airbreath")) {
                this.setSpellConditions(new BreathSpell(), itemStack);
                this.setSpell(14, itemStack);
            } else if (spell.contains("fireball")) {
                this.setSpellConditions(new FireballSpell(), itemStack);
                this.setSpell(15, itemStack);
            } else if (spell.contains("lavaball")) {
                this.setSpellConditions(new LavaballSpell(), itemStack);
                this.setSpell(16, itemStack);
            } else if (spell.contains("poisonball")) {
                this.setSpellConditions(new PoisonBallSpell(), itemStack);
                this.setSpell(17, itemStack);
            } else if (spell.contains("illusion")) {
                this.setSpellConditions(new IllusionSpell(), itemStack);
                this.setSpell(18, itemStack);
            } else if (spell.contains("phantasm")) {
                this.setSpellConditions(new PhantomSpell(), itemStack);
                this.setSpell(19, itemStack);
            } else if (spell.contains("firebreath")) {
                this.setSpellConditions(new FireBreathSpell(), itemStack);
                this.setSpell(20, itemStack);
            } else if (spell.contains("soullight")) {
                this.setSpellConditions(new SoulLightSpell(), itemStack);
                this.setSpell(21, itemStack);
            } else if (spell.contains("glowlight")) {
                this.setSpellConditions(new GlowLightSpell(), itemStack);
                this.setSpell(22, itemStack);
            } else if (spell.contains("icestorm")) {
                this.setSpellConditions(new IceStormSpell(), itemStack);
                this.setSpell(23, itemStack);
            } else if (spell.contains("frostbreath")) {
                this.setSpellConditions(new FrostBreathSpell(), itemStack);
                this.setSpell(24, itemStack);
            } else if (spell.contains("hounding")) {
                this.setSpellConditions(new UndeadWolfSpell(), itemStack);
                this.setSpell(25, itemStack);
            } else if (spell.contains("launch")) {
                this.setSpellConditions(new LaunchSpell(), itemStack);
                this.setSpell(26, itemStack);
            } else if (spell.contains("sonicboom")) {
                this.setSpellConditions(new SonicBoomSpell(), itemStack);
                this.setSpell(27, itemStack);
            } else if (spell.contains("rigid")) {
                this.setSpellConditions(new DredenSpell(), itemStack);
                this.setSpell(28, itemStack);
            } else if (spell.contains("enderacid")) {
                this.setSpellConditions(new AcidBreathSpell(), itemStack);
                this.setSpell(29, itemStack);
            } else if (spell.contains("spooky")) {
                this.setSpellConditions(new WraithSpell(), itemStack);
                this.setSpell(30, itemStack);
            } else if (spell.contains("iceology")) {
                this.setSpellConditions(new IceChunkSpell(), itemStack);
                this.setSpell(31, itemStack);
            }
        } else {
            this.setSpellConditions(null, itemStack);
            this.setSpell(-1, itemStack);
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

    public void setSpell(int spellint, ItemStack stack) {
        if (stack.getTag() != null) {
            stack.getTag().putInt(SPELL, spellint);
        }
    }

    public Spells getSpell(ItemStack stack){
        if (stack.getTag() != null) {
            return new CastSpells(stack.getTag().getInt(SPELL)).getSpell();
        } else {
            return new CastSpells(-1).getSpell();
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

    public CompoundNBT getShareTag(ItemStack stack) {
        CompoundNBT result = new CompoundNBT();
        CompoundNBT tag = super.getShareTag(stack);
        CompoundNBT cap = SoulUsingItemHandler.get(stack).serializeNBT();
        if (tag != null) {
            result.put("tag", tag);
        }
        if (cap != null) {
            result.put("cap", cap);
        }
        return result;
    }

    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        if (nbt != null) {
            if (nbt.contains("tag")) {
                stack.setTag(nbt.getCompound("tag"));
            }
            if (nbt.contains("cap")) {
                SoulUsingItemHandler.get(stack).deserializeNBT(nbt.getCompound("cap"));
            }
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
