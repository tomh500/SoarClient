package com.soarclient.gui.modmenu.toolbar;

import java.util.List;

import com.soarclient.animation.SimpleAnimation;
import com.soarclient.event.impl.RenderBlurEvent;
import com.soarclient.gui.Page;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Fonts;
import com.soarclient.ui.UIRenderer;
import com.soarclient.ui.color.Colors;
import com.soarclient.ui.math.Style;
import com.soarclient.utils.mouse.MouseUtils;

public class Toolbar {

	private SimpleAnimation animation = new SimpleAnimation();
	private List<Page> pages;
	private Page currentPage;
	private final float x, y, width, height;
	private float pageY;

	public Toolbar(List<Page> pages, float x, float y, float width) {
		this.pages = pages;
		this.x = x;
		this.width = width;
		this.currentPage = pages.get(0);
		
		NanoVGHelper nvg = NanoVGHelper.getInstance();
		float totalY = Style.px(8);
		
		for (Page p : pages) {
			float tHeight = nvg.getTextHeight(p.getIcon(), Style.px(34), Fonts.ICON);
			totalY += tHeight + Style.px(28);
		}
		
		this.height = totalY;
		this.y = y - (height / 2);
	}

	public void draw(int mouseX, int mouseY) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();

		float offsetY = Style.px(38);

		UIRenderer.drawWindow(x, y, width, height, Style.px(35));

		for (Page p : pages) {

			float tHeight = nvg.getTextHeight(p.getIcon(), Style.px(34), Fonts.ICON);

			if (p.equals(currentPage)) {

				float r = Style.px(25);

				pageY = y + offsetY - Style.px(3F);
				animation.onTick(pageY, 16);
				nvg.drawCircle(x + (width / 2), animation.getValue(), r, Colors.SURFACE_HIGH);
			}
			
			nvg.drawAlignCenteredText(p.getIcon(), x + (width / 2), y + offsetY,
					p.equals(currentPage) ? Colors.TEXT_PRIMARY : Colors.TEXT_SECONDARY, Style.px(34), Fonts.ICON);

			offsetY += tHeight + Style.px(28);
		}
	}

	public void drawBlur(RenderBlurEvent event) {
		event.drawRoundedRect(x, y, width, height, Style.px(35), 1.0F);
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		NanoVGHelper nvg = NanoVGHelper.getInstance();
		
		float offsetY = Style.px(40);
		
		for (Page p : pages) {
			
			float tHeight = nvg.getTextHeight(p.getIcon(), Style.px(34), Fonts.ICON);
			
			if(MouseUtils.isInside(mouseX, mouseY, x, y + offsetY - Style.px(26), width, Style.px(48)) && mouseButton == 0) {
				currentPage = p;
			}
			
			offsetY += tHeight + Style.px(28);
		}
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
}
