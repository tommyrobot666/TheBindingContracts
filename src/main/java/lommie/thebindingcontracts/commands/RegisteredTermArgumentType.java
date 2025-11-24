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

    public static class Serializer implements ArgumentSerializer<RegisteredTermArgumentType, SerializerProperties> {


        @Override
        public void writePacket(SerializerProperties serializerProperties, PacketByteBuf buf) {

        }

        @Override
        public SerializerProperties fromPacket(PacketByteBuf buf) {
            return null;
        }

        @Override
        public void writeJson(SerializerProperties serializerProperties, JsonObject json) {

        }

        @Override
        public SerializerProperties getArgumentTypeProperties(RegisteredTermArgumentType argumentType) {
            return null;
        }

    }

    public static class SerializerProperties implements ArgumentSerializer.ArgumentTypeProperties<RegisteredTermArgumentType>{

        @Override
        public RegisteredTermArgumentType createType(CommandRegistryAccess commandRegistryAccess) {
            return new RegisteredTermArgumentType();
        }

        @Override
        public ArgumentSerializer<RegisteredTermArgumentType, ?> getSerializer() {
            return this;
        }
    }
}
