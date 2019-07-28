package cn.bluesadi.bluefriends.gui;

import cn.bluesadi.bluefriends.BlueFriends;
import cn.bluesadi.bluefriends.config.Config;
import cn.bluesadi.bluefriends.gui.component.*;
import cn.bluesadi.bluefriends.gui.component.extra.CollectionScrollingList;
import cn.bluesadi.bluefriends.gui.component.extra.CollectionScrollingListFunction;
import cn.bluesadi.bluefriends.mail.Mail;
import cn.bluesadi.bluefriends.mail.MailAttributes;
import cn.bluesadi.bluefriends.player.BFPlayer;
import cn.bluesadi.bluefriends.player.SystemMessage;
import cn.bluesadi.bluefriends.util.BFLogger;
import cn.bluesadi.bluefriends.util.BFUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;
import java.util.function.Function;

public class GuiManager {

    private static GuiManager instance;
    private static int index = Integer.MIN_VALUE;
    private Map<UUID,BluesGui> openingGui = new HashMap<>();
    private Map<String, FileConfiguration> guiConfigurations = new HashMap<>();
    private Map<String,String> guiMap = new HashMap<>();


    public GuiManager(){
        loadGuiConfigurations();
        loadGuiMap();
    }

    public static void loadGuiManager(){
        instance = new GuiManager();
    }

    public int getIndex(){
        index += 1;
        return index;
    }

    public BluesGui getOpeningGui(Player viewer){
        if(openingGui.containsKey(viewer.getUniqueId())) {
            return openingGui.get(viewer.getUniqueId());
        }
        return null;
    }

    public void setOpeningGui(Player viewer,BluesGui gui) {
        openingGui.put(viewer.getUniqueId(),gui);
    }

    private static boolean check(String type,String... args){
        for(int i = 0;i<args.length;i++){
            if(args[i] == null){
                BFLogger.error("§c导入§e"+type+"§e类型组件失败,原因:组件属性不全");
                return false;
            }
        }
        return true;
    }

    public List<BluesComponent> loadComponents(BluesGui gui,ConfigurationSection section){
        return loadComponents(gui,section,null);
    }

    public List<BluesComponent> loadComponents(BluesGui gui, ConfigurationSection section, Function<String,String> placeholder){
        List<BluesComponent> components  = new ArrayList<>();
        Set<String> keys = section.getKeys(false);
        keys.forEach(key -> {
            BluesComponent component = loadComponent(gui,section.getConfigurationSection(key));
            if(component != null) {
                if(placeholder != null){
                    component.setPlaceholderFunction(placeholder);
                }
                components.add(component);
            }
        });
        return components;
    }

