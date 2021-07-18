package com.github.tanokun.karadasagasi.command;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.game.GameRunnable;
import com.github.tanokun.karadasagasi.util.command.Command;
import com.github.tanokun.karadasagasi.util.command.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerCommand {
    @Command(
            parentName = "kjoin",
            name = "",
            desc = ""
    )
    public void joinGame(CommandSender sender, CommandContext commandContext) {
        Player player = (Player) sender;
        GameRunnable gameRunnable = KaradaSagasi.getPlugin().getGameRunnable();
        if (gameRunnable.getPlayers().contains(player)) {
            player.sendMessage(KaradaSagasi.PX + "既に参加しています");
            return;
        }

        gameRunnable.getPlayers().add(player);
        KaradaSagasi.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        Bukkit.broadcastMessage(KaradaSagasi.PX + "§b" + player.getName() + "§bが参加しました §7[" + gameRunnable.getPlayers().size() + "/" + Bukkit.getOnlinePlayers().size() + "]");

    }

    @Command(
            parentName = "kquit",
            name = "",
            desc = ""
    )
    public void QuitGame(CommandSender sender, CommandContext commandContext) {
        Player player = (Player) sender;
        GameRunnable gameRunnable = KaradaSagasi.getPlugin().getGameRunnable();
        if (!gameRunnable.getPlayers().contains(player)) {
            player.sendMessage(KaradaSagasi.PX + "参加していません");
            return;
        }

        gameRunnable.getPlayers().remove(player);
        KaradaSagasi.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        Bukkit.broadcastMessage(KaradaSagasi.PX + "§b" + player.getName() + "§bが参加を取り消しました §7[" + gameRunnable.getPlayers().size() + "/" + Bukkit.getOnlinePlayers().size() + "]");
    }
}
