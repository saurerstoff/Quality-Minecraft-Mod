package saurerstoff.quality.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import saurerstoff.quality.IQualityFurnaceData;
import saurerstoff.quality.ModComponents;
import saurerstoff.quality.QualityData;
import saurerstoff.quality.QualityUtils;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;

@Mixin(AbstractFurnaceBlockEntity.class)
abstract class AbstractFurnaceBlockEntityMixin extends BaseContainerBlockEntity implements WorldlyContainer, RecipeCraftingHolder, StackedContentsCompatible, IQualityFurnaceData {

    protected AbstractFurnaceBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public int getQualityValue() {
        if (!this.components().has(ModComponents.QUALITY_COMPONENT)) 
		    return 0;
        return this.components().get(ModComponents.QUALITY_COMPONENT);
    }

    @ModifyVariable(
        method = "canBurn",
        at = @At(
            value = "STORE", 
            target = "Lnet/minecraft/world/item/crafting/Recipe;getResultItem(Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/world/item/ItemStack;"
        ),
        ordinal = 0
    )
    private static ItemStack modifyCanBurnResult(ItemStack originalResult, @Local(argsOnly = true) NonNullList<ItemStack> slots) {
        if (originalResult.isEmpty()) return originalResult;
        
        ItemStack copy = originalResult.copy();
        ItemStack inputStack = slots.get(0);
        Integer qualityLevel = QualityUtils.getResultQuality(inputStack);
        
        if (qualityLevel > 0) {
            copy.set(ModComponents.QUALITY_COMPONENT, qualityLevel);
        }
        
        return copy;
    }

    @ModifyVariable(
        method = "burn",
        at = @At("STORE"),
        ordinal = 1
    )
    private static ItemStack modifyBurnResult(ItemStack originalResult, @Local(argsOnly = true) RegistryAccess registryAccess, @Local(argsOnly = true) RecipeHolder<?> recipe, @Local(argsOnly = true) NonNullList<ItemStack> slots) {
        if (originalResult.isEmpty()) return originalResult;

        ItemStack copy = originalResult.copy();
        ItemStack inputStack = slots.get(0);
        Integer qualityLevel = QualityUtils.getResultQuality(inputStack);

        if (qualityLevel > 0) {
            copy.set(ModComponents.QUALITY_COMPONENT, qualityLevel);
        }
        
        return copy;
    }

    @ModifyReturnValue(
        method = "getTotalCookTime",
        at = @At("RETURN")
    )
    private static int applyQualityToCookTime(int cookTime, @Local(argsOnly = true) AbstractFurnaceBlockEntity abstractFurnaceBlockEntity){
        if (!abstractFurnaceBlockEntity.components().has(ModComponents.QUALITY_COMPONENT)) 
		    return cookTime;

		switch (abstractFurnaceBlockEntity.components().get(ModComponents.QUALITY_COMPONENT)) {
			case 1:
				return (int)(cookTime * QualityData.UNCOMMON_FURNACE_COOKTIME_MOD);

			case 2:
				return (int)(cookTime * QualityData.RARE_FURNACE_COOKTIME_MOD);

			case 3:
				return (int)(cookTime * QualityData.EPIC_FURNACE_COOKTIME_MOD);

			case 4:
				return (int)(cookTime * QualityData.LEGENDARY_FURNACE_COOKTIME_MOD);
		
			default:
				return cookTime;
		}
    }
}
