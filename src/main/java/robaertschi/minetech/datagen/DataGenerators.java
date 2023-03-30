package robaertschi.minetech.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import robaertschi.minetech.Minetech;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Minetech.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent dataEvent) {
        DataGenerator generator = dataEvent.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = dataEvent.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = dataEvent.getLookupProvider();

        var blockTags = new ModBlockTagProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(dataEvent.includeServer(), blockTags);
        generator.addProvider(dataEvent.includeServer(), new ModItemTagProvider(packOutput, lookupProvider, blockTags.contentsGetter(), existingFileHelper));


        generator.addProvider(dataEvent.includeClient(), ModLootTableProvider.create(packOutput));
        generator.addProvider(dataEvent.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(dataEvent.includeClient(), new ModBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(dataEvent.includeServer(), new ModRecipeProvider(packOutput));
    }
}
