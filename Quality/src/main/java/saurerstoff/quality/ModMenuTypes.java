package saurerstoff.quality;


import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import saurerstoff.quality.blocks.RecyclerMenu;

public class ModMenuTypes {
    public static final MenuType<RecyclerMenu> RECYCLER = register("recycler", RecyclerMenu::new);

	public static <T extends AbstractContainerMenu> MenuType<T> register(
					String name,
					MenuType.MenuSupplier<T> constructor
	) {
		return Registry.register(BuiltInRegistries.MENU, ResourceLocation.fromNamespaceAndPath(Quality.MOD_ID, name), new MenuType<>(constructor, FeatureFlagSet.of()));
	}

    protected static void initialize() {	}
}
