package com.Polarice3.Goety.common.items.magic;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.api.entities.ally.IServant;
import com.Polarice3.Goety.api.items.magic.IFocus;
import com.Polarice3.Goety.api.magic.*;
import com.Polarice3.Goety.common.blocks.tiles.ArcaTileEntity;
import com.Polarice3.Goety.common.items.capability.SoulUsingItemCapability;
import com.Polarice3.Goety.common.items.handler.SoulUsingItemHandler;
import com.Polarice3.Goety.common.magic.spells.ender.RecallSpell;
import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.network.packets.server.SPlayEntitySoundPacket;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModTags;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import com.Polarice3.Goety.utils.SEHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.village.GossipType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
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
    private static final String SECONDS = "Seconds";

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
                compound.putInt(CASTTIME, CastDuration(stack));
                compound.putInt(COOL, 0);
                compound.putInt(SECONDS, 0);
            }
            if (this.getSpell(stack) != null) {
                this.setSpellConditions(this.getSpell(stack), stack, livingEntity);
            } else {
                this.setSpellConditions(null, stack, livingEntity);
            }
            compound.putInt(SOULUSE, SoulUse(livingEntity, stack));
            compound.putInt(CASTTIME, CastDuration(stack));
            if (getFocus(stack) != null){
                getFocus(stack).inventoryTick(worldIn, entityIn, itemSlot, isSelected);
            }
        }
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void onCraftedBy(ItemStack pStack, World pLevel, PlayerEntity pPlayer) {
        CompoundNBT compound = pStack.getOrCreateTag();
        compound.putInt(SOULUSE, SoulUse(pPlayer, pStack));
        compound.putInt(SOULCOST, 0);
        compound.putInt(CASTTIME, CastDuration(pStack));
        compound.putInt(COOL, 0);
        compound.putInt(SECONDS, 0);
        this.setSpellConditions(null, pStack, pPlayer);
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

    public boolean cannotCast(LivingEntity livingEntity, ItemStack stack){
        boolean flag = false;
        if (livingEntity.level instanceof ServerWorld){
            ServerWorld serverLevel = (ServerWorld) livingEntity.level;
            if (this.getSpell(stack) != null){
                if (!this.getSpell(stack).conditionsMet(serverLevel, livingEntity)){
                    flag = true;
                }
            }
        }
        return this.isOnCooldown(livingEntity, stack) || flag;
    }

    public boolean isOnCooldown(LivingEntity livingEntity, ItemStack stack){
        if (livingEntity instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) livingEntity;
            if (getFocus(stack) != null){
                Item item = getFocus(stack).getItem();
                return SEHelper.getFocusCoolDown(player).isOnCooldown(item);
            }
        }
        return false;
    }

    public boolean isNotInstant(ISpell spells){
        return spells != null && spells.defaultCastDuration() > 0;
    }

    public boolean notTouch(ISpell spells){
        return !(spells instanceof ITouchSpell) && !(spells instanceof IBlockSpell);
    }

    @Nonnull
    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand){
        if (!SpellConfig.OwnerHitCommand.get()) {
            if (target instanceof IServant) {
                IServant summonedEntity = (IServant) target;
                if (summonedEntity.getTrueOwner() == player || (summonedEntity.getTrueOwner() instanceof IOwned && ((IOwned) summonedEntity.getTrueOwner()).getTrueOwner() == player)) {
                    if (player.isShiftKeyDown() || player.isCrouching()) {
                        target.kill();
                    } else {
                        if (summonedEntity.canUpdateMove()) {
                            summonedEntity.updateMoveMode(player);
                        }
                    }
                    return ActionResultType.SUCCESS;
                }
            }
        }
        if (this.getSpell(stack) instanceof ITouchSpell){
            ITouchSpell touchSpells = (ITouchSpell) this.getSpell(stack);
            if (this.canCastTouch(stack, player.level, player)) {
                if (player.level instanceof ServerWorld) {
                    ServerWorld serverLevel = (ServerWorld) player.level;
                    touchSpells.touchResult(serverLevel, player, target);
                }
            }
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }

    public ActionResultType useOn(ItemUseContext pContext) {
        World level = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        PlayerEntity player = pContext.getPlayer();
        ItemStack stack = pContext.getItemInHand();
        if (player != null) {
            if (getFocus(stack).getItem() instanceof RecallFocus){
                RecallFocus recallFocus = (RecallFocus) getFocus(stack).getItem();
                CompoundNBT compoundTag = getFocus(stack).getOrCreateTag();
                if (!RecallFocus.hasRecall(getFocus(stack))){
                    TileEntity tileEntity = level.getBlockEntity(blockpos);
                    if (tileEntity instanceof ArcaTileEntity) {
                        ArcaTileEntity arcaTile = (ArcaTileEntity) tileEntity;
                        if (pContext.getPlayer() == arcaTile.getPlayer() && arcaTile.getLevel() != null) {
                            recallFocus.addRecallTags(arcaTile.getLevel().dimension(), arcaTile.getBlockPos(), compoundTag);
                            getFocus(stack).setTag(compoundTag);
                            player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                            if (!level.isClientSide) {
                                ModNetwork.sendTo(player, new SPlayEntitySoundPacket(player.getUUID(), SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                            }
                            return ActionResultType.sidedSuccess(level.isClientSide);
                        }
                    }
                    BlockState blockstate = level.getBlockState(blockpos);
                    if (blockstate.is(ModTags.Blocks.RECALL_BLOCKS)) {
                        recallFocus.addRecallTags(level.dimension(), blockpos, compoundTag);
                        getFocus(stack).setTag(compoundTag);
                        player.playSound(SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F);
                        if (!level.isClientSide) {
                            ModNetwork.sendTo(player, new SPlayEntitySoundPacket(player.getUUID(), SoundEvents.ARROW_HIT_PLAYER, 1.0F, 0.45F));
                        }
                        return ActionResultType.sidedSuccess(level.isClientSide);
                    }
                }
            } else if (this.getSpell(stack) instanceof IBlockSpell){
                IBlockSpell blockSpells = (IBlockSpell) this.getSpell(stack);
                if (player.level instanceof ServerWorld) {
                    ServerWorld serverLevel = (ServerWorld) player.level;
                    if (blockSpells.rightBlock(serverLevel, player, blockpos)) {
                        if (this.canCastTouch(stack, level, player)) {
                            blockSpells.blockResult(serverLevel, player, blockpos);
                        }
                        return ActionResultType.SUCCESS;
                    }
                }
            }
        }
        return super.useOn(pContext);
    }

    public void onUseTick(World worldIn, LivingEntity livingEntityIn, ItemStack stack, int count) {
        if (this.cannotCast(livingEntityIn, stack)){
            return;
        }
        if (this.getSpell(stack) != null && this.isNotInstant(this.getSpell(stack))) {
            SoundEvent soundevent = this.CastingSound(stack);
            int CastTime = stack.getUseDuration() - count;
            if (CastTime == 1 && soundevent != null) {
                worldIn.playSound(null, livingEntityIn.getX(), livingEntityIn.getY(), livingEntityIn.getZ(), soundevent, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }
            if (this.getSpell(stack) instanceof RecallSpell){
                for(int i = 0; i < 2; ++i) {
                    worldIn.addParticle(ParticleTypes.PORTAL, livingEntityIn.getRandomX(0.5D), livingEntityIn.getRandomY() - 0.25D, livingEntityIn.getRandomZ(0.5D), (worldIn.random.nextDouble() - 0.5D) * 2.0D, -worldIn.random.nextDouble(), (worldIn.random.nextDouble() - 0.5D) * 2.0D);
                }
            } else if (!(this.getSpell(stack) instanceof IChargingSpell)) {
                this.useParticles(worldIn, livingEntityIn, this.getSpell(stack));
            }
            if (this.getSpell(stack) instanceof IChargingSpell) {
                if (stack.getTag() != null) {
                    stack.getTag().putInt(COOL, stack.getTag().getInt(COOL) + 1);
                    if (stack.getTag().getInt(COOL) > Cooldown(stack)) {
                        stack.getTag().putInt(COOL, 0);
                        this.MagicResults(stack, worldIn, livingEntityIn);
                    }
                }

                if (livingEntityIn instanceof PlayerEntity){
                    PlayerEntity player = (PlayerEntity) livingEntityIn;
                    if (!SEHelper.getSoulsAmount(player, this.getSpell(stack).soulCost(player)) && !player.isCreative()){
                        player.stopUsingItem();
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
        if (this.getSpell(stack) != null) {
            if (!(this.getSpell(stack) instanceof IChargingSpell) || this.isNotInstant(this.getSpell(stack)) || this.notTouch(this.getSpell(stack))) {
                this.MagicResults(stack, worldIn, entityLiving);
            }
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
            if (this.cannotCast(playerIn, itemstack)){
                return ActionResult.pass(itemstack);
            } else if (this.isNotInstant(this.getSpell(itemstack))){
                if (SEHelper.getSoulsAmount(playerIn, this.getSpell(itemstack).soulCost(playerIn)) || playerIn.abilities.instabuild) {
                    if (!worldIn.isClientSide) {
                        playerIn.startUsingItem(handIn);
                    }
                }
            } else if (this.notTouch(this.getSpell(itemstack))){
                playerIn.swing(handIn);
                this.MagicResults(itemstack, worldIn, playerIn);
            }
        }

        return ActionResult.consume(itemstack);

    }

    public void useParticles(World worldIn, LivingEntity livingEntity, ISpell iSpell){
        double d0 = worldIn.random.nextGaussian() * 0.2D;
        double d1 = worldIn.random.nextGaussian() * 0.2D;
        double d2 = worldIn.random.nextGaussian() * 0.2D;
        if (iSpell != null){
            d0 = iSpell.particleColors(livingEntity).red();
            d1 = iSpell.particleColors(livingEntity).green();
            d2 = iSpell.particleColors(livingEntity).blue();
        }
        worldIn.addParticle(ParticleTypes.ENTITY_EFFECT, livingEntity.getX(), livingEntity.getBoundingBox().maxY + 0.5D, livingEntity.getZ(), d0, d1, d2);
    }

    public void setSpellConditions(@Nullable ISpell spell, ItemStack stack, LivingEntity livingEntity){
        if (stack.getTag() != null) {
            if (spell != null) {
                stack.getTag().putInt(SOULCOST, spell.soulCost(livingEntity));
                stack.getTag().putInt(DURATION, spell.castDuration(livingEntity));
                if (spell instanceof IChargingSpell) {
                    stack.getTag().putInt(COOLDOWN, ((IChargingSpell) spell).Cooldown());
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

    public ISpell getSpell(ItemStack stack){
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

    @Nullable
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

    public static IFocus getMagicFocus(ItemStack itemStack){
        if (getFocus(itemStack) != null && !getFocus(itemStack).isEmpty() && getFocus(itemStack).getItem() instanceof IFocus){
            return (IFocus) getFocus(itemStack).getItem();
        } else {
            return null;
        }
    }

    public boolean canCastTouch(ItemStack stack, World worldIn, LivingEntity caster){
        PlayerEntity playerEntity = (PlayerEntity) caster;
        if (!worldIn.isClientSide) {
            if (this.getSpell(stack) != null && !this.cannotCast(caster, stack)) {
                if (playerEntity.isCreative()){
                    SEHelper.addCooldown(playerEntity, getFocus(stack).getItem(), this.getSpell(stack).spellCooldown());
                    return stack.getTag() != null;
                } else if (SEHelper.getSoulsAmount(playerEntity, SoulUse(caster, stack))) {
                    if (stack.getTag() != null) {
                        SEHelper.decreaseSouls(playerEntity, SoulUse(caster, stack));
                        SEHelper.addCooldown(playerEntity, getFocus(stack).getItem(), this.getSpell(stack).spellCooldown());
                        SEHelper.sendSEUpdatePacket(playerEntity);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void MagicResults(ItemStack stack, World worldIn, LivingEntity caster) {
        PlayerEntity playerEntity = (PlayerEntity) caster;
        if (this.getSpell(stack) != null) {
            if (!worldIn.isClientSide) {
                ServerWorld serverWorld = (ServerWorld) worldIn;
                if (playerEntity.isCreative()) {
                    if (stack.getTag() != null) {
                        this.getSpell(stack).SpellResult(serverWorld, caster, stack.getItem() instanceof SoulStaff);
                        SEHelper.addCooldown(playerEntity, getFocus(stack).getItem(), this.getSpell(stack).spellCooldown());
                    }
                } else if (SEHelper.getSoulsAmount(playerEntity, SoulUse(caster, stack))) {
                    boolean spent = true;
                    if (this.getSpell(stack) instanceof IEverChargeSpell) {
                        if (stack.getTag() != null) {
                            stack.getTag().putInt(SECONDS, stack.getTag().getInt(SECONDS) + 1);
                            if (stack.getTag().getInt(SECONDS) != 20) {
                                spent = false;
                            } else {
                                stack.getTag().putInt(SECONDS, 0);
                            }
                        }
                    }
                    if (spent) {
                        SEHelper.decreaseSouls(playerEntity, SoulUse(caster, stack));
                        SEHelper.sendSEUpdatePacket(playerEntity);
                        if (MainConfig.VillagerHateSpells.get() > 0) {
                            for (VillagerEntity villager : caster.level.getEntitiesOfClass(VillagerEntity.class, caster.getBoundingBox().inflate(16.0D))) {
                                if (villager.canSee(caster)) {
                                    villager.getGossips().add(caster.getUUID(), GossipType.MINOR_NEGATIVE, MainConfig.VillagerHateSpells.get());
                                }
                            }
                        }
                    }
                    if (stack.getTag() != null) {
                        this.getSpell(stack).SpellResult(serverWorld, caster, stack.getItem() instanceof SoulStaff);
                        SEHelper.addCooldown(playerEntity, getFocus(stack).getItem(), this.getSpell(stack).spellCooldown());
                    }
                } else {
                    worldIn.playSound(null, caster.getX(), caster.getY(), caster.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                }
            }
            if (worldIn.isClientSide) {
                if (playerEntity.isCreative()) {
                    if (this.getSpell(stack) instanceof IBreathingSpell) {
                        IBreathingSpell breathingSpell = (IBreathingSpell) this.getSpell(stack);
                        breathingSpell.showWandBreath(caster);
                    }
                } else if (SEHelper.getSoulsAmount(playerEntity, SoulUse(caster, stack))) {
                    if (this.getSpell(stack) instanceof IBreathingSpell) {
                        IBreathingSpell breathingSpell = (IBreathingSpell) this.getSpell(stack);
                        breathingSpell.showWandBreath(caster);
                    }
                } else {
                    this.failParticles(worldIn, caster);
                }
            }
        } else {
            this.failParticles(worldIn, caster);
            worldIn.playSound(null, caster.getX(), caster.getY(), caster.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        }
    }

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
            tooltip.add(new TranslationTextComponent("info.goety.wand.cost", SoulUse));
            if (getSpell(stack) != null) {
                if (this.isNotInstant(this.getSpell(stack)) && !(getSpell(stack) instanceof IChargingSpell)) {
                    int CastTime = stack.getTag().getInt(CASTTIME);
                    tooltip.add(new TranslationTextComponent("info.goety.wand.castTime", CastTime / 20.0F));
                }
                if (getSpell(stack).spellCooldown() > 0){
                    tooltip.add(new TranslationTextComponent("info.goety.wand.coolDown", getSpell(stack).spellCooldown() / 20.0F));
                }
            }
        } else {
            tooltip.add(new TranslationTextComponent("info.goety.wand.cost", SoulCost(stack)));
        }
        if (!getFocus(stack).isEmpty()){
            tooltip.add(new TranslationTextComponent("info.goety.wand.focus", getFocus(stack).getItem().getDescription()));
            if (getFocus(stack).getItem() instanceof RecallFocus){
                ItemStack recallFocus = getFocus(stack);
                RecallFocus.addRecallText(recallFocus, tooltip);
            }
        } else {
            tooltip.add(new TranslationTextComponent("info.goety.wand.focus", "Empty"));
        }
    }
}
