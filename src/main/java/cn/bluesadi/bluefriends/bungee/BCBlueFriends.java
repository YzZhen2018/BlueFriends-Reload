package cn.bluesadi.bluefriends.bungee;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Logger;

public class BCBlueFriends extends Plugin {

    private static Logger logger = BungeeCord.getInstance().getLogger();
    private static BCBlueFriends instance;

    @Override
    public void onEnable() {
        instance = this;
        ProxyServer.getInstance().registerChannel(BCConfig.CHANNEL_BUNGEE_TO_BUKKIT);
        ProxyServer.getInstance().registerChannel(BCConfig.CHANNEL_BUKKIT_TO_BUNGEE);
        BungeeCord.getInstance().getPluginManager().registerListener(this,new MessageListener());
        logger.info("BlueFriends(BC版)已加载!");
    }

    @Override
    public void onDisable() {
        logger.info("BlueFriends(BC版)已卸载!");
    }

    public static BCBlueFriends getInstance() {
        return instance;
    }
}
