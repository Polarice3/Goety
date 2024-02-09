package com.Polarice3.Goety.common.ritual;

import com.Polarice3.Goety.client.inventory.crafting.RitualRecipe;
import com.Polarice3.Goety.common.blocks.tiles.DarkAltarTileEntity;
import com.Polarice3.Goety.common.blocks.tiles.PedestalTileEntity;
import com.Polarice3.Goety.common.entities.ally.SummonedEntity;
import com.Polarice3.Goety.common.entities.hostile.ShadeEntity;
import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.ItemHelper;
import com.Polarice3.Goety.utils.ModMathHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Ritual Codes based from @klikli-dev
 */
public abstract class Ritual {

    public static final int PEDESTAL_RANGE = 8;

    public static final int SACRIFICE_DETECTION_RANGE = 8;

    public RitualRecipe recipe;

    public ResourceLocation factoryId;

    public Ritual(RitualRecipe recipe) {
        this.recipe = recipe;
    }

    public static List<Ingredient> getRemainingAdditionalIngredients(List<Ingredient> additionalIngredients, List<ItemStack> consumedIngredients) {
        List<ItemStack> consumedIngredientsCopy = new ArrayList<>(consumedIngredients);
        List<Ingredient> remainingAdditionalIngredients = new ArrayList<>();
        for (Ingredient ingredient : additionalIngredients) {
            Optional<ItemStack> matchedStack = consumedIngredientsCopy.stream().filter(ingredient::test).findFirst();
            if (matchedStack.isPresent()) {
                consumedIngredientsCopy.remove(matchedStack.get());
            } else {
                remainingAdditionalIngredients.add(ingredient);
            }
        }
        return remainingAdditionalIngredients;
    }

    public ResourceLocation getFactoryID() {
        return this.factoryId;
    }

    public void setFactoryId(ResourceLocation factoryId) {
        this.factoryId = factoryId;
    }

    public RitualRecipe getRecipe() {
        return this.recipe;
    }

    public boolean isValid(World world, BlockPos darkAltarPos, DarkAltarTileEntity tileEntity,
                           PlayerEntity castingPlayer, ItemStack activationItem,
                           List<Ingredient> remainingAdditionalIngredients) {
        return this.recipe.getActivationItem().test(activationItem) &&
                this.areAdditionalIngredientsFulfilled(world, darkAltarPos, remainingAdditionalIngredients);
    }

