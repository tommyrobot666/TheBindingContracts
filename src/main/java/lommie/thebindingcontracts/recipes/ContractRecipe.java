package lommie.thebindingcontracts.recipes;

import com.mojang.serialization.MapCodec;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;

public class ContractRecipe extends ShapedRecipe {
    public ContractRecipe(String group, CraftingRecipeCategory category, RawShapedRecipe raw, ItemStack result, boolean showNotification) {
        super(group, category, raw, result, showNotification);
    }

    @Override
    public ItemStack craft(CraftingRecipeInput craftingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        return super.craft(craftingRecipeInput, wrapperLookup);
    }

    public static class Serializer implements RecipeSerializer<ContractRecipe>{
        public Serializer() {}

        @Override
        public MapCodec<ContractRecipe> codec() {
            return ((MapCodec<ContractRecipe>) (Object) ShapedRecipe.Serializer.CODEC);
        }

        @Override
        public PacketCodec<RegistryByteBuf, ContractRecipe> packetCodec() {
            return ((PacketCodec<RegistryByteBuf, ContractRecipe>) (Object) ShapedRecipe.Serializer.PACKET_CODEC);
        }
    }
}
