package lommie.thebindingcontracts.mixin;

import lommie.thebindingcontracts.TheBindingContracts;
import lommie.thebindingcontracts.items.ContractItem;
import lommie.thebindingcontracts.items.ModItemComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "onDeath", at = @At("HEAD"))
    void onDeath(DamageSource damageSource, CallbackInfo ci){
        TheBindingContracts.LOGGER.error("Mixin stuff");
        final UUID thisUuid = ((Entity) (Object) this).getUuid();
        for (ItemStack stack : ((PlayerEntity) (Object) this).getInventory()) {
            if (stack.getItem().getClass().getSuperclass().equals(ContractItem.class)){
                TheBindingContracts.LOGGER.error(stack.toString());
                TheBindingContracts.LOGGER.error("{}", ContractItem.isValidContract(stack));
                if (ContractItem.isValidContract(stack)) {
                    UUID otherPlayer = ContractItem.getOtherPlayer(stack,thisUuid);
                    TheBindingContracts.playersToKill.add(otherPlayer);
                    stack.set(ModItemComponents.BROKEN, true);
                }
            }
        }
    }
}
