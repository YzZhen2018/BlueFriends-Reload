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
		StringBuilder result = new StringBuilder();
		for (ItemStack item : items) {
			if (item != null) {
				try {
					result.append(gson.toJson(new SerializedStack(new WrappedStack(item))));
					result.append("~");
				} catch (IOException e) {
					System.err.println("错误:在序列化ItemStack时出现了异常");
				}
			}
		}
		return result.toString();
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
		if(paramString == null){
			return new ArrayList<>();
		}
		String[] items = paramString.split("~");
		List<ItemStack> result = new ArrayList<ItemStack>();
		for (String json : items) {
			if (!json.isEmpty()) {
				try {
					result.add((gson.fromJson(json, SerializedStack.class)).buildStack().bukkit());
				} catch (IOException e) {
					System.err.println("错误:在反序列化ItemStack时出现了异常");
				}
			}
		}
		return result;
	}

}
