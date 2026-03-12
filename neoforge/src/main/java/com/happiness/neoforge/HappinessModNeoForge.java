package com.happiness.neoforge;

import com.happiness.HappinessMod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(HappinessMod.MOD_ID)
public class HappinessModNeoForge {
    public HappinessModNeoForge(IEventBus modEventBus) {
        HappinessMod.init();
    }
}
