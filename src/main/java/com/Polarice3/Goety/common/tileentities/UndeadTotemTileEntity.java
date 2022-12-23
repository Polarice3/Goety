package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModTileEntityType;
import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;

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
        for (LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(this.getRange()))) {
            if (livingEntity.getMobType() == CreatureAttribute.UNDEAD && livingEntity.getActiveEffects().isEmpty()) {
                return livingEntity;
            }
        }
        return null;
    }

    @Override
    public void SpecialEffect() {
        int i = this.worldPosition.getX();
        int j = this.worldPosition.getY();
        int k = this.worldPosition.getZ();
        this.playSound(SoundEvents.ILLUSIONER_PREPARE_MIRROR);
        for (LivingEntity entity : this.getLevel().getEntitiesOfClass(LivingEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(this.getRange()))) {
            if (!entity.hasEffect(ModEffects.NECROPOWER.get())){
                if (MobUtil.undeadAndLich(entity)){
                    entity.addEffect(new EffectInstance(ModEffects.NECROPOWER.get(), 900, 1, false, false));
                }
            }
        }
    }

}
