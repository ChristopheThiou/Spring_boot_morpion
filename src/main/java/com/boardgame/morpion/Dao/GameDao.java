package com.boardgame.morpion.Dao;

import fr.le_campus_numerique.square_games.engine.Game;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.stream.Stream;


public interface GameDao {
    @NotNull Stream<Game> findAll();
    Optional<Game> findById(@NotNull String gameId);
    @NotNull Game upsert(@NotNull Game game);
    void delete(@NotNull String gameId);
    }
