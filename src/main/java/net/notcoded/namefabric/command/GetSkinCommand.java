package net.notcoded.namefabric.command;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.brigadier.Command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.*;
import net.notcoded.namefabric.utils.HttpAPI;
import net.notcoded.namefabric.utils.MinecraftAPI;

import java.util.Base64;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.command.CommandSource.suggestMatching;

public class GetSkinCommand {

    private static String PlayerName;
    private static boolean isUsingPlayerName = false;
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("getskin")
                .then(ClientCommandManager.argument("player/uuid", string())
                        .suggests((context, builder) -> suggestMatching(context.getSource().getPlayerNames(), builder))
                        .executes(ctx -> {
                            if(getString(ctx, "player/uuid").length() == 32 || getString(ctx, "player/uuid").length() == 36){
                                try {
                                    return getSkinsUUID(ctx.getSource(), getString(ctx, "player/uuid"));
                                } catch (Exception e) {
                                    ctx.getSource().sendError(new TranslatableText("command.all.error"));
                                }
                            } else {
                                try {
                                    return getSkinsPlayer(ctx.getSource(), getString(ctx, "player/uuid"));
                                } catch (Exception e) {
                                    ctx.getSource().sendError(new TranslatableText("command.all.error"));
                                }
                            }
                            return Command.SINGLE_SUCCESS;
                        })));
    }

    private static int getSkinsUUID(FabricClientCommandSource source, String uuid) {
        if(uuid.length() == 32 || uuid.length() == 36 || isUsingPlayerName) {

            String skinurl = null;
            JsonElement result = JsonParser.parseString(HttpAPI.get("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid));
            try {
                if (!isUsingPlayerName) {
                    PlayerName = result.getAsJsonObject().get("name").getAsString();
                }

                if (result.getAsJsonObject().getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString() != null) {
                    skinurl = new String(Base64.getDecoder().decode(result.getAsJsonObject().getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString()));
                }

                if (skinurl != null && skinurl.trim().length() != 0) {
                    JsonElement result2 = JsonParser.parseString(skinurl);
                    skinurl = result2.getAsJsonObject().get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();
                }
            } catch (Exception e) {
                source.sendError(new TranslatableText("command.all.error"));
            }

            String finalSkinurl = skinurl;
            Text skinText = new LiteralText(finalSkinurl).styled(style -> style
                    .withUnderline(true)
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("click.open.link")))
                    .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, finalSkinurl))
            );
            source.sendFeedback(new TranslatableText("command.getskin.success", PlayerName, skinText));


        } else {
            source.sendError(new TranslatableText("command.all.invalid.uuid"));
        }
        PlayerName = null;
        isUsingPlayerName = false;
        return Command.SINGLE_SUCCESS;
    }
    public static int getSkinsPlayer(FabricClientCommandSource source, String name) {
        String uuid = MinecraftAPI.getUUID(name);
        if(uuid != null && uuid.trim().length() != 0){
            try {
                PlayerName = name;
                isUsingPlayerName = true;
                getSkinsUUID(source, uuid);
            } catch (Exception e) {
                source.sendError(new TranslatableText("command.all.error"));
            }
        } else{
            source.sendError(new TranslatableText("command.all.invalid.name"));
        }
        return Command.SINGLE_SUCCESS;
    }
}
