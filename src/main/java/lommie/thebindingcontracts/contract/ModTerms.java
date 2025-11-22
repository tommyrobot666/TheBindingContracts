package lommie.thebindingcontracts.contract;

import lommie.thebindingcontracts.TheBindingContracts;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModTerms {
    public static <T extends TermsAndConditions> void register(String id, T termsAndConditions){
        Registry.register(TheBindingContracts.TERM_TYPE_REGISTRY, Identifier.of(TheBindingContracts.MOD_ID, id), termsAndConditions);
    }

    public static void register(){}
}
