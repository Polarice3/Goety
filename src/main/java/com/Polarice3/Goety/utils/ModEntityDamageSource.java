package com.Polarice3.Goety.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class ModEntityDamageSource extends ModDamageSource{
    @Nullable
    protected final Entity entity;

    public ModEntityDamageSource(String pDamageTypeId, @Nullable Entity pEntity) {
        super(pDamageTypeId);
        this.entity = pEntity;
    }

    @Nullable
    public Entity getEntity() {
        return this.entity;
    }

    public ITextComponent getLocalizedDeathMessage(LivingEntity pLivingEntity) {
        ItemStack itemstack = this.entity instanceof LivingEntity ? ((LivingEntity)this.entity).getMainHandItem() : ItemStack.EMPTY;
        String s = "death.attack." + this.msgId;
        return !itemstack.isEmpty() && itemstack.hasCustomHoverName() ? new TranslationTextComponent(s + ".item", pLivingEntity.getDisplayName(), this.entity.getDisplayName(), itemstack.getDisplayName()) : new TranslationTextComponent(s, pLivingEntity.getDisplayName(), this.entity.getDisplayName());
    }

    public boolean scalesWithDifficulty() {
        return this.entity != null && this.entity instanceof LivingEntity && !(this.entity instanceof PlayerEntity);
    }

    @Nullable
    public Vector3d getSourcePosition() {
        return this.entity != null ? this.entity.position() : null;
    }

    public String toString() {
        return "ModEntityDamageSource (" + this.entity + ")";
    }
}
