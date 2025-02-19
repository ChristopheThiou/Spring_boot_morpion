package com.boardgame.morpion.Controller;

import com.boardgame.morpion.Service.GameCatalog;
import com.boardgame.morpion.Service.UserService;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGame;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameCatalog gameCatalog;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public TicTacToeGame createGame(@RequestParam int playerCount, @RequestParam int boardSize, @RequestHeader("X-UserId") String userId, @RequestParam(required = false) Set<String> opponentIds) {
        if (!userService.verifyUserId(userId)) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        Set<UUID> playerIds = opponentIds != null && !opponentIds.isEmpty() ? opponentIds.stream().map(UUID::fromString).collect(Collectors.toSet()) : Set.of(UUID.randomUUID(), UUID.randomUUID());
        playerIds.add(UUID.fromString(userId));
        return gameCatalog.createGame(playerCount, boardSize, playerIds);
    }

    @GetMapping("/mygames")
    public List<TicTacToeGame> getGamesForPlayer(@RequestHeader("X-UserId") String userId) {
        if (!userService.verifyUserId(userId)) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        UUID playerId = UUID.fromString(userId);
        return gameCatalog.getGamesForPlayer(playerId);
    }

    @PostMapping("/play")
    public void playMove(@RequestParam UUID gameId, @RequestParam int x, @RequestParam int y, @RequestHeader("X-UserId") String userId) {
        if (!userService.verifyUserId(userId)) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        UUID playerId = UUID.fromString(userId);
        gameCatalog.playMove(gameId, playerId, x, y);
    }
}