package cn.bluesadi.bluefriends.player;

import cn.bluesadi.bluefriends.BFDatabase;
import cn.bluesadi.bluefriends.config.Config;
import cn.bluesadi.bluefriends.database.Row;
import cn.bluesadi.bluefriends.gui.BluesGui;
import cn.bluesadi.bluefriends.gui.GuiManager;
import cn.bluesadi.bluefriends.mail.Mail;
import cn.bluesadi.bluefriends.util.BFCalendar;
import cn.bluesadi.bluefriends.util.BFUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import java.util.*;
import static cn.bluesadi.bluefriends.player.PlayerAttributes.*;

public class BFPlayer {

    protected UUID uuid;
    protected Row sqlRow;
    private boolean fake;

    protected BFPlayer(UUID uuid){
        this.uuid = uuid;
        this.sqlRow = BFDatabase.getInstance().getPlayerTable().getRow(uuid.toString());
        if(BFUtil.existsPlayer(uuid)) {
            fake = false;
            if (!sqlRow.existsRow()) {
                setBCOnline(false);
                setEmail(Config.DEFAULT_EMAIL);
                setNickname(getOfflinePlayer().getName());
                setQQ(Config.DEFAULT_QQ);
                setSex(Config.DEFAULT_SEX);
                setHead(Config.DEFAULT_HEAD);
                setSignature(Config.DEFAULT_SIGNATURE);
                setHeadBorder(Config.DEFAULT_HEAD_BORDER);
                sqlRow.set(FRIEND_LIST, Config.DEFAULT_FRIEND_LIST);
                sqlRow.set(REQUESTER_LIST, Collections.EMPTY_LIST);
                sqlRow.set(MESSAGE_LIST, Config.DEFAULT_MESSAGE_LIST);
                sqlRow.set(HEAD_LIST, Config.DEFAULT_HEAD_LIST);
                sqlRow.set(HEAD_BORDER_LIST, Config.DEFAULT_HEAD_BORDER_LIST);
                sqlRow.set(MAIL_BOX, Config.DEFAULT_MAIL_BOX);
            }
        }else{
            fake = true;
        }
    }

    public static BFPlayer getBFPlayer(UUID uuid){
        return new BFPlayer(uuid);
    }

    public static BFPlayer createFakePlayer(String name,String nickName){
        UUID uuid = BFUtil.getPlayerUUID(name);
        BFPlayer fakePlayer = new BFPlayer(uuid);
        fakePlayer.sqlRow.set(NAME,name);
        fakePlayer.setNickname(nickName);
        fakePlayer.setBCOnline(true);
        fakePlayer.setEmail(Config.DEFAULT_EMAIL);
        fakePlayer.setQQ(Config.DEFAULT_QQ);
        fakePlayer.setSex(Config.DEFAULT_SEX);
        fakePlayer.setHead(Config.DEFAULT_HEAD);
        fakePlayer.setSignature(Config.DEFAULT_SIGNATURE);
        fakePlayer.setHeadBorder(Config.DEFAULT_HEAD_BORDER);
        fakePlayer.sqlRow.set(FRIEND_LIST, Config.DEFAULT_FRIEND_LIST);
        fakePlayer.sqlRow.set(REQUESTER_LIST, Collections.EMPTY_LIST);
        fakePlayer.sqlRow.set(MESSAGE_LIST, Config.DEFAULT_MESSAGE_LIST);
        fakePlayer.sqlRow.set(HEAD_LIST, Config.DEFAULT_HEAD_LIST);
        fakePlayer.sqlRow.set(HEAD_BORDER_LIST, Config.DEFAULT_HEAD_BORDER_LIST);
        fakePlayer.sqlRow.set(MAIL_BOX, Config.DEFAULT_MAIL_BOX);
        BFDatabase.getInstance().getFakePlayerTable().getRow(name).set(UUID,uuid.toString());
        return fakePlayer;
    }

    public boolean isFakePlayer(){
        return fake;
    }

