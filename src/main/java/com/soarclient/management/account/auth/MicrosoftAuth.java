package com.soarclient.management.account.auth;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.soarclient.Soar;
import com.soarclient.management.account.Account;
import com.soarclient.management.account.AccountManager;
import com.soarclient.management.account.AccountType;
import com.soarclient.utils.Multithreading;
import com.soarclient.utils.file.FileLocation;
import com.soarclient.utils.network.HttpUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class MicrosoftAuth {

	public void loginWithPopupWindow() {

		Multithreading.runAsync(() -> {

			try {

				AuthServer.start();
				File webviewDir = new File(FileLocation.EXTERNAL_DIR, "webview");
				File executeFile = new File(webviewDir, "webview.exe");

				Process p = new ProcessBuilder(executeFile.getAbsolutePath()).start();
				p.waitFor();
				AuthServer.close();
			} catch (Exception e) {
			}
		});
	}

	public void loginWithAccount(Account account) {

		String refreshToken = account.getRefreshToken();
		JsonObject response = HttpUtils.readJson(
				"https://login.live.com/oauth20_token.srf?client_id=00000000402b5328&grant_type=refresh_token&refresh_token="
						+ refreshToken,
				null);

		if (response.get("access_token") == null) {
			Soar.getInstance().getAccountManager().getAccounts().remove(account);
			return;
		}

		getXboxLiveToken(response.get("access_token").getAsString(), refreshToken);
	}

	public void loginWithUrl(String url) {
		AuthServer.close();
		try {
			getMicrosoftToken(new URI(url).toURL());
		} catch (MalformedURLException | URISyntaxException e) {
		}
	}

	private void getMicrosoftToken(URL tokenURL) {

		JsonObject response = HttpUtils.readJson(
				"https://login.live.com/oauth20_token.srf?client_id=00000000402b5328&grant_type=authorization_code&redirect_uri=https://login.live.com/oauth20_desktop.srf&code="
						+ tokenURL.toString().split("=")[1],
				null);

		getXboxLiveToken(response.get("access_token").getAsString(), response.get("refresh_token").getAsString());
	}

	private void getXboxLiveToken(String token, String refreshToken) {

		JsonObject properties = new JsonObject();
		properties.addProperty("AuthMethod", "RPS");
		properties.addProperty("SiteName", "user.auth.xboxlive.com");
		properties.addProperty("RpsTicket", "d=" + token);

		JsonObject request = new JsonObject();
		request.add("Properties", properties);
		request.addProperty("RelyingParty", "http://auth.xboxlive.com");
		request.addProperty("TokenType", "JWT");

		JsonObject response = HttpUtils.postJson("https://user.auth.xboxlive.com/user/authenticate", request);

		getXSTS(response.get("Token").getAsString(), refreshToken);
	}

	private void getXSTS(String token, String refreshToken) {

		JsonPrimitive jsonToken = new JsonPrimitive(token);
		JsonArray userTokens = new JsonArray();
		userTokens.add(jsonToken);

		JsonObject properties = new JsonObject();
		properties.addProperty("SandboxId", "RETAIL");
		properties.add("UserTokens", userTokens);

		JsonObject request = new JsonObject();
		request.add("Properties", properties);
		request.addProperty("RelyingParty", "rp://api.minecraftservices.com/");
		request.addProperty("TokenType", "JWT");

		JsonObject response = HttpUtils.postJson("https://xsts.auth.xboxlive.com/xsts/authorize", request);

		if (response.has("XErr")) {
			switch (response.get("XErr").getAsString()) {
			case "2148916233":
				// This account doesn't have an Xbox account.
				break;
			case "2148916235":
				// Xbox isn't available in your country.
				break;
			}
		} else {
			getMinecraftToken(response.getAsJsonObject("DisplayClaims").get("xui").getAsJsonArray().get(0)
					.getAsJsonObject().get("uhs").getAsString(), response.get("Token").getAsString(), refreshToken);
		}
	}

	private void getMinecraftToken(String uhs, String token, String refreshToken) {

		JsonObject request = new JsonObject();
		request.addProperty("identityToken", String.format("XBL3.0 x=%s;%s", uhs, token));

		JsonObject response = HttpUtils.postJson("https://api.minecraftservices.com/authentication/login_with_xbox",
				request);

		checkMinecraftOwnership(response.get("access_token").getAsString(), refreshToken);
	}

	private void checkMinecraftOwnership(String token, String refreshToken) {

		Map<String, String> headers = new HashMap<>();
		boolean ownsMinecraft = false;

		headers.put("Authorization", "Bearer " + token);

		JsonObject request = HttpUtils.readJson("https://api.minecraftservices.com/entitlements/mcstore", headers);

		for (int i = 0; i < request.get("items").getAsJsonArray().size(); i++) {
			String itemName = request.get("items").getAsJsonArray().get(i).getAsJsonObject().get("name").getAsString();
			if (itemName.equals("product_minecraft") || itemName.equals("game_minecraft")) {
				ownsMinecraft = true;
				break;
			}
		}

		if (ownsMinecraft) {
			getMinecraftProfile(token, refreshToken);
		}
	}

	private void getMinecraftProfile(String token, String refreshToken) {

		AccountManager accountManager = Soar.getInstance().getAccountManager();

		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + token);

		JsonObject request = HttpUtils.readJson("https://api.minecraftservices.com/minecraft/profile", headers);

		String name = request.get("name").getAsString();
		String uuid = request.get("id").getAsString();

		Account account = new Account(name, uuid, refreshToken, AccountType.PREMIUM);

		Minecraft.getMinecraft().setSession(new Session(name, uuid, token, "mojang"));

		if (accountManager.getByUuid(account.getUuid()) == null) {
			accountManager.getAccounts().add(account);
		}

		accountManager.setCurrentAccount(account);
	}
}