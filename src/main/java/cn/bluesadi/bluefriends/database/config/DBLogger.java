package cn.bluesadi.bluefriends.database.config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.logging.Logger;

public class DBLogger {
    private static final String DEBUG_PREFIX = ChatColor.BLUE+"DEBUG>";
    private static final String ERROR_PREFIX = ChatColor.RED+"ERROR>";
    private static Logger logger = Bukkit.getLogger();


    public static void info(String msg){
        logger.info(msg);
    }

    public static void debug(String msg){
        logger.info(DEBUG_PREFIX+msg);
    }

    public static void error(String msg){
        logger.info(ERROR_PREFIX+msg);
    }
}
