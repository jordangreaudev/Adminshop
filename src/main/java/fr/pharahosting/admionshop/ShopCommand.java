package fr.pharahosting.admionshop;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

public class ShopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cSeuls les joueurs peuvent utiliser cette commande.");
            return true;
        }
        openMainShop((Player) sender);
        return true;
    }

    public static void openMainShop(Player p) {
        Inventory inv = Bukkit.createInventory(null, 54, "§aShop Principal");

        inv.setItem(0, ShopListener.createGuiItem("STONE", "Blocs", 0, false, false));
        inv.setItem(1, ShopListener.createGuiItem("LOG", "Bois", 0, false, false)); // 'LOG' au lieu de OAK_LOG
        inv.setItem(2, ShopListener.createGuiItem("DIAMOND", "Minerais", 0, false, false));
        inv.setItem(3, ShopListener.createGuiItem("BREAD_0", "Nourriture", 0, false, false));
        inv.setItem(4, ShopListener.createGuiItem("WHEAT", "Agriculture", 0, false, false));
        inv.setItem(5, ShopListener.createGuiItem("WOOL", "Decoration", 0, false, false)); // 'WOOL' au lieu de WHITE_WOOL
        inv.setItem(6, ShopListener.createGuiItem("REDSTONE", "Redstone", 0, false, false));
        inv.setItem(7, ShopListener.createGuiItem("NETHERRACK", "Nether", 0, false, false));
        inv.setItem(8, ShopListener.createGuiItem("LEATHER_0", "Drops", 0, false, false));
        inv.setItem(9, ShopListener.createGuiItem("BUCKET", "Divers", 0, false, false));

        p.openInventory(inv);
    }
}