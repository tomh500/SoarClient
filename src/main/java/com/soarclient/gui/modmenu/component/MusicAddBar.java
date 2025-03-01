package com.soarclient.gui.modmenu.component;

import com.soarclient.Soar;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.management.mod.impl.settings.SystemSettings;
import com.soarclient.management.music.ytdlp.Ytdlp;
import com.soarclient.skia.Skia;
import com.soarclient.skia.font.Icon;
import com.soarclient.ui.component.Component;
import com.soarclient.ui.component.handler.impl.ButtonHandler;
import com.soarclient.ui.component.impl.IconButton;
import com.soarclient.ui.component.impl.IconButton.Size;
import com.soarclient.ui.component.impl.IconButton.Style;
import com.soarclient.ui.component.impl.text.TextField;
import com.soarclient.utils.OS;

public class MusicAddBar extends Component {

	private TextField urlField;
	private IconButton downloadButton;

	public MusicAddBar(float x, float y) {
		super(x, y);
		this.width = 80;
		this.height = 46;

		urlField = new TextField(x, y, 100, "");
		downloadButton = new IconButton(Icon.DOWNLOAD, x, y, Size.SMALL, Style.PRIMARY);
		downloadButton.setHandler(new ButtonHandler() {

			@Override
			public void onAction() {

				if (!urlField.getText().isEmpty() && !urlField.getText().isBlank()) {

					SystemSettings setting = SystemSettings.getInstance();
					Ytdlp ytdlp = new Ytdlp();

					if (OS.isWindows()) {
						ytdlp.setYtdlpPath(setting.getYtdlpPath());
						ytdlp.setFFmpegPath(setting.getFFmpegPath());
					} else if (OS.isLinux() || OS.isMacOS()) {
						ytdlp.setYtdlpCommand(setting.getYtdlpCommand());
					}

					ytdlp.download(urlField.getText());
				}
			}
		});
	}

	@Override
	public void draw(double mouseX, double mouseY) {
		
		Soar instance = Soar.getInstance();
		ColorPalette palette = instance.getColorManager().getPalette();

		Skia.drawRoundedRect(x, y, width, height, 16, palette.getSurface());
		
		downloadButton.draw(mouseX, mouseY);
		urlField.draw(mouseX, mouseY);
	}

	@Override
	public void mousePressed(double mouseX, double mouseY, int button) {
		downloadButton.mousePressed(mouseX, mouseY, button);
		urlField.mousePressed(mouseX, mouseY, button);
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		downloadButton.mouseReleased(mouseX, mouseY, button);
	}

	@Override
	public void charTyped(char chr, int modifiers) {
		urlField.charTyped(chr, modifiers);
	}

	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		urlField.keyPressed(keyCode, scanCode, modifiers);
	}
}
