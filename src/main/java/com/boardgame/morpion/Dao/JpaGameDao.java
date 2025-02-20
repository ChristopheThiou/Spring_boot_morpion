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
            public GameStatus getStatus() {
                return GameStatus.valueOf(gameEntity.status);
            }

            @Override
            public UUID getCurrentPlayerId() {
                return gameEntity.currentPlayerId != null ? UUID.fromString(gameEntity.currentPlayerId) : null;
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
                return gameEntity.remainingTokens.stream()
                                                 .map(this::convertToGameToken)
                                                 .collect(Collectors.toList());
            }

            @Override
            public Collection<Token> getRemovedTokens() {
                return gameEntity.removedTokens.stream()
                                               .map(this::convertToGameToken)
                                               .collect(Collectors.toList());
            }

            private Token convertToGameToken(GameTokenEntity gameTokenEntity) {
                return new Token() {
                    @Override
                    public Optional<UUID> getOwnerId() {
                        return Optional.ofNullable(UUID.fromString(gameTokenEntity.ownerId));
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
                        // Implémentez la logique pour obtenir les mouvements autorisés
                        return new HashSet<>();
                    }

                    @Override
                    public void moveTo(@NotNull CellPosition position) throws InvalidPositionException {
                        // Implémentez la logique pour déplacer le token
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
        gameEntity.tokens = game.getBoard().values().stream()
                                .map(this::convertToGameTokenEntity)
                                .collect(Collectors.toList());
        gameEntity.remainingTokens = game.getRemainingTokens().stream()
                                         .map(this::convertToGameTokenEntity)
                                         .collect(Collectors.toList());
        gameEntity.removedTokens = game.getRemovedTokens().stream()
                                       .map(this::convertToGameTokenEntity)
                                       .collect(Collectors.toList());
        return gameEntity;
    }

    private GameTokenEntity convertToGameTokenEntity(Token token) {
        GameTokenEntity tokenEntity = new GameTokenEntity();
        tokenEntity.ownerId = token.getOwnerId().map(UUID::toString).orElse(null);
        tokenEntity.name = token.getName();
        if (token.getPosition() != null) {
            tokenEntity.x = token.getPosition().x();
            tokenEntity.y = token.getPosition().y();
        }
        return tokenEntity;
    }
}