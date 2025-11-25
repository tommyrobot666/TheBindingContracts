package lommie.thebindingcontracts.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lommie.thebindingcontracts.items.ModItemComponents;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ContractIdSuggestions implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> commandContext, SuggestionsBuilder suggestionsBuilder) {
        try {
            return suggestionsBuilder.suggest(Objects.requireNonNull(Objects.requireNonNull(commandContext.getSource().getPlayer()).getStackInHand(Hand.MAIN_HAND).get(ModItemComponents.CONTRACT_ID)).toString()).buildFuture();
        } catch (Exception e) {
            return suggestionsBuilder.suggest("there was an error", Text.literal("\"oh no!\" (idc)").formatted(Formatting.GREEN)).buildFuture();
        }
    }
}
