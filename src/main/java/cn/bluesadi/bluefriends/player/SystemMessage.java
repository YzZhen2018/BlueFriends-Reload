package cn.bluesadi.bluefriends.player;

import cn.bluesadi.bluefriends.BFDatabase;
import cn.bluesadi.bluefriends.database.Row;
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
