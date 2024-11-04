package com.soarclient.management.mods.impl.hud;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import com.soarclient.Soar;
import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.RenderBlurEvent;
import com.soarclient.event.impl.RenderGameOverlayEvent;
import com.soarclient.management.mods.api.SimpleHUDMod;
import com.soarclient.management.mods.settings.impl.ComboSetting;
import com.soarclient.management.music.Music;
import com.soarclient.management.music.MusicManager;
import com.soarclient.management.music.MusicPlayer;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Fonts;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.shaders.screen.ScreenWrapper;
import com.soarclient.utils.AdaptiveGaussianBlur;
import com.soarclient.utils.ColorUtils;
import com.soarclient.utils.ImageUtils;
import com.soarclient.utils.TimerUtils;

import net.minecraft.client.gui.ScaledResolution;

public class MusicInfoMod extends SimpleHUDMod {

	private final ScreenWrapper wrapper = new ScreenWrapper();
	private final TimerUtils timer = new TimerUtils();
	private float mx, my, dx, dy;
	private Music lastMusic;
	private int texture;
	private final ExecutorService imageProcessingExecutor = Executors.newSingleThreadExecutor();
	private volatile boolean isProcessingImage = false;
	private volatile BufferedImage processedImage = null;
	private volatile Color imageColor;

	private final ComboSetting typeSetting = new ComboSetting("setting.type", "setting.type.description",
			Icon.FORMAT_LIST_BULLETED, this,
			Arrays.asList("setting.simple", "setting.normal", "setting.waveform", "setting.cover"), "setting.simple");

	public MusicInfoMod() {
		super("mod.musicinfo.name", "mod.musicinfo.description", Icon.MUSIC_NOTE);
		dx = 1;
		dy = 1;
		texture = -1;
		imageColor = Color.BLACK;
	}

	@EventHandler
	public void onRenderGameOverlay(RenderGameOverlayEvent event) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();
		String type = typeSetting.getOption();

		float width = 180;
		float height = 45;
		float radius = 10;

		typeSetting.setOption("setting.cover");

