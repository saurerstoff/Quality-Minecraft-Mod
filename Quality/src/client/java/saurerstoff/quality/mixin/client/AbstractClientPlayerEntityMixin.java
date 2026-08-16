package saurerstoff.quality.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import saurerstoff.quality.QualityData;
import saurerstoff.quality.QualityUtils;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerEntityMixin extends Player {

    public AbstractClientPlayerEntityMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
        super(level, blockPos, f, gameProfile);
    }

    @ModifyReturnValue(method = "getFieldOfViewModifier", at = @At("RETURN"))
    public float fovMultiplier(float original) {

        if (Minecraft.getInstance().options.getCameraType().isFirstPerson() && isScoping()) {
            float originalZoom = 0.1f;
            int quality = QualityUtils.getResultQuality(getUseItem());
            return originalZoom * (1 / QualityData.SPYGLASS_ZOOM_BONUS[quality]);
        }
        return original;
    }

}
