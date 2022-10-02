package com.ravisreyas.redis.cache;

import com.ravisreyas.redis.db.DBConnectConfig;
import com.ravisreyas.redis.vo.Customer;
import org.redisson.api.map.MapWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.Map;

public class RedisWriteConfig {

    public MapWriter<String, Customer> createMapWriter() {
        final Connection conn =  DBConnectConfig.getInstance().getDBConnection();
        return new MapWriter<String, Customer>() {

            @Override
            public void write(Map<String, Customer> map) {
                try {
                    try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO customer (id, name, age) values (?, ?, ?)")) {
                        for (Map.Entry<String, Customer> entry : map.entrySet()) {
                            preparedStatement.setString(1, entry.getKey());
                            preparedStatement.setString(2, entry.getValue().getName());
                            preparedStatement.setInt(3, entry.getValue().getAge());
                            preparedStatement.addBatch();
                        }
                        preparedStatement.executeBatch();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void delete(Collection<String> keys) {
                try {
                    try (PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM customer where id = ?")) {
                        for (String key : keys) {
                            preparedStatement.setString(1, key);
                            preparedStatement.addBatch();
                        }
                        preparedStatement.executeBatch();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }
}
