package lommie.thebindingcontracts;

import lommie.thebindingcontracts.items.ModItemComponents;
import lommie.thebindingcontracts.items.ModItems;
import net.fabricmc.api.ModInitializer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TheBindingContracts implements ModInitializer {
    // https://misode.github.io/recipe/
    public static final String MOD_ID = "thebindingcontracts";

    public static final List<UUID> playersToKill = new ArrayList<>();

    @Override
    public void onInitialize() {
        ModItemComponents.register();
        ModItems.register();
    }
}
