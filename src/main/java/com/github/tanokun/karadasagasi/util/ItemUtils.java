package com.github.tanokun.karadasagasi.util;

import com.google.gson.Gson;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemUtils implements Listener {
    private static Gson gson = new Gson();

    public static ItemStack createItem(Material material, String name, int count, boolean glowing){
        ItemStack is = new ItemStack(material);
        is.setAmount(count);

        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        if (glowing) {im.addEnchant(Enchantment.MENDING, 1, true);}
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack createItem(Material material, String name, List<String> lore, int count, boolean glowing){
        ItemStack is = new ItemStack(material);
        is.setAmount(count);

        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
        if (glowing) {im.addEnchant(Enchantment.MENDING, 1, true);}
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack createItem(Material material, String name, int count, boolean glowing, DyeColor color){
        ItemStack is = new ItemStack(material, count, color.getWoolData());
        is.setAmount(count);

        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        if (glowing) {im.addEnchant(Enchantment.MENDING, 1, true);}
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack createItem(Material material, String name, List<String> lore, int count, boolean glowing, DyeColor color){
        ItemStack is = new ItemStack(material, count, color.getWoolData());
        is.setAmount(count);

        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        im.setLore(lore);
        if (glowing) {im.addEnchant(Enchantment.MENDING, 1, true);}
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS,
                ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        is.setItemMeta(im);
        return is;
    }
}
