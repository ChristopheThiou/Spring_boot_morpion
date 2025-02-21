package com.boardgame.morpion.Service;

import com.boardgame.morpion.Dao.GameDao;
import com.boardgame.morpion.Plugin.GamePlugin;
import fr.le_campus_numerique.square_games.engine.CellPosition;
import fr.le_campus_numerique.square_games.engine.Game;
import fr.le_campus_numerique.square_games.engine.InvalidPositionException;
import fr.le_campus_numerique.square_games.engine.Token;
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
            Token token = game.getRemainingTokens().stream()
                    .filter(t -> t.getOwnerId().orElse(null).equals(playerId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No token available for the player"));
            try {
                token.moveTo(position);
                gameDao.upsert(game); // Update the game state after the move
            } catch (InvalidPositionException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("Invalid move or player");
        }
    }
}