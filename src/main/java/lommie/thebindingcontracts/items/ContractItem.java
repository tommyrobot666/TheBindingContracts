package lommie.thebindingcontracts.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
            ItemStack stack = user.getStackInHand(Hand.MAIN_HAND);
            assert stack.isOf(ModItems.CONTRACT); // just in case my code is bad

            if (canAddOtherPlayerToContract(stack,user.getUuid())) {
                addOtherPlayerToContract(stack,user.getUuid());
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    private void addOtherPlayerToContract(ItemStack stack, UUID otherId) {
        stack.set(ModItemComponents.OTHER_CONTRACT_SIGNATURE,otherId);
    }

    private boolean canAddOtherPlayerToContract(ItemStack stack, UUID otherId) {
        if (stack.get(ModItemComponents.CONTRACT_SIGNATURE) == null) return false;
        return stack.get(ModItemComponents.CONTRACT_SIGNATURE) != otherId;
    }

    @Override
    public void onCraftByPlayer(ItemStack stack, PlayerEntity player) {
        stack.set(ModItemComponents.CONTRACT_SIGNATURE,player.getUuid());
    }
}
