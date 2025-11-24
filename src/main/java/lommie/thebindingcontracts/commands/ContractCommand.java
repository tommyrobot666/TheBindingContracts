package lommie.thebindingcontracts.commands;

import com.mojang.brigadier.context.CommandContext;
import lommie.thebindingcontracts.contract.Contract;
import lommie.thebindingcontracts.contract.TermsAndConditions;
import lommie.thebindingcontracts.data.ContractsPersistentState;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class ContractCommand {
    public static int TermModification(CommandContext<ServerCommandSource> source) {
        UUID contractId = source.getArgument("id",UUID.class);
        ContractCommandAction action = source.getArgument("action",ContractCommandAction.class);
        Identifier termId = source.getArgument("term", Identifier.class);
        switch (action){
            case ADD_TERM ->
            {
                try {
                    TermsAndConditions term = TermsAndConditions.createNew(termId);
                    Contract contract = ContractsPersistentState.getAContractInWorld(source.getSource().getWorld(),contractId);
                    contract.addTerm(term);
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
}
