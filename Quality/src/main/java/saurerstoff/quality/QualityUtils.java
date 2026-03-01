package saurerstoff.quality;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmithingRecipeInput;

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

    public static int getResultQuality(SmithingRecipeInput input){
        return 
        Math.min(
            getResultQuality(input.getItem(0)), 
            Math.min(
                getResultQuality(input.getItem(1)), 
                getResultQuality(input.getItem(2))
            )
        );
    }
}
