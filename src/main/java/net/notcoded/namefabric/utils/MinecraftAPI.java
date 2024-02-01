package net.notcoded.namefabric.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class MinecraftAPI {
    public static String getUUID(String name) {
        String response = null;

        try {
            response = HttpAPI.get(String.format("https://api.mojang.com/users/profiles/minecraft/%s", name));
        } catch(Exception ignored) { }

        if(response != null && !response.trim().isEmpty())  {
            JsonElement result = JsonParser.parseString(response);

            String uuid = result.getAsJsonObject().get("id").getAsString();

            if (uuid != null && !uuid.trim().isEmpty() && (uuid.length() == 32 || uuid.length() == 36)) {
                return uuid;
            }
        }

        return response;
    }

    public static String getName(String uuid){
        String response = null;

        try {
            response = HttpAPI.get(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s", uuid));
        } catch(Exception ignored) { }
        if(response != null) {
            JsonElement result = JsonParser.parseString(response);

            String name = result.getAsJsonObject().get("name").getAsString();

            if (name != null && !name.trim().isEmpty()) {
                return name;
            }
        }

        return response;
    }
}
