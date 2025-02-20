package com.boardgame.morpion.Dao;

import fr.le_campus_numerique.square_games.engine.CellPosition;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.GameStatus;
import fr.le_campus_numerique.square_games.engine.Token;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.springframework.jdbc.core.RowMapper;



public class GameRowMapper implements RowMapper<Game> {

    @Override
    public Game mapRow(ResultSet rs, int rowNum) throws SQLException {
        Game game = deserializeGame(rs.getString("data"));
        game.getCurrentPlayerId();
        return game;
    }

    private Game deserializeGame(String data) {
        // Implement your deserialization logic here
        return new Game() {
            @Override
            public @NotNull UUID getId() {
                return null;
            }

            @Override
            public @NotBlank String getFactoryId() {
                return "";
            }

            @Override
            public @NotEmpty Set<UUID> getPlayerIds() {
                return Set.of();
            }

            @Override
            public @NotNull GameStatus getStatus() {
                return null;
            }

            @Override
            public UUID getCurrentPlayerId() {
                return null;
            }

            @Override
            public @Min(2L) int getBoardSize() {
                return 0;
            }

            @Override
            public @NotNull Map<CellPosition, Token> getBoard() {
                return Map.of();
            }

            @Override
            public @NotNull Collection<Token> getRemainingTokens() {
                return List.of();
            }

            @Override
            public @NotNull Collection<Token> getRemovedTokens() {
                return List.of();
            }
        };
    }
}