package cn.bluesadi.bluefriends.mail;

import cn.bluesadi.bluefriends.BFDatabase;
import cn.bluesadi.bluefriends.BlueFriends;
import cn.bluesadi.bluefriends.database.Row;
import cn.bluesadi.bluefriends.player.BFPlayer;
import cn.bluesadi.bluefriends.util.BFLogger;
import org.bukkit.inventory.ItemStack;
import java.util.List;
import java.util.UUID;
import static cn.bluesadi.bluefriends.mail.MailAttributes.*;

public class Mail {

    private UUID uuid;
    private Row row;

    public Mail(java.util.UUID uuid){
        this.uuid = uuid;
        this.row = BlueFriends.getBFDatabase().getMailTable().getRow(uuid.toString());
        check();
    }

    private void check(){
        String test = getContent();
        if(test == null || test.isEmpty()){
            BFLogger.error("邮件§e"+uuid+"§c数据异常，无法正确读取邮件信息");
        }
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getDate() {
        return row.getValue(DATE).getString();
    }

    public String getContent() {
        return row.getValue(CONTENT).getString();
    }

    public List<ItemStack> getItems() {
        return row.getValue(ITEMS).getItemStackList();
    }

    public String getSubject() {
        return row.getValue(SUBJECT).getString();
    }

    public boolean isRead(BFPlayer bfPlayer) {
        return row.getValue(READ).getStringList().contains(bfPlayer.getUUID().toString());
    }

    public void setRead(BFPlayer bfPlayer) {
        List<String> readList = row.getValue(READ).getStringList();
        readList.remove(bfPlayer.getUUID().toString());
        readList.add(bfPlayer.getUUID().toString());
        row.set(READ,readList);
    }

    public boolean hasGotItems(BFPlayer bfPlayer){
        return row.getValue(GOT_ITEMS).getStringList().contains(bfPlayer.getUUID().toString());
    }

    public void setGotItems(BFPlayer bfPlayer){
        List<String> gotList = row.getValue(GOT_ITEMS).getStringList();
        gotList.remove(bfPlayer.getUUID().toString());
        gotList.add(bfPlayer.getUUID().toString());
        row.set(GOT_ITEMS,gotList);
    }

    public void setDate(String date){
        row.set(DATE,date);
    }

    public void delete(){
        BFDatabase.getInstance().getMailTable().removeRow(uuid.toString());
        BlueFriends.getBFPlayers().forEach(bfPlayer -> {
            if(bfPlayer.hasMail(uuid.toString())){
                bfPlayer.deleteMail(uuid);
            }
        });
    }
}
