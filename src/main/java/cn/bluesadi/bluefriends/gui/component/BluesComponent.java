package cn.bluesadi.bluefriends.gui.component;

import cn.bluesadi.bluefriends.gui.BluesGui;
import lk.vexview.gui.components.VexComponents;
import me.clip.placeholderapi.PlaceholderAPI;

import java.util.List;
import java.util.function.Function;

public abstract class BluesComponent {

    protected BluesGui gui;
    protected int x;
    protected int y;
    private Function<String,String> function;
    private String permission;

    public BluesComponent(BluesGui gui, int x, int y){
        this.gui = gui;
        this.x = x;
        this.y = y;
        this.function = attr -> PlaceholderAPI.setPlaceholders(gui.getViewer(),attr);
    }

    public BluesGui getGui() {
        return gui;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPlaceholderFunction(Function<String,String> function){
        this.function = function;
    }

    public String setPlaceholder(String str){
        return function.apply(str);
    }

    public List<String> setPlaceholder(List<String> list){
        for(int i = 0;i< list.size();i++){
            list.set(i,setPlaceholder(list.get(i)));
        }
        return list;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Function<String, String> getPlaceholderFunction() {
        return function;
    }

    abstract public List<VexComponents> asVexComponents();
}
