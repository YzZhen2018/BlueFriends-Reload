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
import cn.bluesadi.bluefriends.util.BFUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import me.clip.placeholderapi.ServerLoadEventListener;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class BlueFriends extends JavaPlugin implements Listener {

    private static BlueFriends instance;
    private static final String CURRENT_CONFIG_VERSION = "1.0";
    private static final String CURRENT_MESSAGE_VERSION = "1.1";

    @Override
    public void onEnable() {
        try {
            instance = this;
            BFLogger.info("开始加载BlueFriends-Reload!");
            BFLogger.info("正在检查前置插件是否齐全...");
            if (!(checkDependedPlugin("VexView") && checkDependedPlugin("ProtocolLib") && checkDependedPlugin("PlaceholderAPI"))) {
                BFLogger.error("检测到有前置插件没有安装,插件即将自动卸载!");
                disablePlugin();
                return;
            }
            BFLogger.info("正在加载插件配置文件...");
            saveResource("map.yml", false);
            BFUtil.mkdirs("error");
            Config.load();
            Message.load();
            checkVersion();
            BFLogger.info("正在连接数据库...");
            BFLogger.info("检测到数据库类型为:§e" + Config.DATABASE_TYPE);
            if (!BFDatabase.load()) {
                BFLogger.error("插件即将自动卸载");
                disablePlugin();
                return;
            }
            //BFLogger.info("正在分配默认权限...");
            //BFLogger.info("注册了" + DefaultPermissions.register() + "个默认权限");
            BFLogger.info("正在注册插件指令...");
            Bukkit.getPluginCommand("friend").setExecutor(new Friend());
            Bukkit.getPluginCommand("bfreload").setExecutor(new BFReload());
            Bukkit.getPluginCommand("bfopen").setExecutor(new BFOpen());
            Bukkit.getPluginCommand("bfadmin").setExecutor(new BFAdmin());
            Bukkit.getPluginCommand("person").setExecutor(new Person());
            BFLogger.info("正在注册插件监听器...");
            Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
            Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
            Bukkit.getPluginManager().registerEvents(this, this);
            BFLogger.info("正在注册PlaceholderAPI变量");
            registerPlaceholderHook();
            if (Config.BUNGEECORD) {
                BFLogger.info("检测到BungeeCord选项开启,设定该服务端为群组端子端");
                BFLogger.info("正在注册通信频道...");
                this.getServer().getMessenger().registerOutgoingPluginChannel(this, Config.CHANNEL_BUKKIT_TO_BUNGEE);
                this.getServer().getMessenger().registerIncomingPluginChannel(this, Config.CHANNEL_BUNGEE_TO_BUKKIT, new MessageListener());
            }
            BFLogger.info("BlueFriends-Reload加载完毕!");
        }catch (Exception e){
            BFLogger.error("BlueFriends无法正常启动",e);
            disablePlugin();
        }
    }

    @EventHandler
    public void onServerLoaded(ServerLoadEvent event){
        BFLogger.info("正在导入GUI界面");
        GuiManager.loadGuiManager();
    }

    private void disablePlugin(){
        Bukkit.getPluginManager().disablePlugin(this);
        new BukkitRunnable(){

            @Override
            public void run() {
                Bukkit.getLogger().info("§cBlueFriends没有正常加载,请检查启动日志!");
            }

        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("VexView"),100L,100L);
    }

    private boolean checkDependedPlugin(String name){
        if(Bukkit.getPluginManager().isPluginEnabled(name)){
            BFLogger.info(name+"已装载!");
            return true;
        }
        BFLogger.error(name+"未装载!");
        return false;
    }

    private void checkVersion(){
        if(!Config.VERSION.equals(CURRENT_CONFIG_VERSION)){
            BFLogger.error("检测到配置文件config.yml版本不正确，这可能会导致一些配置无法正常使用");
            BFLogger.error("当前的版本为:"+CURRENT_CONFIG_VERSION+"，请删除config.yml让插件重新生成");
            BFLogger.error("或自行修改config.yml的内容并将version选项修改为:"+CURRENT_CONFIG_VERSION);
        }
        if(!Message.VERSION.equalsIgnoreCase(CURRENT_MESSAGE_VERSION)){
            BFLogger.error("检测到配置文件message.yml版本不正确，这可能会导致一些文本无法正常显示");
            BFLogger.error("当前的版本为:"+CURRENT_MESSAGE_VERSION+"，请删除message.yml让插件重新生成");
            BFLogger.error("或自行修改message.yml的内容并将version选项修改为:"+CURRENT_MESSAGE_VERSION);
        }
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
                return "%bluefriends_"+params+"%变量解析失败";
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
