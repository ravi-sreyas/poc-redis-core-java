package com.ravisreyas.redis;

import com.ravisreyas.redis.processor.InputProcessor;

import static com.ravisreyas.redis.Constants.HELP;

/**
 * @author Ravi Sreyas S
 */
public class App {
    public static void main( String[] args ) {
        System.out.printf("Welcome! Type '%s' for more info\n", HELP);
        InputProcessor inputProcessor = new InputProcessor();
        inputProcessor.processInput();
    }
}
