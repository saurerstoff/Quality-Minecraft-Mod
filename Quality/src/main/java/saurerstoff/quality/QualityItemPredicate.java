package saurerstoff.quality;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class QualityItemPredicate {
    public static final ItemSubPredicate.Type<HasQualityPredicate> HAS_QUALITY_PREDICATE_TYPE = 
        Registry.register(
            BuiltInRegistries.ITEM_SUB_PREDICATE_TYPE, 
            ResourceLocation.fromNamespaceAndPath(Quality.MOD_ID, "has_quality"), 
            new ItemSubPredicate.Type<HasQualityPredicate>(HasQualityPredicate.CODEC));

    protected static void initialize() {
		Quality.LOGGER.info("Registering {} item predicates", Quality.MOD_ID);
		// Technically this method can stay empty, but some developers like to notify
		// the console, that certain parts of the mod have been successfully initialized
	}

    public record HasQualityPredicate(MinMaxBounds.Ints bounds) implements ItemSubPredicate{
        public static final Codec<HasQualityPredicate> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                MinMaxBounds.Ints.CODEC.optionalFieldOf("bounds", MinMaxBounds.Ints.ANY).forGetter(HasQualityPredicate::bounds)
            ).apply(instance, HasQualityPredicate::new));

        @Override
        public boolean matches(ItemStack stack){
            int quality = QualityUtils.getResultQuality(stack);
            return this.bounds.matches(quality);
        }
    }
}
