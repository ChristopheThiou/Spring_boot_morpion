package com.boardgame.morpion.Controller;

import com.boardgame.morpion.Service.GameCatalog;
import com.boardgame.morpion.Service.UserService;
import fr.le_campus_numerique.square_games.engine.Game;
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

    @PostMapping
    public Game createGame(@RequestBody CreateGameRequest request, @RequestHeader("X-UserId") String userId) {
        if (!userService.verifyUserId(userId)) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        Set<UUID> playerIds = request.getOpponentIds().stream()
                                     .map(UUID::fromString)
                                     .collect(Collectors.toSet());
        playerIds.add(UUID.fromString(userId));
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

class CreateGameRequest {
    private String gameId;
    private int playerCount;
    private int boardSize;
    private Set<String> opponentIds;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public Set<String> getOpponentIds() {
        return opponentIds;
    }

    public void setOpponentIds(Set<String> opponentIds) {
        this.opponentIds = opponentIds;
    }
}