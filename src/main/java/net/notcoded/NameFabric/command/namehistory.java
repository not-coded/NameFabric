package net.notcoded.namefabric.command;

import com.google.gson.*;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;


import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.command.CommandSource.suggestMatching;

public class namehistory {

    // Variables and fetchNameHistory function are copied/modified from https://github.com/Earthcomputer/clientcommands/blob/fabric/src/main/java/net/earthcomputer/clientcommands/command/PlayerInfoCommand.java
    // Credit to EarthComputer, xpple and haykam821 for writing this amazing code!
    private static final Map<String, List<String>> cacheByName = new HashMap<>();
    private static final Map<String, List<String>> cacheByUuid = new HashMap<>();
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final int DURATION = 5; // seconds
    // private static String PlayerName;
    private static boolean isUsingPlayerName = false;
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("namehistory")
                .then(ClientCommandManager.argument("player/uuid", string())
                        .suggests((context, builder) -> suggestMatching(context.getSource().getPlayerNames(), builder))
                        .executes(ctx -> {
                            if(getString(ctx, "player/uuid").length() == 32 || getString(ctx, "player/uuid").length() == 36){
                                try {
                                    return getNamesUUID(ctx.getSource(), getString(ctx, "player/uuid"));
                                } catch (Exception ignored) {
                                    ctx.getSource().getPlayer().sendMessage(Text.translatable("command.all.error"));
                                    return Command.SINGLE_SUCCESS;
                                }
                            } else {
                                try {
                                    return getNamesPlayer(ctx.getSource(), getString(ctx, "player/uuid"));
                                    } catch (JsonIOException e) {
                                    ctx.getSource().getPlayer().sendMessage(Text.translatable("command.all.error"));
                                    return Command.SINGLE_SUCCESS;
                                }
                            }
                        })));
    }



    private static int getNamesUUID(FabricClientCommandSource source, String uuid) {
        ClientPlayerEntity client = source.getPlayer();
        if(uuid.length() == 32 || uuid.length() == 36 || isUsingPlayerName){
            HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.mojang.com/user/profiles/" + uuid + "/names"))
                    .timeout(Duration.ofSeconds(DURATION))
                    .GET()
                    .build();
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(response -> source.getClient().send(() -> {
                        JsonElement result = JsonParser.parseString(response);
                        if (result.isJsonArray()) {
                            JsonArray array = result.getAsJsonArray();
                            List<String> names = new ArrayList<>();
                            array.forEach(name -> names.add(name.getAsJsonObject().get("name").getAsString()));
                            String player = names.get(names.size() - 1);
                            cacheByName.put(player, names);
                            cacheByUuid.put(uuid, names);
                            client.sendMessage(Text.translatable("command.namehistory.success", player, String.join(", ", names)));
                        } else {
                            client.sendMessage(Text.translatable("command.all.error"));
                        }
                    }));
        } else{
            isUsingPlayerName = false;
            client.sendMessage(Text.translatable("command.all.invalid.uuid"));
            return Command.SINGLE_SUCCESS;
        }
        return Command.SINGLE_SUCCESS;
    }


    public static int getNamesPlayer(FabricClientCommandSource source, String name){
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.mojang.com/users/profiles/minecraft/" + name))
                .timeout(Duration.ofSeconds(DURATION))
                .GET()
                .build();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> source.getClient().send(() -> {
                    JsonElement result = JsonParser.parseString(response);
                    String uuid = null;
                    try{
                        if(result.getAsJsonObject().get("id").getAsString() != null){
                            uuid = result.getAsJsonObject().get("id").getAsString();
                        }
                    } catch (Exception ignored) {
                    }
                    if(uuid != null && !uuid.equals("")){
                        try {
                            isUsingPlayerName = true;
                            getNamesUUID(source, uuid);
                        } catch (Exception e) {
                            source.getPlayer().sendMessage(Text.translatable("command.all.error"));
                        }
                    } else{
                        source.getPlayer().sendMessage(Text.translatable("command.all.invalid.name"));
                    }
                }));
        return Command.SINGLE_SUCCESS;
    }
}
