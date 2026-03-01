package saurerstoff.quality.enchantment;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import saurerstoff.quality.Quality;

public class QualityEnchantments {
    public static final ResourceKey<Enchantment> QUALITY_MINING = ResourceKey.create(
        Registries.ENCHANTMENT,
        ResourceLocation.fromNamespaceAndPath(Quality.MOD_ID, "quality_mining")
    );
}
