package com.boardgame.morpion.Plugin;

import com.boardgame.morpion.Service.TokenImpl;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.Token;
import fr.le_campus_numerique.square_games.engine.taquin.TaquinGameFactory;
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
    public Game createGame(OptionalInt playerCount, OptionalInt boardSize, OptionalInt winCondition,
                           OptionalInt maxTurns, UUID... playerIds) {
        int players = playerCount.orElse(defaultPlayerCount);
        int size = boardSize.orElse(3); // Taille par défaut

        // Créez une liste de UUID pour les joueurs en utilisant userId et opponentIds
        List<UUID> playerIdsList = new ArrayList<>();
        for (UUID playerId : playerIds) {
            playerIdsList.add(playerId);
        }

        // Ajout de journaux pour le débogage
        System.out.println("Creating game with the following parameters:");
        System.out.println("Player count: " + players);
        System.out.println("Board size: " + size);
        System.out.println("Player IDs: " + playerIdsList);

        return taquinGameFactory.createGame(4, Set.of(playerIds));
    }

    @Override
    public List<Token> createRemainingTokens(Set<UUID> playerIds, int boardSize) {
        List<Token> remainingTokens = new ArrayList<>();
        for (UUID playerId : playerIds) {
            for (int i = 0; i < boardSize * boardSize - 1; i++) { // Exemple : (boardSize * boardSize - 1) jetons pour Taquin
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