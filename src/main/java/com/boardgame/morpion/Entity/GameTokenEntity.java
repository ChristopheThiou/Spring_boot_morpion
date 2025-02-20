package com.boardgame.morpion.Entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class GameTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public @NotNull String ownerId;
    public @NotNull String name;
    public boolean removed;
    public @Nullable Integer x;
    public @Nullable Integer y;
}