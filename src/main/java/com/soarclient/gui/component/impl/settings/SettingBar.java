package com.soarclient.gui.component.impl.settings;

import com.soarclient.Soar;
import com.soarclient.gui.component.Component;
import com.soarclient.gui.component.handler.impl.ComboButtonHandler;
import com.soarclient.gui.component.handler.impl.HctColorPickerHandler;
import com.soarclient.gui.component.handler.impl.KeybindHandler;
import com.soarclient.gui.component.handler.impl.SliderHandler;
import com.soarclient.gui.component.handler.impl.SwitchHandler;
import com.soarclient.gui.component.impl.HctColorPicker;
import com.soarclient.gui.component.impl.Keybind;
import com.soarclient.gui.component.impl.Switch;
import com.soarclient.gui.component.impl.buttons.ComboButton;
import com.soarclient.gui.component.impl.sliders.Slider;
import com.soarclient.libraries.material3.hct.Hct;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.management.mods.settings.Setting;
import com.soarclient.management.mods.settings.impl.BooleanSetting;
import com.soarclient.management.mods.settings.impl.ComboSetting;
import com.soarclient.management.mods.settings.impl.HctColorSetting;
import com.soarclient.management.mods.settings.impl.KeybindSetting;
import com.soarclient.management.mods.settings.impl.NumberSetting;
import com.soarclient.nanovg.NanoVGHelper;
import com.soarclient.nanovg.font.Fonts;
import com.soarclient.utils.language.I18n;

public class SettingBar extends Component {

	private String title, description, icon;
	private Component component;

	public SettingBar(Setting setting, float x, float y, float width) {
		super(x, y);
		this.title = setting.getName();
		this.description = setting.getDescription();
		this.icon = setting.getIcon();
		this.width = width;
		this.height = 68;

		if (setting instanceof BooleanSetting) {

			BooleanSetting bSetting = (BooleanSetting) setting;
			Switch switchComp = new Switch(x, y, bSetting.isEnabled());

			switchComp.setHandler(new SwitchHandler() {

				@Override
				public void onEnable() {
					bSetting.setEnabled(true);
				}

				@Override
				public void onDisable() {
					bSetting.setEnabled(false);
				}
			});

			component = switchComp;
		}

		if (setting instanceof NumberSetting) {

			NumberSetting nSetting = (NumberSetting) setting;
			Slider slider = new Slider(0, 0, 200, nSetting.getValue(), nSetting.getMinValue(), nSetting.getMaxValue(),
					nSetting.getStep());

			slider.setHandler(new SliderHandler() {

				@Override
				public void onClicked(float value) {
				}

				@Override
				public void onReleased(float value) {
				}

				@Override
				public void onValueChanged(float value) {
					nSetting.setValue(value);
				}
			});

			component = slider;
		}

		if (setting instanceof ComboSetting) {

			ComboSetting cSetting = (ComboSetting) setting;
			ComboButton button = new ComboButton(0, 0, cSetting.getOptions(), cSetting.getOption());

			button.setHandler(new ComboButtonHandler() {

				@Override
				public void onChanged(String option) {
					cSetting.setOption(option);
				}
			});

			component = button;
		}

		if (setting instanceof KeybindSetting) {

			KeybindSetting kSetting = (KeybindSetting) setting;
			Keybind bind = new Keybind(0, 0, kSetting.getKeyCode());

			bind.setHandler(new KeybindHandler() {

				@Override
				public void onBinded(int keyCode) {
					kSetting.setKeyCode(keyCode);
				}
			});

			component = bind;
		}

		if (setting instanceof HctColorSetting) {

			HctColorSetting hSetting = (HctColorSetting) setting;
			HctColorPicker picker = new HctColorPicker(0, 0, hSetting.getHct());

			picker.setHandler(new HctColorPickerHandler() {

				@Override
				public void onPicking(Hct hct) {
					hSetting.setHct(hct);
				}
			});

			component = picker;
		}
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();
		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();

		if (component != null) {
			component.setX(x + width - component.getWidth() - 22);
			component.setY(y + (height - component.getHeight()) / 2);
		}

		nvg.drawRoundedRect(x, y, width, height, 18, palette.getSurface());
		nvg.drawText(icon, x + 20, y + 21, palette.getOnSurface(), 32, Fonts.ICON);
		nvg.drawText(I18n.get(title), x + 58, y + 19, palette.getOnSurface(), 18, Fonts.REGULAR);
		nvg.drawText(I18n.get(description), x + 58, y + 37, palette.getOnSurfaceVariant(), 14, Fonts.REGULAR);

		if (component != null) {
			component.draw(mouseX, mouseY);
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

		if (component != null) {
			component.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

		if (component != null) {
			component.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {
		if (component != null) {
			component.keyTyped(typedChar, keyCode);
		}
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getIcon() {
		return icon;
	}
}
