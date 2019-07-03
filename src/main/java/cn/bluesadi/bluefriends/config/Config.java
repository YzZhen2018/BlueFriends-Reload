package cn.bluesadi.bluefriends.config;

import cn.bluesadi.bluefriends.BlueFriends;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    private static FileConfiguration config;
    public static String DATABASE_TYPE;
    public static String DATABASE_URL;
    public static String DATABASE_USER;
    public static String DATABASE_PASSWORD;
    public static boolean BUNGEECORD;
    public static String SERVER_NAME;
    public static boolean ENABLE_MESSAGE_BOX;
    public static String DATE_FORMAT;

    /**
     * 加载此配置文件
     * */
    public static void load(){
        config = BlueFriends.getInstance().getConfig();
        ConfigurationSection databaseSection = config.getConfigurationSection("Database");
        DATABASE_TYPE = databaseSection.getString("type");
        DATABASE_URL = databaseSection.getString("url");
        DATABASE_USER = databaseSection.getString("user");
        DATABASE_PASSWORD = databaseSection.getString("password");
        ConfigurationSection replacementSection = config.getConfigurationSection("Replacement");
        DATE_FORMAT = replacementSection.getString("date_format");
        ConfigurationSection settingsSection = config.getConfigurationSection("Settings");
        BUNGEECORD = settingsSection.getBoolean("bungeecord",false);
        SERVER_NAME = settingsSection.getString("server_name","Default Server");
        ENABLE_MESSAGE_BOX = settingsSection.getBoolean("enable_message_box",true);
    }

}
