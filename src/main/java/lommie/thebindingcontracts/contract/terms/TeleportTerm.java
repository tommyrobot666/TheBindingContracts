package lommie.thebindingcontracts.contract.terms;

import lommie.thebindingcontracts.contract.Contract;
import lommie.thebindingcontracts.contract.ModTerms;
import lommie.thebindingcontracts.contract.TermsAndConditions;
import lommie.thebindingcontracts.contract.TwoPlayerTermsAndConditions;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

public class TeleportTerm extends TwoPlayerTermsAndConditions {
    public TeleportTerm(NbtCompound savedData) {
        super(savedData);
    }

    public TeleportTerm() {
        super(new NbtCompound());
        savedData.putInt("uses_left",10);
    }

    @Override
    public void onUseWhenOtherIsOnline(ServerWorld world, ServerPlayerEntity user, ServerPlayerEntity other, Hand hand, ItemStack stack, ItemStack stackInOtherHand, Contract contract) {
        int usesLeft = savedData.getInt("uses_left",0);
        if (usesLeft < 1) return;

        user.sendMessage(Text.literal("Uses left: "+usesLeft));
        user.teleportTo(new TeleportTarget(world, other.getEntityPos(), Vec3d.ZERO,
                other.getYaw(), other.getPitch(), TeleportTarget.NO_OP));

        savedData.putInt("uses_left", usesLeft-1);
    }

    @Override
    public TermsAndConditions typeCreateNew(NbtCompound savedData) {
        return new TeleportTerm(savedData);
    }

    @Override
    public TermsAndConditions typeCreateNew() {
        return new TeleportTerm();
    }

    @Override
    public Identifier typeGetId() {
        return ModTerms.TELEPORT;
    }
}
