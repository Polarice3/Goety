package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.client.inventory.crafting.ModRecipeSerializer;
import com.Polarice3.Goety.client.inventory.crafting.SoulAbsorberRecipes;
import com.Polarice3.Goety.common.blocks.CursedBurnerBlock;
import com.Polarice3.Goety.common.items.magic.GoldTotemItem;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModTileEntityType;
import com.Polarice3.Goety.utils.SEHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IClearable;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Optional;

public class SoulAbsorberTileEntity extends TileEntity implements IClearable, ITickableTileEntity, ISidedInventory {
    private static final int[] SLOTS = new int[]{0};
    private ItemStack itemStack = ItemStack.EMPTY;
    private int cookingProgress;
    private int cookingTime;

    public SoulAbsorberTileEntity() {
        super(ModTileEntityType.SOUL_ABSORBER.get());
    }

    public void tick() {
        boolean flag = this.getArcaOwner() != null;
        assert this.level != null;
        boolean flag1 = this.level.isClientSide;
        if (flag1) {
            if (flag) {
                if (!this.itemStack.isEmpty()) {
                    this.makeWorkParticles();
                }
            }

        } else {
            if (flag) {
                this.work();
            } else {
                if (this.itemStack != ItemStack.EMPTY){
                    if (this.cookingProgress > 0) {
                        this.cookingProgress = MathHelper.clamp(this.cookingProgress - 2, 0, this.cookingTime);
                    }
                }
            }
        }
        this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(CursedBurnerBlock.LIT, this.getArcaOwner() != null), 3);
    }

    private void work() {
        if (!this.itemStack.isEmpty()) {
            assert this.level != null;
            if (this.itemStack.getItem() instanceof GoldTotemItem){
                if (GoldTotemItem.currentSouls(this.itemStack) != 0){
                    this.cookingProgress++;
                    if (this.getArcaOwner() != null){
                        SEHelper.increaseSouls(this.getArcaOwner(), 1);
                        GoldTotemItem.decreaseSouls(this.itemStack, 1);
                    }
                } else {
                    this.itemStack.shrink(1);
                    this.level.playSound(null, this.getBlockPos(), SoundEvents.FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    this.finishParticles();
                    this.markUpdated();
                    this.cookingProgress = 0;
                }
            } else {
                IInventory iinventory = new Inventory(this.itemStack);
                int soulIncrease = this.level.getRecipeManager()
                        .getRecipeFor(ModRecipeSerializer.SOUL_ABSORBER_TYPE.get(), iinventory, this.level)
                        .map(SoulAbsorberRecipes::getSoulIncrease).orElse(25);
                if (soulIncrease > 0){
                    this.cookingProgress++;
                }
                if (this.cookingProgress >= this.cookingTime) {
                    if (this.getArcaOwner() != null){
                        SEHelper.increaseSouls(this.getArcaOwner(), soulIncrease);
                    }
                    this.itemStack.shrink(1);
                    this.level.playSound(null, this.getBlockPos(), SoundEvents.FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    this.finishParticles();
                    this.markUpdated();
                    this.cookingProgress = 0;
                }
            }
        }

    }

    public boolean placeItem(ItemStack pStack, int pCookTime) {
        if (this.itemStack.isEmpty()) {
            this.cookingTime = pCookTime;
            this.cookingProgress = 0;
            this.itemStack = pStack;
            assert this.level != null;
            this.level.playSound(null, this.getBlockPos(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.BLOCKS, 1.0F, 0.5F);
            this.markUpdated();
            return true;
        }

        return false;
    }

    public boolean isEmpty() {
        return this.itemStack.isEmpty();
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return this.itemStack;
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {
        Optional<SoulAbsorberRecipes> optional = this.getRecipes(pStack);
        optional.ifPresent(soulAbsorberRecipes -> this.placeItem(pStack, soulAbsorberRecipes.getCookingTime()));
    }

    @Override
    public boolean stillValid(PlayerEntity pPlayer) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return pPlayer.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    private void finishParticles() {
        BlockPos blockpos = this.getBlockPos();

        if (this.level != null) {
            if (!this.level.isClientSide) {
                ServerWorld serverWorld = (ServerWorld) this.level;
                serverWorld.sendParticles(ParticleTypes.LARGE_SMOKE, blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D, 1, 0, 0, 0, 0);
                for (int p = 0; p < 6; ++p) {
                    double d0 = (double) blockpos.getX() + serverWorld.random.nextDouble();
                    double d1 = (double) blockpos.getY() + serverWorld.random.nextDouble();
                    double d2 = (double) blockpos.getZ() + serverWorld.random.nextDouble();
                    serverWorld.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, d0, d1, d2, 1, 0, 0, 0, 0);
                    serverWorld.sendParticles(ParticleTypes.SMOKE, d0, d1, d2, 1, 0, 0, 0, 0);
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void makeWorkParticles() {
        BlockPos blockpos = this.getBlockPos();
        Minecraft MINECRAFT = Minecraft.getInstance();

        if (MINECRAFT.level != null) {
            long t = MINECRAFT.level.getGameTime();
            if (t % 20 == 0) {
                for (int p = 0; p < 6; ++p) {
                    double d0 = (double)blockpos.getX() + MINECRAFT.level.random.nextDouble();
                    double d1 = (double)blockpos.getY() + MINECRAFT.level.random.nextDouble();
                    double d2 = (double)blockpos.getZ() + MINECRAFT.level.random.nextDouble();
                    MINECRAFT.level.addParticle(ParticleTypes.ENCHANT, d0, d1, d2, 0, 0, 0);
                    MINECRAFT.level.addParticle(ParticleTypes.PORTAL, d0, d1, d2, 0.0D, 0, 0.0D);
                }
            }
        }
    }

    public void load(BlockState blockState, CompoundNBT compoundNBT) {
        super.load(blockState, compoundNBT);
        this.itemStack = ItemStack.of(compoundNBT);
        this.cookingProgress = compoundNBT.getInt("CookingTime");
        this.cookingTime = compoundNBT.getInt("CookingTotalTime");
    }

    public CompoundNBT save(CompoundNBT pCompound) {
        this.saveMetadataAndItems(pCompound);
        pCompound.putInt("CookingTime", this.cookingProgress);
        pCompound.putInt("CookingTotalTime", this.cookingTime);
        return pCompound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
        CompoundNBT tag = pkt.getTag();
        load(this.getBlockState(), tag);
    }

    private CompoundNBT saveMetadataAndItems(CompoundNBT pCompound) {
        super.save(pCompound);
        this.itemStack.save(pCompound);
        return pCompound;
    }

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, -1, this.getUpdateTag());
    }

    public CompoundNBT getUpdateTag() {
        return this.saveMetadataAndItems(new CompoundNBT());
    }

    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    private boolean checkArca() {
        assert this.level != null;
        return this.level.getBlockState(new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ())).is(ModBlocks.ARCA_BLOCK.get());
    }

    private PlayerEntity getArcaOwner(){
        if (this.checkArca()){
            BlockPos blockPos = new BlockPos(this.getBlockPos().getX(), this.getBlockPos().getY() - 1, this.getBlockPos().getZ());
            ArcaTileEntity arcaTileEntity = (ArcaTileEntity) this.level.getBlockEntity(blockPos);
            if (arcaTileEntity != null && arcaTileEntity.getPlayer() != null){
                return arcaTileEntity.getPlayer();
            }
        }
        return null;
    }

    public Optional<SoulAbsorberRecipes> getRecipes(ItemStack pStack) {
        return this.level.getRecipeManager().getRecipeFor(ModRecipeSerializer.SOUL_ABSORBER_TYPE.get(), new Inventory(pStack), this.level);
    }

    @Override
    public void clearContent() {
        this.itemStack.shrink(1);
    }

    @Override
    public int[] getSlotsForFace(Direction pSide) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
        Optional<SoulAbsorberRecipes> optional = this.getRecipes(pItemStack);
        if (!optional.isPresent()) return false;
        if (this.getArcaOwner() == null) return false;
        assert this.level != null;
        return !this.level.isClientSide && this.placeItem(pItemStack, optional.get().getCookingTime());
    }

    @Override
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        return false;
    }
}
