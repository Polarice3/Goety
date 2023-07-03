package com.Polarice3.Goety.common.tileentities;

import com.Polarice3.Goety.common.blocks.CultStatueBlock;
import com.Polarice3.Goety.common.entities.hostile.cultists.AbstractCultistEntity;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModTileEntityType;
import com.Polarice3.Goety.utils.ModMathHelper;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class CultStatueTileEntity extends TileEntity implements ITickableTileEntity {
    public int tickCount;

    public CultStatueTileEntity() {
        super(ModTileEntityType.CULT_STATUE.get());
    }

    @Override
    public void tick() {
        if (this.getBlockState().getValue(CultStatueBlock.ENABLED)) {
            ++this.tickCount;
            if (!this.level.isClientSide) {
                int i = this.worldPosition.getX();
                int j = this.worldPosition.getY();
                int k = this.worldPosition.getZ();
                List<AbstractCultistEntity> cultists = this.level.getEntitiesOfClass(AbstractCultistEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(8.0D, 8.0D, 8.0D));
                if (this.tickCount % ModMathHelper.secondsToTicks(5) == 0){
                    for (AbstractCultistEntity cultist : cultists){
                        cultist.addEffect(new EffectInstance(ModEffects.BUFF.get(), 100));
                    }
                }
            }
        }
    }
}
