package ca.rpgcraft.rpgloottables.command.admin;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.menu.admin.MainMenu;
import ca.rpgcraft.rpgloottables.util.PlayerMenuUtility;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Executes RPGLootTable commands.
 */
public class MainMenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!command.getName().equalsIgnoreCase("rpgloot")
        && !command.getName().equalsIgnoreCase("rpgl")
        && !command.getName().equalsIgnoreCase("rloot")
        && !command.getName().equalsIgnoreCase("rl")) return false;
        if(!(sender instanceof Player player)){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cOnly players can use this command."));
            return false;
        }

        if(args.length == 0) return false;

        if(args[0].equalsIgnoreCase("menu")
        || args[0].equalsIgnoreCase("m")){
            if(!player.hasPermission("rpgloot.admin")
                    && !player.isOp()){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to do that."));
                return false;
            }
            PlayerMenuUtility playerMenuUtility = new PlayerMenuUtility(player);
            MainMenu mainMenu = new MainMenu(playerMenuUtility);
            mainMenu.open();
            return false;
        }

        if(args[0].equalsIgnoreCase("version")
        || args[0].equalsIgnoreCase("ver")
        || args[0].equalsIgnoreCase("v")){
            if(!player.hasPermission("rpgloot.version")){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to do that."));
                return false;
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Authors:&a dSevvv, Schmidt."));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + RPGLootTables.getPlugin(RPGLootTables.class).getName() + " &av" + RPGLootTables.getPlugin(RPGLootTables.class).getDescription().getVersion()) + ".");
            return false;
        }

        return false;
    }
}
