package cn.bluesadi.bluefriends.config;

import cn.bluesadi.bluefriends.BlueFriends;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class Config {

    public static String CHANNEL_BUKKIT_TO_BUNGEE = "bf:bukkit_to_bungee";
    public static String CHANNEL_BUNGEE_TO_BUKKIT = "bf:bungee_to_bukkit";

    private static FileConfiguration config;
    public static String VERSION;
    public static String DATABASE_TYPE;
    public static String DATABASE_URL;
    public static String DATABASE_USER;
    public static String DATABASE_PASSWORD;
    public static boolean BUNGEECORD;
    public static String SERVER_NAME;
    public static boolean ENABLE_MESSAGE_BOX;
    public static String DATE_FORMAT;
    public static String ONLINE;
    public static String OFFLINE;
    public static String BC_ONLINE;
    public static String BC_OFFLINE;
    public static String READ;
    public static String NOT_READ;
    public static String MESSAGE_BOX;
    public static String DEFAULT_EMAIL;
    public static String DEFAULT_QQ;
    public static String DEFAULT_SEX;
    public static String DEFAULT_SIGNATURE;
    public static String DEFAULT_HEAD;
    public static String DEFAULT_HEAD_BORDER;
    public static List<String> DEFAULT_FRIEND_LIST;
    public static List<String> DEFAULT_MESSAGE_LIST;
    public static List<String> DEFAULT_MAIL_BOX;
    public static List<String> DEFAULT_HEAD_LIST;
    public static List<String> DEFAULT_HEAD_BORDER_LIST;

    /**
     * 加载此配置文件
     * */
    public static void load(){
        BlueFriends.getInstance().saveResource("config.yml",false);
        config = YamlConfiguration.loadConfiguration(new File(BlueFriends.getInstance().getDataFolder(),"config.yml"));
        VERSION = config.getString("version","1.0");
        ConfigurationSection databaseSection = config.getConfigurationSection("Database");
        DATABASE_TYPE = databaseSection.getString("type");
        DATABASE_URL = databaseSection.getString("url");
        DATABASE_USER = databaseSection.getString("user");
        DATABASE_PASSWORD = databaseSection.getString("password");
        ConfigurationSection replacementSection = config.getConfigurationSection("Replacement");
        DATE_FORMAT = replacementSection.getString("date_format");
        ONLINE = replacementSection.getString("online");
        OFFLINE = replacementSection.getString("offline");
        BC_ONLINE = replacementSection.getString("bc_online");
        BC_OFFLINE = replacementSection.getString("bc_offline");
        READ = replacementSection.getString("read");
        NOT_READ = replacementSection.getString("not_read");
        ConfigurationSection settingsSection = config.getConfigurationSection("Settings");
        BUNGEECORD = settingsSection.getBoolean("bungeecord",false);
        SERVER_NAME = settingsSection.getString("server_name","Default Server");
        ENABLE_MESSAGE_BOX = settingsSection.getBoolean("enable_message_box",true);
        MESSAGE_BOX = settingsSection.getString("message_box","MessageBox.yml");
        ConfigurationSection defaultAttributesSection = config.getConfigurationSection("DefaultAttributes");
        DEFAULT_EMAIL = defaultAttributesSection.getString("email");
        DEFAULT_SEX = defaultAttributesSection.getString("sex");
        DEFAULT_QQ = defaultAttributesSection.getString("qq");
        DEFAULT_SIGNATURE = defaultAttributesSection.getString("signature");
        DEFAULT_HEAD = defaultAttributesSection.getString("head");
        DEFAULT_HEAD_BORDER = defaultAttributesSection.getString("head_border");
        DEFAULT_FRIEND_LIST = defaultAttributesSection.getStringList("friend_list");
        DEFAULT_MAIL_BOX = defaultAttributesSection.getStringList("mail_box");
        DEFAULT_MESSAGE_LIST = defaultAttributesSection.getStringList("message_list");
        DEFAULT_HEAD_LIST = defaultAttributesSection.getStringList("head_list");
        DEFAULT_HEAD_BORDER_LIST = defaultAttributesSection.getStringList("head_border_list");
    }

}
