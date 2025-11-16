package lommie.thebindingcontracts.items;

import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;
import java.util.function.Consumer;

public abstract class ContractItem extends Item {
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
                addOtherPlayerToContract(stack,user.getUuid(),world,user.getBlockPos());
                user.getStackInHand(Hand.OFF_HAND).decrement(1);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    private void addOtherPlayerToContract(ItemStack stack, UUID otherId, World world, BlockPos pos) {
        stack.set(ModItemComponents.OTHER_CONTRACT_SIGNATURE,otherId);
        world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS);
    }

    private boolean canAddOtherPlayerToContract(ItemStack stack, UUID otherId) {
        if (stack.get(ModItemComponents.CONTRACT_SIGNATURE) == null) return false;
        if (isValidContract(stack)) return false;
        return stack.get(ModItemComponents.CONTRACT_SIGNATURE) != otherId;
    }

    @Override
    public void onCraftByPlayer(ItemStack stack, PlayerEntity player) {
        stack.set(ModItemComponents.CONTRACT_SIGNATURE,player.getUuid());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        if (stack.getOrDefault(ModItemComponents.BROKEN, false)) {
            textConsumer.accept(Text.literal("BROKEN").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)));
            return;
        }

        UUID playerSignature = stack.get(ModItemComponents.CONTRACT_SIGNATURE);
        if (playerSignature != null) {
            textConsumer.accept(Text.literal("First signature: "+playerSignature));
        }
        UUID otherPlayerSignature = stack.get(ModItemComponents.OTHER_CONTRACT_SIGNATURE);
        if (otherPlayerSignature != null) {
            textConsumer.accept(Text.literal("Second signature: "+otherPlayerSignature));
        }
    }

    public static boolean isValidContract(ItemStack stack){
        if (stack.getOrDefault(ModItemComponents.BROKEN, false)) return false;
        if (stack.get(ModItemComponents.CONTRACT_SIGNATURE) == null) return false;
        if (stack.get(ModItemComponents.OTHER_CONTRACT_SIGNATURE) == null) return false;
        return stack.get(ModItemComponents.CONTRACT_SIGNATURE) != stack.get(ModItemComponents.OTHER_CONTRACT_SIGNATURE);
    }


    public static UUID getOtherPlayer(ItemStack stack, UUID player) {
        UUID id_one = stack.get(ModItemComponents.CONTRACT_SIGNATURE);
        UUID id_two = stack.get(ModItemComponents.OTHER_CONTRACT_SIGNATURE);

        if (id_one == player){
            return id_two;
        } else {
            return id_one;
        }
    }
}
