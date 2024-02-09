package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class HarpoonEntity extends ArrowEntity {
    public HarpoonEntity(EntityType<? extends ArrowEntity> p_36858_, World p_36859_) {
        super(p_36858_, p_36859_);
    }

    public HarpoonEntity(World p_36866_, LivingEntity p_36867_) {
        super(p_36866_, p_36867_);
    }

    @Override
    public EntityType<?> getType() {
        return ModEntityType.HARPOON.get();
    }

    protected float getWaterInertia() {
        return 0.99F;
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return ModSounds.HARPOON_HIT.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isInWater()){
            this.setSoundEvent(ModSounds.HARPOON_HIT_WATER.get());
        } else {
            this.setSoundEvent(ModSounds.HARPOON_HIT.get());
        }
    }

    protected void onHitBlock(BlockRayTraceResult p_36755_) {
        super.onHitBlock(p_36755_);
        if (this.isInWater()){
            this.setSoundEvent(ModSounds.HARPOON_HIT_WATER.get());
        } else {
            this.setSoundEvent(ModSounds.HARPOON_HIT.get());
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
