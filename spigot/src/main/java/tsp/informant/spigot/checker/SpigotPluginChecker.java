package tsp.informant.spigot.checker;

import club.minnced.discord.webhook.WebhookClient;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import tsp.informant.core.checker.AbstractPlugin;
import tsp.informant.core.checker.AbstractPluginChecker;
import tsp.informant.core.client.SpigotClient;
import tsp.informant.core.config.InformantConfig;

/**
 * @author TheSilentPro (Silent)
 */
public class SpigotPluginChecker extends AbstractPluginChecker {

    public SpigotPluginChecker(SpigotClient client, InformantConfig config, @Nullable WebhookClient discordClient) {
        super(client, config, discordClient);
    }

    @Override
    public void load() {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            installedPlugins.add(new AbstractPlugin(plugin.getName(), plugin.getDescription().getVersion()));
        }
    }

}