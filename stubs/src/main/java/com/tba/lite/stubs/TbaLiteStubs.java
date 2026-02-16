package com.tba.lite.stubs;

import com.tba.lite.stubs.block.StubDisplayBlockRegistration;
import com.tba.lite.stubs.network.CoreCurriculumPayloads;
import com.tba.lite.stubs.network.SceneCraftPayloads;
import com.tba.lite.stubs.network.StreamCraftPayloads;
import com.tba.lite.stubs.network.SynthCraftPayloads;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TbaLiteStubs implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("tba-lite-stubs");

    @Override
    public void onInitialize() {
        LOGGER.info("TBA Lite Stubs loading — registering compatibility payloads");

        // Register StreamCraft display block (block + item + block entity)
        StubDisplayBlockRegistration.initialize();

        // Register all payload types from the 4 custom mods
        StreamCraftPayloads.register();
        SynthCraftPayloads.register();
        SceneCraftPayloads.register();
        CoreCurriculumPayloads.register();

        LOGGER.info("TBA Lite Stubs loaded — 34 payload types registered");
    }
}
