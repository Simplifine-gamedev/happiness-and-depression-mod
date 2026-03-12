package com.happiness.client;

/**
 * Client-side happiness state storage
 */
public class ClientHappinessData {
    
    private static float happiness = 75f;
    private static float targetHappiness = 75f;
    
    // For smooth interpolation
    private static final float LERP_SPEED = 0.1f;
    
    public static float getHappiness() {
        return happiness;
    }
    
    public static void setHappiness(float value) {
        targetHappiness = value;
    }
    
    /**
     * Called every client tick to smoothly interpolate happiness
     */
    public static void tick() {
        // Smoothly interpolate towards target
        if (Math.abs(happiness - targetHappiness) > 0.1f) {
            happiness += (targetHappiness - happiness) * LERP_SPEED;
        } else {
            happiness = targetHappiness;
        }
    }
    
    public static float getHappinessPercent() {
        return happiness / 100f;
    }
    
    public static String getMoodDescription() {
        if (happiness >= 90) return "Euphoric";
        if (happiness >= 70) return "Happy";
        if (happiness >= 50) return "Content";
        if (happiness >= 30) return "Melancholy";
        if (happiness >= 15) return "Sad";
        return "Depressed";
    }
    
    public static int getMoodColor() {
        if (happiness >= 90) return 0xFFFF00; // Bright yellow
        if (happiness >= 70) return 0x90EE90; // Light green
        if (happiness >= 50) return 0xFFFFFF; // White
        if (happiness >= 30) return 0xADD8E6; // Light blue (melancholy)
        if (happiness >= 15) return 0x6495ED; // Cornflower blue (sad)
        return 0x4169E1; // Royal blue (depressed)
    }
}
