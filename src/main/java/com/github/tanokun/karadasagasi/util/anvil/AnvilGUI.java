package com.github.tanokun.karadasagasi.util.anvil;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.common.collect.Maps;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;

public class AnvilGUI {

    public static final HashMap<UUID, AnvilGUI> activeGUIs = Maps.newHashMap();

    private int windowId;
    private String name;
    private ItemStack item;
    private Player viewer;

    private final Consumer<AnvilGUI> done;

    public AnvilGUI(String name, ItemStack item, Consumer<AnvilGUI> done) {
        this.windowId = -new Random().nextInt(127) - 1;
        this.name = name;
        this.item = item;
        this.done = done;
    }

    public boolean open(Player player) {
        PacketContainer openAnvil = new PacketContainer(PacketType.Play.Server.OPEN_WINDOW);
        openAnvil.getIntegers().write(0, this.windowId);
        openAnvil.getStrings().write(0, "minecraft:anvil");
        openAnvil.getChatComponents().write(0, WrappedChatComponent.fromText(this.name));
        PacketContainer windowsItems = new PacketContainer(PacketType.Play.Server.WINDOW_ITEMS);
        windowsItems.getIntegers().write(0, this.windowId);
        windowsItems.getItemListModifier().write(0, Collections.singletonList(item));
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, openAnvil);
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, windowsItems);
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
        activeGUIs.put(player.getUniqueId(), this);
        this.viewer = player;
        return true;
    }

    public ItemStack getItem() {return item;}
    public void setItem(ItemStack item) {this.item = item;}

    public Player getViewer() {return viewer;}

    public String getName() {return name;}

    public int getWindowId() {return windowId;}

    public Consumer<AnvilGUI> getDone() {return done;}
}
