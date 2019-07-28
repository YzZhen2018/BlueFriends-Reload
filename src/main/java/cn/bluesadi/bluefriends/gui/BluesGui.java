package cn.bluesadi.bluefriends.gui;

import cn.bluesadi.bluefriends.BlueFriends;
import cn.bluesadi.bluefriends.gui.component.BluesComponent;
import cn.bluesadi.bluefriends.gui.component.BluesTextField;
import cn.bluesadi.bluefriends.util.BFUtil;
import lk.vexview.api.VexViewAPI;
import lk.vexview.gui.VexGui;
import org.apache.logging.log4j.core.config.builder.api.Component;
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
    private BluesGui lastGui;
    private String source;
    private boolean allowBack;

    public BluesGui(int x,int y,String url,int w,int h,Player viewer,String source,boolean allowBack){
        this.x = x;
        this.y = y;
        this.url = url;
        this.w = w;
        this.h = h;
        this.viewer = viewer;
        this.source = source;
        this.allowBack = allowBack;
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

    public String getURL() {
        return url;
    }

    public void setComponents(List<BluesComponent> components) {
        this.components = components;
    }

    public void open(){
        VexGui vexGui = new VexGui(url,x,y,w,h,w,h);
        components.forEach(component -> {
            if(BFUtil.hasPermission(viewer,component.getPermission())) {
                vexGui.addAllComponents(component.asVexComponents());
            }
        });
        VexViewAPI.openGui(viewer,vexGui);
        BluesGui opening = BlueFriends.getGuiManager().getOpeningGui(viewer);
        if(opening != null) {
            lastGui = opening.allowBack ? opening : opening.lastGui;
        }
        BlueFriends.getGuiManager().setOpeningGui(viewer,this);
    }

    public void back(){
        if(lastGui != null){
            System.out.println(lastGui.source);
            List<BluesComponent> oldComponents = lastGui.getComponents();
            BluesGui lastLastGui = lastGui.lastGui;
            lastGui = GuiManager.getInstance().createGui(lastGui.source,viewer);
            lastGui.lastGui = lastLastGui;
            List<BluesComponent> newComponents = lastGui.getComponents();
            if(oldComponents.size() == newComponents.size()){
                for(int i = 0 ;i < newComponents.size();i++){
                    newComponents.get(i).setPlaceholderFunction(oldComponents.get(i).getPlaceholderFunction());
                }
            }
            VexGui vexGui = new VexGui(lastGui.url,lastGui.x,lastGui.y,lastGui.w,lastGui.h,lastGui.w,lastGui.h);
            newComponents.forEach(component -> {
                if(BFUtil.hasPermission(viewer,component.getPermission())) {
                    vexGui.addAllComponents(component.asVexComponents());
                }
            });
            VexViewAPI.openGui(viewer,vexGui);
            BlueFriends.getGuiManager().setOpeningGui(viewer,lastGui);
        }
    }

}
