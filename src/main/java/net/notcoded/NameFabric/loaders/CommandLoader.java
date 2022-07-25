package net.notcoded.namefabric.loaders;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.notcoded.namefabric.command.*;

public class CommandLoader {
    public static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        namehistory.register(dispatcher);
        getskin.register(dispatcher);
        getcape.register(dispatcher);
        getuuid.register(dispatcher);
    }
}
