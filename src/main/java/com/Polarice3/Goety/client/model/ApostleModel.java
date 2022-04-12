package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.hostile.cultists.AbstractCultistEntity;
import com.Polarice3.Goety.common.items.PitchforkItem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class ApostleModel<T extends AbstractCultistEntity> extends AbstractCultistModel<T> {

    public ApostleModel(float modelSize, float p_i47227_2_) {
        super(modelSize, p_i47227_2_);

        ModelRenderer halo = new ModelRenderer(this);
        halo.setPos(0.0F, 0.0F, 11.0F);
        this.head.addChild(halo);
        halo.texOffs(32, 0).addBox(-8.0F, -17.0F, -6.0F, 16.0F, 16.0F, 0.0F, 0.0F, false);
        this.hat.visible = false;
    }

}
