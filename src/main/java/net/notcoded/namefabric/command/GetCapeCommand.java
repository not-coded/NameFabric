package net.notcoded.namefabric.command;

import com.google.gson.JsonElement;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;

import net.notcoded.namefabric.utils.MinecraftAPI;
import org.jetbrains.annotations.NotNull;
import java.net.URI;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.command.CommandSource.suggestMatching;
import static net.notcoded.namefabric.utils.VersionUtil.*;

//? if >=1.19 {
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
//?} elif <1.19 {
/*import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.notcoded.namefabric.utils.HttpAPI;
*///?}

public class GetCapeCommand {
    //? if >=1.19 {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    //?}
    private static String playerName;
    private static boolean isUsingPlayerName = false;

    public static HashMap<String, String> capes = new HashMap<>();

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("getcape")
                .then(ClientCommandManager.argument("player/uuid", string())
                        .suggests((context, builder) -> suggestMatching(context.getSource().getPlayerNames(), builder))
                        .executes(ctx -> {
                            if(getString(ctx, "player/uuid").length() == 32 || getString(ctx, "player/uuid").length() == 36){
                                try {
                                    return getCapesUUID(ctx.getSource(), getString(ctx, "player/uuid"));
                                } catch (Exception e) {
                                    sendError(ctx.getSource(), "command.all.error");
                                    return 0;
                                }
                            } else {
                                try {
                                    return getCapesPlayer(ctx.getSource(), getString(ctx, "player/uuid"));
                                } catch (Exception e) {
                                    sendError(ctx.getSource(), "command.all.error");
                                    return 0;
                                }
                            }
                        })));
    }

    private static String identifyCape(@NotNull String url) {

        System.out.println(url);

        String cape;
        for (int i = 0; i < capes.size(); i++){
            cape = capes.get(url);
            if(cape != null && !cape.trim().isEmpty()) return cape;
        }

        return "No";
    }


    private static void handleResponse(FabricClientCommandSource source, String response) {
        String capeurl = "";
        JsonElement result = parseString(response);
        if(!isUsingPlayerName){
            playerName = result.getAsJsonObject().get("name").getAsString();
        }
        try {
            if (result.getAsJsonObject().getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString() != null) {
                capeurl = new String(Base64.getDecoder().decode(result.getAsJsonObject().getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString()));
            }
        } catch (Exception e) {
            sendError(source, "command.all.error");
        }

        try{
            if(!capeurl.trim().isEmpty()){
                JsonElement result2 = parseString(capeurl);
                capeurl = result2.getAsJsonObject().get("textures").getAsJsonObject().get("CAPE").getAsJsonObject().get("url").getAsString();
            }
        } catch (Exception ignored) {}

        sendFeedback(source,"command.getcape.success", playerName, identifyCape(capeurl));
    }

    private static int getCapesUUID(@NotNull FabricClientCommandSource source, @NotNull String uuid) {
        if(uuid.length() == 32 || uuid.length() == 36 || isUsingPlayerName){
            String url = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid;

            //? if >=1.19 {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(response -> source.getClient().send(() -> handleResponse(source, response)));
            //?} elif <1.19 {
            /*handleResponse(source, HttpAPI.get(url));            *///?}

        } else {
            sendError(source, "command.all.invalid.uuid");
        }
        playerName = null;
        isUsingPlayerName = false;
        return Command.SINGLE_SUCCESS;
    }

    public static int getCapesPlayer(FabricClientCommandSource source, @NotNull String name) {
        String uuid = MinecraftAPI.getUUID(name);

        if (uuid != null && (uuid.length() == 32 || uuid.length() == 36)) {
            try {
                playerName = name;
                isUsingPlayerName = true;
                getCapesUUID(source, uuid);
            } catch (Exception e) {
                sendError(source, "command.all.error");
            }
        } else {
            sendError(source, "command.all.invalid.name");
        }
        return Command.SINGLE_SUCCESS;
    }

    static {
        capes.put("http://textures.minecraft.net/texture/2340c0e03dd24a11b15a8b33c2a7e9e32abb2051b2481d0ba7defd635ca7a933", "Migrator");
        capes.put("http://textures.minecraft.net/texture/e7dfea16dc83c97df01a12fabbd1216359c0cd0ea42f9999b6e97c584963e980", "MineCon 2016");
        capes.put("http://textures.minecraft.net/texture/b0cc08840700447322d953a02b965f1d65a13a603bf64b17c803c21446fe1635", "MineCon 2015");
        capes.put("http://textures.minecraft.net/texture/153b1a0dfcbae953cdeb6f2c2bf6bf79943239b1372780da44bcbb29273131da", "MineCon 2013");
        capes.put("http://textures.minecraft.net/texture/a2e8d97ec79100e90a75d369d1b3ba81273c4f82bc1b737e934eed4a854be1b6", "MineCon 2012");
        capes.put("http://textures.minecraft.net/texture/953cac8b779fe41383e675ee2b86071a71658f2180f56fbce8aa315ea70e2ed6", "MineCon 2011");
        capes.put("http://textures.minecraft.net/texture/17912790ff164b93196f08ba71d0e62129304776d0f347334f8a6eae509f8a56", "Realms Mapmaker");
        capes.put("http://textures.minecraft.net/texture/5786fe99be377dfb6858859f926c4dbc995751e91cee373468c5fbf4865e7151", "Mojang");
        capes.put("http://textures.minecraft.net/texture/1bf91499701404e21bd46b0191d63239a4ef76ebde88d27e4d430ac211df681e", "Translator");
        capes.put("http://textures.minecraft.net/texture/9e507afc56359978a3eb3e32367042b853cddd0995d17d0da995662913fb00f7", "Mojang Studios");
        capes.put("http://textures.minecraft.net/texture/ae677f7d98ac70a533713518416df4452fe5700365c09cf45d0d156ea9396551", "Mojira Moderator");
        capes.put("http://textures.minecraft.net/texture/ca35c56efe71ed290385f4ab5346a1826b546a54d519e6a3ff01efa01acce81", "Cobalt");
        capes.put("http://textures.minecraft.net/texture/8f120319222a9f4a104e2f5cb97b2cda93199a2ee9e1585cb8d09d6f687cb761", "Mojang (Classic)");
        capes.put("http://textures.minecraft.net/texture/3efadf6510961830f9fcc077f19b4daf286d502b5f5aafbd807c7bbffcaca245", "Scrolls");
        capes.put("http://textures.minecraft.net/texture/2262fb1d24912209490586ecae98aca8500df3eff91f2a07da37ee524e7e3cb6", "Translator (Chinese)");
        capes.put("http://textures.minecraft.net/texture/5048ea61566353397247d2b7d946034de926b997d5e66c86483dfb1e031aee95", "Turtle");
        capes.put("Birthday Cake Texture", "Birthday");
        capes.put("http://textures.minecraft.net/texture/bcfbe84c6542a4a5c213c1cacf8979b5e913dcb4ad783a8b80e3c4a7d5c8bdac", "dB");
        capes.put("http://textures.minecraft.net/texture/70efffaf86fe5bc089608d3cb297d3e276b9eb7a8f9f2fe6659c23a2d8b18edf", "Millionth Customer");
        capes.put("http://textures.minecraft.net/texture/d8f8d13a1adf9636a16c31d47f3ecc9bb8d8533108aa5ad2a01b13b1a0c55eac", "Prismarine");
        capes.put("http://textures.minecraft.net/texture/23ec737f18bfe4b547c95935fc297dd767bb84ee55bfd855144d279ac9bfd9fe", "Snowman");
        capes.put("http://textures.minecraft.net/texture/2e002d5e1758e79ba51d08d92a0f3a95119f2f435ae7704916507b6c565a7da8", "Spade");
        capes.put("http://textures.minecraft.net/texture/ca29f5dd9e94fb1748203b92e36b66fda80750c87ebc18d6eafdb0e28cc1d05f", "Translator (Japanese)");
        capes.put("http://textures.minecraft.net/texture/f9a76537647989f9a0b6d001e320dac591c359e9e61a31f4ce11c88f207f0ad4", "Vanilla");
        capes.put("http://textures.minecraft.net/texture/afd553b39358a24edfe3b8a9a939fa5fa4faa4d9a9c3d6af8eafb377fa05c2bb", "Cherry Blossom");
        capes.put("http://textures.minecraft.net/texture/cd9d82ab17fd92022dbd4a86cde4c382a7540e117fae7b9a2853658505a80625   ", "15th Anniversary");
        capes.put("http://textures.minecraft.net/texture/cb40a92e32b57fd732a00fc325e7afb00a7ca74936ad50d8e860152e482cfbde", "Purple Heart");
        capes.put("http://textures.minecraft.net/texture/569b7f2a1d00d26f30efe3f9ab9ac817b1e6d35f4f3cfb0324ef2d328223d350", "Follower's");
        capes.put("Valentine Texture", "Valentine");
        capes.put("Test Texture", "Test");
        capes.put("http://textures.minecraft.net/texture/56c35628fe1c4d59dd52561a3d03bfa4e1a76d397c8b9c476c2f77cb6aebb1df", "MCC 15th Year");
    }
}