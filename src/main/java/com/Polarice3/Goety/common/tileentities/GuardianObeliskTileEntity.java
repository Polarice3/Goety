package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.init.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class GuardianObeliskTileEntity extends TileEntity implements ITickableTileEntity {

    public GuardianObeliskTileEntity() {
        this(ModTileEntityType.GUARDIAN_OBELISK.get());
    }

    public GuardianObeliskTileEntity(TileEntityType<?> p_i48929_1_) {
        super(p_i48929_1_);
    }

    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    public World getLevel() {
        return GuardianObeliskTileEntity.this.level;
    }
    
    @Override
    public void tick() {
        assert this.level != null;
        if (!this.level.isClientSide()) {
            int i = this.worldPosition.getX();
            int j = this.worldPosition.getY();
            int k = this.worldPosition.getZ();
            this.SpawnParticles();
            for (Entity entity: this.level.getEntitiesOfClass(Entity.class, (new AxisAlignedBB(i, j, k, i, j + 1, k)).inflate(32.0D))){
                if (entity instanceof PlayerEntity){
                    PlayerEntity player = (PlayerEntity) entity;
                    if (!player.isCreative() && !player.hasEffect(ModRegistry.NOMINE.get())){
                        player.addEffect(new EffectInstance(ModRegistry.NOMINE.get(), 120, 0, false, true));
                    }
                }
                if (entity instanceof TNTEntity){
                    entity.spawnAtLocation(new ItemStack(Items.GUNPOWDER, 5));
                    this.level.playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    entity.remove();
                }
                if (entity instanceof CreeperEntity){
                    if (((CreeperEntity) entity).isIgnited()){
                        this.level.explode(entity, entity.getX(), entity.getY(), entity.getZ(), 3.0F, Explosion.Mode.NONE);
                        entity.remove();
                    }
                }
            }
        }
    }

    private void SpawnParticles(){
        BlockPos blockpos = this.getBlockPos();
        Minecraft MINECRAFT = Minecraft.getInstance();

        if (MINECRAFT.level != null) {
            long t = MINECRAFT.level.getGameTime();
            double d0 = (double)blockpos.getX() + MINECRAFT.level.random.nextDouble();
            double d1 = (double)blockpos.getY() + 1.0D + MINECRAFT.level.random.nextDouble();
            double d2 = (double)blockpos.getZ() + MINECRAFT.level.random.nextDouble();
            if (t % 40L == 0L) {
                for (int p = 0; p < 4; ++p) {
                    MINECRAFT.level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0, 5.0E-4D, 0.0);
                }
            }
        }
    }
}
