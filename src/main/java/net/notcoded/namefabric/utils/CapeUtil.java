package net.notcoded.namefabric.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static net.notcoded.namefabric.utils.VersionUtil.parseString;

public class CapeUtil {
    private static final Map<String, String> CAPES = createCapeMap();

    public static String identifyCape(@NotNull String url) {
        String textureHash = textureHash(url);
        if (textureHash == null) return "No";

        String cape = CAPES.get(textureHash);
        if (cape != null && !cape.trim().isEmpty()) return cape;

        return "Unknown (" + textureHash.substring(0, Math.min(12, textureHash.length())) + "...)";
    }

    public static String extractCapeUrl(JsonObject profile) {
        if (!profile.has("properties") || !profile.get("properties").isJsonArray()) {
            return "";
        }

        for (JsonElement property : profile.getAsJsonArray("properties")) {
            JsonObject propertyObject = property.getAsJsonObject();
            if (!propertyObject.has("name") || !"textures".equals(propertyObject.get("name").getAsString())) {
                continue;
            }

            String value = propertyObject.get("value").getAsString();
            String decodedTextures = new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
            JsonObject textures = parseString(decodedTextures).getAsJsonObject().getAsJsonObject("textures");
            if (textures != null && textures.has("CAPE")) {
                return textures.getAsJsonObject("CAPE").get("url").getAsString();
            }
        }

        return "";
    }

    private static String textureHash(String url) {
        if (url == null || url.trim().isEmpty()) return null;

        int texturePath = url.lastIndexOf("/texture/");
        int hashStart = texturePath >= 0 ? texturePath + "/texture/".length() : url.lastIndexOf('/') + 1;
        String hash = url.substring(hashStart);
        int queryStart = hash.indexOf('?');
        if (queryStart >= 0) hash = hash.substring(0, queryStart);

        hash = hash.trim().toLowerCase(Locale.ROOT);
        return hash.isEmpty() ? null : hash;
    }

