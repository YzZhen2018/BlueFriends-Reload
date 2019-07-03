package cn.bluesadi.bluefriends.database.sqlite;

import cn.bluesadi.bluefriends.database.Table;
import cn.bluesadi.bluefriends.database.config.DBLogger;
import cn.bluesadi.bluefriends.database.config.Lang;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLiteTable extends Table {
    private Connection connection;
    private String name;
    private String mainKey;

    private SQLiteTable(Connection connection, String name){
        super(connection,name);
        this.connection = connection;
        this.name = name;
        this.mainKey = "id";
    }
    /**
     * 连接至表
     * @param connection 数据库连接对象
     * @return 表
     * */
    public static SQLiteTable connect(Connection connection, String name){
        return new SQLiteTable(connection,name);
    }
    /**
     * 判断一个列是否存在
     * @param column 列名称
     * @return true 如果该列存在
     * */
    public boolean existsColumn(String column){
        try(PreparedStatement statement = connection.prepareStatement
                ("PRAGMA table_info(" + name + ")")) {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                if(resultSet.getString("name").equals(column)) {
                    return true;
                }
            }
        }catch (SQLException e){
            DBLogger.error(Lang.ERROR_EXECUTE_STATEMENT);
            e.printStackTrace();
        }
        return false;
    }
}
