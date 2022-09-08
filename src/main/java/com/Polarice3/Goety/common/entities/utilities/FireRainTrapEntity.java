package com.Polarice3.Goety.common.entities.utilities;

import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FireRainTrapEntity extends AbstractTrapEntity {

    public FireRainTrapEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.setParticle(ParticleTypes.ASH);
    }

    public FireRainTrapEntity(World worldIn, double x, double y, double z) {
        this(ModEntityType.FIRERAINTRAP.get(), worldIn);
        this.setPos(x, y, z);
    }

    public void tick() {
        super.tick();
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(this.getX(), this.getY(), this.getZ());

        while(blockpos$mutable.getY() < this.getY() + 32.0D && !this.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
            blockpos$mutable.move(Direction.UP);
        }
        if (this.getOwner() != null) {
            SmallFireballEntity fireballEntity = new SmallFireballEntity(level, this.getOwner(), 0, -900D, 0);
            fireballEntity.setPos(this.getX() + this.random.nextInt(5), blockpos$mutable.getY(), this.getZ() + this.random.nextInt(5));
            level.addFreshEntity(fireballEntity);
        } else {
            this.remove();
        }
        if (this.tickCount >= this.getDuration()) {
            this.remove();
        }
    }

}
