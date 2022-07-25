package net.notcoded.namefabric.command;

import com.google.gson.*;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.*;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.command.CommandSource.suggestMatching;

public class getuuid {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final int DURATION = 5; // seconds
    // private static String PlayerName;
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("getuuid")
                .then(ClientCommandManager.argument("player/uuid", string())
                        .suggests((context, builder) -> suggestMatching(context.getSource().getPlayerNames(), builder))
                        .executes(ctx -> {
                            if(getString(ctx, "player/uuid").length() == 32 || getString(ctx, "player/uuid").length() == 36){
                                try {
                                    return getNamesUUID(ctx.getSource(), getString(ctx, "player/uuid"));
                                } catch (Exception e) {
                                    ctx.getSource().getPlayer().sendMessage(new TranslatableText("command.all.error"), false);
                                    return Command.SINGLE_SUCCESS;
                                }
                            } else {
                                try {
                                    return getUuidName(ctx.getSource(), getString(ctx, "player/uuid"));
                                } catch (Exception e) {
                                    ctx.getSource().getPlayer().sendMessage(new TranslatableText("command.all.error"), false);
                                    return Command.SINGLE_SUCCESS;
                                }
                            }
                        })));
    }



    private static int getNamesUUID(FabricClientCommandSource source, String uuid) {
        ClientPlayerEntity client = source.getPlayer();
        if(uuid.length() == 32 || uuid.length() == 36){
            HttpRequest request = HttpRequest.newBuilder(URI.create("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid))
                    .timeout(Duration.ofSeconds(DURATION))
                    .GET()
                    .build();
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(response -> source.getClient().send(() -> {
                        JsonElement result = JsonParser.parseString(response);
                        String name = null;
                        try{
                            if(result.getAsJsonObject().get("name").getAsString() != null){
                                name = result.getAsJsonObject().get("name").getAsString();
                            }
                        } catch (Exception ignored) {
                        }
                        if(name != null && !name.equals("")){
                            try {
                                Text uuidText = new LiteralText(uuid).styled(style -> style
                                        .withUnderline(true)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("Click to copy the uuid!")))
                                        .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, uuid))
                                );
                                source.getPlayer().sendMessage(new TranslatableText("command.getuuid.uuid.success", uuidText, name), false);
                            } catch (Exception e) {
                                source.getPlayer().sendMessage(new TranslatableText("command.all.error"), false);
                            }
                        } else{
                            source.getPlayer().sendMessage(new TranslatableText("command.all.error"), false);
                        }
                    }));
        } else{
            client.sendMessage(new TranslatableText("command.all.invalid.uuid"), false);
        }
        return Command.SINGLE_SUCCESS;
    }


    public static int getUuidName(FabricClientCommandSource source, String name){
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
                            String finalUuid = uuid;
                            Text uuidText = new LiteralText(uuid).styled(style -> style
                                    .withUnderline(true)
                                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("Click to copy the uuid!")))
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, finalUuid))
                            );
                            source.getPlayer().sendMessage(new TranslatableText("command.getuuid.name.success", name, uuidText), false);

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
