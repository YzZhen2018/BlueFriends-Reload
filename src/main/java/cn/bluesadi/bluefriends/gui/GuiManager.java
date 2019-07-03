package cn.bluesadi.bluefriends.gui;

import cn.bluesadi.bluefriends.gui.component.BluesComponent;
import cn.bluesadi.bluefriends.gui.component.CompoentType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GuiManager {

    private static int index = Integer.MIN_VALUE;
    private static Map<UUID,BluesGui> previousGui = new HashMap<>();
    private static Map<UUID,BluesGui> openingGui = new HashMap<>();


    public static void loadGuiManager(){

    }

    public static int getIndex(){
        index += 1;
        return index;
    }

    public static BluesGui getOpeningGui(Player viewer){
        return openingGui.get(viewer.getUniqueId());
    }

    public static BluesGui getPreviousGui(Player viewer){
        return previousGui.get(viewer.getUniqueId());
    }

    public static void setOpeningGui(Player viewer,BluesGui gui) {
        openingGui.put(viewer.getUniqueId(),gui);
    }

    public static void setPreviousGui(Player viewer,BluesGui gui) {
       previousGui.put(viewer.getUniqueId(),gui);
    }

    private static boolean check(String... args){
        for(int i = 0;i<args.length;i++){
            if(args[i] == null){
                return false;
            }
        }
        return true;
    }

    public static BluesComponent loadCompoents(ConfigurationSection section){
        int x = section.getInt("x");
        int y = section.getInt("y");
        String type = section.getString("type");
        if(type.equals(CompoentType.BUTTON)){
            String name = section.getString("name");
            String url1 = section.getString("url1");
            String url2 = section.getString("url1");
            int w = section.getInt("w");
            int h = section.getInt("h");
        }
        return null;
    }
}
