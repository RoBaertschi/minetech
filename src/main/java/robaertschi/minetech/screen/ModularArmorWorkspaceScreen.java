package robaertschi.minetech.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import robaertschi.minetech.Minetech;
import robaertschi.minetech.api.screen.BasicMenu;
import robaertschi.minetech.api.screen.BasicScreen;

public class ModularArmorWorkspaceScreen extends BasicScreen<ModularArmorWorkspaceMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Minetech.MODID, "textures/gui/generators/modular_armor_workspace.png");

    public ModularArmorWorkspaceScreen(ModularArmorWorkspaceMenu menu, Inventory inv, Component name) {
        super(menu, inv, name);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float p_97788_, int p_97789_, int p_97790_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        int x = (width - imageWidth)/2;
        int y = (height - imageHeight)/2;
        RenderSystem.setShaderTexture(0, TEXTURE);

        this.blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);

        energyInfoArea.draw(poseStack);
    }
}
