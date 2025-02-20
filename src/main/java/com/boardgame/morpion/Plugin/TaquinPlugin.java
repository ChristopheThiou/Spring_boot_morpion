package com.boardgame.morpion.Plugin;

import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.taquin.TaquinGameFactory;
import java.util.Locale;
import java.util.OptionalInt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;


@Component
public class TaquinPlugin implements GamePlugin {

    @Autowired
    private TaquinGameFactory taquinGameFactory;

    @Value("${game.taquin.default-player-count}")
    private int defaultPlayerCount;

    @Autowired
    private MessageSource messageSource;

    @Override
    public String getName(Locale locale) {
        return messageSource.getMessage("game.taquin.name", null, locale);
    }

    @Override
    public Game createGame(OptionalInt playerCount, OptionalInt boardSize) {
        int players = playerCount.orElse(defaultPlayerCount);
        int size = boardSize.orElse(3);
        return taquinGameFactory.createGame(players, size);
    }
}