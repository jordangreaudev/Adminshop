package fr.pharahosting.admionshop;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ShopReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("shop.admin")) {
            sender.sendMessage("§cTu n'as pas la permission.");
            return true;
        }

        Main.getInstance().reloadConfig();
        sender.sendMessage("§a[PharaShop] Configuration rechargée avec succès.");
        return true;
    }
}