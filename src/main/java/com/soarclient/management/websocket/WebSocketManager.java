package com.soarclient.management.websocket;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.soarclient.management.websocket.client.SoarWebSocketClient;
import com.soarclient.management.websocket.packet.SoarPacket;
import com.soarclient.utils.HttpUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;

public class WebSocketManager {

	private MinecraftClient client = MinecraftClient.getInstance();
	private GameProfile gameProfile;
	private SoarWebSocketClient webSocket;
	
	public WebSocketManager() {
		new Thread("Websocket Thread") {
			@Override
			public void run() {
				while (true) {
					
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
					}
					
					if(webSocket == null || gameProfile == null || !gameProfile.equals(client.getGameProfile()) || webSocket.isClosed()) {
						
						gameProfile = client.getGameProfile();
						
						if(webSocket != null) {
							webSocket.close();
						}
						
						JsonObject postObject = new JsonObject();
						Session session = client.getSession();

						postObject.addProperty("accessToken", session.getAccessToken());
						postObject.addProperty("selectedProfile", session.getUuidOrNull().toString().replace("-", ""));
						postObject.addProperty("serverId", "cbd2c3f65d7ba5cceba0cc9647ff9a85c371f4");
						HttpUtils.postJson("https://sessionserver.mojang.com/session/minecraft/join", postObject);
						
						Map<String, String> headers = new HashMap<>();
						
						headers.put("name", gameProfile.getName());
						headers.put("uuid", gameProfile.getId().toString().replace("-", ""));
						
						try {
							webSocket = new SoarWebSocketClient(headers);
							webSocket.connect();
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}.start();
	}
	
	public void send(SoarPacket packet) {
		if(webSocket != null) {
			webSocket.send(packet.toJson().toString());
		}
	}
}
