package cn.bluesadi.bluefriends.gui.component;

import cn.bluesadi.bluefriends.gui.BluesGui;
import lk.vexview.gui.components.VexComponents;
import lk.vexview.gui.components.VexSlot;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BluesSlot extends BluesComponent {

    private int id;
    private ItemStack item;

    public BluesSlot(BluesGui gui,int id,int x,int y, ItemStack item){
        super(gui,x,y);
        this.id = id;
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    @Override
    public void addToVexComponents(List<VexComponents> components) {
        components.add(new VexSlot(id,x,y,item));
    }
}
