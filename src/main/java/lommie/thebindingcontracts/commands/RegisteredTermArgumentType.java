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
import lommie.thebindingcontracts.TheBindingContracts;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.command.argument.serialize.ArgumentSerializer;

import java.util.concurrent.CompletableFuture;

public class RegisteredTermArgumentType implements ArgumentType<Identifier>, SuggestionProvider<ServerCommandSource> {
    static SimpleCommandExceptionType ERROR = new SimpleCommandExceptionType(Text.literal("Id not found in Term Type Registry."));

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
        return listSuggestions(commandContext,suggestionsBuilder);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (Identifier id : TheBindingContracts.TERM_TYPE_REGISTRY.getIds()) {
            builder.suggest(id.toString());
        }
        return builder.buildFuture();
    }

    public static class Serializer implements ArgumentSerializer<RegisteredTermArgumentType, Serializer.Properties> {
        @Override
        public void writePacket(Properties properties, PacketByteBuf buf) {}

        @Override
        public Properties fromPacket(PacketByteBuf buf) {return new Properties();}

        @Override
        public Properties getArgumentTypeProperties(RegisteredTermArgumentType argumentType) {
            return new Properties();
        }

        @Override
        public void writeJson(Properties properties, JsonObject json) {}

        public final class Properties implements ArgumentSerializer.ArgumentTypeProperties<RegisteredTermArgumentType>{

            @Override
            public RegisteredTermArgumentType createType(CommandRegistryAccess commandRegistryAccess) {
                return new RegisteredTermArgumentType();
            }

            @Override
            public ArgumentSerializer<RegisteredTermArgumentType, ?> getSerializer() {
                return Serializer.this;
            }
        }
    }
}
