package cn.bluesadi.bluefriends.database;


import cn.bluesadi.bluefriends.BlueFriends;
import cn.bluesadi.bluefriends.config.Config;

/**
 * @author bluesad
 * */
public class DBManager {

    private Schema schema;
    private String schemaName;

    public DBManager(String schema){
        String type = Config.DATABASE_TYPE;
        boolean mysql = type.equalsIgnoreCase("MYSQL");
        String url = "";
        String user = Config.DATABASE_USER;
        String password = Config.DATABASE_PASSWORD;
        if(mysql){
            url = "jdbc:mysql://"+ Config.DATABASE_URL;
            DataBase dataBase = DataBase.connectMySQL(url,user,password);
            this.schemaName = schema;
            this.schema = dataBase.getSchema(schema);
        }else {
            url = "jdbc:sqlite://"+ BlueFriends.getInstance().getDataFolder().getAbsolutePath()+"/data.db";
            DataBase dataBase = DataBase.connectSQLite(url);
            this.schema = dataBase.getSchema(schema);
        }
    }

    public void close(){
        schema.close();
    }

    public Schema getSchema() {
        return schema;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof DBManager)){
            return false;
        }else{
            return (((DBManager)obj).schemaName.equals(schemaName));
        }
    }
}
