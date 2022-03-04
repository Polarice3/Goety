package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.MutatedSheepModel;
import com.Polarice3.Goety.client.model.MutatedSheepWoolModel;
import com.Polarice3.Goety.common.entities.neutral.MutatedSheepEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MutatedSheepWoolLayer extends LayerRenderer<MutatedSheepEntity, MutatedSheepModel<MutatedSheepEntity>> {
    private static final ResourceLocation SHEEP_FUR_LOCATION = new ResourceLocation(Goety.MOD_ID, "textures/entity/mutatedsheepwool.png");
    private final MutatedSheepWoolModel<MutatedSheepEntity> model = new MutatedSheepWoolModel<>();

    public MutatedSheepWoolLayer(IEntityRenderer<MutatedSheepEntity, MutatedSheepModel<MutatedSheepEntity>> p_i50925_1_) {
        super(p_i50925_1_);
    }

    public void render(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, MutatedSheepEntity pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        float f;
        float f1;
        float f2;
        if (pLivingEntity.hasCustomName() && "jeb_".equals(pLivingEntity.getName().getContents())) {
            int i1 = 25;
            int i = pLivingEntity.tickCount / i1 + pLivingEntity.getId();
            int j = DyeColor.values().length;
            int k = i % j;
            int l = (i + 1) % j;
            float f3 = ((float)(pLivingEntity.tickCount % i1) + pPartialTicks) / 25.0F;
            float[] afloat1 = MutatedSheepEntity.getColorArray(DyeColor.byId(k));
            float[] afloat2 = MutatedSheepEntity.getColorArray(DyeColor.byId(l));
            f = afloat1[0] * (1.0F - f3) + afloat2[0] * f3;
            f1 = afloat1[1] * (1.0F - f3) + afloat2[1] * f3;
            f2 = afloat1[2] * (1.0F - f3) + afloat2[2] * f3;
        } else {
            float[] afloat = MutatedSheepEntity.getColorArray(pLivingEntity.getColor());
            f = afloat[0];
            f1 = afloat[1];
            f2 = afloat[2];
        }

        coloredCutoutModelCopyLayerRender(this.getParentModel(), this.model, SHEEP_FUR_LOCATION, pMatrixStack, pBuffer, pPackedLight, pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch, pPartialTicks, f, f1, f2);
    }
}
