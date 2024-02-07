package tsp.informant.core.checker;

import java.util.List;

/**
 * @author TheSilentPro (Silent)
 */
public interface PluginChecker {

    void checkAll();

    List<AbstractPlugin> getInstalledPlugins();


}