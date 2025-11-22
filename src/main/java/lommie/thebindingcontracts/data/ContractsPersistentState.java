package lommie.thebindingcontracts.data;

import com.mojang.serialization.Codec;
import lommie.thebindingcontracts.TheBindingContracts;
import lommie.thebindingcontracts.contract.Contract;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Uuids;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ContractsPersistentState extends PersistentState {
    public static final Codec<ContractsPersistentState> CODEC = Codec.unboundedMap(
            Uuids.CODEC,Contract.CODEC
    ).xmap(
            ContractsPersistentState::new,
            ContractsPersistentState::getContracts
    );

    public static final PersistentStateType<ContractsPersistentState> TYPE =
            new PersistentStateType<>(
                    TheBindingContracts.MOD_ID+"_persistent_contracts",
                    ContractsPersistentState::new,
                    CODEC,
                    null
                    );

    public final HashMap<UUID, Contract> contracts = new HashMap<>();

    public ContractsPersistentState() {}

    public ContractsPersistentState(Map<UUID, Contract> contracts) {
    }

    public static ContractsPersistentState getPersistentStateInWorld(ServerWorld world){
        return world.getServer().getOverworld().getPersistentStateManager().getOrCreate(
            TYPE
        );
    }

    public static HashMap<UUID, Contract> getContractsInWorld(ServerWorld world){
        return getPersistentStateInWorld(world).contracts;
    }

    public static Contract getAContractInWorld(ServerWorld world, UUID id){
        return getContractsInWorld(world).get(id);
    }

    public Contract getAContract(UUID id){
        return contracts.get(id);
    }

    private Map<UUID, Contract> getContracts() {
        return contracts;
    }
}
