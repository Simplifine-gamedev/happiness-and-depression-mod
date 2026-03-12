package com.happiness.block;

import com.happiness.HappinessMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(HappinessMod.MOD_ID, RegistryKeys.BLOCK);

    // Happiness Ore - drops experience like coal ore, has similar properties
    public static final RegistrySupplier<Block> HAPPINESS_ORE = BLOCKS.register("happiness_ore", () ->
            new ExperienceDroppingBlock(
                    UniformIntProvider.create(0, 2),
                    AbstractBlock.Settings.create()
                            .strength(3.0f, 3.0f)
                            .requiresTool()
                            .sounds(BlockSoundGroup.STONE)
            )
    );

    // Deepslate variant
    public static final RegistrySupplier<Block> DEEPSLATE_HAPPINESS_ORE = BLOCKS.register("deepslate_happiness_ore", () ->
            new ExperienceDroppingBlock(
                    UniformIntProvider.create(0, 2),
                    AbstractBlock.Settings.create()
                            .strength(4.5f, 3.0f)
                            .requiresTool()
                            .sounds(BlockSoundGroup.DEEPSLATE)
            )
    );

    public static void init() {
        BLOCKS.register();
        HappinessMod.LOGGER.info("Registered Happiness blocks");
    }
}
