package lommie.thebindingcontracts.mixin;

import lommie.thebindingcontracts.TheBindingContracts;
import lommie.thebindingcontracts.items.ContractItem;
import lommie.thebindingcontracts.items.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Shadow @Final
    PlayerInventory inventory;

    @Inject(method = "onDeath", at = @At("HEAD"))
    void onDeath(DamageSource damageSource, CallbackInfo ci){
        final UUID finalUuid = ((Entity) (Object) this).getUuid();
        this.inventory.forEach(stack -> {
            if (stack.isOf(ModItems.CONTRACT)){
                if (ContractItem.isValidContract(stack)) {
                    UUID otherPlayer = ContractItem.getOtherPlayer(stack,finalUuid);
                    TheBindingContracts.playersToKill.add(otherPlayer);
                    stack.decrement(1);
                }
            }
        });
    }
}
