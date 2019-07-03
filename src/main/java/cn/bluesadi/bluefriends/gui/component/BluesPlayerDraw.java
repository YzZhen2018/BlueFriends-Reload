package cn.bluesadi.bluefriends.gui.component;

import cn.bluesadi.bluefriends.gui.BluesGui;
import lk.vexview.gui.components.VexComponents;
import lk.vexview.gui.components.VexPlayerDraw;

import java.util.List;

public class BluesPlayerDraw extends BluesComponent {

    private int size;

    public BluesPlayerDraw(BluesGui gui,int x,int y,int size){
        super(gui,x,y);
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    @Override
    public void addToVexComponents(List<VexComponents> components) {
        components.add(new VexPlayerDraw(x,y,size,gui.getViewer()));
    }
}
