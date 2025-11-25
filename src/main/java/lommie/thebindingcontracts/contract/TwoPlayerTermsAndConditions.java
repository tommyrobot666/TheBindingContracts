package lommie.thebindingcontracts.contract;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

public abstract class TwoPlayerTermsAndConditions extends TermsAndConditions{
    public TwoPlayerTermsAndConditions(NbtCompound savedData) {
        super(savedData);
    }

    @SuppressWarnings("unused")
    public void onUseWhenOtherIsOnline(ServerWorld world, ServerPlayerEntity user, ServerPlayerEntity other, Hand hand, ItemStack stack, ItemStack stackInOtherHand, Contract contract) {

    }

    @Override
    public int typeMaxPlayers() {
        return 2;
    }
}
