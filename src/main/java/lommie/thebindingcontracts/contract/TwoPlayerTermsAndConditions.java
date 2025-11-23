package lommie.thebindingcontracts.contract;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public abstract class TwoPlayerTermsAndConditions extends TermsAndConditions{
    public TwoPlayerTermsAndConditions(Identifier key, NbtCompound savedData) {
        super(key, savedData);
    }

    public TwoPlayerTermsAndConditions(Identifier key) {
        super(key);
    }

    public void onUseWhenOtherIsOnline(ServerWorld world, ServerPlayerEntity user, ServerPlayerEntity other, Hand hand, ItemStack stack, ItemStack stackInOtherHand, Contract contract) {

    }
}
