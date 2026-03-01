package saurerstoff.quality.docs.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class QualityDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		
		pack.addProvider(BlockLootGenerator::new);
		pack.addProvider(ItemTagGenerator::new);

		QualityEnchantmentGenerator enchantmentGenerator = pack.addProvider(QualityEnchantmentGenerator::new);

		pack.addProvider((output, registriesFuture) ->
			new ModEnchantmentTagGenerator(output, enchantmentGenerator.getRegistriesFuture()));
	}
}
