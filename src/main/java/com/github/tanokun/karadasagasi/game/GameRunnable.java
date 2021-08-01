package com.github.tanokun.karadasagasi.game;

import com.github.tanokun.karadasagasi.KaradaSagasi;
import com.github.tanokun.karadasagasi.game.player.GamePlayer;
import com.github.tanokun.karadasagasi.game.player.TempPlayer;
import com.github.tanokun.karadasagasi.game.stage.Stage;
import com.github.tanokun.karadasagasi.game.stage.teleport.TeleportLocation;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;

public class GameRunnable {
    private Stage stage;

    private Hologram hologram;

    private final HashSet<Player> players = new HashSet<>();

    private final HashSet<Player> students = new HashSet<>();

    private final HashSet<Player> killers = new HashSet<>();

    private final HashSet<Player> diedStudents = new HashSet<>();

    private int time;

    private State state = State.WAIT;

    private Player helmet = null;
    private Player chestplate = null;
    private Player leggings = null;
    private Player boots = null;

    private BukkitTask task;

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

        Bukkit.getScheduler().runTaskLater(KaradaSagasi.getPlugin(), () -> {
            players.stream().filter(p -> !killers.contains(p)).forEach(students::add);

            KaradaSagasi.getPlugin().getBoardManager().update();

            task = Bukkit.getScheduler().runTaskTimer(KaradaSagasi.getPlugin(), new Runnable(this), 1, 20);

            for(Player p : players) {
                GamePlayer gamePlayer = KaradaSagasi.getPlugin().getPlayerManager().getGamePlayer(p.getUniqueId());
                gamePlayer.setJoinGameCount(gamePlayer.getJoinGameCount() + 1);
                if (getStudents().contains(p)) {
                    p.setPlayerListName("§b[生徒] " + p.getName() + " §7[§dLv:§e" + gamePlayer.getHasLevel().getValue() + "§7]");
                    p.teleport(stage.getSpawnStudentLocation());
                } else if (getKillers().contains(p)) {
                    p.setPlayerListName("§c[赤い人] " + p.getName() + " §7[§dLv:§e" + gamePlayer.getHasLevel().getValue() + "§7]");
                }
            }
        }, 120);

        for (Location location : stage.getChestLocationMap().getFirstFloorChestLocations()) {
            if (location.getWorld().getBlockAt(location).getType() == Material.CHEST) continue;
            location.getWorld().getBlockAt(location).setType(Material.CHEST);
        }

        for (Location location : stage.getChestLocationMap().getSecondFloorChestLocations()) {
            if (location.getWorld().getBlockAt(location).getType() == Material.CHEST) continue;
            location.getWorld().getBlockAt(location).setType(Material.CHEST);
        }

        for (Location location : stage.getChestLocationMap().getThirdFloorChestLocations()) {
            if (location.getWorld().getBlockAt(location).getType() == Material.CHEST) continue;
            location.getWorld().getBlockAt(location).setType(Material.CHEST);
        }

        for (Location location : stage.getChestLocationMap().getFourthFloorChestLocations()) {
            if (location.getWorld().getBlockAt(location).getType() == Material.CHEST) continue;
            location.getWorld().getBlockAt(location).setType(Material.CHEST);
        }

        Bukkit.getScheduler().runTaskLater(KaradaSagasi.getPlugin(), () -> {
            if (state != State.GAMING) return;
            for(Player p : players) {
                p.sendMessage(KaradaSagasi.PX + "20秒後に赤い人が現れます");
            }
        }, 140);

        Bukkit.getScheduler().runTaskLater(KaradaSagasi.getPlugin(), () -> {
            new BukkitRunnable() {
                int t = 0;
                public void run() {
                    if (state != State.GAMING) {
                        cancel();
                        return;
                    }

                    switch (t){
                        case 0:
                            Bukkit.broadcastMessage(KaradaSagasi.PX + "ピーン");
                            break;
                        case 1:
                        case 3:
                            Bukkit.broadcastMessage(KaradaSagasi.PX + "ポーン");
                            break;
                        case 2:
                            Bukkit.broadcastMessage(KaradaSagasi.PX + "パーン");
                            break;
                        case 4:
                            Bukkit.broadcastMessage(KaradaSagasi.PX + "赤い人がこの学校のどこかに現れました");
                            for (Player p : killers) {
                                ArrayList<TeleportLocation> locs = stage.getKillerTeleportLocationMap().getKillerTeleportLocations();
                                p.teleport(locs.toArray(new TeleportLocation[locs.size()])[new Random().nextInt(locs.size())].getLocation());
                            }
                            break;
                        default:
                            cancel();
                            break;
                    }
                    t++;
                }
            }.runTaskTimer(KaradaSagasi.getPlugin(), 1, 20);
        }, 540);

        stage.getBodyLocation().getWorld().getBlockAt(stage.getBodyLocation()).setType(Material.CHEST);
        hologram = HologramsAPI.createHologram(KaradaSagasi.getPlugin(), stage.getBodyLocation().clone().add(0, 2.3, 0));
        hologram.appendTextLine("下のチェストに§c見つけた体§rを入れよう");

