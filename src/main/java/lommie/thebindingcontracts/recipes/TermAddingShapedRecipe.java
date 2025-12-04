package lommie.thebindingcontracts.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lommie.thebindingcontracts.contract.TermsAndConditions;
import lommie.thebindingcontracts.mixin.ShapedRecipeAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;

public class TermAddingShapedRecipe extends ShapedRecipe {
    private final TermsAndConditions term;

    public TermAddingShapedRecipe(TermsAndConditions term, String group, CraftingRecipeCategory category, RawShapedRecipe raw, ItemStack result, boolean showNotification) {
        super(group, category, raw, result, showNotification);
        this.term = term;
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
            return null;
        }
    }

    private static TermAddingShapedRecipe fromShaped(ShapedRecipe shapedRecipe, TermsAndConditions term) {
        return new TermAddingShapedRecipe(term, shapedRecipe.getGroup(), shapedRecipe.getCategory(), ((ShapedRecipeAccessor) shapedRecipe).getRaw(),((ShapedRecipeAccessor) shapedRecipe).getResult(), shapedRecipe.showNotification());
    }

    private static TermsAndConditions getTerm(TermAddingShapedRecipe r) {
        return r.term;
    }

    private ShapedRecipe toShapedRecipe() {
        return new ShapedRecipe(getGroup(),getCategory(),((ShapedRecipeAccessor) this).getRaw(),((ShapedRecipeAccessor) this).getResult());
    }
}
