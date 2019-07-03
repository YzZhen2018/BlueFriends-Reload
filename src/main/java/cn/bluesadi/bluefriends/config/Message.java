package cn.bluesadi.bluefriends.config;

import cn.bluesadi.bluefriends.BlueFriends;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Message {

    private static FileConfiguration message;
    public static String NO_PERMISSION;
    public static String RELOAD_COMPLETED;

    public static void load(){
        message = YamlConfiguration.loadConfiguration(new File(BlueFriends.getInstance().getDataFolder(),"message.yml"));
        NO_PERMISSION = message.getString("no_permission");
        RELOAD_COMPLETED = message.getString("reload_completed");
    }
}
