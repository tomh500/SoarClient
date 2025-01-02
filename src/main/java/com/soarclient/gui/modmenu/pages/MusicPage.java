package com.soarclient.gui.modmenu.pages;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.soarclient.Soar;
import com.soarclient.animation.SimpleAnimation;
import com.soarclient.animation.other.DummyAnimation;
import com.soarclient.gui.api.page.Page;
import com.soarclient.gui.api.page.PageGui;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.management.music.Music;
import com.soarclient.management.music.MusicManager;
import com.soarclient.skia.Skia;
import com.soarclient.skia.font.Fonts;
import com.soarclient.skia.font.Icon;
import com.soarclient.utils.ColorUtils;
import com.soarclient.utils.MathUtils;
import com.soarclient.utils.mouse.MouseUtils;

import io.github.humbleui.skija.ClipMode;
import io.github.humbleui.skija.FilterTileMode;
import io.github.humbleui.skija.Image;
import io.github.humbleui.skija.ImageFilter;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Path;
import io.github.humbleui.types.RRect;
import io.github.humbleui.types.Rect;

public class MusicPage extends Page {

	private List<Item> items = new ArrayList<>();

	public MusicPage(PageGui parent) {
		super(parent, "text.music", Icon.MUSIC_NOTE);

		for (Music m : Soar.getInstance().getMusicManager().getMusics()) {
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

		MusicManager musicManager = Soar.getInstance().getMusicManager();
		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();

		int index = 0;
		float offsetX = 32;
		float offsetY = 96;

		for (Item i : items) {

			Music m = i.music;
			SimpleAnimation xAnimation = i.xAnimation;
			SimpleAnimation yAnimation = i.yAnimation;
			SimpleAnimation focusAnimation = i.focusAnimation;
			SimpleAnimation pressAnimation = i.pressAnimation;

			float itemX = x + offsetX;
			float itemY = y + offsetY;

			xAnimation.onTick(itemX, 12);
			yAnimation.onTick(itemY, 12);
			focusAnimation.onTick(MouseUtils.isInside(mouseX, mouseY, itemX, itemY, 174, 174) ? 1 : 0, 10);
			pressAnimation.onTick(i.pressed ? 1 : 0, 10);

			itemX = xAnimation.getValue();
			itemY = yAnimation.getValue();

			if (m.getAlbum() != null) {
				drawRoundedImage(m.getAlbum(), itemX, itemY, 174, 174, 26,
						(Math.abs(focusAnimation.getValue()) + 0.001F) * 6);
			} else {
				Skia.drawRoundedRect(itemX, itemY, 174, 174, 26, palette.getSurfaceContainerHigh());
			}

			String icon = musicManager.getCurrentMusic() != null && musicManager.getCurrentMusic().equals(m)
					&& musicManager.isPlaying() ? Icon.PAUSE : Icon.PLAY_ARROW;

			Skia.save();
			Skia.translate(0, 15 - (focusAnimation.getValue() * 15));
			Skia.drawFullCenteredText(icon, itemX + (174 / 2), itemY + (174 / 2),
					ColorUtils.applyAlpha(Color.WHITE, focusAnimation.getValue()), Fonts.getIconFill(64));
			Skia.restore();

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

		for (Item i : items) {

			float itemX = i.xAnimation.getValue();
			float itemY = i.yAnimation.getValue();

			if (MouseUtils.isInside(mouseX, mouseY, itemX, itemY, 174, 174)) {
				i.pressed = true;
			}
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

		MusicManager musicManager = Soar.getInstance().getMusicManager();

		for (Item i : items) {

			Music m = i.music;
			float itemX = i.xAnimation.getValue();
			float itemY = i.yAnimation.getValue();

			if (MouseUtils.isInside(mouseX, mouseY, itemX, itemY, 174, 174)) {

				i.pressedPos[0] = mouseX - itemX;
				i.pressedPos[1] = mouseY - itemY;

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

	private void drawRoundedImage(File file, float x, float y, float width, float height, float cornerRadius,
			float blurRadius) {

		Path path = new Path();
		path.addRRect(RRect.makeXYWH(x, y, width, height, cornerRadius));

		Paint blurPaint = new Paint();
		blurPaint.setImageFilter(ImageFilter.makeBlur(blurRadius, blurRadius, FilterTileMode.CLAMP));

		Skia.save();

		Skia.getCanvas().clipPath(path, ClipMode.INTERSECT, true);

		Skia.drawImage(file, x, y, width, height);

		if (Skia.getImageHelper().load(file)) {
			Image image = Skia.getImageHelper().get(file.getName());
			if (image != null) {
				Skia.getCanvas().drawImageRect(image, Rect.makeWH(image.getWidth(), image.getHeight()),
						Rect.makeXYWH(x, y, width, height), blurPaint, true);
			}
		}

		Skia.restore();
	}

	private class Item {

		private Music music;
		private SimpleAnimation xAnimation = new SimpleAnimation();
		private SimpleAnimation yAnimation = new SimpleAnimation();
		private SimpleAnimation focusAnimation = new SimpleAnimation();
		private SimpleAnimation pressAnimation = new SimpleAnimation();
		private float[] pressedPos = new float[] { 0, 0 };
		private boolean pressed;

		private Item(Music music) {
			this.music = music;
			this.pressed = false;
		}
	}
}