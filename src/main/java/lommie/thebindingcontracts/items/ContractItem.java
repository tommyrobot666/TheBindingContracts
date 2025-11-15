package lommie.thebindingcontracts.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.UUID;

public class ContractItem extends Item {
    public ContractItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient() || hand == Hand.OFF_HAND) return ActionResult.PASS;
        if (user.getStackInHand(Hand.OFF_HAND).isOf(ModItems.WAX_SEAL)) {
            if (canAddOtherPlayerToContract(user.getUuid())) {
                addOtherPlayerToContract(user.getUuid());
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    private void addOtherPlayerToContract(UUID uuid) {
    }

    private boolean canAddOtherPlayerToContract(UUID uuid) {
        return true;
    }
}
