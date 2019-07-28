package cn.bluesadi.bluefriends.listener;

import cn.bluesadi.bluefriends.BlueFriends;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import java.util.List;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        String title = event.getInventory().getTitle();
        if(title != null && title.equals("编辑邮件附件")){
            List<ItemStack> contents = Lists.newArrayList(event.getInventory().getContents());
            while(contents.remove(null));
            BlueFriends.getMailEditor().setItems(contents);
            Player player = (Player) event.getPlayer();
            Bukkit.dispatchCommand(player,"bfadmin maileditor");
        }
    }

}
