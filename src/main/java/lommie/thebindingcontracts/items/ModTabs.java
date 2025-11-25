package lommie.thebindingcontracts.items;

import lommie.thebindingcontracts.TheBindingContracts;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModTabs {
    public static ItemGroup register(String name, ItemGroup.Builder builder) {
        return Registry.register(Registries.ITEM_GROUP,
                Identifier.of(TheBindingContracts.MOD_ID,name), builder.build());
    }

    @SuppressWarnings("unused")
    public static final ItemGroup OTHER_ITEMS = register(
            "other_items",
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemGroup."+TheBindingContracts.MOD_ID+".other_items"))
                    .icon(ModItems.WAX_SEAL::getDefaultStack)
                    .entries(((context, entries) -> entries.add(ModItems.WAX_SEAL.getDefaultStack())))
    );

    @SuppressWarnings("unused")
    public static final ItemGroup CONTRACTS = register(
            "contracts",
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemGroup."+TheBindingContracts.MOD_ID+".contracts"))
                    .icon(ModItems.CONTRACT::getDefaultStack)
                    .entries((context, entries) -> {
                        entries.add(ModItems.CONTRACT.getDefaultStack());
                        entries.add(ModItems.TWO_PLAYER_CONTRACT.getDefaultStack());
                    })
    );

    @SuppressWarnings("EmptyMethod")
    public static void register() {}
}
