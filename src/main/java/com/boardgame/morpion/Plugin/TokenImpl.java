package com.boardgame.morpion.plugin;

import fr.le_campus_numerique.square_games.engine.CellPosition;
import fr.le_campus_numerique.square_games.engine.InvalidPositionException;
import fr.le_campus_numerique.square_games.engine.Token;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class TokenImpl implements Token {
    private UUID ownerId;
    private String name;
    private CellPosition position;
    private int boardSize;

    public TokenImpl(UUID ownerId, String name, int boardSize) {
        this.ownerId = ownerId;
        this.name = name;
        this.boardSize = boardSize;
    }

    public Optional<UUID> getOwnerId() {
        return Optional.ofNullable(ownerId);
    }

    public String getName() {
        return name;
    }

    public CellPosition getPosition() {
        return position;
    }

    public Set<CellPosition> getAllowedMoves() {
        Set<CellPosition> allowedMoves = new HashSet<>();
        if (position != null) {
            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            for (int[] direction : directions) {
                int newX = position.x() + direction[0];
                int newY = position.y() + direction[1];
                if (newX >= 0 && newX < boardSize && newY >= 0 && newY < boardSize) {
                    allowedMoves.add(new CellPosition(newX, newY));
                }
            }
        }
        return allowedMoves;
    }

    public void moveTo(@NotNull CellPosition position) throws InvalidPositionException {
        this.position = position;
    }
}