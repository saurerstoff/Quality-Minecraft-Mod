package saurerstoff.quality.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import saurerstoff.quality.ModComponents;
import saurerstoff.quality.QualityData;

@Mixin(FishingRodItem.class)
public class FishingRodMixin {
    
    @WrapOperation(
        method = "use",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getFishingLuckBonus(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/Entity;)I"
        )
    )
    private int applyQualityToFishingLuck(ServerLevel serverLevel, ItemStack itemStack, Entity entity, Operation<Integer> original){
        int luck = original.call(serverLevel, itemStack, entity);
        int qualityLevel = 0;
        if (itemStack.has(ModComponents.QUALITY_COMPONENT)){
            qualityLevel = itemStack.get(ModComponents.QUALITY_COMPONENT);
        }
        return luck + QualityData.FISHING_LUCK_BONUS[qualityLevel];
    } 
}
