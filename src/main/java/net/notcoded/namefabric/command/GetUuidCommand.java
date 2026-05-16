package net.notcoded.namefabric.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

//? if >=1.19 {
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
//?} elif <1.19 {
/*import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
*///?}

import net.notcoded.namefabric.utils.CommandUtil;
import net.notcoded.namefabric.utils.MinecraftAPI;
import org.jetbrains.annotations.NotNull;
import static net.notcoded.namefabric.utils.VersionUtil.*;

public class GetUuidCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        CommandUtil.registerPlayerOrUuid(dispatcher, "getname", GetUuidCommand::getNamesUUID, GetUuidCommand::getUUIDName);
        CommandUtil.registerPlayerOrUuid(dispatcher, "getuuid", GetUuidCommand::getNamesUUID, GetUuidCommand::getUUIDName);
    }

    private static int getNamesUUID(FabricClientCommandSource source, @NotNull String uuid) {
        String name = MinecraftAPI.getName(uuid);
        if (name != null && !name.trim().isEmpty()) {
            sendFeedback(source, "command.getuuid.uuid.success", formatUUID(uuid), copyNameText(name));
        } else {
            sendError(source, "command.all.error");
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int getUUIDName(FabricClientCommandSource source, String name) {
        String uuid = MinecraftAPI.getUUID(name);
        if (uuid != null && !uuid.trim().isEmpty()) {
            sendFeedback(source, "command.getuuid.name.success", name, copyUUIDText(formatUUID(uuid)));
        } else {
            sendError(source, "command.all.invalid.name");
        }
        return Command.SINGLE_SUCCESS;
    }

    private static String formatUUID(String uuid) {
        String compactUUID = uuid.replace("-", "");
        if (compactUUID.length() != 32) return uuid;

        return compactUUID.substring(0, 8) + "-"
                + compactUUID.substring(8, 12) + "-"
                + compactUUID.substring(12, 16) + "-"
                + compactUUID.substring(16, 20) + "-"
                + compactUUID.substring(20);
    }
}
