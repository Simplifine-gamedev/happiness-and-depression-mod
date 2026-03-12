package com.happiness;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Applies status effects based on happiness level.
 * 
 * Low happiness = negative effects
 * High happiness = positive effects
 */
public class HappinessEffects {
    
    // Effect durations (in ticks) - slightly longer than update interval to prevent flickering
    private static final int EFFECT_DURATION = 25; // 1.25 seconds
    
    public static void applyEffects(ServerPlayerEntity player, float happiness) {
        // No effects in Creative or Spectator mode
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        
        // Remove conflicting effects first
        clearConflictingEffects(player);
        
        if (happiness <= 15) {
            // Depressed - severe penalties
            applyDepressedEffects(player);
        } else if (happiness <= 30) {
            // Sad - moderate penalties  
            applySadEffects(player);
        } else if (happiness <= 50) {
            // Melancholy - minor penalties
            applyMelancholyEffects(player);
        } else if (happiness >= 90) {
            // Euphoric - bonuses!
            applyEuphoricEffects(player);
        }
        // 50-90 = no effects (neutral)
    }
    
    private static void clearConflictingEffects(ServerPlayerEntity player) {
        // Only clear effects we manage (short duration ones)
        // This prevents interfering with potion effects
    }
    
    private static void applyDepressedEffects(ServerPlayerEntity player) {
        // Slowness II - can barely move
        player.addStatusEffect(new StatusEffectInstance(
            StatusEffects.SLOWNESS, EFFECT_DURATION, 1, false, false, true
        ));
        
        // Mining Fatigue II - everything feels pointless
        player.addStatusEffect(new StatusEffectInstance(
            StatusEffects.MINING_FATIGUE, EFFECT_DURATION, 1, false, false, true
        ));
        
        // Weakness I - no strength to fight
        player.addStatusEffect(new StatusEffectInstance(
            StatusEffects.WEAKNESS, EFFECT_DURATION, 0, false, false, true
        ));
    }
    
    private static void applySadEffects(ServerPlayerEntity player) {
        // Slowness I
        player.addStatusEffect(new StatusEffectInstance(
            StatusEffects.SLOWNESS, EFFECT_DURATION, 0, false, false, true
        ));
        
        // Mining Fatigue I
        player.addStatusEffect(new StatusEffectInstance(
            StatusEffects.MINING_FATIGUE, EFFECT_DURATION, 0, false, false, true
        ));
    }
    
    private static void applyMelancholyEffects(ServerPlayerEntity player) {
        // Just mining fatigue I - feeling unmotivated
        player.addStatusEffect(new StatusEffectInstance(
            StatusEffects.MINING_FATIGUE, EFFECT_DURATION, 0, false, false, true
        ));
    }
    
    private static void applyEuphoricEffects(ServerPlayerEntity player) {
        // Speed I - energized!
        player.addStatusEffect(new StatusEffectInstance(
            StatusEffects.SPEED, EFFECT_DURATION, 0, false, false, true
        ));
        
        // Haste I - motivated to work
        player.addStatusEffect(new StatusEffectInstance(
            StatusEffects.HASTE, EFFECT_DURATION, 0, false, false, true
        ));
    }
}