    private BluesComponent loadComponent(BluesGui gui,ConfigurationSection section){
        int x = section.getInt("x");
        int y = section.getInt("y");
        String type = section.getString("type");
        BluesComponent component = null;
        if(type.equalsIgnoreCase(CompoentType.BUTTON)){
            String name = section.getString("name","");
            String url1 = section.getString("url1");
            String url2 = section.getString("url2");
            int w = section.getInt("w",16);
            int h = section.getInt("h",9);
            List<String> commands = section.getStringList("commands");
            if(check(CompoentType.BUTTON,name,url1,url2)) {
                component = new BluesButton(gui, x, y, name, url1, url2, w,h,commands);
            }
        }else if(type.equalsIgnoreCase(CompoentType.IMAGE)){
            String url = section.getString("url");
            int w = section.getInt("w",64);
            int h = section.getInt("h",64);
            if(check(CompoentType.IMAGE,url)) {
                List<String> hoverTexts = section.getStringList("hover_texts");
                if(!hoverTexts.isEmpty()) {
                    component = new BluesImage(gui, x, y, url, w, h, hoverTexts);
                }else{
                    component = new BluesImage(gui, x, y, url, w, h);
                }
            }
        }else if(type.equalsIgnoreCase(CompoentType.TEXT)){
            List<String> contents = section.getStringList("contents");
            int linefeed = section.getInt("linefeed",0);
            int omit = section.getInt("omit",0);
            component = new BluesText(gui,x,y,contents,linefeed,omit);
        }else if(type.equalsIgnoreCase(CompoentType.TEXTFIELD)){
            int w = section.getInt("w",80);
            int h = section.getInt("h",10);
            int maxString = section.getInt("maxString",100);
            List<String> commands = section.getStringList("commands");
            component = new BluesTextField(gui,x,y,w,h,maxString,commands);
        }else if(type.equalsIgnoreCase(CompoentType.PLAYER_DRAW)){
            int size = section.getInt("size");
            component = new BluesPlayerDraw(gui,x,y,size);
        }else if(type.equalsIgnoreCase(CompoentType.SCROLLING_LIST)){
            int w = section.getInt("w",150);
            int h = section.getInt("h",400);
            int fullH = section.getInt("full_h",1200);
            List<BluesComponent> components = loadComponents(gui,section.getConfigurationSection("components"));
            components.forEach(component1->{
                component1.setX(component1.getX() + x);
                component1.setY(component1.getY() + y);
            });
            component = new BluesScrollingList(gui,x,y,w,h,fullH,components);
        }else if(type.equalsIgnoreCase(CompoentType.FRIEND_LIST) || type.equalsIgnoreCase(CompoentType.MESSAGE_LIST)
                || type.equalsIgnoreCase(CompoentType.MAIL_BOX) || type.equalsIgnoreCase(CompoentType.FAKE_PLAYER_LIST)
                || type.equalsIgnoreCase(CompoentType.REQUESTER_LIST) || type.equalsIgnoreCase(CompoentType.SERVER_MAIL_LIST)){
            int w = section.getInt("w",150);
            int h = section.getInt("h",400);
            if(type.equalsIgnoreCase(CompoentType.FRIEND_LIST)){
                List<BFPlayer> friends = BFPlayer.getBFPlayer(gui.getViewer().getUniqueId()).getFriendList();
                CollectionScrollingListFunction<BFPlayer> placeholder = (raw,friend) -> PlaceholderAPI.setPlaceholders(friend.getOfflinePlayer(),raw);
                component = new CollectionScrollingList<>(gui,x,y,w,h,friends,section,placeholder);
            }else if(type.equalsIgnoreCase(CompoentType.MESSAGE_LIST)){
                List<SystemMessage> messageList = BFPlayer.getBFPlayer(gui.getViewer().getUniqueId()).getMessageList();
                CollectionScrollingListFunction<SystemMessage> placeholder = (raw,message) -> PlaceholderAPI.setPlaceholders(gui.getViewer(),raw)
                            .replaceAll("%message_uuid%",message.getUUID().toString())
                            .replaceAll("%message_date%",message.getDate())
                            .replaceAll("%message_content%",message.getContent());

                component = new CollectionScrollingList<>(gui,x,y,w,h,messageList,section,placeholder);
            }else if(type.equalsIgnoreCase(CompoentType.MAIL_BOX)){
                List<Mail> mailList = BFPlayer.getBFPlayer(gui.getViewer().getUniqueId()).getMailBox();
                CollectionScrollingListFunction<Mail> placeholder = (raw,mail) -> PlaceholderAPI.setPlaceholders(gui.getViewer(),raw)
                        .replaceAll("%mail_uuid%",mail.getUUID().toString())
                        .replaceAll("%mail_date%",mail.getDate())
                        .replaceAll("%mail_subject%",mail.getSubject())
                        .replaceAll("%mail_content%",mail.getContent())
                        .replaceAll("%mail_read%",mail.isRead(BFPlayer.getBFPlayer(gui.getViewer().getUniqueId())) ? Config.READ : Config.NOT_READ)
                        .replaceAll("%mail_items%",String.valueOf(mail.getItems().size()));
                component = new CollectionScrollingList<>(gui,x,y,w,h,mailList,section,placeholder);
            }else if(type.equalsIgnoreCase(CompoentType.FAKE_PLAYER_LIST)){
                List<BFPlayer> fakePlayerList = new ArrayList<>();
                BlueFriends.getBFDatabase().getFakePlayerTable().getRows().forEach(fakePlayer -> fakePlayerList
                        .add(BFPlayer.getBFPlayer(BFUtil.getPlayerUUID(fakePlayer.getValue("name").getString()))));
                CollectionScrollingListFunction<BFPlayer> placeholder = (raw,fake) -> PlaceholderAPI.setPlaceholders(fake.getOfflinePlayer(),raw);
                component = new CollectionScrollingList<>(gui,x,y,w,h,fakePlayerList,section,placeholder);
            }else if(type.equalsIgnoreCase(CompoentType.REQUESTER_LIST)){
                List<BFPlayer> requesterList = BFPlayer.getBFPlayer(gui.getViewer().getUniqueId()).getRequesterList();
                CollectionScrollingListFunction<BFPlayer> placeholder = (raw,requester) -> PlaceholderAPI.setPlaceholders(requester.getOfflinePlayer(),raw);
                component = new CollectionScrollingList<>(gui,x,y,w,h,requesterList,section,placeholder);
            }else if(type.equalsIgnoreCase(CompoentType.SERVER_MAIL_LIST)){
                List<Mail> mailList = BlueFriends.getServerMailList();
                CollectionScrollingListFunction<Mail> placeholder = (raw,mail) -> PlaceholderAPI.setPlaceholders(gui.getViewer(),raw)
                        .replaceAll("%mail_uuid%",mail.getUUID().toString())
                        .replaceAll("%mail_date%",mail.getDate())
                        .replaceAll("%mail_subject%",mail.getSubject())
                        .replaceAll("%mail_content%",mail.getContent())
                        .replaceAll("%mail_read%",mail.isRead(BFPlayer.getBFPlayer(gui.getViewer().getUniqueId())) ? Config.READ : Config.NOT_READ)
                        .replaceAll("%mail_items%",String.valueOf(mail.getItems().size()));
                component = new CollectionScrollingList<>(gui,x,y,w,h,mailList,section,placeholder);
            }
        }
        if(component != null){
            component.setPermission(section.getString("permission"));
        }else {
            BFLogger.error("§e" + type + "§c组件类型不存在,请检查你的配置!");
        }
        return component;
    }

