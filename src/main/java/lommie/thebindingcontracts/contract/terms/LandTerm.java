package lommie.thebindingcontracts.contract.terms;

import lommie.thebindingcontracts.contract.ModTerms;
import lommie.thebindingcontracts.contract.TermsAndConditions;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

/**An example of an empty Term class*/
public class LandTerm extends TermsAndConditions {
    public LandTerm(NbtCompound savedData) {
        super(savedData);
    }

    public LandTerm() {
        super(new NbtCompound());
    }

    @SuppressWarnings("unused")
    @Override
    public TermsAndConditions typeCreateNew(NbtCompound savedData) {
        return new LandTerm(savedData);
    }

    @SuppressWarnings("unused")
    @Override
    public TermsAndConditions typeCreateNew() {
        return new LandTerm();
    }

    @SuppressWarnings("unused")
    @Override
    public Identifier typeGetId() {
        return ModTerms.LAND;
    }
}
