package com.soarclient.management.websocket.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.soarclient.logger.SoarLogger;
import com.soarclient.management.websocket.handler.WebSocketHandler;

public class SoarWebSocketClient extends WebSocketClient {

	private final Map<String, WebSocketHandler> handlers = new HashMap<>();
	private final Gson gson = new Gson();

	public SoarWebSocketClient(Map<String, String> headers) throws URISyntaxException {
		super(new URI("ws://localhost:8080/websocket"), headers);
		initializeHandlers();
	}

    private void initializeHandlers() {
    }
    
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		SoarLogger.info("WebSocket connection opened");
	}

	@Override
	public void onMessage(String message) {

		System.out.println(message);
		/*JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
		String type = jsonObject.get("type").getAsString();

		WebSocketHandler handler = handlers.get(type);

		if (handler != null) {
			handler.handle(jsonObject);
		} else {
			SoarLogger.warn("No handler found for message type: " + type);
		}*/
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		SoarLogger.info("WebSocket connection closed: " + reason);
	}

	@Override
	public void onError(Exception ex) {
		SoarLogger.error("WebSocket error occurred", ex);
	}

	private void registerHandler(String type, WebSocketHandler handler) {
		handlers.put(type, handler);
	}
}