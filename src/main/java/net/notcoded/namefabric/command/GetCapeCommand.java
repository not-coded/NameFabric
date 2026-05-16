package net.notcoded.namefabric.command;

import com.google.gson.JsonObject;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

import net.notcoded.namefabric.utils.CapeUtil;
import net.notcoded.namefabric.utils.HttpAPI;
import net.notcoded.namefabric.utils.MinecraftAPI;
import net.notcoded.namefabric.utils.CommandUtil;
import org.jetbrains.annotations.NotNull;

import static net.notcoded.namefabric.utils.VersionUtil.*;

//? if >=1.19 {
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
//?} elif <1.19 {
/*import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
*///?}

public class GetCapeCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        CommandUtil.registerPlayerOrUuid(dispatcher, "getcape", GetCapeCommand::getCapesUUID, GetCapeCommand::getCapesPlayer);
    }

    private static void handleResponse(FabricClientCommandSource source, String response, String fallbackName) {
        if (response == null || response.trim().isEmpty()) {
            sendError(source, "command.all.error");
            return;
        }

        try {
            JsonObject result = parseString(response).getAsJsonObject();
            String playerName = result.has("name") ? result.get("name").getAsString() : fallbackName;
            String capeUrl = CapeUtil.extractCapeUrl(result);
            String cape = CapeUtil.identifyCape(capeUrl);

            if(cape == null){
                sendFeedback(source, "command.getcape.no_cape", playerName);
                return;
            }

            sendFeedback(source, "command.getcape.success", playerName, cape);
        } catch (Exception e) {
            sendError(source, "command.all.error");
        }
    }

    private static int getCapesUUID(@NotNull FabricClientCommandSource source, @NotNull String uuid) {
        if(uuid.length() == 32 || uuid.length() == 36){
            String url = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid;
            handleResponse(source, HttpAPI.get(url), uuid);
        } else {
            sendError(source, "command.all.invalid.uuid");
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int getCapesPlayer(FabricClientCommandSource source, @NotNull String name) {
        String uuid = MinecraftAPI.getUUID(name);

        if (uuid != null && (uuid.length() == 32 || uuid.length() == 36)) {
            String url = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid;
            handleResponse(source, HttpAPI.get(url), name);
        } else {
            sendError(source, "command.all.invalid.name");
        }
        return Command.SINGLE_SUCCESS;
    }
}
