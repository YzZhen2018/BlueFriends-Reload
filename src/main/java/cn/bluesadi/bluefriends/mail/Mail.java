package cn.bluesadi.bluefriends.mail;

import cn.bluesadi.bluefriends.BFDatabase;
import cn.bluesadi.bluefriends.database.Row;
import cn.bluesadi.bluefriends.player.BFPlayer;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static cn.bluesadi.bluefriends.mail.MailAttributes.*;

public class Mail {

    private UUID uuid;
    private Row sqlRow;
    private String subject;
    private String date;
    private List<ItemStack> items;
    private String content;
    private boolean read;

    public Mail(java.util.UUID uuid){
        this.uuid = uuid;
        this.sqlRow = BFDatabase.getInstance().getMailTable().getRow(uuid.toString());
        this.subject = sqlRow.getValue(SUBJECT).getString();
        this.date = sqlRow.getValue(DATE).getString();
        this.items = sqlRow.getValue(ITEMS).getItemStackList();
        this.content = sqlRow.getValue(CONTENT).getString();
        this.read = sqlRow.getValue(READ).getBoolean();
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public String getSubject() {
        return subject;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        sqlRow.set(READ,read);
    }

    public void clearItems(){
        sqlRow.set(ITEMS,new ArrayList<>());
    }

    public void delete(){
        BFDatabase.getInstance().getMailTable().removeRow(uuid.toString());
    }
}
