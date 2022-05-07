package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ShadeModel;
import com.Polarice3.Goety.common.entities.hostile.ShadeEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class ShadeRenderer extends MobRenderer<ShadeEntity, ShadeModel> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/shade.png");

    public ShadeRenderer(EntityRendererManager renderManagerIn){
        super(renderManagerIn, new ShadeModel(), 0.5F);
    }

    protected int getBlockLightLevel(ShadeEntity pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(ShadeEntity entity) {
        return TEXTURE;
    }
}
