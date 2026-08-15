package saurerstoff.quality.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import saurerstoff.quality.QualityUtils;
import saurerstoff.quality.criterions.QualityCriterions;

@Mixin(ResultSlot.class)
public class ResultSlotMixin {

    @Shadow @Final private Player player;
    
    @Inject(method = "checkTakeAchievements", at = @At("HEAD"))
    private void triggerQualityCraftCriterion(ItemStack stack, CallbackInfo ci) {
        if (player instanceof ServerPlayer serverPlayer && !stack.isEmpty()) {
            int quality = QualityUtils.getResultQuality(stack);
            QualityCriterions.CRAFT_QUALITY.trigger(serverPlayer, quality, stack);
        }
    }
}
