package com.boardgame.morpion.Dao;

import fr.le_campus_numerique.square_games.engine.Game;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;


public class InMemoryGameDao implements GameDao {

    private final Map<UUID, Game> games = new HashMap<>();

    @Override
    public Stream<Game> findAll() {
        return games.values().stream();
    }

    @Override
    public Optional<Game> findById(String gameId) {
        return Optional.ofNullable(games.get(UUID.fromString(gameId)));
    }

    @Override
    public Game upsert(Game game) {
        games.put(game.getId(), game);
        return game;
    }

    @Override
    public void delete(String gameId) {
        games.remove(UUID.fromString(gameId));
    }
}