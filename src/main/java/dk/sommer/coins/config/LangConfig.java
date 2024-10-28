package dk.sommer.coins.config;

import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import eu.okaeri.i18n.configs.LocaleConfig;
import eu.okaeri.platform.core.annotation.Messages;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Messages(defaultLocale = "da", provider = YamlSnakeYamlConfigurer.class)
public class LangConfig extends LocaleConfig {

    private String error = "&4Fejl: &c{fejl}.";
    private String prefix = "&8&l[ &6&lCOIN&f&lSYSTEM &8&l]";

    private String coinBalance = "&7 Du har &f{balance}&6⛃&7!";
    /*private String coinBalance = "&7 Du har &f{balance}&6⛃&7!\n"
            + "&7 Brug &f/coins help&7 for at se alle kommandoer.";*/
    private String coinBalanceOther = "&f {player}&7 har &f{balance}&6⛃&7!";

    private String coinAdminHelp = "&7Følgende kommandoer kan bruges: &8(/coins *)\n"
            + "&8- &7add (spiller) (antal)\n"
            + "&8- &7remove (spiller) (antal)\n"
            + "&8- &7set (spiller) (antal)\n"
            + "&8- &7(spiller)";

    private String coinAdminAddSuccess = "&7Du gav &f{amount}&6⛃&7 til &f{player}&7.";
    private String coinAdminAddReceived = "&7Du fik &f{amount}&6⛃&7 af &f{player}&7.";
    private String coinAdminAddUsage = "&c/coins add (spiller) (antal)";

    private String coinAdminRemoveSuccess = "&7Du fjernede &f{amount}&6⛃&7 fra &f{player}&7.";
    private String coinAdminRemoveLost = "&7Du mistede &f{amount}&6⛃&7.";
    private String coinAdminRemoveError = "&cDu kan ikke fjerne flere coins end spilleren har.";
    private String coinAdminRemoveUsage = "&c/coins remove (spiller) (antal)";

    private String coinAdminSetSuccess = "&7Du satte &f{player}&7s coins&7 til &f{amount}&6⛃&7.";
    private String coinAdminSetNotification = "&7Dine coins blev sat til &f{amount}&6⛃&7.";
    private String coinAdminSetUsage = "&c/coins set (spiller) (antal)";

    private String payCoins = "&7Du sendte &f{amount}&6⛃&7 til &f{player}&7.";
    private String payCoinsReceived = "&7Du modtog &f{amount}&6⛃&7 fra &f{player}&7.";
    private String payCoinsUsage = "&c/coins pay (spiller) (antal)";
    private String coinHelpHint = "&7&o(-/coins help)";
}
