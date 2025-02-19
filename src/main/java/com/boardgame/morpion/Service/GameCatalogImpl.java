package com.boardgame.morpion.Service;

import com.boardgame.morpion.Plugin.GamePlugin;
import fr.le_campus_numerique.square_games.engine.CellPosition;
import fr.le_campus_numerique.square_games.engine.InvalidPositionException;
import fr.le_campus_numerique.square_games.engine.Token;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGame;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GameCatalogImpl implements GameCatalog {

    @Autowired
    private List<GamePlugin> gamePlugins;

    private Map<UUID, TicTacToeGame> games = new HashMap<>();

    @Override
    public Collection<String> getGameIdentifier() {
        return gamePlugins.stream()
                .map(plugin -> plugin.getName(Locale.getDefault()))
                .collect(Collectors.toList());
    }

    @Override
    public TicTacToeGame createGame(int playerCount, int boardSize, Set<UUID> playerIds) {
        GamePlugin plugin = gamePlugins.stream()
                .filter(p -> p.getName(Locale.getDefault()).equalsIgnoreCase("Tic Tac Toe"))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid game ID"));
        TicTacToeGame game = (TicTacToeGame) plugin.createGame(OptionalInt.of(playerCount), OptionalInt.of(boardSize));
        games.put(game.getId(), game);
        return game;
    }

    @Override
    public List<TicTacToeGame> getGamesForPlayer(UUID playerId) {
        List<TicTacToeGame> playerGames = new ArrayList<>();
        for (TicTacToeGame game : games.values()) {
            if (game.getPlayerIds().contains(playerId)) {
                playerGames.add(game);
            }
        }
        return playerGames;
    }

    @Override
    public void playMove(UUID gameId, UUID playerId, int x, int y) {
        TicTacToeGame game = games.get(gameId);
        if (game != null && game.getCurrentPlayerId().equals(playerId)) {
            CellPosition position = new CellPosition(x, y);
            Token token = game.getRemainingTokens().stream()
                    .filter(t -> t.getOwnerId().orElse(null).equals(playerId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No token available for the player"));
            try {
                token.moveTo(position);
            } catch (InvalidPositionException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new IllegalArgumentException("Invalid move or player");
        }
    }
}