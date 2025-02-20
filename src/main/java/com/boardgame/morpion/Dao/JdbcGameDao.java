package com.boardgame.morpion.Dao;

import fr.le_campus_numerique.square_games.engine.Game;
import java.util.*;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class JdbcGameDao implements GameDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Stream<Game> findAll() {
        String sql = "SELECT * FROM games";
        List<Game> games = jdbcTemplate.query(sql, new GameRowMapper());
        return games.stream();
    }

    @Override
    public Optional<Game> findById(String gameId) {
        String sql = "SELECT * FROM games WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", UUID.fromString(gameId));
        Game game = jdbcTemplate.queryForObject(sql, params, new GameRowMapper());
        return Optional.ofNullable(game);
    }

    @Override
    public Game upsert(Game game) {
        String sql = "INSERT INTO games (id, data) VALUES (:id, :data) " +
                     "ON CONFLICT (id) DO UPDATE SET data = :data";
        Map<String, Object> params = new HashMap<>();
        params.put("id", game.getId());
        params.put("data", game);
        jdbcTemplate.update(sql, params);
        return game;
    }

    @Override
    public void delete(String gameId) {
        String sql = "DELETE FROM games WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", UUID.fromString(gameId));
        jdbcTemplate.update(sql, params);
    }
}