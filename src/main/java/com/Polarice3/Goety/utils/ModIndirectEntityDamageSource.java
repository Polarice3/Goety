package com.Polarice3.Goety.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class ModIndirectEntityDamageSource extends ModEntityDamageSource{
    private final Entity owner;

    public ModIndirectEntityDamageSource(String pDamageTypeId, Entity pSource, @Nullable Entity pIndirectEntity) {
        super(pDamageTypeId, pSource);
        this.owner = pIndirectEntity;
    }

    @Nullable
    public Entity getDirectEntity() {
        return this.entity;
    }

    @Nullable
    public Entity getEntity() {
        return this.owner;
    }

    public ITextComponent getLocalizedDeathMessage(LivingEntity pLivingEntity) {
        ITextComponent itextcomponent = this.owner == null ? this.entity.getDisplayName() : this.owner.getDisplayName();
        ItemStack itemstack = this.owner instanceof LivingEntity ? ((LivingEntity)this.owner).getMainHandItem() : ItemStack.EMPTY;
        String s = "death.attack." + this.msgId;
        String s1 = s + ".item";
        return !itemstack.isEmpty() && itemstack.hasCustomHoverName() ? new TranslationTextComponent(s1, pLivingEntity.getDisplayName(), itextcomponent, itemstack.getDisplayName()) : new TranslationTextComponent(s, pLivingEntity.getDisplayName(), itextcomponent);
    }
}
