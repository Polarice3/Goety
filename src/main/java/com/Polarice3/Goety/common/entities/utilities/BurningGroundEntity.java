package com.Polarice3.Goety.common.entities.utilities;

import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

import java.util.List;

public class BurningGroundEntity extends AbstractTrapEntity {

    public BurningGroundEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.setParticle(ModParticleTypes.BURNING.get());
    }

    public BurningGroundEntity(World worldIn, double x, double y, double z) {
        this(ModEntityType.BURNING_GROUND.get(), worldIn);
        this.setPos(x, y, z);
    }

    public void tick() {
        super.tick();
        if (this.isInWater()){
            this.remove();
        }
        List<LivingEntity> list1 = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
        if (!list1.isEmpty()){
            for(LivingEntity livingentity : list1) {
                if (!livingentity.fireImmune()) {
                    livingentity.setSecondsOnFire(8);
                }
            }
        }
        if (this.tickCount >= this.getDuration()) {
            this.remove();
        }
    }
}
