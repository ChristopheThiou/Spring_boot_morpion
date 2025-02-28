package com.boardgame.morpion.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import fr.le_campus_numerique.square_games.engine.Game;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class JdbcGameDao implements GameDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private final ObjectMapper objectMapper;

    @Autowired
    public JdbcGameDao() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new Jdk8Module());
    }

    @Override
    public Stream<Game> findAll() {
        return Stream.empty();
    }

    @Override
    public Optional<Game> findById(String gameId) {
        String sql = "SELECT * FROM games WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", UUID.fromString(gameId));

        Game game = jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
            String gameData = rs.getString("data");
            try {
                return objectMapper.readValue(gameData, Game.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erreur lors de la conversion du JSON en jeu", e);
            }
        });

        return Optional.ofNullable(game);
    }

    @Override
    public Game upsert(Game game) {
        String sql = "INSERT INTO games (id, data) VALUES (:id, :data) " +
                     "ON CONFLICT (id) DO UPDATE SET data = :data";
        Map<String, Object> params = new HashMap<>();
        params.put("id", game.getId());

        try {
            String gameData = objectMapper.writeValueAsString(game);
            params.put("data", gameData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors de la conversion du jeu en JSON", e);
        }

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