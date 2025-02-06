package com.soarclient.management.mod.impl.player;

import com.soarclient.management.mod.Mod;
import com.soarclient.management.mod.ModCategory;
import com.soarclient.management.mod.settings.impl.BooleanSetting;
import com.soarclient.skia.font.Icon;
import com.viaversion.viafabricplus.ViaFabricPlus;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

public class OldAnimationsMod extends Mod {

	private static OldAnimationsMod instance;
	private BooleanSetting oldBreakingSetting = new BooleanSetting("setting.oldbreaking",
			"setting.oldbreaking.description", Icon.SWORDS, this, true);
	private BooleanSetting disableAttackCooldownSetting = new BooleanSetting("setting.disableattackcooldown",
			"setting.disableattackcooldown.description", Icon.SCHEDULE, this, true);
	private BooleanSetting oldPvPSoundsSetting = new BooleanSetting("setting.oldpvpsounds",
			"setting.oldpvpsounds.description", Icon.SPEAKER, this, true);

	public OldAnimationsMod() {
		super("mod.oldanimations.name", "mod.oldanimations.description", Icon.ANIMATION, ModCategory.PLAYER);

		instance = this;
	}

	public static OldAnimationsMod getInstance() {
		return instance;
	}

	public boolean isOldBreaking() {
		return isv1_8() && oldBreakingSetting.isEnabled();
	}
	
	public boolean isDisableAttackCooldown() {
		return isv1_8() && disableAttackCooldownSetting.isEnabled();
	}
	
	public boolean isOldPvPSounds() {
		return isv1_8() && oldPvPSoundsSetting.isEnabled();
	}
	
	private boolean isv1_8() {
		return ViaFabricPlus.getImpl().getTargetVersion().equals(ProtocolVersion.v1_8);
	}
}
