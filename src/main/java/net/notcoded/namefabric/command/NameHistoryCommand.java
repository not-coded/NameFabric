package net.notcoded.namefabric.command;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

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
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.Duration;
//?} elif <1.19 {
/*import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.notcoded.namefabric.utils.HttpAPI;
*///?}

public class NameHistoryCommand {
    //? if >=1.19 {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    //?}

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("namehistory")
                .then(ClientCommandManager.argument("player/uuid", string())
                        .suggests((context, builder) -> suggestMatching(context.getSource().getPlayerNames(), builder))
                        .executes(ctx -> {
                            if(getString(ctx, "player/uuid").length() == 32 || getString(ctx, "player/uuid").length() == 36){
                                return getNamesUUID(ctx.getSource(), getString(ctx, "player/uuid"));
                            } else {
                                return getNamesPlayer(ctx.getSource(), getString(ctx, "player/uuid"));
                            }
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

            //? if >=1.19 {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(response -> source.getClient().send(() -> handleResponse(source, response)));
            //?} elif <1.19 {
             /*handleResponse(source, HttpAPI.get(url));            *///?}


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
