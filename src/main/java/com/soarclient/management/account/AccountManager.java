package com.soarclient.management.account;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.soarclient.management.account.impl.BedrockAccount;
import com.soarclient.management.account.impl.MicrosoftAccount;
import com.soarclient.management.account.impl.OfflineAccount;

public class AccountManager {

	private List<Account> accounts = new ArrayList<>();
	private String currentAccount;
	
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
		
		if(inputUuid == null) {
			return null;
		}
		
		for(Account acc : accounts) {
			
			String pUuid = acc.getUUID().toString().replace("-", "");
			String uuid = inputUuid.replace("-", "");
			
			if(pUuid.equals(uuid)) {
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
