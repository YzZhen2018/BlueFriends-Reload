package cn.bluesadi.bluefriends;

import cn.bluesadi.bluefriends.database.DBManager;
import cn.bluesadi.bluefriends.database.Schema;
import cn.bluesadi.bluefriends.database.Table;
import cn.bluesadi.bluefriends.player.MessageAttributes;

import static cn.bluesadi.bluefriends.player.PlayerAttributes.*;

public class BFDatabase {

    private static BFDatabase instance;
    private DBManager dbManager;
    private Schema schema;
    private Table playerTable;
    private Table mailTable;
    private Table messageTable;

    private BFDatabase(){
        instance = this;
        dbManager = new DBManager("bluefriends");
        schema = instance.dbManager.getSchema();
        schema.createTable("players",UUID,ONLINE,EMAIL,QQ,SEX,SIGNATURE,FRIEND_LIST,MESSAGE_LIST,MAIL_BOX);
        schema.createTable("mails");
        schema.createTable("messages",UUID, MessageAttributes.DATE,MessageAttributes.CONTENT);
        playerTable = instance.schema.getTable("players");
        mailTable = instance.schema.getTable("mails");
        messageTable = instance.schema.getTable("messages");
    }

    public static void load(){
        instance = new BFDatabase();
    }

    public static BFDatabase getInstance(){
        return instance;
    }

    public Table getPlayerTable() {
        return playerTable;
    }

    public Table getMailTable() {
        return mailTable;
    }

    public Table getMessageTable() {
        return messageTable;
    }

    public void close(){
        dbManager.close();
    }
}
