package net.notcoded.namefabric.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

//? if >=1.19 {
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
//?} elif <1.19 {
/*import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
*///?}

import net.notcoded.namefabric.utils.MinecraftAPI;
import org.jetbrains.annotations.NotNull;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.command.CommandSource.suggestMatching;
import static net.notcoded.namefabric.utils.VersionUtil.*;

public class GetUuidCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("getname")
                .then(ClientCommandManager.argument("player/uuid", string())
                        .suggests((context, builder) -> suggestMatching(context.getSource().getPlayerNames(), builder))
                        .executes(GetUuidCommand::execute)));

        dispatcher.register(ClientCommandManager.literal("getuuid")
                .then(ClientCommandManager.argument("player/uuid", string())
                        .suggests((context, builder) -> suggestMatching(context.getSource().getPlayerNames(), builder))
                        .executes(GetUuidCommand::execute)));
    }

    private static int execute(CommandContext<FabricClientCommandSource> ctx) {
        String playerUUID = getString(ctx, "player/uuid");
        if (playerUUID.length() == 32 || playerUUID.length() == 36) {
            return getNamesUUID(ctx.getSource(), playerUUID);
        } else {
            return getUUIDName(ctx.getSource(), playerUUID);
        }
    }

    private static int getNamesUUID(FabricClientCommandSource source, @NotNull String uuid) {
        String name = MinecraftAPI.getName(uuid);
        if (name != null && !name.trim().isEmpty()) {
            sendFeedback(source, "command.getuuid.uuid.success", copyUUIDText(uuid), name);
        } else {
            sendError(source, "command.all.error");
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int getUUIDName(FabricClientCommandSource source, String name) {
        String uuid = MinecraftAPI.getUUID(name);
        if (uuid != null && !uuid.trim().isEmpty()) {
            sendFeedback(source, "command.getuuid.name.success", name, copyUUIDText(uuid));
        } else {
            sendError(source, "command.all.invalid.name");
        }
        return Command.SINGLE_SUCCESS;
    }
}
