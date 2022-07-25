package net.notcoded.namefabric.command;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.*;
import net.notcoded.namefabric.utilities.Utilities;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.command.CommandSource.suggestMatching;

public class getskin {

    private static String PlayerName;
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final int DURATION = 5; // seconds
    private static boolean isUsingPlayerName = false;

    private static boolean isSlim = false;
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("getskin")
                .then(ClientCommandManager.argument("player/uuid", string())
                        .suggests((context, builder) -> suggestMatching(context.getSource().getPlayerNames(), builder))
                        .executes(ctx -> {
                            if(getString(ctx, "player/uuid").length() == 32 || getString(ctx, "player/uuid").length() == 36){
                                try {
                                    return getSkinsUUID(ctx.getSource(), getString(ctx, "player/uuid"));
                                } catch (Exception e) {
                                    ctx.getSource().getPlayer().sendMessage(new TranslatableText("command.all.error"), false);
                                }
                            } else {
                                try {
                                    return getSkinsPlayer(ctx.getSource(), getString(ctx, "player/uuid"));
                                } catch (Exception e) {
                                    ctx.getSource().getPlayer().sendMessage(new TranslatableText("command.all.error"), false);
                                }
                            }
                            return Command.SINGLE_SUCCESS;
                        })));
    }

    private static int getSkinsUUID(FabricClientCommandSource source, String uuid) {
        ClientPlayerEntity player = source.getPlayer();


        if(uuid.length() == 32 || uuid.length() == 36 || isUsingPlayerName){ //yes I know if you put an uuid with the same length as 32 or 36 it will cause an error
            HttpRequest request = HttpRequest.newBuilder(URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid))
                    .timeout(Duration.ofSeconds(DURATION))
                    .GET()
                    .build();
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(response -> {
                        source.getClient().send(() -> {
                            String skinurl = null;
                            JsonElement result = JsonParser.parseString(response);
                            if(!isUsingPlayerName){
                                JsonElement result1 = JsonParser.parseString(response);
                                PlayerName = result1.getAsJsonObject().get("name").getAsString();
                            }
                            try {
                                if (result.getAsJsonObject().getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString() != null) {
                                    skinurl = new String(Utilities.Decode64(result.getAsJsonObject().getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString()));
                                }
                            } catch (Exception e) {
                                player.sendMessage(new TranslatableText("command.all.error"), false);
                            }
                            try{
                                if(skinurl != null && !skinurl.equals("")){
                                    JsonElement result2 = JsonParser.parseString(skinurl);
                                    skinurl = result2.getAsJsonObject().get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();
                                }
                            } catch (Exception e){
                                player.sendMessage(new TranslatableText("command.all.error"), false);
                            }

                            String finalSkinurl = skinurl;
                            Text skinText = new LiteralText(finalSkinurl).styled(style -> style
                                    .withUnderline(true)
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("Click to open the link!")))
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, finalSkinurl))
                            );
                            player.sendMessage(new TranslatableText("command.getskin.success", PlayerName, skinText), false);
                        });
                    });


        } else {
            player.sendMessage(new TranslatableText("command.all.invalid.uuid"), false);
        }
        PlayerName = null;
        isUsingPlayerName = false;
        return Command.SINGLE_SUCCESS;
    }

    public static int getSkinsPlayer(FabricClientCommandSource source, String name){
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
                            PlayerName = name;
                            isUsingPlayerName = true;
                            getSkinsUUID(source, uuid);
                        } catch (Exception e) {
                            source.getPlayer().sendMessage(new TranslatableText("command.all.error"), false);
                        }
                    } else{
                        source.getPlayer().sendMessage(new TranslatableText("command.all.invalid.name"), false);
                    }
                }));
        return Command.SINGLE_SUCCESS;
    }
}
