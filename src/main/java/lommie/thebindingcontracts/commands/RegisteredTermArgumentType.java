package lommie.thebindingcontracts.commands;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lommie.thebindingcontracts.TheBindingContracts;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class RegisteredTermArgumentType implements ArgumentType<Identifier>, SuggestionProvider<ServerCommandSource> {
    static SimpleCommandExceptionType ERROR = new SimpleCommandExceptionType(Text.literal("Id not found it Term Type Registry."));

    @Override
    public Identifier parse(StringReader stringReader) throws CommandSyntaxException {
        Identifier id = Identifier.fromCommandInput(stringReader);
        if (TheBindingContracts.TERM_TYPE_REGISTRY.containsId(id)) {
            return id;
        } else {
            throw ERROR.createWithContext(stringReader);
        }
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> commandContext, SuggestionsBuilder suggestionsBuilder) throws CommandSyntaxException {
        for (Identifier id : TheBindingContracts.TERM_TYPE_REGISTRY.getIds()) {
            suggestionsBuilder.suggest(id.toString());
        }
        return suggestionsBuilder.buildFuture();
    }
}
