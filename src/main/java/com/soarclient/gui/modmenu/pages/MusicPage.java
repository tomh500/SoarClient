package com.soarclient.gui.modmenu.pages;

import java.util.ArrayList;
import java.util.List;

import com.soarclient.Soar;
import com.soarclient.animation.Animation;
import com.soarclient.animation.other.DummyAnimation;
import com.soarclient.gui.Page;
import com.soarclient.gui.modmenu.pages.components.MusicControlBar;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.management.music.Music;
import com.soarclient.management.music.MusicManager;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Fonts;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.utils.mouse.MouseUtils;
import com.soarclient.utils.tuples.Pair;

public class MusicPage extends Page {

	private List<Pair<Music, Animation>> items = new ArrayList<>();
	private MusicControlBar controlBar;
	
	public MusicPage(float x, float y, float width, float height) {
		super("text.music", Icon.MUSIC_NOTE, x, y, width, height);
		
		for(Music m : Soar.getInstance().getMusicManager().getMusics()) {
			items.add(Pair.of(m, new DummyAnimation(0)));
		}
		
		controlBar = new MusicControlBar(x + 22, y + height - 60 - 18, width - 44);
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		
		NanoVGHelper nvg = NanoVGHelper.getInstance();
		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();
		
		int index = 0;
		float offsetX = 32;
		float offsetY = 110;
		
		for(Pair<Music, Animation> i : items) {
			
			Music m = i.getFirst();
			
			String limitedTitle = nvg.getLimitText(m.getTitle(), 14, Fonts.REGULAR, 174);
			String limitedArtist = nvg.getLimitText(m.getArtist(), 12, Fonts.REGULAR, 174);
			
			if(m.getAlbum() != null) {
				nvg.drawRoundedImage(m.getAlbum(), x + offsetX, y + offsetY, 174, 174, 26);
			} else {
				nvg.drawRoundedRect(x + offsetX, y + offsetY, 174, 174, 26, palette.getSurfaceContainerHigh());
			}
			
			nvg.drawText(limitedTitle, x + offsetX, y + offsetY + 174 + 6, palette.getOnSurface(), 15, Fonts.REGULAR);
			nvg.drawText(limitedArtist, x + offsetX, y + offsetY + 174 + 6 + 17, palette.getOnSurfaceVariant(), 12, Fonts.REGULAR);
			
			offsetX += 174 + 32;
			index++;
			
			if(index % 4 == 0) {
				offsetX = 32;
				offsetY += 206 + 23;
			}
		}
		
		controlBar.draw(mouseX, mouseY);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		MusicManager musicManager = Soar.getInstance().getMusicManager();
		
		int index = 0;
		float offsetX = 32;
		float offsetY = 110;
		
		for(Pair<Music, Animation> i : items) {
			
			Music m = i.getFirst();
			
			if(MouseUtils.isInside(mouseX, mouseY, x + offsetX, y + offsetY, 174, 174) && mouseButton == 0) {
				if (musicManager.getCurrentMusic() != m) {
					musicManager.stop();
					musicManager.setCurrentMusic(m);
					musicManager.play();
				} else {
					musicManager.switchPlayBack();
				}
			}
			
			offsetX += 174 + 32;
			index++;
			
			if(index % 4 == 0) {
				offsetX = 32;
				offsetY += 206 + 22;
			}
		}
		
		controlBar.mouseClicked(mouseX, mouseY, mouseButton);
	}
}
