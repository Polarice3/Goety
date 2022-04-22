package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModTileEntityType;
import com.Polarice3.Goety.utils.LichdomHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;
import java.util.List;

public class UndeadTotemTileEntity extends TotemTileEntity {

    public UndeadTotemTileEntity() {
        this(ModTileEntityType.UNDEAD_TOTEM.get());
    }

    public UndeadTotemTileEntity(TileEntityType<?> p_i48929_1_) {
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
            if (livingEntity.getMobType() == CreatureAttribute.UNDEAD && livingEntity.getActiveEffects().isEmpty()) {
                return livingEntity;
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
        this.playSound(SoundEvents.ILLUSIONER_PREPARE_MIRROR);
        for (LivingEntity entity : this.getLevel().getEntitiesOfClass(LivingEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(10.0D, 10.0D, 10.0D))) {
            float f = (float) MathHelper.atan2(entity.getZ() - this.getBlockPos().getZ(), entity.getX() - this.getBlockPos().getX());
            if (entity.getMobType() == CreatureAttribute.UNDEAD && entity.getActiveEffects().isEmpty()){
                entity.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 900, 1));
                entity.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 900, 1));
                entity.addEffect(new EffectInstance(Effects.ABSORPTION, 900, 1));
                entity.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 900, 1));
                entity.addEffect(new EffectInstance(ModEffects.NECROPOWER.get(), 900, 0, false, false));
            }
            if (entity instanceof PlayerEntity){
                if (LichdomHelper.isLich((PlayerEntity) entity) && entity.getActiveEffects().isEmpty()){
                    entity.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 900, 1));
                    entity.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 900, 1));
                    entity.addEffect(new EffectInstance(Effects.ABSORPTION, 900, 1));
                    entity.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 900, 1));
                }
            }
        }
    }

}
