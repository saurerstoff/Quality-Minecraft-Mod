package saurerstoff.quality.enchantment;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import saurerstoff.quality.ModItemTags;
import saurerstoff.quality.Quality;

public class QualityEnchantments {
    public static final ResourceKey<Enchantment> QUALITY_MINING = ResourceKey.create(
        Registries.ENCHANTMENT,
        ResourceLocation.fromNamespaceAndPath(Quality.MOD_ID, "quality_mining")
    );

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderGetter<Item> itemLookup = context.lookup(Registries.ITEM);

        Enchantment pickaxeEnchantment = Enchantment.enchantment(
                Enchantment.definition(
                        itemLookup.getOrThrow(ModItemTags.QUALITY_ENCHANTABLE),
                        10, // weight
                        5,  // max level
                        Enchantment.dynamicCost(1, 20),
                        Enchantment.dynamicCost(10, 40),
                        4,  // anvil cost
                        EquipmentSlotGroup.MAINHAND
                )
        ).build(QUALITY_MINING.location());

        context.register(QUALITY_MINING, pickaxeEnchantment);
    }
}
