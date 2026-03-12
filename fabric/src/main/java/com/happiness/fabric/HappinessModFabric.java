package com.happiness.fabric;

import com.happiness.HappinessMod;
import com.happiness.HappinessManager;
import com.happiness.fabric.worldgen.HappinessWorldGen;
import com.happiness.network.HappinessSyncPacket;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public class HappinessModFabric implements ModInitializer {
    
    // Sync happiness to client every N ticks
    private static final int SYNC_INTERVAL = 10; // Every 0.5 seconds
    private int syncCounter = 0;
    
    @Override
    public void onInitialize() {
        HappinessMod.init();
        HappinessWorldGen.registerWorldGen();
        
        // Register packets
        PayloadTypeRegistry.playS2C().register(HappinessSyncPacket.ID, HappinessSyncPacket.CODEC);
        
        // Server tick - update happiness for all players
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            syncCounter++;
            
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                // Update happiness based on light level
                HappinessManager.getInstance().onServerTick(player);
                
                // Sync to client periodically
                if (syncCounter >= SYNC_INTERVAL) {
                    float happiness = HappinessManager.getInstance().getHappiness(player);
                    ServerPlayNetworking.send(player, new HappinessSyncPacket(happiness));
                }
            }
            
            if (syncCounter >= SYNC_INTERVAL) {
                syncCounter = 0;
            }
        });
        
        HappinessMod.LOGGER.info("Happiness Mod initialized with sunlight/darkness mechanics!");
    }
}
