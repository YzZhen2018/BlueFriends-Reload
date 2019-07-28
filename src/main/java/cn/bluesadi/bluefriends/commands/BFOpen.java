package cn.bluesadi.bluefriends.commands;

import cn.bluesadi.bluefriends.BlueFriends;
import cn.bluesadi.bluefriends.gui.BluesGui;
import cn.bluesadi.bluefriends.gui.GuiManager;
import cn.bluesadi.bluefriends.player.BFPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import static cn.bluesadi.bluefriends.config.Message.*;

public class BFOpen implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length >= 1) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (sender.hasPermission("bluefriends.open.*") || sender.hasPermission("bluefriends.open." + args[0])) {
                    BluesGui gui = BlueFriends.getGuiManager().createGui(args[0],player);
                    if(gui != null){
                        gui.open();
                    }else{
                        sender.sendMessage(GUI_NOT_FOUND.replaceAll("%gui%",args[0]));
                    }
                }
            } else {
                sender.sendMessage(PLAYER_ONLY);
            }
        }else{
            sender.sendMessage(ARGUMENTS_WRONG);
        }
        return false;
    }
}
