package cn.bluesadi.bluefriends.gui.component;

import cn.bluesadi.bluefriends.gui.BluesGui;
import lk.vexview.gui.components.VexComponents;

import java.util.List;

public abstract class BluesComponent {

    protected BluesGui gui;
    protected int x;
    protected int y;

    public BluesComponent(BluesGui gui, int x, int y){
        this.gui = gui;
        this.x = x;
        this.y = y;
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

    abstract public void addToVexComponents(List<VexComponents> components);
}
