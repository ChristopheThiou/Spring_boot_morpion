package com.boardgame.morpion.Plugin;

import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.Token;
import java.util.List;
import java.util.Locale;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;

public interface GamePlugin {
    String getName(Locale locale);
    Game createGame(OptionalInt playerCount, OptionalInt boardSize, OptionalInt winCondition, OptionalInt maxTurns, UUID... playerIds);

    List<Token> createRemainingTokens(Set<UUID> playerIds, int boardSize);
}