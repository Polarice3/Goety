package com.Polarice3.Goety.common.blocks.tiles;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.blocks.TotemHeadBlock;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.projectiles.FangEntity;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModTileEntityType;
import com.Polarice3.Goety.utils.MobUtil;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.UUID;

public class SoulFangTotemTileEntity extends TileEntity implements ITickableTileEntity {
    protected final Object2IntMap<Enchantment> enchantments = new Object2IntOpenHashMap<>();
    public int range;
    public int activated;
    public int levels;
    private UUID ownerUUID;

    public SoulFangTotemTileEntity() {
        super(ModTileEntityType.SOUL_FANG_TOTEM.get());
    }

    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    public World getLevel() {
        return this.level;
    }

    public Object2IntMap<Enchantment> getEnchantments() {
        return this.enchantments;
    }

    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);
        UUID uuid;
        if (nbt.hasUUID("Owner")) {
            uuid = nbt.getUUID("Owner");
        } else {
            String s = nbt.getString("Owner");
            uuid = PreYggdrasilConverter.convertMobOwnerIfNecessary(this.level.getServer(), s);
        }
        if (uuid != null) {
            try {
                this.setOwnerId(uuid);
            } catch (Throwable ignored) {
            }
        }
        ListNBT enchants = nbt.getList("enchantments", Constants.NBT.TAG_COMPOUND);
        Map<Enchantment, Integer> map = EnchantmentHelper.deserializeEnchantments(enchants);
        if (nbt.getInt("potency") > 0) {
            map.put(ModEnchantments.POTENCY.get(), nbt.getInt("potency"));
        }
        if (nbt.getInt("burning") > 0) {
            map.put(ModEnchantments.BURNING.get(), nbt.getInt("burning"));
        }
        if (nbt.getInt("range") > 0) {
            map.put(ModEnchantments.RANGE.get(), nbt.getInt("range"));
        }
        this.enchantments.clear();
        this.enchantments.putAll(map);

    }

    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        if (this.getOwnerId() != null) {
            compound.putUUID("Owner", this.getOwnerId());
        }
        ItemStack stack = new ItemStack(ModBlocks.SOUL_FANG_TOTEM_ITEM.get());
        EnchantmentHelper.setEnchantments(this.enchantments, stack);
        compound.put("enchantments", stack.getEnchantmentTags());

        return compound;
    }

    private double getRange(){
        return 10.0D + this.enchantments.getOrDefault(ModEnchantments.RANGE.get(), 0);
    }

    public boolean hasTargets(){
        if (this.getTrueOwner() != null && this.level != null){
            int i = this.worldPosition.getX();
            int j = this.worldPosition.getY();
            int k = this.worldPosition.getZ();
            for (LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(this.getRange()))){
                if (livingEntity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) livingEntity;
                    if (MobUtil.playerValidity(player, false)) {
                        if (this.getPlayer() != player) {
                            return true;
                        }
                    }
                } else {
                    if (livingEntity.isOnGround()) {
                        if (livingEntity instanceof IOwned) {
                            IOwned summonedEntity = (IOwned) livingEntity;
                            if (summonedEntity.getTrueOwner() != this.getTrueOwner()) {
                                return true;
                            }
                        } else if (livingEntity instanceof TameableEntity) {
                            TameableEntity tameableEntity = (TameableEntity) livingEntity;
                            if (tameableEntity.getOwner() != this.getTrueOwner()) {
                                return true;
                            }
                        } else {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void tick() {
        if (this.level != null) {
            int i = this.worldPosition.getX();
            int j = this.worldPosition.getY();
            int k = this.worldPosition.getZ();
            int j1 = this.levels;
            this.checkBeaconLevel(i, j, k);
            if (!this.level.isClientSide()) {
                if (j1 >= 3) {
                    this.serverParticles();
                    long t = this.level.getGameTime();
                    if (t % 40L == 0L && this.hasTargets()) {
                        this.activated = 20;
                        this.SpecialEffect();
                    }
                    if (this.activated != 0) {
                        --this.activated;
                        this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(TotemHeadBlock.POWERED, true), 3);
                    } else {
                        this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(TotemHeadBlock.POWERED, false), 3);
                    }
                } else {
                    this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(TotemHeadBlock.POWERED, false), 3);
                }
            }
        }
    }

    private void checkBeaconLevel(int beaconXIn, int beaconYIn, int beaconZIn) {
        this.levels = 0;

        for(int i = 1; i <= 3; this.levels = i++) {
            int j = beaconYIn - i;
            if (j < 0) {
                break;
            }

            assert this.level != null;
            boolean flag = this.level.getBlockState(new BlockPos(beaconXIn, j, beaconZIn)).is(ModBlocks.CURSED_TOTEM_BLOCK.get());

            if (!flag) {
                break;
            }
        }

    }

    public void playSound(SoundEvent sound) {
        this.level.playSound(null, this.worldPosition, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public void setRemoved() {
        super.setRemoved();
    }

    private void serverParticles(){
        ServerWorld serverWorld = (ServerWorld) this.level;
        BlockPos blockpos = this.getBlockPos();
        if (serverWorld != null) {
            long t = serverWorld.getGameTime();
            double d0 = (double)blockpos.getX() + serverWorld.random.nextDouble();
            double d1 = (double)blockpos.getY() + serverWorld.random.nextDouble();
            double d2 = (double)blockpos.getZ() + serverWorld.random.nextDouble();
            if (serverWorld.getBlockState(blockpos).getValue(TotemHeadBlock.POWERED)) {
                for (int p = 0; p < 4; ++p) {
                    serverWorld.sendParticles(ModParticleTypes.TOTEM_EFFECT.get(), d0, d1, d2, 0, 0.7, 0.7, 0.7, 0.5F);
                    serverWorld.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, d0, d1, d2, 1, 0, 0, 0, 0);
                }
            } else {
                if (t % 40L == 0L) {
                    for (int p = 0; p < 4; ++p) {
                        serverWorld.sendParticles(ModParticleTypes.TOTEM_EFFECT.get(), d0, d1, d2, 0, 0.45, 0.45, 0.45, 0.5F);
                    }
                }
            }
        }
    }

    public void SpecialEffect() {
        if (this.getTrueOwner() != null && this.level != null) {
            int i = this.worldPosition.getX();
            int j = this.worldPosition.getY();
            int k = this.worldPosition.getZ();
            this.playSound(SoundEvents.EVOKER_PREPARE_ATTACK);
            for (LivingEntity entity : this.level.getEntitiesOfClass(LivingEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(this.getRange()))) {
                float f = (float) MathHelper.atan2(entity.getZ() - this.getBlockPos().getZ(), entity.getX() - this.getBlockPos().getX());
                if (entity instanceof PlayerEntity) {
                    if (this.getPlayer() != entity) {
                        if (MobUtil.playerValidity((PlayerEntity) entity, false)) {
                            this.spawnFangs(entity.getX(), entity.getZ(), entity.getY(), entity.getY() + 1.0D, f);
                        }
                    }
                } else {
                    if (entity.isOnGround()) {
                        if (entity instanceof IOwned) {
                            IOwned summonedEntity = (IOwned) entity;
                            if (summonedEntity.getTrueOwner() != this.getTrueOwner()) {
                                this.spawnFangs(entity.getX(), entity.getZ(), entity.getY(), entity.getY() + 1.0D, f);
                            }
                        } else if (entity instanceof TameableEntity) {
                            TameableEntity tameableEntity = (TameableEntity) entity;
                            if (tameableEntity.getOwner() != this.getTrueOwner()) {
                                this.spawnFangs(entity.getX(), entity.getZ(), entity.getY(), entity.getY() + 1.0D, f);
                            }
                        } else {
                            this.spawnFangs(entity.getX(), entity.getZ(), entity.getY(), entity.getY() + 1.0D, f);
                        }
                    }
                }
            }
        }
    }

    private void spawnFangs(double pPosX, double pPosZ, double PPPosY, double pOPosY, float pYRot) {
        BlockPos blockpos = new BlockPos(pPosX, pOPosY, pPosZ);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = this.level.getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(this.level, blockpos1, Direction.UP)) {
                if (!this.level.isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = this.level.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(this.level, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= MathHelper.floor(PPPosY) - 1);

        if (flag) {
            this.level.addFreshEntity(new FangEntity(this.level, pPosX, (double)blockpos.getY() + d0, pPosZ, pYRot, 0,
                    this.enchantments.getOrDefault(ModEnchantments.POTENCY.get(), 0),
                    this.enchantments.getOrDefault(ModEnchantments.BURNING.get(), 0),
                    this.enchantments.getOrDefault(ModEnchantments.SOUL_EATER.get(), 0) + 1,
                    this.getTrueOwner()));
        }

    }

    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, -1, this.getUpdateTag());
    }

    @Nullable
    public UUID getOwnerId() {
        return this.ownerUUID;
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.ownerUUID = p_184754_1_;
    }

    public LivingEntity getTrueOwner() {
        try {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : this.level.getPlayerByUUID(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    public PlayerEntity getPlayer(){
        return (PlayerEntity) this.getTrueOwner();
    }

}
