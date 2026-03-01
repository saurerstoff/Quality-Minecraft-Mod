package saurerstoff.quality.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import saurerstoff.quality.ModComponents;
import saurerstoff.quality.QualityUtils;

@Mixin(ShapelessRecipe.class)
public class ShapelessRecipeMixin {

    @Inject(method = "assemble", at = @At("RETURN"), cancellable = true)
    private void applyQualityToOutput(CraftingInput input, HolderLookup.Provider provider, CallbackInfoReturnable<ItemStack> callbackReturn) {
        ItemStack result = callbackReturn.getReturnValue();

        if (result.isEmpty()) {
            return;
        }

        Integer qualityLevel = QualityUtils.getResultQuality(input);

        if (qualityLevel > 0 && qualityLevel < 5){
            ItemStack newResult = result.copy();
            newResult.set(ModComponents.QUALITY_COMPONENT, qualityLevel);
            callbackReturn.setReturnValue(newResult);
        }
    }
}
