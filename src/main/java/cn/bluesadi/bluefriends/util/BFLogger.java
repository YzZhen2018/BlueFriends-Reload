package cn.bluesadi.bluefriends.util;

import cn.bluesadi.bluefriends.BlueFriends;
import cn.bluesadi.bluefriends.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.logging.Logger;

public class BFLogger {
    private static final Logger LOGGER = Bukkit.getLogger();
    private static final String INFO_PREFIX = "§8>"+ChatColor.GREEN.toString();
    private static final String DEBUG_PREFIX = ChatColor.BLUE + "§bDEBUG>§c";
    private static final String ERROR_PREFIX = ChatColor.DARK_RED + "§bERROR>§4";


    public static void info(String msg){
        LOGGER.info(INFO_PREFIX + msg);
    }

    public static void debug(String msg){
        LOGGER.info(DEBUG_PREFIX + msg);
    }

    public static void error(String msg){
        LOGGER.info(ERROR_PREFIX + msg);
    }

    public static void error(String msg,Exception exception){
        try {
            String fileName =System.currentTimeMillis()+".log";
            error(msg+"(报错记录已保存至../error/"+fileName+")");
            File targetFile = new File(BlueFriends.getInstance().getDataFolder(), "error/" + fileName);
            targetFile.createNewFile();
            FileOutputStream out = new FileOutputStream(targetFile);
            out.write(exception.getMessage().getBytes());
            exception.printStackTrace(new PrintStream(out));
            out.flush();
            out.close();
        }catch (IOException e){}
    }

}
