package dk.sommer.coins;

import dk.sommer.coins.database.DatabaseManager;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import eu.okaeri.i18n.configs.LocaleConfig;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.bukkit.OkaeriBukkitPlugin;
import eu.okaeri.platform.core.annotation.Bean;
import eu.okaeri.platform.core.annotation.Scan;
import eu.okaeri.platform.core.config.EmptyConfig;
import eu.okaeri.platform.core.plan.ExecutionPhase;
import eu.okaeri.platform.core.plan.Planned;

import java.io.File;

@Scan(exclusions = "dk.sommer.coins.libs", deep = true)
public final class Coins extends OkaeriBukkitPlugin {

    private @Inject DatabaseManager databaseManager;

    @Bean
    public EmptyConfig loadLocaleConfigForCommands(LocaleConfig localeConfig) {
        return ConfigManager.create(EmptyConfig.class, (it) -> {
            it.withConfigurer(new YamlSnakeYamlConfigurer(), new SerdesBukkit());
            it.withBindFile(new File(new File(this.getDataFolder(), "i18n"), "dk.yml"));
            it.withRemoveOrphans(false);
            it.saveDefaults();
            it.load(false);
        });
    }

    @Planned(ExecutionPhase.STARTUP)
    public void startup(){
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        String dbPath = dataFolder + File.separator + "data.db";

        if (databaseManager != null) {
            databaseManager.setDbPath(dbPath);
            databaseManager.openConnection();
            databaseManager.createTableIfNotExists();
        } else {
            databaseManager = new DatabaseManager(dbPath);
        }
    }

    @Planned(ExecutionPhase.SHUTDOWN)
    public void shutdown(){
        if (databaseManager != null) {
            databaseManager.closeConnection();
        }
    }

}
