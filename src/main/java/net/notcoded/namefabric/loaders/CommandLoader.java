package net.notcoded.namefabric.loaders;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.notcoded.namefabric.command.*;

public class CommandLoader {
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        NameHistoryCommand.register(dispatcher);
        GetSkinCommand.register(dispatcher);
        GetCapeCommand.register(dispatcher);
        GetUuidCommand.register(dispatcher);
    }
}
