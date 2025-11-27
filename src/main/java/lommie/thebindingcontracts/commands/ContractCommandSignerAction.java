package lommie.thebindingcontracts.commands;

public enum ContractCommandSignerAction {
    ADD_SIGNER("add_signer"),
    REMOVE_SIGNER("remove_signer");

    final String name;

    ContractCommandSignerAction(String name) {
        this.name = name;
    }

    String getName(){
        return this.name;
    }
}
