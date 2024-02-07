package tsp.informant.core.client;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpigotClient {

    private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> new Thread(r, "Informant Plugin Checker"));
    private final HttpClient client = HttpClient.newBuilder()
            .executor(executor)
            .build();

    /**
     * Retrieve a resource from an id
     *
     * @param id The resource id
     */
    public CompletableFuture<SpigotResource> getResource(int id) {
        return read(id).thenApplyAsync(this::buildResource, executor);
    }

    /**
     * Used for building a {@link SpigotResource}
     *
     * @param json The json response of the resource
     * @return Wrapped resource
     */
    private SpigotResource buildResource(JsonObject json) {
        JsonObject premium = json.get("premium").getAsJsonObject();
        JsonObject author = json.get("author").getAsJsonObject();
        JsonObject stats = json.get("stats").getAsJsonObject();
        JsonObject reviews = stats.get("reviews").getAsJsonObject();

        List<String> versions = new ArrayList<>();
        JsonArray jsonSupportedMinecraftVersions = !json.get("supported_minecraft_versions").isJsonNull() ? json.get("supported_minecraft_versions").getAsJsonArray() : new JsonArray();
        for (JsonElement entry : jsonSupportedMinecraftVersions) {
            versions.add(entry.getAsString());
        }

        String nativeMinecraftVersion = !json.get("native_minecraft_version").isJsonNull()
                ? json.get("native_minecraft_version").getAsString()
                : "";

        return new SpigotResource(
                json.get("id").getAsInt(),
                json.get("title").getAsString(),
                json.get("tag").getAsString(),
                json.get("description").getAsString(),
                json.get("current_version").getAsString(),
                nativeMinecraftVersion,
                versions.toArray(new String[0]),
                json.get("icon_link").getAsString(),

                new SpigotResourceStatistics(
                        author.get("id").getAsInt(),
                        author.get("username").getAsString(),

                        premium.get("price").getAsDouble(),
                        premium.get("currency").getAsString(),

                        stats.get("downloads").getAsInt(),
                        stats.get("updates").getAsInt(),
                        stats.get("rating").getAsDouble(),
                        reviews.get("unique").getAsInt(),
                        reviews.get("total").getAsInt()
                )
        );
    }

    /**
     * Reads the json contents of the url
     *
     * @return Json response
     */
    private CompletableFuture<JsonObject> read(int id) {
        try {
            return client.sendAsync(HttpRequest.newBuilder()
                    .uri(new URL("https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id=" + id).toURI())
                    .GET()
                    .header("User-Agent", "Informant")
                    .build()
            , HttpResponse.BodyHandlers.ofString()).thenApplyAsync(json -> JsonParser.parseString(json.body()).getAsJsonObject(), executor);
        } catch (URISyntaxException | MalformedURLException ex) {
            return CompletableFuture.failedFuture(ex);
        }
    }

}