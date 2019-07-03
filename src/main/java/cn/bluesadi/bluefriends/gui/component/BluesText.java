package cn.bluesadi.bluefriends.gui.component;

import cn.bluesadi.bluefriends.gui.BluesGui;
import lk.vexview.gui.components.VexComponents;
import lk.vexview.gui.components.VexText;
import java.util.List;

public class BluesText extends BluesComponent {

    private List<String> text;

    public BluesText(BluesGui gui,int x, int y, List<String> text){
        super(gui,x,y);
        this.text = text;
    }

    public List<String> getText() {
        return text;
    }

    @Override
    public void addToVexComponents(List<VexComponents> components) {
        components.add(new VexText(x,y,text));
    }
}
