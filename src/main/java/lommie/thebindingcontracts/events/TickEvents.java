package lommie.thebindingcontracts.events;

import lommie.thebindingcontracts.contract.Contract;
import lommie.thebindingcontracts.data.ContractsPersistentState;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Collection;

public class TickEvents {
    static final ArrayList<Contract> contractsToRemove = new ArrayList<>();

    public static void ServerTick(MinecraftServer server) {
        Collection<Contract> contracts = ContractsPersistentState.getContractsInServer(server).values();

        // remove forRemoval
        for (Contract contract : contractsToRemove) {
            contracts.remove(contract);
        }
        contractsToRemove.clear();

        // tick contracts
        for (Contract contract : contracts) {
            // normal tick
            if (contract.isValidAndSigned()) {
                contract.onTick(server);
                // no longer valid = broken
                if (contract.isBroken()){
                    contract.onTermsJustBroken();
                }
            } else if (contract.isBroken()) {
                // only add contract to forRemoval if it's 100% done with its on broken
                boolean forRemoval = contract.onTermsBrokenTick();
                if (forRemoval) {
                    contractsToRemove.add(contract);
                }
            }
        }
    }
}
