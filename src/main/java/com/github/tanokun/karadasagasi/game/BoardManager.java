package com.github.tanokun.karadasagasi.game;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.game.GameRunnable;
import com.github.tanokun.karadasagasi.game.player.GamePlayer;
import com.github.tanokun.karadasagasi.util.scoreboard.FastBoard;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

public class BoardManager {
    private final GameRunnable gameRunnable;
    private final HashMap<Player, FastBoard> boards = new HashMap<>();

    public BoardManager(GameRunnable gameRunnable) {
        this.gameRunnable = gameRunnable;
    }

    public void update() {
        int m = this.gameRunnable.getTime() / 60;
        int s = this.gameRunnable.getTime() % 60;

        String helmet = !gameRunnable.isHelmet() ?         "§c✖ §l頭  4階" : "§a✔ §l頭  4階";
        String chestplate = !gameRunnable.isChestplate() ? "§c✖ §l胸  3階" : "§a✔ §l胸  3階";
        String leggings = !gameRunnable.isLeggings() ?     "§c✖ §l股  2階" : "§a✔ §l股  2階";
        String boots = !gameRunnable.isBoots() ?           "§c✖ §l足  1階" : "§a✔ §l足  1階";

        if (gameRunnable.getState() == State.GAMING) {
            for (Player player : boards.keySet()) {
                if (!gameRunnable.getStudents().contains(player)) continue;
                FastBoard fastBoard = boards.get(player);
                if (fastBoard == null) fastBoard = new FastBoard(player);
                fastBoard.updateTitle("§c§l体探し");
                fastBoard.updateLines(Arrays.asList(
                        "§7§m                 ",
                        "§6役職: §b生徒",
                        "§6時間: §b" + m + "分" + s + "秒",
                        " ",
                        "§c赤い人の数: §c" + gameRunnable.getKillers().size() + "人",
                        "§b生徒数: §b" + gameRunnable.getStudents().size() + "人",
                        "§7死亡者数: §7" + gameRunnable.getDiedStudents().size() + "人",
                        "§7§m                 ",
                        helmet,
                        chestplate,
                        leggings,
                        boots,
                        "§7§m                 "
                ));
                boards.put(player, fastBoard);
            }

            for (Player player : boards.keySet()) {
                if (!gameRunnable.getKillers().contains(player)) continue;
                FastBoard fastBoard = boards.get(player);
                if (fastBoard == null) fastBoard = new FastBoard(player);
                fastBoard.updateTitle("§c§l体探し");
                fastBoard.updateLines(Arrays.asList(
                        "§7§m                 ",
                        "§6役職: §c赤い人",
                        "§6時間: §b" + m + "分" + s + "秒",
                        " ",
                        "§c赤い人の数: §c" + gameRunnable.getKillers().size() + "人",
                        "§b生徒数: §b" + gameRunnable.getStudents().size() + "人",
                        "§7死亡者数: §7" + gameRunnable.getDiedStudents().size() + "人",
                        "§7§m                 ",
                        helmet,
                        chestplate,
                        leggings,
                        boots,
                        "§7§m                 "
                ));
                boards.put(player, fastBoard);
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (gameRunnable.getKillers().contains(player) || gameRunnable.getStudents().contains(player)) continue;
                FastBoard fastBoard = boards.get(player);
                if (fastBoard == null) fastBoard = new FastBoard(player);
                fastBoard.updateTitle("§c§l体探し");
                fastBoard.updateLines(Arrays.asList(
                        "§7§m                 ",
                        "§6役職: §c観戦者",
                        "§6時間: §b" + m + "分" + s + "秒",
                        " ",
                        "§c赤い人の数: §c" + gameRunnable.getKillers().size() + "人",
                        "§b生徒数: §b" + gameRunnable.getStudents().size() + "人",
                        "§7死亡者数: §7" + gameRunnable.getDiedStudents().size() + "人",
                        "§7§m                 ",
                        helmet,
                        chestplate,
                        leggings,
                        boots,
                        "§7§m                 "
                ));
                boards.put(player, fastBoard);
            }

        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                FastBoard fastBoard = boards.get(player);
                GamePlayer gamePlayer = KaradaSagasi.getPlugin().getPlayerManager().getGamePlayer(player.getUniqueId());
                if (fastBoard == null) fastBoard = new FastBoard(player);
                fastBoard.updateTitle("§c§l体探し");
                fastBoard.updateLines(Arrays.asList(
                        "§7§mーーーーーーーーーーーーーーーー",
                        "§6役職: §cゲームが始まっていません",
                        "§6Lv: " + gamePlayer.getHasLevel().getValue() + " §7(" + gamePlayer.getHasEXP() + "/" + gamePlayer.getHasLevel().getMaxEXP() + ")",
                        "§7§mーーーーーーーーーーーーーーーー",
                        "§b参加回数: " + gamePlayer.getJoinGameCount() + "回",
                        "§b生徒陣営の勝利回数: " + gamePlayer.getWinStudentCount() + "回",
                        "§c赤い人陣営の勝利回数: " + gamePlayer.getWinKillerCount() + "回",
                        "§c生徒を殺した回数: " + gamePlayer.getKillStudentCount() + "回",
                        "§7§mーーーーーーーーーーーーーーーー"
                ));
                boards.put(player, fastBoard);
            }
        }
    }

    public void remove(Player player) {
        boards.remove(player).delete();
    }
}
