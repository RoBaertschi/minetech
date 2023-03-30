package robaertschi.minetech.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import robaertschi.minetech.Minetech;
import robaertschi.minetech.api.screen.BasicMenu;
import robaertschi.minetech.api.screen.BasicScreen;

public class FuelGeneratorScreen extends BasicScreen<FuelGeneratorMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Minetech.MODID, "textures/gui/generators/fuel_generator.png");

    public FuelGeneratorScreen(FuelGeneratorMenu menu, Inventory inv, Component name) {
        super(menu, inv, name);
    }

    @Override
    protected void renderBg(@NotNull PoseStack poseStack, float p_97788_, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        int x = (width - imageWidth)/2;
        int y = (height - imageHeight)/2;
        RenderSystem.setShaderTexture(0, TEXTURE);

        blit(poseStack, x, y, 0, 0, imageHeight, imageHeight);

        if (menu.isGenerating()) {
            renderProgressArrow(poseStack, x + 105, y + 33, 176, 0, 8,
                    BasicMenu.getScaledProgress(
                            menu.getData().get(0),
                            menu.getData().get(1),
                            26
                    ));
        }
        energyInfoArea.draw(poseStack);
    }

}
