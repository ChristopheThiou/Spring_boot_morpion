package com.boardgame.morpion.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;



@Entity
public class GameEntity {
    @Id
    public String id;
    public String factoryId;
    public int boardSize;
    public String playerIds;
    public String status;
    public String currentPlayerId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<GameTokenEntity> tokens;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<GameTokenEntity> remainingTokens;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<GameTokenEntity> removedTokens;
}