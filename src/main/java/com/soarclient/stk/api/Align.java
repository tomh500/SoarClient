package com.soarclient.stk.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public enum Align {
    TOP_LEFT(0, 0),
    TOP_CENTER(0.5f, 0),
    TOP_RIGHT(1, 0),
    MIDDLE_LEFT(0, 0.5f),
    MIDDLE_CENTER(0.5f, 0.5f),
    MIDDLE_RIGHT(1, 0.5f),
    BOTTOM_LEFT(0, 1),
    BOTTOM_CENTER(0.5f, 1),
    BOTTOM_RIGHT(1, 1),
    NONE(0, 0);

    private final float x;
    private final float y;

    private Align(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float[] getPosition() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        return getPosition(sr.getScaledWidth(), sr.getScaledHeight());
    }
    
    public float[] getPosition(float width, float height) {
        float xPos = width * x;
        float yPos = height * y;
        return new float[]{xPos, yPos};
    }

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public float[] getSpace(float space) {
		
		float ox = 0, oy = 0;
		
		if(x == 0) {
			ox = space;
		} else if(x == 1) {
			ox = -space;
		}
		
		if(y == 0) {
			oy = space;
		} else if(y == 1) {
			oy = -space;
		}
		
		return new float[] {ox, oy};
	}
}
