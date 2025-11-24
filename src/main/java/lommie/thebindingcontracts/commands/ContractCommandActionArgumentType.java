package lommie.thebindingcontracts.commands;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.network.PacketByteBuf;
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

    public static class Serializer implements ArgumentSerializer<ContractCommandActionArgumentType, Serializer.Properties> {
        @Override
        public void writePacket(Serializer.Properties properties, PacketByteBuf buf) {}

        @Override
        public Serializer.Properties fromPacket(PacketByteBuf buf) {return new Serializer.Properties();}

        @Override
        public Serializer.Properties getArgumentTypeProperties(ContractCommandActionArgumentType argumentType) {return new Serializer.Properties();}

        @Override
        public void writeJson(Serializer.Properties properties, JsonObject json) {}

        public final class Properties implements ArgumentSerializer.ArgumentTypeProperties<ContractCommandActionArgumentType>{

            @Override
            public ContractCommandActionArgumentType createType(CommandRegistryAccess commandRegistryAccess) {return new ContractCommandActionArgumentType();}

            @Override
            public ArgumentSerializer<ContractCommandActionArgumentType, ?> getSerializer() {return Serializer.this;}
        }
    }
}
