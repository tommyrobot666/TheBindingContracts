package lommie.thebindingcontracts.client;

import lommie.thebindingcontracts.items.ModItemComponents;
import lommie.thebindingcontracts.items.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.client.data.*;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class TheBindingContractsDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModelGenerator::new);
        pack.addProvider(EnUsLanguageGenerator::new);
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
            registerContractModel(ModItems.LIFE_LINK_CONTRACT,m);
            registerContractModel(ModItems.TELEPORT_CONTRACT,m);
            registerContractModel(ModItems.TRADE_CONTRACT,m);
            registerContractModel(ModItems.LAND_CONTRACT,m);
            m.register(ModItems.WAX_SEAL);
        }

        public final void registerContractModel(Item item, ItemModelGenerator m){
            ItemModel.Unbaked unsigned = ItemModels.basic(m.registerSubModel(item,"", ModModels.CONTRACT));
            ItemModel.Unbaked one_signature = ItemModels.basic(m.registerSubModel(item,"_one_signature", ModModels.CONTRACT));
            ItemModel.Unbaked complete = ItemModels.basic(m.registerSubModel(item,"_complete",ModModels.CONTRACT));
            ItemModel.Unbaked broken = ItemModels.basic(m.registerSubModel(item,"_broken",ModModels.CONTRACT));
            m.output.accept(item, ItemModels.condition(ItemModels.hasComponentProperty(ModItemComponents.BROKEN),
                    broken,
                    ItemModels.condition(ItemModels.hasComponentProperty(ModItemComponents.CONTRACT_SIGNATURE),
                            ItemModels.condition(ItemModels.hasComponentProperty(ModItemComponents.OTHER_CONTRACT_SIGNATURE),
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
            b.add(ModItems.LIFE_LINK_CONTRACT, "Life Link Contract");
            b.add(ModItems.TELEPORT_CONTRACT, "Teleport Contract");
            b.add(ModItems.TRADE_CONTRACT, "Trade Contract");
            b.add(ModItems.LAND_CONTRACT, "Land Contract");
            b.add(ModItems.WAX_SEAL, "Wax seal");
        }
    }
}
