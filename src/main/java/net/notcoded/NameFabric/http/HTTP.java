package net.notcoded.namefabric.http;

import java.net.http.HttpClient;

public class HTTP {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final int DURATION = 5; // seconds

    /*

    does not work, add for namefabric 1.0.1

    private static String response;

    public static String sendHTTP(FabricClientCommandSource source, String url) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(DURATION))
                .GET()
                .build();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    source.getClient().send(() -> {
                        HTTP.response = response;
                    });
                });
        return HTTP.response;
    }

     */
}
