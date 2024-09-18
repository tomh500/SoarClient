package com.soarclient.management.mods;

import com.soarclient.event.EventBus;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class Mod {
	
	protected Minecraft mc = Minecraft.getMinecraft();
	protected FontRenderer fr = mc.fontRendererObj;

	private String name, description, icon;
	private boolean enabled, movable;
	
	private ModCategory category;
	
	public Mod(String name, String description, String icon, ModCategory category) {
		this.name = name;
		this.description = description;
		this.category = category;
		this.movable = false;
	}
	
	public void onEnable() {
		EventBus.getInstance().register(this);
	}

	public void onDisable() {
		EventBus.getInstance().unregister(this);
	}

	public void toggle() {

		enabled = !enabled;

		if (enabled) {
			onEnable();
		} else {
			onDisable();
		}
	}

	public void setEnabled(boolean enabled) {

		this.enabled = enabled;

		if (enabled) {
			onEnable();
		} else {
			onDisable();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public boolean isMovable() {
		return movable;
	}

	public void setMovable(boolean movable) {
		this.movable = movable;
	}

	public ModCategory getCategory() {
		return category;
	}

	public void setCategory(ModCategory category) {
		this.category = category;
	}

	public boolean isEnabled() {
		return enabled;
	}
}
