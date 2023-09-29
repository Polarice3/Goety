package com.Polarice3.Goety.client.gui.radial;

import net.minecraft.util.text.ITextComponent;

public class TextRadialMenuItem extends RadialMenuItem {
    private final ITextComponent text;
    private final int color;

    public ITextComponent getText() {
        return text;
    }

    public int getColor() {
        return color;
    }

    public TextRadialMenuItem(GenericRadialMenu owner, ITextComponent text) {
        super(owner);
        this.text = text;
        this.color = 0xFFFFFFFF;
    }

    public TextRadialMenuItem(GenericRadialMenu owner, ITextComponent text, int color) {
        super(owner);
        this.text = text;
        this.color = color;
    }

    @Override
    public void draw(DrawingContext context) {
        String textString = text.getString();
        float x = context.x - context.fontRenderer.width(textString) / 2.0f;
        float y = context.y - context.fontRenderer.lineHeight / 2.0f;
        context.fontRenderer.drawShadow(context.matrixStack, textString, x, y, color);
    }

    @Override
    public void drawTooltips(DrawingContext context) {
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