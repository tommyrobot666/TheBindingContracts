package lommie.thebindingcontracts.contract.terms;

import lommie.thebindingcontracts.TheBindingContracts;
import lommie.thebindingcontracts.contract.Contract;
import lommie.thebindingcontracts.contract.ModTerms;
import lommie.thebindingcontracts.contract.TermsAndConditions;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/***/
public class LifeLinkTerm extends TermsAndConditions {
    public static final Identifier MAX_HEALTH_MODIFIER = Identifier.of(
            TheBindingContracts.MOD_ID,"contract"
    );
    public static final String PLAYERS_TO_KILL = "players_to_kill";

    public LifeLinkTerm(NbtCompound savedData) {
        super(savedData);
    }

    public LifeLinkTerm() {
        super(new NbtCompound());
    }

    @SuppressWarnings("unused")
    @Override
    public void onTickForEachPlayer(ServerWorld world, ServerPlayerEntity player, Contract contract) {
        addAttributeModifier(player);
        if (player.deathTime > 0){
            onPlayerJustDied(contract);
        }
    }

    private void onPlayerJustDied(Contract contract) {
        contract.break2();
    }

    @Override
    public void onTermsJustBroken(MinecraftServer server, Contract contract) {
        savedData.put(PLAYERS_TO_KILL, Uuids.CODEC.listOf().encodeStart(NbtOps.INSTANCE, contract.getSigners()).getOrThrow());
    }

    @Override
    public boolean onTermsBrokenTick(MinecraftServer server, Contract contract) {
        ArrayList<UUID> playersToKill = new ArrayList<>(savedData.get(PLAYERS_TO_KILL, Uuids.CODEC.listOf()).orElseThrow());
        if (playersToKill.isEmpty()){
            return true;
        }

        int i = 0;
        while (i < playersToKill.size()) {
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(playersToKill.get(i));
            if (player != null){
                player.kill(player.getEntityWorld());
                playersToKill.remove(i);
            } else {
                i++;
            }
        }
        savedData.put(PLAYERS_TO_KILL,Uuids.CODEC.listOf(),playersToKill);
        return false;
    }

    public void addAttributeModifier(PlayerEntity player) {
        if (player == null) return;
        EntityAttributeInstance maxHealthAttributes = Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.MAX_HEALTH));

        if (!maxHealthAttributes.hasModifier(MAX_HEALTH_MODIFIER)) {
            maxHealthAttributes.addPersistentModifier(new EntityAttributeModifier(MAX_HEALTH_MODIFIER, 1d, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }

    @SuppressWarnings("unused")
    @Override
    public TermsAndConditions typeCreateNew(NbtCompound savedData) {
        return new LifeLinkTerm(savedData);
    }

    @SuppressWarnings("unused")
    @Override
    public TermsAndConditions typeCreateNew() {
        return new LifeLinkTerm();
    }

    @SuppressWarnings("unused")
    @Override
    public Identifier typeGetId() {
        return ModTerms.LIFE_LINK;
    }
}
