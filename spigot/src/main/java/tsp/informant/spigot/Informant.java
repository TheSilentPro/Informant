package tsp.informant.spigot;


import club.minnced.discord.webhook.WebhookClientBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tsp.informant.core.client.SpigotClient;
import tsp.informant.spigot.checker.SpigotPluginChecker;
import tsp.informant.spigot.config.SpigotConfig;

/**
 * @author TheSilentPro (Silent)
 */
public class Informant extends JavaPlugin {

    public static final Logger LOGGER = LoggerFactory.getLogger("Informant");

    private SpigotConfig spigotConfig;
    private SpigotClient spigotClient;
    private SpigotPluginChecker pluginChecker;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        spigotConfig = new SpigotConfig();
        spigotConfig.load();
        spigotClient = new SpigotClient();

        pluginChecker = new SpigotPluginChecker(spigotClient, spigotConfig, spigotConfig.getWebhookUrl() != null ? new WebhookClientBuilder(spigotConfig.getWebhookUrl()).setDaemon(true).build() : null);
        pluginChecker.load();
        pluginChecker.checkAll();
        if (spigotConfig.getCheckInterval() > 1) {
            getServer().getScheduler().runTaskTimerAsynchronously(this, () -> pluginChecker.checkAll(), spigotConfig.getCheckInterval() * 20, spigotConfig.getCheckInterval() * 20);
        }

        new Metrics(this, 20941);
    }

    public SpigotPluginChecker getPluginChecker() {
        return pluginChecker;
    }

    public SpigotClient getSpigotClient() {
        return spigotClient;
    }

    public SpigotConfig getSpigotConfig() {
        return spigotConfig;
    }

    public static Informant getInstance() {
        return getPlugin(Informant.class);
    }

}