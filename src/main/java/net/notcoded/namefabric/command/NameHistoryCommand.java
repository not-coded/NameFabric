package net.notcoded.namefabric.command;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

import net.notcoded.namefabric.utils.HttpAPI;
import net.notcoded.namefabric.utils.MinecraftAPI;
import java.util.ArrayList;
import java.util.List;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.command.CommandSource.suggestMatching;
import static net.notcoded.namefabric.utils.VersionUtil.*;

//? if >=1.19 {
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
//?} elif <1.19 {
/*import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
*///?}

public class NameHistoryCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("namehistory")
                .then(ClientCommandManager.argument("player/uuid", string())
                        .suggests((context, builder) -> suggestMatching(context.getSource().getPlayerNames(), builder))
                        .executes(ctx -> {
                            String playerOrUUID = getString(ctx, "player/uuid");
                            if(playerOrUUID.length() == 32 || playerOrUUID.length() == 36){
                                return getNamesUUID(ctx.getSource(), playerOrUUID);
                            }
                            return getNamesPlayer(ctx.getSource(), playerOrUUID);
                        })));
    }

    private static void handleResponse(FabricClientCommandSource source, String response) {
        JsonElement result = parseString(response);
        if (result.isJsonArray()) {
            JsonArray array = result.getAsJsonArray();
            List<String> names = new ArrayList<>();
            array.forEach(name -> names.add(name.getAsJsonObject().get("name").getAsString().replaceAll("\uFF0D", "[hidden]")));
            String player = names.get(names.size() - 1);
            sendFeedback(source, "command.namehistory.success", player, String.join(", ", names));
        } else {
            sendError(source, "command.all.error");
        }
    }

    private static int getNamesUUID(FabricClientCommandSource source, String uuid) {
        if(uuid.length() == 32 || uuid.length() == 36) {
            String url = String.format("https://laby.net/api/user/%s/get-names", uuid);
            handleResponse(source, HttpAPI.get(url));

        } else {
            sendError(source, "command.all.invalid.uuid");
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }


    public static int getNamesPlayer(FabricClientCommandSource source, String name){
        String uuid = MinecraftAPI.getUUID(name);
        if(uuid != null && !uuid.trim().isEmpty()){
            getNamesUUID(source, uuid);
        } else{
            sendError(source, "command.all.invalid.name");
        }
        return Command.SINGLE_SUCCESS;
    }
}
