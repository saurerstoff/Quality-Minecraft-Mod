package saurerstoff.quality.mixin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import saurerstoff.quality.ModComponents;
import saurerstoff.quality.QualityIconRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class ItemQualityRendererMixin extends Screen{

    protected ItemQualityRendererMixin(Component titleIn) {super(titleIn);}

    @Inject(
        method = "renderSlot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            shift = Shift.AFTER
        )
    )
    public void renderQualityIconInSlot(GuiGraphics graphics, Slot slot, CallbackInfo info){
        if(!slot.getItem().has(ModComponents.QUALITY_COMPONENT)) return;
        QualityIconRenderer.renderQualityIconInSlot(graphics.pose(), slot);
    }
    
    @Inject(
        method = "renderFloatingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            shift = Shift.AFTER
        )
    )
    public void renderQualityIconOnFloatingItem(GuiGraphics graphics, ItemStack itemStack, int x, int y, String string, CallbackInfo info){
        if(!itemStack.has(ModComponents.QUALITY_COMPONENT)) return;
        QualityIconRenderer.renderQualityIconInSlot(graphics.pose(), itemStack, x, y);
        
    }
}