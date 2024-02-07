package tsp.informant.bungee.config;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;
import tsp.informant.bungee.Informant;
import tsp.informant.core.config.InformantConfig;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TheSilentPro (Silent)
 */
public class BungeeConfig extends InformantConfig  {

    @Override
    public void load() {
        Configuration config = Informant.getInstance().getConfig();
        List<String> rawPlugins = config.getStringList("plugins");
        Map<String, Integer> result = new HashMap<>();
        if (!rawPlugins.isEmpty()) {
            for (String entry : rawPlugins) {
                if (entry.isEmpty()) {
                    continue;
                }

                String[] args = COLON.split(entry);
                int id = Integer.parseInt(args[1]);
                Plugin plugin = ProxyServer.getInstance().getPluginManager().getPlugin(args[0]);
                if (plugin == null) {
                    Informant.LOGGER.error("Config plugin is null: " + args[0]);
                    continue;
                }

                result.put(plugin.getDescription().getName(), id);
            }
        }

        // Self checking
        if (config.getBoolean("checkInstalled")) {
            for (Plugin plugin : ProxyServer.getInstance().getPluginManager().getPlugins()) {
                InputStream in = plugin.getResourceAsStream("plugin.yml");
                if (in == null) {
                    Informant.LOGGER.error("Failed to get plugin.yml for plugin: " + plugin.getDescription().getName());
                    continue;
                }


                Configuration data = YamlConfiguration.getProvider(YamlConfiguration.class).load(new InputStreamReader(in));
                if (data.contains("spigot-id")) {
                    try {
                        int id = Integer.parseInt(data.getString("spigot-id"));

                        if (result.containsValue(id)) {
                            Informant.LOGGER.warn("The plugin '" + plugin.getDescription().getName() + "' already specifies it's own spigot-id, remove it from your plugins section in the config.yml!");
                            continue;
                        }

                        result.put(plugin.getDescription().getName(), id);
                    } catch (NumberFormatException ignored) {
                        Informant.LOGGER.error("Id is not a number for plugin: " + plugin.getDescription().getName());
                    }
                } else {
                    Informant.LOGGER.debug("Undefined spigot-id in plugin: " + plugin.getDescription().getName());
                }
            }
        }

        // Set data
        checkInterval = config.getLong("interval");
        checkInstalled = config.getBoolean("checkInstalled");
        plugins = result;
        consoleMessage = config.getString("message.console");

        // Discord
        webhookUrl = config.getString("message.webhookUrl");
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            Informant.LOGGER.info("Discord webhook is disabled by config.yml! Set a webhook url to enable it.");
            return;
        }

        // Embed
        authorName = config.getString("message.author.name");
        authorIcon = config.getString("message.author.icon");
        authorUrl = config.getString("message.author.url");

        embedTitle = config.getString("message.title.name");
        embedTitleUrl = config.getString("message.title.url");

        embedDescription = config.getString("message.description");

        embedFooter = config.getString("message.footer.name");
        embedFooterIcon = config.getString("message.footer.icon");

        embedTimestamp = config.getBoolean("message.timestamp");

        embedFields = config.getStringList("message.fields");
    }

}
