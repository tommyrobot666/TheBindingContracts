package lommie.thebindingcontracts.client;

import lommie.thebindingcontracts.TheBindingContracts;
import lommie.thebindingcontracts.contract.ModTerms;
import lommie.thebindingcontracts.contract.terms.LifeLinkTerm;
import lommie.thebindingcontracts.items.ModItemComponents;
import lommie.thebindingcontracts.items.ModItems;
import lommie.thebindingcontracts.recipes.TermAddingShapedRecipeJsonBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.client.data.*;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class TheBindingContractsDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModelGenerator::new);
        pack.addProvider(EnUsLanguageGenerator::new);
        pack.addProvider(RecipeGenerator::new);
    }

    private static class ModelGenerator extends FabricModelProvider{

        public ModelGenerator(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator m) {

        }

        @Override
        public void generateItemModels(ItemModelGenerator m) {
            registerContractModel(ModItems.CONTRACT,m);
            registerContractModel(ModItems.TWO_PLAYER_CONTRACT,m);
            m.register(ModItems.WAX_SEAL, Models.GENERATED);
            m.register(ModItems.LEGAL_STUFF, Models.GENERATED);
        }

        /***/
        public final void registerContractModel(Item item, ItemModelGenerator m){
            ItemModel.Unbaked unsigned = ItemModels.basic(m.registerSubModel(item,"", ModModels.CONTRACT));
            ItemModel.Unbaked one_signature = ItemModels.basic(m.registerSubModel(item,"_valid", ModModels.CONTRACT));
            ItemModel.Unbaked complete = ItemModels.basic(m.registerSubModel(item,"_complete",ModModels.CONTRACT));
            ItemModel.Unbaked broken = ItemModels.basic(m.registerSubModel(item,"_broken",ModModels.CONTRACT));
            m.output.accept(item, ItemModels.condition(ItemModels.hasComponentProperty(ModItemComponents.BROKEN),
                    broken,
                    ItemModels.condition(ItemModels.hasComponentProperty(ModItemComponents.VALID),
                            ItemModels.condition(ItemModels.hasComponentProperty(ModItemComponents.SIGNED),
                                    complete,
                                    one_signature),
                            unsigned)));
        }
    }

    private static class EnUsLanguageGenerator extends FabricLanguageProvider{

        protected EnUsLanguageGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(dataOutput, "en_us", registryLookup);
        }

        @Override
        public void generateTranslations(RegistryWrapper.WrapperLookup registryAccess, TranslationBuilder b) {
            b.add(ModItems.CONTRACT, "Contract");
            b.add(ModItems.TWO_PLAYER_CONTRACT, "Contract (between only 2 players)");
            b.add(ModItems.WAX_SEAL, "Wax seal");
            b.add("itemGroup."+ TheBindingContracts.MOD_ID+".other_items","The Binding Uncontracts");
            b.add("itemGroup."+ TheBindingContracts.MOD_ID+".contracts","The Binding Contracts");
            b.add(ModItems.LEGAL_STUFF, "Legal Stuff");
            b.add(ModTerms.LIFE_LINK.toTranslationKey("term"),"Life boost");
            b.add(ModTerms.TELEPORT.toTranslationKey("term"),"Teleport");
            b.add(ModTerms.TRADE.toTranslationKey("term"),"Send item");
            b.add(ModTerms.TELEPORT.toTranslationKey("term","uses_left"),"Uses left:");
            b.add(ModItems.CONTRACT.getTranslationKey()+".broken","BROKEN");
            b.add(ModItems.CONTRACT.getTranslationKey()+".terms","Terms:");
            b.add(ModItems.CONTRACT.getTranslationKey()+".no_signatures","No signatures");
            b.add(ModItems.CONTRACT.getTranslationKey()+".signed_by","Signed by");
            b.add(ModItems.CONTRACT.getTranslationKey()+".no_terms","No terms");

        }
    }

    private static class RecipeGenerator extends FabricRecipeProvider{

        public RecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected net.minecraft.data.recipe.RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup lookup, RecipeExporter exporter) {
            return new net.minecraft.data.recipe.RecipeGenerator(lookup, exporter) {
                @Override
                public void generate() {
                    RegistryEntryLookup<Item> itemLookup = registries.getOrThrow(RegistryKeys.ITEM);

                    TermAddingShapedRecipeJsonBuilder.create(
                            new LifeLinkTerm(),
                            itemLookup,
                            RecipeCategory.MISC,
                            ModItems.CONTRACT)
                            .criterion(hasItem(ModItems.CONTRACT),conditionsFromItem(ModItems.CONTRACT))
                            .pattern("*!*")
                            .input('*', Items.NETHER_STAR)
                            .input('!', TagKey.of(RegistryKeys.ITEM,Identifier.of(TheBindingContracts.MOD_ID,"any_contract")))
                            .offerTo(exporter);

                    ShapedRecipeJsonBuilder.create(
                            itemLookup,
                            RecipeCategory.MISC,
                            ModItems.CONTRACT)
                            .pattern("## ")
                            .pattern(" # ")
                            .pattern(" ##")
                            .input('#',ModItems.LEGAL_STUFF)
                            .criterion(hasItem(ModItems.LEGAL_STUFF),conditionsFromItem(ModItems.LEGAL_STUFF))
                            .offerTo(exporter);

                    ShapedRecipeJsonBuilder.create(
                                    itemLookup,
                                    RecipeCategory.MISC,
                                    ModItems.TWO_PLAYER_CONTRACT)
                            .pattern(" ##")
                            .pattern(" # ")
                            .pattern("## ")
                            .input('#',ModItems.LEGAL_STUFF)
                            .criterion(hasItem(ModItems.LEGAL_STUFF),conditionsFromItem(ModItems.LEGAL_STUFF))
                            .offerTo(exporter);
                }
            };
        }

        @Override
        public String getName() {
            return TheBindingContracts.MOD_ID;
        }
    }
}
