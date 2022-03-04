package com.Polarice3.Goety.common.tileentities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.List;

public class WindTotemTileEntity extends TotemTileEntity {

    public WindTotemTileEntity() {
        this(ModTileEntityType.WIND_TOTEM.get());
    }

    public WindTotemTileEntity(TileEntityType<?> p_i48929_1_) {
        super(p_i48929_1_);
    }

    @Nullable
    @Override
    public LivingEntity findExistingTarget() {
        int i = this.worldPosition.getX();
        int j = this.worldPosition.getY();
        int k = this.worldPosition.getZ();
        assert this.level != null;
        List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(10.0D, 10.0D, 10.0D));
        if (list.size() > 0) {
            LivingEntity livingEntity = list.get(0);
            if (!livingEntity.hasEffect(Effects.SLOW_FALLING)) {
                if (livingEntity instanceof PlayerEntity) {
                    if (((PlayerEntity) livingEntity).isCreative()) {
                        if (list.size() > 1) {
                            return list.get(this.level.random.nextInt(list.size()));
                        } else {
                            return null;
                        }
                    } else {
                        return livingEntity;
                    }
                } else {
                    return livingEntity;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void SpecialEffect() {
        int i = this.worldPosition.getX();
        int j = this.worldPosition.getY();
        int k = this.worldPosition.getZ();
        this.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH);
        assert this.getLevel() != null;
        for (LivingEntity entity : this.getLevel().getEntitiesOfClass(LivingEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(10.0D, 10.0D, 10.0D))) {
            if (!entity.hasEffect(Effects.SLOW_FALLING)) {
                if (entity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) entity;
                    if (!player.isCreative()) {
                        this.launch(player);
                        player.addEffect(new EffectInstance(Effects.SLOW_FALLING, 100));
                    }
                } else {
                    this.launch(entity);
                    entity.addEffect(new EffectInstance(Effects.SLOW_FALLING, 100));
                }
            }
        }
    }

    private void launch(LivingEntity p_213688_1_) {
        double d0 = p_213688_1_.getX() - this.getBlockPos().getX();
        double d1 = p_213688_1_.getZ() - this.getBlockPos().getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        p_213688_1_.push(0.0D, 2.0D, 0.0D);
    }


}
