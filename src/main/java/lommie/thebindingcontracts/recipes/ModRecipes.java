package lommie.thebindingcontracts.recipes;

import lommie.thebindingcontracts.TheBindingContracts;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModRecipes {
    static <T extends Recipe<?>> RecipeType<T> register(String name) {
        return Registry.register(Registries.RECIPE_TYPE, Identifier.of(TheBindingContracts.MOD_ID,name),
                new RecipeType<T>(){
            public String toString() {
                return name;
            }
        });
    }

    public static void register(){}
}
