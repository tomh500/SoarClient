package com.soarclient.management.mods.impl.hud;

import com.soarclient.event.EventHandler;
import com.soarclient.event.impl.AttackEntityEvent;
import com.soarclient.event.impl.ClientTickEvent;
import com.soarclient.event.impl.DamageEntityEvent;
import com.soarclient.management.mods.api.SimpleHUDMod;
import com.soarclient.nanovg.font.Icon;

public class ComboCounterMod extends SimpleHUDMod {

	private long hitTime = -1;
	private int combo, possibleTarget;

	public ComboCounterMod() {
		super("mod.combocounter.name", "mod.combocounter.description", Icon.PERSON_ADD);
	}

	@EventHandler
	public void onClientTick(ClientTickEvent event) {
		if ((System.currentTimeMillis() - hitTime) > 2000) {
			combo = 0;
		}
	}

	@EventHandler
	public void onAttackEntity(AttackEntityEvent event) {
		possibleTarget = event.getEntity().getEntityId();
	}

	@EventHandler
	public void onDamageEntity(DamageEntityEvent event) {
		if (event.getEntity().getEntityId() == possibleTarget) {
			combo++;
			possibleTarget = -1;
			hitTime = System.currentTimeMillis();
		} else if (event.getEntity() == mc.thePlayer) {
			combo = 0;
		}
	}

	@Override
	public String getText() {
		if (combo == 0) {
			return "No Combo";
		} else {
			return combo + " Combo";
		}
	}

	@Override
	public String getIcon() {
		return Icon.PERSON_ADD;
	}
}
