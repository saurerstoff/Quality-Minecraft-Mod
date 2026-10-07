package saurerstoff.quality;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    private ModSounds() {
		// private empty constructor to avoid accidental instantiation
	}

	public static final SoundEvent RECYCLER_LOOP = registerSound("recycler_loop");
	public static final SoundEvent RECYCLER_FINISHED = registerSound("recycler_finished");
    public static final int RECYCLER_LOOP_DURATION_TICKS = 20;

	// actual registration of all the custom SoundEvents
	private static SoundEvent registerSound(String id) {
		ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(Quality.MOD_ID, id);
		return Registry.register(BuiltInRegistries.SOUND_EVENT, resourceLocation, SoundEvent.createVariableRangeEvent(resourceLocation));
	}

	// This static method starts class initialization, which then initializes
	public static void initialize() {
		Quality.LOGGER.info("Registering " + Quality.MOD_ID + " Sounds");
	}
}
