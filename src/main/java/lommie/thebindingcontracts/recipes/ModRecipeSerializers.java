package lommie.thebindingcontracts.recipes;

import lommie.thebindingcontracts.TheBindingContracts;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipeSerializers {
    public static <T extends Recipe<?>> RecipeSerializer<T> register(String name, RecipeSerializer<T> serializer){
        return Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(TheBindingContracts.MOD_ID, name), serializer);
    }

    public static final RecipeSerializer<TermAddingShapedRecipe> TERM_ADDING_SHAPED_RECIPE = register("term_adding_shaped_recipe", new TermAddingShapedRecipe.Serializer());

    @SuppressWarnings("EmptyMethod")
    public static void register(){}
}
