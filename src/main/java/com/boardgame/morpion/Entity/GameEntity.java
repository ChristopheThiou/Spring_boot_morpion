package com.boardgame.morpion.entity;

import jakarta.persistence.*;
import java.util.List;



@Entity
public class GameEntity {

    @Id
    private String id;

    private int boardSize;

    private String currentPlayerId;

    private String factoryId;

    private String playerIds;

    private String status;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameTokenEntity> tokens;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public String getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(String currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public String getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(String playerIds) {
        this.playerIds = playerIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<GameTokenEntity> getTokens() {
        return tokens;
    }

    public void setTokens(List<GameTokenEntity> tokens) {
        this.tokens = tokens;
    }
}