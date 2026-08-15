package saurerstoff.quality.docs.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import saurerstoff.quality.enchantment.QualityEnchantments;

public class QualityDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		
		pack.addProvider(BlockLootGenerator::new);
		pack.addProvider(ItemTagGenerator::new);

		QualityEnchantmentGenerator enchantmentGenerator = pack.addProvider(QualityEnchantmentGenerator::new);

		pack.addProvider((output, registriesFuture) ->
			new ModEnchantmentTagGenerator(output, enchantmentGenerator.getRegistriesFuture()));

		pack.addProvider(QualityAdvancementProvider::new);
	}

	@Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        // Registers the enchantment to the dynamic registry future
        registryBuilder.add(Registries.ENCHANTMENT, QualityEnchantments::bootstrap);
    }

}
