package com.soarclient.management.account.impl;

import java.util.UUID;

import com.google.gson.JsonObject;
import com.soarclient.management.account.Account;
import com.soarclient.management.account.AccountType;

import net.raphimc.minecraftauth.MinecraftAuth;
import net.raphimc.minecraftauth.step.AbstractStep;
import net.raphimc.minecraftauth.step.bedrock.StepMCChain;
import net.raphimc.minecraftauth.step.bedrock.StepPlayFabToken;
import net.raphimc.minecraftauth.step.bedrock.session.StepFullBedrockSession;
import net.raphimc.minecraftauth.step.xbl.StepXblXstsToken;
import net.raphimc.minecraftauth.util.MicrosoftConstants;

public class BedrockAccount extends Account {

	public static final AbstractStep<?, StepFullBedrockSession.FullBedrockSession> DEVICE_CODE_LOGIN = MinecraftAuth
			.builder().withClientId(MicrosoftConstants.BEDROCK_ANDROID_TITLE_ID)
			.withScope(MicrosoftConstants.SCOPE_TITLE_AUTH).deviceCode().withDeviceToken("Android")
			.sisuTitleAuthentication(MicrosoftConstants.BEDROCK_XSTS_RELYING_PARTY)
			.buildMinecraftBedrockChainStep(true, true);

	private StepFullBedrockSession.FullBedrockSession bedrockSession;

	public BedrockAccount(final JsonObject jsonObject) {
		super(AccountType.BEDROCK);
		this.bedrockSession = DEVICE_CODE_LOGIN.fromJson(jsonObject.getAsJsonObject("bedrockSession"));
	}

	public BedrockAccount(final StepFullBedrockSession.FullBedrockSession bedrockSession) {
		super(AccountType.BEDROCK);
		this.bedrockSession = bedrockSession;
	}

	@Override
	public JsonObject toJson() {
		final JsonObject jsonObject = new JsonObject();
		jsonObject.add("bedrockSession", DEVICE_CODE_LOGIN.toJson(this.bedrockSession));
		return jsonObject;
	}

	@Override
	public String getName() {
		return this.bedrockSession.getMcChain().getDisplayName();
	}

	@Override
	public UUID getUUID() {
		return this.bedrockSession.getMcChain().getId();
	}

	public StepMCChain.MCChain getMcChain() {
		return this.bedrockSession.getMcChain();
	}

	public StepPlayFabToken.PlayFabToken getPlayFabToken() {
		return this.bedrockSession.getPlayFabToken();
	}

	public StepXblXstsToken.XblXsts<?> getRealmsXsts() {
		return this.bedrockSession.getRealmsXsts();
	}

	@Override
	public String getDisplayString() {
		return this.getName() + " (Bedrock)";
	}

	@Override
	public boolean refresh() throws Exception {
		if (!super.refresh())
			return false;

		this.bedrockSession = DEVICE_CODE_LOGIN.refresh(MinecraftAuth.createHttpClient(), this.bedrockSession);
		return true;
	}
}