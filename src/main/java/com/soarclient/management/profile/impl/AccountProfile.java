package com.soarclient.management.profile.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.soarclient.Soar;
import com.soarclient.management.account.Account;
import com.soarclient.management.account.AccountManager;
import com.soarclient.management.account.AccountType;
import com.soarclient.management.account.impl.BedrockAccount;
import com.soarclient.management.account.impl.MicrosoftAccount;
import com.soarclient.management.account.impl.OfflineAccount;
import com.soarclient.management.profile.Profile;

public class AccountProfile extends Profile {

	public AccountProfile() {
		super("account");
	}

	@Override
	public void load(JsonElement jsonElement) throws Throwable {
		
		AccountManager accountManager = Soar.getInstance().getAccountManager();
		
		accountManager.getAccounts().clear();
		
		for(JsonElement element : jsonElement.getAsJsonArray()) {
			
			final JsonObject jsonObject = element.getAsJsonObject();
			final AccountType type = AccountType.getById(jsonObject.get("type").getAsString());
			
			if(type.equals(AccountType.BEDROCK)) {
				accountManager.getAccounts().add(new BedrockAccount(jsonObject));
			} else if(type.equals(AccountType.MICROSOFT)) {
				accountManager.getAccounts().add(new MicrosoftAccount(jsonObject));
			} else if(type.equals(AccountType.OFFLINE)) {
				accountManager.getAccounts().add(new OfflineAccount(jsonObject));
			}
		}
	}

	@Override
	public JsonElement save() throws Throwable {
		
		final JsonArray array = new JsonArray();
		
		for(Account acc : Soar.getInstance().getAccountManager().getAccounts()) {
			final JsonObject jsonObject = acc.toJson();
			jsonObject.addProperty("type", acc.getType().getId());
			array.add(jsonObject);
		}
		
		return array;
	}

}
