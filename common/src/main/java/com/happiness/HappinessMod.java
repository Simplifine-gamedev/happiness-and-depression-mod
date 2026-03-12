package com.happiness;

import com.happiness.block.ModBlocks;
import com.happiness.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HappinessMod {
    public static final String MOD_ID = "happiness";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOGGER.info("Happiness Mod initializing - spreading joy throughout the world!");
        ModBlocks.init();
        ModItems.init();
    }
}
