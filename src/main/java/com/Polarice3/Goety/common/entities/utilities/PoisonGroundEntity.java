package com.Polarice3.Goety.common.entities.utilities;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

import java.util.List;

public class PoisonGroundEntity extends AbstractTrapEntity {

    public PoisonGroundEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.setParticle(ModParticleTypes.POISON.get());
    }

    public PoisonGroundEntity(World worldIn, double x, double y, double z) {
        this(ModEntityType.POISON_GROUND.get(), worldIn);
        this.setPos(x, y, z);
    }

    public void tick() {
        super.tick();
        List<LivingEntity> list1 = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
        if (!list1.isEmpty()){
            for(LivingEntity livingentity : list1) {
                if (this.getOwner() != null) {
                    if (livingentity != this.getOwner()) {
                        livingentity.addEffect(new EffectInstance(Effects.POISON, 200));
                        livingentity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200));
                    }
                } else {
                    livingentity.addEffect(new EffectInstance(Effects.POISON, 200));
                    livingentity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200));
                }
            }
        }
        if (this.tickCount >= this.getDuration()) {
            this.remove();
        }
    }
}
