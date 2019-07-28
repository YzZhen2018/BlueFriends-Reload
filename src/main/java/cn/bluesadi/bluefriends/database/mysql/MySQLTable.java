package cn.bluesadi.bluefriends.database.mysql;

import cn.bluesadi.bluefriends.database.Table;
import cn.bluesadi.bluefriends.database.config.DBLogger;
import cn.bluesadi.bluefriends.database.config.Lang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static cn.bluesadi.bluefriends.database.config.Lang.*;

public class MySQLTable extends Table {
    private Connection connection;
    private String name;
    private String mainKey;

    private MySQLTable(Connection connection, String name){
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
    public static MySQLTable connect(Connection connection, String name){
        return new MySQLTable(connection,name);
    }
    /**
     * 判断一个列是否存在
     * @param column 列名称
     * @return true 如果该列存在
     * */
    public boolean existsColumn(String column){
        try(PreparedStatement statement = connection.prepareStatement
                ("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '"+name+"'")) {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                if(column.equalsIgnoreCase(resultSet.getString(1))){
                    return true;
                }
            }
        }catch (SQLException e){
            DBLogger.error(UNKNOWN_ERROR);
            e.printStackTrace();
        }
        return false;
    }
}
