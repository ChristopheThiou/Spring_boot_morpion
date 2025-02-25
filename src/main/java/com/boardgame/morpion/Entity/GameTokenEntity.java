package com.boardgame.morpion.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class GameTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name;

    public String ownerId;

    public Boolean removed;

    public Integer x;

    public Integer y;

    @ManyToOne
    @JoinColumn(name = "game_id")
    public GameEntity game;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_token_id")
    public List<GameTokenEntity> tokens;

    // Getters and setters
}