package cn.bluesadi.bluefriends.player;

import cn.bluesadi.bluefriends.BFDatabase;
import cn.bluesadi.bluefriends.database.Row;
import cn.bluesadi.bluefriends.util.BFLogger;

import java.util.UUID;
import static cn.bluesadi.bluefriends.player.MessageAttributes.*;

public class SystemMessage {

    private UUID uuid;
    private Row sqlRow;
    private String date;
    private String content;

    public SystemMessage(UUID uuid){
        this.uuid = uuid;
        this.sqlRow = BFDatabase.getInstance().getMessageTable().getRow(uuid.toString());
        this.date = sqlRow.getValue(DATE).getString();
        this.content = sqlRow.getValue(CONTENT).getString();
        check();
    }

    private void check(){
        String test = date;
        if(test == null || test.isEmpty()){
            BFLogger.error("系统消息§e"+uuid+"§c数据异常，无法正确读取系统消息");
        }
    }

    public java.util.UUID getUUID() {
        return uuid;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public void delete(){
        BFDatabase.getInstance().getMessageTable().removeRow(uuid.toString());
    }
}
