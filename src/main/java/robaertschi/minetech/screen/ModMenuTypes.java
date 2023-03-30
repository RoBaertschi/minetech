package robaertschi.minetech.screen;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import robaertschi.minetech.Minetech;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Minetech.MODID);

    public static final RegistryObject<MenuType<FuelGeneratorMenu>> FUEL_GENERATOR_MENU =
            registerMenuType(FuelGeneratorMenu::new, "fuel_generator_menu");

    public static final RegistryObject<MenuType<ModularArmorWorkspaceMenu>> MODULAR_ARMOR_WORKSPACE_MENU =
            registerMenuType(ModularArmorWorkspaceMenu::new, "modular_armor_workspace_menu");

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                                  String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
