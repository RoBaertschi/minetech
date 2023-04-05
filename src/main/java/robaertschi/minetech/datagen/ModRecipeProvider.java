package robaertschi.minetech.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import robaertschi.minetech.Minetech;

import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> p_251297_) {

    }

    protected static void oreSmelting(@NotNull Consumer<FinishedRecipe> p_250654_, List<ItemLike> p_250172_, @NotNull RecipeCategory p_250588_, @NotNull ItemLike p_251868_, float p_250789_, int p_252144_, @NotNull String p_251687_) {
        oreCooking(p_250654_, RecipeSerializer.SMELTING_RECIPE, p_250172_, p_250588_, p_251868_, p_250789_, p_252144_, p_251687_, "_from_smelting");
    }

    protected static void oreCooking(@NotNull Consumer<FinishedRecipe> p_250791_, @NotNull RecipeSerializer<? extends AbstractCookingRecipe> p_251817_, List<ItemLike> p_249619_, @NotNull RecipeCategory p_251154_, @NotNull ItemLike p_250066_, float p_251871_, int p_251316_, @NotNull String p_251450_, String p_249236_) {
        for(ItemLike itemlike : p_249619_) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), p_251154_, p_250066_, p_251871_, p_251316_, p_251817_).group(p_251450_)
                    .unlockedBy(getHasName(itemlike), has(itemlike)).save(p_250791_, new ResourceLocation(Minetech.MODID, getItemName(p_250066_)) + p_249236_ + "_" + getItemName(itemlike));
        }
    }

    protected static void nineBlockStorageRecipes(@NotNull Consumer<FinishedRecipe> p_249580_, @NotNull RecipeCategory p_251203_, ItemLike p_251689_, @NotNull RecipeCategory p_251376_, ItemLike p_248771_) {
        nineBlockStorageRecipes(p_249580_, p_251203_, p_251689_, p_251376_, p_248771_, getSimpleRecipeName(p_248771_), null, getSimpleRecipeName(p_251689_), null);
    }

    protected static void nineBlockStorageRecipes(@NotNull Consumer<FinishedRecipe> p_250423_, @NotNull RecipeCategory p_250083_, ItemLike p_250042_,
                                                  @NotNull RecipeCategory p_248977_, ItemLike p_251911_, String p_250475_, @Nullable String p_248641_,
                                                  String p_252237_, @Nullable String p_250414_) {
        ShapelessRecipeBuilder.shapeless(p_250083_, p_250042_, 9).requires(p_251911_).group(p_250414_).unlockedBy(getHasName(p_251911_), has(p_251911_))
                .save(p_250423_, new ResourceLocation(Minetech.MODID, p_252237_));
        ShapedRecipeBuilder.shaped(p_248977_, p_251911_).define('#', p_250042_).pattern("###").pattern("###").pattern("###").group(p_248641_)
                .unlockedBy(getHasName(p_250042_), has(p_250042_)).save(p_250423_, new ResourceLocation(Minetech.MODID, p_250475_));
    }
}
