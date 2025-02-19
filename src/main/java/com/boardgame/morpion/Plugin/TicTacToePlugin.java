package com.boardgame.morpion.Plugin;

import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGame;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGameFactory;
import java.util.Locale;
import java.util.OptionalInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;


@Component
public class TicTacToePlugin implements GamePlugin {

    @Autowired
    private TicTacToeGameFactory ticTacToeGameFactory;

    @Value("${game.tictactoe.default-player-count}")
    private int defaultPlayerCount;

    @Autowired
    private MessageSource messageSource;

    @Override
    public String getName(Locale locale) {
        return messageSource.getMessage("game.tictactoe.name", null, locale);
    }

    @Override
    public TicTacToeGame createGame(OptionalInt playerCount, OptionalInt boardSize) {
        int players = playerCount.orElse(defaultPlayerCount);
        int size = boardSize.orElse(3);
        return ticTacToeGameFactory.createGame(players, size);
    }
}