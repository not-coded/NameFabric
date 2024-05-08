package net.notcoded.namefabric.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.HashMap;

public class MinecraftAPI {

    public static HashMap<String, String> cachedNames = new HashMap<>();

    public static HashMap<String, String> cachedUUIDs = new HashMap<>();

    public static String getUUID(String name) {

        if(cachedUUIDs.get(name) != null) return cachedUUIDs.get(name);

        String response = null;

        try {
            response = HttpAPI.get(String.format("https://api.mojang.com/users/profiles/minecraft/%s", name));
        } catch(Exception ignored) { }

        if(response != null && !response.trim().isEmpty())  {
            JsonElement result = new JsonParser().parse(response);

            String uuid = result.getAsJsonObject().get("id").getAsString();

            if (uuid != null && !uuid.trim().isEmpty() && (uuid.length() == 32 || uuid.length() == 36)) {
                cachedNames.put(uuid, name);
                return uuid;
            }
        }

        return response;
    }

    public static String getName(String uuid){

        if(cachedNames.get(uuid) != null) return cachedNames.get(uuid);

        String response = null;

        try {
            response = HttpAPI.get(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s", uuid));
        } catch(Exception ignored) { }
        if(response != null) {
            JsonElement result = new JsonParser().parse(response);

            String name = result.getAsJsonObject().get("name").getAsString();

            if (name != null && !name.trim().isEmpty()) {
                cachedUUIDs.put(name, uuid);
                return name;
            }
        }

        return response;
    }
}