        ArrayList<Location> locs = stage.getChestLocationMap().getFirstFloorChestLocations();
        Location bodyLoc = locs.toArray(new Location[locs.size()])[new Random().nextInt(locs.size())];
        Chest chest = (Chest) bodyLoc.getWorld().getBlockAt(bodyLoc).getState();
        chest.getBlockInventory().setItem(new Random().nextInt(27), new ItemStack(Material.DIAMOND_BOOTS));

        locs = stage.getChestLocationMap().getSecondFloorChestLocations();
        bodyLoc = locs.toArray(new Location[locs.size()])[new Random().nextInt(locs.size())];
        chest = (Chest) bodyLoc.getWorld().getBlockAt(bodyLoc).getState();
        chest.getBlockInventory().setItem(new Random().nextInt(27), new ItemStack(Material.DIAMOND_LEGGINGS));

        locs = stage.getChestLocationMap().getThirdFloorChestLocations();
        bodyLoc = locs.toArray(new Location[locs.size()])[new Random().nextInt(locs.size())];
        chest = (Chest) bodyLoc.getWorld().getBlockAt(bodyLoc).getState();
        chest.getBlockInventory().setItem(new Random().nextInt(27), new ItemStack(Material.DIAMOND_CHESTPLATE));

        locs = stage.getChestLocationMap().getFourthFloorChestLocations();
        bodyLoc = locs.toArray(new Location[locs.size()])[new Random().nextInt(locs.size())];
        chest = (Chest) bodyLoc.getWorld().getBlockAt(bodyLoc).getState();
        chest.getBlockInventory().setItem(new Random().nextInt(27), new ItemStack(Material.DIAMOND_HELMET));

        for (Player p : killers) {
            p.getInventory().setItem(1, new ItemStack(Material.IRON_SWORD));
            ItemStack helmet = new ItemStack(Material.LEATHER_HELMET); LeatherArmorMeta leather = (LeatherArmorMeta) helmet.getItemMeta(); leather.setColor(Color.RED);
            ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE); leather = (LeatherArmorMeta) chestplate.getItemMeta(); leather.setColor(Color.RED);
            ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS); leather = (LeatherArmorMeta) leggings.getItemMeta(); leather.setColor(Color.RED);
            ItemStack boots = new ItemStack(Material.LEATHER_BOOTS); leather = (LeatherArmorMeta) boots.getItemMeta(); leather.setColor(Color.RED);
            p.getInventory().setHelmet(helmet);
            p.getInventory().setChestplate(chestplate);
            p.getInventory().setLeggings(leggings);
            p.getInventory().setBoots(boots);
        }

        return null;
    }

    public String stop(GameEndState endState) {
        if (state == State.WAIT) return "試合が始まっていません";

        Bukkit.getScheduler().cancelTask(task.getTaskId());

        for (Player p: players) {
            GamePlayer gamePlayer = KaradaSagasi.getPlugin().getPlayerManager().getGamePlayer(p.getUniqueId());
            gamePlayer.setTempPlayer(new TempPlayer());
            p.setPlayerListName("§7" + p.getName() + " §7[§dLv:§e" + KaradaSagasi.getPlugin().getPlayerManager().getGamePlayer(p.getUniqueId()).getHasLevel().getValue() + "§7]");
            Bukkit.getOnlinePlayers().stream().forEach(player -> KaradaSagasi.playSound(player, Sound.ENTITY_WOLF_HOWL, 1, 1));
            p.getInventory().clear();
        }

        if (endState == GameEndState.DRAW) {
            Bukkit.getOnlinePlayers().stream().forEach(player -> player.sendTitle("§c体探し終了！", "§6§l引き分け", 4, 20, 4));
        } else if (endState == GameEndState.KILLER) {
            Bukkit.getOnlinePlayers().stream().forEach(player -> player.sendTitle("§c体探し終了！", "§c§l赤い人の勝利", 4, 20, 4));
            for (Player p : killers) {
                GamePlayer gamePlayer = KaradaSagasi.getPlugin().getPlayerManager().getGamePlayer(p.getUniqueId());
                gamePlayer.setWinKillerCount(gamePlayer.getWinKillerCount() + 1);
            }
        } else if (endState == GameEndState.STUDENT) {
            Bukkit.getOnlinePlayers().stream().forEach(player -> player.sendTitle("§c体探し終了！", "§b§l生徒の勝利", 4, 20, 4));
            for (Player p : students) {
                GamePlayer gamePlayer = KaradaSagasi.getPlugin().getPlayerManager().getGamePlayer(p.getUniqueId());
                gamePlayer.setWinStudentCount(gamePlayer.getWinStudentCount() + 1);
            }
        }

        Bukkit.getScheduler().runTaskLater(KaradaSagasi.getPlugin(), () -> {
            KaradaSagasi.playSound(Bukkit.getOnlinePlayers().toArray(new Player[players.size()]), Sound.BLOCK_ANVIL_LAND, 1, 1.5);
            Bukkit.broadcastMessage("§c§m                     ");
            Bukkit.broadcastMessage("§c頭: " + (helmet == null ? "いませんでした" : helmet.getName()));
            Bukkit.broadcastMessage("§c胸: " + (chestplate == null ? "いませんでした" : chestplate.getName()));
            Bukkit.broadcastMessage("§c股: " + (leggings == null ? "いませんでした" : leggings.getName()));
            Bukkit.broadcastMessage("§c足: " + (boots == null ? "いませんでした" : boots.getName()));

            Bukkit.broadcastMessage("§c§m                     ");

            Bukkit.getScheduler().scheduleSyncDelayedTask(KaradaSagasi.getPlugin(), () -> {
                for (Player p : players) p.teleport(KaradaSagasi.getPlugin().getDataManager().getLobbyPosition());
                players.clear();
                students.clear();
                killers.clear();
                diedStudents.clear();
            }, 60);


            helmet = null;
            chestplate = null;
            leggings = null;
            boots = null;

        }, 20);

        for (Location location : stage.getChestLocationMap().getFirstFloorChestLocations()) {
            Chest chest = (Chest) location.getWorld().getBlockAt(location).getState();
            chest.getBlockInventory().clear();
        }

        for (Location location : stage.getChestLocationMap().getSecondFloorChestLocations()) {
            Chest chest = (Chest) location.getWorld().getBlockAt(location).getState();
            chest.getBlockInventory().clear();
        }

        for (Location location : stage.getChestLocationMap().getThirdFloorChestLocations()) {
            Chest chest = (Chest) location.getWorld().getBlockAt(location).getState();
            chest.getBlockInventory().clear();
        }

        for (Location location : stage.getChestLocationMap().getFourthFloorChestLocations()) {
            Chest chest = (Chest) location.getWorld().getBlockAt(location).getState();
            chest.getBlockInventory().clear();
        }

        for (Entity entity : stage.getBodyLocation().getWorld().getEntities()) if (entity instanceof Item) entity.remove();

        hologram.delete(); hologram = null;

        state = State.WAIT;
        time = 0;

        stage = null;

        KaradaSagasi.getPlugin().getBoardManager().update();

        return null;
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
        return helmet != null;
    }

    public boolean isChestplate() {
        return chestplate != null;
    }

    public boolean isLeggings() {
        return leggings != null;
    }

    public boolean isBoots() {
        return boots != null;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setHelmet(Player helmet) {
        this.helmet = helmet;
    }

    public void setChestplate(Player chestplate) {
        this.chestplate = chestplate;
    }

    public void setLeggings(Player leggings) {
        this.leggings = leggings;
    }

    public void setBoots(Player boots) {
        this.boots = boots;
    }

    public int getTime() {
        return time;
    }

    public State getState() {
        return state;
    }

    public BukkitTask getTask() {
        return task;
    }
}

