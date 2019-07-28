package cn.bluesadi.bluefriends.bungee;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MessageListener implements Listener {

    @EventHandler
    public void onMessage(PluginMessageEvent event){
        if(event.getTag().equalsIgnoreCase(BCConfig.CHANNEL_BUKKIT_TO_BUNGEE)){
            for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers()) {
                BungeeCord.getInstance().getScheduler().runAsync(BCBlueFriends.getInstance(),
                        ()->player.getServer().sendData(BCConfig.CHANNEL_BUNGEE_TO_BUKKIT,event.getData()));
            }
        }
    }

}
