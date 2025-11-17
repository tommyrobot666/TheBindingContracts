package lommie.thebindingcontracts.items;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.UUID;

public class TradeContract extends ContractItem{
    public TradeContract(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()){
            ItemStack stackInOtherHand = inOtherHand(user,hand);
            if (!stackInOtherHand.isEmpty()) {
                ItemStack stack = user.getStackInHand(hand);
                assert stack.isOf(ModItems.TRADE_CONTRACT);
                if (isValidContract(stack)) {
                    UUID otherId = ContractItem.getOtherPlayer(stack, user.getUuid());
                    PlayerEntity other = world.getPlayerAnyDimension(otherId);
                    if (other != null) {
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
            }
        }

        return super.use(world, user, hand);
    }

    ItemStack inOtherHand(PlayerEntity player, Hand hand){
        if (hand == Hand.MAIN_HAND){
            return player.getStackInHand(Hand.OFF_HAND);
        } else {
            return player.getStackInHand(Hand.MAIN_HAND);
        }
    }
}
