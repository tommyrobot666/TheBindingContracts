package lommie.thebindingcontracts.commands;

import com.mojang.brigadier.context.CommandContext;
import lommie.thebindingcontracts.contract.Contract;
import lommie.thebindingcontracts.contract.TermsAndConditions;
import lommie.thebindingcontracts.data.ContractsPersistentState;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class ContractCommand {
    public static int TermModification(CommandContext<ServerCommandSource> source) {
        UUID contractId = source.getArgument("id",UUID.class);
        ContractCommandTermAction action = source.getArgument("action", ContractCommandTermAction.class);
        Identifier termId = source.getArgument("term", Identifier.class);
        switch (action){
            case ADD_TERM ->
            {
                try {
                    TermsAndConditions term = TermsAndConditions.createNew(termId);
                    Contract contract = ContractsPersistentState.getAContractInWorld(source.getSource().getWorld(),contractId);
                    contract.addTerm(term);
                    source.getSource().sendFeedback(() -> Text.literal("Added term "+termId.toString()+" to contract "+contractId.toString()+"!"), true);
                    source.getSource().sendFeedback(() -> Text.literal("Now terms is "+contract.listTermIds()), false);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return 1;
            }
            case REMOVE_TERM -> {
                source.getSource().sendError(Text.literal("nah, I don't think you really even need this feature anyway"));
                source.getSource().sendError(Text.literal("(no terms removed)"));
                return 1;
            }
            case null, default -> {
                return 0;
            }
        }
    }

    public static int SignersModification(CommandContext<ServerCommandSource> source) {
        UUID contractId = source.getArgument("id",UUID.class);
        ContractCommandSignerAction action = source.getArgument("action", ContractCommandSignerAction.class);
        switch (action){
            case ADD_SIGNER ->
            {
                try {
                    PlayerEntity player = source.getArgument("player", EntitySelector.class).getPlayer(source.getSource());
                    Contract contract = ContractsPersistentState.getAContractInWorld(source.getSource().getWorld(),contractId);
                    contract.addSigner(player.getUuid());
                    source.getSource().sendFeedback(() -> Text.literal("Added signer ").append(player.getDisplayName()).append(Text.literal("to contract "+contractId.toString()+"!")), true);
                    source.getSource().sendFeedback(() -> Text.literal("Now signers is "+contract.listSignerIds()), false);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return 1;
            }
            case REMOVE_SIGNER -> {
                source.getSource().sendError(Text.literal("nah, I don't think you really even need this feature anyway"));
                source.getSource().sendError(Text.literal("(no signers removed)"));
                return 1;
            }
            case null, default -> {
                return 0;
            }
        }
    }

    public static int StateModification(CommandContext<ServerCommandSource> source) {
        UUID contractId = source.getArgument("id",UUID.class);
        ContractCommandStateAction action = source.getArgument("action", ContractCommandStateAction.class);
        boolean bool = source.getArgument("bool", boolean.class);
        switch (action){
            case SET_BROKEN ->
            {
                try {
                    Contract contract = ContractsPersistentState.getAContractInWorld(source.getSource().getWorld(),contractId);
                    contract.forceSetBroken(bool);
                    source.getSource().sendFeedback(() -> Text.literal("Set broken to "+bool+" for contract "+contractId.toString()+"!"), true);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return 1;
            }
            case SET_SIGNED -> {
                try {
                    Contract contract = ContractsPersistentState.getAContractInWorld(source.getSource().getWorld(),contractId);
                    contract.forceSetSigned(bool);
                    source.getSource().sendFeedback(() -> Text.literal("Set signed to "+bool+" for contract "+contractId.toString()+"!"), true);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return 1;
            }
            case null, default -> {
                return 0;
            }
        }
    }
}
