package kamorzy.portablecrates.screen;

import kamorzy.portablecrates.PortableCrates;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class CrateScreen extends AbstractContainerScreen<CrateMenu> {
    private static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(PortableCrates.MOD_ID, "textures/gui/container/crate.png");

    public CrateScreen(CrateMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int x = this.leftPos;
        int y = this.topPos;

        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                TEXTURE,
                x,
                y,
                0,
                0,
                this.imageWidth,
                this.imageHeight,
                256,
                256
        );
    }
}