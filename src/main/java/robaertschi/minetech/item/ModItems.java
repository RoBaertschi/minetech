package robaertschi.minetech.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import robaertschi.minetech.Minetech;
import robaertschi.minetech.item.custom.ModArmorMaterials;
import robaertschi.minetech.item.custom.ModularArmorItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Minetech.MODID);

    public static final RegistryObject<Item> MODULAR_ARMOR_HELMET =
            ITEMS.register("modular_armor_helmet", () ->
                    new ModularArmorItem(ModArmorMaterials.MODULAR_ARMOR, ArmorItem.Type.HELMET));
    public static final RegistryObject<Item> MODULAR_ARMOR_CHESTPLATE =
            ITEMS.register("modular_armor_chestplate", () ->
                    new ModularArmorItem(ModArmorMaterials.MODULAR_ARMOR, ArmorItem.Type.CHESTPLATE));

    public static final RegistryObject<Item> MODULAR_ARMOR_LEGGINGS =
            ITEMS.register("modular_armor_leggings", () ->
                    new ModularArmorItem(ModArmorMaterials.MODULAR_ARMOR, ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<Item> MODULAR_ARMOR_BOOTS =
            ITEMS.register("modular_armor_boots", () ->
                    new ModularArmorItem(ModArmorMaterials.MODULAR_ARMOR, ArmorItem.Type.BOOTS));
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
