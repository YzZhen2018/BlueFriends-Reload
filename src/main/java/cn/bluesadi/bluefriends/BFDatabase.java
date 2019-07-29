package cn.bluesadi.bluefriends;

import cn.bluesadi.bluefriends.database.DBManager;
import cn.bluesadi.bluefriends.database.Schema;
import cn.bluesadi.bluefriends.database.Table;
import cn.bluesadi.bluefriends.mail.Mail;
import cn.bluesadi.bluefriends.mail.MailAttributes;
import cn.bluesadi.bluefriends.player.MessageAttributes;
import cn.bluesadi.bluefriends.util.BFLogger;
import org.bukkit.Bukkit;

import java.net.UnknownHostException;
import java.sql.SQLException;

import static cn.bluesadi.bluefriends.player.PlayerAttributes.*;

public class BFDatabase {

    private static BFDatabase instance;
    private DBManager dbManager;
    private Schema schema;
    private Table playerTable;
    private Table mailTable;
    private Table messageTable;
    private Table fakePlayerTable;

    private BFDatabase() throws SQLException{
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

    public static boolean load(){
        try{
            instance = new BFDatabase();
            return true;
        }catch (SQLException e){
            if(e.getMessage().startsWith("Access denied")){
                BFLogger.error("用于连接数据库的用户名或密码错误,请检查你的配置");
            }else {
                BFLogger.error("在连接数据库时出现了异常", e);
            }
            return false;
        }
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