    public void start(World world, BlockPos darkAltarPos, DarkAltarTileEntity tileEntity,
                      PlayerEntity castingPlayer, ItemStack activationItem) {
        world.playSound(null, darkAltarPos, ModSounds.ALTAR_START.get(), SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
    }

    public void finish(World world, BlockPos darkAltarPos, DarkAltarTileEntity tileEntity,
                       PlayerEntity castingPlayer, ItemStack activationItem) {
        if (tileEntity.getCurrentRitualRecipe().getCraftType().contains("forge")){
            world.playSound(null, darkAltarPos, SoundEvents.ANVIL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
        }
        world.playSound(null, darkAltarPos, ModSounds.ALTAR_FINISH.get(), SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
    }

    public void interrupt(World world, BlockPos darkAltarPos, DarkAltarTileEntity tileEntity,
                          PlayerEntity castingPlayer, ItemStack activationItem) {
        world.playSound(null, darkAltarPos, ModSounds.SPELL_FAIL.get(), SoundCategory.BLOCKS, 0.7f, 0.7f);
    }

    public void update(World world, BlockPos darkAltarPos, DarkAltarTileEntity tileEntity,
                       PlayerEntity castingPlayer, ItemStack activationItem,
                       List<Ingredient> remainingAdditionalIngredients, int time, int totalTime) {

        int progress = totalTime - time;

        if (world.getGameTime() % ModMathHelper.secondsToTicks(4) == 0 && ModMathHelper.secondsToTicks(4) > progress) {
            world.playSound(null, darkAltarPos, ModSounds.ALTAR_LOOP.get(), SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
        }

        List<PedestalTileEntity> pedestals = this.getPedestals(world, darkAltarPos);
        for (PedestalTileEntity pedestal : pedestals) {
            pedestal.itemStackHandler.map(handler -> {
                ItemStack stack = handler.extractItem(0, 1, true);
                if (!stack.isEmpty()) {
                    addItemParticles((ServerWorld) world, pedestal.getBlockPos(), darkAltarPos, stack);
                }
                return true;
            });
        }
    }

    public void update(World world, BlockPos darkAltarPos, DarkAltarTileEntity tileEntity,
                       PlayerEntity castingPlayer, ItemStack activationItem, int time, int totalTime) {
        this.update(world, darkAltarPos, tileEntity, castingPlayer, activationItem, new ArrayList<Ingredient>(),
                time, totalTime);
    }

    public boolean identify(World world, BlockPos darkAltarPos, ItemStack activationItem) {
        return this.recipe.getActivationItem().test(activationItem) &&
                this.areAdditionalIngredientsFulfilled(world, darkAltarPos, this.recipe.getIngredients());
    }

    public boolean consumeAdditionalIngredients(World world, BlockPos darkAltarPos, PlayerEntity player,
                                                List<Ingredient> remainingAdditionalIngredients, int time,
                                                List<ItemStack> consumedIngredients) {
        if (remainingAdditionalIngredients.isEmpty())
            return true;

        int totalIngredientsToConsume = (int) Math.floor(time / this.recipe.getDurationPerIngredient());
        int ingredientsConsumed = consumedIngredients.size();

        int ingredientsToConsume = totalIngredientsToConsume - ingredientsConsumed;
        if (ingredientsToConsume == 0)
            return true;

        List<PedestalTileEntity> pedestals = this.getPedestals(world, darkAltarPos);
        int consumed = 0;
        for (Iterator<Ingredient> it = remainingAdditionalIngredients.iterator();
             it.hasNext() && consumed < ingredientsToConsume; consumed++) {
            Ingredient ingredient = it.next();
            if (this.consumeAdditionalIngredient(world, darkAltarPos, pedestals, ingredient,
                    consumedIngredients)) {
                it.remove();
            } else {
                player.displayClientMessage(new TranslationTextComponent("info.goety.ritual.cannotConsume.fail"), true);
                return false;
            }
        }
        return true;
    }

    public boolean consumeAdditionalIngredient(World world, BlockPos darkAltarPos,
                                               List<PedestalTileEntity> pedestals,
                                               Ingredient ingredient, List<ItemStack> consumedIngredients) {
        for (PedestalTileEntity pedestal : pedestals) {
            if (pedestal.itemStackHandler.map(handler -> {
                ItemStack stack = handler.extractItem(0, 1, true);
                if (ingredient.test(stack)) {
                    ItemStack extracted = handler.extractItem(0, 1, false);

                    consumedIngredients.add(extracted);

                    if (extracted.getItem() instanceof BucketItem){
                        BucketItem bucketItem = (BucketItem) extracted.getItem();
                        if (!bucketItem.getFluid().defaultFluidState().isEmpty()) {
                            ItemHelper.addItemEntity(world, pedestal.getBlockPos(), new ItemStack(Items.BUCKET));
                            world.playSound(null, pedestal.getBlockPos(), SoundEvents.BUCKET_EMPTY, SoundCategory.BLOCKS,
                                    0.7F, 0.7F);
                        }
                    } else if (extracted.hasContainerItem()){
                        ItemHelper.addItemEntity(world, pedestal.getBlockPos(), extracted.getContainerItem());
                    }
                    world.playSound(null, pedestal.getBlockPos(), SoundEvents.ITEM_PICKUP, SoundCategory.BLOCKS,
                            0.7f, 0.7f);
                    return true;
                }
                return false;
            }).orElse(false))
                return true;

        }
        return false;
    }

    private void addItemParticles(ServerWorld world, BlockPos pedestalPos, BlockPos darkAltarPos, ItemStack stack) {
        double d0 = 0.1D * (darkAltarPos.getX() - pedestalPos.getX());
        double d1 = 0.3D;
        double d2 = 0.1D * (darkAltarPos.getZ() - pedestalPos.getZ());
        world.sendParticles(new ItemParticleData(ParticleTypes.ITEM, stack), pedestalPos.getX() + 0.5D, pedestalPos.getY() + 1.5D, pedestalPos.getZ() + 0.5D, 0, d0, d1, d2, 1.0D);
    }

    public boolean areAdditionalIngredientsFulfilled(World world, BlockPos darkAltarPos,
                                                     List<Ingredient> additionalIngredients) {
        return this.matchesAdditionalIngredients(additionalIngredients,
                this.getItemsOnPedestals(world, darkAltarPos));
    }

    public boolean matchesAdditionalIngredients(List<Ingredient> additionalIngredients, List<ItemStack> items) {

        if (additionalIngredients.size() != items.size())
            return false;

        if (additionalIngredients.isEmpty())
            return true;

        List<ItemStack> remainingItems = new ArrayList<>(items);

        for (Ingredient ingredient : additionalIngredients) {
            boolean isMatched = false;
            for (int i = 0; i < remainingItems.size(); i++) {
                ItemStack stack = remainingItems.get(i);
                if (ingredient.test(stack)) {
                    isMatched = true;
                    remainingItems.remove(i);
                    break;
                }
            }
            if (!isMatched)
                return false;
        }

        return true;
    }

    public List<ItemStack> getItemsOnPedestals(World world, BlockPos darkAltarPos) {
        List<ItemStack> result = new ArrayList<>();

        List<PedestalTileEntity> pedestals = this.getPedestals(world, darkAltarPos);
        for (PedestalTileEntity pedestalTile : pedestals) {
            pedestalTile.itemStackHandler.ifPresent(handler -> {
                ItemStack stack = handler.getStackInSlot(0);
                if (!stack.isEmpty()) {
                    result.add(stack);
                }
            });
        }

        return result;
    }

    public List<PedestalTileEntity> getPedestals(World world, BlockPos darkAltarPos) {
        List<PedestalTileEntity> result = new ArrayList<>();
        Iterable<BlockPos> blocksToCheck = BlockPos.betweenClosed(
                darkAltarPos.offset(-PEDESTAL_RANGE, -PEDESTAL_RANGE, -PEDESTAL_RANGE),
                darkAltarPos.offset(PEDESTAL_RANGE, PEDESTAL_RANGE, PEDESTAL_RANGE));
        for (BlockPos blockToCheck : blocksToCheck) {
            TileEntity tileEntity = world.getBlockEntity(blockToCheck);
            if (tileEntity instanceof PedestalTileEntity &&
                    !(tileEntity instanceof DarkAltarTileEntity)) {
                result.add((PedestalTileEntity) tileEntity);
            }
        }
        return result;
    }

    public void prepareLivingEntityForSpawn(LivingEntity livingEntity, World world, BlockPos darkAltarPos, DarkAltarTileEntity tileEntity,
                                            PlayerEntity castingPlayer, boolean setTamed) {
        if (setTamed && livingEntity instanceof TameableEntity) {
            ((TameableEntity) livingEntity).tame(castingPlayer);
        }
        if (setTamed && livingEntity instanceof AbstractHorseEntity) {
            ((AbstractHorseEntity) livingEntity).setTamed(true);
            ((AbstractHorseEntity) livingEntity).setOwnerUUID(castingPlayer.getUUID());
        }
        if (setTamed && livingEntity instanceof OwnedEntity) {
            OwnedEntity summonedEntity = (OwnedEntity) livingEntity;
            summonedEntity.setPersistenceRequired();
            summonedEntity.setOwnerId(castingPlayer.getUUID());
            if (summonedEntity instanceof SummonedEntity){
                ((SummonedEntity) summonedEntity).setWandering(false);
            }
        }
        if (livingEntity instanceof ShadeEntity){
            ((ShadeEntity) livingEntity).setBoundOrigin(darkAltarPos);
        }
        livingEntity.absMoveTo(darkAltarPos.getX(), darkAltarPos.getY(), darkAltarPos.getZ(),
                world.random.nextInt(360), 0);
        if (livingEntity instanceof MobEntity)
            ((MobEntity) livingEntity).finalizeSpawn((ServerWorld) world, world.getCurrentDifficultyAt(darkAltarPos),
                    SpawnReason.MOB_SUMMONED, null,
                    null);
    }

    public boolean isValidSacrifice(LivingEntity entity) {
        return entity != null && this.recipe.requiresSacrifice() && this.recipe.getEntityToSacrifice().contains(entity.getType());
    }

    public boolean requiresSacrifice() {
        return this.recipe.requiresSacrifice();
    }

    public void dropResult(World world, BlockPos darkAltarPos, DarkAltarTileEntity tileEntity,
                           PlayerEntity castingPlayer, ItemStack stack) {
        ItemEntity entity = new ItemEntity(world, darkAltarPos.getX(), darkAltarPos.getY() + 1.0F,
                darkAltarPos.getZ(), stack);
        entity.setPickUpDelay(10);
        world.addFreshEntity(entity);
    }
}
/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */