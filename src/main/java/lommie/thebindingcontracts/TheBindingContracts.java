package lommie.thebindingcontracts;

import lommie.thebindingcontracts.items.ModItemComponents;
import lommie.thebindingcontracts.items.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TheBindingContracts implements ModInitializer {
    // https://misode.github.io/recipe/
    public static final String MOD_ID = "thebindingcontracts";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final List<UUID> playersToKill = new ArrayList<>();

    @Override
    public void onInitialize() {
        ModItemComponents.register();
        ModItems.register();
    }
}
