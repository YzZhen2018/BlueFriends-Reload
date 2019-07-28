package cn.bluesadi.bluefriends.gui.component.extra;

import cn.bluesadi.bluefriends.gui.BluesGui;
import cn.bluesadi.bluefriends.gui.GuiManager;
import cn.bluesadi.bluefriends.gui.component.BluesComponent;
import cn.bluesadi.bluefriends.gui.component.BluesScrollingList;
import org.bukkit.configuration.ConfigurationSection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionScrollingList <T> extends BluesScrollingList {

    public CollectionScrollingList(BluesGui gui, int x, int y, int w, int h, Collection<T> collection, ConfigurationSection entrySection, CollectionScrollingListFunction<T> placeholder){
        super(gui,x,y,w,h,0,new ArrayList<>());
        int entryInterval = entrySection.getInt("interval",0);
        List<BluesComponent> components = new ArrayList<>();
        int i = 0;
        for(T element : collection){
            List<BluesComponent> entryComponents = GuiManager.getInstance().loadComponents(gui,entrySection.getConfigurationSection("components"));
            for(BluesComponent component : entryComponents){
                component.setPlaceholderFunction(raw ->placeholder.setPlaceholder(raw,element));
                component.setY(component.getY() + i * entryInterval);
            }
            components.addAll(entryComponents);
            i ++;
        }
        this.fullH = entryInterval * collection.size();
        if(fullH < h){
            this.fullH = h;
        }
        this.scrollingListComponents = components;
    }
}
