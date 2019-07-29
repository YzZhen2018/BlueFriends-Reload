package cn.bluesadi.bluefriends.util;

import cn.bluesadi.bluefriends.BFDatabase;
import cn.bluesadi.bluefriends.BlueFriends;
import cn.bluesadi.bluefriends.config.Config;
import cn.bluesadi.bluefriends.player.BFPlayer;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BFUtil {

    public static String color(String str){
        return str.replaceAll("&","§");
    }

    public static List<String> color(List<String> list){
        List<String> newList = new ArrayList<>();
        list.forEach(str -> newList.add(color(str)));
        return newList;
    }

    public static boolean existsPlayer(UUID uuid){
        return Bukkit.getOfflinePlayer(uuid).hasPlayedBefore();
    }

    public static boolean existsPlayer(String name){
        return Bukkit.getOfflinePlayer(name).hasPlayedBefore();
    }

    public static boolean hasPermission(Player player, String str){
        if(str == null){
            return true;
        }
        if(str.startsWith("-")){
            return !player.hasPermission(str.substring(1));
        }else{
            return player.hasPermission(str);
        }
    }

    public static boolean existsFakePlayer(String name){
        return BlueFriends.getBFDatabase().getFakePlayerTable().getRow(name).existsRow();
    }

    public static boolean existsSystemMessage(String uuid){
        return BFDatabase.getInstance().getMessageTable().getRow(uuid).existsRow();
    }

    public static boolean existsMail(String uuid){
        return BFDatabase.getInstance().getMailTable().getRow(uuid).existsRow();
    }

    public static void sendMessageBox(CommandSender commandSender,String message){
        if(commandSender instanceof Player){
            BFPlayer bfPlayer = BFPlayer.getBFPlayer(((Player)commandSender).getUniqueId());
            bfPlayer.sendMessageBox(message);
        }else{
            commandSender.sendMessage(message);
        }
    }

    public static void sendPluginMessage(String... args){
        if(Bukkit.getOnlinePlayers().size() > 0){
            Player p = Bukkit.getOnlinePlayers().iterator().next();
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            for(String arg : args){
                out.writeUTF(arg);
            }
            p.sendPluginMessage(BlueFriends.getInstance(), Config.CHANNEL_BUKKIT_TO_BUNGEE,out.toByteArray());
        }
    }

    public static UUID getPlayerUUID(String name){
        if(existsFakePlayer(name)){
            return UUID.fromString(BlueFriends.getBFDatabase().getFakePlayerTable().getRow(name).getValue("uuid").getString());
        }
        return Bukkit.getOfflinePlayer(name).getUniqueId();
    }

    public static void mkdirs(String dir){
        File file = new File(BlueFriends.getInstance().getDataFolder(),dir);
        file.mkdirs();
    }
}
