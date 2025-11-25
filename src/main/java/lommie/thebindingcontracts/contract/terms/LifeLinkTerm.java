package lommie.thebindingcontracts.contract.terms;

import lommie.thebindingcontracts.TheBindingContracts;
import lommie.thebindingcontracts.contract.ModTerms;
import lommie.thebindingcontracts.contract.TermsAndConditions;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.Objects;

public class LifeLinkTerm extends TermsAndConditions {
    public static final Identifier MAX_HEALTH_MODIFIER = Identifier.of(
            TheBindingContracts.MOD_ID,"contract"
    );

    public LifeLinkTerm(NbtCompound savedData) {
        super(savedData);
    }

    public LifeLinkTerm() {
        super(new NbtCompound());
    }

    @Override
    public void onTickForEachPlayer(ServerWorld world, ServerPlayerEntity player) {
        addAttributeModifier(player);
    }

    public void addAttributeModifier(PlayerEntity player){
        if (player == null) return;
        EntityAttributeInstance maxHealthAttributes = Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.MAX_HEALTH));

        if (!maxHealthAttributes.hasModifier(MAX_HEALTH_MODIFIER)) {
            maxHealthAttributes.addPersistentModifier(new EntityAttributeModifier(MAX_HEALTH_MODIFIER, 1d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }

    @Override
    public TermsAndConditions typeCreateNew(NbtCompound savedData) {
        return new LifeLinkTerm(savedData);
    }

    @Override
    public TermsAndConditions typeCreateNew() {
        return new LifeLinkTerm();
    }

    @Override
    public Identifier typeGetId() {
        return ModTerms.LIFE_LINK;
    }
}
