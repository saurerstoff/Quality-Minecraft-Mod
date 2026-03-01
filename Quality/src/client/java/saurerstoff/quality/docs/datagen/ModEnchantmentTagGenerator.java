package saurerstoff.quality.docs.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.tags.EnchantmentTags;
import saurerstoff.quality.enchantment.QualityEnchantments;

public class ModEnchantmentTagGenerator extends FabricTagProvider.EnchantmentTagProvider {

    public ModEnchantmentTagGenerator(FabricDataOutput output, CompletableFuture<Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(Provider wrapperLookup) {
        getOrCreateTagBuilder(EnchantmentTags.IN_ENCHANTING_TABLE)
                .addOptional(QualityEnchantments.QUALITY_MINING);

        getOrCreateTagBuilder(EnchantmentTags.TRADEABLE)
                .addOptional(QualityEnchantments.QUALITY_MINING);

        getOrCreateTagBuilder(EnchantmentTags.ON_RANDOM_LOOT)
                .addOptional(QualityEnchantments.QUALITY_MINING);
    }

}
