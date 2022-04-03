package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.blocks.CursedCageBlock;
import com.Polarice3.Goety.common.enchantments.ModEnchantmentsType;
import com.Polarice3.Goety.common.entities.ally.FriendlyVexEntity;
import com.Polarice3.Goety.common.entities.ally.SummonedEntity;
import com.Polarice3.Goety.common.entities.neutral.MutatedEntity;
import com.Polarice3.Goety.utils.GoldTotemFinder;
import com.Polarice3.Goety.init.ModRegistry;
import com.Polarice3.Goety.utils.LichdomUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class GoldTotemItem extends Item {
    public static final String SOULSAMOUNT = "Souls";
    public static final int MAXSOULS = MainConfig.MaxSouls.get();

    public GoldTotemItem() {
        super(new Properties().tab(Goety.TAB).stacksTo(1).rarity(Rarity.RARE));
        ItemModelsProperties.register(this, new ResourceLocation("souls"),
                (stack, world, living) -> ((float) currentSouls(stack)) / MAXSOULS);
        ItemModelsProperties.register(this, new ResourceLocation("activated"),
                (stack, world, living) -> isActivated(stack) ? 1.0F : 0.0F);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack container = itemStack.copy();
        assert container.getTag() != null;
        if (container.getTag().getInt(SOULSAMOUNT) > MainConfig.CraftingSouls.get()) {
            GoldTotemItem.decreaseSouls(container, MainConfig.CraftingSouls.get());
            return container;
        } else {
            return new ItemStack(ModRegistry.SPENTTOTEM.get());
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getTag() == null){
            CompoundNBT compound = stack.getOrCreateTag();
            compound.putInt(SOULSAMOUNT, 0);
        }
        if (stack.getTag().getInt(SOULSAMOUNT) > MAXSOULS){
            stack.getTag().putInt(SOULSAMOUNT, MAXSOULS);
        }
        if (stack.getTag().getInt(SOULSAMOUNT) < 0){
            stack.getTag().putInt(SOULSAMOUNT, 0);
        }
/*        if (MainConfig.TotemUndying.get()) {
            if (entityIn instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entityIn;
                if (MainConfig.LichHardcore.get()){
                    if (LichdomUtil.isLich(player) && player.level.getLevelData().isHardcore()){
                        if (stack.getTag().getInt(SOULSAMOUNT) >= MAXSOULS * 0.9) {
                            ItemStack foundStack = GoldTotemFinder.FindTotem(player);
                            if (!foundStack.isEmpty()) {
                                ((LivingEntity) entityIn).addEffect(new EffectInstance(ModRegistry.DEATHPROTECT.get(), 20, 0, false, false));
                            }
                        }
                    }
                } else {
                    if (stack.getTag().getInt(SOULSAMOUNT) == MAXSOULS) {
                        ItemStack foundStack = GoldTotemFinder.FindTotem(player);
                        if (!foundStack.isEmpty()) {
                            ((LivingEntity) entityIn).addEffect(new EffectInstance(ModRegistry.DEATHPROTECT.get(), 20, 0, false, false));
                        }
                    }
                }
            }
        }*/
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    public static boolean isActivated(ItemStack itemStack){
        return itemStack.getTag() != null;
    }

    private static boolean isFull(ItemStack itemStack) {
        assert itemStack.getTag() != null;
        int Soulcount = itemStack.getTag().getInt(SOULSAMOUNT);
        return Soulcount == MAXSOULS;
    }

    private static boolean isEmpty(ItemStack itemStack) {
        assert itemStack.getTag() != null;
        int Soulcount = itemStack.getTag().getInt(SOULSAMOUNT);
        return Soulcount == 0;
    }

    public static boolean UndyingEffect(PlayerEntity player){
        ItemStack itemStack = GoldTotemFinder.FindTotem(player);
        if (!itemStack.isEmpty()) {
            if (itemStack.getTag() != null) {
                if (MainConfig.TotemUndying.get()) {
                    if (MainConfig.LichHardcore.get()) {
                        if (LichdomUtil.isLich(player) && player.level.getLevelData().isHardcore()) {
                            return itemStack.getTag().getInt(SOULSAMOUNT) >= MAXSOULS * 0.9;
                        } else {
                            return itemStack.getTag().getInt(SOULSAMOUNT) == MAXSOULS;
                        }
                    } else {
                        return itemStack.getTag().getInt(SOULSAMOUNT) == MAXSOULS;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static int currentSouls(ItemStack itemStack){
        assert itemStack.getTag() != null;
        return itemStack.getTag().getInt(SOULSAMOUNT);
    }

    public static void handleKill(PlayerEntity playerEntity, LivingEntity victim) {
        ItemStack foundStack = GoldTotemFinder.FindTotem(playerEntity);

        if (!foundStack.isEmpty()) {
            if (!(victim instanceof SummonedEntity || victim instanceof FriendlyVexEntity)) {
                if (victim.getMobType() == CreatureAttribute.UNDEAD) {
                    increaseSouls(foundStack, MainConfig.UndeadSouls.get() * SoulMultiply(playerEntity));
                } else if (victim.getMobType() == CreatureAttribute.ARTHROPOD) {
                    increaseSouls(foundStack, MainConfig.AnthropodSouls.get() * SoulMultiply(playerEntity));
                } else if (victim instanceof AbstractRaiderEntity) {
                    increaseSouls(foundStack, MainConfig.IllagerSouls.get() * SoulMultiply(playerEntity));
                } else if (victim instanceof VillagerEntity && !victim.isBaby()) {
                    increaseSouls(foundStack, MainConfig.VillagerSouls.get() * SoulMultiply(playerEntity));
                }  else if (victim instanceof AbstractPiglinEntity || victim instanceof TameableEntity || victim instanceof MutatedEntity) {
                    increaseSouls(foundStack, MainConfig.PiglinSouls.get() * SoulMultiply(playerEntity));
                } else if (victim instanceof EnderDragonEntity) {
                    increaseSouls(foundStack, MainConfig.EnderDragonSouls.get() * SoulMultiply(playerEntity));
                } else if (victim instanceof PlayerEntity) {
                    increaseSouls(foundStack, MainConfig.PlayerSouls.get() * SoulMultiply(playerEntity));
                } else {
                    increaseSouls(foundStack, MainConfig.DefaultSouls.get() * SoulMultiply(playerEntity));
                }
            }
        }
    }

    public static void EmptySoulTotem(PlayerEntity playerEntity){
        ItemStack foundStack = GoldTotemFinder.FindTotem(playerEntity);
        if (GoldTotemItem.isFull(foundStack)){
            foundStack.setCount(0);
            playerEntity.addItem(new ItemStack(ModRegistry.SPENTTOTEM.get()));
        }
    }

    public static int SoulMultiply(PlayerEntity playerEntity){
        ItemStack weapon= playerEntity.getMainHandItem();
        int i = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantmentsType.SOULEATER.get(), weapon);
        if (i > 0){
            return i + 1;
        } else {
            return 1;
        }
    }

    public static void setSoulsamount(ItemStack itemStack, int souls){
        if (itemStack.getItem() != ModRegistry.GOLDTOTEM.get()) {
            return;
        }
        assert itemStack.getTag() != null;
        itemStack.getTag().putInt(SOULSAMOUNT, souls);
    }

    public static void increaseSouls(ItemStack itemStack, int souls) {
        if (itemStack.getItem() != ModRegistry.GOLDTOTEM.get()) {
            return;
        }
        assert itemStack.getTag() != null;
        int Soulcount = itemStack.getTag().getInt(SOULSAMOUNT);
        if (!isFull(itemStack)) {
            Soulcount += souls;
            itemStack.getTag().putInt(SOULSAMOUNT, Soulcount);
        }
    }

    public static void decreaseSouls(ItemStack itemStack, int souls) {
        if (itemStack.getItem() != ModRegistry.GOLDTOTEM.get()) {
            return;
        }
        assert itemStack.getTag() != null;
        int Soulcount = itemStack.getTag().getInt(SOULSAMOUNT);
        if (!isEmpty(itemStack)) {
            Soulcount -= souls;
            itemStack.getTag().putInt(SOULSAMOUNT, Soulcount);
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.getTag() != null;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack){
        if (stack.getTag() != null) {
            int Soulcount = stack.getTag().getInt(SOULSAMOUNT);
            return 1.0D - (Soulcount / (double) MAXSOULS);
        } else {
            return 1.0D;
        }
    }

    public ActionResultType useOn(ItemUseContext pContext) {
        World world = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);
        if (blockstate.is(ModRegistry.CURSED_CAGE_BLOCK.get()) && !blockstate.getValue(CursedCageBlock.POWERED)) {
            ItemStack itemstack = pContext.getItemInHand();
            if (!world.isClientSide) {
                ((CursedCageBlock) ModRegistry.CURSED_CAGE_BLOCK.get()).setItem(world, blockpos, blockstate, itemstack);
                world.levelEvent(null, 1010, blockpos, Item.getId(this));
                itemstack.shrink(1);
            }

            return ActionResultType.sidedSuccess(world.isClientSide);
        } else {
            return ActionResultType.PASS;
        }
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (playerIn.isCreative()){
            assert itemstack.getTag() != null;
            itemstack.getTag().putInt(SOULSAMOUNT, MAXSOULS);
            return ActionResult.consume(itemstack);
        } else {
            return ActionResult.fail(itemstack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (stack.getTag() != null) {
            int Soulcounts = stack.getTag().getInt(SOULSAMOUNT);
            tooltip.add(new TranslationTextComponent("info.goety.goldtotem.souls", Soulcounts, MAXSOULS));
        }
    }

}
