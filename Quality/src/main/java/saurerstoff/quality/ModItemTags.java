package saurerstoff.quality;


import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
    public static final TagKey<Item> QUALITY_ENCHANTABLE = 
        TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Quality.MOD_ID, "quality_enchantable"));
}
