package com.boardgame.morpion.Plugin;

import com.boardgame.morpion.Service.TokenImpl;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.Token;
import fr.le_campus_numerique.square_games.engine.connectfour.ConnectFourGameFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.OptionalInt;
import java.util.Set;
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
    public Game createGame(OptionalInt playerCount, OptionalInt boardSize, OptionalInt winCondition,
                           OptionalInt maxTurns, UUID... playerIds) {
        int players = playerCount.orElse(defaultPlayerCount);

        // Créez une liste de UUID pour les joueurs en utilisant userId et opponentIds
        List<UUID> playerIdsList = new ArrayList<>();
        for (UUID playerId : playerIds) {
            playerIdsList.add(playerId);
        }

        // Ajout de journaux pour le débogage
        System.out.println("Creating game with the following parameters:");
        System.out.println("Player count: " + players);

        return connectFourGameFactory.createGame(7, Set.of(playerIds));
    }

    @Override
    public List<Token> createRemainingTokens(Set<UUID> playerIds, int boardSize) {
        List<Token> remainingTokens = new ArrayList<>();
        for (UUID playerId : playerIds) {
            for (int i = 0; i < boardSize * boardSize; i++) { // Exemple : (boardSize * boardSize) jetons pour Connect Four
                remainingTokens.add(new TokenImpl(playerId, "Token " + i, boardSize));
            }
        }

        // Ajout de journaux pour le débogage
        System.out.println("Created remaining tokens:");
        for (Token token : remainingTokens) {
            System.out.println("Token: " + token.getName() + ", Owner: " + token.getOwnerId().orElse(null));
        }

        return remainingTokens;
    }
}