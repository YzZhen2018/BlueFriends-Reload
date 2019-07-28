package cn.bluesadi.bluefriends;

import cn.bluesadi.bluefriends.commands.*;
import cn.bluesadi.bluefriends.config.Config;
import cn.bluesadi.bluefriends.config.Message;
import cn.bluesadi.bluefriends.gui.GuiManager;
import cn.bluesadi.bluefriends.listener.InventoryListener;
import cn.bluesadi.bluefriends.mail.Mail;
import cn.bluesadi.bluefriends.mail.MailEditor;
import cn.bluesadi.bluefriends.player.BFPlayer;
import cn.bluesadi.bluefriends.listener.PlayerListener;
import cn.bluesadi.bluefriends.util.BFLogger;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class BlueFriends extends JavaPlugin {

    private static BlueFriends instance;

    @Override
    public void onEnable() {
        instance = this;
        BFLogger.info("开始加载BlueFriends-Reload!");
        BFLogger.info("正在检查前置插件是否齐全...");
        if(!(checkDependedPlugin("VexView") && checkDependedPlugin("ProtocolLib") && checkDependedPlugin("PlaceholderAPI"))){
            BFLogger.error("检测到有前置插件没有安装,插件即将自动卸载!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        BFLogger.info("正在加载插件配置文件...");
        saveResource("config.yml",false);
        saveResource("message.yml",false);
        saveResource("map.yml",false);
        new File(getDataFolder(),"gui").mkdirs();
        Config.load();
        Message.load();
        BFLogger.info("正在加载GUI配置文件...");
        saveResource("gui/MessageBox.yml",false);
        GuiManager.loadGuiManager();
        BFLogger.info("正在连接数据库...");
        BFLogger.info("检测到数据库类型为:§e"+Config.DATABASE_TYPE);
        try{
            BFDatabase.load();
        }catch (Exception e){
            BFLogger.error("在连接数据库时出现了异常!",e);
        }
        BFLogger.info("正在注册插件指令...");
        Bukkit.getPluginCommand("friend").setExecutor(new Friend());
        Bukkit.getPluginCommand("bfreload").setExecutor(new BFReload());
        Bukkit.getPluginCommand("bfopen").setExecutor(new BFOpen());
        Bukkit.getPluginCommand("bfadmin").setExecutor(new BFAdmin());
        Bukkit.getPluginCommand("person").setExecutor(new Person());
        BFLogger.info("正在注册插件监听器...");
        Bukkit.getPluginManager().registerEvents(new PlayerListener(),this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(),this);
        BFLogger.info("正在注册PlaceholderAPI变量");
        registerPlaceholderHook();
        if(Config.BUNGEECORD){
            BFLogger.info("检测到BungeeCord选项开启,设定该服务端为群组端子端");
            BFLogger.info("正在注册通信频道...");
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, Config.CHANNEL_BUKKIT_TO_BUNGEE);
            this.getServer().getMessenger().registerIncomingPluginChannel(this, Config.CHANNEL_BUNGEE_TO_BUKKIT, new MessageListener());
        }
        BFLogger.info("BlueFriends-Reload加载完毕!");
    }

    @Override
    public void onDisable() {
        BFLogger.info("开始卸载BlueFriends-Reload!");
    }

    private boolean checkDependedPlugin(String name){
        if(Bukkit.getPluginManager().isPluginEnabled(name)){
            BFLogger.info(name+"已装载!");
            return true;
        }
        BFLogger.error(name+"未装载!");
        return false;
    }

    @Override
    public void saveResource(String resourcePath, boolean replace) {
        if(!getDataFolder().exists()){
            getDataFolder().mkdirs();
        }
        if(replace){
            super.saveResource(resourcePath,true);
        }else{
            if(!new File(getDataFolder(),resourcePath).exists()){
                super.saveResource(resourcePath,true);
            }
        }
    }

    private void registerPlaceholderHook(){
        PlaceholderAPI.registerPlaceholderHook("bluefriends",(new PlaceholderHook() {

            @Override
            public String onRequest(OfflinePlayer p, String params) {
                BFPlayer bfPlayer = BFPlayer.getBFPlayer(p.getUniqueId());
                if(params.equalsIgnoreCase("bc_online")){
                    return bfPlayer.isBCOnline() ? Config.BC_ONLINE : Config.BC_OFFLINE;
                }else if(params.equalsIgnoreCase("name")){
                    return bfPlayer.getName();
                }else if(params.equalsIgnoreCase("email")){
                    return bfPlayer.getEmail();
                }else if(params.equalsIgnoreCase("qq")){
                    return bfPlayer.getQQ();
                }else if(params.equalsIgnoreCase("sex")){
                    return bfPlayer.getSex();
                }else if(params.equalsIgnoreCase("head")){
                    return bfPlayer.getHead();
                }else if(params.equalsIgnoreCase("head_border")){
                    return bfPlayer.getHeadBorder();
                }else if(params.equalsIgnoreCase("signature")){
                    return bfPlayer.getSignature();
                }else if(params.equalsIgnoreCase("nickname")){
                    return bfPlayer.getNickname();
                }else if(params.equalsIgnoreCase("friends_number")){
                    return String.valueOf(bfPlayer.getFriendList().size());
                }else if(params.equalsIgnoreCase("online_friends_number")){
                    int num = 0;
                    for(BFPlayer b :bfPlayer.getFriendList()){
                        if(b.isBCOnline()){
                            num ++;
                        }
                    }
                    return String.valueOf(num);
                }else if(params.equalsIgnoreCase("mails_number")){
                    return String.valueOf(bfPlayer.getMailBox().size());
                }else if(params.equalsIgnoreCase("read_mails_number")){
                    int num = 0;
                    for(Mail m :bfPlayer.getMailBox()){
                        if(m.isRead(bfPlayer)){
                            num ++;
                        }
                    }
                    return String.valueOf(num);
                }else if(params.equalsIgnoreCase("not_read_mails_number")){
                    int num = 0;
                    for(Mail m :bfPlayer.getMailBox()){
                        if(!m.isRead(bfPlayer)){
                            num ++;
                        }
                    }
                    return String.valueOf(num);
                }else if(params.equalsIgnoreCase("messages_number")){
                    return String.valueOf(bfPlayer.getMessageList().size());
                }else if(params.equalsIgnoreCase("requesters_number")){
                    return String.valueOf(bfPlayer.getRequesterList().size());
                }
                return "%bluefriends_"+params+"%";
            }
        }));
    }

    public static BlueFriends getInstance() {
        return instance;
    }

    public static BFDatabase getBFDatabase(){
        return BFDatabase.getInstance();
    }

    public static MailEditor getMailEditor(){
        return MailEditor.getInstance();
    }

    public static GuiManager getGuiManager(){
        return GuiManager.getInstance();
    }

    public static List<BFPlayer> getBFPlayers(){
        List<BFPlayer> result = new ArrayList<>();
        getBFDatabase().getPlayerTable().getRows().forEach(row -> result.add(BFPlayer.getBFPlayer(UUID.fromString(row.getMainValue()))));
        return result;
    }

    public static List<Mail> getServerMailList(){
        List<Mail> result = new ArrayList<>();
        getBFDatabase().getMailTable().getRows().forEach(row ->result.add(new Mail(UUID.fromString(row.getMainValue()))));
        return result;
    }
}
