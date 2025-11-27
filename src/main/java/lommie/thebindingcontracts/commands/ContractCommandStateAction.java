package lommie.thebindingcontracts.commands;

public enum ContractCommandStateAction {
    SET_SIGNED("set_signed"),
    SET_BROKEN("set_broken");

    final String name;

    ContractCommandStateAction(String name) {
        this.name = name;
    }

    String getName(){
        return this.name;
    }
}
