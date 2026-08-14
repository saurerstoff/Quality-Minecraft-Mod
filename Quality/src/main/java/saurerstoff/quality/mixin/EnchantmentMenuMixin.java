package saurerstoff.quality.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import saurerstoff.quality.QualityUtils;
import saurerstoff.quality.enchantment.EnchantmentState;


@Mixin(EnchantmentMenu.class)
public class EnchantmentMenuMixin {
    @Shadow @Final private Container enchantSlots;
    
    private void updateEnchantingQuality(){
        ItemStack lapis = this.enchantSlots.getItem(1);
        if (lapis.is(Items.LAPIS_LAZULI)){
            int quality = QualityUtils.getResultQuality(lapis);
            EnchantmentState.CURRENT_QUALITY.set(quality);
        }
    }

    private void clearEnchantingQuality(){
        EnchantmentState.CURRENT_QUALITY.set(0);
    }

    @Inject(method = "slotsChanged", at = @At("HEAD"))
    private void slotsChangedStart(Container inventory, CallbackInfo ci) {
        updateEnchantingQuality();
    }

    @Inject(method = "slotsChanged", at = @At("RETURN"))
    private void slotsChangedEnd(Container inventory, CallbackInfo ci) {
        clearEnchantingQuality();
    }

    // Triggered when the player actually clicks one of the 3 enchantment buttons
    @Inject(method = "clickMenuButton", at = @At("HEAD"))
    private void clickMenuButtonStart(Player player, int id, CallbackInfoReturnable<Boolean> cir) {
        updateEnchantingQuality();
    }

    @Inject(method = "clickMenuButton", at = @At("RETURN"))
    private void clickMenuButtonEnd(Player player, int id, CallbackInfoReturnable<Boolean> cir) {
        clearEnchantingQuality();
    }

}
