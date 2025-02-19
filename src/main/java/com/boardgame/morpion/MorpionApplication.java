package com.boardgame.morpion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.boardgame.morpion"})

public class MorpionApplication {

    public static void main(String[] args) {
        SpringApplication.run(MorpionApplication.class, args);
    }

}
