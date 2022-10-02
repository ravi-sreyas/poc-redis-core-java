package com.ravisreyas.redis.db;

import com.ravisreyas.redis.vo.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBAccessService {

    public static final DBAccessService INSTANCE = new DBAccessService();
    private final Connection conn;

    private DBAccessService() {
        this.conn  =  DBConnectConfig.getInstance().getDBConnection();
    }

    public static DBAccessService getInstance() {
        return INSTANCE;
    }

    public Customer retrieve(String key) {
        try {
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM customer where id = ?")) {
                preparedStatement.setString(1, key);
                ResultSet result = preparedStatement.executeQuery();
                if (result.next()) {
                    return new Customer(result.getObject(1), result.getObject(2), result.getObject(3));
                }
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
