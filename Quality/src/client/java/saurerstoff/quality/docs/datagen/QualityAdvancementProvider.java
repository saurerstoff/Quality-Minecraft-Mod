package saurerstoff.quality.docs.datagen;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.EnchantedItemTrigger;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.FishingRodHookedTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemEnchantmentsPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemSubPredicates;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import saurerstoff.quality.Quality;
import saurerstoff.quality.QualityItemPredicate;
import saurerstoff.quality.criterions.SmeltQualityCriterion;
import saurerstoff.quality.criterions.CraftQualityCriterion;
import saurerstoff.quality.criterions.QualityCriterions;
import saurerstoff.quality.enchantment.QualityEnchantments;

public class QualityAdvancementProvider extends FabricAdvancementProvider {
        protected QualityAdvancementProvider(FabricDataOutput output,
                        CompletableFuture<HolderLookup.Provider> registryLookup) {
                super(output, registryLookup);
        }

    @Override
    public void generateAdvancement(HolderLookup.Provider wrapperLookup, Consumer<AdvancementHolder> consumer) {
        
        HolderGetter<Enchantment> enchantmentLookup = wrapperLookup.lookupOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> qualityEnchantment = enchantmentLookup.getOrThrow(QualityEnchantments.QUALITY_MINING);

        
        ItemPredicate hasQualityEnchant = ItemPredicate.Builder.item()
                .withSubPredicate(
                        ItemSubPredicates.ENCHANTMENTS,
                        ItemEnchantmentsPredicate.enchantments(List.of(
                                new EnchantmentPredicate(qualityEnchantment, MinMaxBounds.Ints.atLeast(1)))))
                .build();

        ItemPredicate hasAnyQuality = ItemPredicate.Builder.item()
                .withSubPredicate(
                        QualityItemPredicate.HAS_QUALITY_PREDICATE_TYPE, 
                        new QualityItemPredicate.HasQualityPredicate(MinMaxBounds.Ints.between(1, 4)))
                .build();

        ItemPredicate hasLegendaryQuality = ItemPredicate.Builder.item()
                .withSubPredicate(
                        QualityItemPredicate.HAS_QUALITY_PREDICATE_TYPE, 
                        new QualityItemPredicate.HasQualityPredicate(MinMaxBounds.Ints.exactly(4)))
                .build();

        ItemPredicate isPickaxe = ItemPredicate.Builder.item()
                .of(ItemTags.PICKAXES)
                .build();

        ItemPredicate isSword = ItemPredicate.Builder.item()
                .of(ItemTags.SWORDS)
                .build();

        ItemPredicate isArmor = ItemPredicate.Builder.item()
                .of(ItemTags.ARMOR_ENCHANTABLE)
                .build();

        ItemPredicate isNetheritePickaxe = ItemPredicate.Builder.item()
                .of(Items.NETHERITE_PICKAXE)
                .build();

        ItemPredicate isFurnace = ItemPredicate.Builder.item()
                .of(Items.FURNACE)
                .build();

        AdvancementHolder enchantQuality = Advancement.Builder.advancement()
                .display(
                        Items.ENCHANTED_BOOK,
                        Component.literal("Quality"),
                        Component.literal("Enchant a tool with Quality"),
                        ResourceLocation.withDefaultNamespace("textures/gui/advancements/backgrounds/adventure.png"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .addCriterion("enchant_quality",
                        CriteriaTriggers.ENCHANTED_ITEM.createCriterion(
                                new EnchantedItemTrigger.TriggerInstance(
                                        Optional.empty(),
                                        Optional.of(hasQualityEnchant),
                                        MinMaxBounds.Ints.ANY)))
                .save(consumer, Quality.MOD_ID + "/enchant_quality");

        AdvancementHolder getQualityItem = Advancement.Builder.advancement()
                .parent(enchantQuality)
                .display(
                        Items.RAW_IRON,
                        Component.literal("Make it better"),
                        Component.literal("Get an item with quality"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .addCriterion("get_quality_item",
                        CriteriaTriggers.INVENTORY_CHANGED.createCriterion(
                            new InventoryChangeTrigger.TriggerInstance(
                                Optional.empty(),
                                InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                                List.of(hasAnyQuality)
                            ))
                        )
                .save(consumer, Quality.MOD_ID + "/get_quality_item");

        AdvancementHolder craftQualityItem = Advancement.Builder.advancement()
                .parent(getQualityItem)
                .display(
                        Items.CRAFTING_TABLE,
                        Component.literal("Crafting with quality"),
                        Component.literal("Craft a quality item using quality ingredients"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .addCriterion("craft_quality_item",
                        QualityCriterions.CRAFT_QUALITY.createCriterion(
                                new CraftQualityCriterion.Conditions(
                                        Optional.empty(), 
                                        1, 
                                        List.of()
                                ))
                        )
                .save(consumer, Quality.MOD_ID + "/craft_quality_item");

        AdvancementHolder smeltQualityItem = Advancement.Builder.advancement()
                .parent(getQualityItem)
                .display(
                        Items.FURNACE,
                        Component.literal("Smelting with quality"),
                        Component.literal("Smelt a quality item"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .addCriterion("smelt_quality_item",
                        QualityCriterions.SMELT_QUALITY.createCriterion(
                                new SmeltQualityCriterion.Conditions(
                                        Optional.empty(), 
                                        1, 
                                        Optional.empty(),
                                        0
                                ))
                        )
                .save(consumer, Quality.MOD_ID + "/smelt_quality_item");

        AdvancementHolder getLegendaryItem = Advancement.Builder.advancement()
                .parent(getQualityItem)
                .display(
                        Items.DIAMOND,
                        Component.literal("My items are legendary"),
                        Component.literal("Get an item with legendary quality"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .addCriterion("get_legendary_item",
                        CriteriaTriggers.INVENTORY_CHANGED.createCriterion(
                            new InventoryChangeTrigger.TriggerInstance(
                                Optional.empty(),
                                InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                                List.of(hasLegendaryQuality)
                            ))
                        )
                .save(consumer, Quality.MOD_ID + "/get_legendary_item");

        AdvancementHolder craftQualityPickaxe = Advancement.Builder.advancement()
                .parent(craftQualityItem)
                .display(
                        Items.IRON_PICKAXE,
                        Component.literal("You are doing it right"),
                        Component.literal("Craft a quality pickaxe"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .addCriterion("craft_quality_pickaxe",
                        QualityCriterions.CRAFT_QUALITY.createCriterion(
                                new CraftQualityCriterion.Conditions(
                                        Optional.empty(), 
                                        1, 
                                        List.of(isPickaxe)))
                        )
                .save(consumer, Quality.MOD_ID + "/craft_quality_pickaxe");

        AdvancementHolder obtainLegendaryPickaxe = Advancement.Builder.advancement()
                .parent(craftQualityPickaxe)
                .display(
                        Items.NETHERITE_PICKAXE,
                        Component.literal("Mining with determination"),
                        Component.literal("Obtain a legendary netherite pickaxe"),
                        null,
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        false)
                .addCriterion("obtain_legendary_pickaxe",
                        CriteriaTriggers.INVENTORY_CHANGED.createCriterion(
                            new InventoryChangeTrigger.TriggerInstance(
                                Optional.empty(),
                                InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                                List.of(isNetheritePickaxe, hasLegendaryQuality)
                            ))
                        )
                .save(consumer, Quality.MOD_ID + "/obtain_legendary_pickaxe");

        AdvancementHolder craftQualityFurnace = Advancement.Builder.advancement()
                .parent(craftQualityItem)
                .display(
                        Items.FURNACE,
                        Component.literal("Smelting with speed"),
                        Component.literal("Craft a quality furnace"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .addCriterion("craft_quality_furnace",
                        QualityCriterions.CRAFT_QUALITY.createCriterion(
                                new CraftQualityCriterion.Conditions(
                                        Optional.empty(), 
                                        1, 
                                        List.of(isFurnace)))
                        )
                .save(consumer, Quality.MOD_ID + "/craft_quality_furnace");

        AdvancementHolder smeltLegendary = Advancement.Builder.advancement()
                .parent(smeltQualityItem)
                .display(
                        Items.CHARCOAL,
                        Component.literal("Art of smelting"),
                        Component.literal("Smelt a legendary item with a legendary furnace"),
                        null,
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        false)
                .addCriterion("smelt_legendary",
                        QualityCriterions.SMELT_QUALITY.createCriterion(
                                new SmeltQualityCriterion.Conditions(
                                        Optional.empty(), 
                                        4, 
                                        Optional.empty(),
                                        4
                                ))
                        )
                .save(consumer, Quality.MOD_ID + "/smelt_legendary");

        AdvancementHolder craftQualitySword = Advancement.Builder.advancement()
                .parent(craftQualityItem)
                .display(
                        Items.IRON_SWORD,
                        Component.literal("We need bigger swords"),
                        Component.literal("Craft a quality sword"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .addCriterion("craft_quality_sword",
                        QualityCriterions.CRAFT_QUALITY.createCriterion(
                                new CraftQualityCriterion.Conditions(
                                        Optional.empty(), 
                                        1, 
                                        List.of(isSword)))
                        )
                .save(consumer, Quality.MOD_ID + "/craft_quality_sword");

        AdvancementHolder craftQualityArmor = Advancement.Builder.advancement()
                .parent(craftQualityItem)
                .display(
                        Items.IRON_CHESTPLATE,
                        Component.literal("Look at my shiny rare armor"),
                        Component.literal("Craft a quality armor"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .addCriterion("craft_quality_armor",
                        QualityCriterions.CRAFT_QUALITY.createCriterion(
                                new CraftQualityCriterion.Conditions(
                                        Optional.empty(), 
                                        1, 
                                        List.of(isArmor)))
                        )
                .save(consumer, Quality.MOD_ID + "/craft_quality_armor");

        AdvancementHolder fishWithQualityRod = Advancement.Builder.advancement()
                .parent(craftQualityItem)
                .display(
                        Items.FISHING_ROD,
                        Component.literal("Today's fish is trout a la creme"),
                        Component.literal("Fish using a quality fishing rod"),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        false)
                .addCriterion("fish_with_quality_rod",
                        CriteriaTriggers.FISHING_ROD_HOOKED.createCriterion(
                                new FishingRodHookedTrigger.TriggerInstance(
                                        Optional.empty(),
                                        Optional.of(hasAnyQuality),
                                        Optional.empty(),
                                        Optional.empty()
                                ))
                        )
                .save(consumer, Quality.MOD_ID + "/fish_with_quality_rod");

        /*
                TODO
                - enchant with quality lapis
                - smelt with quality fuel
        */
    }
}
