package lommie.thebindingcontracts.contract.terms;

import lommie.thebindingcontracts.contract.Contract;
import lommie.thebindingcontracts.contract.ModTerms;
import lommie.thebindingcontracts.contract.TermsAndConditions;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

public class TradeTerm extends TermsAndConditions {
    public TradeTerm() {
        super(ModTerms.TRADE);
    }

    @Override
    public void onUseWhenOtherIsOnline(ServerWorld world, ServerPlayerEntity user, ServerPlayerEntity other, Hand hand, ItemStack stack, ItemStack stackInOtherHand, Contract contract) {
        ItemEntity itemEntity = new ItemEntity(other.getEntityWorld(),
                other.getX(),other.getY(),other.getZ(),
                stackInOtherHand.copy());
        itemEntity.setOwner(other.getUuid());
        itemEntity.setThrower(other);
        itemEntity.setPickupDelay(0);
        itemEntity.setNeverDespawn();
        other.getEntityWorld().spawnEntity(itemEntity);
        stackInOtherHand.setCount(0);
    }

}
