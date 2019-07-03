package cn.bluesadi.bluefriends;

import cn.bluesadi.bluefriends.mail.Mail;
import cn.bluesadi.bluefriends.mail.MailEditor;
import cn.bluesadi.bluefriends.player.BFPlayer;
import cn.bluesadi.bluefriends.util.BFCalendar;
import cn.bluesadi.bluefriends.util.BFLogger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class BlueFriends extends JavaPlugin {

    private static BlueFriends instance;

    @Override
    public void onEnable() {
        instance = this;
        BFLogger.info("开始加载BlueFriends-Reload!");
        BFLogger.info("正在检测前置插件是否齐全...");
        if(!(checkDependedPlugin("VexView") && checkDependedPlugin("ProtocolLib") && checkDependedPlugin("PlaceholderAPI"))){
            Bukkit.getPluginManager().disablePlugin(this);
        }
        BFLogger.info("BlueFriends-Reload加载完毕!");
    }

    @Override
    public void onDisable() {
        BFLogger.info("开始卸载BlueFriends-Reload!");
    }

    private boolean checkDependedPlugin(String name){
        if(Bukkit.getPluginManager().isPluginEnabled(name)){
            BFLogger.info(name+"已装载!");
            return true;
        }
        BFLogger.error(name+"未装载!");
        return false;
    }

    @Override
    public void saveResource(String resourcePath, boolean replace) {
        if(!getDataFolder().exists()){
            getDataFolder().mkdirs();
        }
        if(replace){
            super.saveResource(resourcePath,true);
        }else{
            if(!new File(getDataFolder(),resourcePath).exists()){
                super.saveResource(resourcePath,true);
            }
        }
    }

    public static BlueFriends getInstance() {
        return instance;
    }

    public static BFDatabase getBFDatabase(){
        return BFDatabase.getInstance();
    }

    public static MailEditor getMailEditor(){
        return MailEditor.getInstance();
    }

    public static List<BFPlayer> getBFPlayers(){
        List<BFPlayer> result = new ArrayList<>();
        getBFDatabase().getPlayerTable().getRows().forEach(row -> result.add(new BFPlayer(UUID.fromString(row.getMainValue()))));
        return result;
    }
}
