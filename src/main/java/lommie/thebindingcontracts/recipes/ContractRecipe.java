package lommie.thebindingcontracts.recipes;

import com.mojang.serialization.MapCodec;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class ContractRecipe implements CraftingRecipe {
    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        return false;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        return null;
    }

    @Override
    public RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return null;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return null;
    }

    @Override
    public CraftingRecipeCategory getCategory() {
        return null;
    }

    public static class Serializer implements RecipeSerializer<ContractRecipe>{
        public Serializer(){}

        @Override
        public MapCodec<ContractRecipe> codec() {
            return null;
        }

        @Override
        public PacketCodec<RegistryByteBuf, ContractRecipe> packetCodec() {
            return null;
        }
    }
}
