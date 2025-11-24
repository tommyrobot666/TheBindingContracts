package lommie.thebindingcontracts.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import lommie.thebindingcontracts.TheBindingContracts;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

public class ModCommands {
    public static void register(){
        ArgumentTypeRegistry.registerArgumentType(
                Identifier.of(TheBindingContracts.MOD_ID,"registered_term"),
                RegisteredTermArgumentType.class,
                new RegisteredTermArgumentType.Serializer()
        );
        ArgumentTypeRegistry.registerArgumentType(
                Identifier.of(TheBindingContracts.MOD_ID,"contracts_command_actions"),
                ContractCommandActionArgumentType.class,
                new ContractCommandActionArgumentType.Serializer()
        );
        registerEvent();
    }

    public static void registerEvent(){
        CommandRegistrationCallback.EVENT.register(ModCommands::commandRegisterEvent);
    }

    private static void commandRegisterEvent(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("contracts")
                .then(CommandManager.argument("id", new UuidArgumentType())
                //TODO: allow only valid contract id. suggest one in hand, then others in inventory
                    .then(CommandManager.argument("action", new ContractCommandActionArgumentType())
                            .suggests(new ContractCommandActionArgumentType())
                            .then(CommandManager.argument("term", new RegisteredTermArgumentType())
                                    .suggests(new RegisteredTermArgumentType())
                                    .executes(commandContext -> 1)) //TODO: add ContractCommand::TermModification
                            .then(CommandManager.argument("player", EntityArgumentType.player())
                                    //TODO: suggest ids and player names that have signed the contract
                                    .suggests((commandContext, suggestionsBuilder) -> suggestionsBuilder.buildFuture())
                                    .executes(commandContext -> 1)) //TODO: add ContractCommand::SignersModification
                            .then(CommandManager.argument("bool", BoolArgumentType.bool())
                                    .executes(commandContext -> 1))) //TODO: add ContractCommand::StateModification
                ));
    }


}
