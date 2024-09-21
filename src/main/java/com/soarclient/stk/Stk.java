package com.soarclient.stk;

import com.soarclient.stk.api.Align;
import com.soarclient.stk.api.Window;
import com.soarclient.stk.component.Component;
import com.soarclient.stk.component.impl.Box;
import com.soarclient.stk.component.impl.button.Button;
import com.soarclient.stk.component.impl.button.ButtonStyle;
import com.soarclient.stk.component.impl.checkbox.Checkbox;
import com.soarclient.stk.component.impl.tswitch.Switch;
import com.soarclient.stk.gui.GuiSoarScreen;

public class Stk {
	
	public static GuiSoarScreen create(Window window) {
		return new GuiSoarScreen(window);
	}
	
	public static Window createWindow(float width, float height, Align align) {
		return new Window(width, height, align);
	}
	
	public static Box createBox(float width, float height, Align align) {
		return new Box(width, height, align);
	}
	
	public static Button createButton(String text, ButtonStyle style, Component parent, Align align) {
		return new Button(text, style, parent, align);
	}
	
	public static Button createButton(String text, String icon, ButtonStyle style, Component parent, Align align) {
		return new Button(text, icon, style, parent, align);
	}
	
	public static Button createButton(String text, ButtonStyle style, float x, float y) {
		return new Button(text, style, x, y);
	}
	
	public static Button createButton(String text, String icon, ButtonStyle style, float x, float y) {
		return new Button(text, icon, style, x, y);
	}
	
	public static Switch createSwitch(Switch.State state, Component parent, Align align) {
		return new Switch(state, parent, align);
	}
	
	public static Switch createSwitch(Component parent, Align align) {
		return new Switch(parent, align);
	}
	
	public static Switch createSwitch(Switch.State state, float x, float y) {
		return new Switch(state, x, y);
	}
	
	public static Switch createSwitch(float x, float y) {
		return new Switch(x, y);
	}
	
	public static Checkbox createCheckbox(boolean selected, Component parent, Align align) {
		return new Checkbox(selected, parent, align);
	}
	
	public static Checkbox createCheckbox(Component parent, Align align) {
		return new Checkbox(parent, align);
	}
}
