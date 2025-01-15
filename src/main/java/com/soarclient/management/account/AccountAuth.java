package com.soarclient.management.account;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import com.soarclient.Soar;
import com.soarclient.libraries.skin.SkinHelper;
import com.soarclient.management.account.impl.BedrockAccount;
import com.soarclient.management.account.impl.MicrosoftAccount;
import com.soarclient.management.config.ConfigType;
import com.soarclient.utils.TFunction;
import com.soarclient.utils.file.FileLocation;

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

	public static void handleLogin(Account account) {
		try {
			if (account instanceof MicrosoftAccount) {
				MicrosoftAccount msAccount = (MicrosoftAccount) account;
				MCProfile profile = msAccount.getMcProfile();

				msAccount.refresh();
				Minecraft.getMinecraft().setSession(new Session(profile.getName(), profile.getId().toString(),
						profile.getMcToken().getAccessToken(), "legacy"));
			}
			
			if (account instanceof BedrockAccount) {
				
				BedrockAccount beAccount = (BedrockAccount) account;
				
				beAccount.refresh();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(Soar.getInstance().getConfigManager() != null) {
			Soar.getInstance().getConfigManager().save(ConfigType.ACCOUNT);
		}
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
				SkinHelper.downloadJavaSkin(msAccount.getUUID().toString().replace("-", ""),
						new File(FileLocation.CACHE_DIR, msAccount.getUUID().toString().replace("-", "")));
				Minecraft.getMinecraft().setSession(new Session(profile.getName(), profile.getId().toString(),
						profile.getMcToken().getAccessToken(), "legacy"));
				accountManager.setCurrentAccount(msAccount.getUUID().toString().replace("-", ""));
				Soar.getInstance().getConfigManager().save(ConfigType.ACCOUNT);
			}

			if (account instanceof BedrockAccount) {

				BedrockAccount beAccount = (BedrockAccount) account;

				accountManager.getAccounts().add(beAccount);
				SkinHelper.downloadBedrockSkin(beAccount.getMcChain().getXuid(),
						new File(FileLocation.CACHE_DIR, beAccount.getMcChain().getXuid()));
				Soar.getInstance().getConfigManager().save(ConfigType.ACCOUNT);
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
