package saurerstoff.quality;

import com.mojang.serialization.Codec;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class ModComponents {
    protected static void initialize() {
		Quality.LOGGER.info("Registering {} components", Quality.MOD_ID);
		// Technically this method can stay empty, but some developers like to notify
		// the console, that certain parts of the mod have been successfully initialized
	}

    public static final DataComponentType<Integer> QUALITY_COMPONENT = Registry.register(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        ResourceLocation.fromNamespaceAndPath(Quality.MOD_ID, "quality"),
        DataComponentType.<Integer>builder().persistent(Codec.INT).build()
    );
}
