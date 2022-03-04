package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.ally.TamedSpiderEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SpiderModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TamedSpiderCollarLayer <T extends TamedSpiderEntity> extends LayerRenderer<T, SpiderModel<T>> {
    private static final ResourceLocation COLLAR_LOCATION = new ResourceLocation(Goety.MOD_ID, "textures/entity/tamed_spider_collar.png");

    public TamedSpiderCollarLayer(IEntityRenderer<T, SpiderModel<T>> p_i50914_1_) {
        super(p_i50914_1_);
    }

    public void render(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (!pLivingEntity.isInvisible()) {
            float[] afloat = pLivingEntity.getCollarColor().getTextureDiffuseColors();
            renderColoredCutoutModel(this.getParentModel(), COLLAR_LOCATION, pMatrixStack, pBuffer, pPackedLight, pLivingEntity, afloat[0], afloat[1], afloat[2]);
        }
    }
}
