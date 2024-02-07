package tsp.informant.core.config;

import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import tsp.informant.core.client.SpigotResource;
import tsp.informant.core.client.SpigotResourceStatistics;

import java.awt.Color;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author TheSilentPro (Silent)
 */
public abstract class InformantConfig {

    protected static final Pattern SEMI_COLON = Pattern.compile(";");
    protected static final Pattern COLON = Pattern.compile(":");

    protected long checkInterval;
    protected boolean checkInstalled;
    protected Map<String, Integer> plugins;
    protected String consoleMessage;

    // Discord
    protected String webhookUrl;

    // Author
    protected String authorName;
    protected String authorIcon;
    protected String authorUrl;

    // Title
    protected String embedTitle;
    protected String embedTitleUrl;

    // Misc
    protected String embedDescription;
    protected String embedFooter;
    protected String embedFooterIcon;
    protected boolean embedTimestamp;

    // Fields
    protected List<String> embedFields;

    public abstract void load();

    public long getCheckInterval() {
        return checkInterval;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public String getConsoleMessage() {
        return consoleMessage;
    }

    public Optional<Integer> getId(String name) {
        return Optional.ofNullable(this.plugins.get(name));
    }

    public WebhookEmbed buildEmbed(SpigotResource resource, String name, String version) {
        WebhookEmbedBuilder builder = new WebhookEmbedBuilder()
                .setAuthor(new WebhookEmbed.EmbedAuthor(translate(authorName, resource), translate(authorIcon, resource), authorUrl))
                .setColor(Color.ORANGE.getRGB()) // todo: customizable embed color
                .setTitle(new WebhookEmbed.EmbedTitle(translate(embedTitle, resource), embedTitleUrl))
                .setDescription(translate(embedDescription, resource))
                .setFooter(new WebhookEmbed.EmbedFooter(translate(embedFooter, resource), embedFooterIcon))
                ;

        if (embedTimestamp) {
            builder.setTimestamp(Instant.now());
        }

        if (!embedFields.isEmpty()) {
            for (String rawField : embedFields) {
                String[] args = SEMI_COLON.split(translatePlugin(rawField, resource, name, version));
                builder.addField(new WebhookEmbed.EmbedField(Boolean.parseBoolean(args[0]), args[1], args[2]));
            }
        }

        return builder.build();
    }

    public String translatePlugin(String s, SpigotResource resource, String name, String version) {
        return translate(s.replace("%currentVersion%", version).replace("%name%", name), resource);
    }

    private String translate(String s, SpigotResource resource) {
        SpigotResourceStatistics stats = resource.getStatistics();

        return s.replace("%id%", String.valueOf(resource.getId()))
                .replace("%iconUrl%", resource.getIconLink())
                .replace("%latestVersion%", resource.getCurrentVersion())
                .replace("%nativeVersion%", resource.getNativeMinecraftVersion())
                .replace("%supportedVersions%", Arrays.toString(resource.getSupportedMinecraftVersion()))
                .replace("%title%", resource.getTitle())
                .replace("%tag%", resource.getTag())
                .replace("%description%", resource.getDescription())

                .replace("%downloads%", String.valueOf(stats.getDownloads()))
                .replace("%updates%", String.valueOf(stats.getUpdates()))
                .replace("%rating%", String.valueOf(stats.getRating()))
                .replace("%totalReviews%", String.valueOf(stats.getTotalReviews()))
                .replace("%uniqueReviews%", String.valueOf(stats.getUniqueReviews()))

                .replace("%price%", String.valueOf(stats.getPrice()))
                .replace("%currency%", stats.getCurrency())

                // Author
                .replace("%authorId%", String.valueOf(stats.getAuthorId()))
                .replace("%authorUsername%", stats.getAuthorUsername());
    }

}