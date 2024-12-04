package com.soarclient.libraries.sodium.client.gui.frame;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import com.soarclient.libraries.sodium.client.gui.options.control.ControlElement;
import com.soarclient.libraries.sodium.client.gui.utils.Drawable;
import com.soarclient.libraries.sodium.client.gui.utils.Element;
import com.soarclient.libraries.sodium.client.gui.utils.ParentElement;
import com.soarclient.libraries.sodium.client.gui.widgets.AbstractWidget;
import com.soarclient.libraries.sodium.client.util.Dim2i;

public abstract class AbstractFrame extends AbstractWidget implements ParentElement {
	protected final Dim2i dim;
	protected final List<AbstractWidget> children = new ArrayList();
	protected final List<Drawable> drawable = new ArrayList();
	protected final List<ControlElement<?>> controlElements = new ArrayList();
	protected boolean renderOutline;
	private Element focused;
	private boolean dragging;

	public AbstractFrame(Dim2i dim, boolean renderOutline) {
		this.dim = dim;
		this.renderOutline = renderOutline;
	}

	public void buildFrame() {
		for (Element element : this.children) {
			if (element instanceof AbstractFrame abstractFrame) {
				this.controlElements.addAll(abstractFrame.controlElements);
			}

			if (element instanceof ControlElement<?> controlElement) {
				this.controlElements.add(controlElement);
			}

			if (element instanceof Drawable drawable) {
				this.drawable.add(drawable);
			}
		}
	}

	@Override
	public void render(int mouseX, int mouseY, float delta) {
		if (this.renderOutline) {
			this.drawRectOutline((double) this.dim.getOriginX(), (double) this.dim.getOriginY(),
					(double) this.dim.getLimitX(), (double) this.dim.getLimitY(), -5592406);
		}

		for (Drawable drawable : this.drawable) {
			drawable.render(mouseX, mouseY, delta);
		}
	}

	public void applyScissor(int x, int y, int width, int height, Runnable action) {
		double scale = (double) new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
		GL11.glEnable(3089);
		GL11.glScissor((int) ((double) x * scale),
				(int) ((double) Minecraft.getMinecraft().getFramebuffer().framebufferHeight
						- (double) (y + height) * scale),
				(int) ((double) width * scale), (int) ((double) height * scale));
		action.run();
		GL11.glDisable(3089);
	}

	protected void drawRectOutline(double x, double y, double w, double h, int color) {
		float a = (float) (color >> 24 & 0xFF) / 255.0F;
		float r = (float) (color >> 16 & 0xFF) / 255.0F;
		float g = (float) (color >> 8 & 0xFF) / 255.0F;
		float b = (float) (color & 0xFF) / 255.0F;
		this.drawQuads(vertices -> {
			addQuad(vertices, x, y, w, y + 1.0, a, r, g, b);
			addQuad(vertices, x, h - 1.0, w, h, a, r, g, b);
			addQuad(vertices, x, y, x + 1.0, h, a, r, g, b);
			addQuad(vertices, w - 1.0, y, w, h, a, r, g, b);
		});
	}

	@Override
	public boolean isDragging() {
		return this.dragging;
	}

	@Override
	public void setDragging(boolean dragging) {
		this.dragging = dragging;
	}

	@Nullable
	@Override
	public Element getFocused() {
		return this.focused;
	}

	@Override
	public void setFocused(@Nullable Element focused) {
		this.focused = focused;
	}

	@Override
	public List<? extends Element> children() {
		return this.children;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY) {
		return this.dim.containsCursor(mouseX, mouseY);
	}
}
