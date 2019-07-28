package cn.bluesadi.bluefriends.util;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

/**
 * MIT License
 * 
 * Copyright (c) 2017 ChenJi
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without
 * limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT
 * SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 *
 */
/**
 * 更新记录：
 * 2017-8-2 修了一个死循环Bug。
 */
public class PlayerInventoryUtils {
	/**
	 * 获取玩家背包内空闲位置的个数
	 * 
	 * @param p 玩家
	 * @return 数量
	 */
	public static int getFreeSize(Player p) {
		int index = -1;
		int freesize = 0;
		while (index <= 35) {
			index++;
			ItemStack i = p.getInventory().getItem(index);
			if (i == null) {
				freesize++;
				continue;
			}
			if (i.getType().equals(Material.AIR)) {
				freesize++;
				continue;
			}
		}
		return freesize;
	}

	/**
	 * 获取玩家背包内最多可以装入多少个指定物品
	 * 
	 * @param p 玩家
	 * @param m 物品
	 * @return 数量
	 */
	public static int getFreesize(Player p, ItemStack m) {
		int index = -1;
		int freesize = 0;
		while (index <= 35) {
			index++;
			ItemStack i = p.getInventory().getItem(index);
			if (i == null) {
				freesize = freesize + m.getMaxStackSize();
				continue;
			}
			if (i.isSimilar(m)) {
				freesize = freesize + (m.getMaxStackSize() - i.getAmount());
				continue;
			}
		}
		return freesize;
	}

	/**
	 * 返回这个物品列表是否可以放入玩家的背包
	 * 
	 * @param p 玩家
	 * @param items 物品列表
	 * @return 是否可以放入
	 */
	public static boolean canPut(Player p, List<ItemStack> items) {
		List<ItemStack> alread = Lists.newArrayList();
		for (ItemStack i : items) {
			int freeslot = getOverlapSize(p, i, alread);
			int allfree = getFreeSlot(p, alread) * i.getMaxStackSize() + freeslot;
			if (allfree >= i.getAmount()) {
				alread.add(i);
				continue;
			}
			return false;
		}
		return true;
	}

	/**
	 * 返回一个物品在玩家背包中，可以重叠的个数
	 * 
	 * @param p 玩家
	 * @param m 物品列表
	 * @return 数量
	 **/
	public static int getOverlapSize(Player p, ItemStack m) {
		int index = -1;
		int freesize = 0;
		while (index <= 35) {
			index++;
			ItemStack i = p.getInventory().getItem(index);
			if (i == null) {
				continue;
			}
			if (i.isSimilar(m)) {
				freesize = freesize + (m.getMaxStackSize() - i.getAmount());
				continue;
			}
		}
		return freesize;
	}

	/**
	 * 返回当一个列表的物品放入玩家背包以后，可以重叠的位置的个数。
	 * 
	 * @param p 玩家
	 * @param m 物品
	 * @param i 列表
	 * @return 位置个数
	 */
	public static int getOverlapSize(Player p, ItemStack m, List<ItemStack> i) {
		int amountTotal = 0;
		for (ItemStack item : i) {
			if (!item.isSimilar(m)) {
				continue;
			}
			amountTotal = amountTotal + item.getAmount();
		}
		int overlapTotal = getOverlapSize(p, m);
		if (amountTotal > overlapTotal) {
			return (amountTotal - overlapTotal) % m.getMaxStackSize();
		} else {
			return overlapTotal - amountTotal;
		}
	}

	/**
	 * 返回当列表内的物品放入玩家背包后，空位置的个数。
	 * 
	 * @param p 玩家
	 * @param i 物品列表
	 * @return 空位置的个数
	 */
	public static int getFreeSlot(Player p, List<ItemStack> i) {
		int freesize = getFreeSize(p);
		for (ItemStack item : i) {
			int oversize = getOverlapSize(p, item);
			if (oversize >= item.getAmount()) {
				continue;
			}
			int nowamount = item.getAmount() - oversize;
			if (nowamount % item.getMaxStackSize() == 0 || nowamount < item.getMaxStackSize()) {
				freesize = freesize - nowamount / item.getMaxStackSize();
			} else {
				freesize = freesize - ((nowamount / item.getMaxStackSize()) + 1);
			}
		}
		return freesize;
	}
}
