package com.soarclient.management.account;

import java.awt.Desktop;
import java.net.URI;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import com.soarclient.Soar;
import com.soarclient.management.account.impl.BedrockAccount;
import com.soarclient.management.account.impl.MicrosoftAccount;
import com.soarclient.utils.TFunction;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.raphimc.minecraftauth.MinecraftAuth;
import net.raphimc.minecraftauth.step.java.StepMCProfile.MCProfile;
import net.raphimc.minecraftauth.step.msa.StepMsaDeviceCode;

public class AccountAuth {

	public static void handleMicrosoftLogin() {
		handleLogin(msaDeviceCodeConsumer -> {
			return new MicrosoftAccount(
					MicrosoftAccount.DEVICE_CODE_LOGIN.getFromInput(MinecraftAuth.createHttpClient(),
							new StepMsaDeviceCode.MsaDeviceCodeCallback(msaDeviceCodeConsumer)));
		});
	}

	public static void handleBedrockLogin() {
		handleLogin(msaDeviceCodeConsumer -> {
			return new BedrockAccount(BedrockAccount.DEVICE_CODE_LOGIN.getFromInput(MinecraftAuth.createHttpClient(),
					new StepMsaDeviceCode.MsaDeviceCodeCallback(msaDeviceCodeConsumer)));
		});
	}

	private static void handleLogin(
			final TFunction<Consumer<StepMsaDeviceCode.MsaDeviceCode>, Account> requestHandler) {

		AccountManager accountManager = Soar.getInstance().getAccountManager();
		
		try {
			final Account account = requestHandler
					.apply(msaDeviceCode -> SwingUtilities.invokeLater(() -> login(msaDeviceCode)));

			if (account instanceof MicrosoftAccount) {

				MicrosoftAccount msAccount = (MicrosoftAccount) account;
				MCProfile profile = msAccount.getMcProfile();

				accountManager.getAccounts().add(msAccount);
				Minecraft.getMinecraft().setSession(new Session(profile.getName(), profile.getId().toString(),
						profile.getMcToken().getAccessToken(), "legacy"));
				accountManager.save();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private static boolean login(StepMsaDeviceCode.MsaDeviceCode deviceCode) {
		return openUrlWithDesktop(deviceCode.getDirectVerificationUri());
	}

	private static boolean openUrlWithDesktop(String url) {
		try {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().browse(new URI(url));
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
