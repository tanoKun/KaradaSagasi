package com.github.tanokun.karadasagasi;

import com.comphenix.protocol.ProtocolLibrary;
import com.github.tanokun.karadasagasi.command.MainCommand;
import com.github.tanokun.karadasagasi.command.PlayerCommand;
import com.github.tanokun.karadasagasi.event.listener.WgRegionEventListener;
import com.github.tanokun.karadasagasi.game.BoardManager;
import com.github.tanokun.karadasagasi.game.GameRunnable;
import com.github.tanokun.karadasagasi.game.player.GamePlayer;
import com.github.tanokun.karadasagasi.game.player.PlayerManager;
import com.github.tanokun.karadasagasi.game.stage.StageManager;
import com.github.tanokun.karadasagasi.listener.GameListener;
import com.github.tanokun.karadasagasi.listener.StopListener;
import com.github.tanokun.karadasagasi.util.anvil.AnvilClickListener;
import com.github.tanokun.karadasagasi.util.command.CommandManager;
import com.github.tanokun.karadasagasi.util.handle.listener.HandleListener;
import com.github.tanokun.karadasagasi.util.io.Config;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.InventoryManager;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class KaradaSagasi extends JavaPlugin {
    public static final String PX = "§c[§c-｜ §c§lSystem§c ｜-§c] §7=> §c";

    private static KaradaSagasi plugin;

    private CommandManager commandManager;

    private InventoryManager inventoryManager;

    private StageManager stageManager;

    private GameRunnable gameRunnable;

    private DataManager dataManager;

    private PlayerManager playerManager;

    private Config stageConfig;

    private BoardManager boardManager;

    public void onEnable() {
        plugin = this;
        stageConfig = new Config("stage.yml", this);
        registerOthers();
        setupManagers();
        registerListeners();
        registerCommands();
        registerTasks();

        for (Player p : Bukkit.getOnlinePlayers()) {
            KaradaSagasi.getPlugin().getPlayerManager().register(new GamePlayer(p.getUniqueId()).load(new Config("pd.yml", KaradaSagasi.getPlugin()), ""));
            p.setPlayerListName("§7" + p.getName() + " §7[§dLv:§e" + KaradaSagasi.getPlugin().getPlayerManager().getGamePlayer(p.getUniqueId()).getHasLevel().getValue() + "§7]");
            KaradaSagasi.getPlugin().getBoardManager().update();
        }
    }

    public void onDisable() {
        stageManager.save(stageConfig, ""); stageConfig.saveConfig();
        dataManager.save();

        for (GamePlayer gamePlayer : playerManager.getGamePlayers().values()) {
            gamePlayer.save(new Config("pd.yml", KaradaSagasi.getPlugin()), gamePlayer.getUuid().toString());
        }
    }

    private void setupManagers() {
        commandManager = new CommandManager();

        inventoryManager = new InventoryManager(this);

        stageManager = new StageManager().load(stageConfig, "");

        dataManager = new DataManager(this);

        playerManager = new PlayerManager();

        boardManager = new BoardManager(gameRunnable);

    }

    private void registerListeners() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new AnvilClickListener());

        Bukkit.getPluginManager().registerEvents(new HandleListener(), this);

        Bukkit.getPluginManager().registerEvents(new StopListener(), this);

        Bukkit.getPluginManager().registerEvents(new GameListener(), this);

        Bukkit.getPluginManager().registerEvents(new WgRegionEventListener(this, (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard")), Bukkit.getPluginManager().getPlugin("WorldGuard"));
    }

    private void registerCommands() {
        getCommandManager().registerCommand(new MainCommand());

        getCommandManager().registerCommand(new PlayerCommand());
    }

    private void registerTasks(){
    }

    private void registerOthers() {
        gameRunnable = new GameRunnable(null);
    }

    public static KaradaSagasi getPlugin() {
        return plugin;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public StageManager getStageManager() {
        return stageManager;
    }

    public GameRunnable getGameRunnable() {
        return gameRunnable;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public BoardManager getBoardManager() {
        return boardManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!commandManager.hasCommand(command.getName())) return true;

        commandManager.getCommand(command.getName()).execute(sender, label, args);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!commandManager.hasCommand(command.getName())) return null;
        return commandManager.getCommand(command.getName()).tabComplete(sender, alias, args);
    }

    public static void playSound(Player player, Sound sound, int volume, double v2) {
        player.playSound(player.getLocation(), sound, volume, (float) v2);
    }

    public static void playSound(Player[] players, Sound sound, int volume, double v2) {
        for (Player player : players) {
            player.playSound(player.getLocation(), sound, volume, (float) v2);
        }
    }
}