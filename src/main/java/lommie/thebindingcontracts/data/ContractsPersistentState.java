package lommie.thebindingcontracts.data;

import com.mojang.serialization.Codec;
import lommie.thebindingcontracts.TheBindingContracts;
import lommie.thebindingcontracts.contract.Contract;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;

import java.util.HashMap;
import java.util.UUID;

public class ContractsPersistentState extends PersistentState {
    public static final Codec<ContractsPersistentState> CODEC = null;
    public static final PersistentStateType<ContractsPersistentState> TYPE =
            new PersistentStateType<>(
                    TheBindingContracts.MOD_ID+"_persistent_contracts",
                    ContractsPersistentState::new,
                    CODEC,
                    null
                    );

    public static final HashMap<UUID, Contract> contracts = new HashMap<>();

    ContractsPersistentState getSavedData(ServerWorld world){
        return world.getServer().getOverworld().getPersistentStateManager().getOrCreate(
            TYPE
        );
    }
}
