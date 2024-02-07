package tsp.informant.core.config;

import java.util.Optional;

/**
 * A collection of known/popular plugin ids from spigot.
 *
 * @author TheSilentPro (Silent)
 */
@SuppressWarnings("SpellCheckingInspection")
public enum SpigotId {

    // Some plugins I can think from the top of my heads and from spigot.
    SKINSRESTORER("SkinsRestorer", 2124),
    SPARK("spark", 57242),
    LUCKPERMS("LuckPerms", 28140),
    ESSENTIALSX("Essentails", 28140),
    VAULT("Vault", 34315),
    VIAVERSION("ViaVersion", 19254),
    VIABACKWARDS("ViaBackwards", 27448),
    PROTOCOLLIB("ProtocolLib", 1997),
    PROTOCOLSUPPORT("ProtocolSupport", 7201),
    AUTHMERELOADED("AuthMeReloaded", 6269),
    LOGINSECURITY("LoginSecurity", 19362);

    public static final SpigotId[] VALUES = values();
    private final String name;
    private final int id;

    SpigotId(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public static Optional<SpigotId> match(String name) {
        for (SpigotId value : VALUES) {
            if (value.name.equalsIgnoreCase(name)) {
                return Optional.of(value);
            }
        }

        return Optional.empty();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

}