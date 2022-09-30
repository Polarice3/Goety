package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModTileEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.List;

public class MutateTotemTileEntity extends TotemTileEntity {

    public MutateTotemTileEntity() {
        this(ModTileEntityType.MUTATE_TOTEM.get());
    }

    public MutateTotemTileEntity(TileEntityType<?> p_i48929_1_) {
        super(p_i48929_1_);
    }

    @Nullable
    @Override
    public LivingEntity findExistingTarget() {
        int i = this.worldPosition.getX();
        int j = this.worldPosition.getY();
        int k = this.worldPosition.getZ();
        assert this.level != null;
        List<AnimalEntity> list = this.level.getEntitiesOfClass(AnimalEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(this.getRange()));
        if (list.size() > 0) {
            AnimalEntity animal = list.get(this.level.random.nextInt(list.size()));
            if (animal instanceof PigEntity || animal instanceof SheepEntity || animal instanceof CowEntity || animal instanceof ChickenEntity || animal instanceof RabbitEntity){
                return animal;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void SpecialEffect() {
        this.playSound(SoundEvents.ILLUSIONER_CAST_SPELL);
        if (this.target != null) {
            if (!this.target.hasEffect(ModEffects.COSMIC.get())
                    && (this.target instanceof CowEntity || this.target instanceof ChickenEntity || this.target instanceof SheepEntity
                    || this.target instanceof PigEntity || this.target instanceof RabbitEntity)) {
                this.target.addEffect(new EffectInstance(ModEffects.COSMIC.get(), 200));
            }
        }
    }
}
