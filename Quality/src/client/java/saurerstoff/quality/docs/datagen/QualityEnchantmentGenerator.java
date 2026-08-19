package saurerstoff.quality.docs.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import saurerstoff.quality.ModItemTags;
import saurerstoff.quality.enchantment.QualityEnchantments;

public class QualityEnchantmentGenerator extends FabricDynamicRegistryProvider {
    private final CompletableFuture<HolderLookup.Provider> registriesFuture;

    public QualityEnchantmentGenerator(FabricDataOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
        this.registriesFuture = registriesFuture;
    }

    @Override
    public String getName() {
        return "Quality Enchantments";
    }

    @Override
    protected void configure(Provider registries, Entries entries) {

        Enchantment pickaxeEnchantment = Enchantment.enchantment(
                Enchantment.definition(
                        registries.lookupOrThrow(Registries.ITEM).getOrThrow(ModItemTags.QUALITY_ENCHANTABLE),
                        10,
                        5, // max level
                        Enchantment.dynamicCost(1, 20),
                        Enchantment.dynamicCost(10, 40),
                        4,
                        EquipmentSlotGroup.MAINHAND))
                .build(QualityEnchantments.QUALITY_MINING.location());
        entries.add(QualityEnchantments.QUALITY_MINING, pickaxeEnchantment);
    }

    public CompletableFuture<HolderLookup.Provider> getRegistriesFuture() {
        return registriesFuture;
    }
}
