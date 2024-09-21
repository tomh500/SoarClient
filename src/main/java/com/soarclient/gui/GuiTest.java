package com.soarclient.gui;

import com.soarclient.stk.Stk;
import com.soarclient.stk.api.Align;
import com.soarclient.stk.api.Window;
import com.soarclient.stk.component.impl.button.Button;
import com.soarclient.stk.component.impl.button.ButtonHandler;
import com.soarclient.stk.component.impl.button.ButtonStyle;
import com.soarclient.stk.component.impl.checkbox.Checkbox;
import com.soarclient.stk.component.impl.checkbox.CheckboxHandler;
import com.soarclient.stk.component.impl.tswitch.Switch;
import com.soarclient.stk.component.impl.tswitch.SwitchHandler;

import net.minecraft.client.gui.GuiScreen;

public class GuiTest {

	public static GuiScreen getTestGui() {
		
		Window window = Stk.createWindow(1280, 720, Align.MIDDLE_CENTER);
		Button button = Stk.createButton("Hello World", ButtonStyle.FILLED_TONAL, window, Align.MIDDLE_CENTER);
		Switch tSwitch = Stk.createSwitch(window, Align.TOP_LEFT);
		Checkbox checkbox = Stk.createCheckbox(window, Align.TOP_RIGHT);
		
		button.setHandler(new ButtonHandler() {

			@Override
			public void onClicked() {
				System.out.println("Button Clicked!");
			}
		});
		
		tSwitch.setHandler(new SwitchHandler() {

			@Override
			public void onEnable() {
				System.out.println("Enable!");
			}

			@Override
			public void onDisable() {
				System.out.println("Disable!");
			}
		});
		
		checkbox.setHandler(new CheckboxHandler() {

			@Override
			public void onSelect() {
				System.out.println("Select!");
			}

			@Override
			public void onUnSelect() {
				System.out.println("UnSelect!");
			}
		});
		
		window.addComponent(button);
		window.addComponent(tSwitch);
		window.addComponent(checkbox);
		
		return Stk.create(window);
	}
}
