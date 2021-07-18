package com.github.tanokun.karadasagasi.util.anvil;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.github.tanokun.karadasagasi.KaradaSagasi;
import net.minecraft.server.v1_12_R1.PacketPlayInWindowClick;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class AnvilClickListener extends PacketAdapter  {
    public AnvilClickListener() {
        super(KaradaSagasi.getPlugin(), PacketType.Play.Client.CLOSE_WINDOW, PacketType.Play.Client.WINDOW_CLICK);
    }

    @Override
    public void onPacketReceiving(PacketEvent e) {
        AnvilGUI anvilGUI = AnvilGUI.activeGUIs.get(e.getPlayer().getUniqueId());
        if (anvilGUI != null) {
            if (e.getPacketType() == PacketType.Play.Client.WINDOW_CLICK) {
                if (e.getPacket().getIntegers().read(1) == 2) {
                    anvilGUI.setItem(e.getPacket().getItemModifier().read(0));
                    Bukkit.getScheduler().runTask(KaradaSagasi.getPlugin(), () -> anvilGUI.getDone().accept(anvilGUI));
                    AnvilGUI.activeGUIs.remove(e.getPlayer().getUniqueId());
                }
            } else if (e.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW) {
                AnvilGUI.activeGUIs.remove(e.getPlayer().getUniqueId());
            }
        }
    }
}
