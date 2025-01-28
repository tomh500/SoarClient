package com.soarclient.management.mod.impl.hud;

import com.soarclient.event.EventBus;
import com.soarclient.event.impl.AttackEntityEvent;
import com.soarclient.event.impl.ClientTickEvent;
import com.soarclient.event.impl.DamageEntityEvent;
import com.soarclient.event.impl.RenderSkiaEvent;
import com.soarclient.management.mod.api.hud.SimpleHUDMod;
import com.soarclient.skia.font.Icon;

public class ComboCounterMod extends SimpleHUDMod {

	private long hitTime = -1;
	private int combo, possibleTarget;

	public ComboCounterMod() {
		super("mod.combocounter.name", "mod.combocounter.description", Icon.PERSON_ADD);
	}

	public final EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {
		this.draw();
	};

	public final EventBus.EventListener<DamageEntityEvent> onDamageEntity = event -> {
		if (event.getEntity().getId() == possibleTarget) {
			combo++;
			possibleTarget = -1;
			hitTime = System.currentTimeMillis();
		} else if (event.getEntity().getId() == client.player.getId()) {
			combo = 0;
		}
	};

	public final EventBus.EventListener<AttackEntityEvent> onAttackEntity = event -> {
		possibleTarget = event.getEntity().getId();
	};
	
	public final EventBus.EventListener<ClientTickEvent> onClientTick = event -> {
		if ((System.currentTimeMillis() - hitTime) > 2000) {
			combo = 0;
		}
	};

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
