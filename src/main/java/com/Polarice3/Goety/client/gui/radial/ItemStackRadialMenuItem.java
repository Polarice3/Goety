package com.Polarice3.Goety.client.gui.radial;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class ItemStackRadialMenuItem extends TextRadialMenuItem {
    private final int slot;
    private final ItemStack stack;

    public int getSlot() {
        return slot;
    }

    public ItemStack getStack() {
        return stack;
    }

    public ItemStackRadialMenuItem(GenericRadialMenu owner, int slot, ItemStack stack, ITextComponent altText) {
        super(owner, altText, 0x7FFFFFFF);
        this.slot = slot;
        this.stack = stack;
    }

    @Override
    public void draw(DrawingContext context) {
        if (stack.getCount() > 0) {
            RenderHelper.turnBackOn();
            RenderSystem.pushMatrix();
            RenderSystem.translatef(-8, -8, context.z);
            context.itemRenderer.renderAndDecorateItem(stack, (int) context.x, (int) context.y);
            context.itemRenderer.renderGuiItemDecorations(context.fontRenderer, stack, (int) context.x, (int) context.y, "");
            RenderSystem.popMatrix();
            RenderHelper.turnOff();
        } else {
            super.draw(context);
        }
    }

    @Override
    public void drawTooltips(DrawingContext context) {
        if (stack.getCount() > 0) {
            context.drawingHelper.renderTooltip(context.matrixStack, stack, (int) context.x, (int) context.y);
        } else {
            super.drawTooltips(context);
        }
    }
}
/*
 * Copyright (c) 2015, David Quintana <gigaherz@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the author nor the
 *       names of the contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */