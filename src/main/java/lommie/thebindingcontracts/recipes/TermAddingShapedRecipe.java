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
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.recipe.display.ShapedCraftingRecipeDisplay;
import net.minecraft.recipe.display.SlotDisplay;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TermAddingShapedRecipe extends ShapedRecipe {
    private final List<TermsAndConditions> terms;
    private CraftingRecipeInput lastCraftingRecipeInput; //just for display

    public TermAddingShapedRecipe(List<TermsAndConditions> terms, String group, CraftingRecipeCategory category, RawShapedRecipe raw, ItemStack result, boolean showNotification) {
        super(group, category, raw, result, showNotification);
        this.terms = terms;
    }

    @Override
    public RecipeSerializer<? extends ShapedRecipe> getSerializer() {
        return ModRecipeSerializers.TERM_ADDING_SHAPED_RECIPE;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput craftingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        ItemStack stack = super.craft(craftingRecipeInput, wrapperLookup);
        stack = getContractItemInGrid(craftingRecipeInput, stack);

        ArrayList<TermsAndConditions> termsToAddOnNextTick = new ArrayList<>(stack.getOrDefault(ModItemComponents.TERMS_TO_ADD_ON_NEXT_TICK, List.of()));
        terms.forEach((term) -> termsToAddOnNextTick.add(TermsAndConditions.createNew(term.typeGetId(),term.savedData)));
        stack.set(ModItemComponents.TERMS_TO_ADD_ON_NEXT_TICK, termsToAddOnNextTick);
        return stack;
    }

    private static ItemStack getContractItemInGrid(CraftingRecipeInput craftingRecipeInput, ItemStack stack) {
        if (craftingRecipeInput == null) return stack;
        for (ItemStack craftingRecipeInputStack : craftingRecipeInput.getStacks()) {
            if (craftingRecipeInputStack.hasChangedComponent(ModItemComponents.CONTRACT_ID)){
                stack = craftingRecipeInputStack.copy();
                break;
            }
        }
        return stack;
    }

    @Override
    public List<RecipeDisplay> getDisplays() {
        ShapedCraftingRecipeDisplay display = (ShapedCraftingRecipeDisplay) super.getDisplays().getFirst();
        ItemStack item = getContractItemInGrid(lastCraftingRecipeInput,((ShapedRecipeAccessor) this).getResult());
        return List.of(new ShapedCraftingRecipeDisplay(
                display.height(),display.width(),
                display.ingredients(),new SlotDisplay.StackSlotDisplay(item),
                display.craftingStation()));
    }

    @Override
    public boolean matches(CraftingRecipeInput craftingRecipeInput, World world) {
        this.lastCraftingRecipeInput = craftingRecipeInput;
        return super.matches(craftingRecipeInput, world);
    }

    public static class Serializer implements RecipeSerializer<TermAddingShapedRecipe> {
        public static final MapCodec<TermAddingShapedRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) ->
            instance.group(ShapedRecipe.Serializer.CODEC.codec().fieldOf("").forGetter(TermAddingShapedRecipe::toShapedRecipe),
                            TermsAndConditions.CODEC.listOf().fieldOf("terms").forGetter(TermAddingShapedRecipe::getTerms))
                    .apply(instance, TermAddingShapedRecipe::fromShaped));
        public static final PacketCodec<RegistryByteBuf, TermAddingShapedRecipe> PACKET_CODEC = PacketCodec.ofStatic(Serializer::encode, Serializer::decode);

        private static TermAddingShapedRecipe decode(RegistryByteBuf buf) {
            return TermAddingShapedRecipe.fromShaped(
                    ShapedRecipe.Serializer.PACKET_CODEC.decode(buf),
                    buf.readList((b) -> TermsAndConditions.PACKET_CODEC.decode((RegistryByteBuf) b))
            );
        }

        private static void encode(RegistryByteBuf buf, TermAddingShapedRecipe r) {
            ShapedRecipe.Serializer.PACKET_CODEC.encode(buf, r.toShapedRecipe());
            buf.writeCollection(r.terms, (b,term) -> TermsAndConditions.PACKET_CODEC.encode(buf, term));
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

    public static TermAddingShapedRecipe fromShaped(ShapedRecipe shapedRecipe, List<TermsAndConditions> term) {
        return new TermAddingShapedRecipe(term, shapedRecipe.getGroup(), shapedRecipe.getCategory(), ((ShapedRecipeAccessor) shapedRecipe).getRaw(),((ShapedRecipeAccessor) shapedRecipe).getResult(), shapedRecipe.showNotification());
    }

    private static List<TermsAndConditions> getTerms(TermAddingShapedRecipe r) {
        return r.terms;
    }

    private ShapedRecipe toShapedRecipe() {
        return new ShapedRecipe(getGroup(),getCategory(),((ShapedRecipeAccessor) this).getRaw(),((ShapedRecipeAccessor) this).getResult());
    }
}
