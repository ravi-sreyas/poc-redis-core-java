package com.ravisreyas.redis.cache;

import com.ravisreyas.redis.vo.Customer;
import org.redisson.Redisson;
import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.MapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.MapLoader;
import org.redisson.api.map.MapWriter;

import static com.ravisreyas.redis.cache.RedisConstants.*;

public class RedisConfig {

    private final RedissonClient redisson = Redisson.create();
    private final RLocalCachedMap<String, Customer> reader;
    private final RLocalCachedMap<String, Customer> writer;

    public RedisConfig() {
        this.reader = initializeReader();
        this.writer = initializeWriter();
    }

    public RLocalCachedMap<String, Customer> getReader() {
        return reader;
    }

    public RLocalCachedMap<String, Customer> getWriter() {
        return writer;
    }

    private RLocalCachedMap<String, Customer> initializeReader() {
        MapLoader<String, Customer> mapLoader = new RedisReadConfig().createMapReader();
        LocalCachedMapOptions<String, Customer> options = LocalCachedMapOptions.<String, Customer>defaults()
                .loader(mapLoader);

        return redisson.getLocalCachedMap(KEY, options);
    }

    private RLocalCachedMap<String, Customer> initializeWriter() {
        MapWriter<String, Customer> mapWriter = new RedisWriteConfig().createMapWriter();
        LocalCachedMapOptions<String, Customer> options = LocalCachedMapOptions.<String, Customer>defaults()
                .writer(mapWriter)
                .writeMode(MapOptions.WriteMode.WRITE_BEHIND)
                .writeBehindDelay(WRITE_BEHIND_DELAY)
                .writeBehindBatchSize(WRITE_BEHIND_BATCH_SIZE);

        return redisson.getLocalCachedMap(KEY, options);
    }
}
