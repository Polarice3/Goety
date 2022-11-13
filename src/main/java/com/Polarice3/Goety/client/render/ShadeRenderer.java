package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ShadeModel;
import com.Polarice3.Goety.common.entities.hostile.ShadeEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class ShadeRenderer extends MobRenderer<ShadeEntity, ShadeModel> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/shade.png");

    public ShadeRenderer(EntityRendererManager renderManagerIn){
        super(renderManagerIn, new ShadeModel(), 0.5F);
    }

    protected int getBlockLightLevel(ShadeEntity pEntity, BlockPos pPos) {
        return 15;
    }

    @Nullable
    protected RenderType getRenderType(ShadeEntity p_230496_1_, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
        return RenderType.entityTranslucent(TEXTURE);
    }

    @Override
    public ResourceLocation getTextureLocation(ShadeEntity entity) {
        return TEXTURE;
    }
}
