package lommie.thebindingcontracts.commands;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.concurrent.CompletableFuture;

public class ContractCommandStateActionArgumentType implements ArgumentType<ContractCommandStateAction>, SuggestionProvider<ServerCommandSource> {
    static final SimpleCommandExceptionType ERROR = new SimpleCommandExceptionType(Text.literal("Action not found"));

    @Override
    public ContractCommandStateAction parse(StringReader stringReader) throws CommandSyntaxException {
        String value = stringReader.readString().strip().toLowerCase();
        for (ContractCommandStateAction action : ContractCommandStateAction.values()){
            if (value.contains(action.getName())){
                return action;
            }
        }
        throw ERROR.createWithContext(stringReader);
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> commandContext, SuggestionsBuilder suggestionsBuilder) {
        return listSuggestions(commandContext,suggestionsBuilder);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CompletableFuture.completedFuture(builder
                .suggest("add_term")
                .suggest("remove_term")
                .suggest("add_signer")
                .suggest("remove_signer")
                .suggest("set_signed")
                .suggest("set_broken")
                .build());
    }

    public static class Serializer implements ArgumentSerializer<ContractCommandStateActionArgumentType, Serializer.Properties> {
        @Override
        public void writePacket(Properties properties, PacketByteBuf buf) {}

        @Override
        public Properties fromPacket(PacketByteBuf buf) {return new Properties();}

        @Override
        public Properties getArgumentTypeProperties(ContractCommandStateActionArgumentType argumentType) {return new Properties();}

        @Override
        public void writeJson(Properties properties, JsonObject json) {}

        public final class Properties implements ArgumentTypeProperties<ContractCommandStateActionArgumentType>{

            @Override
            public ContractCommandStateActionArgumentType createType(CommandRegistryAccess commandRegistryAccess) {return new ContractCommandStateActionArgumentType();}

            @Override
            public ArgumentSerializer<ContractCommandStateActionArgumentType, ?> getSerializer() {return Serializer.this;}
        }
    }
}
