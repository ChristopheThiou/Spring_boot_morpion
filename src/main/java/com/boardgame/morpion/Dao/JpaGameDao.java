package com.boardgame.morpion.Dao;

import com.boardgame.morpion.Entity.GameEntity;
import com.boardgame.morpion.Entity.GameTokenEntity;
import com.boardgame.morpion.Repository.GameEntityRepository;
import fr.le_campus_numerique.square_games.engine.*;
import jakarta.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JpaGameDao implements GameDao {

    @Autowired
    private GameEntityRepository gameEntityRepository;

    @Override
    public Stream<Game> findAll() {
        List<GameEntity> gameEntities = gameEntityRepository.findAll();
        return gameEntities.stream().map(this::convertToGame);
    }

    @Override
    public Optional<Game> findById(String gameId) {
        Optional<GameEntity> gameEntity = gameEntityRepository.findById(gameId);
        return gameEntity.map(this::convertToGame);
    }

    @Override
    public Game upsert(Game game) {
        GameEntity gameEntity = convertToGameEntity(game);
        gameEntityRepository.save(gameEntity);
        return game;
    }

    @Override
    public void delete(String gameId) {
        gameEntityRepository.deleteById(gameId);
    }

    private Game convertToGame(GameEntity gameEntity) {
        return new Game() {

            @Override
            public UUID getId() {
                return UUID.fromString(gameEntity.id);
            }

            @Override
            public String getFactoryId() {
                return gameEntity.factoryId;
            }

            @Override
            public Set<UUID> getPlayerIds() {
                return Arrays.stream(gameEntity.playerIds.split(","))
                             .map(UUID::fromString)
                             .collect(Collectors.toSet());
            }

            @Override
            public int getBoardSize() {
                return gameEntity.boardSize;
            }

            @Override
            public Map<CellPosition, Token> getBoard() {
                return gameEntity.tokens.stream()
                                        .filter(token -> token.x != null && token.y != null)
                                        .collect(Collectors.toMap(
                                            token -> new CellPosition(token.x, token.y),
                                            this::convertToGameToken
                                        ));
            }

            @Override
            public Collection<Token> getRemainingTokens() {
                return gameEntity.tokens.stream()
                                        .filter(token -> token.x == null && token.y == null)
                                        .map(this::convertToGameToken)
                                        .collect(Collectors.toList());
            }

            @Override
            public Collection<Token> getRemovedTokens() {
                return Collections.emptyList(); // Retourne une collection vide si non implémenté
            }

            @Override
            public @NotNull GameStatus getStatus() {
                return GameStatus.valueOf(gameEntity.status);
            }

            @Override
            public UUID getCurrentPlayerId() {
                return gameEntity.currentPlayerId != null ? UUID.fromString(gameEntity.currentPlayerId) : null;
            }

            private Token convertToGameToken(GameTokenEntity gameTokenEntity) {
                return new Token() {

                    @Override
                    public Optional<UUID> getOwnerId() {
                        return Optional.ofNullable(gameTokenEntity.ownerId != null ? UUID.fromString(gameTokenEntity.ownerId) : null);
                    }

                    @Override
                    public String getName() {
                        return gameTokenEntity.name;
                    }

                    @Override
                    public CellPosition getPosition() {
                        return gameTokenEntity.x != null && gameTokenEntity.y != null
                                ? new CellPosition(gameTokenEntity.x, gameTokenEntity.y)
                                : null;
                    }

                    @Override
                    public Set<CellPosition> getAllowedMoves() {
                        Set<CellPosition> allowedMoves = new HashSet<>();
                        int boardSize = gameEntity.boardSize;
                        Map<CellPosition, Token> board = getBoard();

                        CellPosition currentPosition = getPosition();
                        if (currentPosition != null) {
                            int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
                            for (int[] direction : directions) {
                                int newX = currentPosition.x() + direction[0];
                                int newY = currentPosition.y() + direction[1];
                                if (newX >= 0 && newX < boardSize && newY >= 0 && newY < boardSize) {
                                    CellPosition newPosition = new CellPosition(newX, newY);
                                    if (!board.containsKey(newPosition)) {
                                        allowedMoves.add(newPosition);
                                    }
                                }
                            }
                        }
                        return allowedMoves;
                    }

                    @Override
                    public void moveTo(@NotNull CellPosition position) throws InvalidPositionException {
                        // Vérifiez que la position cible est vide avant de déplacer le token
                        Map<CellPosition, Token> board = getBoard();
                        if (board.containsKey(position)) {
                            throw new InvalidPositionException("Position is already occupied");
                        }
                        gameTokenEntity.x = position.x();
                        gameTokenEntity.y = position.y();
                    }
                };
            }
        };
    }

    private GameEntity convertToGameEntity(Game game) {
        GameEntity gameEntity = new GameEntity();
        gameEntity.id = game.getId().toString();
        gameEntity.factoryId = game.getFactoryId();
        gameEntity.boardSize = game.getBoardSize();
        gameEntity.playerIds = game.getPlayerIds().stream()
                                   .map(UUID::toString)
                                   .collect(Collectors.joining(","));
        gameEntity.status = game.getStatus().name();
        gameEntity.currentPlayerId = game.getCurrentPlayerId() != null ? game.getCurrentPlayerId().toString() : null;
        gameEntity.tokens = new ArrayList<>();
        gameEntity.tokens.addAll(game.getBoard().values().stream()
                                .map(token -> convertToGameTokenEntity(token, gameEntity))
                                .collect(Collectors.toList()));
        gameEntity.tokens.addAll(game.getRemainingTokens().stream()
                                .map(token -> convertToGameTokenEntity(token, gameEntity))
                                .collect(Collectors.toList()));
        return gameEntity;
    }

    private GameTokenEntity convertToGameTokenEntity(Token token, GameEntity gameEntity) {
        GameTokenEntity tokenEntity = new GameTokenEntity();
        tokenEntity.ownerId = token.getOwnerId().map(UUID::toString).orElse(null);
        tokenEntity.name = token.getName();
        tokenEntity.game = gameEntity;
        if (token.getPosition() != null) {
            tokenEntity.x = token.getPosition().x();
            tokenEntity.y = token.getPosition().y();
        }
        return tokenEntity;
    }
}