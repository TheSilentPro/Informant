package tsp.informant.velocity.checker;

import club.minnced.discord.webhook.WebhookClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.velocitypowered.api.plugin.PluginContainer;
import org.jetbrains.annotations.Nullable;
import tsp.informant.core.checker.AbstractPlugin;
import tsp.informant.core.checker.AbstractPluginChecker;
import tsp.informant.core.client.SpigotClient;
import tsp.informant.core.config.InformantConfig;
import tsp.informant.velocity.Informant;
import tsp.informant.velocity.config.VelocityConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author TheSilentPro (Silent)
 */
public class VelocityPluginChecker extends AbstractPluginChecker {

    public VelocityPluginChecker(SpigotClient client, InformantConfig config, @Nullable WebhookClient discordClient) {
        super(client, config, discordClient);
    }

    @Override
    public void load() {
        VelocityConfig config = Informant.getInstance().getConfig();
        // Provided config list
        config.getPlugins().forEach((id, plugin) -> installedPlugins.add(new AbstractPlugin(plugin.getDescription().getName().orElseThrow(), String.valueOf(id))));

        // Self checking
        if (config.checkInstalled()) {
            for (PluginContainer plugin : Informant.getInstance().getServer().getPluginManager().getPlugins()) {
                //First check for bungee.yml
                int id = -1;

                URL in = plugin.getInstance().get().getClass().getResource("bungee.yml");
                if(in == null) {
                    // check for plugin.yml
                    in = plugin.getInstance().get().getClass().getResource("plugin.yml");
                    if (in == null) {
                        // check for velocity-plugin.json
                        id = extractId(plugin);
                    }
                }

                if (id != -1) {
                    try {
                        installedPlugins.add(new AbstractPlugin(plugin.getDescription().getName().orElseThrow(), String.valueOf(id)));
                    } catch (NumberFormatException ignored) {
                        Informant.getInstance().getLogger().debug("Id is not a number for plugin: " + plugin.getDescription().getName());
                    }
                } else {
                    Informant.getInstance().getLogger().debug("Undefined spigot-id in plugin: " + plugin.getDescription().getName());
                }
            }
        }
    }

    private int extractId(PluginContainer plugin) {
        try {
            File file = new File(plugin.getClass().getResource("velocity-plugin.json").toURI());
            JsonObject main = JsonParser.parseReader(new FileReader(file)).getAsJsonObject();
            return main.get("spigot-id").getAsInt();
        } catch (URISyntaxException | FileNotFoundException ex) {
            Informant.getInstance().getLogger().debug("Could not extract id from: " + plugin.getDescription().getName(), ex);
            return -1;
        }
    }

}
