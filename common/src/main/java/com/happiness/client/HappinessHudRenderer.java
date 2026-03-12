package com.happiness.client;

import com.happiness.HappinessManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

/**
 * Renders happiness as icons (like hearts/hunger) above the hotbar.
 * 
 * Position: Right side, above hotbar, mirroring hunger bar placement
 * Display: 10 sun icons representing 0-100 happiness
 */
public class HappinessHudRenderer {
    
    // Texture for happiness icons (sun icons)
    private static final Identifier HAPPINESS_ICONS = Identifier.of("happiness", "textures/gui/happiness_icons.png");
    
    // Icon dimensions
    private static final int ICON_SIZE = 9;  // Same as hearts/hunger
    private static final int ICON_SPACING = 8; // Spacing between icons
    private static final int TOTAL_ICONS = 10; // 10 icons = 100 happiness (10 each)
    
    // Texture UV coordinates (in a 32x32 texture)
    // Row 0: Full sun, Half sun, Empty sun
    // Row 1: Variants for low happiness (darker versions)
    private static final int FULL_ICON_U = 0;
    private static final int HALF_ICON_U = 9;
    private static final int EMPTY_ICON_U = 18;
    private static final int NORMAL_V = 0;
    private static final int SAD_V = 9;  // Darker variant when happiness is low
    
    public static void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        // Don't render if no player, in creative, spectator, or HUD is hidden
        if (client.player == null || client.options.hudHidden) {
            return;
        }
        
        // Don't show in Creative or Spectator mode
        if (client.player.isCreative() || client.player.isSpectator()) {
            return;
        }
        
        float happiness = ClientHappinessData.getHappiness();
        
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        
        // Position: Right side of screen, same height as hunger bar
        // Hunger bar is at: screenWidth / 2 + 10, screenHeight - 39
        // We'll put happiness on the LEFT side, mirroring hearts
        // Hearts are at: screenWidth / 2 - 91, screenHeight - 39
        // Put happiness ABOVE the hearts row
        int startX = screenWidth / 2 - 91;
        int startY = screenHeight - 39 - 10; // 10 pixels above hearts
        
        // Determine which texture row to use based on happiness level
        int vOffset = happiness < 30 ? SAD_V : NORMAL_V;
        
        // Calculate how many full and half icons to show
        // 100 happiness = 10 full icons
        // Each icon represents 10 happiness points
        int fullIcons = (int) (happiness / 10);
        boolean hasHalf = (happiness % 10) >= 5;
        
        // Draw the icons right-to-left (like hunger) or left-to-right (like hearts)
        // Let's do left-to-right like hearts
        for (int i = 0; i < TOTAL_ICONS; i++) {
            int x = startX + (i * ICON_SPACING);
            int y = startY;
            
            // First draw empty background
            context.drawTexture(HAPPINESS_ICONS, x, y, EMPTY_ICON_U, vOffset, ICON_SIZE, ICON_SIZE, 32, 32);
            
            // Then draw full or half icon on top
            if (i < fullIcons) {
                // Full icon
                context.drawTexture(HAPPINESS_ICONS, x, y, FULL_ICON_U, vOffset, ICON_SIZE, ICON_SIZE, 32, 32);
            } else if (i == fullIcons && hasHalf) {
                // Half icon
                context.drawTexture(HAPPINESS_ICONS, x, y, HALF_ICON_U, vOffset, ICON_SIZE, ICON_SIZE, 32, 32);
            }
        }
        
        // Add a subtle bounce/shake effect when happiness is very low
        if (happiness < 15 && client.world != null) {
            // Could add animation here in the future
        }
    }
}
