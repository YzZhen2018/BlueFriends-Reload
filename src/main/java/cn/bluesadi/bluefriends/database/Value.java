package cn.bluesadi.bluefriends.database;

import cn.bluesadi.bluefriends.util.item.ItemSerializerUtil;
import cn.bluesadi.bluefriends.util.location.LocationSerializerUtil;
import com.google.gson.Gson;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author bluesad
 * */
public class Value{
    private static final Gson GSON = new Gson();
    private String rawString;

    private Value(String rawString){
        this.rawString = rawString;
    }

    public String getRawString(String def) {
        return rawString == null ? def : rawString;
    }

    /**
     * 从原始字符串中读取真实值
     * @param rawString 原始字符串
     * @return 真实值对象
     * */
    public static Value fromRawString(String rawString){
        return new Value(rawString);
    }
    /**
     * 转为字符串类型
     * @return 字符串
     * */
    public String getString(){
        return GSON.fromJson(getRawString(""),String.class);
    }
    /**
     * 转为Double类型
     * @return Double
     * */
    public double getDouble(){
        return GSON.fromJson(getRawString("0"),Double.TYPE);
    }
    /**
     * 转为Float类型
     * @return Float
     * */
    public float getFloat(){
        return GSON.fromJson(getRawString("0"),Float.TYPE);
    }
    /**
     * 转为布尔类型
     * @return 布尔
     * */
    public boolean getBoolean(){
        return GSON.fromJson(getRawString("true"),Boolean.TYPE);
    }
    /**
     * 转为Int类型
     * @return Int
     * */
    public int getInt(){
        return GSON.fromJson(getRawString("0"), Integer.TYPE);
    }
    /**
     * 转为Long类型
     * @return Long
     * */
    public long getLong(){
        return GSON.fromJson(getRawString("0"), Long.TYPE);
    }
    /**
     * 转为StringList类型
     * @return StringList
     * */
    public List<String> getStringList(){
        if(rawString == null || rawString.isEmpty()) {
            return new ArrayList<>();
        }else{
            return new ArrayList<>(Arrays.asList(GSON.fromJson(getRawString("[]"), String[].class)));
        }
    }
    /**
     * 转为LongList类型
     * @return LongList
     * */
    public List<Long> getLongList(){
        return new ArrayList<>(Arrays.asList(GSON.fromJson(getRawString("[]"),Long[].class)));
    }
    /**
     * 转为IntList类型
     * @return IntList
     * */
    public List<Integer> getIntList(){
        return new ArrayList<>(Arrays.asList(GSON.fromJson(getRawString("[]"),Integer[].class)));
    }
    /**
     * 转为ItemStackList类型
     * @return ItemStackList
     * */
    public List<ItemStack> getItemStackList(){
        return ItemSerializerUtil.fromString(rawString);
    }
    /**
     * 转为LocationList类型
     * @return LocationList
     * */
    public List<Location> getLocationList(){
        return LocationSerializerUtil.toLocationList(rawString);
    }
    /**
     * 转为Location类型
     * @return Location
     * */
    public Location getLocation(){
        return LocationSerializerUtil.toLocation(rawString);
    }
}