    private static Map<String, String> createCapeMap() {
        HashMap<String, String> capes = new HashMap<>();

        putCape(capes, "2340c0e03dd24a11b15a8b33c2a7e9e32abb2051b2481d0ba7defd635ca7a933", "Migrator");
        putCape(capes, "28de4a81688ad18b49e735a273e086c18f1e3966956123ccb574034c06f5d336", "Pan");
        putCape(capes, "5ec930cdd2629c8771655c60eebeb867b4b6559b0e6d3bc71c40c96347fa03f0", "Common");
        putCape(capes, "dbc21e222528e30dc88445314f7be6ff12d3aeebc3c192054fba7e3b3f8c77b1", "Menace");
        putCape(capes, "e7dfea16dc83c97df01a12fabbd1216359c0cd0ea42f9999b6e97c584963e980", "MineCon 2016");
        putCape(capes, "b0cc08840700447322d953a02b965f1d65a13a603bf64b17c803c21446fe1635", "MineCon 2015");
        putCape(capes, "153b1a0dfcbae953cdeb6f2c2bf6bf79943239b1372780da44bcbb29273131da", "MineCon 2013");
        putCape(capes, "a2e8d97ec79100e90a75d369d1b3ba81273c4f82bc1b737e934eed4a854be1b6", "MineCon 2012");
        putCape(capes, "953cac8b779fe41383e675ee2b86071a71658f2180f56fbce8aa315ea70e2ed6", "MineCon 2011");
        putCape(capes, "17912790ff164b93196f08ba71d0e62129304776d0f347334f8a6eae509f8a56", "Realms Mapmaker");
        putCape(capes, "5786fe99be377dfb6858859f926c4dbc995751e91cee373468c5fbf4865e7151", "Mojang");
        putCape(capes, "1bf91499701404e21bd46b0191d63239a4ef76ebde88d27e4d430ac211df681e", "Translator");
        putCape(capes, "9e507afc56359978a3eb3e32367042b853cddd0995d17d0da995662913fb00f7", "Mojang Studios");
        putCape(capes, "ae677f7d98ac70a533713518416df4452fe5700365c09cf45d0d156ea9396551", "Mojira Moderator");
        putCape(capes, "ca35c56efe71ed290385f4ab5346a1826b546a54d519e6a3ff01efa01acce81", "Cobalt");
        putCape(capes, "8f120319222a9f4a104e2f5cb97b2cda93199a2ee9e1585cb8d09d6f687cb761", "Mojang (Classic)");
        putCape(capes, "3efadf6510961830f9fcc077f19b4daf286d502b5f5aafbd807c7bbffcaca245", "Scrolls");
        putCape(capes, "2262fb1d24912209490586ecae98aca8500df3eff91f2a07da37ee524e7e3cb6", "Translator (Chinese)");
        putCape(capes, "5048ea61566353397247d2b7d946034de926b997d5e66c86483dfb1e031aee95", "Turtle");
        putCape(capes, "bcfbe84c6542a4a5c213c1cacf8979b5e913dcb4ad783a8b80e3c4a7d5c8bdac", "dB");
        putCape(capes, "70efffaf86fe5bc089608d3cb297d3e276b9eb7a8f9f2fe6659c23a2d8b18edf", "Millionth Customer");
        putCape(capes, "d8f8d13a1adf9636a16c31d47f3ecc9bb8d8533108aa5ad2a01b13b1a0c55eac", "Prismarine");
        putCape(capes, "23ec737f18bfe4b547c95935fc297dd767bb84ee55bfd855144d279ac9bfd9fe", "Snowman");
        putCape(capes, "2e002d5e1758e79ba51d08d92a0f3a95119f2f435ae7704916507b6c565a7da8", "Spade");
        putCape(capes, "ca29f5dd9e94fb1748203b92e36b66fda80750c87ebc18d6eafdb0e28cc1d05f", "Translator (Japanese)");
        putCape(capes, "f9a76537647989f9a0b6d001e320dac591c359e9e61a31f4ce11c88f207f0ad4", "Vanilla");
        putCape(capes, "afd553b39358a24edfe3b8a9a939fa5fa4faa4d9a9c3d6af8eafb377fa05c2bb", "Cherry Blossom");
        putCape(capes, "cd9d82ab17fd92022dbd4a86cde4c382a7540e117fae7b9a2853658505a80625", "15th Anniversary");
        putCape(capes, "cb40a92e32b57fd732a00fc325e7afb00a7ca74936ad50d8e860152e482cfbde", "Purple Heart");
        putCape(capes, "569b7f2a1d00d26f30efe3f9ab9ac817b1e6d35f4f3cfb0324ef2d328223d350", "Follower's");
        putCape(capes, "1de21419009db483900da6298a1e6cbf9f1bc1523a0dcdc16263fab150693edd", "Home");
        putCape(capes, "5c29410057e32abec02d870ecb52ec25fb45ea81e785a7854ae8429d7236ca26", "Valentine");
        putCape(capes, "56c35628fe1c4d59dd52561a3d03bfa4e1a76d397c8b9c476c2f77cb6aebb1df", "MCC 15th Year");
        putCape(capes, "7658c5025c77cfac7574aab3af94a46a8886e3b7722a895255fbf22ab8652434", "Minecraft Experience");
        putCape(capes, "99aba02ef05ec6aa4d42db8ee43796d6cd50e4b2954ab29f0caeb85f96bf52a1", "Founder's");
        putCape(capes, "a3f6e4f14801f3ea55e3d95b9b4ef3b5e8802d947f669de93d6ec4b9354a436b", "Zombie Horse");
        putCape(capes, "308b32a9e303155a0b4262f9e5483ad4a22e3412e84fe8385a0bdd73dc41fa89", "Yearn");
        putCape(capes, "5e6f3193e74cd16cdd6637d9bae5484e3a37ff2a14c2d157c659a07810b1bdca", "Copper");

        return Collections.unmodifiableMap(capes);
    }

    private static void putCape(Map<String, String> capes, String textureHash, String name) {
        capes.put(textureHash.toLowerCase(Locale.ROOT), name);
    }
}
