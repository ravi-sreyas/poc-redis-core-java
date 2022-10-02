# poc-redis-core-java
POC on integration of redis with core java and setting the cache to follow write-behind strategy

-------------------------------
## Software & Tools used
* Java 8
* SQLite3
* Redis
* Maven
  * Dependencies:
    * redisson : Redis client
    * sqlite-jdbc : SQLite client

-------------------------------
## Usage
### Build it
```
cd poc-redis
mvn clean install -DskipTests
```

### Run it
Can run from editor. If using command line:
```
mvn exec:java -Dexec.mainClass=com.ravisreyas.redis.App
```

### Available options
Following options are supported by this app:
```
Available options:
--help                                                                 Show supported options
--insert <customer name> <customer age>           Add new elements to cache & eventually persist to DB
--showall                                                              Print all cached items
--show <key>                                                     Print cached item with mentioned key
--db <key>                                                      Print item from DB
--exit                                                             Terminate the application
```

-------------------------------
## Redis configuration
1. Redis configuration can be located at `RedisConfig.java` where the writer is configured as write-behind configuration
2. Values of the delay and batch size can be found at `RedisConstants.java`
   * Initial values:
     * Write behind delay: 30 seconds
     * Write behind batch size: 100