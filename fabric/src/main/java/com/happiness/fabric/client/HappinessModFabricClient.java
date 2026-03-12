package com.happiness.fabric.client;

import com.happiness.HappinessMod;
import com.happiness.client.ClientHappinessData;
import com.happiness.client.HappinessHudRenderer;
import com.happiness.network.HappinessSyncPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class HappinessModFabricClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        // Register packet receiver
        ClientPlayNetworking.registerGlobalReceiver(HappinessSyncPacket.ID, (packet, context) -> {
            context.client().execute(() -> {
                ClientHappinessData.setHappiness(packet.happiness());
            });
        });
        
        // Register HUD renderer
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            HappinessHudRenderer.render(context, tickDelta.getTickDelta(true));
        });
        
        // Client tick for smooth interpolation
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientHappinessData.tick();
        });
        
        HappinessMod.LOGGER.info("Happiness Mod client initialized!");
    }
}
