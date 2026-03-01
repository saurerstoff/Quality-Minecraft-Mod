package saurerstoff.quality.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import saurerstoff.quality.IQualityFurnaceData;

@Mixin(AbstractFurnaceMenu.class)
public abstract class AbstractFurnaceMenuMixin extends RecipeBookMenu<SingleRecipeInput, AbstractCookingRecipe> implements IQualityFurnaceData{
    
    private int syncedQualityValue;

    protected AbstractFurnaceMenuMixin(
		MenuType<?> menuType,
		int i
	) {
        super(menuType, i);
    }

    @Override
    public int getSyncedQualityValue(){
        return this.syncedQualityValue;
    }

    @Inject(
        method = "<init>(Lnet/minecraft/world/inventory/MenuType;Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/inventory/RecipeBookType;ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/Container;Lnet/minecraft/world/inventory/ContainerData;)V", 
        at = @At("RETURN")
    )
    private void onInit(
        MenuType<?> menuType, 
        RecipeType<?> recipeType, 
        RecipeBookType recipeBookType, 
        int containerId, 
        Inventory playerInventory, 
        Container container, 
        ContainerData containerData, 
        CallbackInfo ci){
        this.addDataSlot(new DataSlot() {
            @Override
            public int get() {
                if (container instanceof IQualityFurnaceData qualityData){
                    return qualityData.getQualityValue();
                }
                return 0;
            }
            @Override
            public void set(int i) {
                AbstractFurnaceMenuMixin.this.syncedQualityValue = i;
            }
        });
    }
}

