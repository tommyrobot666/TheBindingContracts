package lommie.thebindingcontracts.contract;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public interface TermsAndConditionsType {
    TermsAndConditions typeCreateNew(NbtCompound savedData);

    TermsAndConditions typeCreateNew();

    Identifier typeGetId();

    default boolean typeHasAction(){return false;}

    default Text typeGetDisplayName(){
        return Text.translatable(typeGetId().toTranslationKey("term"));
    }

    @SuppressWarnings("unused")
    default int typeMaxPlayers(){
        return -1;
    }
}
