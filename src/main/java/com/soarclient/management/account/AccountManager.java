package com.soarclient.management.account;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.soarclient.Soar;
import com.soarclient.management.account.impl.BedrockAccount;
import com.soarclient.management.account.impl.MicrosoftAccount;
import com.soarclient.management.account.impl.OfflineAccount;
import com.soarclient.utils.JsonUtils;
import com.soarclient.utils.file.FileLocation;
import com.soarclient.utils.file.FileUtils;

public class AccountManager {

	private List<Account> accounts = new ArrayList<>();
	private String currentAccount;
	private File file;

	public AccountManager() {
		file = new File(FileLocation.SOAR_DIR, "Account.json");
		FileUtils.createFile(file);
		load();
	}

	public void save() {

		try (FileWriter writer = new FileWriter(file)) {

			Gson gson = new Gson();
			JsonObject jsonObject = new JsonObject();
			JsonArray jsonArray = new JsonArray();

			jsonObject.addProperty("currentAccount", currentAccount);

			for (Account acc : Soar.getInstance().getAccountManager().getAccounts()) {
				final JsonObject innerJsonObject = acc.toJson();
				innerJsonObject.addProperty("type", acc.getType().getId());
				jsonArray.add(innerJsonObject);
			}

			jsonObject.add("accounts", jsonArray);
			gson.toJson(jsonObject, writer);
		} catch (Exception e) {
		}
	}

	public void load() {

		accounts.clear();

		try (FileReader reader = new FileReader(file)) {

			Gson gson = new Gson();
			JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

			if (jsonObject != null && jsonObject.isJsonObject()) {

				JsonArray jsonArray = JsonUtils.getArrayProperty(jsonObject, "accounts");

				currentAccount = JsonUtils.getStringProperty(jsonObject, "currentAccount", "null");

				if (jsonArray != null) {

					Iterator<JsonElement> iterator = jsonArray.iterator();

					while (iterator.hasNext()) {

						JsonElement jsonElement = (JsonElement) iterator.next();
						JsonObject accJsonObject = gson.fromJson(jsonElement, JsonObject.class);

						final AccountType type = AccountType.getById(accJsonObject.get("type").getAsString());

						if (type.equals(AccountType.BEDROCK)) {
							accounts.add(new BedrockAccount(accJsonObject));
						} else if (type.equals(AccountType.MICROSOFT)) {
							accounts.add(new MicrosoftAccount(accJsonObject));
						} else if (type.equals(AccountType.OFFLINE)) {
							accounts.add(new OfflineAccount(accJsonObject));
						}
					}
				}
			}

			Account acc = getByUuid(currentAccount);

			if (acc != null) {
				AccountAuth.handleLogin(acc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public List<OfflineAccount> getOfflineAccounts() {
		return accounts.stream().filter(a -> a instanceof OfflineAccount).map(a -> (OfflineAccount) a)
				.collect(Collectors.toList());
	}

	public List<MicrosoftAccount> getMicrosoftAccounts() {
		return accounts.stream().filter(a -> a instanceof MicrosoftAccount).map(a -> (MicrosoftAccount) a)
				.collect(Collectors.toList());
	}

	public List<BedrockAccount> getBedrockAccounts() {
		return accounts.stream().filter(a -> a instanceof BedrockAccount).map(a -> (BedrockAccount) a)
				.collect(Collectors.toList());
	}

	public Account getByUuid(String inputUuid) {

		for (Account acc : accounts) {

			String pUuid = acc.getUUID().toString().replace("-", "");
			String uuid = inputUuid.replace("-", "");

			if (pUuid.equals(uuid)) {
				return acc;
			}
		}

		return null;
	}

	public Account getCurrentAccount() {
		return getByUuid(currentAccount);
	}

	public void setCurrentAccount(String currentAccount) {
		this.currentAccount = currentAccount;
	}
}