    /**
     * 向玩家发送一条消息
     * 如果配置文件中enable_message_box的选项为true,则发送一个MessageBox
     * 如果上述选项为false 并且玩家在线 则发送一条普通的消息
     * 如果玩家不在线则发送一条留言
     * @param msg 发送的消息
     * */
    public void sendMessageBox(String msg){
        if(isFakePlayer()){
            return;
        }
        if(isBCOnline()){
            if(isOnline()){
                BluesGui box = GuiManager.getInstance().createGui(Config.MESSAGE_BOX,getPlayer());
                box.getComponents().forEach(c -> c.setPlaceholderFunction(raw -> PlaceholderAPI.setPlaceholders(getOfflinePlayer(),raw).replaceAll("%system_message%",msg)));
                box.open();
            }else{
                BFUtil.sendPluginMessage(getName(),msg);
            }
        }else{
            addMessage(msg);
        }
    }

    public void sendMessage(String msg){
        if(isFakePlayer()){
            return;
        }
        if(isBCOnline()){
            if(isOnline()){
                getPlayer().sendMessage(PlaceholderAPI.setPlaceholders(getOfflinePlayer(),msg));
            }else{
                BFUtil.sendPluginMessage(getName(),msg);
            }
        }else{
            addMessage(msg);
        }
    }

    /**
     * @return 玩家的UUID
     * */
    public UUID getUUID() {
        return uuid;
    }

    public String getName(){
        return !isFakePlayer() ? getOfflinePlayer().getName() : sqlRow.getValue(NAME).getString();
    }

    /**
     * @return OfflinePlayer
     * */
    public OfflinePlayer getOfflinePlayer(){
        return Bukkit.getOfflinePlayer(uuid);
    }

    /**
     * @return Player
     * 当该玩家不在线或不存在时返回null
     * */
    public Player getPlayer(){
        if(isFakePlayer()){
            throw new UnsupportedOperationException("无法对假玩家执行getPlayer方法!");
        }
        return Bukkit.getPlayer(uuid);
    }

    /**
     * @return true 如果玩家在本服务器在线
     * */
    public boolean isOnline(){
        if(isFakePlayer()){
            return true;
        }
        return getOfflinePlayer().isOnline();
    }

    /**
     * @return true如果玩家在群组服子服上的某一个服务器在线
     * */
    public boolean isBCOnline(){
        if(isFakePlayer()){
            return true;
        }
        return sqlRow.getValue(ONLINE).getBoolean();
    }

    public void setBCOnline(boolean bcOnline){
        sqlRow.set(ONLINE,bcOnline);
    }

    public List<BFPlayer> getFriendList(){
        List<String> friends = sqlRow.getValue(FRIEND_LIST).getStringList();
        List<BFPlayer> result = new ArrayList<>();
        friends.forEach(uuid->result.add(new BFPlayer(java.util.UUID.fromString(uuid))));
        return result;
    }

    public void addFriend(BFPlayer bfPlayer){
        List<String> friends = sqlRow.getValue(FRIEND_LIST).getStringList();
        friends.add(bfPlayer.getUUID().toString());
        sqlRow.set(FRIEND_LIST,friends);
    }

    public void removeFriend(BFPlayer bfPlayer){
        List<String> friends = sqlRow.getValue(FRIEND_LIST).getStringList();
        friends.remove(bfPlayer.getUUID().toString());
        sqlRow.set(FRIEND_LIST, friends);
    }

    public boolean hasFriend(BFPlayer bfPlayer){
        return sqlRow.getValue(FRIEND_LIST).getStringList().contains(bfPlayer.getUUID().toString());
    }

    public boolean hasMail(String uuid){
        return sqlRow.getValue(MAIL_BOX).getStringList().contains(uuid);
    }

    public boolean hasMessage(String uuid){
        return sqlRow.getValue(MESSAGE_LIST).getStringList().contains(uuid);
    }

    public List<BFPlayer> getRequesterList(){
        List<String> requesterList = sqlRow.getValue(REQUESTER_LIST).getStringList();
        List<BFPlayer> result = new ArrayList<>();
        requesterList.forEach(uuid->result.add(new BFPlayer(java.util.UUID.fromString(uuid))));
        return result;
    }

    public boolean hasRequester(BFPlayer bfPlayer){
        return sqlRow.getValue(REQUESTER_LIST).getStringList().contains(bfPlayer.getUUID().toString());
    }

    public void addRequester(BFPlayer bfPlayer){
        List<String> requesters = sqlRow.getValue(REQUESTER_LIST).getStringList();
        requesters.add(bfPlayer.getUUID().toString());
        sqlRow.set(REQUESTER_LIST,requesters);
    }

