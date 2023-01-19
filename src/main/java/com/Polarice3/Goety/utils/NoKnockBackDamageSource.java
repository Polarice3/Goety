package com.Polarice3.Goety.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class NoKnockBackDamageSource extends DamageSource {
    @Nullable
    protected final Entity entity;
    @Nullable
    private final Entity owner;

    public NoKnockBackDamageSource(String pMessageId, @Nullable Entity pSource, @Nullable Entity pIndirectEntity) {
        super(pMessageId);
        this.entity = pSource;
        this.owner = pIndirectEntity;
    }

    @Nullable
    public Entity getDirectAttacker() {
        return this.entity;
    }

    @Nullable
    public Entity getOwner() {
        return this.owner;
    }

    public ITextComponent getLocalizedDeathMessage(LivingEntity pLivingEntity) {
        String s = "death.attack." + this.msgId;
        if (this.entity != null){
            ITextComponent itextcomponent = this.owner == null ? this.entity.getDisplayName() : this.owner.getDisplayName();
            ItemStack itemstack = this.owner instanceof LivingEntity ? ((LivingEntity)this.owner).getMainHandItem() : ItemStack.EMPTY;
            String s1 = s + ".item";
            return !itemstack.isEmpty() && itemstack.hasCustomHoverName() ? new TranslationTextComponent(s1, pLivingEntity.getDisplayName(), itextcomponent, itemstack.getDisplayName()) : new TranslationTextComponent(s, pLivingEntity.getDisplayName(), itextcomponent);
        } else {
            String s1 = s + ".player";
            return new TranslationTextComponent(s1, pLivingEntity.getDisplayName(), pLivingEntity.getDisplayName());
        }
    }
}
