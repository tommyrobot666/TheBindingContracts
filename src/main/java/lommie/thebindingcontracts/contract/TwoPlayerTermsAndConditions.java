package lommie.thebindingcontracts.contract;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

/***/
@SuppressWarnings("unused")
public interface TwoPlayerTermsAndConditions<T extends TermsAndConditions> extends TermsAndConditionsType{

    void onUseWhenOtherIsOnline(ServerWorld world, ServerPlayerEntity user, ServerPlayerEntity other, Hand hand, ItemStack stack, ItemStack stackInOtherHand, Contract contract);

    @Override
    default int typeMaxPlayers() {
        return 2;
    }
}
