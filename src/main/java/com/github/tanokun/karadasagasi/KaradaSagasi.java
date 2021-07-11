package com.github.tanokun.karadasagasi;

import com.github.tanokun.karadasagasi.util.command.CommandManager;
import com.github.tanokun.karadasagasi.util.smart_inv.inv.InventoryManager;
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

    public void onEnable() {
        plugin = this;
        setupManagers();
        registerListeners();
        registerCommands();
        registerTasks();
        registerOthers();
    }

    public void onDisable() { }

    private void setupManagers() {
        commandManager = new CommandManager(this);

        inventoryManager = new InventoryManager(this);
    }

    private void registerListeners() {
    }

    private void registerCommands() {
    }

    private void registerTasks(){
    }

    private void registerOthers() {
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

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void setInventoryManager(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
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