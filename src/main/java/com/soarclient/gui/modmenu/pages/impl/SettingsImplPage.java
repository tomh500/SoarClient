package com.soarclient.gui.modmenu.pages.impl;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.soarclient.gui.Page;
import com.soarclient.gui.PageDirection;
import com.soarclient.gui.component.Component;
import com.soarclient.gui.modmenu.GuiModMenu;
import com.soarclient.management.mods.Mod;
import com.soarclient.management.mods.settings.Setting;
import com.soarclient.nanovg.font.Icon;
import com.soarclient.utils.mouse.ScrollHelper;
import com.soarclient.utils.tuples.Pair;

public class SettingsImplPage extends Page {
	
	private List<Pair<Setting, Component>> components = new ArrayList<>();
	private ScrollHelper scrollHelper = new ScrollHelper();
	private GuiModMenu parent;
	private Class<? extends Page> prevPage;
	private Mod mod;

	public SettingsImplPage(GuiModMenu parent, Class<? extends Page> prevPage, float x, float y, float width, float height, Mod mod) {
		super(PageDirection.RIGHT, "text.mods", Icon.SETTINGS, x, y, width, height);

		this.prevPage = prevPage;
		this.parent = parent;
		this.mod = mod;
		this.parent.setCloseable(false);
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {
		if(keyCode == Keyboard.KEY_ESCAPE) {
			parent.setPage(prevPage);
			parent.setCloseable(true);
		}
	}
}
