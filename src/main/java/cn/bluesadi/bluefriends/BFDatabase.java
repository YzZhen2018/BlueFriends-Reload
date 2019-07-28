package cn.bluesadi.bluefriends;

import cn.bluesadi.bluefriends.database.DBManager;
import cn.bluesadi.bluefriends.database.Schema;
import cn.bluesadi.bluefriends.database.Table;
import cn.bluesadi.bluefriends.mail.Mail;
import cn.bluesadi.bluefriends.mail.MailAttributes;
import cn.bluesadi.bluefriends.player.MessageAttributes;
import static cn.bluesadi.bluefriends.player.PlayerAttributes.*;

public class BFDatabase {

    private static BFDatabase instance;
    private DBManager dbManager;
    private Schema schema;
    private Table playerTable;
    private Table mailTable;
    private Table messageTable;
    private Table fakePlayerTable;

    private BFDatabase(){
        instance = this;
        dbManager = new DBManager("bluefriends");
        schema = instance.dbManager.getSchema();
        schema.createTable("players",UUID,NAME,NICKNAME,ONLINE,HEAD,HEAD_BORDER,EMAIL,QQ,SEX,SIGNATURE,FRIEND_LIST,HEAD_BORDER_LIST,REQUESTER_LIST,HEAD_LIST,MESSAGE_LIST,MAIL_BOX);
        schema.createTable("mails", MailAttributes.UUID,MailAttributes.SUBJECT, MailAttributes.CONTENT,MailAttributes.DATE,MailAttributes.ITEMS,MailAttributes.READ, MailAttributes.GOT_ITEMS);
        schema.createTable("messages",UUID, MessageAttributes.DATE,MessageAttributes.CONTENT);
        schema.createTable("fake_players","name",UUID);
        playerTable = instance.schema.getTable("players");
        playerTable.setMainKey(UUID);
        mailTable = instance.schema.getTable("mails");
        mailTable.setMainKey(UUID);
        messageTable = instance.schema.getTable("messages");
        messageTable.setMainKey(UUID);
        fakePlayerTable = instance.schema.getTable("fake_players");
        fakePlayerTable.setMainKey("name");
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

    public Table getFakePlayerTable() {
        return fakePlayerTable;
    }

    public void close(){
        dbManager.close();
    }

    public static void main(String[] args) {
        System.out.println(java.util.UUID.randomUUID());
    }
}
