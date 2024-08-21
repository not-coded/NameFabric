package net.notcoded.namefabric.loaders;

import com.mojang.brigadier.CommandDispatcher;

//? if >=1.19 {
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
//?} elif <1.19 {
/*import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
*///?}

import net.notcoded.namefabric.command.*;

public class CommandLoader {
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        GetSkinCommand.register(dispatcher);
        GetCapeCommand.register(dispatcher);
        GetUuidCommand.register(dispatcher);
        NameHistoryCommand.register(dispatcher);
    }

    //? if >=1.19 {
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess) {
        registerCommands(dispatcher);
    }
    //?}
}
