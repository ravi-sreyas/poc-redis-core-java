package com.ravisreyas.redis.processor;

import com.ravisreyas.redis.cache.RedisConfig;
import com.ravisreyas.redis.db.DBAccessService;
import com.ravisreyas.redis.vo.Customer;
import org.redisson.api.RLocalCachedMap;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

import static com.ravisreyas.redis.Constants.*;
import static com.ravisreyas.redis.Constants.HELP;
import static com.ravisreyas.redis.cache.RedisConstants.WRITE_BEHIND_DELAY;
import static jodd.util.StringUtil.isEmpty;

public class InputProcessor {
    private Scanner scanner;
    private Random r;
    private RLocalCachedMap<String, Customer>  reader;
    private RLocalCachedMap<String, Customer>  writer;

    public void processInput() {
        initialize();
        try {
            while (true) {
                if (process()) System.exit(0);
            }
        } catch (IllegalStateException | NoSuchElementException e) {
                System.out.println("System.in was closed; exiting");
        }
    }

    private boolean process() {
        String input = this.scanner.nextLine();
        if (isEmpty(input) || EXIT.equals(input)) {
            System.out.println("Bye! :)");
            return true;
        }
        String[] options = input.toLowerCase().split(" ");
        switch (options[0]) {
            case INSERT:
                insert(options[1], options[2]);
                break;
            case GET_ALL:
                showAll();
                break;
            case GET_KEY:
                showKey(options[1]);
                break;
            case DB:
                showDb(options[1]);
                break;
            case EXIT:
                System.out.println("Bye! :)");
                return true;
            case HELP:
            default:
                printHelp();
        }
        return false;
    }

    private void initialize() {
        this.scanner = new Scanner(System.in);
        RedisConfig config = new RedisConfig();
        this.r = new Random();
        this.reader = config.getReader();
        this.writer = config.getWriter();
    }

    private void printHelp() {
        System.out.println("Available options:");
        System.out.printf("%s                                                                 Show supported options\n", HELP);
        System.out.printf("%s <customer name> <customer age>           Add new elements to cache & eventually persist to DB\n", INSERT);
        System.out.printf("%s                                                              Print all cached items\n", GET_ALL);
        System.out.printf("%s <key>                                                     Print cached item with mentioned key\n", GET_KEY);
        System.out.printf("%s <key>                                                      Print item from DB\n", DB);
        System.out.printf("%s                                                             Terminate the application\n", EXIT);
    }

    private void insert(String name, String age) {
        int ageTries = 0;
        while (ageTries < 5) {
            try {
                Integer.parseInt(age);
                break;
            } catch (NumberFormatException ex) {
                System.out.println("Entered age is not an integer. Please enter correct age..");
                ageTries++;
                age = this.scanner.nextLine();
            }
        }
        if (ageTries == 5) {
            System.out.println("Age is invalid. Please retry...");
            return;
        }
        Integer key = generateKey();
        this.writer.put(String.valueOf(key), new Customer(key, name, Integer.parseInt(age)));
        System.out.println("Successfully added customer to cache with key as " + key);
    }

    private void showAll() {
        System.out.printf("Totally %d entries are currently cached:\n", this.reader.entrySet().size());
        for (Map.Entry<String, Customer> entry : this.reader.entrySet()) {
            System.out.printf("%s : %s\n", entry.getKey() , entry.getValue());
        }
    }

    private void showKey(String key) {
        Customer value = this.reader.get(key);
        if (value == null)
            System.out.printf("%s key is not present in cache\n", key);
        else
            System.out.printf("%s : %s\n", key, value);
    }

    private void showDb(String key) {
        Customer value = DBAccessService.getInstance().retrieve(key);
        if (value == null)
            System.out.printf("%s key is not present in DB. Check if key is invalid (or) wait upto %d seconds for cache to write-behind and update DB\n", key, WRITE_BEHIND_DELAY/1000);
        else
            System.out.printf("%s : %s\n", key, value);
    }

    private int generateKey() {
        return 10000 + this.r.nextInt(20000);
    }
}
