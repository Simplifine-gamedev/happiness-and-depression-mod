package com.happiness.item;

import com.happiness.HappinessMod;
import com.happiness.block.ModBlocks;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(HappinessMod.MOD_ID, RegistryKeys.ITEM);

    // Block items
    public static final RegistrySupplier<Item> HAPPINESS_ORE = ITEMS.register("happiness_ore", () ->
            new BlockItem(ModBlocks.HAPPINESS_ORE.get(), new Item.Settings())
    );

    public static final RegistrySupplier<Item> DEEPSLATE_HAPPINESS_ORE = ITEMS.register("deepslate_happiness_ore", () ->
            new BlockItem(ModBlocks.DEEPSLATE_HAPPINESS_ORE.get(), new Item.Settings())
    );

    // Raw happiness ore (dropped when mining)
    public static final RegistrySupplier<Item> RAW_HAPPINESS = ITEMS.register("raw_happiness", () ->
            new Item(new Item.Settings())
    );

    // Happiness Pill - the smelted result, can be given to villagers
    public static final RegistrySupplier<Item> HAPPINESS_PILL = ITEMS.register("happiness_pill", () ->
            new HappinessPillItem(new Item.Settings().maxCount(64))
    );

    public static void init() {
        ITEMS.register();
        HappinessMod.LOGGER.info("Registered Happiness items");
    }
}
