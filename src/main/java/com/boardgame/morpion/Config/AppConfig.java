package com.boardgame.morpion.Config;

import fr.le_campus_numerique.square_games.engine.connectfour.ConnectFourGameFactory;
import fr.le_campus_numerique.square_games.engine.taquin.TaquinGameFactory;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGameFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;




@Configuration
public class AppConfig {

    @Bean
    public TicTacToeGameFactory ticTacToeGameFactory() {
        return new TicTacToeGameFactory();
    }

    @Bean
    public TaquinGameFactory taquinGameFactory() {
        return new TaquinGameFactory() ;
    }

    @Bean
    public ConnectFourGameFactory connectFourGameFactory() {
        return new ConnectFourGameFactory();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}