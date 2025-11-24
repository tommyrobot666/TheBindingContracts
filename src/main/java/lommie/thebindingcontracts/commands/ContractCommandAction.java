package lommie.thebindingcontracts.commands;

public enum ContractCommandAction {
    ADD_TERM("add_term"),
    REMOVE_TERM("remove_term"),
    ADD_SIGNER("add_signer"),
    REMOVE_SIGNER("remove_signer"),
    SET_SIGNED("set_signed"),
    SET_BROKEN("set_broken");

    final String name;

    ContractCommandAction(String name) {
        this.name = name;
    }

    String getName(){
        return this.name;
    }
}
