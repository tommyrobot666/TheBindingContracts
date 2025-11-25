package lommie.thebindingcontracts.contract.terms;

import lommie.thebindingcontracts.contract.ModTerms;
import lommie.thebindingcontracts.contract.TermsAndConditions;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class LandTerm extends TermsAndConditions {
    public LandTerm(NbtCompound savedData) {
        super(savedData);
    }

    public LandTerm() {
        super(new NbtCompound());
    }

    @Override
    public TermsAndConditions typeCreateNew(NbtCompound savedData) {
        return new LandTerm(savedData);
    }

    @Override
    public TermsAndConditions typeCreateNew() {
        return new LandTerm();
    }

    @Override
    public Identifier typeGetId() {
        return ModTerms.LAND;
    }
}
