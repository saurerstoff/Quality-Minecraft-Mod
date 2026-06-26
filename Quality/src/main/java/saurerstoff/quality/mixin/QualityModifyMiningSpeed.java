package saurerstoff.quality.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Tool;
import saurerstoff.quality.ModComponents;
import saurerstoff.quality.QualityData;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;


@Mixin(Item.class)
public class QualityModifyMiningSpeed {

	@ModifyReturnValue(
		method = "getDestroySpeed", 
		at = @At("RETURN")
	)
	public float ModifyMiningSpeed(float miningSpeed, @Local(argsOnly = true) ItemStack itemStack){
		Tool tool = itemStack.get(DataComponents.TOOL);

		if (!itemStack.has(ModComponents.QUALITY_COMPONENT)
			|| tool == null 
			|| miningSpeed <= 1.0F
			|| itemStack.is(ItemTags.SWORDS))
		return miningSpeed;

		return miningSpeed * QualityData.MINING_SPEED_BONUS[itemStack.get(ModComponents.QUALITY_COMPONENT)];
	}
}