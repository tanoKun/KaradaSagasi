package com.github.tanokun.karadasagasi.game;

import com.github.tanokun.karadasagasi.BoardManager;
import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.game.stage.Stage;
import com.github.tanokun.karadasagasi.util.scoreboard.FastBoard;
import org.apache.logging.log4j.core.impl.ThreadContextDataInjector;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GameRunnable extends BukkitRunnable {
    private Stage stage;

    private final HashSet<Player> players = new HashSet<>();

    private final HashSet<Player> students = new HashSet<>();

    private final HashSet<Player> killers = new HashSet<>();

    private final HashSet<Player> diedStudents = new HashSet<>();

    private final BoardManager boardManager = new BoardManager();

    private int time;

    private State state = State.WAIT;

    private boolean helmet = false;
    private boolean chestplate = false;
    private boolean leggings = false;
    private boolean boots = false;

    public GameRunnable(Stage stage) {
        this.stage = stage;
        if (stage != null) this.time = stage.getTime();
    }

    public String start(Stage stage) {
        if (stage == null) return "ステージを選択してください";
        this.stage = stage;
        this.time = stage.getTime();

        if (state == State.GAMING) return "既にゲームが始まっています";
        state = State.GAMING;

        /*
        if (players.size() < 2) return "2人以上のプレイヤーが必要です";

        if (killers.size() < 1) return "1人以上の赤い人が必要です";
        */


        state = State.GAMING;

        Bukkit.getScheduler().runTaskLater(KaradaSagasi.getPlugin(), () -> {
            KaradaSagasi.playSound(players.toArray(new Player[players.size()]), Sound.BLOCK_ANVIL_LAND, 1, 1);
            players.stream().forEach(player -> player.sendTitle("§e-----5-----", "", 4, 20, 4));
        }, 20);

        Bukkit.getScheduler().runTaskLater(KaradaSagasi.getPlugin(), () -> {
            KaradaSagasi.playSound(players.toArray(new Player[players.size()]), Sound.BLOCK_ANVIL_LAND, 1, 1);
            players.stream().forEach(player -> player.sendTitle("§e----4----", "", 4, 20, 4));
        }, 40);

        Bukkit.getScheduler().runTaskLater(KaradaSagasi.getPlugin(), () -> {
            KaradaSagasi.playSound(players.toArray(new Player[players.size()]), Sound.BLOCK_ANVIL_LAND, 1, 1);
            players.stream().forEach(player -> player.sendTitle("§c---3---", "", 4, 20, 4));
        }, 60);

        Bukkit.getScheduler().runTaskLater(KaradaSagasi.getPlugin(), () -> {
            KaradaSagasi.playSound(players.toArray(new Player[players.size()]), Sound.BLOCK_ANVIL_LAND, 1, 1);
            players.stream().forEach(player -> player.sendTitle("§c--2--", "", 4, 20, 4));
       }, 80);

        Bukkit.getScheduler().runTaskLater(KaradaSagasi.getPlugin(), () -> {
            KaradaSagasi.playSound(players.toArray(new Player[players.size()]), Sound.BLOCK_ANVIL_LAND, 1, 1);
            players.stream().forEach(player -> player.sendTitle("§c-1-", "", 4, 20, 4));
        }, 100);

        Bukkit.getScheduler().runTaskLater(KaradaSagasi.getPlugin(), () -> {
            KaradaSagasi.playSound(players.toArray(new Player[players.size()]), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
            players.stream().forEach(player -> player.sendTitle("§c------=体探し=------", "スタート!", 4, 20, 4));
        }, 120);

        this.runTaskTimer(KaradaSagasi.getPlugin(), 0, 20);

        return null;
    }

    public void stop() {

    }

    @Override
    public void run() {

    }

    public Stage getStage() {
        return stage;
    }

    public HashSet<Player> getPlayers() {
        return players;
    }

    public HashSet<Player> getStudents() {
        return students;
    }

    public HashSet<Player> getKillers() {
        return killers;
    }

    public HashSet<Player> getDiedStudents() {
        return diedStudents;
    }

    public boolean isHelmet() {
        return helmet;
    }

    public boolean isChestplate() {
        return chestplate;
    }

    public boolean isLeggings() {
        return leggings;
    }

    public boolean isBoots() {
        return boots;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setHelmet(boolean helmet) {
        this.helmet = helmet;
    }

    public void setChestplate(boolean chestplate) {
        this.chestplate = chestplate;
    }

    public void setLeggings(boolean leggings) {
        this.leggings = leggings;
    }

    public void setBoots(boolean boots) {
        this.boots = boots;
    }

    public int getTime() {
        return time;
    }

    public State getState() {
        return state;
    }
}
