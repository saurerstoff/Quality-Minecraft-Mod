package saurerstoff.quality;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import saurerstoff.quality.blocks.RecyclerMenu;

public class RecyclerScreen extends AbstractContainerScreen<RecyclerMenu> {
    private static final ResourceLocation CONTAINER_TEXTURE = ResourceLocation.fromNamespaceAndPath(Quality.MOD_ID,
            "textures/gui/container/recycler.png");
    private static final ResourceLocation PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");
    private static final int PROGRESS_SPRITE_WIDTH = 24;
    private static final int PROGRESS_SPRITE_HEIGHT = 16;

    public RecyclerScreen(RecyclerMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        this.imageWidth = 176;
        this.imageHeight = 174;
        this.inventoryLabelY = 80;
    }

    @Override
    protected void init() {
        super.init();

        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        graphics.blit(CONTAINER_TEXTURE, xo, yo, 0, 0, this.imageWidth, this.imageHeight);

        int calculatedWidth = Mth.ceil(this.menu.getProgress() * PROGRESS_SPRITE_WIDTH);
        //progress sprite
        graphics.blitSprite(
            PROGRESS_SPRITE, 
            PROGRESS_SPRITE_WIDTH, PROGRESS_SPRITE_HEIGHT, 
            0, 0,
            leftPos + 36, topPos + 17,
            calculatedWidth, PROGRESS_SPRITE_HEIGHT
        );
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        // Render item tooltips
        this.renderTooltip(graphics, mouseX, mouseY);
    }
}
