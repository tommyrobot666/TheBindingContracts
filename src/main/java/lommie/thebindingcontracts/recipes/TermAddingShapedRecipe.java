package lommie.thebindingcontracts.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lommie.thebindingcontracts.contract.TermsAndConditions;
import lommie.thebindingcontracts.items.ModItemComponents;
import lommie.thebindingcontracts.mixin.ShapedRecipeAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;

import java.util.ArrayList;
import java.util.List;

public class TermAddingShapedRecipe extends ShapedRecipe {
    private final TermsAndConditions term;

    public TermAddingShapedRecipe(TermsAndConditions term, String group, CraftingRecipeCategory category, RawShapedRecipe raw, ItemStack result, boolean showNotification) {
        super(group, category, raw, result, showNotification);
        this.term = term;
    }

    @Override
    public RecipeSerializer<? extends ShapedRecipe> getSerializer() {
        return ModRecipeSerializers.TERM_ADDING_SHAPED_RECIPE;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput craftingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        ItemStack stack = super.craft(craftingRecipeInput, wrapperLookup);
        //TODO: figure out why it just creates a new contract instead of finding the old one
        for (ItemStack craftingRecipeInputStack : craftingRecipeInput.getStacks()) {
            if (stack.hasChangedComponent(ModItemComponents.CONTRACT_ID)){
                stack = craftingRecipeInputStack;
                break;
            }
        }

        ArrayList<TermsAndConditions> termsToAddOnNextTick = new ArrayList<>(stack.getOrDefault(ModItemComponents.TERMS_TO_ADD_ON_NEXT_TICK, List.of()));
        termsToAddOnNextTick.add(TermsAndConditions.createNew(term.typeGetId(),term.savedData));
        stack.set(ModItemComponents.TERMS_TO_ADD_ON_NEXT_TICK, termsToAddOnNextTick);
        return stack;
    }

    public static class Serializer implements RecipeSerializer<TermAddingShapedRecipe> {
        public static final MapCodec<TermAddingShapedRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(ShapedRecipe.Serializer.CODEC.codec().fieldOf("").forGetter(TermAddingShapedRecipe::toShapedRecipe),
                            TermsAndConditions.CODEC.fieldOf("term").forGetter(TermAddingShapedRecipe::getTerm))
                    .apply(instance, TermAddingShapedRecipe::fromShaped));
        public static final PacketCodec<RegistryByteBuf, TermAddingShapedRecipe> PACKET_CODEC = PacketCodec.ofStatic(Serializer::encode, Serializer::decode);

        private static TermAddingShapedRecipe decode(RegistryByteBuf buf) {
            return TermAddingShapedRecipe.fromShaped(
                    ShapedRecipe.Serializer.PACKET_CODEC.decode(buf),
                    TermsAndConditions.PACKET_CODEC.decode(buf)
            );
        }

        private static void encode(RegistryByteBuf buf, TermAddingShapedRecipe r) {
            ShapedRecipe.Serializer.PACKET_CODEC.encode(buf, r.toShapedRecipe());
            TermsAndConditions.PACKET_CODEC.encode(buf, r.term);
        }

        @Override
        public MapCodec<TermAddingShapedRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, TermAddingShapedRecipe> packetCodec() {
            return PACKET_CODEC;
        }
    }

    public static TermAddingShapedRecipe fromShaped(ShapedRecipe shapedRecipe, TermsAndConditions term) {
        return new TermAddingShapedRecipe(term, shapedRecipe.getGroup(), shapedRecipe.getCategory(), ((ShapedRecipeAccessor) shapedRecipe).getRaw(),((ShapedRecipeAccessor) shapedRecipe).getResult(), shapedRecipe.showNotification());
    }

    private static TermsAndConditions getTerm(TermAddingShapedRecipe r) {
        return r.term;
    }

    private ShapedRecipe toShapedRecipe() {
        return new ShapedRecipe(getGroup(),getCategory(),((ShapedRecipeAccessor) this).getRaw(),((ShapedRecipeAccessor) this).getResult());
    }
}
