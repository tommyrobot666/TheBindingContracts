package lommie.thebindingcontracts.data;

import com.mojang.serialization.Codec;
import lommie.thebindingcontracts.TheBindingContracts;
import lommie.thebindingcontracts.contract.Contract;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Uuids;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**Stores the world's contracts. Very important.*/
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

    public final HashMap<UUID, Contract> contracts;

    public ContractsPersistentState() {
        this(Map.of());
    }

    public ContractsPersistentState(Map<UUID, Contract> contracts) {
        this.contracts = new HashMap<>(contracts);
    }

    public static ContractsPersistentState getPersistentStateInWorld(ServerWorld world){
        return world.getServer().getOverworld().getPersistentStateManager().getOrCreate(
            TYPE
        );
    }

    public static ContractsPersistentState getPersistentStateInServer(MinecraftServer server){
        return server.getOverworld().getPersistentStateManager().getOrCreate(
                TYPE
        );
    }

    public static HashMap<UUID, Contract> getContractsInWorld(ServerWorld world){
        return getPersistentStateInWorld(world).contracts;
    }

    public static HashMap<UUID, Contract> getContractsInServer(MinecraftServer server){
        return getPersistentStateInServer(server).contracts;
    }

    public static Contract getAContractInWorld(ServerWorld world, UUID id){
        return getPersistentStateInWorld(world).getAContract(id);
    }

    public static Contract getAContractInWorldAndDirty(ServerWorld world, UUID uuid) {
        ContractsPersistentState state = getPersistentStateInWorld(world);
        state.markDirty();
        return state.getAContract(uuid);
    }

    public Contract getAContract(UUID id){
        Contract contract = contracts.get(id);
        if (contract == null){
            contracts.put(id, new Contract());
            return getAContract(id);
        }
        else {
            return contract;
        }
    }

    private Map<UUID, Contract> getContracts() {
        return contracts;
    }
}
