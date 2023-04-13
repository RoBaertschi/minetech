package robaertschi.minetech.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import robaertschi.minetech.Minetech;
import robaertschi.minetech.block.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Minetech.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        horizontalBlock(ModBlocks.MODULAR_ARMOR_WORKSPACE.get(),
                new ResourceLocation(Minetech.MODID, "block/modular_armor_workspace_side"),
                new ResourceLocation(Minetech.MODID, "block/modular_armor_workspace_side"),
                new ResourceLocation(Minetech.MODID, "block/modular_armor_workspace_top"));
    }

    @SuppressWarnings("unused")
    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
