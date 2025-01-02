package com.soarclient.gui.modmenu.pages;

import java.util.ArrayList;
import java.util.List;

import com.soarclient.Soar;
import com.soarclient.animation.SimpleAnimation;
import com.soarclient.gui.api.page.Page;
import com.soarclient.gui.api.page.PageGui;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.management.music.Music;
import com.soarclient.management.music.MusicManager;
import com.soarclient.skia.Skia;
import com.soarclient.skia.font.Icon;
import com.soarclient.utils.mouse.MouseUtils;

public class MusicPage extends Page {

	private List<Item> items = new ArrayList<>();
	
	public MusicPage(PageGui parent) {
		super(parent, "text.music", Icon.MUSIC_NOTE);
		
		for(Music m : Soar.getInstance().getMusicManager().getMusics()) {
			items.add(new Item(m));
		}
	}

	@Override
	public void init() {
		for (Item i : items) {
			i.xAnimation.setFirstTick(true);
			i.yAnimation.setFirstTick(true);
		}
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		
		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();
		
		int index = 0;
		float offsetX = 32;
		float offsetY = 96;
		
		for(Item i : items) {
			
			Music m = i.music;
			SimpleAnimation xAnimation = i.xAnimation;
			SimpleAnimation yAnimation = i.yAnimation;
			
			float itemX = x + offsetX;
			float itemY = y + offsetY;
			
			xAnimation.onTick(itemX, 12);
			yAnimation.onTick(itemY, 12);

			itemX = xAnimation.getValue();
			itemY = yAnimation.getValue();
			
			if(m.getAlbum() != null) {
				Skia.drawRoundedImage(m.getAlbum(), itemX, itemY, 174, 174, 26);
			} else {
				Skia.drawRoundedRect(itemX, itemY, 174, 174, 26, palette.getSurfaceContainerHigh());
			}
			
			offsetX += 174 + 32;
			index++;

			if (index % 4 == 0) {
				offsetX = 32;
				offsetY += 206 + 23;
			}
		}
	}

	@Override
	public void mousePressed(int mouseX, int mouseY, int mouseButton) {
		
		for(Item i : items) {
			
			float itemX = i.xAnimation.getValue();
			float itemY = i.yAnimation.getValue();
			
			if(MouseUtils.isInside(mouseX, mouseY, itemX, itemY, 174, 174)) {
				i.pressed = true;
			}
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		
		MusicManager musicManager = Soar.getInstance().getMusicManager();
		
		for(Item i : items) {
			
			Music m = i.music;
			float itemX = i.xAnimation.getValue();
			float itemY = i.yAnimation.getValue();
			
			if(MouseUtils.isInside(mouseX, mouseY, itemX, itemY, 174, 174)) {
				if (musicManager.getCurrentMusic() != m) {
					musicManager.stop();
					musicManager.setCurrentMusic(m);
					musicManager.play();
				} else {
					musicManager.switchPlayBack();
				}
			}
			
			i.pressed = false;
		}
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
	}

	@Override
	public void onClosed() {
	}
	
	private class Item {
		
		private Music music;
		private SimpleAnimation xAnimation = new SimpleAnimation();
		private SimpleAnimation yAnimation = new SimpleAnimation();
		private SimpleAnimation focusAnimation = new SimpleAnimation();
		private boolean pressed;

		private Item(Music music) {
			this.music = music;
			this.pressed = false;
		}
	}
}