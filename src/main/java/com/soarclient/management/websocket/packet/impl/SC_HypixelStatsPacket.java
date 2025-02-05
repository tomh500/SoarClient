package com.soarclient.management.websocket.packet.impl;

import com.google.gson.JsonObject;
import com.soarclient.management.websocket.packet.SoarPacket;

public class SC_HypixelStatsPacket extends SoarPacket {

	private final String uuid;
	
	protected SC_HypixelStatsPacket(String uuid) {
		super("sc-hypixel-stats");
		this.uuid = uuid;
	}

	@Override
	public JsonObject toJson() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("uuid", uuid);
		return jsonObject;
	}
}
