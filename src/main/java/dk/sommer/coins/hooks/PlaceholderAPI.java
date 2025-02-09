package dk.sommer.coins.hooks;


import dk.sommer.coins.Coins;
import dk.sommer.coins.database.DatabaseManager;
import dk.sommer.coins.utils.NumUtils;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.injector.annotation.PostConstruct;
import eu.okaeri.platform.core.annotation.Component;
import lombok.Getter;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Component
public class PlaceholderAPI extends PlaceholderExpansion {
    private @Inject DatabaseManager db;
    private @Inject Coins plugin;

    @Getter
    private static PlaceholderAPI instance;

    @PostConstruct
    public void setInstance() {
        instance = this;
    }


    @Override
    public String onPlaceholderRequest(@NotNull Player player, @NotNull String identifier) {
        switch (identifier.toLowerCase()) {
            case "balance":
                return String.valueOf(db.getCoins(player));
            case "formatted":
                return NumUtils.formatted(db.getCoins(player));
            default:
                return null;
        }
    }

    @Override
    public @NotNull String getIdentifier() {
        return "coins";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
