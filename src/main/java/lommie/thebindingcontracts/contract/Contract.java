package lommie.thebindingcontracts.contract;

import java.util.ArrayList;

public class Contract {
    public ArrayList<TermsAndConditions> terms = new ArrayList<>();

    public Contract(){}

    public void onUseItem(int selectedTerm) {
        terms.get(selectedTerm);
    }
}
