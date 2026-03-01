package saurerstoff.quality.docs.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.tags.ItemTags;
import saurerstoff.quality.ModItemTags;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider  {

    public ItemTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

    @Override
    protected void addTags(Provider wrapperLookup) {
        getOrCreateTagBuilder(ModItemTags.QUALITY_ENCHANTABLE)
            .addOptionalTag(ItemTags.PICKAXES)
            .addOptionalTag(ItemTags.AXES)
            .addOptionalTag(ItemTags.SHOVELS)
            .addOptionalTag(ItemTags.SWORDS);
    }
    
}
