package com.boardgame.morpion.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

@Entity
public class GameEntity {
    @Id
    public String id;

    public @NotNull String factoryId;
    public @Positive int boardSize;
    public @NotNull String playerIds;
    public @NotNull String status;
    public String currentPlayerId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<GameTokenEntity> tokens;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<GameTokenEntity> remainingTokens;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<GameTokenEntity> removedTokens;
}