package com.happiness;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages player happiness levels.
 * 
 * Core mechanics:
 * - Happiness ranges from 0 (depressed) to 100 (euphoric)
 * - Being in darkness (low sky light) drains happiness
 * - Being in sunlight restores happiness
 * - Happiness pills give instant boost
 * 
 * Effects at low happiness:
 * - Slowness
 * - Mining fatigue
 * - Screen darkening (client-side)
 */
public class HappinessManager {
    
    // Singleton instance
    private static final HappinessManager INSTANCE = new HappinessManager();
    
    // Player UUID -> Happiness level (0-100)
    private final Map<UUID, Float> playerHappiness = new HashMap<>();
    
    // Configuration
    public static final float MAX_HAPPINESS = 100f;
    public static final float MIN_HAPPINESS = 0f;
    public static final float DEFAULT_HAPPINESS = 75f;
    
    // Rates per second (slowed down for better gameplay)
    public static final float DARKNESS_DRAIN_RATE = 0.15f;  // Lost per second in complete darkness (~11 min to drain)
    public static final float SUNLIGHT_RESTORE_RATE = 0.25f; // Gained per second in full sunlight (~6.5 min to restore)
    public static final float PILL_BOOST = 25f;             // Instant boost from pill
    
    // Light thresholds
    public static final int DARKNESS_THRESHOLD = 4;  // Below this = darkness
    public static final int SUNLIGHT_THRESHOLD = 12; // Above this = sunlight
    
    // Tick tracking (20 ticks = 1 second)
    private int tickCounter = 0;
    private static final int TICKS_PER_UPDATE = 20; // Update every second
    
    private HappinessManager() {}
    
    public static HappinessManager getInstance() {
        return INSTANCE;
    }
    
    /**
     * Get a player's current happiness level
     */
    public float getHappiness(PlayerEntity player) {
        return playerHappiness.getOrDefault(player.getUuid(), DEFAULT_HAPPINESS);
    }
    
    /**
     * Set a player's happiness level (clamped to valid range)
     */
    public void setHappiness(PlayerEntity player, float happiness) {
        float clamped = Math.max(MIN_HAPPINESS, Math.min(MAX_HAPPINESS, happiness));
        playerHappiness.put(player.getUuid(), clamped);
    }
    
    /**
     * Add happiness to a player (can be negative)
     */
    public void addHappiness(PlayerEntity player, float amount) {
        float current = getHappiness(player);
        setHappiness(player, current + amount);
    }
    
    /**
     * Called every tick from the server
     */
    public void onServerTick(ServerPlayerEntity player) {
        // Skip happiness system entirely in Creative or Spectator mode
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        
        tickCounter++;
        
        if (tickCounter >= TICKS_PER_UPDATE) {
            tickCounter = 0;
            updatePlayerHappiness(player);
        }
    }
    
    /**
     * Update happiness based on light level
     */
    private void updatePlayerHappiness(ServerPlayerEntity player) {
        World world = player.getWorld();
        BlockPos pos = player.getBlockPos();
        
        // Get sky light level (affected by time of day and blocks above)
        int skyLight = world.getLightLevel(LightType.SKY, pos);
        
        // Check if it's actually daytime (sun is up)
        boolean isDaytime = world.isDay();
        
        // Effective light considers both sky light and time of day
        int effectiveLight = isDaytime ? skyLight : Math.min(skyLight, 4);
        
        float currentHappiness = getHappiness(player);
        float newHappiness = currentHappiness;
        
        if (effectiveLight <= DARKNESS_THRESHOLD) {
            // In darkness - drain happiness
            // Scale drain based on how dark it is
            float drainMultiplier = 1.0f - (effectiveLight / (float) DARKNESS_THRESHOLD);
            newHappiness -= DARKNESS_DRAIN_RATE * drainMultiplier;
            
            HappinessMod.LOGGER.debug("Player {} in darkness (light={}), draining happiness: {} -> {}", 
                player.getName().getString(), effectiveLight, currentHappiness, newHappiness);
                
        } else if (effectiveLight >= SUNLIGHT_THRESHOLD && isDaytime) {
            // In sunlight - restore happiness
            float restoreMultiplier = (effectiveLight - SUNLIGHT_THRESHOLD) / (float) (15 - SUNLIGHT_THRESHOLD);
            newHappiness += SUNLIGHT_RESTORE_RATE * restoreMultiplier;
            
            HappinessMod.LOGGER.debug("Player {} in sunlight (light={}), restoring happiness: {} -> {}", 
                player.getName().getString(), effectiveLight, currentHappiness, newHappiness);
        }
        // Between thresholds = neutral, no change
        
        setHappiness(player, newHappiness);
        
        // Apply effects based on happiness level
        applyHappinessEffects(player, newHappiness);
    }
    
    /**
     * Apply status effects based on happiness level
     */
    private void applyHappinessEffects(ServerPlayerEntity player, float happiness) {
        // Effects are applied via HappinessEffects class
        HappinessEffects.applyEffects(player, happiness);
    }
    
    /**
     * Called when player consumes a happiness pill
     */
    public void onPillConsumed(PlayerEntity player) {
        addHappiness(player, PILL_BOOST);
        HappinessMod.LOGGER.info("Player {} consumed happiness pill, happiness now: {}", 
            player.getName().getString(), getHappiness(player));
    }
    
    /**
     * Get happiness as a percentage (0.0 - 1.0)
     */
    public float getHappinessPercent(PlayerEntity player) {
        return getHappiness(player) / MAX_HAPPINESS;
    }
    
    /**
     * Get the mood description based on happiness level
     */
    public String getMoodDescription(float happiness) {
        if (happiness >= 90) return "Euphoric";
        if (happiness >= 70) return "Happy";
        if (happiness >= 50) return "Content";
        if (happiness >= 30) return "Melancholy";
        if (happiness >= 15) return "Sad";
        return "Depressed";
    }
    
    /**
     * Clean up player data when they disconnect
     */
    public void onPlayerDisconnect(UUID playerId) {
        // Keep data for now - could persist to NBT later
    }
}