    public void loadGuiConfigurations(){
        File folder = new File(BlueFriends.getInstance().getDataFolder(),"gui");
        File[] files = folder.listFiles();
        for(File file : files){
            if(file.getName().endsWith(".yml")){
                this.guiConfigurations.put(file.getName(), YamlConfiguration.loadConfiguration(file));
            }
        }
    }

    public void loadGuiMap(){
        BlueFriends.getInstance().saveResource("map.yml",false);
        File mapFile = new File(BlueFriends.getInstance().getDataFolder(),"map.yml");
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(mapFile);
        configuration.getKeys(false).forEach(key->guiMap.put(key,configuration.getString(key)));
    }

    public BluesGui createGui(String name,Player viewer){
        String fileName = name;
        if(guiMap.keySet().contains(fileName)){
            fileName = guiMap.get(name);
        }
        if(guiConfigurations.keySet().contains(fileName)){
            FileConfiguration guiConfig = guiConfigurations.get(fileName);
            int x = guiConfig.getInt("x",-1);
            int y = guiConfig.getInt("y",-1);
            int w = guiConfig.getInt("w",320);
            int h = guiConfig.getInt("h",180);
            String url = guiConfig.getString("url","[local]gui.png");
            boolean allowBack = guiConfig.getBoolean("allow_back",true);
            BluesGui gui = new BluesGui(x,y,url,w,h,viewer,fileName,allowBack);
            gui.setComponents(loadComponents(gui,guiConfig.getConfigurationSection("components")));
            return gui;
        }
        return null;
    }

    public static GuiManager getInstance() {
        return instance;
    }
}
