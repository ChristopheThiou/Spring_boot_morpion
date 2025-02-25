package com.boardgame.morpion.Controller;

import com.boardgame.morpion.Dto.GameDto;
import com.boardgame.morpion.Service.GameCatalog;
import com.boardgame.morpion.Service.UserService;
import fr.le_campus_numerique.square_games.engine.Game;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameCatalog gameCatalog;

    @Autowired
    private UserService userService;

    @PostMapping
    public Game createGame(@RequestBody GameDto request, @RequestHeader("X-UserId") String userId) {
        if (!userService.verifyUserId(userId)) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        String[] opponentIdsArray = request.getOpponentIds().split(",");
        if (opponentIdsArray.length != 1) {
            throw new IllegalArgumentException("opponentIds must contain exactly one player");
        }

        Set<UUID> playerIds = new HashSet<>();
        playerIds.add(UUID.fromString(userId));
        for (String opponentId : opponentIdsArray) {
            playerIds.add(UUID.fromString(opponentId));
        }

        if (playerIds.size() != 2) {
            throw new IllegalArgumentException("There must be exactly two distinct players");
        }

        return gameCatalog.createGame(request.getGameId(), request.getPlayerCount(), request.getBoardSize(), playerIds);
    }

    @GetMapping("/mygames")
    public List<Game> getGamesForPlayer(@RequestHeader("X-UserId") String userId) {
        if (!userService.verifyUserId(userId)) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        UUID playerId = UUID.fromString(userId);
        return gameCatalog.getGamesForPlayer(playerId);
    }

    @PutMapping("/{gameId}/move")
    public void playMove(@PathVariable UUID gameId, @RequestParam int x, @RequestParam int y, @RequestHeader("X-UserId") String userId) {
        if (!userService.verifyUserId(userId)) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        UUID playerId = UUID.fromString(userId);
        gameCatalog.playMove(gameId, playerId, x, y);
    }
}
