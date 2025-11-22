package lommie.thebindingcontracts.items;

import lommie.thebindingcontracts.contract.Contract;
import lommie.thebindingcontracts.data.ContractsPersistentState;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class ContractItem extends Item {
    public ContractItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) return ActionResult.PASS;
        ItemStack stack = user.getStackInHand(hand);
        Contract contract = getContract(stack, (ServerWorld) world);
        contract.onUseItem(0,world,user,hand);

        if (hand == Hand.OFF_HAND) return ActionResult.PASS;
        ItemStack stackInOtherHand = user.getStackInHand(Hand.OFF_HAND);
        if (canAddPlayerToContract(contract,user.getUuid())){
            addPlayerToContract(contract,user.getUuid(),world,user.getBlockPos());
            return ActionResult.SUCCESS;
        }
        if (stackInOtherHand.isOf(ModItems.WAX_SEAL) && contract.isValid()){
            stackInOtherHand.decrement(1);
            contract.sign();
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    private void addPlayerToContract(Contract contract, UUID player, World world, BlockPos pos) {
        contract.addSigner(player);
        world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS);
    }

    private boolean canAddPlayerToContract(Contract contract, UUID player){
        return !contract.getSigners().contains(player)&&contract.isUnfinished();
    }

    @Override
    public void onCraftByPlayer(ItemStack stack, PlayerEntity player) {
        if (player.getEntityWorld().isClient()) return;
        Contract contract = getContract(stack, (ServerWorld) player.getEntityWorld());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        if (stack.getOrDefault(ModItemComponents.BROKEN, false)) {
            textConsumer.accept(Text.literal("BROKEN").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)));
            return;
        }

        //TODO: send usernames to client
        UUID playerSignature = null;//stack.get(ModItemComponents.CONTRACT_SIGNATURE);
        if (playerSignature != null) {
            textConsumer.accept(Text.literal("First signature: "+playerSignature));
        }
        UUID otherPlayerSignature = null;//stack.get(ModItemComponents.OTHER_CONTRACT_SIGNATURE);
        if (otherPlayerSignature != null) {
            textConsumer.accept(Text.literal("Second signature: "+otherPlayerSignature));
        }
    }

    public static UUID getOtherPlayer(Contract contract, UUID player) {
        UUID id_one = Objects.requireNonNull(contract.getSigners().getFirst());
        UUID id_two = Objects.requireNonNull(contract.getSigners().getLast());

        if (id_one.equals(player)){
            return id_two;
        } else {
            return id_one;
        }
    }

    public static Contract getContract(ItemStack stack, ServerWorld world){
        return ContractsPersistentState.getAContractInWorldAndDirty(world,
                Objects.requireNonNull(stack.get(ModItemComponents.CONTRACT_ID)));
    }

    public static Contract getContractNoDirty(ItemStack stack, ServerWorld world){
        return ContractsPersistentState.getAContractInWorld(world,
                Objects.requireNonNull(stack.get(ModItemComponents.CONTRACT_ID)));
    }
}
