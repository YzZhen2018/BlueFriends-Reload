package cn.bluesadi.bluefriends.database;

import cn.bluesadi.bluefriends.database.config.DBLogger;
import cn.bluesadi.bluefriends.database.config.Lang;

import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static cn.bluesadi.bluefriends.database.config.Lang.*;

/**
 * 数据库连接对象
 * @author bluesad
 * */
public class DataBase{
    private String url;
    private String user;
    private String password;
    private DataBaseType dataBaseType;

    private DataBase(String url, String user, String password){
        this.url = url;
        this.user = user;
        this.password = password;
    }
    private DataBase(String url){
        this.url = url;
    }
    /**
     * 获取MySQL数据库连接对象
     * @param url 数据库URL地址
     * @param user 数据库用户名
     * @param password 数据库密码
     * */
    public static DataBase connectMySQL(String url, String user, String password){
        DataBase dataBase = new DataBase(url,user,password);
        dataBase.dataBaseType = DataBaseType.MYSQL;
        dataBase.dataBaseType.enable();
        return dataBase;
    }
    /**
     * 获取SQLite数据库连接对象
     * @param url 数据库URL地址
     * */
    public static DataBase connectSQLite(String url){
        DataBase dataBase = new DataBase(url);
        dataBase.dataBaseType = DataBaseType.SQLITE;
        dataBase.dataBaseType.enable();
        return dataBase;
    }
    /**
     * 数据库类型
     * */
    private enum DataBaseType{
        MYSQL(com.mysql.jdbc.Driver.class,"MySQL"),
        SQLITE(org.sqlite.JDBC.class,"SQLite");

        private Class<?> driverClass;
        private String name;

        DataBaseType(Class<?> driverClass,String name){
            this.driverClass = driverClass;
            this.name = name;
        }
        /**
         * 获取该数据库的驱动类
         * */
        public Class<?> getDriverClass() {
            return driverClass;
        }
        /**
         * 获取该数据库类型的名称
         * */
        public String getName() {
            return name;
        }

        /**
         * 加载驱动类
         * */
        public void enable(){
            try {
                Class.forName(driverClass.getName());
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }
    /**
     * 获取一个具体的数据库对象
     * 如果该数据库不存在将会自动创建
     * @param schema 数据库名称,如果是SQLite随便输一个值都可以
     * @return 数据库对象
     * */
    public Schema getSchema(String schema) throws SQLException {
        if(dataBaseType.equals(DataBaseType.MYSQL)) {
            Connection connection = DriverManager.getConnection(url,user,password);
            createSchema(connection,schema);
            connection.setCatalog(schema);
            return Schema.connect(connection);
        }else if(dataBaseType.equals(DataBaseType.SQLITE)){
            Connection connection = DriverManager.getConnection(url);
            return Schema.connect(connection);
        }
        return null;
    }
    /**
     * 创建一个数据库
     * 如果该数据库已经存在将不会有任何反应
     * @param schema 数据库名称
     * @return true 如果创建数据库成功
     * 也就是说该名称的数据库原本不存在,而创建之后存在
     * @exception IllegalArgumentException 如果该种数据库不支持创建Schema
     * */
    private boolean createSchema(Connection connection,String schema){
        if(dataBaseType.equals(DataBaseType.MYSQL)) {
            try {
                PreparedStatement statement = connection.prepareStatement("CREATE DATABASE " + schema);
                boolean s = statement.execute();
                statement.close();
                return s;
            } catch (SQLException e) {
                return false;
            }
        }else {
            throw new IllegalArgumentException(UNKNOWN_ERROR);
        }
    }
}
