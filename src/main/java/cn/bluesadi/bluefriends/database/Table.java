package cn.bluesadi.bluefriends.database;

import cn.bluesadi.bluefriends.database.config.DBLogger;
import cn.bluesadi.bluefriends.database.config.Lang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static cn.bluesadi.bluefriends.database.config.Lang.*;

/**
 * 表示数据库中的一个表
 * @author bluesad
 * */
abstract public class Table {
    private Connection connection;
    private String name;
    private String mainKey;

    protected Table(Connection connection, String name){
        this.connection = connection;
        this.name = name;
        this.mainKey = "id";
    }
    /**
     * 获取表名称
     * @return 表名称
     * */
    public String getName() {
        return name;
    }
    /**
     * 判断一个列是否存在
     * @param column 列名称
     * @return true 如果该列存在
     * */
    abstract public boolean existsColumn(String column);
    /**
     * 在表中创建一个新列
     * @param column 列名称
     * @return true 如果创建成功
     * */
    public boolean addColumn(String column){
        if(!existsColumn(column)) {
            try (PreparedStatement statement = connection.prepareStatement
                    ("alter table " + name + " add column "+column+" TEXT(10000)")) {
                return statement.execute();
            } catch (SQLException e) {
                DBLogger.error(UNKNOWN_ERROR);
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 在表中删除一个列
     * @param column 列名称
     * @return true 如果删除成功
     * */
    public boolean deleteColumn(String column){
        if(existsColumn(column)){
            try (PreparedStatement statement = connection.prepareStatement
                    ("alter table " + name + " drop column "+column)) {
                return statement.execute();
            } catch (SQLException e) {
                DBLogger.error(UNKNOWN_ERROR);
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 获取该表中所有的行
     * @return 该表中所有的行
     * */
    public List<Row> getRows(){
        List<Row> rows = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement
                ("SELECT * FROM " + name)) {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                rows.add(new Row(this,resultSet.getString(mainKey)));
            }
        } catch (SQLException e) {
            DBLogger.error(UNKNOWN_ERROR);
            e.printStackTrace();
        }
        return rows;
    }
    /**
     * 获取该表中所有的行
     * @param condition 接收的条件
     * @return 该表中所有的行
     * */
    public List<Row> getRows(String condition){
        List<Row> rows = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement
                ("SELECT * FROM " + name + " WHERE "+condition)) {
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                rows.add(new Row(this,resultSet.getString(mainKey)));
            }
        } catch (SQLException e) {
            DBLogger.error(UNKNOWN_ERROR);
            e.printStackTrace();
        }
        return rows;
    }
    /**
     * 获取一行
     * @param mainValue 主键值
     * @return 满足 `主键 = 主键值` 的一行
     * */
    public Row getRow(String mainValue){
        /*try (PreparedStatement statement = connection.prepareStatement
                ("SELECT * FROM " + name + " WHERE "+mainKey+"='"+mainValue+"'")) {
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return new Row(this,resultSet.getString(mainKey));
            }
        } catch (SQLException e) {
            DBLogger.error(Lang.ERROR_EXECUTE_STATEMENT);
            e.printStackTrace();
        }
        throw new IllegalArgumentException(Lang.ROW_NOT_FOUND+"WHERE "+mainKey+" = '"+mainValue+"'");
        */
        return new Row(this,mainValue);
    }
    /**
     * 删除某一行
     * */
    public boolean removeRow(String mainValue){
        try (PreparedStatement statement = connection.prepareStatement
                ("DELETE FROM "+name+" WHERE "+mainKey+" = "+mainValue)) {
            return statement.execute();
        } catch (SQLException e) {
            DBLogger.error(UNKNOWN_ERROR);
            e.printStackTrace();
        }
        return false;
    }
    /**
     *      * 设置该表的主键
     * @param mainKey 主键 默认为 id
     * */
    public void setMainKey(String mainKey) {
        this.mainKey = mainKey;
    }
    /**
     * 获取该表的主键
     * @return 主键名
     * */
    public String getMainKey() {
        return mainKey;
    }
    /**
     * 获取数据库连接对象
     * */
    public Connection getConnection() {
        return connection;
    }
}
