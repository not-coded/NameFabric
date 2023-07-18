package net.notcoded.namefabric.utils;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpAPI {

    public static String userAgent = "Mozilla/5.0 (compatible; namefabric; +https://github.com/not-coded/NameFabric)";

    public static String get(@NotNull String url) {
        try {
            URL apiUrl = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            connection.setRequestProperty("User-Agent", userAgent);
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            return response.toString();
        } catch (Exception ignored) { return null; }
    }
}
