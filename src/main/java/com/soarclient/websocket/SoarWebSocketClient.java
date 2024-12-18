package com.soarclient.websocket;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.soarclient.websocket.handler.WebSocketHandler;
import com.soarclient.websocket.packet.SoarPacket;

public class SoarWebSocketClient extends WebSocketClient {

	private Map<String, WebSocketHandler> handlers = new HashMap<>();

	/*
	 * TODO： 接続するときにname, uuidをヘッダーに入れて送信 アカウントを切り替えるときは一回接続を切らないといけない
	 */
	public SoarWebSocketClient(String url, Map<String, String> headers) throws Exception {
		super(new URI(url));
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {

	}

	@Override
	public void onMessage(String message) {

		JsonObject jsonObject = new Gson().fromJson(message, JsonObject.class);
		String type = jsonObject.get("type").getAsString();

		WebSocketHandler handler = handlers.get(type);

		if (type != null) {
			handler.handle(jsonObject);
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("closed");
	}

	@Override
	public void onError(Exception ex) {
	}

	public void sendPacket(SoarPacket packet) {
		send(packet.toJson().toString());
	}
}
