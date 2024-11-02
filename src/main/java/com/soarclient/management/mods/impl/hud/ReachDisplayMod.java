package com.soarclient.management.mods.impl.hud;

import java.text.DecimalFormat;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.DamageEntityEvent;
import com.soarclient.management.mods.api.SimpleHUDMod;
import com.soarclient.nanovg.font.Icon;

import net.minecraft.util.MovingObjectPosition;

public class ReachDisplayMod extends SimpleHUDMod {

	private DecimalFormat df = new DecimalFormat("0.##");

	private double distance = 0;
	private long hitTime = -1;

	public ReachDisplayMod() {
		super("mod.reachdisplay.name", "mod.reachdisplay.description", Icon.SOCIAL_DISTANCE);
	}

	@EventHandler
	public void onDamageEntity(DamageEntityEvent event) {
		if (mc.objectMouseOver != null && mc.objectMouseOver.hitVec != null
				&& mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
			distance = mc.objectMouseOver.hitVec.distanceTo(mc.thePlayer.getPositionEyes(1.0F));
			hitTime = System.currentTimeMillis();
		}
	}

	@Override
	public String getText() {

		if ((System.currentTimeMillis() - hitTime) > 5000) {
			distance = 0;
		}

		if (distance == 0) {
			return "Hasn't attacked";
		} else {
			return df.format(distance) + " blocks";
		}
	}

	@Override
	public String getIcon() {
		return Icon.SOCIAL_DISTANCE;
	}
}
