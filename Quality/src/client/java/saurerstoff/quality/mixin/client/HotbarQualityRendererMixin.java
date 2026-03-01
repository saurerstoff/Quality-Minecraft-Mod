package saurerstoff.quality.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import saurerstoff.quality.ModComponents;
import saurerstoff.quality.QualityIconRenderer;

@Mixin(Gui.class)
public class HotbarQualityRendererMixin {
    @Inject(
        method = "renderSlot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V", 
            shift = Shift.AFTER
        )
    )
    public void renderSlot(GuiGraphics graphics, int x, int y, DeltaTracker tracker, Player player, ItemStack item, int something, CallbackInfo info)
    {
        if(!item.has(ModComponents.QUALITY_COMPONENT)) return;

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(0, 0, -100);
        QualityIconRenderer.renderQualityIconInSlot(poseStack, item, x, y);
        poseStack.popPose();
    }
}
