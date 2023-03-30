package robaertschi.minetech.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import robaertschi.minetech.Minetech;
import robaertschi.minetech.block.custom.FuelGeneratorBlock;
import robaertschi.minetech.block.custom.ModularArmorWorkspaceBlock;
import robaertschi.minetech.item.ModItems;

import java.util.function.Supplier;


public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Minetech.MODID);

    public static final RegistryObject<Block> FUEL_GENERATOR =
            registerBlock("fuel_generator",
                    () -> new FuelGeneratorBlock(BlockBehaviour.Properties.of(Material.METAL)
                            ));

    public static final RegistryObject<Block> MODULAR_ARMOR_WORKSPACE =
            registerBlock("modular_armor_workspace",
                    () -> new ModularArmorWorkspaceBlock(BlockBehaviour.Properties.of(Material.METAL)
                    ));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
