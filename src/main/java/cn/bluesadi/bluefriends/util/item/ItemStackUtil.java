package cn.bluesadi.bluefriends.util.item;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * ItemStack的工具类
 * @author bluesad
 * */
public class ItemStackUtil {
    /**
     * 将ItemStack列表转换为数组
     * @return 不忽略null值的数组
     * */
    public static ItemStack[] toItemStackArray(List<ItemStack> itemList){
        return itemList.toArray(new ItemStack[itemList.size()]);
    }
    /**
     * 将ItemStack数组转换为列表
     * @return 忽略掉所有null值的列表
     * */
    public static List<ItemStack> toItemStackList(ItemStack[] arr){
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack item : arr){
            if(item != null) {
                items.add(item);
            }
        }
        return items;
    }
}
