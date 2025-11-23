package lommie.thebindingcontracts.items;

import lommie.thebindingcontracts.TheBindingContracts;
import lommie.thebindingcontracts.contract.terms.LifeLinkTerm;
import lommie.thebindingcontracts.contract.terms.TeleportTerm;
import lommie.thebindingcontracts.contract.terms.TradeTerm;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModItems {
    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TheBindingContracts.MOD_ID, name));
        Item item = itemFactory.apply(settings.registryKey(itemKey));
        return Registry.register(Registries.ITEM, itemKey, item);
    }

    public static final Item WAX_SEAL = register("wax_seal",
            Item::new, new Item.Settings());

    public static final Item CONTRACT = register("contract",
            ContractItem::new, new Item.Settings());

    public static final Item TWO_PLAYER_CONTRACT = register("two_player_contract",
            TwoPlayerContractItem::new, new Item.Settings());

    public static void register(){}
}
