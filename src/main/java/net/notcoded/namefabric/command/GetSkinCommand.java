package net.notcoded.namefabric.command;

import com.google.gson.JsonElement;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

import net.notcoded.namefabric.utils.MinecraftAPI;
import java.net.URI;
import java.time.Duration;
import java.util.Base64;
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
//?} elif <1.19 {
/*import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.notcoded.namefabric.utils.HttpAPI;
*///?}

public class GetSkinCommand {

    private static String playerName;
    private static boolean isUsingPlayerName = false;

    //? if >=1.19 {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    //?}
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("getskin")
                .then(ClientCommandManager.argument("player/uuid", string())
                        .suggests((context, builder) -> suggestMatching(context.getSource().getPlayerNames(), builder))
                        .executes(ctx -> {
                            if(getString(ctx, "player/uuid").length() == 32 || getString(ctx, "player/uuid").length() == 36){
                                try {
                                    return getSkinsUUID(ctx.getSource(), getString(ctx, "player/uuid"));
                                } catch (Exception e) {
                                    sendError(ctx.getSource(), "command.all.error");
                                }
                            } else {
                                try {
                                    return getSkinsPlayer(ctx.getSource(), getString(ctx, "player/uuid"));
                                } catch (Exception e) {
                                    sendError(ctx.getSource(), "command.all.error");
                                }
                            }
                            return Command.SINGLE_SUCCESS;
                        })));
    }


    private static void handleResponse(FabricClientCommandSource source, String response) {
        String skinurl = null;
        JsonElement result = parseString(response);
        if(!isUsingPlayerName){
            playerName = result.getAsJsonObject().get("name").getAsString();
        }
        try {
            if (result.getAsJsonObject().getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString() != null) {
                skinurl = new String(Base64.getDecoder().decode(result.getAsJsonObject().getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString()));
            }
        } catch (Exception e) {
            sendError(source, "command.all.error");
            return;
        }
        try{
            if(skinurl != null && !skinurl.trim().isEmpty()){
                JsonElement result2 = parseString(skinurl);
                skinurl = result2.getAsJsonObject().get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();
            }
        } catch (Exception e){
            sendError(source, "command.all.error");
            return;
        }

        String finalSkinURL = skinurl;

        if(finalSkinURL == null) {
            sendError(source, "command.all.error");
            return;
        }

        sendFeedback(source, "command.getskin.success", playerName, webLink(finalSkinURL));
    }

    private static int getSkinsUUID(FabricClientCommandSource source, String uuid) {
        if(uuid.length() == 32 || uuid.length() == 36 || isUsingPlayerName) {
            String url = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid;

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
        }
        playerName = null;
        isUsingPlayerName = false;
        return Command.SINGLE_SUCCESS;
    }
    public static int getSkinsPlayer(FabricClientCommandSource source, String name) {
        String uuid = MinecraftAPI.getUUID(name);
        if(uuid != null && !uuid.trim().isEmpty()){
            try {
                playerName = name;
                isUsingPlayerName = true;
                getSkinsUUID(source, uuid);
            } catch (Exception e) {
                sendError(source, "command.all.error");
            }
        } else{
            sendError(source, "command.all.invalid.name");
        }
        return Command.SINGLE_SUCCESS;
    }
}
