package lommie.thebindingcontracts.items;

import lommie.thebindingcontracts.TheBindingContracts;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class ContractItem extends Item {
    public static final Identifier MAX_HEALTH_MODIFIER = Identifier.of(
            TheBindingContracts.MOD_ID,"contract"
    );

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

    @SuppressWarnings("deprecation")
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        UUID playerSignature = stack.get(ModItemComponents.CONTRACT_SIGNATURE);
        if (playerSignature != null) {
            textConsumer.accept(Text.literal("First signature: "+playerSignature));
        }
        UUID otherPlayerSignature = stack.get(ModItemComponents.OTHER_CONTRACT_SIGNATURE);
        if (otherPlayerSignature != null) {
            textConsumer.accept(Text.literal("Second signature: "+otherPlayerSignature));
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        if (!isValidContract(stack)) return;
        PlayerEntity player = world.getPlayerAnyDimension(stack.get(ModItemComponents.CONTRACT_SIGNATURE));
        assert player != null;
        addAttributeModifier(player);
        PlayerEntity otherPlayer = world.getPlayerAnyDimension(stack.get(ModItemComponents.OTHER_CONTRACT_SIGNATURE));
        assert otherPlayer != null;
        addAttributeModifier(otherPlayer);
    }

    public void addAttributeModifier(PlayerEntity player){
        EntityAttributeInstance maxHealthAttributes = Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.MAX_HEALTH));

        if (!maxHealthAttributes.hasModifier(MAX_HEALTH_MODIFIER)) {
            maxHealthAttributes.addPersistentModifier(new EntityAttributeModifier(MAX_HEALTH_MODIFIER, 2d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }

    public boolean isValidContract(ItemStack stack){
        if (stack.get(ModItemComponents.CONTRACT_SIGNATURE) == null) return false;
        if (stack.get(ModItemComponents.OTHER_CONTRACT_SIGNATURE) == null) return false;
        return stack.get(ModItemComponents.CONTRACT_SIGNATURE) != stack.get(ModItemComponents.OTHER_CONTRACT_SIGNATURE);
    }
}
