package com.boardgame.morpion.Service;

import com.boardgame.morpion.Dao.GameDao;
import com.boardgame.morpion.Exception.InvalidMoveException;
import com.boardgame.morpion.Plugin.GamePlugin;
import fr.le_campus_numerique.square_games.engine.*;

import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GameCatalogImpl implements GameCatalog {

    @Autowired
    private List<GamePlugin> gamePlugins;

    @Autowired
    private GameDao gameDao;

    @Override
    public Collection<String> getGameIdentifier() {
        return gamePlugins.stream()
                .map(plugin -> plugin.getName(Locale.getDefault()))
                .collect(Collectors.toList());
    }

    @Override
    public Game createGame(String gameId, int playerCount, int boardSize, Set<UUID> playerIds) {
        GamePlugin plugin = gamePlugins.stream()
                .filter(p -> p.getName(Locale.getDefault()).equalsIgnoreCase(gameId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid game ID"));
        UUID userId = playerIds.iterator().next();
        playerIds.remove(userId);
        UUID opponentIds = playerIds.iterator().next();
        Game game = plugin.createGame(OptionalInt.of(playerCount), OptionalInt.of(boardSize), OptionalInt.empty(), OptionalInt.empty(), userId, opponentIds);
        gameDao.upsert(game);
        return game;
    }

    @Override
    public List<Game> getGamesForPlayer(UUID playerId) {
        return gameDao.findAll()
                .filter(game -> game.getPlayerIds().contains(playerId))
                .collect(Collectors.toList());
    }

    @Override
    public void playMove(UUID gameId, UUID playerId, int x, int y) {
        Game game = gameDao.findById(gameId.toString())
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));
        if (game.getCurrentPlayerId().equals(playerId)) {
            CellPosition position = new CellPosition(x, y);
            // Vérifiez si la position est déjà occupée
            if (game.getBoard().containsKey(position)) {
                throw new InvalidMoveException("Position is already occupied");
            }
            Token token = game.getRemainingTokens().stream()
                    .filter(t -> t.getOwnerId().orElse(null).equals(playerId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No token available for the player"));
            try {
                token.moveTo(position);
                UUID newCurrentPlayerId = switchCurrentPlayer(game.getPlayerIds(), game.getCurrentPlayerId());
                Game updatedGame = createUpdatedGame(game, newCurrentPlayerId);
                gameDao.upsert(updatedGame);  // Assurez-vous que l'objet mis à jour est sauvegardé
            } catch (InvalidPositionException e) {
                throw new RuntimeException("Erreur lors du déplacement du jeton: " + e.getMessage(), e);
            }
        } else {
            throw new IllegalArgumentException("Coup invalide ou joueur non autorisé.");
        }
    }

    private UUID switchCurrentPlayer(Set<UUID> playerIds, UUID currentPlayerId) {
        UUID[] playersArray = playerIds.toArray(new UUID[0]);
        return playersArray[0].equals(currentPlayerId) ? playersArray[1] : playersArray[0];
    }

    private Game createUpdatedGame(Game game, UUID newCurrentPlayerId) {
        return new Game() {
            @Override
            public UUID getId() {
                return game.getId();
            }

            @Override
            public String getFactoryId() {
                return game.getFactoryId();
            }

            @Override
            public Set<UUID> getPlayerIds() {
                return game.getPlayerIds();
            }

            @Override
            public GameStatus getStatus() {
                return game.getStatus();
            }

            @Override
            public UUID getCurrentPlayerId() {
                return newCurrentPlayerId;
            }

            @Override
            public int getBoardSize() {
                return game.getBoardSize();
            }

            @Override
            public Map<CellPosition, Token> getBoard() {
                return game.getBoard();
            }

            @Override
            public Collection<Token> getRemainingTokens() {
                return game.getRemainingTokens();
            }

            @Override
            public Collection<Token> getRemovedTokens() {
                return game.getRemovedTokens();
            }
        };
    }
}