    public void removeRequester(BFPlayer bfPlayer){
        List<String> requesters = sqlRow.getValue(REQUESTER_LIST).getStringList();
        requesters.remove(bfPlayer.getUUID().toString());
        sqlRow.set(REQUESTER_LIST, requesters);
    }

    public List<SystemMessage> getMessageList(){
        List<String> messages = sqlRow.getValue(MESSAGE_LIST).getStringList();
        List<SystemMessage> result = new LinkedList<>();
        messages.forEach(uuid->result.add(0,new SystemMessage(java.util.UUID.fromString(uuid))));
        return result;
    }

    public void addMessage(String message){
        List<String> messages = sqlRow.getValue(MESSAGE_LIST).getStringList();
        UUID id = java.util.UUID.randomUUID();
        String date = BFCalendar.getDate(Config.DATE_FORMAT);
        messages.add(id.toString());
        sqlRow.set(MESSAGE_LIST,messages);
        Row messageRow = BFDatabase.getInstance().getMessageTable().getRow(id.toString());
        messageRow.set(MessageAttributes.DATE,date);
        messageRow.set(MessageAttributes.CONTENT,message);
    }

    public void deleteMessage(UUID id){
        List<String> messages = sqlRow.getValue(MESSAGE_LIST).getStringList();
        messages.remove(id.toString());
        sqlRow.set(UUID,messages);
        BFDatabase.getInstance().getMessageTable().removeRow(id.toString());
    }

    public List<Mail> getMailBox(){
        List<String> mailBox = sqlRow.getValue(MAIL_BOX).getStringList();
        List<Mail> result = new ArrayList<>();
        mailBox.forEach(uuid->result.add(new Mail(java.util.UUID.fromString(uuid))));
        return result;
    }

    public void addMail(UUID id){
        List<String> mails = sqlRow.getValue(MAIL_BOX).getStringList();
        mails.add(id.toString());
        sqlRow.set(MAIL_BOX,mails);
    }

    public void deleteMail(UUID id){
        List<String> mails = sqlRow.getValue(MAIL_BOX).getStringList();
        mails.remove(id.toString());
        sqlRow.set(MAIL_BOX,mails);
    }

    public List<String> getHeadList(){
        return sqlRow.getValue(HEAD_LIST).getStringList();
    }

    public void addHead(String url){
        List<String> headList = getHeadList();
        headList.add(0,url);
        sqlRow.set(HEAD_LIST,headList);
    }

    public void deleteHead(String url){
        List<String> headList = getHeadList();
        headList.remove(url);
        sqlRow.set(HEAD_LIST,headList);
    }

    public List<String> getHeadBorderList(){
        return sqlRow.getValue(HEAD_BORDER_LIST).getStringList();
    }

    public void addHeadBorder(String url){
        List<String> headList = getHeadList();
        headList.add(0,url);
        sqlRow.set(HEAD_BORDER_LIST,headList);
    }

    public void deleteHeadBorder(String url){
        List<String> headList = getHeadList();
        headList.remove(url);
        sqlRow.set(HEAD_BORDER_LIST,headList);
    }

    public void setEmail(String email){
        sqlRow.set(EMAIL,email);
    }

    public String getEmail(){
        return sqlRow.getValue(EMAIL).getString();
    }

    public void setQQ(String qq){
        sqlRow.set(QQ,qq);
    }

    public String getQQ(){
        return sqlRow.getValue(QQ).getString();
    }

    public void setSex(String sex){
        sqlRow.set(SEX,sex);
    }

    public String getSex(){
        return sqlRow.getValue(SEX).getString();
    }

    public void setHead(String head){
        sqlRow.set(HEAD,head);
    }

    public String getHead(){
        return sqlRow.getValue(HEAD).getString();
    }

    public void setSignature(String signature) {
        sqlRow.set(SIGNATURE,signature);
    }

    public String getSignature(){
        return sqlRow.getValue(SIGNATURE).getString();
    }

    public void setHeadBorder(String headBorder){
        sqlRow.set(HEAD_BORDER,headBorder);
    }

    public String getHeadBorder(){
        return sqlRow.getValue(HEAD_BORDER).getString();
    }

    public void setNickname(String nickname){
        sqlRow.set(NICKNAME,nickname);
    }

    public String getNickname(){
        return sqlRow.getValue(NICKNAME).getString();
    }

}
