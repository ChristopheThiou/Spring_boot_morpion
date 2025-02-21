package com.boardgame.morpion.Plugin;

import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.InconsistentGameDefinitionException;
import fr.le_campus_numerique.square_games.engine.TokenPosition;
import fr.le_campus_numerique.square_games.engine.connectfour.ConnectFourGameFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.OptionalInt;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;




@Component
public class ConnectFourPlugin implements GamePlugin {

    @Autowired
    private ConnectFourGameFactory connectFourGameFactory;

    @Value("${game.connectfour.default-player-count}")
    private int defaultPlayerCount;

    @Autowired
    private MessageSource messageSource;

    @Override
    public String getName(Locale locale) {
        return messageSource.getMessage("game.connectfour.name", null, locale);
    }

    @Override
public Game createGame(OptionalInt playerCount, OptionalInt boardSize, OptionalInt boardTokens, OptionalInt removedTokens, UUID userId, UUID opponentIds) {
    int players = playerCount.orElse(defaultPlayerCount);
    int size = boardSize.orElse(3); // Taille par défaut

    // Créez une liste de UUID pour les joueurs en utilisant userId et opponentIds
    List<UUID> playerIds = new ArrayList<>();
    playerIds.add(userId);
    playerIds.add(opponentIds);

    // Créez des collections vides pour les jetons de plateau et les jetons retirés
    Collection<TokenPosition<UUID>> boardTokensCollection = new ArrayList<>();
    Collection<TokenPosition<UUID>> removedTokensCollection = new ArrayList<>();

    try {
        return connectFourGameFactory.createGameWithIds(null, size, playerIds, boardTokensCollection, removedTokensCollection);
    } catch (InconsistentGameDefinitionException e) {
        throw new RuntimeException("Failed to create game", e);
    }
}
}