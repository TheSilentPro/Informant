package tsp.informant.bungee;

import club.minnced.discord.webhook.WebhookClientBuilder;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tsp.informant.bungee.checker.BungeePluginChecker;
import tsp.informant.bungee.config.BungeeConfig;
import tsp.informant.core.client.SpigotClient;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author TheSilentPro (Silent)
 */
public class Informant extends Plugin {

    public static final Logger LOGGER = LoggerFactory.getLogger("Informant");
    private static Informant instance;
    private Configuration config;
    private BungeeConfig bungeeConfig;
    private SpigotClient spigotClient;
    private BungeePluginChecker pluginChecker;

    @Override
    public void onEnable() {
        instance = this;

        //save default config
        if(!new File("plugins/Informant/config.yml").exists()) {
            try (OutputStreamWriter s = new OutputStreamWriter(new FileOutputStream("plugins/Informant/config.yml"))) {
                s.append(fromStream(getResourceAsStream("config.yml")));
            }catch(Exception ex) {
                LOGGER.error("Failed to load config.", ex);
            }
        }

        try {
            config = YamlConfiguration.getProvider(YamlConfiguration.class).load(new File("plugins/Informant/config.yml"));
        } catch (Exception ex) {
            LOGGER.error("Failed to load config.", ex);
        }

        bungeeConfig = new BungeeConfig();
        bungeeConfig.load();
        spigotClient = new SpigotClient();

        pluginChecker = new BungeePluginChecker(spigotClient, bungeeConfig, bungeeConfig.getWebhookUrl() != null ? new WebhookClientBuilder(bungeeConfig.getWebhookUrl()).setDaemon(true).build() : null);
        pluginChecker.load();
        pluginChecker.checkAll();
        if (bungeeConfig.getCheckInterval() > 1) {
            getProxy().getScheduler().schedule(this, () -> pluginChecker.checkAll(), bungeeConfig.getCheckInterval(), bungeeConfig.getCheckInterval(), TimeUnit.SECONDS);
        }

        new Metrics(this, 20941);
    }

    public BungeeConfig getBungeeConfig() {
        return bungeeConfig;
    }

    public SpigotClient getSpigotClient() {
        return spigotClient;
    }

    public BungeePluginChecker getPluginChecker() {
        return pluginChecker;
    }

    public String fromStream(InputStream stream) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8), 8192);
            StringBuilder sb = new StringBuilder(512);
            String content;
            while ((content = br.readLine()) != null)
                sb.append(content).append(System.lineSeparator());
            br.close();
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public Configuration getConfig() {
        return config;
    }

    public static Informant getInstance() {
        return instance;
    }

}