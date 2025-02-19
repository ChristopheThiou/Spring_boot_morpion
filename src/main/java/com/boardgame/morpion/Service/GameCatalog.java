package com.boardgame.morpion.Service;

import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGame;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public interface GameCatalog {
    Collection<String> getGameIdentifier();
    TicTacToeGame createGame(int playerCount, int boardSize, Set<UUID> playerIds);
    List<TicTacToeGame> getGamesForPlayer(UUID playerId);
    void playMove(UUID gameId, UUID playerId, int x, int y);
}