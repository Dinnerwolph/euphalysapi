package net.euphalys.core.api.commands;

import net.euphalys.api.report.IReport;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * @author Dinnerwolph
 */

public class ListReportCommands extends AbstractCommands {

    public ListReportCommands() {
        super("listreport", "euphalys.cmd.listreport");
    }

    @Override
    public boolean onCommand(Player player, String[] strings) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 6, "report");
        List<IReport> reports = api.getReportManager().getAllsReports();
        reports.forEach(report -> {
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("#" + report.getReportId());
            itemMeta.setLore(Arrays.asList(report.getMessage(), api.getUUUIDTranslator().getName(report.getReportedBy()), api.getUUUIDTranslator().getName(report.getTarget())));
            itemStack.setItemMeta(itemMeta);
            inventory.addItem(itemStack);
        });
        player.openInventory(inventory);
        return true;
    }

    @Override
    protected void displayHelp() {

    }
}
