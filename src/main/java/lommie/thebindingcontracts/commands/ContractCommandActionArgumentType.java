package lommie.thebindingcontracts.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;

import java.util.concurrent.CompletableFuture;

public class ContractCommandActionArgumentType implements ArgumentType<ContractCommandAction>, SuggestionProvider<ServerCommandSource> {
    @Override
    public ContractCommandAction parse(StringReader stringReader) throws CommandSyntaxException {
        return ContractCommandAction.valueOf(stringReader.readString());
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> commandContext, SuggestionsBuilder suggestionsBuilder) throws CommandSyntaxException {
        return CompletableFuture.completedFuture(suggestionsBuilder
                .suggest("add_term")
                .suggest("remove_term")
                .suggest("add_signer")
                .suggest("remove_signer")
                .suggest("set_signed")
                .suggest("set_broken")
                .build());
    }
}
