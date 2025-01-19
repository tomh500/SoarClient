package com.soarclient.gui.modmenu.component;

import com.soarclient.management.mod.settings.Setting;
import com.soarclient.ui.component.Component;

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
	}

	@Override
	public void draw(int mouseX, int mouseY) {
		// TODO Auto-generated method stub
		
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
