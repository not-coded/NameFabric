package net.notcoded.namefabric.loaders;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.notcoded.namefabric.command.*;

public class CommandLoader {
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        GetSkinCommand.register(dispatcher);
        GetCapeCommand.register(dispatcher);
        GetUuidCommand.register(dispatcher);
        NameHistoryCommand.register(dispatcher);
    }
}
