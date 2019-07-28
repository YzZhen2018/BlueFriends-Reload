package cn.bluesadi.bluefriends;

import cn.bluesadi.bluefriends.config.Config;
import cn.bluesadi.bluefriends.player.BFPlayer;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class MessageListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(channel.equalsIgnoreCase(Config.CHANNEL_BUNGEE_TO_BUKKIT)){
            ByteArrayDataInput in = ByteStreams.newDataInput(message);
            String playerName = in.readUTF();
            String chatMessage = in.readUTF();
            if (player != null && playerName.equalsIgnoreCase(player.getName())) {
                BFPlayer bfPlayer = BFPlayer.getBFPlayer(player.getUniqueId());
                bfPlayer.sendMessage(chatMessage);
            }
        }
    }
}
