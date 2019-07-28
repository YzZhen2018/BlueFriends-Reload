package cn.bluesadi.bluefriends.gui.component;

import cn.bluesadi.bluefriends.gui.BluesGui;
import lk.vexview.gui.components.ScrollingListComponent;
import lk.vexview.gui.components.VexComponents;
import lk.vexview.gui.components.VexScrollingList;

import java.util.Collections;
import java.util.List;

public class BluesScrollingList extends BluesComponent {

    private int w;
    private int h;
    protected int fullH;
    protected List<BluesComponent> scrollingListComponents;

    public BluesScrollingList(BluesGui gui, int x, int y, int w, int h, int fullH,List<BluesComponent> scrollingListComponents){
        super(gui,x,y);
        this.w = w;
        this.h = h;
        this.fullH  = fullH;
        this.scrollingListComponents = scrollingListComponents;

    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public int getFullH() {
        return fullH;
    }

    @Override
    public List<VexComponents> asVexComponents() {
        VexScrollingList scrollingList = new VexScrollingList(x,y,w,h,fullH);
        scrollingListComponents.forEach(bluesComponent -> {
            bluesComponent.asVexComponents().forEach(vexComponents ->{
                if(vexComponents instanceof ScrollingListComponent){
                    scrollingList.addComponent((ScrollingListComponent)vexComponents);
                }
            });
        });
        return Collections.singletonList(scrollingList);
    }
}
