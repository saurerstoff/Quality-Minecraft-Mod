package saurerstoff.quality.mixin;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.ShapedRecipe;
import saurerstoff.quality.ModComponents;
import saurerstoff.quality.Quality;
import saurerstoff.quality.QualityData;
import saurerstoff.quality.QualityUtils;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin<T extends RecipeInput> {
    @Unique
    private static final ResourceLocation BASE_ATTACK_DAMAGE_ID = ResourceLocation
            .withDefaultNamespace("base_attack_damage");

    @Inject(method = "assemble", at = @At("RETURN"), cancellable = true)
    private void applyQualityToOutput(CraftingInput input, HolderLookup.Provider provider,
            CallbackInfoReturnable<ItemStack> callbackReturn) {

        ItemStack result = callbackReturn.getReturnValue();
        if (result.isEmpty()) {
            return;
        }

        Integer qualityLevel = QualityUtils.getResultQuality(input);

        if (qualityLevel > 0 && qualityLevel < 5) {
            ItemStack newResult = result.copy();
            newResult.set(ModComponents.QUALITY_COMPONENT, qualityLevel);

            if (newResult.is(ItemTags.SWORDS)) {
                modifySwordOutput(newResult, qualityLevel);
            } else if (newResult.is(ItemTags.ARMOR_ENCHANTABLE)) {
                modifyArmorOutput(newResult, qualityLevel);
            }

            callbackReturn.setReturnValue(newResult);
        }
    }

    private void modifySwordOutput(ItemStack itemStack, int qualityLevel) {
        Quality.LOGGER.info("crafting sword");
        ItemAttributeModifiers currentModifiers = itemStack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();

        for (ItemAttributeModifiers.Entry entry : currentModifiers.modifiers()) {
            if (entry.attribute() == Attributes.ATTACK_DAMAGE) {
                Quality.LOGGER.info("Modifier" + entry.modifier());
                float bonus = QualityData.SWORD_DAMAGE_BONUS[qualityLevel];
                builder.add(
                        entry.attribute(), new AttributeModifier(BASE_ATTACK_DAMAGE_ID,
                                entry.modifier().amount() + bonus, AttributeModifier.Operation.ADD_VALUE),
                        entry.slot());
            } else {
                builder.add(entry.attribute(), entry.modifier(), entry.slot());
            }
        }

        itemStack.set(DataComponents.ATTRIBUTE_MODIFIERS, builder.build());
    }

    private void modifyArmorOutput(ItemStack itemStack, Integer qualityLevel) {
        Quality.LOGGER.info("crafting armor");
        ItemAttributeModifiers currentModifiers = itemStack.get(DataComponents.ATTRIBUTE_MODIFIERS);
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        if (itemStack.getItem() instanceof ArmorItem armor) {
            Quality.LOGGER.info(armor.getType().getName());

            for (ItemAttributeModifiers.Entry entry : currentModifiers.modifiers()) {
                Quality.LOGGER.info(entry.attribute() + " " + entry.modifier());
                builder.add(entry.attribute(), entry.modifier(), entry.slot());
            }

            int originalDefense = armor.getDefense();

            ResourceLocation resourceLocation = ResourceLocation.withDefaultNamespace("armor." + armor.getType().getName());
            builder.add(
                Attributes.ARMOR, 
                new AttributeModifier(resourceLocation, originalDefense + QualityData.ARMOR_BONUS[qualityLevel], AttributeModifier.Operation.ADD_VALUE), 
                EquipmentSlotGroup.bySlot(armor.getEquipmentSlot())
            );

            itemStack.set(DataComponents.ATTRIBUTE_MODIFIERS, builder.build());
        }
        else return;
    }
}
