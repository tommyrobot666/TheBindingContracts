package lommie.thebindingcontracts;

import lommie.thebindingcontracts.items.ModItemComponents;
import lommie.thebindingcontracts.items.ModItems;
import net.fabricmc.api.ModInitializer;

public class TheBindingContracts implements ModInitializer {
    // https://misode.github.io/recipe/
    public static final String MOD_ID = "thebindingcontracts";

    @Override
    public void onInitialize() {
        ModItemComponents.register();
        ModItems.register();
    }
}
