package com.Polarice3.Goety.common.entities.utilities;

import com.Polarice3.Goety.AttributesConfig;
import com.Polarice3.Goety.common.entities.bosses.ApostleEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.ConstantPaths;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ArrowRainTrapEntity extends AbstractTrapEntity {

    public ArrowRainTrapEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.setParticle(ParticleTypes.CRIT);
    }

    public ArrowRainTrapEntity(World worldIn, double x, double y, double z) {
        this(ModEntityType.ARROW_RAIN_TRAP.get(), worldIn);
        this.setPos(x, y, z);
    }

    public void tick() {
        super.tick();
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(this.getX(), this.getY(), this.getZ());

        while(blockpos$mutable.getY() < this.getY() + 32.0D && !this.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
            blockpos$mutable.move(Direction.UP);
        }
        if (this.getOwner() != null) {
            if (this.getOwner() instanceof ApostleEntity){
                ApostleEntity apostle = (ApostleEntity) this.getOwner();
                ItemStack itemstack = apostle.getProjectile(apostle.getItemInHand(ProjectileHelper.getWeaponHoldingHand(apostle, item -> item instanceof net.minecraft.item.BowItem)));
                for(int i = 0; i < 3; ++i) {
                    AbstractArrowEntity abstractarrowentity = apostle.getArrow(itemstack, Math.max(AttributesConfig.ApostleBowDamage.get() / 2.0F, 1.0F));
                    abstractarrowentity.addTag(ConstantPaths.rainArrow());
                    abstractarrowentity.setPos(this.getX() + this.random.nextInt(5), blockpos$mutable.getY(), this.getZ() + this.random.nextInt(5));
                    abstractarrowentity.shoot(0, -900, 0, 2, 10);
                    abstractarrowentity.setOwner(apostle);
                    this.level.addFreshEntity(abstractarrowentity);
                }
            } else {
                ItemStack itemStack = new ItemStack(Items.ARROW);
                ArrowItem arrowitem = (ArrowItem)(itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW);
                for(int i = 0; i < 3; ++i) {
                    AbstractArrowEntity abstractArrowEntity = arrowitem.createArrow(this.level, itemStack, this.getOwner());
                    abstractArrowEntity.addTag(ConstantPaths.rainArrow());
                    abstractArrowEntity.setPos(this.getX() + this.random.nextInt(5), blockpos$mutable.getY(), this.getZ() + this.random.nextInt(5));
                    abstractArrowEntity.shoot(0, -900, 0, 2, 0);
                    abstractArrowEntity.setOwner(this.getOwner());
                    this.level.addFreshEntity(abstractArrowEntity);
                }
            }
        } else {
            this.remove();
        }
        if (this.tickCount >= this.getDuration()) {
            this.remove();
        }
    }

}
