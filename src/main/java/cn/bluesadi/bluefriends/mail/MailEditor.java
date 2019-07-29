package cn.bluesadi.bluefriends.mail;

import cn.bluesadi.bluefriends.BFDatabase;
import cn.bluesadi.bluefriends.BlueFriends;
import cn.bluesadi.bluefriends.config.Config;
import cn.bluesadi.bluefriends.database.Row;
import cn.bluesadi.bluefriends.player.BFPlayer;
import cn.bluesadi.bluefriends.util.BFCalendar;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static cn.bluesadi.bluefriends.mail.MailAttributes.*;

public class MailEditor {
    private static MailEditor instance = new MailEditor();
    private String subject = "请输入主题";
    private List<ItemStack> items = new ArrayList<>();
    private String content = "请输入正文";

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
        subject = "请输入主题";
        items = new ArrayList<>();
        content = "请输入正文";
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
            sqlRow.set(DATE,"暂未发送");
            if(!items.isEmpty()) {
                sqlRow.set(ITEMS,items);
            }
            sqlRow.set(CONTENT,content);
            sqlRow.set(READ, Collections.EMPTY_LIST);
            sqlRow.set(GOT_ITEMS,Collections.EMPTY_LIST);
        }
        return false;
    }

}
