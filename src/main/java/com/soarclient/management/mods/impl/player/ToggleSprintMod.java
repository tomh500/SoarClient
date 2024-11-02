package com.soarclient.management.mods.impl.player;

import org.lwjgl.input.Keyboard;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.ClientTickEvent;
import com.soarclient.event.impl.PlayerUpdateEvent;
import com.soarclient.event.impl.RenderBlurEvent;
import com.soarclient.event.impl.RenderGameOverlayEvent;
import com.soarclient.management.mods.ModCategory;
import com.soarclient.management.mods.api.SimpleHUDMod;
import com.soarclient.management.mods.settings.impl.BooleanSetting;
import com.soarclient.nanovg.font.Icon;

import net.minecraft.client.settings.KeyBinding;

public class ToggleSprintMod extends SimpleHUDMod {

	private BooleanSetting hudSetting = new BooleanSetting("setting.hud", "setting.hud.description", Icon.MONITOR, this,
			true);
	private BooleanSetting alwaysSetting = new BooleanSetting("setting.always", "setting.always.description",
			Icon.SPRINT, this, false);

	private long startTime;
	private boolean wasDown;

	private State state;

	public ToggleSprintMod() {
		super("mod.togglesprint.name", "mod.togglesprint.description", Icon.SPRINT);

		this.setCategory(ModCategory.PLAYER);
		state = State.WALK;
	}

	@EventHandler
	@Override
	public void onRenderGameOverlay(RenderGameOverlayEvent event) {

		this.setMovable(hudSetting.isEnabled());

		if (hudSetting.isEnabled()) {
			super.onRenderGameOverlay(event);
		}
	}

	@EventHandler
	@Override
	public void onRenderBlur(RenderBlurEvent event) {
		if (hudSetting.isEnabled()) {
			super.onRenderBlur(event);
		}
	}

	@EventHandler
	public void onPlayerUpdate(PlayerUpdateEvent event) {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(),
				state.equals(State.HELD) || state.equals(State.TOGGLED) || alwaysSetting.isEnabled());
	}

	@EventHandler
	public void onClientTick(ClientTickEvent event) {

		boolean down = Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.getKeyCode());

		if (alwaysSetting.isEnabled() || mc.currentScreen != null) {
			return;
		}

		if (down) {
			if (!wasDown) {

				startTime = System.currentTimeMillis();

				if (state.equals(State.TOGGLED)) {
					state = State.HELD;
				} else {
					state = State.TOGGLED;
				}
			} else if ((System.currentTimeMillis() - startTime) > 250) {
				state = State.HELD;
			}
		} else if (state.equals(State.HELD) && mc.thePlayer.isSprinting()) {
			state = State.VANILLA;
		} else if ((state.equals(State.VANILLA) || state.equals(State.HELD)) && !mc.thePlayer.isSprinting()) {
			state = State.WALK;
		}

		wasDown = down;
	}

	@Override
	public String getText() {

		String prefix = "Sprinting";

		if (alwaysSetting.isEnabled()) {
			return prefix + " (Always)";
		}

		if (state.equals(State.WALK)) {
			return "Walking";
		}

		return prefix + " (" + state.name + ")";
	}

	@Override
	public String getIcon() {

		if (alwaysSetting.isEnabled()) {
			return Icon.SPRINT;
		}

		return state.icon;
	}

	private enum State {
		WALK("Walking", Icon.DIRECTIONS_WALK), VANILLA("Vanilla", Icon.DIRECTIONS_WALK), HELD("Key Held", Icon.SPRINT),
		TOGGLED("Toggled", Icon.SPRINT);

		private String name;
		private String icon;

		private State(String name, String icon) {
			this.name = name;
			this.icon = icon;
		}
	}
}
