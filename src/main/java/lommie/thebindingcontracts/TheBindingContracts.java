package lommie.thebindingcontracts;

import lommie.thebindingcontracts.items.ModItemComponents;
import lommie.thebindingcontracts.items.ModItems;
import lommie.thebindingcontracts.recipes.ModRecipeSerializers;
import net.fabricmc.api.ModInitializer;

public class TheBindingContracts implements ModInitializer {
    public static final String MOD_ID = "thebindingcontracts";

    @Override
    public void onInitialize() {
        ModItemComponents.register();
        ModItems.register();
        ModRecipeSerializers.register();
    }
}
