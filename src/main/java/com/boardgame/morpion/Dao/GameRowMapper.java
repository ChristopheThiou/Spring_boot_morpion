package com.boardgame.morpion.Dao;

import fr.le_campus_numerique.square_games.engine.Game;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;



public class GameRowMapper implements RowMapper<Game> {

    @Override
    public Game mapRow(ResultSet rs, int rowNum) throws SQLException {
        Game game = deserializeGame(rs.getString("data"));
        game.getCurrentPlayerId();
        return game;
    }

    private Game deserializeGame(String data) {
        return null;
    }
}