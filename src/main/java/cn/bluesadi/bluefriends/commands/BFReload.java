package cn.bluesadi.bluefriends.commands;

import cn.bluesadi.bluefriends.BFDatabase;
import cn.bluesadi.bluefriends.config.Config;
import cn.bluesadi.bluefriends.config.Message;
import cn.bluesadi.bluefriends.gui.GuiManager;
import cn.bluesadi.bluefriends.player.BFPlayer;
import cn.bluesadi.bluefriends.util.BFUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BFReload implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("bluefriends.reload")){
            Config.load();//重载config.yml
            Message.load();//重载message.yml
            BFDatabase.getInstance().close();//重新连接数据库
            BFDatabase.load();
            GuiManager.loadGuiManager();
            BFUtil.sendMessageBox(sender,Message.RELOAD_COMPLETED);
        }else{
            BFUtil.sendMessageBox(sender,Message.NO_PERMISSION);
        }
        return false;
    }
}
