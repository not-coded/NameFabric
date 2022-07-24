package net.notcoded.namefabric.http;

import java.net.http.HttpClient;

public class HTTP {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36";
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
