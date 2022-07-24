package net.notcoded.namefabric.utilities;

import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import java.net.http.HttpClient;
import java.util.Base64;

public class Utilities {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final int DURATION = 5; // seconds

    public static byte[] Decode64(String base64){
        return(Base64.getDecoder().decode(base64));
        // new String(base64)
    }

    public static String getPlayerName(FabricClientCommandSource source, String uuid) {
        if (source.getClient().isInSingleplayer()) {
            ServerPlayerEntity playerEntity = source.getClient().getServer().getPlayerManager().getPlayer(uuid);
            if (playerEntity == null) {
                return(uuid);
            } else {
                return(playerEntity.getEntityName());
            }
        } else {
            PlayerListEntry playerListEntry = source.getClient().getNetworkHandler().getPlayerListEntry(uuid);
            if (playerListEntry == null) {
                return(uuid);
            } else {
                return(playerListEntry.getProfile().getName());
            }
        }
    }

    public static String getPlayerUUID(FabricClientCommandSource source, String name) {
        if (source.getClient().isInSingleplayer()) {
            ServerPlayerEntity playerEntity = source.getClient().getServer().getPlayerManager().getPlayer(name);
            if (playerEntity == null) {
                return(name);
            } else {
                return(playerEntity.getUuidAsString());
            }
        } else {
            PlayerListEntry playerListEntry = source.getClient().getNetworkHandler().getPlayerListEntry(name);
            if (playerListEntry == null) {
                return(name);
            } else {
                return(playerListEntry.getProfile().getId().toString());
            }
        }
    }

    public static byte[] UrlDecode64(String base64){
        return(Base64.getUrlDecoder().decode(base64));
    }

    public static String Encode64(byte[] base64){
        return(new String(Base64.getEncoder().encode(base64)));
        // new String(base64)
    }

    public static String UrlEncode64(byte[] base64){
        return(new String(Base64.getUrlEncoder().encode(base64)));
    }
}
