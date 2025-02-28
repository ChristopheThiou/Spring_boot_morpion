package com.boardgame.morpion.dao;

import com.boardgame.morpion.entity.GameEntity;
import com.boardgame.morpion.entity.GameTokenEntity;
import com.boardgame.morpion.repository.GameEntityRepository;
import fr.le_campus_numerique.square_games.engine.*;
import jakarta.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;



@Repository
@Primary
public class JpaGameDao implements GameDao {

    private final GameEntityRepository gameEntityRepository;

    public JpaGameDao(GameEntityRepository gameEntityRepository) {
        this.gameEntityRepository = gameEntityRepository;
    }

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

            public UUID getId() {
                return UUID.fromString(gameEntity.getId());
            }

            public String getFactoryId() {
                return gameEntity.getFactoryId();
            }

            public Set<UUID> getPlayerIds() {
                return Arrays.stream(gameEntity.getPlayerIds().split(","))
                             .map(UUID::fromString)
                             .collect(Collectors.toSet());
            }

            public int getBoardSize() {
                return gameEntity.getBoardSize();
            }

            public Map<CellPosition, Token> getBoard() {
                return gameEntity.getTokens().stream()
                                        .filter(token -> token.getX() != null && token.getY() != null)
                                        .collect(Collectors.toMap(
                                            token -> new CellPosition(token.getX(), token.getY()),
                                            this::convertToGameToken
                                        ));
            }

            public Collection<Token> getRemainingTokens() {
                return gameEntity.getTokens().stream()
                                        .filter(token -> token.getX() == null && token.getY() == null)
                                        .map(this::convertToGameToken)
                                        .collect(Collectors.toList());
            }

            public Collection<Token> getRemovedTokens() {
                return Collections.emptyList();
            }

            public @NotNull GameStatus getStatus() {
                return GameStatus.valueOf(gameEntity.getStatus());
            }

            public UUID getCurrentPlayerId() {
                return gameEntity.getCurrentPlayerId() != null ? UUID.fromString(gameEntity.getCurrentPlayerId()) : null;
            }

            private Token convertToGameToken(GameTokenEntity gameTokenEntity) {
                return new Token() {

                    public Optional<UUID> getOwnerId() {
                        return Optional.ofNullable(gameTokenEntity.getOwnerId() != null ? UUID.fromString(gameTokenEntity.getOwnerId()) : null);
                    }

                    public String getName() {
                        return gameTokenEntity.getName();
                    }

                    public CellPosition getPosition() {
                        return gameTokenEntity.getX() != null && gameTokenEntity.getY() != null
                                ? new CellPosition(gameTokenEntity.getX(), gameTokenEntity.getY())
                                : null;
                    }

                    public Set<CellPosition> getAllowedMoves() {
                        Set<CellPosition> allowedMoves = new HashSet<>();
                        int boardSize = gameEntity.getBoardSize();
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

                    public void moveTo(@NotNull CellPosition position) throws InvalidPositionException {
                        Map<CellPosition, Token> board = getBoard();
                        if (board.containsKey(position)) {
                            throw new InvalidPositionException("Position is already occupied");
                        }
                        gameTokenEntity.setX(position.x());
                        gameTokenEntity.setY(position.y());
                    }
                };
            }
        };
    }

    private GameEntity convertToGameEntity(Game game) {
        GameEntity gameEntity = new GameEntity();
        gameEntity.setId(game.getId().toString());
        gameEntity.setFactoryId(game.getFactoryId());
        gameEntity.setBoardSize(game.getBoardSize());
        gameEntity.setPlayerIds(game.getPlayerIds().stream()
                                   .map(UUID::toString)
                                   .collect(Collectors.joining(",")));
        gameEntity.setStatus(game.getStatus().name());
        gameEntity.setCurrentPlayerId(game.getCurrentPlayerId() != null ? game.getCurrentPlayerId().toString() : null);
        gameEntity.setTokens(new ArrayList<>());
        gameEntity.getTokens().addAll(game.getBoard().values().stream()
                                .map(token -> convertToGameTokenEntity(token, gameEntity))
                                .toList());
        gameEntity.getTokens().addAll(game.getRemainingTokens().stream()
                                .map(token -> convertToGameTokenEntity(token, gameEntity))
                                .toList());
        return gameEntity;
    }

    private GameTokenEntity convertToGameTokenEntity(Token token, GameEntity gameEntity) {
        GameTokenEntity tokenEntity = new GameTokenEntity();
        tokenEntity.setOwnerId(token.getOwnerId().map(UUID::toString).orElse(null));
        tokenEntity.setName(token.getName());
        tokenEntity.setGame(gameEntity);
        if (token.getPosition() != null) {
            tokenEntity.setX(token.getPosition().x());
            tokenEntity.setY(token.getPosition().y());
        }
        return tokenEntity;
    }
}