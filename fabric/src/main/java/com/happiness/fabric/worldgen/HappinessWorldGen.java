package com.happiness.fabric.worldgen;

import com.happiness.HappinessMod;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;

public class HappinessWorldGen {
    public static void registerWorldGen() {
        // Register happiness ore to spawn in all overworld biomes (like coal)
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES,
                RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(HappinessMod.MOD_ID, "happiness_ore"))
        );
        
        HappinessMod.LOGGER.info("Registered Happiness ore world generation");
    }
}
