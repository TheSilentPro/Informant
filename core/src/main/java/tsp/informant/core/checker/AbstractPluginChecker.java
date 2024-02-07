package tsp.informant.core.checker;

import club.minnced.discord.webhook.WebhookClient;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tsp.informant.core.client.SpigotClient;
import tsp.informant.core.config.InformantConfig;
import tsp.informant.core.config.SpigotId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author TheSilentPro (Silent)
 */
public abstract class AbstractPluginChecker implements PluginChecker {

    private final Logger LOGGER = LoggerFactory.getLogger("Informant");

    private final SpigotClient client;
    private final InformantConfig config;
    private final WebhookClient discordClient;
    protected final List<AbstractPlugin> installedPlugins = new ArrayList<>();

    public AbstractPluginChecker(SpigotClient client, InformantConfig config, @Nullable WebhookClient discordClient) {
        this.client = client;
        this.config = config;
        this.discordClient = discordClient;
    }

    public abstract void load();

    @Override
    public void checkAll() {
        for (AbstractPlugin plugin : installedPlugins) {
            int id = config.getId(plugin.name()).orElse(-1);
            if (id == -1) {
                Optional<SpigotId> matched = SpigotId.match(plugin.name());
                if (matched.isPresent()) {
                    id = matched.get().getId();
                }
            }

            if (id == -1) {
                LOGGER.debug("Failed to find spigot ID for: " + plugin.name());
                continue;
            }

            client.getResource(id).thenAccept(resource -> {
                if (!resource.getCurrentVersion().equals(plugin.version())) {
                    // Send console message
                    LOGGER.warn(config.translatePlugin(config.getConsoleMessage(), resource, plugin.name(), plugin.version()));

                    // Send discord message
                    if (discordClient != null) {
                        discordClient.send(config.buildEmbed(resource, plugin.name(), plugin.version())).whenComplete((msg, ex) -> {
                            if (ex != null) {
                                LOGGER.error("Failed to send discord message!", ex);
                            } else {
                                LOGGER.debug("Sent new version message with id: " + msg.getId());
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public List<AbstractPlugin> getInstalledPlugins() {
        return installedPlugins;
    }

}