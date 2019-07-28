package cn.bluesadi.bluefriends.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import static cn.bluesadi.bluefriends.database.config.Lang.*;

public class Column {
    private Connection connection;
    private String table;
    private String column;

    public Column(Table connectedTable, String column){
        this.connection = connectedTable.getConnection();
        this.table = connectedTable.getName();
        this.column = column;
    }
    /**
     * 遍历该列中的所有值
     * @param consumer 函数
     * */
    public void forEach(Consumer<Value> consumer) {
        try (PreparedStatement statement = connection.prepareStatement
                ("SELECT " + column + " FROM " + table)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                consumer.accept(Value.fromRawString(resultSet.getString(1)));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(UNKNOWN_ERROR + column);
        }
        throw new IllegalArgumentException(UNKNOWN_ERROR + column);
    }
}
