package tsp.informant.spigot.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import tsp.informant.core.config.InformantConfig;
import tsp.informant.spigot.Informant;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author TheSilentPro (Silent)
 */
public class SpigotConfig extends InformantConfig {

    public void load() {
        FileConfiguration config = Informant.getInstance().getConfig();
        // Provided config list
        List<String> rawPlugins = config.getStringList("plugins");
        Map<String, Integer> result = new HashMap<>();
        if (!rawPlugins.isEmpty()) {
            for (String entry : rawPlugins) {
                if (entry.isEmpty()) {
                    continue;
                }

                String[] args = COLON.split(entry);
                int id = Integer.parseInt(args[1]);
                Plugin plugin = Bukkit.getPluginManager().getPlugin(args[0]);
                if (plugin == null) {
                    Informant.LOGGER.error("Config plugin is null: " + args[0]);
                    continue;
                }

                result.put(plugin.getName(), id);
            }
        }

        // Self checking
        if (config.getBoolean("checkInstalled")) {
            for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                InputStream in = plugin.getResource("plugin.yml");
                if (in == null) {
                    Informant.LOGGER.error("Failed to get plugin.yml for plugin: " + plugin.getName());
                    continue;
                }

                YamlConfiguration data = YamlConfiguration.loadConfiguration(new InputStreamReader(in));
                if (data.contains("spigot-id")) {
                    try {
                        //noinspection DataFlowIssue - Caught with NumberFormantException
                        int id = Integer.parseInt(data.getString("spigot-id"));

                        if (result.containsValue(id)) {
                            Informant.LOGGER.warn("The plugin '" + plugin.getName() + "' already specifies it's own spigot-id, remove it from your plugins section in the config.yml!");
                            continue;
                        }

                        result.put(plugin.getName(), id);
                    } catch (NumberFormatException ignored) {
                        Informant.LOGGER.error("Id is not a number for plugin: " + plugin.getName());
                    }
                } else {
                    Informant.LOGGER.debug("Undefined spigot-id in plugin: " + plugin.getName());
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
