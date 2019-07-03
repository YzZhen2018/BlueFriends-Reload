package cn.bluesadi.bluefriends.gui.component;

import cn.bluesadi.bluefriends.gui.BluesGui;
import lk.vexview.gui.components.VexComponents;
import lk.vexview.gui.components.VexScrollingList;
import java.util.List;

public class BluesScrollingList extends BluesComponent {

    private int w;
    private int h;
    private int fullH;
    private List<BluesScrollingListComponent> scrollingListComponents;

    public BluesScrollingList(BluesGui gui, int x, int y, int w, int h, int fullH,List<BluesScrollingListComponent> scrollingListComponents){
        super(gui,x,y);
        this.w = w;
        this.h = h;
        this.fullH  = h;
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
    public void addToVexComponents(List<VexComponents> components) {
        VexScrollingList scrollingList = new VexScrollingList(x,y,w,h,fullH);
        scrollingListComponents.forEach(s->s.getComponents().forEach(c->scrollingList.addComponent(c)));
        components.add(scrollingList);
    }
}
