package net.euphalys.core.api.listener.inventory;

import net.euphalys.api.player.IEuphalysPlayer;
import net.euphalys.core.api.EuphalysApi;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftInventoryCustom;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.UUID;

/**
 * @author Dinnerwolph
 */

public class Click implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;
        if (event.getInventory() == null) return;
        if (event.getInventory() instanceof CraftingInventory) return;
        String name = event.getView().getTitle();
        if (name.contains("sanctions #")) {
            ItemStack itemStack = event.getCurrentItem();
            event.setCancelled(true);
            if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("Validé")) {
                int id = Integer.parseInt(event.getInventory().getName().replaceAll("[^\\d.]", ""));
                EuphalysApi.getInstance().getSanctionsManager().removesanction(id);
                UUID target = EuphalysApi.getInstance().getUUUIDTranslator().getUUID(name.split(" ")[2]);
                IEuphalysPlayer targetplayer = EuphalysApi.getInstance().getPlayer(target);
                Inventory inventory = Bukkit.createInventory(event.getWhoClicked(), 9 * 6, name.split(" ")[2] + " sanctions");
                targetplayer.getSanctions().forEach(sanctions -> {
                    ItemStack item = new ItemStack(Material.PAPER);
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setDisplayName("#" + sanctions.getSanctionsId());
                    itemMeta.setLore(Arrays.asList(sanctions.getType().name, sanctions.getServer(), sanctions.getDuration() + "", sanctions.getMessage()));
                    item.setItemMeta(itemMeta);
                    inventory.addItem(item);
                });
                event.getWhoClicked().openInventory(inventory);
            } else if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("Annulé")) {
                UUID target = EuphalysApi.getInstance().getUUUIDTranslator().getUUID(name.split(" ")[2]);
                IEuphalysPlayer targetplayer = EuphalysApi.getInstance().getPlayer(target);
                Inventory inventory = Bukkit.createInventory(event.getWhoClicked(), 9 * 6, name.split(" ")[2] + " sanctions");
                targetplayer.getSanctions().forEach(sanctions -> {
                    ItemStack item = new ItemStack(Material.PAPER);
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setDisplayName("#" + sanctions.getSanctionsId());
                    itemMeta.setLore(Arrays.asList(sanctions.getType().name, sanctions.getServer(), sanctions.getDuration() + "", sanctions.getMessage()));
                    item.setItemMeta(itemMeta);
                    inventory.addItem(item);
                });
                event.getWhoClicked().openInventory(inventory);

            }
        } else if (name.contains("sanctions")) {
            event.setCancelled(true);
            ItemStack itemStack = event.getCurrentItem();
            int id = Integer.parseInt(itemStack.getItemMeta().getDisplayName().substring(1));
            Inventory inventory = Bukkit.createInventory(null, 6 * 9, "sanctions #" + id + " " + event.getInventory().getName().split(" ")[0]);
            ItemStack itemStackv = new ItemStack(Material.STAINED_GLASS, 1, (short) 5);
            ItemMeta meta = itemStackv.getItemMeta();
            meta.setDisplayName("Validé");
            itemStackv.setItemMeta(meta);
            ItemStack itemStackr = new ItemStack(Material.STAINED_GLASS, 1, (short) 14);
            meta = itemStackr.getItemMeta();
            meta.setDisplayName("Annulé");
            itemStackr.setItemMeta(meta);
            inventory.setItem(22, itemStack);
            inventory.setItem(38, itemStackv);
            inventory.setItem(42, itemStackr);
            event.getWhoClicked().openInventory(inventory);
        }
    }
}
