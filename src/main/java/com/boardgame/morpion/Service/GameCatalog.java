package com.boardgame.morpion.service;

import fr.le_campus_numerique.square_games.engine.Game;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;



public interface GameCatalog {
    Collection<String> getGameIdentifier();
    Game createGame(String gameId, int playerCount, int boardSize, Set<UUID> playerIds);
    List<Game> getGamesForPlayer(UUID playerId);
    void playMove(UUID gameId, UUID playerId, int x, int y);
}