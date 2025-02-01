package com.soarclient.libraries.soarium.gui.component;

import com.soarclient.libraries.soarium.gui.component.handler.SoariumSliderHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

public class GuiSoariumSlider extends GuiButton {

    private float sliderValue;
    public boolean dragging;
    private final float minValue;
    private final float maxValue;
    private final float snapInterval;
    private String displayText;
    private SoariumSliderHandler handler;

    public GuiSoariumSlider(int x, int y, String displayText, float initialValue, float minValue, float maxValue, SoariumSliderHandler handler) {
        super(-1, x, y, 150, 20, "");
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.snapInterval = 1;
        this.sliderValue = (initialValue - minValue) / (maxValue - minValue);
        this.displayText = displayText;
        this.handler = handler;
        updateDisplayString();
    }

    private void updateDisplayString() {
        this.displayString = String.format("%s: %s", displayText, getValue());
    }

    public int getValue() {
        return (int) (minValue + (maxValue - minValue) * sliderValue);
    }

    public void setValue(float value) {
        float snappedValue = snapValue(value);
        this.sliderValue = (snappedValue - minValue) / (maxValue - minValue);
        updateDisplayString();
    }

    private float snapValue(float value) {
        if (snapInterval <= 0) {
            return value;
        }
        float snappedValue = Math.round(value / snapInterval) * snapInterval;
        return MathHelper.clamp_float(snappedValue, minValue, maxValue);
    }

    @Override
    protected int getHoverState(boolean mouseOver) {
        return 0;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
                this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
                float value = minValue + (maxValue - minValue) * sliderValue;
                float snappedValue = snapValue(value);
                this.sliderValue = (snappedValue - minValue) / (maxValue - minValue);
                updateDisplayString();
            }

            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)),
                    this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)) + 4,
                    this.yPosition, 196, 66, 4, 20);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
            this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
            float value = minValue + (maxValue - minValue) * sliderValue;
            float snappedValue = snapValue(value);
            this.sliderValue = (snappedValue - minValue) / (maxValue - minValue);
            updateDisplayString();
            this.dragging = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (dragging) {
            handler.onValueChanged(getValue());
        }
        this.dragging = false;
    }
}