		switch (type) {
		case "setting.simple":
			super.onRenderGameOverlay(event);
			break;
		case "setting.normal":
			nvg.setupAndDraw(() -> {
				renderer.drawBackground(width, height, radius);
			});
			nvg.setupAndDraw(() -> drawInfo(width, height, position.getScale()));
			position.setSize(width, height);
			break;
		case "setting.cover":
			if (texture == -1) {
				nvg.setupAndDraw(() -> {
					renderer.drawBackground(width, height, radius);
				});
			}
			drawCover(position.getX(), position.getY(), position.getWidth(), position.getHeight(),
					radius * position.getScale());
			nvg.setupAndDraw(() -> drawInfo(width, height, position.getScale()));
			position.setSize(width, height);
			break;
		case "setting.waveform":
			nvg.setupAndDraw(() -> drawWaveform(nvg));
			break;
		}
	}

	@Override
	@EventHandler
	public void onRenderBlur(RenderBlurEvent event) {

		String type = typeSetting.getOption();

		float radius = 10;

		switch (type) {
		case "setting.simple":
			super.onRenderBlur(event);
			break;
		case "setting.normal":
			event.setupAndDraw(() -> event.drawRoundedRect(position.getX(), position.getY(), position.getWidth(),
					position.getHeight(), radius * position.getScale(), 1F));
			break;
		case "setting.cover":
			event.setupAndDraw(() -> event.drawRoundedRect(position.getX(), position.getY(), position.getWidth(),
					position.getHeight(), radius * position.getScale(), 1F));
			break;
		}
	}

	private void drawWaveform(NanoVGHelper nvg) {

		ScaledResolution sr = new ScaledResolution(mc);
		MusicManager musicManager = Soar.getInstance().getMusicManager();
		Music m = musicManager.getCurrentMusic();
		
		if (m != lastMusic && !isProcessingImage) {
			processNewImage(m);
		}
		
		int offsetX = 0;
		
		if (musicManager.isPlaying()) {

			for (int i = 0; i < MusicPlayer.SPECTRUM_BANDS; i++) {

				MusicPlayer.ANIMATIONS[i].onTick(MusicPlayer.VISUALIZER[i], 10);
				nvg.drawRect(offsetX, sr.getScaledHeight() + MusicPlayer.ANIMATIONS[i].getValue(), 10,
						sr.getScaledHeight(), ColorUtils.applyAlpha(imageColor, 80));

				offsetX += 10;
			}
		}
		
		if (processedImage != null) {
			imageColor = ImageUtils.calculateAverageColor(processedImage);
			processedImage = null;
			isProcessingImage = false;
		}
	}

	private void drawInfo(float width, float height, float scale) {

		String type = typeSetting.getOption();

		NanoVGHelper nvg = NanoVGHelper.getInstance();
		MusicManager musicManager = Soar.getInstance().getMusicManager();
		Music m = musicManager.getCurrentMusic();
		float padding = 4.5F;
		float albumSize = height - (padding * 2);

		Color textColor = type.equals("setting.cover") ? Color.WHITE : renderer.getTextColor();

		if (m != null && m.getAlbum() != null) {
			nvg.drawRoundedImage(m.getAlbum(), position.getX() + padding * scale, position.getY() + padding * scale,
					albumSize * scale, albumSize * scale, 6 * scale);
		} else {
			nvg.drawRoundedRect(position.getX() + padding * scale, position.getY() + padding * scale, albumSize * scale,
					albumSize * scale, 6 * scale, ColorUtils.applyAlpha(textColor, 0.2F));
		}

		if (m != null) {

			float offsetX = (padding * 2) + albumSize;
			String limitedTitle = nvg.getLimitText(m.getTitle(), 9, Fonts.REGULAR, width - offsetX - 12);
			String limitedArtist = nvg.getLimitText(m.getArtist(), 6.5F, Fonts.REGULAR, width - offsetX - 10);

			renderer.drawText(limitedTitle, offsetX, padding + 3F, textColor, 9, Fonts.REGULAR);
			renderer.drawText(limitedArtist, offsetX, padding + 13F, ColorUtils.applyAlpha(textColor, 0.8F), 6.5F,
					Fonts.REGULAR);
		}
	}

	private void drawCover(float x, float y, float width, float height, float radius) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();
		Music m = Soar.getInstance().getMusicManager().getCurrentMusic();
		float scale = position.getScale();
		float coverSize = 256 * scale;

		if (m != lastMusic && !isProcessingImage) {
			processNewImage(m);
		}

		nvg.setupAndDraw(() -> {
			nvg.drawShadow(x, y, width, height, radius);
		});

		wrapper.wrap(() -> {
			nvg.setupAndDraw(() -> {
				nvg.save();
				nvg.scale(x, y, scale);
				if (texture != -1) {
					nvg.drawImage(texture, x - mx, y - my, coverSize, coverSize, 1F);
				}
				nvg.restore();
			});
		}, x, y, width, height, radius, 1, 1, true);

		if (timer.delay(80)) {
			updatePosition(width, height, coverSize);
			timer.reset();
		}

		if (processedImage != null) {
			texture = ImageUtils.toTexture(processedImage);
			processedImage = null;
			isProcessingImage = false;
		}
	}

	private void processNewImage(Music music) {

		if (isProcessingImage || music == null || music.getAlbum() == null) {
			texture = -1;
			imageColor = Color.BLACK;
			return;
		}

		isProcessingImage = true;

		CompletableFuture.supplyAsync(() -> {
			try {
				BufferedImage source = ImageIO.read(music.getAlbum());
				return AdaptiveGaussianBlur.applyGaussianBlur(source, 50);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}, imageProcessingExecutor).thenAccept(result -> {
			if (result != null) {
				processedImage = result;
				lastMusic = music;
			} else {
				isProcessingImage = false;
			}
		});
	}

	private void updatePosition(float width, float height, float coverSize) {

		mx += dx;
		my += dy;

		if (mx <= 0 || mx + width >= coverSize) {
			dx = -dx;
			if (mx <= 0) {
				mx = 0;
			}
			if (mx + width >= coverSize) {
				mx = coverSize - width;
			}
		}
		if (my <= 0 || my + height >= coverSize) {
			dy = -dy;
			if (my <= 0) {
				my = 0;
			}
			if (my + height >= coverSize) {
				my = coverSize - height;
			}
		}
	}

	public void cleanup() {
		imageProcessingExecutor.shutdown();
	}

	@Override
	public String getText() {

		MusicManager musicManager = Soar.getInstance().getMusicManager();

		if (musicManager.getCurrentMusic() != null && musicManager.isPlaying()) {
			return "Playing: " + musicManager.getCurrentMusic().getTitle();
		} else {
			return "Nothing is Playing";
		}
	}

	@Override
	public String getIcon() {
		return Icon.MUSIC_NOTE;
	}
}