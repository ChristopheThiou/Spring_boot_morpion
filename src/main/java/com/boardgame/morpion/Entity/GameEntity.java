package com.boardgame.morpion.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class GameEntity {

    @Id
    public String id;

    public int boardSize;

    public String currentPlayerId;

    public String factoryId;

    public String playerIds;

    public String status;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<GameTokenEntity> tokens;

    // Getters and setters
}