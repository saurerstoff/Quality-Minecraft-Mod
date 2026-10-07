package saurerstoff.quality;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.level.storage.loot.LootContext;

public class QualityUtils {
    public static int getResultQuality(CraftingInput input) {
        Integer qualityLevel = 4;
        for (int i = 0; i < input.size(); i++) {
            ItemStack ingredient = input.getItem(i);
            if (!ingredient.isEmpty()) {
                if (ingredient.has(ModComponents.QUALITY_COMPONENT)) {
                    Integer ingredientQuality = ingredient.get(ModComponents.QUALITY_COMPONENT);
                    if (ingredientQuality < qualityLevel) {
                        qualityLevel = ingredientQuality;
                    }
                } else {
                    qualityLevel = 0;
                    break;
                }
            }
        }

        return qualityLevel;
    }

    public static int getResultQuality(ItemStack input) {
        Integer qualityLevel = 0;
        if (!input.isEmpty()) {
            if (input.has(ModComponents.QUALITY_COMPONENT)) {
                Integer ingredientQuality = input.get(ModComponents.QUALITY_COMPONENT);
                qualityLevel = ingredientQuality;
            } else {
                qualityLevel = 0;
            }
        }

        return qualityLevel;
    }

    public static int getResultQuality(SingleRecipeInput input) {
        Integer qualityLevel = 4;
        ItemStack ingredient = input.item();
        if (!ingredient.isEmpty()) {
            if (ingredient.has(ModComponents.QUALITY_COMPONENT)) {
                Integer ingredientQuality = ingredient.get(ModComponents.QUALITY_COMPONENT);
                if (ingredientQuality < qualityLevel) {
                    qualityLevel = ingredientQuality;
                }
            } else {
                qualityLevel = 0;
            }
        }
        return qualityLevel;
    }

    public static int getResultQuality(SmithingRecipeInput input) {
        return Math.min(
                getResultQuality(input.getItem(0)),
                Math.min(
                        getResultQuality(input.getItem(1)),
                        getResultQuality(input.getItem(2))));
    }

    public static int rollAndApplyQuality(ItemStack itemStack, LootContext lootContext, int level) {
        return rollAndApplyQuality(itemStack, lootContext.getRandom(), level, 0);
    }

    public static int rollAndApplyQuality(ItemStack itemStack, RandomSource random, int level) {
        return rollAndApplyQuality(itemStack, random, level, 0);
    }

    public static int rollAndApplyQuality(ItemStack itemStack, RandomSource random, int level, int inputQuality) {
        int qualityLevel = rollQuality(itemStack, random, level, inputQuality);
        
        if (qualityLevel > 0 && qualityLevel < 5) {
            itemStack.set(ModComponents.QUALITY_COMPONENT, qualityLevel);
            return qualityLevel;
        }
        else return 0;
    }

    public static int rollQuality(ItemStack itemStack, LootContext lootContext, int level) {
        return rollQuality(itemStack, lootContext.getRandom(), level, 0);
    }

    public static int rollQuality(ItemStack itemStack, RandomSource random, int level) {
        return rollQuality(itemStack, random, level, 0);
    }

    public static int rollQuality(ItemStack itemStack, RandomSource random, int level, int inputQuality) {
        if (inputQuality == 4) {
            itemStack.set(ModComponents.QUALITY_COMPONENT, 4);
            return 4;
        }

        if (level > 0) {
            int qualityLevel = inputQuality;

            float q = 0.03f + (level - 1) * 0.06f;
            float roll = random.nextFloat();

            if (roll < q) {
                qualityLevel++;
                while (qualityLevel < 4) {
                    roll = random.nextFloat();
                    if (roll < 0.1f) {
                        qualityLevel++;
                    } else {
                        break;
                    }
                }
            }

            if (qualityLevel > 0 && qualityLevel < 5) {
                return qualityLevel;
            }
        }

        return 0;
    }
}
