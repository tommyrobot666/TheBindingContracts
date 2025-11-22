package lommie.thebindingcontracts.contract;

import lommie.thebindingcontracts.TheBindingContracts;
import lommie.thebindingcontracts.contract.terms.LandTerm;
import lommie.thebindingcontracts.contract.terms.LifeLinkTerm;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModTerms {
    public static <T extends TermsAndConditions> Identifier register(String id, T termsAndConditions){
        Registry.register(TheBindingContracts.TERM_TYPE_REGISTRY, Identifier.of(TheBindingContracts.MOD_ID, id), termsAndConditions);
        return Identifier.of(TheBindingContracts.MOD_ID, id);
    }

    public static final Identifier LIFE_LINK = register("life_link",new LifeLinkTerm());
    public static final Identifier TELEPORT = register("teleport",new LifeLinkTerm());
    public static final Identifier TRADE = register("trade",new LifeLinkTerm());
    public static final Identifier LAND = register("land",new LandTerm());

    public static void register(){}
}
