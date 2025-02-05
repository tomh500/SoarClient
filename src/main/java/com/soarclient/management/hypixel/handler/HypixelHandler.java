package com.soarclient.management.hypixel.handler;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.soarclient.Soar;
import com.soarclient.event.EventBus;
import com.soarclient.event.client.ClientTickEvent;
import com.soarclient.management.hypixel.HypixelManager;
import com.soarclient.utils.TimerUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;

public class HypixelHandler {
	
    private static final long REQUEST_DELAY_MS = 1500;
    
    private final MinecraftClient client = MinecraftClient.getInstance();;
    private final TimerUtils timer;
    private final Queue<String> requests;
    private String currentRequest;

    public HypixelHandler() {
        this.timer = new TimerUtils();
        this.requests = new LinkedBlockingQueue<>();
    }

    public final EventBus.EventListener<ClientTickEvent> onClientTick = event -> {
    	if(client.getCurrentServerEntry() != null && client.getCurrentServerEntry().address.contains("hypixel")) {
            processEntities();
            cleanupProcessedRequests();
            processNextRequest();
    	}
    };

    private void processEntities() {
    	
        if (client.world == null) {
        	return;
        }

        for (Entity entity : client.world.getEntities()) {
        	
            if (!(entity instanceof ClientPlayerEntity)) {
            	continue;
            }

            ClientPlayerEntity player = (ClientPlayerEntity) entity;
            
            if (!isValidPlayer(player)) {
            	continue;
            }

            String uuid = getFormattedUuid(player);
            
            if (shouldAddToRequests(uuid)) {
                requests.add(uuid);
            }
        }
    }

    private boolean isValidPlayer(ClientPlayerEntity player) {
        return player.getGameProfile() != null && !player.isInvisible();
    }

    private String getFormattedUuid(ClientPlayerEntity player) {
        return player.getGameProfile().getId().toString().replace("-", "");
    }

    private boolean shouldAddToRequests(String uuid) {
        HypixelManager hypixelManager = Soar.getInstance().getHypixelManager();
        return hypixelManager.getByUuid(uuid) == null && !requests.contains(uuid);
    }

    private void cleanupProcessedRequests() {
        HypixelManager hypixelManager = Soar.getInstance().getHypixelManager();
        requests.removeIf(uuid -> hypixelManager.getByUuid(uuid) != null);
    }

    private void processNextRequest() {
    	
        if (!timer.delay(REQUEST_DELAY_MS)) {
            return;
        }

        currentRequest = requests.poll();
        if (currentRequest != null && !currentRequest.isEmpty()) {
            // Soar.getInstance().getWebSocket().sendPacket(new SC_HypixelStatsPacket(currentRequest));
            timer.reset();
        }
    }
}
