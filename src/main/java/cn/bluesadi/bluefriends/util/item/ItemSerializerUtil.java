package cn.bluesadi.bluefriends.util.item;

import cn.bluesadi.bluefriends.util.BFLogger;
import com.google.gson.Gson;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 一个实现ItemStack序列化和反序列化工具类
 * @author bluesad
 */
public class ItemSerializerUtil {
	private static Gson gson = new Gson();

	/**
	 * 将物品反序列化为Base64数据
	 * 
	 * @param items
	 *            物品
	 * @return 物品的序列化json
	 */
	public static String toString(ItemStack[] items) {
		List<String> result = new ArrayList<>();
		for (ItemStack item : items) {
			if (item != null) {
				try {
					result.add(gson.toJson(new SerializedStack(new WrappedStack(item))));
				} catch (IOException e) {
					System.err.println("错误:在序列化ItemStack时出现了异常");
					e.printStackTrace();
				}
			}
		}
		return gson.toJson(result);
	}

	public static String toString(ItemStack item){
		try{
			return gson.toJson(new SerializedStack(new WrappedStack(item)));
		}catch (IOException e){
			BFLogger.debug("错误:在序列化ItemStack时出现了异常");
		}
		return null;
	}
	/**
	 * 将json序列化为物品
	 *
	 * @param paramString
	 *            json
	 * @return 物品列表
	 */
	public static List<ItemStack> fromString(String paramString) {
		List<String> base = gson.fromJson(paramString,ArrayList.class);
		List<ItemStack> result = new ArrayList<>();
		for (String json : base){
			try{
				result.add(gson.fromJson(json,SerializedStack.class).buildStack().bukkit());
			}catch (IOException e){
				System.err.println("错误:在反序列化ItemStack时出现了异常");
				e.printStackTrace();
			}
		}
		return result;
	}

}
