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
                Identifier.of(TheBindingContracts.MOD_ID,"contracts_command_term_actions"),
                ContractCommandTermActionArgumentType.class,
                new ContractCommandTermActionArgumentType.Serializer()
        );
        ArgumentTypeRegistry.registerArgumentType(
                Identifier.of(TheBindingContracts.MOD_ID,"contracts_command_signer_actions"),
                ContractCommandSignerActionArgumentType.class,
                new ContractCommandSignerActionArgumentType.Serializer()
        );
        ArgumentTypeRegistry.registerArgumentType(
                Identifier.of(TheBindingContracts.MOD_ID,"contracts_command_state_actions"),
                ContractCommandStateActionArgumentType.class,
                new ContractCommandStateActionArgumentType.Serializer()
        );
        registerEvent();
    }

    public static void registerEvent(){
        CommandRegistrationCallback.EVENT.register(ModCommands::commandRegisterEvent);
    }

    @SuppressWarnings("unused")
    private static void commandRegisterEvent(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("contracts")
                .then(CommandManager.argument("id", new UuidArgumentType())
                        .suggests(new ContractIdSuggestions())
                        .then(CommandManager.argument("term_action", new ContractCommandTermActionArgumentType())
                                .suggests(new ContractCommandTermActionArgumentType())
                                .then(CommandManager.argument("term", new RegisteredTermArgumentType())
                                        .suggests(new RegisteredTermArgumentType())
                                        .executes(ContractCommand::TermModification)
                                )
                        )
                        .then(CommandManager.argument("signer_action", new ContractCommandSignerActionArgumentType())
                                .then(CommandManager.argument("player", EntityArgumentType.player())
                                        .suggests(new ContractSignersSuggestions())
                                        .executes(ContractCommand::SignersModification)
                                )
                        )
                        .then(CommandManager.argument("state_action", new ContractCommandStateActionArgumentType())
                                .then(CommandManager.argument("bool", BoolArgumentType.bool())
                                        .executes(ContractCommand::StateModification)
                                )
                        )
                )
        );
    }


}
