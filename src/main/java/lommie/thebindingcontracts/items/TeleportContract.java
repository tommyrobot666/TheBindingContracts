package lommie.thebindingcontracts.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

import java.util.UUID;

public class TeleportContract extends ContractItem{
    public TeleportContract(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient()){
            ItemStack stack = user.getStackInHand(hand);
            assert stack.isOf(ModItems.TELEPORT_CONTRACT);
            if (isValidContract(stack)) {
                UUID otherId = ContractItem.getOtherPlayer(stack, user.getUuid());
                PlayerEntity other = world.getPlayerAnyDimension(otherId);
                if (other != null) {
                    stack.damage(1,user);
                    user.teleportTo(new TeleportTarget(((ServerWorld) world), other.getEntityPos(), Vec3d.ZERO,
                            other.getYaw(), other.getPitch(), TeleportTarget.NO_OP));
                }
            }
        }

        return super.use(world, user, hand);
    }
}
