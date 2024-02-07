package tsp.informant.velocity;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

import club.minnced.discord.webhook.WebhookClientBuilder;
import tsp.informant.core.client.SpigotClient;
import tsp.informant.velocity.checker.VelocityPluginChecker;
import tsp.informant.velocity.config.VelocityConfig;

/**
 * @author TheSilentPro (Silent)
 */
@Plugin(id = "informant", name = "Informant", version = "1.0.0", authors = {"Silent", "TheDevTec"})
public class Informant {

    private static Informant instance;
    private final Logger logger;
    private final ProxyServer server;
    private final VelocityConfig config;

    private SpigotClient spigotClient;
    private VelocityPluginChecker pluginChecker;

    @Inject
    public Informant(ProxyServer server, Logger logger, Metrics.Factory factory) {
        instance = this;
        this.logger = logger;
        logger.info("Loading Informant - " + server.getPluginManager().getPlugin("informant").get().getDescription().getVersion().get());
        this.server = server;
        this.config = new VelocityConfig(new File("plugins/Informant/config.json"));
        this.config.createThenLoad();

        spigotClient = new SpigotClient();

        pluginChecker = new VelocityPluginChecker(spigotClient, config, config.getWebhookUrl() != null ? new WebhookClientBuilder(config.getWebhookUrl()).setDaemon(true).build() : null);
        pluginChecker.load();
        pluginChecker.checkAll();
        if (config.getCheckInterval() > 1) {
            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> pluginChecker.checkAll(), config.getCheckInterval(), config.getCheckInterval(), TimeUnit.SECONDS);
        }

        factory.make(this, 20941);

        logger.info("Done!");
    }

    public VelocityPluginChecker getPluginChecker() {
        return pluginChecker;
    }

    public Logger getLogger() {
        return logger;
    }

    public SpigotClient getSpigotClient() {
        return spigotClient;
    }

    public VelocityConfig getConfig() {
        return config;
    }

    public ProxyServer getServer() {
        return server;
    }

    public static Informant getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Instance is null.");
        }

        return instance;
    }


}