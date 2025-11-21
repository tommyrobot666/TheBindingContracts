package lommie.thebindingcontracts.contract;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

public class TermsAndConditions {
    public void onUseWhenOtherIsOnline(ServerWorld world, ServerPlayerEntity user, ServerPlayerEntity other, Hand hand, ItemStack stack, ItemStack stackInOtherHand, Contract contract) {

    }

    public void onTickForEachPlayer(ServerWorld world, ServerPlayerEntity player){

    }
}
