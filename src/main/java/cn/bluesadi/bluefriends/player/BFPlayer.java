package cn.bluesadi.bluefriends.player;

import cn.bluesadi.bluefriends.BFDatabase;
import cn.bluesadi.bluefriends.config.Config;
import cn.bluesadi.bluefriends.database.Row;
import cn.bluesadi.bluefriends.mail.Mail;
import cn.bluesadi.bluefriends.util.BFCalendar;
import com.sun.istack.internal.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import static cn.bluesadi.bluefriends.player.PlayerAttributes.*;

public class BFPlayer {

    private UUID uuid;
    private Row sqlRow;

    public BFPlayer(UUID uuid){
        this.uuid = uuid;
        this.sqlRow = BFDatabase.getInstance().getPlayerTable().getRow(uuid.toString());
    }

    /**
     * 向玩家发送一条消息
     * 如果配置文件中enable_message_box的选项为true,则发送一个MessageBox
     * 如果上述选项为false 并且玩家在线 则发送一条普通的消息
     * 如果玩家不在线则发送一条留言
     * @param msg 发送的消息
     * */
    public void sendMessage(String msg){
        if(Config.ENABLE_MESSAGE_BOX){

        }else{

        }
    }

    /**
     * @return 玩家的UUID
     * */
    public UUID getUUID() {
        return uuid;
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
        return Bukkit.getPlayer(uuid);
    }

    /**
     * @return true 如果玩家在本服务器在线
     * */
    public boolean isOnline(){
        return getOfflinePlayer().isOnline();
    }

    /**
     * @return true如果玩家在群组服子服上的某一个服务器在线
     * */
    public boolean isBCOnline(){
        return sqlRow.getValue(ONLINE).getBoolean();
    }

    public String getAttribute(String key){
        return sqlRow.getValue(key).getString();
    }

    public void setAttribute(@NotNull String key,@NotNull String value){
        sqlRow.set(key,value);
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
        sqlRow.set(UUID,messages);
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
}
