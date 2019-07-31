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
    private static final String INFO_PREFIX = "§8> "+ChatColor.GREEN.toString();
    private static final String DEBUG_PREFIX = ChatColor.BLUE + "§bDEBUG>§c";
    private static final String ERROR_PREFIX = ChatColor.DARK_RED + "§bERROR>§4";
    private static PrintStream errorStream;


    public static void info(String msg){
        LOGGER.info(INFO_PREFIX + msg);
    }

    public static void debug(String msg){
        LOGGER.info(DEBUG_PREFIX + msg);
    }

    public static void error(String msg){
        try {
            if (errorStream == null) {
                File errorFile = new File(BlueFriends.getInstance().getDataFolder(), "error.log");
                errorFile.createNewFile();
                errorStream = new PrintStream(new FileOutputStream(errorFile));
            }
            LOGGER.info(ERROR_PREFIX + msg);
            errorStream.println(ChatColor.stripColor(ERROR_PREFIX + msg));
            errorStream.flush();
        }catch (IOException e){}
    }

    public static void error(String msg,Exception exception){
        try {
            String fileName =System.currentTimeMillis()+".log";
            error(msg+"(报错记录已保存至../error/"+fileName+")");
            File targetFile = new File(BlueFriends.getInstance().getDataFolder(), "error/" + fileName);
            targetFile.createNewFile();
            PrintStream out = new PrintStream(new FileOutputStream(targetFile));
            out.println(exception.getMessage());
            exception.printStackTrace(new PrintStream(out));
            out.flush();
            out.close();
        }catch (IOException e){}
    }

    public static boolean hasError(){
        File errorFile = new File(BlueFriends.getInstance().getDataFolder(), "error.log");
        File errorFolder = new File(BlueFriends.getInstance().getDataFolder(),"error");
        if(errorFile.exists() || errorFolder.listFiles().length > 0){
            return true;
        }
        return false;
    }

}
