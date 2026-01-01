package lommie.thebindingcontracts.items;

import lommie.thebindingcontracts.contract.Contract;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.Objects;
import java.util.UUID;

/***/
public class TwoPlayerContractItem extends ContractItem{
    public TwoPlayerContractItem(Settings settings) {
        super(settings);
    }

    public static UUID getOtherPlayerId(Contract contract, UUID player) {
        UUID id_one = Objects.requireNonNull(contract.getSigners().getFirst());
        UUID id_two = Objects.requireNonNull(contract.getSigners().getLast());

        if (id_one.equals(player)){
            return id_two;
        } else {
            return id_one;
        }
    }

    public static ServerPlayerEntity getOtherPlayer(Contract contract, ServerWorld world, UUID player){
        return (ServerPlayerEntity) world.getPlayerAnyDimension(getOtherPlayerId(contract, player));
    }

    @Override
    public void onCraftByPlayer(ItemStack stack, PlayerEntity player) {
        if (player.getEntityWorld().isClient()) return;
        Contract contract = getContract(stack, (ServerWorld) player.getEntityWorld());
        addPlayerToContract(stack, contract, player, player.getEntityWorld());
    }

    @Override
    public boolean canAddPlayerToContract(Contract contract, UUID player) {
        return super.canAddPlayerToContract(contract, player) && contract.getSigners().size() < 2;
    }
}
