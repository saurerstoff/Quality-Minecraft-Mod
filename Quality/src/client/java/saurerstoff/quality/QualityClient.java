package saurerstoff.quality;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class QualityClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		QualityTooltip.registerTooltips();
		MenuScreens.register(ModMenuTypes.RECYCLER, RecyclerScreen::new);
	}
}