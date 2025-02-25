package com.boardgame.morpion.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class GameTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String ownerId;

    private Boolean removed;

    private Integer x;

    private Integer y;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameEntity game;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_token_id")
    private List<GameTokenEntity> tokens;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Boolean getRemoved() {
        return removed;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public GameEntity getGame() {
        return game;
    }

    public void setGame(GameEntity game) {
        this.game = game;
    }

    public List<GameTokenEntity> getTokens() {
        return tokens;
    }

    public void setTokens(List<GameTokenEntity> tokens) {
        this.tokens = tokens;
    }
}