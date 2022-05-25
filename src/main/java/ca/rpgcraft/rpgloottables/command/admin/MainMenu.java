package ca.rpgcraft.rpgloottables.command.admin;

import ca.rpgcraft.rpgloottables.RPGLootTables;
import ca.rpgcraft.rpgloottables.util.PlayerMenuManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Executes RPGLootTable commands.
 */
public class MainMenu implements CommandExecutor, TabCompleter {
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
            PlayerMenuManager playerMenuManager = new PlayerMenuManager(player);
            ca.rpgcraft.rpgloottables.menu.admin.MainMenu mainMenu = new ca.rpgcraft.rpgloottables.menu.admin.MainMenu(playerMenuManager);
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(!command.getName().equalsIgnoreCase("rpgloot")
        && !command.getName().equalsIgnoreCase("rpgl")
        && !command.getName().equalsIgnoreCase("rloot")
        && !command.getName().equalsIgnoreCase("rl")) return null;
        if(args.length == 1){
            List<String> completions = new ArrayList<>();
            completions.add("menu");

            List<String> result = new ArrayList<>();

            for(String a : completions){
                if(a.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(a);
            }
            return result;
        }
        return null;
    }
}
