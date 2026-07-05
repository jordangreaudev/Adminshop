package fr.pharahosting.admionshop;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;

public class ShopListener implements Listener {

    public static ItemStack createGuiItem(String key, String name, double price, boolean buyable, boolean sellable) {
        String[] parts = key.split("_");
        String matName = (parts.length >= 3) ? parts[0] + "_" + parts[1] : parts[0];
        Material mat = Material.matchMaterial(matName);

        short data = 0;
        try {
            data = Short.parseShort(parts[parts.length - 1]);
        } catch (NumberFormatException ignored) {}

        if (mat == null) mat = Material.STONE;

        ItemStack item = new ItemStack(mat, 1, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);

        if (price > 0) {
            meta.setLore(Arrays.asList(
                    "§aAchat: " + (buyable ? price + "$" : "Non"),
                    "§cVente: " + (sellable ? (price / 2) + "$" : "Non"),
                    "§7L-Clic: 1 | Shift-Clic: 64"
            ));
        } else {
            meta.setLore(Collections.singletonList("§7Cliquez pour interagir."));
        }
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle() == null || !e.getView().getTitle().startsWith("§a")) return;
        e.setCancelled(true);
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;

        Player p = (Player) e.getWhoClicked();
        String title = ChatColor.stripColor(e.getView().getTitle());
        String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

        if (name.equals("Retour")) {
            ShopCommand.openMainShop(p);
            return;
        }

        if (title.equals("Shop Principal")) {
            openCategory(p, name.toLowerCase());
        } else {
            String cat = title.toLowerCase();
            ItemStack clicked = e.getCurrentItem();
            String itemKey = clicked.getType().name() + "_" + clicked.getDurability();

            Main.getInstance().getLogger().info("Clic sur: " + itemKey + " dans la catégorie: " + cat);

            if (!Main.getInstance().getConfig().contains("shop." + cat + "." + itemKey)) {
                p.sendMessage("§cErreur: Cet item n'est pas configuré correctement.");
                return;
            }

            double price = Main.getInstance().getConfig().getDouble("shop." + cat + "." + itemKey + ".price");
            boolean buyable = Main.getInstance().getConfig().getBoolean("shop." + cat + "." + itemKey + ".buyable");
            boolean sellable = Main.getInstance().getConfig().getBoolean("shop." + cat + "." + itemKey + ".sellable");

            int amount = e.isShiftClick() ? 64 : 1;

            if (e.isLeftClick() && buyable) {
                if (Main.getEconomy().getBalance(p) >= (price * amount)) {
                    Main.getEconomy().withdrawPlayer(p, price * amount);
                    p.getInventory().addItem(new ItemStack(clicked.getType(), amount, clicked.getDurability()));
                    p.sendMessage("§aAchat de " + amount + " effectué.");
                } else {
                    p.sendMessage("§cPas assez d'argent !");
                }
            } else if (e.isRightClick() && sellable) {
                // Création d'un ItemStack propre pour la vérification
                ItemStack itemToSell = new ItemStack(clicked.getType(), amount, clicked.getDurability());

                if (p.getInventory().containsAtLeast(itemToSell, amount)) {
                    p.getInventory().removeItem(itemToSell);
                    Main.getEconomy().depositPlayer(p, (price / 2) * amount);
                    p.sendMessage("§eVente de " + amount + " effectuée.");
                } else {
                    p.sendMessage("§cTu n'as pas assez d'items !");
                }
            }
        }
    }

    public void openCategory(Player p, String cat) {
        Inventory inv = Bukkit.createInventory(null, 54, "§a" + cat);
        if (Main.getInstance().getConfig().contains("shop." + cat)) {
            for (String key : Main.getInstance().getConfig().getConfigurationSection("shop." + cat).getKeys(false)) {
                double price = Main.getInstance().getConfig().getDouble("shop." + cat + "." + key + ".price");
                boolean b = Main.getInstance().getConfig().getBoolean("shop." + cat + "." + key + ".buyable");
                boolean s = Main.getInstance().getConfig().getBoolean("shop." + cat + "." + key + ".sellable");
                inv.addItem(createGuiItem(key, "§f" + key.replace("_", " "), price, b, s));
            }
        }
        inv.setItem(8, createGuiItem("BOOK", "Retour", 0, false, false));
        p.openInventory(inv);
    }
}