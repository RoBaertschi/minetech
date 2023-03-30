package robaertschi.minetech.api.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;
import robaertschi.minetech.api.screen.renderer.EnergyInfoArea;
import robaertschi.minetech.api.screen.renderer.FluidTankRenderer;
import robaertschi.minetech.api.util.IEnergyContainer;
import robaertschi.minetech.api.util.IFluidContainer;
import robaertschi.minetech.api.util.MouseUtil;

import java.util.Optional;

/*
 * Most code:
 * CREDIT:  https://github.com/Tutorials-By-Kaupenjoe/Forge-Tutorial-1.19 by Kaupenjoe
 * Under MIT-License: https://github.com/Tutorials-By-Kaupenjoe/Forge-Tutorial-1.19/blob/main/LICENSE
 */

/**
 * An Basic Screen. It adds an Energy and Fluid Info area if needed.
 * @author Kaupenjoe
 * @author RoBaertschi
 * @since 0.1.0-1.19.4
 * @version 1
 * @param <M> The Menu.
 */

public abstract class BasicScreen<M extends BasicMenu<?>> extends AbstractContainerScreen<M> {
    protected EnergyInfoArea energyInfoArea;
    protected FluidTankRenderer renderer;

    public BasicScreen(M menu, Inventory inv, Component name) {
        super(menu, inv, name);
    }

    @Override
    protected void init() {
        super.init();
        if (menu.blockEntity instanceof IEnergyContainer container)
            assignEnergyInfoArea(container);
        if (menu.blockEntity instanceof IFluidContainer container)
            assignFluidRenderer(container);
    }

    private void assignFluidRenderer(IFluidContainer container) {
        // For Intellij. Should not be called or else, there would be a bug in an easy condition.
        renderer = new FluidTankRenderer(container.getFluidTank().getCapacity(), true, 16, 61);
    }

    private void assignEnergyInfoArea(IEnergyContainer container) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        energyInfoArea = new EnergyInfoArea(x + 156, y + 13, container.getEnergyStorage());
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        if (menu.blockEntity instanceof IEnergyContainer)
            renderEnergyAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);
        if (menu.blockEntity instanceof IFluidContainer)
            renderFluidAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);
    }

    protected void renderFluidAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 55, 15)) {
            renderTooltip(pPoseStack, renderer.getTooltip(menu.getFluidStack(), TooltipFlag.Default.NORMAL),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    protected void renderEnergyAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 156, 13, 8, 64)) {
            renderTooltip(pPoseStack, energyInfoArea.getTooltips(),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    /**
     * Better Documented {@link AbstractContainerScreen#blit(PoseStack, int, int, int, int, int, int)} for Progress stuff. Use {@link BasicMenu#getScaledProgress(int, int, int)} for progress stuff.
     * @param pPoseStack PoseStack
     * @param x X Position on the Screen.
     * @param y Y Position on the Screen.
     * @param textureX The X start position on the Texture.
     * @param textureY The Y start position on the Texture.
     * @param width The width of the Arrow
     * @param height The height of the Arrow
     */
    protected void renderProgressArrow(PoseStack pPoseStack, int x, int y, int textureX, int textureY, int width, int height) {
        blit(pPoseStack, x, y, textureX, textureY, width, height);
    }

    @Override
    public void render(PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }
}
