package cn.bluesadi.bluefriends.util.location;

import com.google.gson.Gson;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个实现Location序列化和反序列化的工具类
 * @author bluesad
 * */
public class LocationSerializerUtil {
    private static Gson gson = new Gson();
    /**
     * 从字符串中读取一个Location
     * */
    public static Location toLocation(String str){
        return gson.fromJson(str,SerializedLocation.class).buildLocation();
    }
    /**
     * 从字符串中读取一个Location的列表
     * */
    public static List<Location> toLocationList(String str){
        List<Location> list = new ArrayList<>();
        for(String lstr : (List<String>)gson.fromJson(str,ArrayList.class)){
            list.add(toLocation(lstr));
        }
        return list;
    }
    /**
     * 将Location转化为字符串
     * */
    public static String toString(Location loc){
        return gson.toJson(new SerializedLocation(loc));
    }
    /**
     * 将LocationList转化为字符串
     * */
    public static String toString(List<Location> list){
        List<String> result = new ArrayList<>();
        for(Location loc : list){
            result.add(toString(loc));
        }
        return gson.toJson(result);
    }
}
