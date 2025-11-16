package lommie.thebindingcontracts.items;

import lommie.thebindingcontracts.TheBindingContracts;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class LifeLinkContract extends ContractItem{
    public static final Identifier MAX_HEALTH_MODIFIER = Identifier.of(
            TheBindingContracts.MOD_ID,"contract"
    );

    public LifeLinkContract(Settings settings) {
        super(settings);
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
        if (player == null) return;
        EntityAttributeInstance maxHealthAttributes = Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.MAX_HEALTH));

        if (!maxHealthAttributes.hasModifier(MAX_HEALTH_MODIFIER)) {
            maxHealthAttributes.addPersistentModifier(new EntityAttributeModifier(MAX_HEALTH_MODIFIER, 1d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }
}
