package cn.bluesadi.bluefriends.gui.component;

import cn.bluesadi.bluefriends.gui.BluesGui;
import cn.bluesadi.bluefriends.gui.GuiManager;
import lk.vexview.gui.components.ButtonFunction;
import lk.vexview.gui.components.VexButton;
import lk.vexview.gui.components.VexComponents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.List;

public class BluesButton extends BluesComponent {

    private String name;
    private String url1;
    private String url2;
    private int w;
    private int h;
    private List<String> commands;


    public BluesButton(BluesGui gui, int x, int y,String name, String url1, String url2, int w, int h, List<String> commands){
        super(gui,x,y);
        this.name = name;
        this.url1  = url1;
        this.url2 = url2;
        this.w = w;
        this.h = h;
        this.commands  = commands;
    }

    public String getName() {
        return name;
    }

    public String getUrl1() {
        return url1;
    }

    public String getUrl2() {
        return url2;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public List<String> getCommands() {
        return commands;
    }

    @Override
    public void addToVexComponents(List<VexComponents> components) {
        int id = GuiManager.getIndex();
        VexButton button = new VexButton(id, name,url1, url2, x, y, w, h);
        commands.forEach(cmd -> {
            if(cmd.startsWith("openurl")){
                button.setWebUrl(cmd.replaceAll("openurl ",""));
            }
        });
        ButtonFunction buttonFunction = new ButtonFunction() {
            @Override
            public void run(Player player) {
                commands.forEach(cmd -> {
                    if(cmd.equals("close")){
                        gui.getViewer().closeInventory();
                    }else if(cmd.equals("back")){
                        GuiManager.getPreviousGui(player).open();
                    }else if(cmd.equals("textfield")){
                        gui.getComponents().forEach(component ->{
                            if(component instanceof BluesTextField){
                                BluesTextField bluesTextField = (BluesTextField)component;
                                bluesTextField.getCommands().forEach(command ->
                                    Bukkit.dispatchCommand(gui.getViewer(),command.replaceAll("%text%",bluesTextField.getTypedText()))
                                );
                            }
                        });
                    }else{
                        Bukkit.dispatchCommand(gui.getViewer(),cmd);
                    }
                });
            }
        };
        button.setFunction(buttonFunction);
        components.add(button);
    }
}
