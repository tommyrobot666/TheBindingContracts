package lommie.thebindingcontracts.recipes;

import lommie.thebindingcontracts.TheBindingContracts;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipeTypes {
    @SuppressWarnings("unused")
    public static <T extends Recipe<?>> RecipeType<T> register(String name){
        RecipeType<T> type = new RecipeType<>() {
            @Override
            public String toString() {
                return name;
            }
        };
        return Registry.register(Registries.RECIPE_TYPE, Identifier.of(TheBindingContracts.MOD_ID, name), type);
    }

    public static void register(){}
}
