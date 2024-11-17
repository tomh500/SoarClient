package com.soarclient.gui.component.impl.settingbar;

import com.soarclient.Soar;
import com.soarclient.gui.component.Component;
import com.soarclient.gui.component.handler.impl.SliderHandler;
import com.soarclient.gui.component.handler.impl.SwitchHandler;
import com.soarclient.gui.component.impl.Switch;
import com.soarclient.gui.component.impl.sliders.Slider;
import com.soarclient.management.color.api.ColorPalette;
import com.soarclient.management.mods.settings.Setting;
import com.soarclient.management.mods.settings.impl.BooleanSetting;
import com.soarclient.management.mods.settings.impl.NumberSetting;
import com.soarclient.nanovg.NanoVGHelper;

public class SettingBar extends Component {

	private String title, description, icon;
	private Component component;
	private Setting setting;

	public SettingBar(String title, String description, String icon, Setting setting, float x, float y, float width) {
		super(x, y);
		this.title = title;
		this.description = description;
		this.icon = icon;
		this.setting = setting;
		this.width = width;
		this.height = 64;

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
			Slider slider = new Slider(0, 0, 0, nSetting.getValue(), nSetting.getMinValue(), nSetting.getMaxValue(),
					nSetting.getStep());
			
			slider.setHandler(new SliderHandler() {

				@Override
				public void onClicked(float value) {}

				@Override
				public void onReleased(float value) {}

				@Override
				public void onValueChanged(float value) {
					nSetting.setValue(value);
				}
			});
			
			component = slider;
		}
	}

	@Override
	public void draw(int mouseX, int mouseY) {

		NanoVGHelper nvg = NanoVGHelper.getInstance();
		ColorPalette palette = Soar.getInstance().getColorManager().getPalette();
		
		nvg.drawRoundedRect(x, y, width, height, 18, palette.getSurface());
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
