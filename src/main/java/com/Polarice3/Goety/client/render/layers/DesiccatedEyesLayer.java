package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.Goety;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;

public class DesiccatedEyesLayer<T extends MobEntity & IRangedAttackMob, M extends SkeletonModel<T>> extends AbstractEyesLayer<T, M> {
    private static final RenderType EYES = RenderType.eyes(Goety.location("textures/entity/dead/desiccated_eyes.png"));

    public DesiccatedEyesLayer(IEntityRenderer<T, M> p_i50921_1_) {
        super(p_i50921_1_);
    }

    public RenderType renderType() {
        return EYES;
    }
}
