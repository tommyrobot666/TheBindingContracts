package lommie.thebindingcontracts.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lommie.thebindingcontracts.contract.Contract;
import lommie.thebindingcontracts.data.ContractsPersistentState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ContractSignersSuggestions implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> commandContext, SuggestionsBuilder suggestionsBuilder) throws CommandSyntaxException {
        UUID contractId = commandContext.getArgument("id",UUID.class);
        Contract contract = ContractsPersistentState.getAContractInWorld(commandContext.getSource().getWorld(),contractId);
        ArrayList<String> allSigners = new ArrayList<>();

        for (UUID signer : contract.getSigners()) {
            PlayerEntity player = commandContext.getSource().getWorld().getPlayerAnyDimension(signer);
            if (player != null){
                allSigners.add(player.getStringifiedName());
            } else {
                allSigners.add(signer.toString());
            }
        }

        for (String signer : allSigners) {
            if (signer.startsWith(suggestionsBuilder.getRemainingLowerCase())){
                suggestionsBuilder.suggest(signer);
            }
        }

        return suggestionsBuilder.buildFuture();
    }
}
