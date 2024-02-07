package tsp.informant.core.client;

public class SpigotResource {

    private final int id;
    private final String title;
    private final String tag;
    private final String description;
    private final String currentVersion;
    private final String nativeMinecraftVersion;
    private final String[] supportedMinecraftVersion;
    private final String iconLink;

    private final SpigotResourceStatistics statistics;

    public SpigotResource(
            int id,
            String title,
            String tag,
            String description,
            String currentVersion,
            String nativeMinecraftVersion,
            String[] supportedMinecraftVersions,
            String iconLink,
            SpigotResourceStatistics statistics
    ) {
        this.id = id;
        this.title = title;
        this.tag = tag;
        this.description = description;
        this.currentVersion = currentVersion;
        this.nativeMinecraftVersion = nativeMinecraftVersion;
        this.supportedMinecraftVersion = supportedMinecraftVersions;
        this.iconLink = iconLink;
        this.statistics = statistics;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTag() {
        return tag;
    }

    public String getDescription() {
        return description;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public String getNativeMinecraftVersion() {
        return nativeMinecraftVersion;
    }

    public String[] getSupportedMinecraftVersion() {
        return supportedMinecraftVersion;
    }

    public String getIconLink() {
        return iconLink;
    }

    public SpigotResourceStatistics getStatistics() {
        return statistics;
    }

}