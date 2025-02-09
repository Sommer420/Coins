package dk.sommer.coins.cmd;

import dk.sommer.coins.config.LangConfig;
import dk.sommer.coins.database.DatabaseManager;
import eu.okaeri.commands.annotation.Arg;
import eu.okaeri.commands.annotation.Command;
import eu.okaeri.commands.annotation.Context;
import eu.okaeri.commands.annotation.Executor;
import eu.okaeri.commands.bukkit.annotation.Async;
import eu.okaeri.commands.bukkit.annotation.Permission;
import eu.okaeri.commands.service.CommandService;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.bukkit.i18n.BI18n;
import org.bukkit.entity.Player;

@Command(label = "coins", aliases = "c")
public class CoinsCmd implements CommandService {
    private @Inject LangConfig lang;
    private @Inject BI18n i18n;
    private @Inject DatabaseManager db;


    @Executor
    @Async
    public void __(@Context Player sender) {
        i18n.get(lang.getPrefix()).sendTo(sender);
        i18n.get(lang.getCoinBalance())
                .with("balance", db.getCoins(sender))
                .sendTo(sender);
    }

    @Executor(pattern = "*")
    @Permission("coins.command.coins.other")
    @Async
    public void __(@Context Player sender, @Arg Player player) {
        if (player == null) {
            i18n.get(lang.getError())
                    .with("fejl", "Spilleren findes ikke")
                    .sendTo(sender);
            return;
        }
        i18n.get(lang.getPrefix()).sendTo(sender);
        i18n.get(lang.getCoinBalanceOther())
                .with("player", player.getName())
                .with("balance", db.getCoins(player))
                .sendTo(sender);
    }

    @Executor(pattern = "pay * *")
    @Permission("coins.command.pay")
    @Async
    public void pay(@Context Player sender, @Arg Player player, @Arg int amount) {
        if (player == null) {
            i18n.get(lang.getError())
                    .with("fejl", "Spilleren findes ikke")
                    .sendTo(sender);
            return;
        }
        if (amount < 0) {
            i18n.get(lang.getError())
                    .with("fejl", "Du kan ikke sende et negativt antal coins")
                    .sendTo(sender);
            return;
        }
        if (db.getCoins(sender) < amount) {
            i18n.get(lang.getError())
                    .with("fejl", "Du har ikke nok coins")
                    .sendTo(sender);
            return;
        }
        db.removeCoins(sender, amount);
        db.addCoins(player, amount);
        i18n.get(lang.getPrefix()).sendTo(sender);
        i18n.get(lang.getPayCoins())
                .with("amount", amount)
                .with("player", player.getName())
                .sendTo(sender);
        i18n.get(lang.getPrefix()).sendTo(player);
        i18n.get(lang.getPayCoinsReceived())
                .with("amount", amount)
                .with("player", sender.getName())
                .sendTo(player);
    }

    @Executor(pattern = "add * *")
    @Permission("coins.command.add")
    @Async
    public void add(@Context Player sender, @Arg Player player, @Arg int amount) {
        if (player == null) {
            i18n.get(lang.getError())
                    .with("fejl", "Spilleren findes ikke")
                    .sendTo(sender);
            return;
        }
        if (amount < 0) {
            i18n.get(lang.getError())
                    .with("fejl", "Du kan ikke give et negativt antal coins")
                    .sendTo(sender);
            return;
        }
        db.addCoins(player, amount);
        i18n.get(lang.getPrefix()).sendTo(sender);
        i18n.get(lang.getCoinAdminAddSuccess())
                .with("player", player.getName())
                .with("amount", amount)
                .sendTo(sender);
        i18n.get(lang.getPrefix()).sendTo(player);
        i18n.get(lang.getCoinAdminAddReceived())
                .with("player", sender.getName())
                .with("amount", amount)
                .sendTo(player);
    }

    @Executor(pattern = "remove * *")
    @Permission("coins.command.remove")
    @Async
    public void remove(@Context Player sender, @Arg Player player, @Arg int amount) {
        if (player == null) {
            i18n.get(lang.getError())
                    .with("fejl", "Spilleren findes ikke")
                    .sendTo(sender);
            return;
        }
        if (amount < 0) {
            i18n.get(lang.getError())
                    .with("fejl", "Du kan ikke fjerne et negativt antal coins")
                    .sendTo(sender);
            return;
        }
        db.removeCoins(player, amount);
        i18n.get(lang.getPrefix()).sendTo(sender);
        i18n.get(lang.getCoinAdminRemoveSuccess())
                .with("amount", amount)
                .sendTo(sender);
        i18n.get(lang.getPrefix()).sendTo(player);
        i18n.get(lang.getCoinAdminRemoveLost())
                .with("amount", amount)
                .sendTo(player);
    }

    @Executor(pattern = "set * *")
    @Permission("coins.command.set")
    @Async
    public void set(@Context Player sender, @Arg Player player, @Arg int amount) {
        if (player == null) {
            i18n.get(lang.getError())
                    .with("fejl", "Spilleren findes ikke")
                    .sendTo(sender);
            return;
        }
        if (amount < 0) {
            i18n.get(lang.getError())
                    .with("fejl", "Du kan ikke sætte et negativt antal coins")
                    .sendTo(sender);
            return;
        }
        db.setCoins(player, amount);
        i18n.get(lang.getPrefix()).sendTo(sender);
        i18n.get(lang.getCoinAdminSetSuccess())
                .with("player", player.getName())
                .with("amount", amount)
                .sendTo(sender);
        i18n.get(lang.getPrefix()).sendTo(player);
        i18n.get(lang.getCoinAdminSetSuccess())
                .with("player", sender.getName())
                .with("amount", amount)
                .sendTo(player);
    }

    @Executor(pattern = "help")
    @Permission("coins.command.help")
    @Async
    public void help(@Context Player sender) {
        i18n.get(lang.getPrefix()).sendTo(sender);
        i18n.get(lang.getCoinAdminHelp()).sendTo(sender);
    }
}
