package lommie.thebindingcontracts.commands;

public enum ContractCommandTermAction {
    ADD_TERM("add_term"),
    REMOVE_TERM("remove_term");

    final String name;

    ContractCommandTermAction(String name) {
        this.name = name;
    }

    String getName(){
        return this.name;
    }
}
