package lommie.thebindingcontracts;

import lommie.thebindingcontracts.commands.ModCommands;
import lommie.thebindingcontracts.contract.ModTerms;
import lommie.thebindingcontracts.contract.TermsAndConditions;
import lommie.thebindingcontracts.items.ModItemComponents;
import lommie.thebindingcontracts.items.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TheBindingContracts implements ModInitializer {
    // https://misode.github.io/recipe/
    public static final String MOD_ID = "thebindingcontracts";
    @SuppressWarnings("unused")
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final RegistryKey<Registry<TermsAndConditions>> TERM_TYPE_REGISTRY_KEY = RegistryKey.ofRegistry(
            Identifier.of(MOD_ID,"term_type")
    );
    public static final SimpleRegistry<TermsAndConditions> TERM_TYPE_REGISTRY = FabricRegistryBuilder.createSimple(
            TERM_TYPE_REGISTRY_KEY
    ).buildAndRegister();

    @Override
    public void onInitialize() {
        ModItemComponents.register();
        ModItems.register();
        ModTerms.register();
        ModCommands.register();
    }
}
