package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.inventory.crafting.ModRecipeSerializer;
import com.Polarice3.Goety.client.inventory.crafting.RitualRecipe;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.blocks.DarkAltarBlock;
import com.Polarice3.Goety.common.ritual.Ritual;
import com.Polarice3.Goety.common.ritual.RitualStructures;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModTileEntityType;
import com.Polarice3.Goety.utils.ConstantPaths;
import com.Polarice3.Goety.utils.EntityFinder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DarkAltarTileEntity extends PedestalTileEntity implements ITickableTileEntity {
    private CursedCageTileEntity cursedCageTile;
    public RitualRecipe currentRitualRecipe;
    public ResourceLocation currentRitualRecipeId;
    public UUID castingPlayerId;
    public PlayerEntity castingPlayer;
    public List<Ingredient> remainingAdditionalIngredients = new ArrayList<>();
    public List<ItemStack> consumedIngredients = new ArrayList<>();
    public boolean sacrificeProvided;
    public boolean itemUseProvided;
    public int currentTime;
    public int structureTime;

    public DarkAltarTileEntity() {
        super(ModTileEntityType.DARK_ALTAR.get());
    }

    public RitualRecipe getCurrentRitualRecipe(){
        if(this.currentRitualRecipeId != null){
            if(this.level != null) {
                Optional<? extends IRecipe<?>> recipe = this.level.getRecipeManager().byKey(this.currentRitualRecipeId);
                recipe.map(r -> (RitualRecipe) r).ifPresent(r -> this.currentRitualRecipe = r);
                this.currentRitualRecipeId = null;
            }
        }
        return this.currentRitualRecipe;
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);

        this.consumedIngredients.clear();
        if (this.currentRitualRecipeId != null || this.getCurrentRitualRecipe() != null) {
            if (compound.contains("consumedIngredients")) {
                ListNBT list = compound.getList("consumedIngredients", Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < list.size(); i++) {
                    ItemStack stack = ItemStack.of(list.getCompound(i));
                    this.consumedIngredients.add(stack);
                }
            }
            this.restoreRemainingAdditionalIngredients();
        }
        if (compound.contains("sacrificeProvided")) {
            this.sacrificeProvided = compound.getBoolean("sacrificeProvided");
        }
        if (compound.contains("requiredItemUsed")) {
            this.itemUseProvided = compound.getBoolean("requiredItemUsed");
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        if (this.getCurrentRitualRecipe() != null) {
            if (this.consumedIngredients.size() > 0) {
                ListNBT list = new ListNBT();
                for (ItemStack stack : this.consumedIngredients) {
                    list.add(stack.serializeNBT());
                }
                compound.put("consumedIngredients", list);
            }
            compound.putBoolean("sacrificeProvided", this.sacrificeProvided);
            compound.putBoolean("requiredItemUsed", this.itemUseProvided);
        }
        return super.save(compound);
    }

    @Override
    public void readNetwork(CompoundNBT compound) {
        super.readNetwork(compound);
        if (compound.contains("currentRitual")) {
            this.currentRitualRecipeId = new ResourceLocation(compound.getString("currentRitual"));
        }

        if (compound.contains("castingPlayerId")) {
            this.castingPlayerId = compound.getUUID("castingPlayerId");
        }

        this.currentTime = compound.getInt("currentTime");
        this.structureTime = compound.getInt("structureTime");
    }

    @Override
    public CompoundNBT writeNetwork(CompoundNBT compound) {
        RitualRecipe recipe = this.getCurrentRitualRecipe();
        if (recipe != null) {
            compound.putString("currentRitual", recipe.getId().toString());
        }
        if (this.castingPlayerId != null) {
            compound.putUUID("castingPlayerId", this.castingPlayerId);
        }
        compound.putInt("currentTime", this.currentTime);
        compound.putInt("structureTime", this.structureTime);
        return super.writeNetwork(compound);
    }

    @Override
    public void tick() {
        boolean flag = this.checkCage();
        assert this.level != null;
        if (flag) {
            if (this.cursedCageTile.getSouls() > 0){
                RitualRecipe recipe = this.getCurrentRitualRecipe();
                double d0 = (double)this.worldPosition.getX() + this.level.random.nextDouble();
                double d1 = (double)this.worldPosition.getY() + this.level.random.nextDouble();
                double d2 = (double)this.worldPosition.getZ() + this.level.random.nextDouble();
                if (!this.level.isClientSide) {
                    ServerWorld serverWorld = (ServerWorld) this.level;
                    if (recipe != null) {
                        this.cursedCageTile.generateParticles();

                        this.restoreCastingPlayer();

                        if (this.castingPlayer == null || !this.sacrificeFulfilled() || !this.itemUseFulfilled()) {
                            for (int p = 0; p < 4; ++p) {
                                serverWorld.sendParticles(ParticleTypes.FLAME, d0, d1, d2, 1, 0.0F, 0.0F, 0.0F, 0.0F);
                            }
                        }
                        for (int p = 0; p < 4; ++p) {
                            serverWorld.sendParticles(ModParticleTypes.TOTEM_EFFECT.get(), d0, d1, d2, 0, 0.45, 0.45, 0.45, 1);
                        }

                        if (this.remainingAdditionalIngredients == null) {
                            this.restoreRemainingAdditionalIngredients();
                            if (this.remainingAdditionalIngredients == null) {
                                Goety.LOGGER
                                        .warn("Could not restore remainingAdditionalIngredients during tick - world seems to be null. Will attempt again next tick.");
                                return;
                            }
                        }

                        IItemHandler handler = this.itemStackHandler.orElseThrow(RuntimeException::new);
                        if (!recipe.getRitual().isValid(this.level, this.worldPosition, this, this.castingPlayer,
                                handler.getStackInSlot(0), this.remainingAdditionalIngredients)) {
                            this.stopRitual(false);
                            return;
                        }

                        if (this.castingPlayer == null || !this.sacrificeFulfilled() || !this.itemUseFulfilled()) {
                            return;
                        }

                        if (this.level.getGameTime() % 20 == 0) {
                            this.cursedCageTile.decreaseSouls(recipe.getSoulCost());
                            this.currentTime++;
                        }

                        recipe.getRitual().update(this.level, this.worldPosition, this, this.castingPlayer, handler.getStackInSlot(0),
                                this.currentTime);

                        if (!recipe.getRitual()
                                .consumeAdditionalIngredients(this.level, this.worldPosition, this.remainingAdditionalIngredients,
                                        this.currentTime, this.consumedIngredients)) {
                            this.stopRitual(false);
                            return;
                        }

                        if (recipe.getDuration() >= 0 && this.currentTime >= recipe.getDuration()) {
                            this.stopRitual(true);
                        }

                        int totalSTime = 60;

                        if (!RitualStructures.getProperStructure(recipe.getCraftType(), this, this.worldPosition, this.level)){
                            ++this.structureTime;
                            if (this.structureTime >= totalSTime) {
                                this.castingPlayer.displayClientMessage(new TranslationTextComponent("info.goety.ritual.structure.fail"), true);
                                this.stopRitual(false);
                            }
                        } else {
                            this.structureTime = 0;
                        }
                    } else {
                        if (this.level.getGameTime() % 20 == 0) {
                            for (int p = 0; p < 4; ++p) {
                                serverWorld.sendParticles(ModParticleTypes.TOTEM_EFFECT.get(), d0, d1, d2, 0, 0.45, 0.45, 0.45, 1);
                            }
                        }
                    }
                }
            } else {
                this.stopRitual(false);
            }
        } else {
            this.stopRitual(false);
        }
        this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(DarkAltarBlock.LIT, flag), 3);
    }

    public void restoreCastingPlayer() {
        if (this.castingPlayer == null && this.castingPlayerId != null) {
            if (this.level != null) {
                if (this.level.getGameTime() % (20 * 30) == 0) {
                    this.castingPlayer = EntityFinder.getPlayerByUuiDGlobal(this.castingPlayerId).orElse(null);
                    this.setChanged();
                    this.markNetworkDirty();
                }
            }
        }
    }

    public boolean activate(World world, BlockPos pos, PlayerEntity player, Hand hand, Direction face) {
        if (!world.isClientSide) {
            if (this.checkCage()){
                ItemStack activationItem = player.getItemInHand(hand);
                if (activationItem == ItemStack.EMPTY){
                    this.RemoveItem();
                }

                if (this.getCurrentRitualRecipe() == null) {

                    RitualRecipe ritualRecipe = this.level.getRecipeManager().getAllRecipesFor(ModRecipeSerializer.RITUAL_TYPE.get()).stream().filter(
                            r -> r.matches(world, pos, activationItem)
                    ).findFirst().orElse(null);

                    if (ritualRecipe != null) {
                        if (ritualRecipe.getRitual().isValid(world, pos, this, player, activationItem, ritualRecipe.getIngredients())) {

                            if (!RitualStructures.getProperStructure(ritualRecipe.getCraftType(), this, pos, world)){
                                player.displayClientMessage(new TranslationTextComponent("info.goety.ritual.structure.fail"), true);
                                return false;
                            } else if (ritualRecipe.getCraftType().contains("adept_nether") || ritualRecipe.getCraftType().contains("sabbath") || ritualRecipe.getCraftType().contains("expert_nether")){
                                CompoundNBT playerData = player.getPersistentData();
                                CompoundNBT data = playerData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
                                if (data.getBoolean(ConstantPaths.readNetherBook())){
                                    this.startRitual(player, activationItem, ritualRecipe);
                                } else {
                                    player.displayClientMessage(new TranslationTextComponent("info.goety.ritual.fail"), true);
                                    return false;
                                }
                            } else if (ritualRecipe.getCraftType().contains("lich")){
                                CompoundNBT playerData = player.getPersistentData();
                                CompoundNBT data = playerData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
                                if (data.getBoolean(ConstantPaths.readScroll())){
                                    this.startRitual(player, activationItem, ritualRecipe);
                                } else {
                                    player.displayClientMessage(new TranslationTextComponent("info.goety.ritual.fail"), true);
                                    return false;
                                }
                            } else {
                                this.startRitual(player, activationItem, ritualRecipe);
                            }
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    this.stopRitual(false);
                }
            } else {
                this.RemoveItem();
            }
        }
        return true;
    }

    public void RemoveItem(){
        IItemHandler handler = this.itemStackHandler.orElseThrow(RuntimeException::new);
        ItemStack itemStack = handler.getStackInSlot(0);
        if (itemStack != ItemStack.EMPTY){
            InventoryHelper.dropItemStack(this.level, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(),
                    handler.extractItem(0, 1, false));
        }
        this.currentRitualRecipe = null;
        this.castingPlayerId = null;
        this.castingPlayer = null;
        this.currentTime = 0;
        this.sacrificeProvided = false;
        this.itemUseProvided = false;
        if (this.remainingAdditionalIngredients != null)
            this.remainingAdditionalIngredients.clear();
        this.consumedIngredients.clear();
        this.structureTime = 0;
        this.setChanged();
        this.markNetworkDirty();
    }

    public void startRitual(PlayerEntity player, ItemStack activationItem, RitualRecipe ritualRecipe) {
        if (!this.level.isClientSide) {
            this.currentRitualRecipe = ritualRecipe;
            this.castingPlayerId = player.getUUID();
            this.castingPlayer = player;
            this.currentTime = 0;
            this.sacrificeProvided = false;
            this.itemUseProvided = false;
            this.consumedIngredients.clear();
            this.remainingAdditionalIngredients = new ArrayList<>(this.currentRitualRecipe.getIngredients());
            IItemHandler handler = this.itemStackHandler.orElseThrow(RuntimeException::new);
            handler.insertItem(0, activationItem.split(1), false);
            this.currentRitualRecipe.getRitual().start(this.level, this.worldPosition, this, player, handler.getStackInSlot(0));
            this.structureTime = 40;
            this.setChanged();
            this.markNetworkDirty();
        }
    }

    public void stopRitual(boolean finished) {
        if (!this.level.isClientSide) {
            RitualRecipe recipe = this.getCurrentRitualRecipe();
            if (recipe != null && this.castingPlayer != null) {
                IItemHandler handler = this.itemStackHandler.orElseThrow(RuntimeException::new);
                if (finished) {
                    ItemStack activationItem = handler.getStackInSlot(0);
                    recipe.getRitual().finish(this.level, this.worldPosition, this, this.castingPlayer, activationItem);
                } else {
                    recipe.getRitual().interrupt(this.level, this.worldPosition, this, this.castingPlayer,
                            handler.getStackInSlot(0));
                    InventoryHelper.dropItemStack(this.level, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(),
                            handler.extractItem(0, 1, false));
                }
            }
            this.currentRitualRecipe = null;
            this.castingPlayerId = null;
            this.castingPlayer = null;
            this.currentTime = 0;
            this.sacrificeProvided = false;
            this.itemUseProvided = false;
            if (this.remainingAdditionalIngredients != null)
                this.remainingAdditionalIngredients.clear();
            this.consumedIngredients.clear();
            this.structureTime = 0;
            this.setChanged();
            this.markNetworkDirty();
        }
    }

    public boolean sacrificeFulfilled() {
        return !this.getCurrentRitualRecipe().requiresSacrifice() || this.sacrificeProvided;
    }

    public boolean itemUseFulfilled() {
        return !this.getCurrentRitualRecipe().requiresItemUse() || this.itemUseProvided;
    }

    public void notifySacrifice(LivingEntity entityLivingBase) {
        this.sacrificeProvided = true;
    }

    public void notifyItemUse(PlayerInteractEvent.RightClickItem event) {
        this.itemUseProvided = true;
    }

    protected void restoreRemainingAdditionalIngredients() {
        if (this.level == null) {
            this.remainingAdditionalIngredients = null;
        } else {
            if (this.consumedIngredients.size() > 0) {
                this.remainingAdditionalIngredients = Ritual.getRemainingAdditionalIngredients(
                        this.getCurrentRitualRecipe().getIngredients(), this.consumedIngredients);
            } else {
                this.remainingAdditionalIngredients = new ArrayList<>(this.getCurrentRitualRecipe().getIngredients());
            }
        }

    }

    private boolean checkCage() {
        assert this.level != null;
        BlockPos pos = new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ());
        BlockState blockState = this.level.getBlockState(pos);
        if (blockState.is(ModBlocks.CURSED_CAGE_BLOCK.get())){
            TileEntity tileentity = this.level.getBlockEntity(pos);
            if (tileentity instanceof CursedCageTileEntity){
                this.cursedCageTile = (CursedCageTileEntity) tileentity;
                return !cursedCageTile.getItem().isEmpty();
            } else {
                return false;
            }
        } else {
            return false;
        }
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