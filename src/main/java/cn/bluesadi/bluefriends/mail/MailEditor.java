package cn.bluesadi.bluefriends.mail;

import cn.bluesadi.bluefriends.BFDatabase;
import cn.bluesadi.bluefriends.BlueFriends;
import cn.bluesadi.bluefriends.config.Config;
import cn.bluesadi.bluefriends.database.Row;
import cn.bluesadi.bluefriends.player.BFPlayer;
import cn.bluesadi.bluefriends.util.BFCalendar;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static cn.bluesadi.bluefriends.mail.MailAttributes.*;

public class MailEditor {
    private static MailEditor instance = new MailEditor();
    private String subject;
    private List<ItemStack> items = new ArrayList<>();
    private String content;

    public static MailEditor getInstance() {
        return instance;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    /**
     * 从数据库中读取邮件到右键编辑器
     * @param uuid 邮件的uuid
     * */
    public void load(UUID uuid){
        Row sqlRow = BFDatabase.getInstance().getMailTable().getRow(uuid.toString());
        this.subject = sqlRow.getValue(SUBJECT).getString();
        this.items = sqlRow.getValue(ITEMS).getItemStackList();
        this.content = sqlRow.getValue(CONTENT).getString();
    }

    private boolean checkNull(){
        return items != null && content != null && subject != null;
    }

    /**
     * 清除邮件编辑器的内容
     * */
    public void clear(){
        subject = null;
        items = new ArrayList<>();
        content = null;
    }

    /**
     * 将已经编辑好的邮件保存到数据库
     * @return false 如果邮件中有属性还没有编辑
     * */
    public boolean save(){
        UUID uuid = java.util.UUID.randomUUID();
        Row sqlRow = BFDatabase.getInstance().getMailTable().getRow(uuid.toString());
        if(checkNull()){
            sqlRow.set(SUBJECT,subject);
            sqlRow.set(DATE,"暂未设置");
            sqlRow.set(ITEMS,items);
            sqlRow.set(CONTENT,content);
            sqlRow.set(READ,false);
        }
        return false;
    }

    /**
     * 将这封邮件发给某个特定的玩家
     * @param bfPlayer 玩家
     * */
    public boolean send(BFPlayer bfPlayer){
        UUID uuid = java.util.UUID.randomUUID();
        Row sqlRow = BFDatabase.getInstance().getMailTable().getRow(uuid.toString());
        if(checkNull()){
            sqlRow.set(SUBJECT,subject);
            sqlRow.set(DATE, BFCalendar.getDate(Config.DATE_FORMAT));
            sqlRow.set(ITEMS,items);
            sqlRow.set(CONTENT,content);
            sqlRow.set(READ,false);
            bfPlayer.addMail(uuid);
        }
        return false;
    }

    /**
     * 将这封邮件发给所有玩家
     * */
    public boolean sendToAll(){
        if(checkNull()) {
            BlueFriends.getBFPlayers().forEach(bfPlayer -> send(bfPlayer));
            return true;
        }
        return false;
    }

    /**
     * 将这封邮件发给所有玩家满足指定条件的玩家
     * */
    public boolean sendIf(String condition){
        ReceiveCondition receiveCondition = new ReceiveCondition(condition);
        if(checkNull()){
            BlueFriends.getBFPlayers().forEach(bfPlayer -> {
                if(receiveCondition.test(bfPlayer)){
                    send(bfPlayer);
                }
            });
            return true;
        }
        return false;
    }


}
