package saurerstoff.quality.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import saurerstoff.quality.IQualityFurnaceData;
import saurerstoff.quality.QualityIconRenderer;

@Mixin(AbstractFurnaceScreen.class)
public abstract class AbstractFurnaceScreenMixin extends AbstractContainerScreen<AbstractFurnaceMenu>{
    
    public AbstractFurnaceScreenMixin(AbstractFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Inject(
        method = "render", 
        at = @At("TAIL"))
    private void renderCustomValue(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        // 1. Get the synced value from the menu
        int qualityValue = ((IQualityFurnaceData) this.menu).getSyncedQualityValue();

        // 2. Define coordinates (leftPos and topPos are the top-left corner of the GUI texture)
        int x = this.leftPos + 8;
        int y = this.topPos + 8;

        QualityIconRenderer.renderQualityIcon(graphics.pose(), qualityValue, x, y, 8, 8);
        //graphics.drawString(this.font, "Value: " + qualityValue, textX, textY, 0xFFFFFF, false);
        
    }
}
