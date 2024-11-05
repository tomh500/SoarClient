package com.soarclient.management.account.auth;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import com.soarclient.utils.Multithreading;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class AuthServer {

	private static HttpServer server;
	private static boolean running;

	public static void start() {
		if (server == null) {
			try {
				server = HttpServer.create(new InetSocketAddress(9000), 0);
				server.createContext("/login", new HttpHandler() {
					@Override
					public void handle(HttpExchange exchange) throws IOException {

						Map<String, List<String>> headers = exchange.getRequestHeaders();
						String url = getHeaderValue(headers, "url");

						if (url != null) {
							Multithreading.runAsync(() -> {
								new MicrosoftAuth().loginWithUrl(url);
							});
						}

						String response = "Success";
						exchange.sendResponseHeaders(200, response.getBytes().length);
						OutputStream os = exchange.getResponseBody();
						os.write(response.getBytes());
						os.close();
					}
				});

				running = true;
				server.start();
			} catch (IOException e) {
				running = false;
			}
		}
	}

	public static void close() {
		if (server != null) {
			running = false;
			server.stop(0);
		}
		server = null;
	}

	public static boolean isRunning() {
		return running;
	}

	private static String getHeaderValue(Map<String, List<String>> headers, String headerName) {

		List<String> values = headers.get(headerName);

		if (values != null && !values.isEmpty()) {
			return values.get(0);
		}

		return null;
	}
}