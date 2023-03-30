package robaertschi.minetech.blockentity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import robaertschi.minetech.Minetech;
import robaertschi.minetech.block.ModBlocks;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Minetech.MODID);

    public static final RegistryObject<BlockEntityType<FuelGeneratorBlockEntity>> FUEL_GENERATOR =
            BLOCK_ENTITIES.register("fuel_generator", () ->
                    BlockEntityType.Builder.of(FuelGeneratorBlockEntity::new,
                            ModBlocks.FUEL_GENERATOR.get()).build(null));

    public static final RegistryObject<BlockEntityType<ModularArmorWorkspaceBlockEntity>> MODULAR_ARMOR_WORKSPACE =
            BLOCK_ENTITIES.register("modular_armor_workspace", () ->
                    BlockEntityType.Builder.of(ModularArmorWorkspaceBlockEntity::new,
                            ModBlocks.MODULAR_ARMOR_WORKSPACE.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
