package saurerstoff.quality.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import saurerstoff.quality.QualityData;
import saurerstoff.quality.enchantment.EnchantmentState;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    
    @ModifyVariable(
            method = "selectEnchantment", 
            at = @At("HEAD"), 
            ordinal = 0, 
            argsOnly = true
    )
    private static int applyQualityToEnchantment(int originalLevel) {
        int quality = EnchantmentState.CURRENT_QUALITY.get();
        return originalLevel + QualityData.LAPIS_ENCHANT_BONUS[quality];
    }
}
