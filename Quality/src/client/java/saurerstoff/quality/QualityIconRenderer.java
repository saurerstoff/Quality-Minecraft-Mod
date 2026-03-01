package saurerstoff.quality;

import org.joml.Matrix4f;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class QualityIconRenderer {

    private static final ResourceLocation UNCOMMON_QUALITY_ICON = 
        ResourceLocation.fromNamespaceAndPath(Quality.MOD_ID, "textures/gui/uncommon.png");
    private static final ResourceLocation RARE_QUALITY_ICON = 
        ResourceLocation.fromNamespaceAndPath(Quality.MOD_ID, "textures/gui/rare.png");
    private static final ResourceLocation EPIC_QUALITY_ICON = 
        ResourceLocation.fromNamespaceAndPath(Quality.MOD_ID, "textures/gui/epic.png");
    private static final ResourceLocation LEGENDARY_QUALITY_ICON = 
        ResourceLocation.fromNamespaceAndPath(Quality.MOD_ID, "textures/gui/legendary.png");

    public static void renderQualityIconInSlot(PoseStack poseStack, Slot slot)
	{
        ItemStack item = slot.getItem();
        if (item.isEmpty()) return;
		render(poseStack, item.get(ModComponents.QUALITY_COMPONENT), slot.x + 12, slot.y, 4, 4);
	}

    public static void renderQualityIconInSlot(PoseStack poseStack, ItemStack item, int x, int y)
	{
        if (item.isEmpty()) return;
		render(poseStack, item.get(ModComponents.QUALITY_COMPONENT), x + 12, y, 4 , 4);
	}

    public static void renderQualityIcon(PoseStack poseStack, int quality, int x, int y, int width, int height){
        render(poseStack, quality, x, y, width, height);
    }

    private static void render(PoseStack poseStack, int quality, int x, int y, int width, int height)
	{
        RenderSystem.disableDepthTest();

        poseStack.pushPose();

        switch (quality) {
            case 0:
                return;
            case 1:
                RenderSystem.setShaderTexture(0, UNCOMMON_QUALITY_ICON);
                break;
            case 2:
                RenderSystem.setShaderTexture(0, RARE_QUALITY_ICON);
                break;
            case 3:
                RenderSystem.setShaderTexture(0, EPIC_QUALITY_ICON);
                break;
            case 4:
                RenderSystem.setShaderTexture(0, LEGENDARY_QUALITY_ICON);
                break;
            default:
                return;
        }
        
        blit(poseStack, x, y, width, height, 0, 0, 8, 8, 8, 8);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        poseStack.popPose();
    }

    private static void blit(PoseStack poseStack, int x, int y, int width, int height, float texX, float texY, int texWidth, int texHeight, int fullWidth, int fullHeight) {
		blit(poseStack, x, x + width, y, y + height, 0, texWidth, texHeight, texX, texY, fullWidth, fullHeight);
	}

    private static void blit(PoseStack poseStack, int x0, int x1, int y0, int y1, int z, int texWidth, int texHeight, float texX, float texY, int fullWidth, int fullHeight) {
		innerBlit(poseStack, x0, x1, y0, y1, z, (texX + 0.0F) / (float)fullWidth, (texX + (float)texWidth) / (float)fullWidth, (texY + 0.0F) / (float)fullHeight, (texY + (float)texHeight) / (float)fullHeight);
	}

    private static void innerBlit(PoseStack poseStack, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1)
	{
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
		Matrix4f matrix4f = poseStack.last().pose();
		BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferBuilder.addVertex(matrix4f, (float)x0, (float)y0, (float)z).setUv(u0, v0);
		bufferBuilder.addVertex(matrix4f, (float)x0, (float)y1, (float)z).setUv(u0, v1);
		bufferBuilder.addVertex(matrix4f, (float)x1, (float)y1, (float)z).setUv(u1, v1);
		bufferBuilder.addVertex(matrix4f, (float)x1, (float)y0, (float)z).setUv(u1, v0);
		BufferUploader.drawWithShader(bufferBuilder.build());
	}
}
