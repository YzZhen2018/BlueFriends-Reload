package cn.bluesadi.bluefriends.listener;

import cn.bluesadi.bluefriends.config.Message;
import cn.bluesadi.bluefriends.player.BFPlayer;
import cn.bluesadi.bluefriends.util.BFLogger;
import me.clip.placeholderapi.PlaceholderAPI;
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
        if(player.isOp() && BFLogger.hasError()){
            player.sendMessage("§bBlueFriends§8>> §c你还有一些错误日志没有处理");
            player.sendMessage("§bBlueFriends§8>> §c请查看BlueFriends/error中的文件或BlueFriends/error.log");
        }
        player.sendMessage(PlaceholderAPI.setPlaceholders(player,Message.JOIN).toArray(new String[0]));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        BFPlayer bfPlayer = BFPlayer.getBFPlayer(player.getUniqueId());
        bfPlayer.setBCOnline(false);
    }
}
