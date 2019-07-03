package cn.bluesadi.bluefriends.util;

import org.bukkit.Bukkit;

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
        return Bukkit.getOfflinePlayer(uuid).getName() != null;
    }

    public static boolean existsPlayer(String name){
        return Bukkit.getOfflinePlayer(name).getUniqueId() != null;
    }
}
