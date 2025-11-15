package lommie.thebindingcontracts.recipes;

import lommie.thebindingcontracts.TheBindingContracts;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipeSerializers {
    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(TheBindingContracts.MOD_ID,id)
                ,serializer);
    }

    public static RecipeSerializer<ContractRecipe> CONTRACT = register(
            "contract",
            new ContractRecipe.Serializer()
    );

    public static void register(){}
}
