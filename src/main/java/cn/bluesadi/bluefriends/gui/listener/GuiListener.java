package cn.bluesadi.bluefriends.gui.listener;

import cn.bluesadi.bluefriends.gui.GuiManager;
import lk.vexview.event.gui.VexGuiCloseEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GuiListener implements Listener {

    @EventHandler
    public void onGuiClose(VexGuiCloseEvent event){
        Player player = event.getPlayer();
        GuiManager.setPreviousGui(player,GuiManager.getOpeningGui(player));
        GuiManager.setOpeningGui(player,null);
    }
}
