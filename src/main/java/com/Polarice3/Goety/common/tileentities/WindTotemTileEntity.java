package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.init.ModTileEntityType;
import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;

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
        for (LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(this.getRange()))){
            if (livingEntity.isOnGround()) {
                if (!livingEntity.hasEffect(Effects.SLOW_FALLING)) {
                    if (livingEntity instanceof PlayerEntity) {
                        PlayerEntity player = (PlayerEntity) livingEntity;
                        if (!player.isCreative()) {
                            return livingEntity;
                        }
                    } else {
                        return livingEntity;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void SpecialEffect() {
        int i = this.worldPosition.getX();
        int j = this.worldPosition.getY();
        int k = this.worldPosition.getZ();
        this.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH);
        assert this.getLevel() != null;
        for (LivingEntity entity : this.getLevel().getEntitiesOfClass(LivingEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(this.getRange()))) {
            if (entity.isOnGround()) {
                if (!entity.hasEffect(Effects.SLOW_FALLING)) {
                    if (entity instanceof PlayerEntity) {
                        PlayerEntity player = (PlayerEntity) entity;
                        if (MobUtil.playerValidity(player, false)) {
                            this.launch(player);
                            player.hurtMarked = true;
                            if (!this.getLevel().isClientSide){
                                player.hasImpulse = true;
                                player.setOnGround(false);
                            }
                            player.addEffect(new EffectInstance(Effects.SLOW_FALLING, 100));
                        }
                    } else {
                        this.launch(entity);
                        entity.addEffect(new EffectInstance(Effects.SLOW_FALLING, 100));
                    }
                }
            }
        }
    }

    private void launch(LivingEntity livingEntity) {
        livingEntity.push(0.0D, 2.0D, 0.0D);
    }


}
