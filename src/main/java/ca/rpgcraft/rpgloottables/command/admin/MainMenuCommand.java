package ca.rpgcraft.rpgloottables.command.admin;

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
        if(!player.hasPermission("rpgloot.admin")
        && !player.isOp()){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permission to do that."));
        }

        if(args.length == 0) return false;

        if(args[0].equalsIgnoreCase("menu")){
            PlayerMenuUtility playerMenuUtility = new PlayerMenuUtility(player);
            MainMenu mainMenu = new MainMenu(playerMenuUtility);
            mainMenu.open();
        }

        return false;
    }
}
