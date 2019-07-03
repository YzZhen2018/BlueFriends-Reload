package cn.bluesadi.bluefriends.gui;

import cn.bluesadi.bluefriends.gui.component.BluesComponent;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class BluesGui {

    private int x;
    private int y;
    private String url;
    private int w;
    private int h;
    private Player viewer;
    private List<BluesComponent> components = new ArrayList<>();

    public BluesGui(int x,int y,String url,int w,int h,Player viewer){
        this.x = x;
        this.y = y;
        this.url = url;
        this.w = w;
        this.h = h;
        this.viewer = viewer;
    }

    public List<BluesComponent> getComponents() {
        return components;
    }

    public Player getViewer(){
        return this.viewer;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public String getUrl() {
        return url;
    }

    public void open(){
        VexGui vexGui = new VexGui(url,x,y,w,h,w,h);
        components.forEach(component -> component.addToVexComponents(vexGui.getComponents()));
        VexViewAPI.openGui(viewer,vexGui);
        GuiManager.setOpeningGui(viewer,this);
    }
}
