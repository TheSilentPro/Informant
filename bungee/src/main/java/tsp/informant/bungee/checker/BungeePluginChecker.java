package tsp.informant.bungee.checker;

import club.minnced.discord.webhook.WebhookClient;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import tsp.informant.core.checker.AbstractPlugin;
import tsp.informant.core.checker.AbstractPluginChecker;
import tsp.informant.core.client.SpigotClient;
import tsp.informant.core.config.InformantConfig;

/**
 * @author TheSilentPro (Silent)
 */
public class BungeePluginChecker extends AbstractPluginChecker {

    public BungeePluginChecker(SpigotClient client, InformantConfig config, @Nullable WebhookClient discordClient) {
        super(client, config, discordClient);
    }

    @Override
    public void load() {
        for (Plugin plugin : ProxyServer.getInstance().getPluginManager().getPlugins()) {
            installedPlugins.add(new AbstractPlugin(plugin.getDescription().getName(), plugin.getDescription().getVersion()));
        }
    }

}
