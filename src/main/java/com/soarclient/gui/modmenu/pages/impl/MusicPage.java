package com.soarclient.gui.modmenu.pages.impl;

import java.util.ArrayList;
import java.util.List;

import com.soarclient.Soar;
import com.soarclient.animation.SimpleAnimation;
import com.soarclient.gui.Page;
import com.soarclient.gui.PageDirection;
import com.soarclient.gui.component.impl.text.SearchBar;
import com.soarclient.gui.modmenu.pages.components.MusicControlBar;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.management.music.Music;
import com.soarclient.management.music.MusicManager;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Fonts;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.utils.ColorUtils;
import com.soarclient.utils.SearchUtils;
import com.soarclient.utils.language.I18n;
import com.soarclient.utils.mouse.MouseUtils;
import com.soarclient.utils.mouse.ScrollHelper;
import com.soarclient.utils.tuples.Pair;

public class MusicPage extends Page {

	private SearchBar searchBar;
	private ScrollHelper scrollHelper = new ScrollHelper();

	private List<Pair<Music, SimpleAnimation>> items = new ArrayList<>();
	private MusicControlBar controlBar;

	public MusicPage(float x, float y, float width, float height) {
		super(PageDirection.LEFT, "text.music", Icon.MUSIC_NOTE, x, y, width, height);

		for (Music m : Soar.getInstance().getMusicManager().getMusics()) {
			items.add(Pair.of(m, new SimpleAnimation()));
		}

		controlBar = new MusicControlBar(x + 22, y + height - 60 - 18, width - 44);
		searchBar = new SearchBar(x + width - 260 - 32, y + 32, 260, () -> {
			scrollHelper.reset();
		});
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		MusicManager musicManager = Soar.getInstance().getMusicManager();
		NanoVGHelper nvg = NanoVGHelper.getInstance();
		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();

		int index = 0;
		float offsetX = 32;
		float offsetY = 106;

		scrollHelper.onScroll();

		mouseY = (int) (mouseY - scrollHelper.getValue());

		nvg.save();
		nvg.translate(0, scrollHelper.getValue());

		searchBar.draw(mouseX, mouseY);

		for (Pair<Music, SimpleAnimation> i : items) {

			Music m = i.getFirst();
			SimpleAnimation a = i.getSecond();

			if (!searchBar.getText().isEmpty()
					&& !SearchUtils.isSimillar(m.getTitle() + " " + m.getArtist(), searchBar.getText())) {
				continue;
			}

			float itemX = x + offsetX;
			float itemY = y + offsetY;

			if (itemY + 206 < y - scrollHelper.getValue() || itemY > y + height - scrollHelper.getValue()) {
				index++;
				offsetX += 174 + 32;
				if (index % 4 == 0) {
					offsetX = 32;
					offsetY += 206 + 23;
				}
				continue;
			}

			a.onTick(MouseUtils.isInside(mouseX, mouseY, itemX, itemY, 174, 174) ? 1 : 0, 16);

			String limitedTitle = nvg.getLimitText(m.getTitle(), 14, Fonts.REGULAR, 174);
			String limitedArtist = nvg.getLimitText(m.getArtist(), 12, Fonts.REGULAR, 174);

			if (m.getAlbum() != null) {
				nvg.drawRoundedImage(m.getAlbum(), itemX, itemY, 174, 174, 26);
			} else {
				nvg.drawRoundedRect(itemX, itemY, 174, 174, 26, palette.getSurfaceContainerHigh());
			}

			nvg.drawText(limitedTitle, itemX, itemY + 174 + 6, palette.getOnSurface(), 15, Fonts.REGULAR);
			nvg.drawText(limitedArtist, itemX, itemY + 174 + 6 + 17, palette.getOnSurfaceVariant(), 12, Fonts.REGULAR);

			String icon = musicManager.getCurrentMusic() != null && musicManager.getCurrentMusic().equals(m)
					&& musicManager.isPlaying() ? Icon.PAUSE : Icon.PLAY_ARROW;

			nvg.drawRoundedRect(itemX, itemY, 174, 174, 26,
					ColorUtils.applyAlpha(palette.getOnSurface(), a.getValue() / 6));

			nvg.save();
			nvg.translate(0, 10 - (a.getValue() * 10));
			nvg.drawAlignCenteredText(icon, itemX + (174 / 2), itemY + (174 / 2),
					ColorUtils.applyAlpha(palette.getSurface(), a.getValue()), 60, Fonts.ICON_FILL);
			nvg.restore();

			offsetX += 174 + 32;
			index++;

			if (index % 4 == 0) {
				offsetX = 32;
				offsetY += 206 + 23;
			}
		}

		nvg.restore();

		controlBar.draw(mouseX, mouseY);
		scrollHelper.setMaxScroll(106 + 26 + 60 + 8, items.size(), height, 206, 23, 4);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		MusicManager musicManager = Soar.getInstance().getMusicManager();

		controlBar.mouseClicked(mouseX, mouseY, mouseButton);

		if (MouseUtils.isInside(mouseX, mouseY, controlBar.getX(), controlBar.getY(), controlBar.getWidth(),
				controlBar.getHeight())) {
			return;
		}

		if (mouseX < x || mouseX > x + width || mouseY < y || mouseY > y + height) {
			return;
		}

		int index = 0;
		float offsetX = 32;
		float offsetY = 106;

		mouseY = (int) (mouseY - scrollHelper.getValue());

		searchBar.mouseClicked(mouseX, mouseY, mouseButton);

		for (Pair<Music, SimpleAnimation> i : items) {
			
			Music m = i.getFirst();

			if (!searchBar.getText().isEmpty()
					&& !SearchUtils.isSimillar(m.getTitle() + " " + m.getArtist(), searchBar.getText())) {
				continue;
			}
			
			float itemX = x + offsetX;
			float itemY = y + offsetY;

			if (itemY + 206 < y - scrollHelper.getValue() || itemY > y + height - scrollHelper.getValue() - (60 + 18)) {
				index++;
				offsetX += 174 + 32;
				if (index % 4 == 0) {
					offsetX = 32;
					offsetY += 206 + 22;
				}
				continue;
			}

			if (MouseUtils.isInside(mouseX, mouseY, itemX, itemY, 174, 174) && mouseButton == 0) {
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

			if (index % 4 == 0) {
				offsetX = 32;
				offsetY += 206 + 22;
			}
		}
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		searchBar.keyTyped(typedChar, keyCode);
	}
}