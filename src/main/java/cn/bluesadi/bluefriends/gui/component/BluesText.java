package cn.bluesadi.bluefriends.gui.component;

import cn.bluesadi.bluefriends.gui.BluesGui;
import lk.vexview.gui.components.VexComponents;
import lk.vexview.gui.components.VexText;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class BluesText extends BluesComponent{

    private int linefeed;
    private int omit;
    private List<String> text;

    public BluesText(BluesGui gui,int x, int y, List<String> text,int linefeed,int omit){
        super(gui,x,y);
        this.text = text;
        this.linefeed = linefeed;
        this.omit = omit;
    }

    public List<String> getText() {
        return text;
    }

    public int getLinefeed() {
        return linefeed;
    }

    public int getOmit() {
        return omit;
    }

    @Override
    public List<VexComponents> asVexComponents() {
        Function<String,String> f = getPlaceholderFunction();
        setPlaceholderFunction(raw -> {
            String result = f.apply(raw);
            if(omit > 0){
                char[] chars = result.toCharArray();
                StringBuilder builder = new StringBuilder();
                int length = 0;
                for(char c : chars){
                    if(String.valueOf(c).matches("[\u4e00-\u9fa5]")){
                        length += 2;
                    }else{
                        length ++;
                    }
                    builder.append(c);
                    if(length > omit){
                        builder.append("...");
                        return builder.toString();
                    }
                }
            }
            if(linefeed >0){
                char[] chars = result.toCharArray();
                StringBuilder builder = new StringBuilder();
                int length = 0;
                for(char c : chars){
                    if(String.valueOf(c).matches("[\u4e00-\u9fa5]")){
                        length += 2;
                    }else{
                        length ++;
                    }
                    builder.append(c);
                    if(length >= linefeed){
                        builder.append("\n");
                        length = 0;
                    }
                }
                return builder.toString();
            }
            return result;
        });
        return Collections.singletonList(new VexText(x,y,setPlaceholder(text)));
    }
}
