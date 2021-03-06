package cn.bluesadi.bluefriends.gui.component;

import cn.bluesadi.bluefriends.gui.BluesGui;
import lk.vexview.gui.components.VexComponents;
import lk.vexview.gui.components.VexHoverText;
import lk.vexview.gui.components.VexImage;
import java.util.Collections;
import java.util.List;

public class BluesImage extends BluesComponent {

    private int w;
    private int h;
    private String url;
    private List<String> hoverText;


    public BluesImage(BluesGui gui,int x,int y,String url,int w,int h){
        this(gui,x,y,url,w,h,null);
    }

    public BluesImage(BluesGui gui, int x, int y, String url,int w, int h, List<String> hoverText){
        super(gui,x,y);
        this.w = w;
        this.h = h;
        this.hoverText = hoverText;
        this.url = url;
    }

    public List<VexComponents> asVexComponents(){
        if(hoverText == null){
            return Collections.singletonList(new VexImage(setPlaceholder(url),x,y,w,h));
        }else {
            return Collections.singletonList(new VexImage(setPlaceholder(url),getX(),getY(),w,h,new VexHoverText(setPlaceholder(hoverText))));
        }
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

    public List<String> getHoverText() {
        return hoverText;
    }
}
