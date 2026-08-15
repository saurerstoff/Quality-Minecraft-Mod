package saurerstoff.quality;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import saurerstoff.quality.criterions.QualityCriterions;
import saurerstoff.quality.loot.ApplyQualityFunction;
import saurerstoff.quality.loot.ModLootFunctions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Quality implements ModInitializer {
	public static final String MOD_ID = "quality";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModComponents.initialize();
		QualityItemPredicate.initialize();
		ModLootFunctions.initialize();
		QualityCriterions.initialize();

		applyQualityToLoot();

		LOGGER.info("Finished Initializing Quality!");
	}

	public void applyQualityToLoot() {
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			tableBuilder.modifyPools(poolBuilder -> {
				poolBuilder.apply(ApplyQualityFunction.builder());
			});
		});
	}
}