package saurerstoff.quality.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import saurerstoff.quality.ModComponents;
import saurerstoff.quality.QualityUtils;
import saurerstoff.quality.criterions.QualityCriterions;

@Mixin(FurnaceResultSlot.class)
public abstract class FurnaceResultSlotMixin extends Slot{

    public FurnaceResultSlotMixin(Container container, int i, int j, int k) {
        super(container, i, j, k);
    }

    @Shadow @Final private Player player;
    
    @Inject(method = "checkTakeAchievements", at = @At("HEAD"))
    private void triggerQualityCraftCriterion(ItemStack stack, CallbackInfo ci) {
        if (player instanceof ServerPlayer serverPlayer && !stack.isEmpty()) {
            int quality = QualityUtils.getResultQuality(stack);
            int furnaceQuality = 0;
            if (this.container instanceof AbstractFurnaceBlockEntity abstractFurnaceBlockEntity && abstractFurnaceBlockEntity.components().has(ModComponents.QUALITY_COMPONENT)){
                furnaceQuality = abstractFurnaceBlockEntity.components().get(ModComponents.QUALITY_COMPONENT);
            }
            QualityCriterions.SMELT_QUALITY.trigger(serverPlayer, quality, stack, furnaceQuality);
        }
    }
}
