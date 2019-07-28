package cn.bluesadi.bluefriends.database;

import cn.bluesadi.bluefriends.database.config.DBLogger;
import cn.bluesadi.bluefriends.database.config.Lang;
import cn.bluesadi.bluefriends.util.item.ItemSerializerUtil;
import cn.bluesadi.bluefriends.util.item.ItemStackUtil;
import cn.bluesadi.bluefriends.util.location.LocationSerializerUtil;
import com.google.gson.Gson;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import static cn.bluesadi.bluefriends.database.config.Lang.*;

public class Row{
    private static final Gson GSON = new Gson();
    private Connection connection;
    private Table connectedTable;
    private String table;
    private String mainKey;
    private String mainValue;

    public Row(Table connectedTable, String mainValue){
        this.connection = connectedTable.getConnection();
        this.connectedTable = connectedTable;
        this.table = connectedTable.getName();
        this.mainKey = connectedTable.getMainKey();
        this.mainValue = mainValue;
    }
    /**
     * 获取某一列的原始字符串
     * @param column 列名
     * @return 原始字符串
     * */
    public String getRawString(String column){
        try(PreparedStatement statement = connection.prepareStatement
                ("SELECT "+ column + " FROM " + table +" WHERE "+ mainKey +"='"+mainValue+"'")){
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString(column);
            }
        }catch (SQLException e){
            DBLogger.error(UNKNOWN_ERROR);
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 判断一个列是否存在
     * @param column 列名称
     * @return true 如果该列存在
     * */
    public boolean existsColumn(String column){
        return connectedTable.existsColumn(column);
    }
    /**
     * 判断该行是否真实存在
     * */
    public boolean existsRow(){
        try {
            String raw = getRawString(mainKey);
            return raw != null && !raw.isEmpty();
        }catch (Exception e){
            return false;
        }
    }
    /**
     * 设置某一列的值
     * @param column 列名
     * @param obj 值
     * @exception IllegalArgumentException 如果该列不存在
     * */
    public void set(String column,Object obj){
        if(existsColumn(column)) {
            String value;
            try{
                if(obj instanceof Collection){
                    Collection<?> collection = (Collection)(obj);
                    if(!collection.isEmpty() && collection.iterator().next() instanceof ItemStack){
                        value = ItemSerializerUtil.toString(ItemStackUtil.toItemStackArray((List<ItemStack>) obj));
                    }else if(!collection.isEmpty() && collection.iterator().next() instanceof Location){
                        value = LocationSerializerUtil.toString((List<Location>) obj);
                    }else{
                        value = GSON.toJson(obj);
                    }
                }else if(obj instanceof Location){
                    value = LocationSerializerUtil.toString((Location)obj);
                }else if(obj instanceof ItemStack){
                    value = ItemSerializerUtil.toString((ItemStack)obj);
                }else{
                    value = GSON.toJson(obj);
                }
                if(!existsRow()){
                    PreparedStatement statement = connection.prepareStatement("INSERT INTO "+table+"( "+mainKey+","+column+" ) VALUES ( '"+mainValue+"' ,'"+value+"' )");
                    statement.execute();
                    statement.close();
                }else{
                    PreparedStatement statement = connection.prepareStatement("UPDATE "+table+" SET "+column+"='"+value+"' WHERE "+mainKey+"='"+mainValue+"'");
                    statement.execute();
                    statement.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }else {
             DBLogger.error(UNKNOWN_ERROR);
        }
    }
    /**
     * 获取某一列的真实值
     * @param column 列名
     * @return 某一列的真实值
     * */
    public Value getValue(String column){
        return Value.fromRawString(getRawString(column));
    }
    /**
     * 获取主键值
     * @return 主键值
     * */
    public String getMainValue() {
        return mainValue;
    }
    /**
     * 删除该行
     * @return true 如果执行成功
     * */
    public boolean delete(){
        try(PreparedStatement statement = connection.prepareStatement
                ("DELETE FROM "+table+" WHERE "+ mainKey +"='"+mainValue+"'")){
            return statement.execute();
        }catch (SQLException e){
            DBLogger.error(UNKNOWN_ERROR);
            e.printStackTrace();
        }
        return false;
    }

}
