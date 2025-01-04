package com.soarclient.gui.modmenu.pages.music;

import java.io.File;

import com.soarclient.Soar;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.management.music.Music;
import com.soarclient.management.music.MusicManager;
import com.soarclient.skia.Skia;
import com.soarclient.skia.font.Fonts;
import com.soarclient.ui.component.Component;

public class MusicControlBar extends Component {

	public MusicControlBar(float x, float y, float width) {
		super(x, y);
		this.width = width;
		this.height = 64;
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		
		Soar instance = Soar.getInstance();
		ColorPalette palette = instance.getColorManager().getPalette();
		MusicManager musicManager = instance.getMusicManager();
		Music music = musicManager.getCurrentMusic();
		
		Skia.drawRoundedRect(x, y, width, height, 16, palette.getSurface());
		
		File album = music != null ? music.getAlbum() : null;
		
		if(album != null) {
			Skia.drawRoundedImage(album, x + 8, y + 8, 48, 48, 10);
		} else {
			Skia.drawRoundedRect(x + 8, y + 8, 48, 48, 10, palette.getSurfaceContainerHigh());
		}
		
		if (music != null) {
			
			String limitedTitle = Skia.getLimitText(music.getTitle(), Fonts.getRegular(16), 170);
			String limitedArtist = Skia.getLimitText(music.getArtist(), Fonts.getRegular(12), 170);
			
			Skia.drawText(limitedTitle, x + 66, y + 17, palette.getOnSurface(), Fonts.getRegular(16));
			Skia.drawText(limitedArtist, x + 66, y + 34, palette.getOnSurfaceVariant(), Fonts.getRegular(12));
		}
	}

	@Override
	public void mousePressed(int mouseX, int mouseY, int mouseButton) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		// TODO Auto-generated method stub
		
	}

}
