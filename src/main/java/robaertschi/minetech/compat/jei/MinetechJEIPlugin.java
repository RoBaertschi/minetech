package robaertschi.minetech.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import robaertschi.minetech.Minetech;

@JeiPlugin
@SuppressWarnings("unused")
public class MinetechJEIPlugin implements IModPlugin {



    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(Minetech.MODID, "jei_plugin");
    }
}
