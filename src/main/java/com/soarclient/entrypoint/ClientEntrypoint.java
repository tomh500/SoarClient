package com.soarclient.entrypoint;

import com.soarclient.Soar;

import net.fabricmc.api.ClientModInitializer;

public class ClientEntrypoint implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
    	Soar.getInstance().start();
    }
}
