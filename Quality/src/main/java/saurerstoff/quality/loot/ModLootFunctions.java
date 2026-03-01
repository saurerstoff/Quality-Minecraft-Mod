package saurerstoff.quality.loot;


import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import saurerstoff.quality.Quality;

public class ModLootFunctions {
    public static final LootItemFunctionType<ApplyQualityFunction> APPLY_QUALITY = Registry.register(
        BuiltInRegistries.LOOT_FUNCTION_TYPE,
        ResourceLocation.fromNamespaceAndPath(Quality.MOD_ID, "apply_quality"),
        new LootItemFunctionType<>(ApplyQualityFunction.CODEC)
    );

    public static void initialize(){
        
    }
}
