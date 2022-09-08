package com.Polarice3.Goety.common.entities.projectiles;

import com.Polarice3.Goety.common.entities.utilities.BurningGroundEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

@OnlyIn(
        value = Dist.CLIENT,
        _interface = IRendersAsItem.class
)
public class BurningPotionEntity extends ProjectileItemEntity {
    public BurningPotionEntity(EntityType<? extends BurningPotionEntity> p_i50159_1_, World p_i50159_2_) {
        super(p_i50159_1_, p_i50159_2_);
    }

    public BurningPotionEntity(World worldIn, LivingEntity throwerIn) {
        super(ModEntityType.BURNING_POTION.get(), throwerIn, worldIn);
    }

    public BurningPotionEntity(World worldIn, double x, double y, double z) {
        super(ModEntityType.BURNING_POTION.get(), x, y, z, worldIn);
    }

    protected Item getDefaultItem() {
        return ModItems.BURNING_POTION.get();
    }

    protected void onHit(RayTraceResult pResult) {
        super.onHit(pResult);
        Entity entity = this.getOwner();
        if (pResult.getType() != RayTraceResult.Type.ENTITY) {
            if (!this.level.isClientSide) {
                List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D));
                BurningGroundEntity burningGroundEntity = new BurningGroundEntity(this.level, this.getX(), this.getY(), this.getZ());
                if (entity instanceof LivingEntity) {
                    burningGroundEntity.setOwner((LivingEntity)entity);
                }
                burningGroundEntity.setDuration(600);
                if (!list.isEmpty()) {
                    for(LivingEntity livingentity : list) {
                        double d0 = this.distanceToSqr(livingentity);
                        if (d0 < 16.0D) {
                            burningGroundEntity.setPos(livingentity.getX(), livingentity.getY(), livingentity.getZ());
                            break;
                        }
                    }
                }

                this.level.levelEvent(2007, this.blockPosition(), this.isSilent() ? -1 : 1);
                this.level.addFreshEntity(burningGroundEntity);
                this.remove();
            }

        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
