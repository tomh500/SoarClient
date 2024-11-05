package com.soarclient.management.account;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.soarclient.utils.JsonUtils;
import com.soarclient.utils.file.FileLocation;
import com.soarclient.utils.file.FileUtils;

public class AccountManager {

	private List<Account> accounts = new ArrayList<>();
	private Account currentAccount;
	private File file;
	
	public AccountManager() {
		file = new File(FileLocation.SOAR_DIR, "account.json");
		FileUtils.createFile(file);
	}
	
	public void load() {
		
		Gson gson = new Gson();
		
		try (FileReader reader = new FileReader(file)) {
			
			JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);
			JsonArray jsonArray = JsonUtils.getArrayProperty(jsonObject, "accounts");
			
			if(jsonArray != null) {
				
				Iterator<JsonElement> iterator = jsonArray.iterator();
				
				while(iterator.hasNext()) {
					
					JsonElement jsonElement = (JsonElement) iterator.next();
					JsonObject accJsonObject = gson.fromJson(jsonElement, JsonObject.class);
					String name = JsonUtils.getStringProperty(accJsonObject, "name", "null");
					String uuid = JsonUtils.getStringProperty(accJsonObject, "uuid", "null");
					String token = JsonUtils.getStringProperty(accJsonObject, "token", "null");
					int typeId = JsonUtils.getIntProperty(accJsonObject, "type", -1);
					
					if(!name.equals("null") && !uuid.equals("null") && !token.equals("null") && typeId != -1) {
						
						AccountType type = AccountType.get(typeId);
						
						if(type != null) {
							accounts.add(new Account(name, uuid, token, type));
						}
					}
				}
			}
		} catch (Exception e) {
		}
	}
	
	public void save() {
		
		JsonObject jsonObject = new JsonObject();
		JsonArray jsonArray = new JsonArray();
		
		jsonObject.addProperty("current_account", currentAccount.getUuid());
		
		for(Account acc : accounts) {
			
			JsonObject innerJsonObject = new JsonObject();
			
			innerJsonObject.addProperty("name", acc.getName());
			innerJsonObject.addProperty("uuid", acc.getUuid());
			innerJsonObject.addProperty("token", acc.getRefreshToken());
			innerJsonObject.addProperty("type", acc.getType().getId());
			
			jsonArray.add(jsonArray);
		}
		
		jsonObject.add("accounts", jsonArray);
	}
	
	public Account getByUuid(String uuid) {
		
		for(Account acc : accounts) {
			if(acc.getUuid().equals(uuid)) {
				return acc;
			}
		}
		
		return null;
	}

	public Account getCurrentAccount() {
		return currentAccount;
	}

	public void setCurrentAccount(Account currentAccount) {
		this.currentAccount = currentAccount;
	}

	public List<Account> getAccounts() {
		return accounts;
	}
}
