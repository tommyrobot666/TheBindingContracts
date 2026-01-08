package lommie.thebindingcontracts.client;

import lommie.thebindingcontracts.items.ModItemComponents;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.ItemModels;
import net.minecraft.client.data.Model;
import net.minecraft.client.data.TextureKey;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**This class is used to help with generating the mod's models*/
public class ModModels {
    public static final Model CONTRACT = new Model(
            Optional.of(Identifier.ofVanilla("item/generated")),
            Optional.empty(), TextureKey.LAYER0);

    /**Generates a model for a {@link lommie.thebindingcontracts.items.ContractItem}
     * <p>Suffixes for texture files:</p>
     * <p>none -> Contract is not ready to be signed</p>
     * <p>"_valid" -> All conditions for being signed are satisfied</p>
     * <p>"_complete" -> Contract is signed and the terms are currently affecting players</p>
     * <p>"_broken" -> An action has caused the contract to be broken</p>*/
    public static void registerContractModel(Item item, ItemModelGenerator m){
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