class Runnable implements java.lang.Runnable {
    final GameRunnable gameRunnable;

    Runnable(GameRunnable gameRunnable) {
        this.gameRunnable = gameRunnable;
    }

    public void run() {
        gameRunnable.setTime(gameRunnable.getTime() - 1);
        KaradaSagasi.getPlugin().getBoardManager().update();
        if (gameRunnable.getTime() <= 0) {
            int body = 0;
            System.out.println(gameRunnable.isHelmet());
            if (gameRunnable.isHelmet()) body++;
            if (gameRunnable.isChestplate()) body++;
            if (gameRunnable.isLeggings()) body++;
            if (gameRunnable.isBoots()) body++;
            if (body >= 3) {
                Bukkit.getScheduler().runTaskLater(KaradaSagasi.getPlugin(), () -> {
                    gameRunnable.stop(GameEndState.STUDENT);
                }, 40);
                Bukkit.broadcastMessage(KaradaSagasi.PX + "§b集まった体が半数を超えたため...");
                Bukkit.getOnlinePlayers().stream().forEach(player -> player.sendTitle("  ", "§b集まった体が半数を超えたため...", 4, 20, 4));
                gameRunnable.getTask().cancel();
            }
            else {
                Bukkit.getScheduler().runTaskLater(KaradaSagasi.getPlugin(), () -> {
                    gameRunnable.stop(GameEndState.DRAW);
                }, 40);
                Bukkit.broadcastMessage(KaradaSagasi.PX + "§b集まった体が半数を超えなかったため...");
                Bukkit.getOnlinePlayers().stream().forEach(player -> player.sendTitle("  ", "§b集まった体が半数を超えなかったため...", 4, 20, 4));
                gameRunnable.getTask().cancel();
            }
        }

        if (gameRunnable.getStudents().size() == gameRunnable.getDiedStudents().size()) {
            Bukkit.getScheduler().runTaskLater(KaradaSagasi.getPlugin(), () -> {
                gameRunnable.stop(GameEndState.KILLER);
            }, 80);
            Bukkit.broadcastMessage(KaradaSagasi.PX + "§c生徒が全滅してしまいました...");
            Bukkit.getOnlinePlayers().stream().forEach(player -> player.sendTitle("  ", "§c生徒が全滅してしまいました...", 4, 20, 4));
            gameRunnable.getTask().cancel();
        }
    }
}
