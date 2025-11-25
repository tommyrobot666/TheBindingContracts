package lommie.thebindingcontracts.contract;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public interface TermsAndConditionsType {
    TermsAndConditions typeCreateNew(NbtCompound savedData);

    TermsAndConditions typeCreateNew();

    Identifier typeGetId();

    @SuppressWarnings("unused")
    default int typeMaxPlayers(){
        return -1;
    }
}
