package net.notcoded.namefabric.utils;

import com.google.gson.JsonElement;

import java.util.HashMap;

import static net.notcoded.namefabric.utils.VersionUtil.parseString;

public class MinecraftAPI {

    public static HashMap<String, String> cachedNames = new HashMap<>();

    public static HashMap<String, String> cachedUUIDs = new HashMap<>();

    public static String getUUID(String name) {

        if(cachedUUIDs.get(name.toLowerCase()) != null) return cachedUUIDs.get(name.toLowerCase());

        String response;

        try {
            response = HttpAPI.get(String.format("https://api.mojang.com/users/profiles/minecraft/%s", name));
        } catch(Exception ignored) {
            return null;
        }

        if (response == null || response.trim().isEmpty()) return null;


        JsonElement result = parseString(response);
        String uuid = result.getAsJsonObject().get("id").getAsString();

        if (uuid != null && !uuid.trim().isEmpty() && (uuid.length() == 32 || uuid.length() == 36)) {
            cachedNames.put(uuid, name.toLowerCase());
            return uuid;
        }

        return response;
    }

    public static String getName(String uuid) {
        if (cachedNames.get(uuid.toLowerCase()) != null) return cachedNames.get(uuid.toLowerCase());

        String response;

        try {
            response = HttpAPI.get(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s", uuid));
        } catch (Exception ignored) {
            return null;
        }

        if (response == null || response.trim().isEmpty()) return null;

        JsonElement result = parseString(response);
        String name = result.getAsJsonObject().get("name").getAsString();

        if (name != null && !name.trim().isEmpty()) {
            cachedUUIDs.put(name, uuid.toLowerCase());
            return name;
        }


        return null;
    }
}
