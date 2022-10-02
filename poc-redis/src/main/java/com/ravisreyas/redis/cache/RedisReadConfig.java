package com.ravisreyas.redis.cache;

import com.ravisreyas.redis.db.DBAccessService;
import com.ravisreyas.redis.db.DBConnectConfig;
import com.ravisreyas.redis.vo.Customer;
import org.redisson.api.map.MapLoader;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RedisReadConfig {

    public MapLoader<String, Customer> createMapReader() {
        final Connection conn =  DBConnectConfig.getInstance().getDBConnection();
        return new MapLoader<String, Customer>() {
            @Override
            public Iterable<String> loadAllKeys() {
                List<String> list = new ArrayList<>();
                try {
                    try (Statement statement = conn.createStatement()) {
                        ResultSet result = statement.executeQuery("SELECT id FROM customer");
                        while (result.next()) {
                            list.add(result.getString(1));
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return list;
            }

            @Override
            public Customer load(String key) {
                return DBAccessService.getInstance().retrieve(key);
            }
        };
    }
}
