package com.github.tanokun.karadasagasi.command;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.game.GameRunnable;
import com.github.tanokun.karadasagasi.game.inv.SettingStageListMenu;
import com.github.tanokun.karadasagasi.util.command.Command;
import com.github.tanokun.karadasagasi.util.command.CommandContext;
import com.github.tanokun.karadasagasi.util.command.CommandPermission;
import com.github.tanokun.karadasagasi.util.command.TabComplete;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainCommand {

    @Command(
            parentName = "k",
            name = "map",
            desc = ""
    )
    @CommandPermission(
            permission = "karadasagasi.command.map",
            perDefault = PermissionDefault.OP)
    public void openInit(CommandSender sender, CommandContext commandContext) {
        Player p = (Player) sender;
        new SettingStageListMenu().getInv().open(p);
    }

    @Command(
            parentName = "k",
            name = "setwp",
            desc = ""
    )
    @CommandPermission(
            permission = "karadasagasi.command.setwp",
            perDefault = PermissionDefault.OP)
    public void setKillerWaitingPosition(CommandSender sender, CommandContext commandContext) {
        Player player = (Player) sender;
        KaradaSagasi.getPlugin().getDataManager().setKillerWaitingPosition(player.getLocation());
        sender.sendMessage(KaradaSagasi.PX + "§b赤い人の待機場所を設定しました");
    }

    @Command(
            parentName = "k",
            name = "start",
            desc = ""
    )
    @CommandPermission(
            permission = "karadasagasi.command.setwp",
            perDefault = PermissionDefault.OP)
    public void startGame(CommandSender sender, CommandContext commandContext) {
        KaradaSagasi.getPlugin().getGameRunnable().start(null);
    }

    @Command(
            parentName = "k",
            name = "killer",
            desc = ""
    )
    @CommandPermission(
            permission = "karadasagasi.command.killer",
            perDefault = PermissionDefault.OP)
    public void setKiller(CommandSender sender, CommandContext commandContext) {
        ArrayList<Player> players = new ArrayList<>();

        if (!commandContext.getArg(0, "").equals("")) {
            Player killer = Bukkit.getPlayer(commandContext.getArg(0, ""));
            if (killer == null) {
                sender.sendMessage(KaradaSagasi.PX + "そのプレイヤーは存在しません");
                return;
            }

            KaradaSagasi.getPlugin().getGameRunnable().getKillers().add(killer);
            Bukkit.broadcastMessage(KaradaSagasi.PX + killer.getName() + "が赤い人になりました");
            killer.teleport(KaradaSagasi.getPlugin().getDataManager().getKillerWaitingPosition());
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.REDSTONE_BLOCK) players.add(player);
        }

        if (players.size() == 0) {
            Bukkit.broadcastMessage(KaradaSagasi.PX + "抽選対象者がいません！ 誰でもいいから入りましょう！");
            return;
        }

        Player killer = players.get(new Random().nextInt(players.size()));
        KaradaSagasi.getPlugin().getGameRunnable().getKillers().add(killer);
        Bukkit.broadcastMessage(KaradaSagasi.PX + killer.getName() + "が赤い人になりました");
        killer.teleport(KaradaSagasi.getPlugin().getDataManager().getKillerWaitingPosition());

    }

    @TabComplete(
            parentName = "k",
            name = "killer"
    )
    public List<String> setKillerTabComplete(CommandSender sender, CommandContext commandContext) {
        ArrayList<String> tc = new ArrayList<>();

        if (commandContext.getBaseArgs().length <= 2){
            String search = commandContext.getBaseArgs().length == 1 ? commandContext.getBaseArgs()[0] : "";
            Bukkit.getOnlinePlayers().stream()
                    .filter(t -> t.getName().startsWith(search))
                    .forEach(t -> tc.add(t.getName()));
        }
        return tc;
    }
}
