package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class SoulLightEntity extends LightProjectileEntity {

    public SoulLightEntity(EntityType<? extends SoulLightEntity> p_i50147_1_, World p_i50147_2_) {
        super(p_i50147_1_, p_i50147_2_);
    }

    public SoulLightEntity(final World world, final double x, final double y, final double z) {
        super(world, x, y, z);
    }

    public SoulLightEntity(final World world, final LivingEntity shooter) {
        super(world, shooter);
    }

    @Override
    public IParticleData sourceParticle() {
        return ModParticleTypes.SOUL_LIGHT_EFFECT.get();
    }

    @Override
    public IParticleData trailParticle() {
        return ModParticleTypes.BULLET_EFFECT.get();
    }

    @Override
    public Block LightBlock() {
        return ModBlocks.SOUL_LIGHT_BLOCK.get();
    }

    @Override
    public EntityType<?> getType() {
        return ModEntityType.SOUL_LIGHT.get();
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
