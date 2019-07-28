package cn.bluesadi.bluefriends.database;

import cn.bluesadi.bluefriends.BlueFriends;
import cn.bluesadi.bluefriends.config.Config;
import cn.bluesadi.bluefriends.database.config.DBLogger;
import cn.bluesadi.bluefriends.database.config.Lang;
import cn.bluesadi.bluefriends.database.mysql.MySQLTable;
import cn.bluesadi.bluefriends.database.sqlite.SQLiteTable;
import org.bukkit.Bukkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static cn.bluesadi.bluefriends.database.config.Lang.*;

/**
 * 数据库对象
 * @author bluesad
 * */
public class Schema {
    private Connection connection;
    private static final long WAIT_TIMEOUT = 4 * 3600 * 20;

    private Schema(Connection connection){
        this.connection = connection;
        Bukkit.getScheduler().runTaskTimer(BlueFriends.getInstance(),()->{
            try {
                if (!connection.isClosed()) {
                    existsTable("t233");
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        },WAIT_TIMEOUT,WAIT_TIMEOUT);
    }
    /**
     * 连接至数据库
     * @param connection 数据库连接对象
     * @return 数据库对象
     * */
    public static Schema connect(Connection connection){
        return new Schema(connection);
    }
    /**
     * 获取表
     * @param table 表名称
     * @return 表
     * @exception IllegalArgumentException 如果该表不存在
     * */
    public Table getTable(String table){
        if(existsTable(table)) {
            if(Config.DATABASE_TYPE.equalsIgnoreCase("MYSQL")) {
                return MySQLTable.connect(connection, table);
            }else{
                return SQLiteTable.connect(connection,table);
            }
        }else{
            throw new IllegalArgumentException(UNKNOWN_ERROR);
        }
    }
    /**
     * 创建表
     * @param table 表名
     * @param values 表中的列
     * @return true 如果创建成功
     * @exception IllegalArgumentException 如果values.length<=0
     * */
    public boolean createTable(String table,String... values){
        if(values.length <= 0){
            throw new IllegalArgumentException(UNKNOWN_ERROR);
        }
        try{
            if(existsTable(table)){
                Table t = getTable(table);
                for(String value : values){
                    t.addColumn(value);
                }
                return false;
            }else{
                StringBuilder sqlBuilder = new StringBuilder();
                sqlBuilder.append("CREATE TABLE ")
                        .append(table)
                        .append("(")
                        .append("id INT UNSIGNED AUTO_INCREMENT,");
                for(String value: values){
                    sqlBuilder.append("`");
                    sqlBuilder.append(value);
                    sqlBuilder.append("`");
                    sqlBuilder.append(" TEXT(10000)");
                    if(!value.equals(values[values.length-1])){
                        sqlBuilder.append(",");
                    }else{
                        sqlBuilder.append(", PRIMARY KEY ( ");
                        sqlBuilder.append("id");
                        sqlBuilder.append(" ))");
                    }
                }
                PreparedStatement statement = connection.prepareStatement(sqlBuilder.toString());
                boolean s = statement.execute();
                statement.close();
                return s;
            }
        }catch (SQLException e){
            DBLogger.error(UNKNOWN_ERROR);
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 判断是否存在一个表
     * @param table 表名称
     * @return true 如果存在这个表
     * */
    public boolean existsTable(String table){
        try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM "+table);){
            return statement.execute();
        }catch (SQLException e){
            return false;
        }
    }
    /**
     * 获取数据库连接对象
     * */
    public Connection getConnection() {
        return connection;
    }
    /**
     * 关闭数据库连接
     * */
    public void close(){
        try {
            connection.close();
        }catch (SQLException e){
            DBLogger.error(UNKNOWN_ERROR);
            e.printStackTrace();
        }
    }

}
