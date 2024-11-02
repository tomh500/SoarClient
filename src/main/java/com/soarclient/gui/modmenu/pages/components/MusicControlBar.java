package com.soarclient.gui.modmenu.pages.components;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.soarclient.Soar;
import com.soarclient.gui.component.Component;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.management.music.Music;
import com.soarclient.management.music.MusicManager;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Fonts;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.utils.mouse.MouseUtils;

public class MusicControlBar extends Component {

	private List<ControlButton> buttons = new ArrayList<>();

	public MusicControlBar(float x, float y, float width) {
		super(x, y);
		this.width = width;
		this.height = 60;

		MusicManager musicManager = Soar.getInstance().getMusicManager();
		
		buttons.add(new ControlButton(Icon.REPEAT, 0, y + 12, () -> {
			musicManager.setShuffle(false);
			musicManager.setRepeat(!musicManager.isRepeat());
		}));
		buttons.add(new ControlButton(Icon.SKIP_PREVIOUS, 0, y + 12, () -> {
			musicManager.back();
		}));
		buttons.add(new ControlButton(musicManager.isPlaying() ? Icon.PAUSE : Icon.PLAY_ARROW, 0, y + 12, () -> {
			musicManager.switchPlayBack();
		}));
		buttons.add(new ControlButton(Icon.SKIP_NEXT, 0, y + 12, () -> {
			musicManager.next();
		}));
		buttons.add(new ControlButton(Icon.SHUFFLE, 0, y + 12, () -> {
			musicManager.setRepeat(false);
			musicManager.setShuffle(!musicManager.isShuffle());
		}));

		float totalWidth = (buttons.size() * 28) + ((buttons.size() - 1) * 2);
		float centerX = x + (width / 2);
		float offsetX = centerX - (totalWidth / 2);

		for (ControlButton b : buttons) {
			b.setX(offsetX);
			offsetX += 30;
		}
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();
		Soar instance = Soar.getInstance();
		ColorPalette palette = instance.getColorManager().getPalette();
		MusicManager musicManager = instance.getMusicManager();

		nvg.drawRoundedRect(x, y, width, height, 16, palette.getSurface());

		Music music = musicManager.getCurrentMusic();
		File album = music != null ? music.getAlbum() : null;

		if (album != null) {
			nvg.drawRoundedImage(album, x + 13, y + 6, 48, 48, 8);
		} else {
			nvg.drawRoundedRect(x + 13, y + 6, 48, 48, 8, palette.getSurfaceContainerHigh());
		}

		if (music != null) {
			
			String limitedTitle = nvg.getLimitText(music.getTitle(), 15, Fonts.REGULAR, 170);
			String limitedArtist = nvg.getLimitText(music.getArtist(), 12, Fonts.REGULAR, 170);
			
			nvg.drawText(limitedTitle, x + 66, y + 16, palette.getOnSurface(), 15, Fonts.REGULAR);
			nvg.drawText(limitedArtist, x + 66, y + 34, palette.getOnSurfaceVariant(), 12, Fonts.REGULAR);
		}

		float seekWidth = 308;
		float seekHeight = 6;

		drawSeekBar(x + (width / 2) - (seekWidth / 2), y + height - seekHeight - 12, seekWidth, seekHeight);
		buttons.get(2).icon = musicManager.isPlaying() ? Icon.PAUSE : Icon.PLAY_ARROW;
		buttons.get(0).color = musicManager.isRepeat() ? palette.getPrimary() : palette.getOnSurface();
		buttons.get(4).color = musicManager.isShuffle() ? palette.getPrimary() : palette.getOnSurface();
		
		for (ControlButton b : buttons) {
			b.draw(mouseX, mouseY);
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (ControlButton b : buttons) {
			b.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	private void drawSeekBar(float x, float y, float width, float height) {

		Soar instance = Soar.getInstance();
		MusicManager musicManager = instance.getMusicManager();
		ColorPalette palette = instance.getColorManager().getPalette();
		NanoVGHelper nvg = NanoVGHelper.getInstance();

		float current = musicManager.getCurrentTime();
		float end = musicManager.getEndTime();
		String currentS = secondsToTime((int) current);

		nvg.drawRoundedRect(x, y, width, height, 3.5F, palette.getSurfaceContainerHigh());
		nvg.drawRoundedRect(x, y, (current / end) * width, height, 3.5F, palette.getPrimary());
	}

	private String secondsToTime(int totalSeconds) {

		int minutes = (totalSeconds % 3600) / 60;
		int seconds = totalSeconds % 60;

		return String.format("%02d:%02d", minutes, seconds);
	}

	private class ControlButton extends Component {

		private String icon;
		private Color color;
		private Runnable task;

		public ControlButton(String icon, float x, float y, Runnable task) {
			super(x, y);
			this.icon = icon;
			this.width = 26;
			this.height = 26;
			this.task = task;
			
			ColorPalette palette = Soar.getInstance().getColorManager().getPalette();
			
			this.color = palette.getOnSurface();
		}

		@Override
		public void draw(int mouseX, int mouseY) {

			NanoVGHelper nvg = NanoVGHelper.getInstance();

			nvg.drawText(icon, ControlButton.this.x, ControlButton.this.y, color, 28, Fonts.ICON_FILL);
		}

		@Override
		public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
			if(MouseUtils.isInside(mouseX, mouseY, ControlButton.this.x, ControlButton.this.y, 26, 26) && mouseButton == 0) {
				task.run();
			}
		}
	}
}
