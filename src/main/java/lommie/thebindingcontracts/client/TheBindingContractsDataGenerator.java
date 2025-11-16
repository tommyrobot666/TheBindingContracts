package lommie.thebindingcontracts.client;

import lommie.thebindingcontracts.items.ModItemComponents;
import lommie.thebindingcontracts.items.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.item.Item;

public class TheBindingContractsDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModelGenerator::new);
    }

    public static class ModelGenerator extends FabricModelProvider{

        public ModelGenerator(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator m) {

        }

        @Override
        public void generateItemModels(ItemModelGenerator m) {
            registerContractModel(ModItems.LIFE_LINK_CONTRACT,m);
        }

        public final void registerContractModel(Item item, ItemModelGenerator m){
            ItemModel.Unbaked unsigned = ItemModels.basic(ModelIds.getItemModelId(item));
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
}
