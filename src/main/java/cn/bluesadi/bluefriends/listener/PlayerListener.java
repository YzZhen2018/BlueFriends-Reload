package cn.bluesadi.bluefriends.listener;

import cn.bluesadi.bluefriends.player.BFPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        BFPlayer bfPlayer = BFPlayer.getBFPlayer(player.getUniqueId());
        bfPlayer.setBCOnline(true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        BFPlayer bfPlayer = BFPlayer.getBFPlayer(player.getUniqueId());
        bfPlayer.setBCOnline(false);
    }
}
