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
        if (getString(ctx, "player/uuid").length() == 32 || getString(ctx, "player/uuid").length() == 36) {
            try {
                return getNamesUUID(ctx.getSource(), getString(ctx, "player/uuid"));
            } catch (Exception e) {
                sendError(ctx.getSource(), "command.all.error");
                return Command.SINGLE_SUCCESS;
            }
        } else {
            try {
                return getUuidName(ctx.getSource(), getString(ctx, "player/uuid"));
            } catch (Exception e) {
                sendError(ctx.getSource(), "command.all.error");
                return Command.SINGLE_SUCCESS;
            }
        }
    }

    private static int getNamesUUID(FabricClientCommandSource source, @NotNull String uuid) {
        String name = MinecraftAPI.getName(uuid);
        if (name != null && !name.trim().isEmpty()) {
            try {
                sendFeedback(source, "command.getuuid.uuid.success", copyUUID(uuid), name);
            } catch (Exception e) {
                sendError(source, "command.all.error");
            }
        } else {
            sendError(source, "command.all.error");
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int getUuidName(FabricClientCommandSource source, String name) {
        String uuid = MinecraftAPI.getUUID(name);
        if (uuid != null && !uuid.trim().isEmpty()) {
            try {
                sendFeedback(source, "command.getuuid.name.success", name, copyUUID(uuid));
            } catch (Exception e) {
                sendError(source, "command.all.error");
            }
        } else {
            sendError(source, "command.all.invalid.name");
        }
        return Command.SINGLE_SUCCESS;
    }
}
