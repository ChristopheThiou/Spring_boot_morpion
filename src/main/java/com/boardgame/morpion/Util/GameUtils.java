package com.boardgame.morpion.util;

import fr.le_campus_numerique.square_games.engine.CellPosition;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.GameStatus;
import fr.le_campus_numerique.square_games.engine.Token;

import java.util.*;



public class GameUtils {

    public static Game createUpdatedGame(Game game, UUID newCurrentPlayerId) {
        return new Game() {

            public UUID getId() {
                return game.getId();
            }

            public String getFactoryId() {
                return game.getFactoryId();
            }

            public Set<UUID> getPlayerIds() {
                return game.getPlayerIds();
            }

            public GameStatus getStatus() {
                return game.getStatus();
            }

            public UUID getCurrentPlayerId() {
                return newCurrentPlayerId;
            }

            public int getBoardSize() {
                return game.getBoardSize();
            }

            public Map<CellPosition, Token> getBoard() {
                return game.getBoard();
            }

            public Collection<Token> getRemainingTokens() {
                return game.getRemainingTokens();
            }

            public Collection<Token> getRemovedTokens() {
                return game.getRemovedTokens();
            }
        };
    }
}