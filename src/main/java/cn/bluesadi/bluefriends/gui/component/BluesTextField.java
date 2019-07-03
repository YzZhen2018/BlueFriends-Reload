package cn.bluesadi.bluefriends.gui.component;

import cn.bluesadi.bluefriends.gui.BluesGui;
import cn.bluesadi.bluefriends.gui.GuiManager;
import lk.vexview.gui.components.VexComponents;
import lk.vexview.gui.components.VexTextField;

import java.util.List;

public class BluesTextField extends BluesComponent {

    private List<String> commands;
    private int w;
    private int h;
    private int maxString;
    private VexTextField textField;

    public BluesTextField(BluesGui gui,int x, int y,int w,int h,int maxString, List<String> commands){
        super(gui,x,y);
        this.commands = commands;
        this.w = w;
        this.h = h;
        this.maxString = maxString;
        this.textField = new VexTextField(x,y,w,h,maxString,GuiManager.getIndex());
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public int getMaxString() {
        return maxString;
    }

    public List<String> getCommands() {
        return commands;
    }

    public String getTypedText(){
        return textField.getTypedText();
    }

    @Override
    public void addToVexComponents(List<VexComponents> components) {
        components.add(textField);
    }
}
